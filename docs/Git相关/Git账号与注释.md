# GIT账号


## 本地配置

Windows
修改本地

	%HOMEPATH%\.gitconfig
	
文件,将email改为和申请邮箱一致,如邮箱有变动也请一并修改,否则gitlab中显示代码提交用户会出错

文件采用utf-8编码,否则中文用户名会出错

````
[user]
	name = 林泽涛
	email = chenxing21@chinaunicom.cn
[credential]
	helper = manager
````
# GIT注释填写规范

```
    <type>(<scope>): <subject>
    <BLANK LINE>
    <body>
    <BLANK LINE>
    <footer>
````

## Header

Header部分只有一行，包括三个字段：type（必需）、scope（可选）和subject（必需）。

### type

用于说明 commit 的类别，只允许使用下面7个标识。

- feat：新功能（feature）
- fix：修补bug
- docs：文档（documentation）
- style： 格式（不影响代码运行的变动）
- refactor：重构（即不是新增功能，也不是修改bug的代码变动）
- test：增加测试
- chore：构建过程或辅助工具的变动

### scope

scope用于说明 commit 影响的范围，比如数据层、控制层、视图层等等，视项目不同而不同。

例如在Angular，可以是$location, $browser, $compile, $rootScope, ngHref, ngClick, ngView等。

如果你的修改影响了不止一个scope，你可以使用*代替。

### subject

subject是 commit 目的的简短描述，不超过50个字符。

### 其他注意事项：

以动词开头，使用第一人称现在时，比如change，而不是changed或changes
第一个字母小写
结尾不加句号（.）

## Body

Body 部分是对本次 commit 的详细描述，可以分成多行。下面是一个范例。

More detailed explanatory text, if necessary.  Wrap it to 
about 72 characters or so. 

Further paragraphs come after blank lines.

- Bullet points are okay, too
- Use a hanging indent
有两个注意点:

使用第一人称现在时，比如使用change而不是changed或changes。
永远别忘了第2行是空行
应该说明代码变动的动机，以及与以前行为的对比。
Footer

Footer 部分只用于以下两种情况：

不兼容变动

如果当前代码与上一个版本不兼容，则 Footer 部分以BREAKING CHANGE开头，后面是对变动的描述、以及变动理由和迁移方法。