//获取用户信息 
/*var userloginTel;
function getUserInfo(){
	cordova(success,null,"UserService","getUserInfo",["abb51d434015fabc91009d7364bb79f7"]);
}

function success(userInfo){
	userloginTel=userInfo.phoneNumber;
}*/

/**
*v_url  			访问地址 
*o_data 			传递参数
*successFun 		访问成功调用的函数
*errorFun 			错误函数
*beforeSendFun 		ajax请求之前方法
*completeFun	 	ajax请求之后方法
**/
jQuery.requestData=function(v_url,o_data,successFun,errorFun,beforeSendFun,completeFun){
	 $.ajax({
	             type: "post",	             
				 async: false,
	             url:v_url,
				 data:o_data,
				 contentType:"application/x-www-form-urlencoded;charset=UTF-8",
	             dataType:"jsonp",
	             jsonp: "callback",	//传递给请求处理程序或页面的，用以获得jsonp回调函数名的参数名(一般默认为:callback)
				 //jsonpCallback:"itemsSearchGet",//自定义的jsonp回调函数名称，默认为jQuery自动生成的随机函数名，也可以写"?"，jQuery会自动为你处理数据
	             error: (errorFun !== null && errorFun != undefined) ? errorFun: function (req) {
						alert("request error!");
				}, 
				success:(successFun !== null && successFun != undefined) ? successFun: function (req){
					
				}, 
				beforeSend:(beforeSendFun !== null && beforeSendFun != undefined) ? beforeSendFun: function (jqXHR){
					showLoader();
				}, 
				complete:(completeFun !== null && completeFun != undefined) ? completeFun: function (jqXHR,textStatus){
					hideLoader();
				}
			});
}

//模仿android 的 taost 功能
jQuery.toast=function(msg){
	$("<div class='ui-loader ui-overlay-shadow ui-body-e ui-corner-all'><h3>"+msg+"</h3></div>")
	.css({ display: "block", 
		opacity: 0.90, 
		position: "fixed",
		padding: "7px",
		"text-align": "center",
		width: "270px",
		left: ($(window).width() - 284)/2,
		top: $(window).height()/2 })
		.appendTo( $.mobile.pageContainer ).delay( 1500 )
		.fadeOut( 400, function(){
			$(this).remove();
		});
}

//显示加载信息
function showLoader() {  
    //显示加载器.for jQuery Mobile 1.2.0  
    $.mobile.loading('show', {  
        text: '加载中...', //加载器中显示的文字  
        textVisible: true, //是否显示文字  
        theme: 'a',        //加载器主题样式a-e  
        textonly: false,   //是否只显示文字  
        html: ""           //要显示的html内容，如图片等  
    });  
}

//隐藏加载器.for jQuery Mobile 1.2.0  
function hideLoader() {  
    //隐藏加载器  
    $.mobile.loading('hide');
}

//用户登录，信息本地存储
function localStorageUser(loginName,userPass,accessToken,unitId){
	localStorage.clear();
	localStorage.setItem("loginName", loginName);
	localStorage.setItem("userPass", userPass);
	localStorage.setItem("accessToken",accessToken);
	localStorage.setItem("unitId",unitId);
}

//用户注销
function logout(){
	localStorage.removeItem("loginName");
	localStorage.removeItem("userPass");
	localStorage.removeItem("accessToken");
	parent.window.location.href = "login.html";
}
		

/**json转换成String**/
function json2str(o) { 
	var arr = []; 
	var fmt = function(s) { 
	if (typeof s == 'object' && s != null) return json2str(s); 
		return /^(string|number)$/.test(typeof s) ? "'" + s + "'" : s; 
	} 
	for (var i in o) arr.push("'" + i + "':" + fmt(o[i])); 
		return '{' + arr.join(',') + '}'; 
}

/**获取地址栏参数**/
function getUrlParam(string) {  
		var obj =  new Array();  
		if (string.indexOf("?") != -1) {  
			var string = string.substr(string.indexOf("?") + 1); 
			var strs = string.split("&");  
			for(var i = 0; i < strs.length; i ++) {  
				var tempArr = strs[i].split("=");  
				obj[i] = tempArr[1];
			}  
		}  
		return obj;  
}

/*受理件性质*/
function xzStr(xz){
	var xzStr = "";
	if(xz==1){
		xzStr = "投诉";
	}else if(xz==2){
		xzStr = "咨询";
	}else if(xz==3){
		xzStr = "建议";
	}else if(xz==4){
		xzStr = "举报";
	}else if(xz==5){
		xzStr = "转检查";
	}else if(xz==6){
		xzStr = "反映";
	}else if(xz==7){
		xzStr = "其它";
	}else if(xz==8){
		xzStr = "求助";
	}else{
		xzStr = "无";
	}
	return xzStr;
}

/**返回**/
function goBack(){
	history.go(-1);
}

var intervalCounter = 0;
//toast隐藏
function hideToast(){
    var alert = document.getElementById("toast");
    alert.style.opacity = 0;
    clearInterval(intervalCounter);
}
//toast加载
function drawToast(message){
    var dig = document.getElementById("toast");
    if (dig == null){
        var toastHTML = '<div id="toast" >' + message + '</div>';
        document.body.insertAdjacentHTML('beforeEnd', toastHTML);
    }else{
		$("#toast").text(message);
        dig.style.opacity = .9;
    }
    intervalCounter = setInterval("hideToast()", 2000);
}

function formToJson(data) {
     data=data.replace(/&/g,"\",\"");
     data=data.replace(/=/g,"\":\"");
     data="{\""+data+"\"}";
     return data;
}


//选中OBJ中所有的先项
function selectAll(checkobj, obj) {
	if (checkobj.checked) {
		if (obj.length == null) {
			$('#'+obj.id).attr('checked',true).checkboxradio("refresh");
		} else {
			for (tmp = 0; tmp < obj.length; tmp++) {
				$('#'+obj[tmp].id).attr('checked',true).checkboxradio("refresh");
			}
		}
	} else {
		if (obj.length == null) {
			$('#'+obj.id).attr('checked',false).checkboxradio("refresh");
		} else {
			for (tmp = 0; tmp < obj.length; tmp++) {
				$('#'+obj[tmp].id).attr('checked',false).checkboxradio("refresh");
			}
		}
	}
}


Date.prototype.format = function(format) {  
    /* 
     * eg:format="yyyy-MM-dd hh:mm:ss"; 
     */  
	if(!format||format==null||format=='') format="yyyy-MM-dd hh:mm:ss";
    var o = {  
        "M+" : this.getMonth() + 1, // month  
        "d+" : this.getDate(), // day  
        "h+" : this.getHours(), // hour  
        "m+" : this.getMinutes(), // minute  
        "s+" : this.getSeconds(), // second  
        "q+" : Math.floor((this.getMonth() + 3) / 3), // quarter  
        "S" : this.getMilliseconds()  
        // millisecond  
    };  
  
    if (/(y+)/.test(format)) {  
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4  
                        - RegExp.$1.length));  
    }  
  
    for (var k in o) {  
        if (new RegExp("(" + k + ")").test(format)) {  
            format = format.replace(RegExp.$1, RegExp.$1.length == 1  
                            ? o[k]  
                            : ("00" + o[k]).substr(("" + o[k]).length));  
        }  
    }  
    return format;  
};