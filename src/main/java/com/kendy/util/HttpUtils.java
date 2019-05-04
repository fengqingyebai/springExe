/**
 *
 */
package com.kendy.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
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
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Http连接工具类
 *
 * @author ChenXing www.10155.com
 * @version 2018年4月27日
 */
public class HttpUtils {

  private Logger log = LoggerFactory.getLogger(HttpUtils.class);

  private CloseableHttpClient httpClient;

  /**
   * 最大连接数
   */
  public final static int MAX_TOTAL_CONNECTIONS = 500;

  /**
   * 每个路由最大连接数
   */
  public final static int MAX_ROUTE_CONNECTIONS = 20;
  /**
   * 连接超时时间
   */
  public final static int CONNECT_TIMEOUT = 2000;

  private PoolingHttpClientConnectionManager cm;

  private RequestConfig defaultRequestConfig;

  private static final HttpUtils instance;

  static {
    instance = new HttpUtils();
    instance.init();
  }

  protected void init() {
    cm = new PoolingHttpClientConnectionManager();
    cm.setMaxTotal(MAX_TOTAL_CONNECTIONS);
    cm.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);

    defaultRequestConfig = RequestConfig.custom().setConnectionRequestTimeout(CONNECT_TIMEOUT)
        .setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(CONNECT_TIMEOUT).build();

    Builder builder = SocketConfig.custom();
    builder.setSoKeepAlive(true);
    cm.setDefaultSocketConfig(builder.build());

    httpClient = HttpClients.custom().setConnectionManager(cm)
        .setDefaultRequestConfig(defaultRequestConfig).build();
  }

  /**
   * 获取实例
   *
   * @param route 需请求的URL
   * @param max 最大连接数
   */
  public static HttpUtils getInstance(String route, int max) {
    try {
      URL url = new URL(route);
      HttpHost host = new HttpHost(url.getHost(), url.getPort(), url.getProtocol());
      instance.cm.setMaxPerRoute(new HttpRoute(host), max);
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException("url格式错误: " + route);
    }
    return instance;
  }

  public static HttpUtils getInstance() {
    return instance;
  }

  /**
   * @param charset 字符集
   */
  public HttpResult get(String url, Charset charset) throws URISyntaxException, IOException {
    return get(url, charset, -1);
  }

  /**
   * @param timeout 超时时间(毫秒)
   */
  public HttpResult get(String url, Charset charSet, int timeout)
      throws URISyntaxException, IOException {
    return get(url, charSet, null, timeout);
  }

  /**
   * @param charset 字符集
   * @param headers Http头
   */
  public HttpResult get(String url, Charset charset, Map<String, String> headers)
      throws URISyntaxException, IOException {
    return get(url, charset, headers, -1);
  }

  /**
   * @param charset 字符集
   * @param headers Http头
   * @param timeout 超时时间(毫秒)
   */
  public HttpResult get(String url, Charset charset, Map<String, String> headers, int timeout)
      throws URISyntaxException, IOException {
    RequestConfig requestConfig = null;
    if (timeout > -1) {
      requestConfig = RequestConfig.copy(defaultRequestConfig).setConnectionRequestTimeout(timeout)
          .setConnectTimeout(timeout).setSocketTimeout(timeout).build();
    }

    return get(url, charset, headers, requestConfig);
  }

  /**
   * @param charset 字符集
   * @param headers Http头
   */
  public HttpResult get(String url, Charset charset, Map<String, String> headers,
      RequestConfig requestConfig) throws URISyntaxException, IOException {
    return request("get", url, charset, null, headers, null, requestConfig);
  }

  /**
   * 表单方式提交<br/> Content-Type=application/x-www-form-urlencoded
   *
   * @param charset 字符集
   * @param form 表单参数
   */
  public HttpResult post(String url, Charset charset, Map<String, String> form)
      throws URISyntaxException, IOException {
    return post(url, charset, form, -1);
  }

  /**
   * 表单方式提交<br/> Content-Type=application/x-www-form-urlencoded
   *
   * @param charset 字符集
   * @param form 表单参数
   * @param timeout 超时时间(毫秒)
   */
  public HttpResult post(String url, Charset charset, Map<String, String> form, int timeout)
      throws URISyntaxException, IOException {
    if (charset == null) {
      charset = Charset.forName("utf-8");
    }
    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
    form.forEach((key, value) -> {
      nvps.add(new BasicNameValuePair(key, value));
    });
    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nvps);
    entity.setContentEncoding(charset.name());

    RequestConfig requestConfig = null;
    if (timeout > -1) {
      requestConfig = RequestConfig.copy(defaultRequestConfig).setConnectionRequestTimeout(timeout)
          .setConnectTimeout(timeout).setSocketTimeout(timeout).build();
    }

    return post(url, charset, entity, null, requestConfig);
  }

  public HttpResult formPost(String url, Charset charset, Map<String, String> form, Map<String, String> headers)
      throws URISyntaxException, IOException {
    if (charset == null) {
      charset = Charset.forName("utf-8");
    }
    List<NameValuePair> nvps = new ArrayList<NameValuePair>();
    form.forEach((key, value) -> {
      nvps.add(new BasicNameValuePair(key, value));
    });
    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nvps);
    entity.setContentEncoding(charset.name());

    RequestConfig requestConfig = null;

    return post(url, charset, entity, headers, requestConfig);
  }

  /**
   * 文本方式提交<br/> Content-Type=text/plain
   *
   * @param charset 字符集
   * @param body 文本body
   */
  public HttpResult post(String url, Charset charset, String body)
      throws URISyntaxException, IOException {
    return post(url, charset, body, null, -1);
  }

  /**
   * 文本方式提交<br/> Content-Type=text/plain
   *
   * @param charset 字符集
   * @param body 文本body
   * @param timeout 超时时间(毫秒)
   */
  public HttpResult post(String url, Charset charset, String body, int timeout)
      throws URISyntaxException, IOException {
    return post(url, charset, body, null, timeout);
  }

  /**
   * @param charset 字符集
   * @param body entity
   * @param headers Http头
   */
  public HttpResult post(String url, Charset charset, String body, Map<String, String> headers)
      throws URISyntaxException, IOException {
    return post(url, charset, body, headers, -1);
  }

  /**
   * 文本方式提交<br/> Content-Type=text/plain
   *
   * @param charset 字符集
   * @param body 文本body
   * @param headers Http头
   * @param timeout 超时时间(毫秒)
   */
  public HttpResult post(String url, Charset charset, String body, Map<String, String> headers,
      int timeout) throws URISyntaxException, IOException {
    RequestConfig requestConfig = null;
    if (timeout > -1) {
      requestConfig = RequestConfig.copy(defaultRequestConfig).setConnectionRequestTimeout(timeout)
          .setConnectTimeout(timeout).setSocketTimeout(timeout).build();
    }

    if (charset == null) {
      charset = Charset.forName("utf-8");
    }

    StringEntity entity = new StringEntity(body, charset);
    if (body != null) {
      entity = new StringEntity(body, charset);
    }

    return post(url, charset, entity, headers, requestConfig);
  }


  /**
   * Content-Type取决于HttpEntity类型
   *
   * @param charset 字符集
   * @param body entity
   * @param headers Http头
   */
  public HttpResult post(String url, Charset charset, HttpEntity body, Map<String, String> headers,
      RequestConfig requestConfig) throws URISyntaxException, IOException {
    return request("post", url, charset, body, headers, null, requestConfig);
  }


  /**
   * 发送Request
   */
  private HttpResult request(String method, String url, Charset charset, HttpEntity body,
      Map<String, String> headers, HttpContext context, RequestConfig config)
      throws URISyntaxException, IOException {
    CloseableHttpResponse response = null;
    HttpResult result = new HttpResult();
    try {
      long t = System.currentTimeMillis();
      HttpRequestBase request = null;

      log.debug("url: " + url);

      if (charset == null) {
        charset = Charset.forName("utf-8");
      }

      if (StringUtils.equalsIgnoreCase(method, HttpPost.METHOD_NAME)) {
        HttpPost post = new HttpPost();
        if (log.isDebugEnabled() && body != null) {
          log.debug("body: " + body + ", " + EntityUtils.toString(body));
        }

        post.setEntity(body);
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

      response = httpClient.execute(request, context);

      if (response.getStatusLine() != null) {
        result.setStatusCode(response.getStatusLine().getStatusCode());
      }
      if (response.getEntity() != null) {
        result.setContent(IOUtils.toString(response.getEntity().getContent(), charset));
      }
      if (log.isDebugEnabled()) {
        log.debug("response: cost=" + (System.currentTimeMillis() - t) + "ms, code="
            + result.getStatusCode() + ", " + result.getContent());
      }
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
