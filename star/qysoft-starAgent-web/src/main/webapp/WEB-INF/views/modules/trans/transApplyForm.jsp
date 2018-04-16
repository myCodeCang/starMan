<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>订货表管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}

			});
			if(${transApply.ifHoldGroupStatus}){
				$("#holdSwitch").attr("checked",true);
				$("#relationButton").show();
				$("#inputHoldGroupNum").show();

			}else{
				$("#holdSwitch").attr("checked",false);
				$("#relationButton").hide();
				$("#inputHoldGroupNum").hide();
			}
			if(${transApply.ifKuisunStatus}){
				$("#kuisunSwitch").attr("checked",true);
				$("#ifKuisunStatusDiv").show();
			}else{
				 $("#kuisunSwitch").attr("checked",false);
				 $("#ifKuisunStatusDiv").hide();
			}    
		});



	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/trans/transApply/">艺术品订货列表</a></li>
		<li class="active"><a href="${ctx}/trans/transApply/form?id=${transApply.id}">艺术品订货<shiro:hasPermission name="user:transApply:edit">${not empty transApply.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="user:transApply:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="transApply" action="${ctx}/trans/transApply/save" method="post" class="form-horizontal layui-form">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>


		<blockquote class="layui-elem-quote">基本信息</blockquote>

		<div class="control-group">
			<label class="control-label">标题：</label>
			<div class="controls">
				<form:input path="title" htmlEscape="false" maxlength="255" cssClass="required" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">艺术品名称：</label>
			<div class="controls">
				<sys:transGoodsGroup id="groupName" value="${transApply.transGoodsGroup.name}" labelValue="${transApply.groupId}" title="艺术品" cssClass="required"  />
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">开始时间：</label>
			<div class="controls">
				<input name="startTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required "
					value="<fmt:formatDate value="${transApply.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
					<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">结束时间：</label>
			<div class="controls">
				<input name="endTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required "
					value="<fmt:formatDate value="${transApply.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
					<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">总订货数量：</label>
			<div class="controls">
				<form:input path="allNum" htmlEscape="false" maxlength="11" class="input-xlarge required" />
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">剩余订货数量：</label>
			<div class="controls">
				<form:input path="nowNum" htmlEscape="false" maxlength="11" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">价格：</label>
			<div class="controls">
				<form:input path="money" htmlEscape="false" maxlength="11" class="input-xlarge required" type="number"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">每人最多可订货数量：</label>
			<div class="controls">
				<form:input path="maxnum" htmlEscape="false" maxlength="11" class="input-xlarge required" type="number"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">描述：</label>
			<div class="controls">
				<form:input path="message" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge"/>
			</div>
		</div>
		<form:hidden path="ifHoldGroupStatus"/>
		<form:hidden path="ifKuisunStatus"/>
		
		<blockquote class="layui-elem-quote">活动配置</blockquote>
		<style>
			#lables,#labless{margin-top:8px;}
		</style>
		<div class="control-group">
			<label class="control-label" id="lables">限制亏损申购：</label>
			<div class="controls">
				<input type="checkbox" lay-skin="switch" lay-text="开启|关闭" lay-filter="ifKuisunStatus" id="kuisunSwitch">
			</div>
		</div>

		<style>
			.layui-elem-field legend{
				border: none;
				width: inherit;
			}

		</style>
		<fieldset class="layui-elem-field" id="ifKuisunStatusDiv">
			<legend>限制亏损配置</legend>
			<div class="layui-field-box">
				<div class="control-group">
					<label class="control-label" >亏损金额配置：</label>
					<div class="controls">
						<form:input path="ifKuisunMoney" htmlEscape="false" maxlength="11" class="input-xlarge "/>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">亏损金额开始时间：</label>
					<div class="controls">
						<input name="ifKuisunBegin" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
							   value="<fmt:formatDate value="${transApply.ifKuisunBegin}" pattern="yyyy-MM-dd HH:mm:ss"/>"
							   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">亏损金额结束时间：</label>
					<div class="controls">
						<input name="ifKuisunEnd" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
							   value="<fmt:formatDate value="${transApply.ifKuisunEnd}" pattern="yyyy-MM-dd HH:mm:ss"/>"
							   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
					</div>
				</div>

			</div>
		</fieldset>


		<div class="control-group">
			<label class="control-label" id="labless">购买限制 - 指定文化产品 ：</label>
			<div class="controls">
				<input type="checkbox"   lay-skin="switch" lay-text="开启|关闭" lay-filter="holdGroupStatus" id="holdSwitch"/>
			</div>
		</div>
		<div style="display: none;" id="inputHoldGroupNum">
			<c:forEach items="${transApply.contiditonList}" var="contiditon">
				<div id= "oldid_${contiditon.id}"><div class="control-group">
  				  <input type="hidden" name="contiditonId" value="${contiditon.id}" />
  				  <input type="hidden" name="contiditonStatus" value="0" id="statusid_${contiditon.id}"/>
				        <label class="control-label">限制商品编号:</label>
				        <div class="controls">
				            <input type="text" name="contiditonGroupId"
				                   value="${contiditon.holdGroupId}" htmlEscape="false"
				                   maxlength="20" class="input-xlarge required" readonly style="width: 60px;"/> 
				           &nbsp;限制数量: <input type="text" name="contiditonNum"
				           value="${contiditon.holdGroupNum}" htmlEscape="false"
				           maxlength="20" class="input-xlarge" onchange="return checkme(this)" style="width: 35px;" />
				           &nbsp;<a  class="layui-btn layui-btn-mini layui-btn-normal" onclick="oldDelete(${contiditon.id})">删除</a>
				            </div></div></div>
								
			</c:forEach>
		</div>
		<a id="relationButton" href="javascript:" class="btn"  style="display: none;" cssClass="required">添加相关交易品</a>
		<div class="form-actions">
			<shiro:hasPermission name="user:transApply:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>

	<script>
		//Demo
		layui.use('form', function(){
			var form = layui.form();

			//监听提交
			form.on('submit(formDemo)', function(data){
				layer.msg(JSON.stringify(data.field));
				return false;
			});
		});
	</script>
<script type="text/html" id="tpl_hold_group">

    <div><div class="control-group">
    <input type="hidden" id="contiditonId" />
        <label class="control-label">限制商品Id</label>
        <div class="controls">
            <input type="text" name="contiditonId"
                   value="55" htmlEscape="false"
                   maxlength="20" class="input-xlarge required" readonly/> 
           <input type="text" name="contiditonNum"
           value="66" htmlEscape="false"
           maxlength="20" class="input-xlarge required"/> 
            </div></div></div>

</script>

<script type="application/javascript">
var index = 0;
	layui.use(['form'], function(){
		var form = layui.form();

		form.on('switch(ifKuisunStatus)', function(data){
			if(data.elem.checked){
				$("#ifKuisunStatus").val("1");
				$("#kuisunSwitch").attr("checked",true);
				$("#ifKuisunStatusDiv").show();
			}
			else{
				$("#ifKuisunStatus").val("0");
				$("#kuisunSwitch").attr("checked",false);
				$("#ifKuisunStatusDiv").hide();
			}
		});

		form.on('switch(holdGroupStatus)', function(data){
			if(data.elem.checked){
				$("#ifHoldGroupStatus").val("1");
				$("#holdSwitch").attr("checked",true);
				$("#relationButton").show();
				$("#inputHoldGroupNum").show();

			}
			else{
				$("#holdSwitch").attr("checked",false);
				$("#ifHoldGroupStatus").val("0");
				$("#relationButton").hide();
				$("#inputHoldGroupNum").hide();
			}

		});

	});

	

	function oldDelete(id){
		$("#oldid_"+id).remove();
	}

	function newDelete(id){
		$("#newid_"+id).remove();
	}
	function checkme(obj){
		var reg = /^[1-9]\d*|0$/;
		var val = obj.value;
		
	        if(!reg.test(val) || isNaN(val)){
	     		layer.open({
					  title: '输入错误'
					  ,content: '请重新输入正确的数量'
					}); 
	     		obj.value = 1;
	     	}

	}
	$("#relationButton").click(function(){
		top.$.jBox.open("iframe:${ctx}/trans/transGoodsGroup/selectTrans?pageSize=8", "选择${title}", 750, 500, {
			ajaxData:{selectIds: ""},buttons:{"确定":"ok", ${allowClear?"\"清除\":\"clear\", ":""}"关闭":true}, submit:function(v, h, f){

				if (v=="ok"){
					var table = h.find("iframe")[0].contentWindow.searchUserTable;
					var oldId = $("input[name='contiditonGroupId']");
					var oldStatus = $("input[name='contiditonStatus']");
					var id =  $(table).find(".selected").attr('data');
					for (var i = 0; i < oldId.length; i++) {
						if(oldId[i].value == id && oldStatus[i].value == 0){
							layer.open({
							  title: '选择错误'
							  ,content: '请不要重复添加一件艺术品'
							}); 
							return;
						}
					}
					index ++;
					var result ='';
						result += '<div><div class="control-group" id="newid_'+index+'">';
						result += '<input type="hidden" name="contiditonId" value="0"/>';
                    	result += '<input type="hidden" name="contiditonStatus" value="0"/>';
						result += '<label class="control-label">限制商品编号:</label>';
						result += '<div class="controls">';
                        result += '<input type="text" name="contiditonGroupId" value="'+id+'" htmlEscape="false"maxlength="20" class="input-xlarge required" readonly style="width: 60px;"/>'; 
                       result += ' &nbsp;限制数量: <input type="text" name="contiditonNum" value="1" htmlEscape="false" maxlength="20" class="input-xlarge required" onchange="return checkme(this)" style="width: 35px;" />';
                       result += ' &nbsp;<a  class="layui-btn layui-btn-mini layui-btn-normal" onclick="newDelete('+index+')">删除</a>';
                       result += ' </div></div></div>';
            		$("#inputHoldGroupNum").append(result);
					//$("#articleSelectList").val(username);
				}
				else if (v=="clear"){
					$("#${id}").val("");
				}
				if(typeof ${id}TreeselectCallBack == 'function'){
					${id}TreeselectCallBack(v, h, f);
				}
			}, loaded:function(h){
				$(".jbox-content", top.document).css("overflow-y","hidden");
			}
		});
	});

</script>
</body>
</html>