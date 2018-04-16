<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/website/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta name="decorator" content="cms_default_${site.theme}"/>
    <meta name="description" content="JeeSite ${site.description}" />
    <meta name="keywords" content="JeeSite ${site.keywords}" />

    <title>会员网络图</title>


</head>
<body>

<!-- 内容区域 -->
<div class="tpl-content-wrapper">

    <div class="widget-head am-cf">
        <div class="widget-body am-fr">

            <form:form id="searchForm" modelAttribute="userUserinfo" action="${ctx}/user/userGrid/" method="post" class="am-form tpl-form-border-form tpl-form-border-br am-form-inline">
                <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>

                <label  class="am-form-label">账号: </label>
                <div class="am-form-group am-form-icon ">
                    <form:input path="userName" htmlEscape="false" maxlength="255" class="input-medium"/>
                </div>

                <label  class="am-form-label">手机号: </label>
                <div class="am-form-group am-form-icon ">
                    <form:input path="mobile" htmlEscape="false" maxlength="255" class="input-medium"/>
                </div>

                <div class="am-form-group am-form-icon ">
                    <button type="submit"  id="btnSubmit" class="am-btn am-btn-primary  am-input-sm tpl-btn-bg-color-success ">查询</button>
                </div>

            </form:form>
            <sys:messageWeb content="${message}"/>
        </div>

    </div>


    <div id="chart-container">
    </div>


</div>


<script >


    (function($){

        $(function() {

            var datascource = ${treeMap};

            var ajaxURLs = {
                'children': function(nodeData){
                    return '${ctx}/user/userGridChildren?id=' + nodeData.id;
                },
                'parent': function(nodeData){
                    return '${ctx}/user/userGridParent?id=' + nodeData.id;
                },
                'siblings': function(nodeData) {
                    return '${ctx}/user/userGridSiblings?id=' + nodeData.id;
                },
                'families': function(nodeData) {
                    return '${ctx}/user/userGridFamilies?id=' + nodeData.id;
                }
            };

            $('#chart-container').orgchart({
                'data' : datascource,
                'nodeContent': 'title',
                'nodeId': 'id',
                'ajaxURL': ajaxURLs,
                'pan': true,
                'zoom': true
            });
        });

    })(jQuery);
</script>


</body>


</html>



<%--<html>--%>
<%--<head>--%>
    <%--<title>会员网络图</title>--%>
    <%--<meta name="decorator" content="default"/>--%>

    <%--<link href="${ctxStatic}/OrgChart/css/font-awesome.min.css" rel="stylesheet" />--%>
    <%--<link href="${ctxStatic}/OrgChart/css/jquery.orgchart.css" rel="stylesheet" />--%>

    <%--<script src="${ctxStatic}/jquery/jquery-3.1.0.min.js" type="text/javascript"></script>--%>
    <%--<script src="${ctxStatic}/OrgChart/js/jquery.orgchart.js" type="text/javascript"></script>--%>

    <%--<script type="text/javascript">--%>
        <%--$(document).ready(function() {--%>

        <%--});--%>

    <%--</script>--%>
<%--</head>--%>
<%--<body>--%>
<%--<ul class="nav nav-tabs">--%>
    <%--<li class="active"><a href="${ctx}/user/userGrid/">会员网络图</a></li>--%>
<%--</ul>--%>
<%--<form:form id="searchForm" modelAttribute="userUserinfo" action="${ctx}/user/userUserinfo/userGrid/" method="post" class="breadcrumb form-search">--%>
    <%--<ul class="ul-form">--%>
        <%--<li><label>账号：</label>--%>
            <%--<form:input path="userName" htmlEscape="false" maxlength="255" class="input-medium"/>--%>
        <%--</li>--%>
        <%--<li><label>手机号：</label>--%>
            <%--<form:input path="mobile" htmlEscape="false" maxlength="50" class="input-medium"/>--%>
        <%--</li>--%>
        <%--<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>--%>
        <%--<li class="clearfix"></li>--%>
    <%--</ul>--%>
<%--</form:form>--%>
<%--<sys:message content="${message}"/>--%>



<%--</body>--%>
<%--</html>--%>