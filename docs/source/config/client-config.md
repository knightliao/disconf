client配置
=======

##Disconf-Client##

### 配置文件 disconf.properties 说明 ###

所有配置均可以通过 命令行 `-Dname=value` 参数传入。
    
<table border="1" cellspacing="1" cellpadding="1">
  <tr>
   <th width="100px">配置项</th>
   <th width="150px">说明</th>
   <th width="30px">是否必填</th>
   <th width="50px">默认值</th>
  </tr>
  <tr>
    <td width="100px">disconf.conf_server_host</td>
    <td width="150px">配置服务器的 HOST,用逗号分隔 ，示例：127.0.0.1:8000,127.0.0.1:8000</td>
    <td width="30px">是</td>
    <td width="50px">必填</td>
  </tr>
  <tr>
    <td width="100px">disconf.app</td>
    <td width="150px">APP 请采用 产品线_服务名 格式 </td>
    <td width="30px">否</td>
    <td width="50px">优先读取命令行参数，然后再读取此文件的值</td>
  </tr>
  <tr>
    <td width="100px">disconf.version</td>
    <td width="150px">版本号, 请采用  X_X_X_X 格式 </td>
    <td width="30px">否</td>
    <td width="50px">默认为 DEFAULT_VERSION。优先读取命令行参数，然后再读取此文件的值，最后才读取默认值。</td>
  </tr>
  <tr>
    <td width="100px">disconf.enable.remote.conf</td>
    <td width="150px">是否使用远程配置文件，true(默认)会从远程获取配置， false则直接获取本地配置</td>
    <td width="30px">否</td>
    <td width="50px">false</td>
  </tr>
  <tr>
    <td width="100px">disconf.env</td>
    <td width="150px">环境</td>
    <td width="30px">否</td>
    <td width="50px">默认为 DEFAULT_ENV。优先读取命令行参数，然后再读取此文件的值，最后才读取默认值</td>
  </tr>
  <tr>
    <td width="100px">disconf.ignore</td>
    <td width="150px">忽略的分布式配置，用空格分隔</td>
    <td width="30px">否</td>
    <td width="50px">空</td>
  </tr>
  <tr>
      <td width="100px">disconf.debug</td>
      <td width="150px">调试模式。调试模式下，ZK超时或断开连接后不会重新连接（常用于client单步debug）。非调试模式下，ZK超时或断开连接会自动重新连接。</td>
      <td width="30px">否</td>
      <td width="50px">false</td>
    </tr>
  <tr>
    <td width="100px">disconf.conf_server_url_retry_times</td>
    <td width="150px">获取远程配置 重试次数，默认是3次</td>
    <td width="30px">否</td>
    <td width="50px">3</td>
  </tr>
  <tr>
    <td width="100px">disconf.conf_server_url_retry_sleep_seconds</td>
    <td width="150px">获取远程配置 重试时休眠时间，默认是5秒 </td>
    <td width="30px">否</td>
    <td width="50px">5</td>
  </tr>
  <tr>
    <td width="100px">disconf.user_define_download_dir</td>
    <td width="150px">用户定义的下载文件夹, 远程文件下载后会放在这里。注意，此文件夹必须有有权限，否则无法下载到这里</td>
    <td width="30px">否</td>
    <td width="50px">./disconf/download</td>
  </tr>
  <tr>
      <td width="100px">disconf.enable_local_download_dir_in_class_path</td>
      <td width="150px">下载的文件会被迁移到classpath根路径下，强烈建议将此选项置为 true(默认是true)  </td>
      <td width="30px">否</td>
      <td width="50px">true</td>
  </tr>
</table>
   
### 自定义 disconf.properties 文件的路径

一般情况下，disconf.properties 应该放在应用程序的根目录下，如果想自定义路径可以使用：

    -Ddisconf.conf=/tmp/disconf.properties



















