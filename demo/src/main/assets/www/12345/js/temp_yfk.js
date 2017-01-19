function createYfkTemp(fkobj,num){
	var temp_yfk = '<li>'+
	'<label class="title1" for="t_lbl_fkdw"'+num+'>反馈单位：</label><label><span id="lbl_fkdw"'+num+'>'+fkobj.ext3+'</span></label><br/>'
	+'</li>'+
	'<li>'+
	'<label class="title1" for="t_lbl_fksj"'+num+'>反馈时间：</label><label><span id="lbl_fksj"'+num+'>'+fkobj.handledate+'</span></label><br/>'
	+'</li>'+
	'<li>'+
	'<label class="title1" for="t_lbl_fknr"'+num+'>反馈内容：</label><label><span id="lbl_fknr"'+num+'>'+fkobj.handlenote+'</span></label><br/>'
	+'</li>'+
	'<li>'+
	'<label class="title1" for="t_lbl_fknr"'+num+'>审核操作：</label>'+
		'<input type="radio" data-role="none" name="bj_shenghe'+num+'" id="bj_shenghe'+num+'" value="1" style="float:left;" onclick="shbj_ok()"><label for="bj_shenghe'+num+'" style="float:left;">同意办结</label>'+
		'<input type="radio" data-role="none" name="bj_shenghe'+num+'" id="bj_shenghe'+num+'" value="2" style="float:left;" onclick="shbj_back()"><label for="bj_shenghe'+num+'" style="float:left;">发回重办</label>'+
		'<input id="is_shenghe'+num+'" name="is_shenghe'+num+'" type="hidden" style="float:left;" value="'+fkobj.nibanid+'_'+fkobj.unitid+'">'
	+'</li>'+
	'<li>'+
	'<div data-role="fieldcontain">'+
	'<label class="title1" for="t_lbl_fknr"'+num+'>审核意见：</label><br/>'+
	'<textarea id="shenghe_note'+num+'" rows="4" cols="50" name="shenghe_note'+num+'" onchange="checkshnote(this.value)"></textarea>'+
	'<span style="color: red;">&nbsp;&nbsp;审核意见不能超过200字！</span></div>'
	+'</li>';
	return temp_yfk;
}

	
//检测办理评价
function checkhandlepj(){
	var cks=document.form1.handlepj;
	for(var i=0;i<cks.length;i++){
		if(cks[i].checked){
			return true;
		}
	}
	alert("请选择办理评价！");
	return false;
}

//办结提交
function bjSave() {
	jQuery.ajax( {
		type : 'POST',
		url : baseURL+"/app/dobj",
		data : $("#form1").serialize(),
		success : function(res) {
			if (res == "true") {
				alert("操作成功！");
			} else {
				alert("操作失败！");
			}
			return false;
		},
		fail : function(res) {
			alert("系统错误?!");
		}
	});
}

//提交
function subslj(){
	if(isbj_ok==0){
		alert("请先选择审核结果！");
		return;
	}
	if(isbj_ok==1){
		if(checkhfnote(document.form1.hfnote.value)){
			if(checkshenghe_note() && checkhandlepj()){
				getshjg();
				bjSave();
			}
		}
	}else if(isbj_ok==2){//发回重办
		if(checkshenghe_note()){
			getshjg();
			bjSave();
		}
	}
}

//获取审核结果
function getshjg(){
	var shengjg="";
	for(var i=0;i<fksize;i++){
		if(backs[i]==2){
			var shengheduixiang=document.getElementById('is_shenghe'+(i+1));
			if(shengjg=="")
				shengjg=shengheduixiang.value;
			else
				shengjg=shengjg+"<>"+shengheduixiang.value;
		}
	}
	$("#is_shenghe").val(shengjg);
}

function JTrim(s) {
	var r1, r2, s1, s2, s3;
	r1 = new RegExp("^ *");
	r2 = new RegExp(" *$");
	s1 = "" + s + "";
	s2 = s1.replace(r1, "");
	s3 = s2.replace(r2, "");
	r1 = null;
	r2 = null;
	return (s3);
}

//检测审批意见
function checkshenghe_note(){
	var shnote="";
	for(var i=0;i<fksize;i++){
		if(backs[i]==2){
			var shenghenote=document.getElementById('shenghe_note'+(i+1));
			if(shenghenote.value=="" || JTrim(shenghenote.value)==""){
				alert("请输入审核意见！");
				shenghenote.focus();
				return false;
			}else if(shenghenote.value.length>200){
				alert("审核意见不能超过200个字！");
				shenghenote.focus();
				return false;
			}
			if(shnote=="")
				shnote=shenghenote.value;
			else
				shnote=shnote+"<>"+shenghenote.value;
		}
	}
	$("#shenghenote").val(shnote);
	return true;
}