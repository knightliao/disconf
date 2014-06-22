(function($) {
    var isUpdate = Util.param.isUpdate();
    var activityId = Util.param.getActivityId ();
    
    var ticketPool = {};
    var ticketIndex = 0;
    var itemTpl = $("#ticketPoolItemTpl").html();
    
    if (isUpdate) {
        get();
    }
    
    //更新
    function get() {
        $.ajax({
            type: "GET",
            url: "api/activity/sec/" + activityId
        })
        .done(function( data ) {
            if (data.success === "true") {
                var result = data.result;
                //票
                var ticketDataArr = result.ticketData.split(",");
                var ticketItemArr = [];
                if (ticketDataArr.length && ticketDataArr[0]) {
                    $.each(ticketDataArr, function(index, item) {
                        item && ticketItemArr.push(item.split("|"));
                    });
                    if (ticketItemArr.length) {
                        $.each(ticketItemArr, function(index, item) {
                            saveItem(null, item);
                        });
                    }
                }
                //卡
                if (!result.cardData) {
                    getDefaultCard();
                    return;
                }
                var cardDataArr = result.cardData.split(",");
                var cardItemArr = [];
                if (cardDataArr.length) {
                    $.each(cardDataArr, function(index, item) {
                        cardItemArr.push(item.split("|"));
                    });
                    if (cardItemArr.length) {
                        $("#goldId").val(cardItemArr[0][0]);
                        $("#goldDiscount").val(cardItemArr[0][1]);
                        $("#goldScore").val(cardItemArr[0][2]);
                        $("#platinumId").val(cardItemArr[1][0]);
                        $("#platinumDiscount").val(cardItemArr[1][1]);
                        $("#platinumScore").val(cardItemArr[1][2]);
                        $("#blackId").val(cardItemArr[2][0]);
                        $("#blackDiscount").val(cardItemArr[2][1]);
                        $("#blackScore").val(cardItemArr[2][2]);
                    }
                }
            }
            else {
                Util.input.whiteError($("#error"), data);
            }
        });
    }
    
    function saveItem(e, item) {
        var ticketName = item ? item[0] : $("#ticketName").val();
        var ticketPrice = item ? item[1] : $("#ticketPrice").val();
        var ticketMax = item ? item[2] : $("#ticketMax").val();
        var ticketDescribe = item ? item[3] : $("#ticketDescribe").val();
        ticketPool[ticketIndex] = {
            "ticketName" : ticketName,
            "ticketPrice" : ticketPrice,
            "ticketMax" : ticketMax,
            "ticketDescribe" : ticketDescribe,
        };
        var itemHtml = '';
        $.each(ticketPool, function (key, obj) {
            if (!obj) {
                return;
            }
            itemHtml += rendItem(obj, key);
        });
        $("#ticketPoolWrap").html(itemHtml);
        bindEvent();
        ticketIndex ++;
        closeItem();
        //console.log(ticketPool);
    }
    
    function rendItem(item, key) {
        return Util.string.format(
            itemTpl,
            item.ticketName,
            item.ticketPrice,
            item.ticketMax,
            item.ticketDescribe,
            key
        );
    }
    
    function bindEvent() {
        $.each(ticketPool, function(key, item) {
            $(("#closePoolItem" + key)).click(function(e) {
                ticketPool[key] = null;
                $(("#ticketPoolWrapItem" + key)).remove();
                console.log(ticketPool);
            });
            //编辑
            $(("#eidtPoolItem" + key)).click(function(e) {
                $(("#ticketPoolWrapItem" + key)).remove();
                $("#ticketItemWrap").show();
                var item = ticketPool[key];
                ticketPool[key] = null;
                $("#ticketName").val(item.ticketName);
                $("#ticketPrice").val(item.ticketPrice);
                $("#ticketMax").val(item.ticketMax);
                $("#ticketDescribe").val(item.ticketDescribe);
            });
        });
    }
    $("#save").on("click", saveItem);
    
    //新建票种
    $("#create").click(function(e) {
        $("#ticketItemWrap").show();
    });
    //关闭
    $("#close").on("click", closeItem);
    function closeItem() {
        $("#ticketName").val('');
        $("#ticketPrice").val('');
        $("#ticketMax").val('');
        $("#ticketDescribe").val('');
        $("#ticketItemWrap").hide();
    }
    
    //提交
    $("#submit").on("click", function(e) {
        //activityId
        var activityId = Util.param.getActivityId();
        
        //ticketData
        var ticketArr = [];
        $.each(ticketPool, function(key, item) {
            if (!item) return;
            var one = [];
            $.each(item, function(index, single) {
                if (!single) return;
                one.push(single);
            });
            ticketArr.push(one.join("|"));
        });
        var ticketData = ticketArr.join();
        
        //cardData
        var gold = [$("#goldId").val(), $("#goldDiscount").val(), $("#goldScore").val()].join("|");
        var platinum = [$("#platinumId").val(), $("#platinumDiscount").val(), $("#platinumScore").val()].join("|");
        var black = [$("#blackId").val(), $("#blackDiscount").val(), $("#blackScore").val()].join("|");
        var cardData = [gold, platinum, black].join();
        
        //cardType
        var cardType = "0";
        var discountCheck = $("#discount").is(":checked");
        var scoreCheck = $("#score").is(":checked");
        if (discountCheck && !scoreCheck) {
            cardType = "1";
        }
        else if (!discountCheck && scoreCheck) {
            cardType = "2";
        }
        else if (discountCheck && scoreCheck) {
            cardType = "3";
        }
        
         //验证
        if (!activityId || !cardType || !ticketData 
            || !$("#goldId").val() || !$("#goldDiscount").val() || !$("#goldScore").val()
            || !$("#platinumId").val() || !$("#platinumDiscount").val() || !$("#platinumScore").val()
            || !$("#blackId").val() || !$("#blackDiscount").val() || !$("#blackScore").val()
            ) {
            $("#error").show();
            $("#error").html("表单不能为空或填写格式错误！");
            return;
        }
        $("#error").hide();
        $.ajax({
            type: "POST",
            url: "api/activity/sec",
            data: {
                "activityId":activityId,
                "cardType": cardType,
                "ticketData" : ticketData,
                "cardData": cardData
            }
        })
        .done(function(data) {
            if (data.success === "true") {
                if (isUpdate) {
                    window.location.href = "regForm.html?type=update&activityId=" + data.result.id;
                }
                else {
                    window.location.href = "regForm.html?activityId=" + data.result.id
                                         + "&createIndex=2";
                }
            }
            else {
                Util.input.whiteError($("#error"), data);
            }
        });
    });
    
    //获取卡片信息
    if (!isUpdate) {
        getDefaultCard();
    }
    function getDefaultCard() {
        $.ajax({
            type: "GET",
            url: "/api/companycard/"
        })
        .done(function( data ) {
            if(data.success === "false") return;
            var result = data.result;
            if (!result.length) return;
            $.each(result, function(index, item) {
                if (item.name == "金卡") {
                    $("#goldId").val(item.cardId);
                    $("#goldDiscount").val(item.discount);
                    $("#goldScore").val(item.coinNum);
                }
                else if (item.name == "白金卡") { 
                    $("#platinumId").val(item.cardId);
                    $("#platinumDiscount").val(item.discount);
                    $("#platinumScore").val(item.coinNum);
                }
                else if (item.name == "黑卡"){
                    $("#blackId").val(item.cardId);
                    $("#blackDiscount").val(item.discount);
                    $("#blackScore").val(item.coinNum);
                }
            });
           
        });
    }
})(jQuery);
