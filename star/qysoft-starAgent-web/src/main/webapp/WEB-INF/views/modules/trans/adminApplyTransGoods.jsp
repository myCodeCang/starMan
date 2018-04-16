<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员充值管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
                    $("#btnSubmit").attr("disabled", true);
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
				},
                rules:{
                    changeMoney:{
                        required:true,
                        checkName:true,
                    },

                },
            });

            $.validator.addMethod("checkName",function(value,element,params){
                var checkName = /^(-?)([1-9]\d*|0)(\.\d{1,2})?$/g;
                return this.optional(element)||(checkName.test(value));
            },"*只能输入整数或两位小数！");
        });

        layui.use('layer', function() {
		var layer = layui.layer;
	 	}); 

        function verifyMoney() {
            var money = $("input[name='money']").val();
            if(money <= 0 ){
                layer.open({
                    title: '输入错误'
                    ,content: '请重新输入正确的单价'
                });
                $("input[name='money']").val("");
            }
            if($("input[name='num']").val()){
                verifyNum();
			}   
        }

        function verifyNum() {
            var reg = /^[1-9]\d*|0$/;
            var num = $("input[name='num']").val();
            var money = $("input[name='money']").val();
            if(!money){
                layer.open({
                    title: '输入错误'
                    ,content: '请先输入正确的单价'
                });
			}
            if(!reg.test(num) || isNaN(num)){
                layer.open({
                    title: '输入错误'
                    ,content: '请重新输入正确的数量'
                });
                $("input[name='num']").val("");
            }else{
                var cost = (money * num)+(num * 1);
                $("#cost").val(cost);
			}
        }

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="##">手动配发艺术品</a></li>
	</ul><br/>
	<form id="inputForm"  action="${ctx}/trans/transApply/adminApplyTransGoods" method="post" class="form-horizontal">
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">用户帐号：</label>
			<div class="controls">
				<sys:userinfoSelect id="userName"  title="用户选择" cssClass="required"  value="${userName}"/>
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
			<label class="control-label">单价：</label>
			<div class="controls">
				<input name="money" htmlEscape="false" maxlength="11" class="input-xlarge required" type="number" onchange="verifyMoney()"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">数量：</label>
			<div class="controls">
				<input name="num" htmlEscape="false" maxlength="11" class="input-xlarge required" type="number" onchange="verifyNum()"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">费用：</label>
			<div class="controls">
				<input id="cost" htmlEscape="false" maxlength="11" class="input-xlarge required"  type="text" readonly />
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="user:UserChargeLog:edit"><input id="btnSubmit"  class="btn btn-primary" type="submit" value="配  发"/>&nbsp;</shiro:hasPermission>

		</div>
	</form>
</body>
</html>