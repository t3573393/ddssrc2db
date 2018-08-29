# ddssrc2db

ddssrc2db是一个开源的针对AS400中的DDS文件解析 ，保存和查询的工具。方便AS400的开发人员进行物理文件和逻辑文件的查找和显示。

## 所使用技术

- Spring Boot
- Spring MVC   
- Bootstrap Admin  

## 配置和运行

### 数据库配置

需要修改src/main/resources/application.properties中的数据库配置

```properties
spring.datasource.url=jdbc:oracle:thin:@<yourdbconfig>
spring.datasource.driver-class-name=oracle.jdbc.driver.OracleDriver
spring.datasource.username=<yourdbuser>
spring.datasource.password=<yourdbpassword>
```

### 启动

由于采用spring boot的打包方式，只需要直接运行对应的jar，则可以启动。  

```shell
java -jar **.jar
```

如果需要解析对应文件夹下的DDS文件，则可以使用命令进行调用 。   
```shell
java -jar **.jar resolve <your ddssrc folder path>
```

