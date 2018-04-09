package com.kendy.spider;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.SocketConfig;
import org.apache.http.config.SocketConfig.Builder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP服务
 */
public class HttpService {

    private Logger log = LoggerFactory.getLogger(HttpService.class);

    private CloseableHttpClient httpClient;

    /**
     * 最大连接数
     */
    public final static int MAX_TOTAL_CONNECTIONS = 500;

    /**
     * 每个路由最大连接数
     */
    public final static int MAX_ROUTE_CONNECTIONS = 100;
    /**
     * 连接超时时间
     */
    public final static int CONNECT_TIMEOUT = 2000;

    @PostConstruct
    protected void init() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(MAX_TOTAL_CONNECTIONS);
        cm.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);

        // try {
        // URL url = new URL(netInfoUrl);
        // cm.setMaxPerRoute(new HttpRoute(new HttpHost(url.getHost())), 100);
        // } catch (MalformedURLException e) {
        // }

        // httpClient.setHttpRequestRetryHandler(new
        // DefaultHttpRequestRetryHandler(0, false));
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(2000).setConnectTimeout(500)
                .setSocketTimeout(1000).build();

        Builder builder = SocketConfig.custom();
        builder.setSoKeepAlive(true);
        cm.setDefaultSocketConfig(builder.build());

        httpClient = HttpClients.custom().setConnectionManager(cm).setDefaultRequestConfig(requestConfig).build();
    }

    /**
     * 调用httpClient自行编码进行get或post之后请关闭连接,详见<br>
     * https://hc.apache.org/httpcomponents-client-ga/quickstart.html<br>
     * <br>
     * try { <br>
     * System.out.println(response2.getStatusLine()); <br>
     * HttpEntity entity2 = response2.getEntity(); <br>
     * // do something useful with the response body <br>
     * // and ensure it is fully consumed <br>
     * EntityUtils.consume(entity2); } <br>
     * finally {<br>
     * response2.close(); <br>
     * }
     * 
     * @return
     */
    protected CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * @param url
     * @return
     */
    public HttpResult get(String url) throws URISyntaxException, IOException {
        return get(url, null, null, null);
    }

    /**
     * @param url
     * @param headers
     * @return
     */
    public HttpResult get(String url, Map<String, String> headers) throws URISyntaxException, IOException {
        return get(url, headers, null, null);
    }

    /**
     * @param url
     * @param headers
     * @param context
     * @return
     */
    public HttpResult get(String url, Map<String, String> headers, HttpContext context, RequestConfig config)
            throws URISyntaxException, IOException {
        return request("get", url, null, null, headers, context, config);
    }

    /**
     * @param url
     * @param body
     * @return
     */
    public HttpResult post(String url, String body) throws URISyntaxException, IOException {
        return post(url, body, null, null, null);
    }

    /**
     * @param url
     * @param body
     * @param headers
     * @return
     */
    public HttpResult post(String url, String body, Map<String, String> headers)
            throws URISyntaxException, IOException {
        return post(url, body, headers, null, null);
    }

    /**
     * @param url
     * @param body
     * @param headers
     * @param context
     * @param config
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    public HttpResult post(String url, String body, Map<String, String> headers, HttpContext context,
            RequestConfig config) throws URISyntaxException, IOException {
        return request("post", url, body, null, headers, context, config);
    }

    public HttpResult postWithMap(String url, Map<String, String> params) throws URISyntaxException, IOException {
        return requestWithMap("post", url, params, null, null, null, null);
    }

    /**
     * @param url
     * @param body
     * @param charSet
     * @param headers
     * @param context
     * @param config
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    public HttpResult post(String url, String body, Charset charSet, Map<String, String> headers, HttpContext context,
            RequestConfig config) throws URISyntaxException, IOException {
        return request("post", url, body, charSet, headers, context, config);
    }

    /**
     * @param method
     * @param url
     * @param body
     * @param headers
     * @param context
     * @return
     * @throws URISyntaxException
     */
    private HttpResult request(String method, String url, String body, Charset charSet, Map<String, String> headers,
            HttpContext context, RequestConfig config) throws URISyntaxException, IOException {
        CloseableHttpResponse response = null;
        HttpResult result = new HttpResult();
        try {
            long t = System.currentTimeMillis();
            HttpRequestBase request = null;

            if (method.equalsIgnoreCase(HttpPost.METHOD_NAME)) {
                HttpPost post = new HttpPost();
                log.debug("body: " + body);
                if (charSet == null) {
                    charSet = Charset.forName("utf-8");
                }

                post.setEntity(new StringEntity(body, charSet));
                request = post;
            } else {
                request = new HttpGet();
            }

            request.setURI(new URI(url));

            if (headers != null && headers.size() > 0) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    request.addHeader(entry.getKey(), entry.getValue());
                }
            }

            if (config != null) {
                request.setConfig(config);
            }

            log.debug("url: " + url);

            response = httpClient.execute(request, context);

            if (response.getStatusLine() != null) {
                result.setStatusCode(response.getStatusLine().getStatusCode());
            }
            if (response.getEntity() != null) {
                result.setContent(IOUtils.toString(response.getEntity().getContent(), "utf-8"));
            }

            log.debug("cost=" + (System.currentTimeMillis() - t) + "ms, code=" + result.getStatusCode() + ", response="
                    + result.getContent());
            EntityUtils.consume(response.getEntity());
        } catch (IOException e) {
            throw e;
        } catch (URISyntaxException e) {
            throw e;
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
            }
        }
        return result;
    }

    private HttpResult requestWithMap(String method, String url, Map<String, String> params, String charSet,
            Map<String, String> headers, HttpContext context, RequestConfig config)
            throws URISyntaxException, IOException {
        CloseableHttpResponse response = null;
        HttpResult result = new HttpResult();
        try {
            long t = System.currentTimeMillis();
            HttpRequestBase request = null;

            if (method.equalsIgnoreCase(HttpPost.METHOD_NAME)) {
                HttpPost post = new HttpPost();
                // log.debug("body: " + body);
                if (charSet == null) {
                    charSet = HTTP.UTF_8;
                }

                if (params != null) {
                    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                    Set key = params.keySet();
                    Iterator keyIte = key.iterator();
                    while (keyIte.hasNext()) {
                        Object paramName = keyIte.next();
                        Object paramValue = params.get(paramName);
                        if (paramName instanceof String && paramValue instanceof String) {
                            nvps.add(new BasicNameValuePair((String) paramName, (String) paramValue));
                        }
                    }
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nvps);
                    System.out.println("[nvps.toString()]" + nvps.toString());/////
                    entity.setContentEncoding(charSet);
                    post.setEntity(entity);
                }

                // post.setEntity(new StringEntity(body, charSet));
                request = post;
            } else {
                request = new HttpGet();
            }

            request.setURI(new URI(url));

            if (headers != null && headers.size() > 0) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    request.addHeader(entry.getKey(), entry.getValue());
                }
            }

            if (config != null) {
                request.setConfig(config);
            }

            log.debug("url: " + url);

            response = httpClient.execute(request, context);

            if (response.getStatusLine() != null) {
                result.setStatusCode(response.getStatusLine().getStatusCode());
            }
            if (response.getEntity() != null) {
                result.setContent(IOUtils.toString(response.getEntity().getContent(), "utf-8"));
            }

            log.debug("cost=" + (System.currentTimeMillis() - t) + "ms, code=" + result.getStatusCode() + ", response="
                    + result.getContent());
            EntityUtils.consume(response.getEntity());
        } catch (IOException e) {
            throw e;
        } catch (URISyntaxException e) {
            throw e;
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
            }
        }
        return result;
    }

    public class HttpResult {

        public boolean isOK() {
            return statusCode == HttpStatus.SC_OK;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        int statusCode;
        String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

    }
}