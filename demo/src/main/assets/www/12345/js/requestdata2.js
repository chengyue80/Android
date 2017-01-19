var baseURL="http://221.130.129.139:8888/webServer_bz";
//var baseURL="http://192.168.0.26:8080/webServer_bz";
//参数key  分页key
var keyPageNumber="pageNumber";
//ID key
var keyId="id";
var keyType="";
var pageNumber = 0;
var accessToktypeen = "";

var request={
 
 //创造json对象
 createParamJson:function(key,value){
 	var json={};
 	json[key]=value;
 	return json;
 },
  //用户提交信件
 addXJ:function(dataObj,funOk,funError,beforeFun,endFun){
	var requestURL = baseURL + "/app/addXJ";
	$.requestData(requestURL,dataObj,funOk,funError,beforeFun,endFun);
 }
}

 
