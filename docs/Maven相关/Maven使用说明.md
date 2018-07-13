# Maven使用说明


## Maven占位符说明
因Spring默认占位符和Maven资源文件默认占位符相同,都为${},导致在资源文件拷贝时maven可能会错误的替换

本项目全局修改maven资源文件占位符为#{}

使用占位符时,请明确此占位符是需要由Maven在打包时进行替换还是让Spring在运行时进行替换,请注意区分


## 打包
* `mvn clean package `
