Tutorial 12 disconf-web 为界面 开放的 Http API
=======

## 前言

- 目标：让开发者具有自定义定制web控制台界面的能力

以下接口均需要权限控制

## app接口

### /api/app/list

- 描述：返回所有app的列表
- 请求类型: GET
- 参数示例：N/A
- 返回示例:

    {"message":{},"sessionId":"3e560be1-9000-4a5c-8371-35312040d8ac","success":"true","page":{"result":[
{"id":2,"name":"disconf_demo"}],"order":null,"orderBy":null,"totalCount":1,"pageNo":null,"pageSize":null
,"footResult":null}}

### /api/app

- 描述：生成一个app
- 请求类型: POST
- 参数

    |  |name   |desc   |是否必要|
    |---|-------|-------|----|
    |1  |app  |app的名字 |是|
    |2  |desc |描述  |是|
    |3  |emails|关联的邮箱列表，逗号分隔|否|
    
- 返回示例:

    {"message":{},"sessionId":"31617346-2020-4016-bc0d-f7a62d91945b","success":"true","result":"创建成功"}
    
## auth接口

### /api/account/session

- 描述：获取当前会话信息
- 请求类型: GET
- 参数示例：N/A
- 返回示例:
    
    {"message":{},"sessionId":"9d466ef4-1782-451a-8a4c-e2b99601dcba","success":"true","result":{"visitor"
:{"id":6,"name":"admin","role":null}}}

### /api/account/signin

- 描述：登录
- 请求类型: POST
- 参数

    |#|name   |desc   |是否必要|
    |---|-------|-------|----|
    |1|name  |用户名 |是|
    |2|password |密码  |是|
    |3|remember|是否记住自己|是|
    
- 返回示例:
    
    失败示例：
    
    {"message":{"field":{"password":"密码不正确"}},"sessionId":"29efac2d-fec1-40c7-b0d2-8433fb8a8c2c","success"
:"false","status":2000}

    或成功示例
    
    {"message":{},"sessionId":"53e68882-bf9a-47ab-a1f6-ba347906c2a5","success":"true","result":{"visitor"
:{"id":6,"name":"admin","role":null}}}

### /api/account/signout

- 描述：退出
- 请求类型: GET
- 参数示例：N/A
- 返回示例:

    {"message":{},"sessionId":"e6c3134d-ba55-4e34-837d-46d08604e2b1","success":"true","result":{"ok":"ok"
}}

## env接口
### /api/env/list

- 描述：返回所有环境的列表
- 请求类型: GET
- 参数示例：N/A
- 返回示例:

    {"message":{},"sessionId":"826141a9-3255-4beb-88c8-018e40981f6c","success":"true","page":{"result":[
{"id":1,"name":"rd"},{"id":2,"name":"qa"},{"id":3,"name":"local"},{"id":4,"name":"online"}],"order":null
,"orderBy":null,"totalCount":4,"pageNo":null,"pageSize":null,"footResult":null}}

## config接口

### /api/web/config/item

- 描述：创建配置项
- 请求类型: POST
- 参数

    |#|name   |desc   |是否必要|
    |---|-------|-------|----|
    |1|key  |配置项key |是|
    |2|value |配置项value  |是|
    |3|appId |app  |是|
    |4|version |版本  |是|
    |5|envId |环境  |是|
    
- 返回示例:

    {"message":{},"sessionId":"dc7b3355-2763-4122-bb69-96d2eb282027","success":"true","result":"创建成功"}

### /api/web/config/file

- 描述：生成配置, 采用直接上传文件方式
- 请求类型: POST
- 参数

    |#|name   |desc   |是否必要|
    |---|-------|-------|----|
    |1|myfilerar  |配置文件 |是|
    |2|appId |app  |是|
    |3|version |版本  |是|
    |4|envId |环境  |是|
    
- 返回示例:

    {"message":{},"sessionId":"b6a75894-a94b-4075-a4c7-05ed0be6b016","success":"true","result":"创建成功"}

### /api/web/config/filetext

- 描述：生成配置, 采用直接上传文本方式
- 请求类型: POST
- 参数

    |#|name   |desc   |是否必要|
    |---|-------|-------|----|
    |1|fileName  |文件名 |是|
    |2|fileContent  |文件内容 |是|
    |3|appId |app  |是|
    |4|version |版本  |是|
    |5|envId |环境  |是|
    
- 返回示例:

    {"message":{},"sessionId":"b6a75894-a94b-4075-a4c7-05ed0be6b016","success":"true","result":"创建成功"}

### /api/web/config/versionlist

- 描述：根据app, env 获取所有的 版本列表
- 请求类型: GET
- 参数

    |#|name   |desc   |是否必要|
    |---|-------|-------|----|
    |1|appId |app  |是|
    |2|envId |环境  |否|
    
- 返回示例:

    {"message":{},"sessionId":"cd908c6a-1dba-42b4-8a6f-3cb997ffb747","success":"true","page":{"result":["1_0_0_0"
],"order":null,"orderBy":null,"totalCount":1,"pageNo":null,"pageSize":null,"footResult":null}}

### /api/web/config/list

- 描述：根据app, env , version 获取所有的 配置列表，含有machine size, machine list,error num 等信息
- 请求类型: GET
- 参数

    |#|name   |desc   |是否必要|
    |---|-------|-------|----|
    |1|appId |app  |是|
    |2|envId |环境  |是|
    |3|version |版本  |是|
    
- 返回示例:

    {"message":{},"sessionId":"95839567-d098-4456-b44a-dd556454ec65","success":"true","page":{"result":[
{"configId":148,"appName":"disconf_demo","appId":2,"version":"1_0_0_0","envId":1,"envName":"rd","type"
:"配置文件","typeId":0,"key":"autoconfig.properties","value":"auto=bbdxxjdccdcccdxdcdc\nxx","createTime"
:"20150320130619","modifyTime":"201603271140","machineSize":1,"machineList":[{"machine":"localhost_0_4b860678-290a-4bdf-9a79-2600598f419b"
,"value":"{\"auto\":\"bbdxxjdccdcccdxdcdc\",\"xx\":\"\"}","errorList":[]}],"errorNum":0},{"configId"
:149,"appName":"disconf_demo","appId":2,"version":"1_0_0_0","envId":1,"envName":"rd","type":"配置文件","typeId"
:0,"key":"autoconfig2.properties","value":"auto2=cd你好 坑爹 22fd d","createTime":"20150320130625","modifyTime"
:"201602011810","machineSize":1,"machineList":[{"machine":"localhost_0_4b860678-290a-4bdf-9a79-2600598f419b"
,"value":"{\"auto2\":\"cd你好 坑爹 22fd d\"}","errorList":[]}],"errorNum":0}.....

### /api/web/config/simple/list

- 描述：根据app, env , version 获取所有的 配置列表，无machine size, machine list,error num 等信息
- 请求类型: GET
- 参数

    |#|name   |desc   |是否必要|
    |---|-------|-------|----|
    |1|appId |app  |是|
    |2|envId |环境  |是|
    |3|version |版本  |是|
    
- 返回示例:

    {"message":{},"sessionId":"ee170075-0974-4b9a-b341-f8f33beda453","success":"true","page":{"result":[
{"configId":155,"appName":"测试","appId":3,"version":"1_0_0_0","envId":1,"envName":"rd","type":"配置文件","typeId"
:0,"key":"a.properties","value":"","createTime":"20160423115212","modifyTime":"201604231152","machineSize"
:0,"machineList":[],"errorNum":0},{"configId":154,"appName":"测试","appId":3,"version":"1_0_0_0","envId"
:1,"envName":"rd","type":"配置项","typeId":1,"key":"dd","value":"","createTime":"20160423114721","modifyTime"
:"201604231147","machineSize":0,"machineList":[],"errorNum":0}],"order":"asc","orderBy":"name","totalCount"
:2,"pageNo":null,"pageSize":null,"footResult":null}}

### /api/web/config/{configId}

- 描述：获取某个config的内容
- 请求类型: GET
- 参数

    |#|name   |desc   |是否必要|
    |---|-------|-------|----|
    |1|configId |configId  |是|
    
- 返回示例:

    {"message":{},"sessionId":"2944fb48-3735-48a0-a1bf-ad1bf4980c71","success":"true","result":{"configId"
:148,"appName":"disconf_demo","appId":2,"version":"1_0_0_0","envId":1,"envName":"rd","type":"配置文件","typeId"
:0,"key":"autoconfig.properties","value":"auto=bbdxxjdccdcccdxdcdc\nxx","createTime":"20150320130619"
,"modifyTime":"201603271140","machineSize":0,"machineList":null,"errorNum":0}}


### /api/web/config/zk/{configId}

- 描述：获取该配置相对应的机器列表以及数据
- 请求类型: GET
- 参数

    |#|name   |desc   |是否必要|
    |---|-------|-------|----|
    |1|configId |configId  |是|
    
- 返回示例:

    {"message":{},"sessionId":"6bf69e7e-a6f7-4c42-b04e-0336c132fef2","success":"true","result":{"datalist"
:[{"machine":"localhost_0_4b860678-290a-4bdf-9a79-2600598f419b","value":"{\"auto\":\"bbdxxjdccdcccdxdcdc
\",\"xx\":\"\"}","errorList":[]}],"errorNum":0,"machineSize":1}}

### /api/web/config/download/{configId}

- 描述：以下载文件的形式下载配置
- 请求类型: GET
- 参数

    |#|name   |desc   |是否必要|
    |---|-------|-------|----|
    |1|configId |configId  |是|
    
- 返回示例: N/A

### /api/web/config/downloadfilebatch

- 描述：以下载文件的形式批量下载配置
- 请求类型: GET
- 参数

    |#|name   |desc   |是否必要|
    |---|-------|-------|----|
    |1|appId |app  |是|
    |2|envId |环境  |是|
    |3|version |版本  |是|
    
- 返回示例: N/A

### /api/web/config/item/{configId}

- 描述：修改配置项的值
- 请求类型: PUT
- 参数

    |#|name   |desc   |是否必要|
    |---|-------|-------|----|
    |1|configId |configId  |是|
    |1|value |value  |是|
    
- 返回示例: 

    {"message":{},"sessionId":"004cd21f-f2d4-4754-b5c1-f215266d63c4","success":"true","result":"修改成功，邮件发
送失败，请检查邮箱配置"}

### /api/web/config/file/{configId}

- 描述：以上传文件的形式 修改配置文件的值
- 请求类型: PUT
- 参数

    |#|name   |desc   |是否必要|
    |---|-------|-------|----|
    |1|configId |configId  |是|
    |1|myfilerar |文件  |是|
    
- 返回示例:  {"message":{},"sessionId":"6bacbb02-faf4-416b-bf12-b33d4df328ca","success":"true","result":"修改成功，邮件发
送失败，请检查邮箱配置"}


### /api/web/config/filetext/{configId}

- 描述：以上传文件内容的形式 修改配置文件的值
- 请求类型: PUT
- 参数

    |#|name   |desc   |是否必要|
    |---|-------|-------|----|
    |1|configId |configId  |是|
    |1|fileContent |文件内容  |是|
    
- 返回示例:  {"message":{},"sessionId":"6bacbb02-faf4-416b-bf12-b33d4df328ca","success":"true","result":"修改成功，邮件发
送失败，请检查邮箱配置"}

### /api/web/config/{configId}

- 描述：删除配置
- 请求类型: DELETE
- 参数

    |#|name   |desc   |是否必要|
    |---|-------|-------|----|
    |1|configId |configId  |是|
    
- 返回示例:  {"message":{},"sessionId":"b2e36172-1a60-479a-acc9-5854e3f93d98","success":"true","result":"删除成功"}

## zk 接口

## /api/zoo/zkdeploy

- 描述：获取ZK部署情况
- 请求类型: GET
- 参数

    |#|name   |desc   |是否必要|
    |---|-------|-------|----|
    |1|appId |app  |是|
    |2|envId |环境  |是|
    |3|version |版本  |是|
    
- 返回示例:  N/A



