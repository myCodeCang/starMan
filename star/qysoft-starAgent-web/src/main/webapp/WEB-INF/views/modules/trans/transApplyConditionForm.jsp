<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>订货限制管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function () {
            //$("#name").focus();
            $("#inputForm").validate({
                submitHandler: function (form) {
                    loading('正在提交，请稍等...');
                    form.submit();
                },
                errorContainer: "#messageBox",
                errorPlacement: function (error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
        });
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li><a href="${ctx}/trans/transApplyCondition/">订货限制列表</a></li>
    <li class="active"><a
            href="${ctx}/trans/transApplyCondition/form?id=${transApplyCondition.id}">订货限制<shiro:hasPermission
            name="user:transApplyCondition:edit">${not empty transApplyCondition.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission
            name="user:transApplyCondition:edit">查看</shiro:lacksPermission></a></li>
</ul>
<br/>
<form:form id="inputForm" modelAttribute="transApplyCondition" action="${ctx}/trans/transApplyCondition/save"
           method="post" class="form-horizontal">
    <form:hidden path="id"/>
    <sys:message content="${message}"/>
    <div class="control-group">
        <label class="control-label">订货表编号：</label>
        <div class="controls">
            <form:input path="applyId" htmlEscape="false" maxlength="11" class="input-xlarge "/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">1: 订货需要持有某票：</label>
        <div class="controls">
            <form:input path="type" htmlEscape="false" maxlength="1" class="input-xlarge "/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">需要持有票的group id：</label>
        <div class="controls">
            <form:input path="holdGroupId" htmlEscape="false" maxlength="11" class="input-xlarge "/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">需要持有票的数量：</label>
        <div class="controls">
            <form:input path="holdGroupNum" htmlEscape="false" maxlength="11" class="input-xlarge "/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">remarks：</label>
        <div class="controls">
            <form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
        </div>
    </div>
    <div class="form-actions">
        <shiro:hasPermission name="trans:transApplyCondition:edit"><input id="btnSubmit" class="btn btn-primary"
                                                                          type="submit"
                                                                          value="保 存"/>&nbsp;</shiro:hasPermission>
        <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
    </div>
</form:form>
</body>
</html>