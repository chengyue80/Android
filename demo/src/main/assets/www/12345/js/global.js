// 等待加载PhoneGap
document.addEventListener("deviceready", onDeviceReady, false);
// PhoneGap加载完毕
function onDeviceReady() {
	// 按钮事件
	document.addEventListener("backbutton", eventBackButton, false); 		// 返回键
	
}

// 返回键
function eventBackButton() {
	var url = window.location.href;
	if(url.indexOf("index") != -1 && url.indexOf("#") < 0 ){
		//drawToast("再按一次退出程序");
		//document.removeEventListener("backbutton", eventBackButton, false); 	//注销返回键
		//document.addEventListener("backbutton", exitApp, false);//绑定退出事件
		//3秒后重新注册
		//var intervalID = window.setInterval(
		//function() {
		//	window.clearInterval(intervalID);
		//	document.removeEventListener("backbutton", exitApp, false); 		// 注销返回键
		//	document.addEventListener("backbutton", eventBackButton, false); 	// 返回键
		//},3000);
		exitApp();
	}else{
		history.go(-1);
	}
}

function exitApp(){
	navigator.app.exitApp();
}