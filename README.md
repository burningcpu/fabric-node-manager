# fabric节点管理服务
## 一、简介
fabric-node-manager使用可配置管理多个front,也提供数据查询的restful接口

## 二、前提条件

| name | age |
|:----:|:----:|
| Java | JDK8或以上版本 |
| fabric-front-gateway | |

## 三、安装
### 3.1. 拉取代码

```
git clone https://github.com/burningcpu/fabric-node-manager.git
```

进入目录：

```
cd fabric-node-manager
```

### 3.2. 编译代码

方式一：如果服务器已安装Gradle，且版本为Gradle-4.10或以上

```shell
gradle build -x test
```

方式二：如果服务器未安装Gradle，或者版本不是Gradle-4.10或以上，使用gradlew编译

```shell
chmod +x ./gradlew && ./gradlew build -x test
```

构建完成后，会在根目录fabric-node-manager下生成已编译的代码目录dist。

### 3.3. 修改配置
* 修改配置`vi application.yml`（根据实际情况修改）：

```
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/fabricmanager?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull
    username: "root"
    password: "123456"
```


### 3.4. 服务启停

返回到dist目录执行：
```shell
启动: bash start.sh
停止: bash stop.sh
检查: bash status.sh
```
**备注**：服务进程起来后，需通过日志确认是否正常启动，出现以下内容表示正常；如果服务出现异常，确认修改配置后，重启提示服务进程在运行，则先执行stop.sh，再执行start.sh。

```
...
	Application() - main run success...
```

### 3.5. 访问swagger页面

```
http://{deployIP}:{frontPort}/Fabric-Node-Manager/swagger-ui.html
示例：http://localhost:8080/Fabric-Node-Manager/swagger-ui.html
```

- 部署服务器IP和服务端口需对应修改，网络策略需开通


### 3.6. 查看日志

在dist目录查看：

```
前置服务日志：tail -f log/fabric-manager.log
```
