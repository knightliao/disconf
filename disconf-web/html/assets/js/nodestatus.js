getSession();

$.getUrlParam = function (name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}


var configId  = $.getUrlParam('configId');

fetchConfigUsage();

//
// 渲染主列表
//
function fetchConfigUsage() {
	
	$.ajax({
	    type: "GET",
	    url: "/api/web/config/zk/" + configId
	}).done(
	    function (data) {
	        if (data.success === "true") {
	            var result = data.result;
	            
	            var e = $(this);
	            $("#nodestatusbody").html("");
	            $("#nodestatusbody").html(getMachineList(result.datalist,configId));
	      
	        }
	 });

}

/**
 * @param result
 * @returns {String}
 */
function getMachineList(machinelist,configId) {

    var tip;
    if (machinelist.length == 0) {
        tip = "";
    } else {
        for (var i = 0; i < machinelist.length; i++) {
            var item = machinelist[i];
            var flag = "正常";
            var style = "";
            if (item.errorList.length != 0) {
	                flag = "未同步";
                style = "text-error";
            }

            if( flag  ==  "未同步"){
            	var state =  '未同步' ;
            	var opt =  '<a id="brand_url" onClick="nodeSysn(' + '\'' + configId +'\',\''+item.machine+ '\'' + ')" href="javascript:void(0)" > 点击同步</a>'
            }else{
            	var opt = ''
            }
            
            tip += '<tr><td><pre>' + item.machine +  ' </pre></td><td><pre> '
                +   flag  + '</pre></td><td>'+opt+'</td></tr>';
        }
        tip += '</table></div>';
    }
    return tip;
}

function nodeSysn(configId,machineName){
	 
	$.ajax({
	    type: "POST",
	    url: "/api/web/config/nodeUpdate/",
	    data:{
	    	configId:configId,
	    	machineName:machineName
	    }
	}).done(
	    function (data) {
	        if (data.success === "true") {
	            var result = data.result;
	            alert(data.result)
	            fetchConfigUsage()
	      
	        }
	 });
	
	
}

 