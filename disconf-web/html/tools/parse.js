var fs = require('fs');
var ejs = require('ejs');
var EventProxy = require('eventproxy');
var ep = new EventProxy();

// 在所有指定的事件触发后，将会被调用执行
// 参数对应各自的事件名的最新数据
ep.tail('basehead', 'head', 'nav','leftbar', 'foot', 'basefoot', 'main', function(basehead,
		head, nav, leftbar, foot, basefoot, mainArr) {
	render(basehead, head, nav, leftbar, foot, basefoot, mainArr[0], mainArr[1]);
});

// 读取Base头部模板
fs.readFile('../unitTpl/basehead.html.tpl', 'utf8', function(err, basehead) {
	if (err)
		throw err;
	ep.emit('basehead', basehead);
});
// 读取头部模板
fs.readFile('../unitTpl/head.html.tpl', 'utf8', function(err, head) {
	if (err)
		throw err;
	ep.emit('head', head);
});
// 读取导航模板
fs.readFile('../unitTpl/nav.html.tpl', 'utf8', function(err, nav) {
    if (err)
        throw err;
    ep.emit('nav', nav);
});
//读取左侧导航模板
fs.readFile('../unitTpl/leftbar.html.tpl', 'utf8', function(err, leftbar) {
    if (err)
        throw err;
    ep.emit('leftbar', leftbar);
});
// 读取尾部模板
fs.readFile('../unitTpl/foot.html.tpl', 'utf8', function(err, foot) {
	if (err)
		throw err;
	ep.emit('foot', foot);
});
// 读取尾部模板
fs.readFile('../unitTpl/basefoot.html.tpl', 'utf8', function(err, basefoot) {
	if (err)
		throw err;
	ep.emit('basefoot', basefoot);
});
// 读取页面主模板
fs.readdir("../mainTpl", function(err, dirArr) {
	if (err)
		throw err;
	dirArr.forEach(function(item, index) {
		var name = /^(\w+)\.tpl/.exec(item)[1];
		var mainPath = "../mainTpl/" + item;
		fs.readFile(mainPath, 'utf8', function(err1, main) {
			if (err1)
				throw err1;
			ep.emit('main', [ main, name ]);
		});
	});
});

// 转义html标签
function decodeHtml(html) {
	return String(html).replace(/&amp;/g, "&").replace(/&quot;/g, '"').replace(
			/&#39;/g, "'").replace(/&lt;/g, "<").replace(/&gt;/g, ">");
}
// 拼接模板
function render(basehead, head, nav, leftbar, foot, basefoot, main, name) {
	var html = ejs.render(main, {
		"page" : {
			"basehead" : basehead,
			"head" : head,
			"nav" : nav,
			"leftbar":leftbar,
			"foot" : foot,
			"basefoot" : basefoot
		}
	});
	var html = decodeHtml(html);
	createFile(html, name);
}
// 生成最终文件
function createFile(html, name) {
	fs.writeFile('../' + name + '.html', html, 'utf8', function(err) {
		if (err)
			throw err;
		console.log(name + '.html is created!'); // 文件被保存
	});
}
