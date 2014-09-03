EventProxy [![Build Status](https://secure.travis-ci.org/JacksonTian/eventproxy.png)](http://travis-ci.org/JacksonTian/eventproxy) [![NPM version](https://badge.fury.io/js/eventproxy.png)](http://badge.fury.io/js/eventproxy) [English Doc](https://github.com/JacksonTian/eventproxy/blob/master/README_en.md)
======

> 这个世界上不存在所谓回调函数深度嵌套的问题。 —— [Jackson Tian](http://weibo.com/shyvo)

> 世界上本没有嵌套回调，写得人多了，也便有了`}}}}}}}}}}}}`。 —— [fengmk2](http://fengmk2.github.com)

* API文档: [API Documentation](http://html5ify.com/eventproxy/api.html)
* jscoverage: [97%](http://html5ify.com/eventproxy/coverage.html)
* 源码注解：[注解文档](http://html5ify.com/eventproxy/eventproxy.html)


EventProxy 仅仅是一个很轻量的工具，但是能够带来一种事件式编程的思维变化。有几个特点：

1. 利用事件机制解耦复杂业务逻辑
2. 移除被广为诟病的深度callback嵌套问题
3. 将串行等待变成并行等待，提升多异步协作场景下的执行效率
4. 友好的Error handling
5. 无平台依赖，适合前后端，能用于浏览器和Node.js
6. 兼容CMD，AMD以及CommonJS模块环境

现在的，无深度嵌套的，并行的

```js
var ep = EventProxy.create("template", "data", "l10n", function (template, data, l10n) {
  _.template(template, data, l10n);
});

$.get("template", function (template) {
  // something
  ep.emit("template", template);
});
$.get("data", function (data) {
  // something
  ep.emit("data", data);
});
$.get("l10n", function (l10n) {
  // something
  ep.emit("l10n", l10n);
});
```

过去的，深度嵌套的，串行的。

```js
var render = function (template, data) {
  _.template(template, data);
};
$.get("template", function (template) {
  // something
  $.get("data", function (data) {
    // something
    $.get("l10n", function (l10n) {
      // something
      render(template, data, l10n);
    });
  });
});
```
## 安装
### Node用户
通过NPM安装即可使用：

```bash
$ npm install eventproxy
```

调用:

```js
var EventProxy = require('eventproxy');
```

### Component

```bash
$ component install JacksonTian/eventproxy
```

### 前端用户
以下示例均指向Github的源文件地址，您也可以[下载源文件](https://raw.github.com/JacksonTian/eventproxy/master/lib/eventproxy.js)到你自己的项目中。整个文件注释全面，带注释和空行，一共约500行。为保证EventProxy的易嵌入，项目暂不提供压缩版。用户可以自行采用Uglify、YUI Compressor或Google Closure Complier进行压缩。

#### 普通环境
在页面中嵌入脚本即可使用：

```html
<script src="https://raw.github.com/JacksonTian/eventproxy/master/lib/eventproxy.js"></script>
```
使用：

```js
// EventProxy此时是一个全局变量
var ep = new EventProxy();
```

#### SeaJS用户
SeaJS下只需配置别名，然后`require`引用即可使用。

```js
// 配置
seajs.config({
  alias: {
    eventproxy: 'https://raw.github.com/JacksonTian/eventproxy/master/lib/eventproxy.js'
  }
});
// 使用
seajs.use(['eventproxy'], function (EventProxy) {
  // TODO
});
// 或者
define('test', function (require, exports, modules) {
  var EventProxy = require('eventproxy');
});
```

#### RequireJS用户
RequireJS实现的是AMD规范。

```js
// 配置路径
require.config({
  paths: {
    eventproxy: "https://raw.github.com/JacksonTian/eventproxy/master/lib/eventproxy"
  }
});
// 使用
require(["eventproxy"], function (EventProxy) {
  // TODO
});
```
## 异步协作
### 多类型异步协作
此处以页面渲染为场景，渲染页面需要模板、数据。假设都需要异步读取。

```js
var ep = new EventProxy();
ep.all('tpl', 'data', function (tpl, data) {
  // 在所有指定的事件触发后，将会被调用执行
  // 参数对应各自的事件名
});
fs.readFile('template.tpl', 'utf-8', function (err, content) {
  ep.emit('tpl', content);
});
db.get('some sql', function (err, result) {
  ep.emit('data', result);
});
```

`all`方法将handler注册到事件组合上。当注册的多个事件都触发后，将会调用handler执行，每个事件传递的数据，将会依照事件名顺序，传入handler作为参数。
#### 快速创建
EventProxy提供了`create`静态方法，可以快速完成注册`all`事件。

```js
var ep = EventProxy.create('tpl', 'data', function (tpl, data) {
  // TODO
});
```

以上方法等效于

```js
var ep = new EventProxy();
ep.all('tpl', 'data', function (tpl, data) {
  // TODO
});
```

### 重复异步协作
此处以读取目录下的所有文件为例，在异步操作中，我们需要在所有异步调用结束后，执行某些操作。

```js
var ep = new EventProxy();
ep.after('got_file', files.length, function (list) {
  // 在所有文件的异步执行结束后将被执行
  // 所有文件的内容都存在list数组中
});
for (var i = 0; i < files.length; i++) {
  fs.readFile(files[i], 'utf-8', function (err, content) {
    // 触发结果事件
    ep.emit('got_file', content);
  });
}
```

`after`方法适合重复的操作，比如读取10个文件，调用5次数据库等。将handler注册到N次相同事件的触发上。达到指定的触发数，handler将会被调用执行，每次触发的数据，将会按触发顺序，存为数组作为参数传入。

### 持续型异步协作
此处以股票为例，数据和模板都是异步获取，但是数据会持续刷新，视图会需要重新刷新。

```js
var ep = new EventProxy();
ep.tail('tpl', 'data', function (tpl, data) {
  // 在所有指定的事件触发后，将会被调用执行
  // 参数对应各自的事件名的最新数据
});
fs.readFile('template.tpl', 'utf-8', function (err, content) {
  ep.emit('tpl', content);
});
setInterval(function () {
  db.get('some sql', function (err, result) {
    ep.emit('data', result);
  });
}, 2000);
```

`tail`与`all`方法比较类似，都是注册到事件组合上。不同在于，指定事件都触发之后，如果事件依旧持续触发，将会在每次触发时调用handler，极像一条尾巴。


## 基本事件
通过事件实现异步协作是EventProxy的主要亮点。除此之外，它还是一个基本的事件库。携带如下基本API

- `on`/`addListener`，绑定事件监听器
- `emit`，触发事件
- `once`，绑定只执行一次的事件监听器
- `removeListener`，移除事件的监听器
- `removeAllListeners`，移除单个事件或者所有事件的监听器

为了照顾各个环境的开发者，上面的方法多具有别名。

- YUI3使用者，`subscribe`和`fire`你应该知道分别对应的是`on`/`addListener`和`emit`。
- jQuery使用者，`trigger`对应的方法是`emit`，`bind`对应的就是`on`/`addListener`。
- `removeListener`和`removeAllListeners`其实都可以通过别名`unbind`完成。

所以在你的环境下，选用你喜欢的API即可。

更多API的描述请访问[API Docs](http://html5ify.com/eventproxy/api.html)。

## 异常处理
在异步方法中，实际上，异常处理需要占用一定比例的精力。在过去一段时间内，我们都是通过额外添加`error`事件来进行处理的，代码大致如下：

```js
exports.getContent = function (callback) {
 var ep = new EventProxy();
  ep.all('tpl', 'data', function (tpl, data) {
    // 成功回调
    callback(null, {
      template: tpl,
      data: data
    });
  });
  // 侦听error事件
  ep.bind('error', function (err) {
    // 卸载掉所有handler
    ep.unbind();
    // 异常回调
    callback(err);
  });
  fs.readFile('template.tpl', 'utf-8', function (err, content) {
    if (err) {
      // 一旦发生异常，一律交给error事件的handler处理
      return ep.emit('error', err);
    }
    ep.emit('tpl', content);
  });
  db.get('some sql', function (err, result) {
    if (err) {
      // 一旦发生异常，一律交给error事件的handler处理
      return ep.emit('error', err);
    }
    ep.emit('data', result);
  });
};
```

代码量因为异常的处理，一下子上去了很多。在这里EventProxy经过很多实践后，我们根据我们的最佳实践提供了优化的错误处理方案。

```js
exports.getContent = function (callback) {
 var ep = new EventProxy();
  ep.all('tpl', 'data', function (tpl, data) {
    // 成功回调
    callback(null, {
      template: tpl,
      data: data
    });
  });
  // 添加error handler
  ep.fail(callback);

  fs.readFile('template.tpl', 'utf-8', ep.done('tpl'));
  db.get('some sql', ep.done('data'));
};
```

上述代码优化之后，业务开发者几乎不用关心异常处理了。代码量降低效果明显。  
这里代码的转换，也许有开发者并不放心。其实秘诀在`fail`方法和`done`方法中。

### 神奇的fail

```js
ep.fail(callback);
// 由于参数位相同，它实际是
ep.fail(function (err) {
  callback(err);
});

// 等价于
ep.bind('error', function (err) {
  // 卸载掉所有handler
  ep.unbind();
  // 异常回调
  callback(err);
});
```

`fail`方法侦听了`error`事件，默认处理卸载掉所有handler，并调用回调函数。

### 神奇的done

```js
ep.done('tpl');
// 等价于
function (err, content) {
  if (err) {
    // 一旦发生异常，一律交给error事件的handler处理
    return ep.emit('error', err);
  }
  ep.emit('tpl', content);
}
```

在Node的最佳实践中，回调函数第一个参数一定会是一个`error`对象。检测到异常后，将会触发`error`事件。剩下的参数，将触发事件，传递给对应handler处理。

#### done也接受回调函数
`done`方法除了接受事件名外，还接受回调函数。如果是函数时，它将剔除第一个`error`对象(此时为`null`)后剩余的参数，传递给该回调函数作为参数。该回调函数无需考虑异常处理。

```js
ep.done(function (content) {
  // 这里无需考虑异常
  // 手工emit
  ep.emit('someevent', newcontent);
});
```

当然手工emit的方式并不太好，我们更进一步的版本：

```js
ep.done('tpl', function (tpl) {
  // 将内容更改后，返回即可
  return tpl.trim();
});
```

#### 注意事项
如果`emit`需要传递多个参数时，`ep.done(event, fn)`的方式不能满足需求，还是需要`ep.done(fn)`，进行手工`emit`多个参数。

### 神奇的group
`fail`除了用于协助`all`方法完成外，也能协助`after`中的异常处理。另外，在`after`的回调函数中，结果顺序是与用户`emit`的顺序有关。为了满足返回数据按发起异步调用的顺序排列，`EventProxy`提供了`group`方法。

```js
var ep = new EventProxy();
ep.after('got_file', files.length, function (list) {
  // 在所有文件的异步执行结束后将被执行
  // 所有文件的内容都存在list数组中，按顺序排列
});
for (var i = 0; i < files.length; i++) {
  fs.readFile(files[i], 'utf-8', ep.group('got_file'));
}
```
`group`秉承`done`函数的设计，它包含异常的传递。同时它还隐含了对返回数据进行编号，在结束时，按顺序返回。

```js
ep.group('got_file');
// 约等价于
function (err, data) {
  if (err) {
    return ep.emit('error', err);
  }
  ep.emit('got_file', data);
};
```

当回调函数的数据还需要进行加工时，可以给`group`带上回调函数，只要在操作后将数据返回即可：

```js
ep.group('got_file', function (data) {
  // some code
  return data;
});
```

### 异步事件触发: emitLater && doneLater

在node中，`emit`方法是同步的，EventProxy中的`emit`，`trigger`等跟node的风格一致，也是同步的。看下面这段代码，可能眼尖的同学一下就发现了隐藏的bug:     
```js
var ep = EventProxy.create();

db.check('key', function (err, permission) {
  if (err) {
    return ep.emit('error', err);
  }
  ep.emit('check', permission);
});

ep.once('check', function (permission) {
  permission && db.get('key', function (err, data) {
    if (err) {
      return ep.emit('error');
    }
    ep.emit('get', data);
  });
});

ep.once('get', function (err, data) {
  if (err) {
    retern ep.emit('error', err);
  }
  render(data);
});

ep.on('error', errorHandler);
```

没错，万一`db.check`的`callback`被同步执行了，在`ep`监听`check`事件之前，它就已经被抛出来了，后续逻辑没办法继续执行。尽管node的约定是所有的`callback`都是需要异步返回的，但是如果这个方法是由第三方提供的，我们没有办法保证`db.check`的`callback`一定会异步执行，所以我们的代码通常就变成了这样:   

```js
var ep = EventProxy.create();

ep.once('check', function (permission) {
  permission && db.get('key', function (err, data) {
    if (err) {
      return ep.emit('error');
    }
    ep.emit('get', data);
  });
});

ep.once('get', function (err, data) {
  if (err) {
    retern ep.emit('error', err);
  }
  render(data);
});

ep.on('error', errorHandler);

db.check('key', function (err, permission) {
  if (err) {
    return ep.emit('error', err);
  }
  ep.emit('check', permission);
});
```
我们被迫把`db.check`挪到最后，保证事件先被监听，再执行`db.check`。`check`->`get`->`render`的逻辑，在代码中看起来变成了`get`->`render`->`check`。如果整个逻辑更加复杂，这种风格将会让代码很难读懂。   

这时候，我们需要的就是 __异步事件触发__：   

```js
var ep = EventProxy.create();

db.check('key', function (err, permission) {
  if (err) {
    return ep.emitLater('error', err);
  }
  ep.emitLater('check', permission);
});

ep.once('check', function (permission) {
  permission && db.get('key', function (err, data) {
    if (err) {
      return ep.emit('error');
    }
    ep.emit('get', data);
  });
});

ep.once('get', function (err, data) {
  if (err) {
    retern ep.emit('error', err);
  }
  render(data);
});

ep.on('error', errorHandler);
```
上面代码中，我们把`db.check`的回调函数中的事件通过`emitLater`触发，这样,就算`db.check`的回调函数被同步执行了，事件的触发也还是异步的，`ep`在当前事件循环中监听了所有的事件，之后的事件循环中才会去触发`check`事件。代码顺序将和逻辑顺序保持一致。   
当然，这么复杂的代码，必须可以像`ep.done()`一样通过`doneLater`来解决：   

```js
var ep = EventProxy.create();

db.check('key', ep.doneLater('check'));

ep.once('check', function (permission) {
  permission && db.get('key', ep.done('get'));
});

ep.once('get', function (data) {
  render(data);
});

ep.fail(errorHandler);
```
最终呈现出来的，是一段简洁且清晰的代码。   


## 注意事项
- 请勿使用`all`作为业务中的事件名。该事件名为保留事件。
- 异常处理部分，请遵循Node的最佳实践(回调函数首个参数为异常传递位)。

## [贡献者们](https://github.com/JacksonTian/eventproxy/graphs/contributors)
谢谢EventProxy的使用者们，享受EventProxy的过程，也给EventProxy回馈良多。

```bash
 project  : eventproxy
 repo age : 1 year, 10 months
 active   : 58 days
 commits  : 136
 files    : 18
 authors  : 
   123  Jackson Tian            90.4%
     6  fengmk2                 4.4%
     4  dead-horse              2.9%
     1  haoxin                  0.7%
     1  redky                   0.7%
     1  yaoazhen                0.7%

```

## License 

[The MIT License](https://github.com/JacksonTian/eventproxy/blob/master/MIT-License)。请自由享受开源。

## 捐赠
如果您觉得本模块对您有帮助，欢迎请作者一杯咖啡

[![捐赠EventProxy](https://img.alipay.com/sys/personalprod/style/mc/btn-index.png)](https://me.alipay.com/jacksontian)


[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/JacksonTian/eventproxy/trend.png)](https://bitdeli.com/free "Bitdeli Badge")

