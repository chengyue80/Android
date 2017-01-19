var baseURL="http://221.130.129.139:8888/webServer_bz";
//var baseURL="http://192.168.0.26:8080/webServer_bz";
//参数key  分页key
var keyPageNumber="pageNumber";
//ID key
var keyId="id";
var keyType="type";
var pageNumber = 0;
var accessToken = "";


var request={
 
 //创造json对象
 createParamJson:function(key,value){
 	var json={};
 	json[key]=value;
 	return json;
 },
 
 
 //首页
 main:function(pageNumber,funOk,funError,beforeShow,endShow){
	var requestURL = baseURL + "/app/main";
	var params = request.createParamJson("pageNumber",pageNumber);
	$.requestData(requestURL,params,funOk,funError,beforeShow,endShow);
 },
 
 //首页详细
 mainDetail:function(id,funOk,funError){
	var requestURL = baseURL + "/app/mainDetail";
	var params = request.createParamJson("id",id);
	$.requestData(requestURL,params,funOk,funError);
 },
 
 //受理件列表
 sljList:function(pageNumber,funOk,funError,beforeShow,endShow){
	var requestURL = baseURL + "/app/SljList";
	var params = request.createParamJson("pageNumber",pageNumber);
	$.requestData(requestURL,params,funOk,funError,beforeShow,endShow);
 },
 
 //受理件详情查询
 sljinfo:function(id,funOk,funError){
	var requestURL = baseURL + "/app/SljInfo";
	var params = request.createParamJson("id",id);
	$.requestData(requestURL,params,funOk,funError);
 },
 
 //部门联系方式
 unitContact:function(funOk,funError){
	var requestURL = baseURL + "/app/unitContact";
	$.requestData(requestURL,"",funOk,funError);
 },
 
 //受理件类型
 sljType:function(funOk,funError){
	var requestURL = baseURL + "/app/sljType";
	$.requestData(requestURL,"",funOk,funError);
 },
 
 //添加网络受理件
 saveSlj:function(dataObj,funOk,funError){
	var requestURL = baseURL + "/app/saveSlj";
	$.requestData(requestURL,dataObj,funOk,funError);
 },
 
 //我的12345受理件
 mySlj:function(pageNumber,userloginTel,funOk,funError,beforeShow,endShow){
	var requestURL = baseURL + "/app/myShow";
	var params = request.createParamJson("pageNumber",pageNumber);
	params["loginname"]=userloginTel;
	$.requestData(requestURL,params,funOk,funError,beforeShow,endShow);
 },
 
 //保存评价消息
 saveEvaluate:function(dataObj,funOk,funError){
	var requestURL = baseURL + "/app/evaluate";
	$.requestData(requestURL,dataObj,funOk,funError);
 }

}