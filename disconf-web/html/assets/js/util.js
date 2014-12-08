var Util = {};
Util.number = {};
Util.lang = {};
Util.string = {};
Util.date = {};
Util.cookie = {};
Util.param = {};
Util.input = {};

/**
 * 对目标数字进行0补齐处理
 *
 * @name Util.number.pad
 * @function
 * @grammar Util.number.pad(source, length)
 * @param {number}
 *            source 需要处理的数字
 * @param {number}
 *            length 需要输出的长度
 *
 * @returns {string} 对目标数字进行0补齐处理后的结果
 */
Util.number.pad = function (source, length) {
    var pre = "", negative = (source < 0), string = String(Math.abs(source));

    if (string.length < length) {
        pre = (new Array(length - string.length + 1)).join('0');
    }

    return (negative ? "-" : "") + pre + string;
};

/**
 * 对目标日期对象进行格式化
 *
 * @name Util.date.format
 * @function
 * @grammar Util.date.format(source, pattern)
 * @param {Date}
 *            source 目标日期对象
 * @param {string}
 *            pattern 日期格式化规则
 * @remark
 *
 * <b>格式表达式，变量含义：</b><br>
 * <br>
 * hh: 带 0 补齐的两位 12 进制时表示<br>
 * h: 不带 0 补齐的 12 进制时表示<br>
 * HH: 带 0 补齐的两位 24 进制时表示<br>
 * H: 不带 0 补齐的 24 进制时表示<br>
 * mm: 带 0 补齐两位分表示<br>
 * m: 不带 0 补齐分表示<br>
 * ss: 带 0 补齐两位秒表示<br>
 * s: 不带 0 补齐秒表示<br>
 * yyyy: 带 0 补齐的四位年表示<br>
 * yy: 带 0 补齐的两位年表示<br>
 * MM: 带 0 补齐的两位月表示<br>
 * M: 不带 0 补齐的月表示<br>
 * dd: 带 0 补齐的两位日表示<br>
 * d: 不带 0 补齐的日表示
 *
 * @returns {string} 格式化后的字符串
 */
Util.date.format = function (source, pattern) {
    if ('string' != typeof pattern) {
        return source.toString();
    }

    function replacer(patternPart, result) {
        pattern = pattern.replace(patternPart, result);
    }

    var pad = Util.number.pad, year = source.getFullYear(), month = source
        .getMonth() + 1, date2 = source.getDate(), hours = source
        .getHours(), minutes = source.getMinutes(), seconds = source
        .getSeconds();

    replacer(/yyyy/g, pad(year, 4));
    replacer(/yy/g, pad(parseInt(year.toString().slice(2), 10), 2));
    replacer(/MM/g, pad(month, 2));
    replacer(/M/g, month);
    replacer(/dd/g, pad(date2, 2));
    replacer(/d/g, date2);

    replacer(/HH/g, pad(hours, 2));
    replacer(/H/g, hours);
    replacer(/hh/g, pad(hours % 12, 2));
    replacer(/h/g, hours % 12);
    replacer(/mm/g, pad(minutes, 2));
    replacer(/m/g, minutes);
    replacer(/ss/g, pad(seconds, 2));
    replacer(/s/g, seconds);

    return pattern;
};

/**
 * 将14位时间格式转化为日期字符串
 *
 * @param {string}
 *            dateNum 日期字符串.
 * @param {string=}
 *            opt_formatType 日期字符串的格式，默认是yyyy-M-d HH:mm:ss.
 * @return {string}
 */
Util.date.dateFormat = function (dateNum, opt_formatType) {
    if ('' === dateNum || '--' === dateNum) {
        return '--';
    }
    var type = opt_formatType || 'yyyy-MM-dd';
    return Util.date.format(Util.date.parseToDate(dateNum), type);
},

    Util.lang.isDate = function (source) {
        return '[object Date]' == Object.prototype.toString.call(
            /** @type {Object} */
            (source));
    };
/**
 * 把20110102235959 14位数字转为DATE对象
 *
 * @param {string}
 *            num 需要转化的数字.
 * @return {Date} 日期对象.
 */
Util.date.parseToDate = function (num) {
    function parse(source) {
        var match = null;
        if (match = /^(\d{4})[-\/]?([01]\d)[-\/]?([0-3]\d)$/.exec(source)) {
            return new Date(parseInt(match[1], 10), parseInt(match[2], 10) - 1,
                parseInt(match[3], 10));
        }
        return null;
    }
    ;

    if (Util.lang.isDate(num)) {
        return num;
    }
    num = num + '';
    var date = parse(num.substring(0, 8));
    if (num.substring(8, 10))
        date.setHours(num.substring(8, 10));
    if (num.substring(10, 12))
        date.setMinutes(num.substring(10, 12));
    if (num.substring(12))
        date.setSeconds(num.substring(12));
    return date;
};
/**
 * 把DATE对象 生成 14位数字，后面是235959，用于ENDTIME
 *
 * @param date
 */
Util.date.parseEndTime = function (date) {
    return Util.date.format(date, 'yyyyMMdd') + '235959';
};

/**
 * 把DATE对象 转为 14位数字 20110102235959
 *
 * @param date
 */
Util.date.dateToNumber = function (date) {
    return Util.date.format(date, 'yyyyMMdd');
};

/**
 * 是否是一个简单的对象
 *
 * @param {*}
 *            source 需要判断的对象.
 * @return {boolean} true是，false不是.
 */
Util.lang.isPlainObject = function (source) {
    return '[object Object]' == Object.prototype.toString.call(
        /** @type {Object} */
        (source));
};

/**
 * 对目标字符串进行格式化
 *
 * @param {string}
 *            source 目标字符串.
 * @param {Object|string}
 *            opts 提供相应数据的对象.
 * @return {string} 格式化后的字符串.
 */
Util.string.format = function (source, opts) {
    source = String(source);

    if ('undefined' != typeof opts) {
        if (Util.lang.isPlainObject(opts)) {
            return source.replace(/\$\{(.+?)\}/g, function (match, key) {
                var replacer = opts[key];
                if ('function' == typeof replacer) {
                    replacer = replacer(key);
                }
                return ('undefined' == typeof replacer ? '' : replacer);
            });
        } else {
            var data = Array.prototype.slice.call(arguments, 1), len = data.length;
            return source.replace(/\{(\d+)\}/g, function (match, index) {
                index = parseInt(index, 10);
                return (index >= len ? match : data[index]);
            });
        }
    }

    return source;
};

/**
 * 验证字符串是否合法的cookie键名
 *
 * @param {string}
 *            source 需要遍历的数组
 * @meta standard
 * @return {boolean} 是否合法的cookie键名
 */
Util.cookie._isValidKey = function (key) {
    // http://www.w3.org/Protocols/rfc2109/rfc2109
    // Syntax: General
    // The two state management headers, Set-Cookie and Cookie, have common
    // syntactic properties involving attribute-value pairs. The following
    // grammar uses the notation, and tokens DIGIT (decimal digits) and
    // token (informally, a sequence of non-special, non-white space
    // characters) from the HTTP/1.1 specification [RFC 2068] to describe
    // their syntax.
    // av-pairs = av-pair *(";" av-pair)
    // av-pair = attr ["=" value] ; optional value
    // attr = token
    // value = word
    // word = token | quoted-string

    // http://www.ietf.org/rfc/rfc2068.txt
    // token = 1*<any CHAR except CTLs or tspecials>
    // CHAR = <any US-ASCII character (octets 0 - 127)>
    // CTL = <any US-ASCII control character
    // (octets 0 - 31) and DEL (127)>
    // tspecials = "(" | ")" | "<" | ">" | "@"
    // | "," | ";" | ":" | "\" | <">
    // | "/" | "[" | "]" | "?" | "="
    // | "{" | "}" | SP | HT
    // SP = <US-ASCII SP, space (32)>
    // HT = <US-ASCII HT, horizontal-tab (9)>

    return (new RegExp(
        "^[^\\x00-\\x20\\x7f\\(\\)<>@,;:\\\\\\\"\\[\\]\\?=\\{\\}\\/\\u0080-\\uffff]+\x24"))
        .test(key);
};
/**
 * 获取cookie的值，不对值进行解码
 *
 * @name Util.cookie.getRaw
 * @function
 * @grammar Util.cookie.getRaw(key)
 * @param {string}
 *            key 需要获取Cookie的键名
 * @meta standard
 * @see Util.cookie.get,Util.cookie.setRaw
 *
 * @returns {string|null} 获取的Cookie值，获取不到时返回null
 */
Util.cookie.getRaw = function (key) {
    if (Util.cookie._isValidKey(key)) {
        var reg = new RegExp("(^| )" + key + "=([^;]*)(;|\x24)"), result = reg
            .exec(document.cookie);

        if (result) {
            return result[2] || null;
        }
    }

    return null;
};
/*
 * Tangram Copyright 2009 Util Inc. All rights reserved.
 * 
 * path: Util/cookie/get.js author: erik version: 1.1.0 date: 2009/11/15
 */

/**
 * 获取cookie的值，用decodeURIComponent进行解码
 *
 * @name Util.cookie.get
 * @function
 * @grammar Util.cookie.get(key)
 * @param {string}
 *            key 需要获取Cookie的键名
 * @remark <b>注意：</b>该方法会对cookie值进行decodeURIComponent解码。如果想获得cookie源字符串，请使用getRaw方法。
 * @meta standard
 * @see Util.cookie.getRaw,Util.cookie.set
 *
 * @returns {string|null} cookie的值，获取不到时返回null
 */
Util.cookie.get = function (key) {
    var value = Util.cookie.getRaw(key);
    if ('string' == typeof value) {
        value = decodeURIComponent(value);
        return value;
    }
    return null;
};
/*
 * Tangram Copyright 2009 Util Inc. All rights reserved.
 * 
 * path: Util/cookie/setRaw.js author: erik version: 1.1.0 date: 2009/11/15
 */

/**
 * 设置cookie的值，不对值进行编码
 *
 * @name Util.cookie.setRaw
 * @function
 * @grammar Util.cookie.setRaw(key, value[, options])
 * @param {string}
 *            key 需要设置Cookie的键名
 * @param {string}
 *            value 需要设置Cookie的值
 * @param {Object}
 *            [options] 设置Cookie的其他可选参数
 * @config {string} [path] cookie路径
 * @config {Date|number} [expires] cookie过期时间,如果类型是数字的话, 单位是毫秒
 * @config {string} [domain] cookie域名
 * @config {string} [secure] cookie是否安全传输
 * @remark
 *
 * <b>options参数包括：</b><br>
 * path:cookie路径<br>
 * expires:cookie过期时间，Number型，单位为毫秒。<br>
 * domain:cookie域名<br>
 * secure:cookie是否安全传输
 *
 * @meta standard
 * @see Util.cookie.set,Util.cookie.getRaw
 */
Util.cookie.setRaw = function (key, value, options) {
    if (!Util.cookie._isValidKey(key)) {
        return;
    }

    options = options || {};
    // options.path = options.path || "/"; // meizz 20100402 设定一个初始值，方便后续的操作
    // berg 20100409 去掉，因为用户希望默认的path是当前路径，这样和浏览器对cookie的定义也是一致的

    // 计算cookie过期时间
    var expires = options.expires;
    if ('number' == typeof options.expires) {
        expires = new Date();
        expires.setTime(expires.getTime() + options.expires);
    }

    document.cookie = key + "=" + value
        + (options.path ? "; path=" + options.path : "")
        + (expires ? "; expires=" + expires.toGMTString() : "")
        + (options.domain ? "; domain=" + options.domain : "")
        + (options.secure ? "; secure" : '');
};
/*
 * Tangram Copyright 2009 Util Inc. All rights reserved.
 * 
 * path: Util/cookie/remove.js author: erik version: 1.1.0 date: 2009/11/15
 */

/**
 * 删除cookie的值
 *
 * @name Util.cookie.remove
 * @function
 * @grammar Util.cookie.remove(key, options)
 * @param {string}
 *            key 需要删除Cookie的键名
 * @param {Object}
 *            options 需要删除的cookie对应的 path domain 等值
 * @meta standard
 */
Util.cookie.remove = function (key, options) {
    options = options || {};
    options.expires = new Date(0);
    Util.cookie.setRaw(key, '', options);
};
/*
 * Tangram Copyright 2009 Util Inc. All rights reserved.
 * 
 * path: Util/cookie/set.js author: erik version: 1.1.0 date: 2009/11/15
 */

/**
 * 设置cookie的值，用encodeURIComponent进行编码
 *
 * @name Util.cookie.set
 * @function
 * @grammar Util.cookie.set(key, value[, options])
 * @param {string}
 *            key 需要设置Cookie的键名
 * @param {string}
 *            value 需要设置Cookie的值
 * @param {Object}
 *            [options] 设置Cookie的其他可选参数
 * @config {string} [path] cookie路径
 * @config {Date|number} [expires] cookie过期时间,如果类型是数字的话, 单位是毫秒
 * @config {string} [domain] cookie域名
 * @config {string} [secure] cookie是否安全传输
 * @remark
 *
 * 1. <b>注意：</b>该方法会对cookie值进行encodeURIComponent编码。如果想设置cookie源字符串，请使用setRaw方法。<br>
 * <br>
 * 2. <b>options参数包括：</b><br>
 * path:cookie路径<br>
 * expires:cookie过期时间，Number型，单位为毫秒。<br>
 * domain:cookie域名<br>
 * secure:cookie是否安全传输
 *
 * @meta standard
 * @see Util.cookie.setRaw,Util.cookie.get
 */
Util.cookie.set = function (key, value, options) {
    Util.cookie.setRaw(key, encodeURIComponent(value), options);
};

/*
 * 获取location中search的值 @return {string} value 返回activityId
 */
Util.param.getActivityId = function () {
    var ids = /activityId=(\d+)&?/.exec(location.search);
    var id = ids ? ids[1] : '';
    return id;
};

/*
 * 获取location中search的值 @return {string} value 返回configId
 */
Util.param.getConfigId = function () {
    var ids = /configId=(\d+)&?/.exec(location.search);
    var id = ids ? ids[1] : '';
    return id;
};

/*
 * 获取location中path的值 @return {string} value 返回pageName
 */
Util.param.getPageName = function () {
    var pageNames = /\/(\w+)\.html/.exec(location.pathname);
    var pageName = pageNames ? pageNames[1] : 'index';
    return pageName;
};

/*
 * 获取location中search的值 @return {string} value 返回status
 */
Util.param.getStatus = function () {
    var stas = /status=(\d+)&?/.exec(location.search);
    var sta = stas ? stas[1] : '';
    return sta;
};

/*
 * 获取location中search的值 @return {string} value 返回创建页面时的序号
 */
Util.param.getCreateIndex = function () {
    var stas = /createIndex=(\d+)&?/.exec(location.search);
    var sta = stas ? stas[1] : '';
    return sta;
};

/*
 * 判断是否是更新 @return {boolean}
 */
Util.param.isUpdate = function () {
    // var typeArr = location.search.match(/type=[a-zA-Z]+/g);
    if (location.search.indexOf("type=update") !== -1) {
        return true;
    }
};

/*
 * 判断是否是创建 @return {boolean}
 */
Util.param.isCreate = function () {
    if (location.search.indexOf("createIndex") !== -1) {
        return true;
    }
};
/*
 * 输出错误信息 @param {Object} $elm jQuery对象 @param {Object} data 返回的对象
 */
Util.input.whiteError = function ($elm, data) {
    var html = '';
    $elm.show();
    if (data.message.global) {
        $elm.html(data.message.global);
        return;
    }
    $.each(data.message.field, function (key, value) {
        html += key + "：" + value + "<br/>";
    });
    $elm.html(html);
};

//
// 回车转为br标签
//
Util.input.return2Br = function (str) {
    return str.replace(/\r?\n/g, "<br />");
}


var entityMap = {
    "&": "&amp;",
    "<": "&lt;",
    ">": "&gt;",
    '"': '&quot;',
    "'": '&#39;',
    "/": '&#x2F;'
};


//
// 回车转为br标签
//
Util.input.escapeHtml = function escapeHtml(string) {
    return String(string).replace(/[&<>"'\/]/g, function (s) {
        return entityMap[s];
    });
}