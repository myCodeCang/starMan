<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>会员奖金明细管理</title>
    <meta name="decorator" content="default" />
    <script type="text/javascript">
        $(document).ready(function() {});
        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }
    </script>
</head>
<body>
<ul class="nav nav-tabs">
    <li class="active"><a href="##">艺术品交易次数统计</a></li>
    <!-- <shiro:hasPermission name="user:userReport:edit"><li><a href="${ctx}/user/userReport/form">会员奖金明细添加</a></li></shiro:hasPermission> -->
</ul>
<form:form id="searchForm" modelAttribute="transGoodsNumReport"
           action="${ctx}/trans/transGoods/transGoodsNumReport" method="post"
           class="breadcrumb form-search">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
    <input id="pageSize" name="pageSize" type="hidden"
           value="${page.pageSize}" />
    <ul class="ul-form">
        <li><label>艺术品名称：</label><sys:transGoodsGroup id="groupName" value="${transGoodsNumReport.groupName}" labelValue="${transGoodsNumReport.groupId}" title="艺术品" />
        </li>
        
        <li class="btns"><input id="btnSubmit" class="btn btn-primary"
                                type="submit" value="查询" /></li>
        <li class="clearfix"></li>
    </ul>
</form:form>
<form:form id="inputForm" modelAttribute="goodsNum" action="" method="post" class="form-horizontal">
    <sys:message content="${message}"/>
    <div class="control-group">
        <label class="control-label">商品总数：</label>
        <div class="controls">
            <form:input path="goodsNum" htmlEscape="false" maxlength="255" class="input-xlarge" readonly="true" />

        </div>
    </div>
    <div class="control-group">
        <label class="control-label">提货个数：</label>
        <div class="controls">
            <form:input path="pickNum" htmlEscape="false" maxlength="255" class="input-xlarge" readonly="true" />

        </div>
    </div>
    <div class="control-group">
        <label class="control-label">交易0次个数：</label>
        <div class="controls">
            <form:input path="zero" htmlEscape="false" maxlength="255" class="input-xlarge" readonly="true" />

        </div>
    </div>
    <div class="control-group">
        <label class="control-label">交易1次个数：</label>
        <div class="controls">
            <form:input path="one" htmlEscape="false" maxlength="255" class="input-xlarge" readonly="true" />

        </div>
    </div>
    <div class="control-group">
        <label class="control-label">交易2次个数：</label>
        <div class="controls">
            <form:input path="two" htmlEscape="false" maxlength="255" class="input-xlarge" readonly="true" />

        </div>
    </div>
    <div class="control-group">
        <label class="control-label">交易3次个数：</label>
        <div class="controls">
            <form:input path="three" htmlEscape="false" class="input-xlarge" readonly="true" />

        </div>
    </div>
    <div class="control-group">
        <label class="control-label">交易4次个数：</label>
        <div class="controls">
            <form:input path="four" htmlEscape="false" maxlength="8" class="input-xlarge" readonly="true" />

        </div>
    </div>
    <div class="control-group">
        <label class="control-label">交易5次个数：</label>
        <div class="controls">
            <form:input path="five" htmlEscape="false" maxlength="8" class="input-xlarge" readonly="true" />

        </div>
    </div>
    <div class="control-group">
        <label class="control-label">交易多次个数：</label>
        <div class="controls">
            <form:input path="more" htmlEscape="false" maxlength="8" class="input-xlarge" readonly="true" />

        </div>
    </div>
    <div class="control-group">
        <label class="control-label">持仓均价：</label>
        <div class="controls">
            <form:input path="avePrice" htmlEscape="false" maxlength="8" class="input-xlarge" readonly="true" />

        </div>
    </div>
    
</form:form>
<div class="pagination">${page}</div>
</body>
</html>