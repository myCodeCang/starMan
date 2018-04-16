<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/website/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>首页</title>
    <meta name="decorator" content="cms_default_${site.theme}"/>
    <meta name="description" content="JeeSite ${site.description}"/>
    <meta name="keywords" content="JeeSite ${site.keywords}"/>

</head>
<body >

<!-- 内容区域 -->

<div class="tpl-content-wrapper">

    <div class="container-small am-cf">
        <div class="row">
            <div class="am-u-sm-12 am-u-md-12 am-u-lg-9">
                <div class="page-header-heading"><span class="am-icon-home page-header-heading-icon"></span> 交易中查詢
                    
                </div>
            </div>

        </div>

    </div>


    <style>
        .theme-black .tpl-form-border-form option{
            color:#000;
        }
    </style>
    <div class="row-content am-cf">


        <div class="row">
            <div class="am-u-sm-12 am-u-md-12 am-u-lg-12">
                <div class="widget am-cf">
                    <div class="widget-head am-cf">
                        <div class="widget-body am-fr">

                            <form:form id="searchForm" modelAttribute="mdTrade" action="${ctx}/user/transingLog" method="post" class="am-form tpl-form-border-form tpl-form-border-br am-form-inline">
                                <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                                <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                                <%--<label  class="am-form-label">用户名: </label>--%>
                                <%--<div class="am-form-group am-form-icon ">--%>
                                <%--<form:input path="userName" htmlEscape="false" maxlength="255" class="input-medium"/>--%>
                                <%--</div>--%>
                                <label>交易类型：</label>
                                <div class="am-form-group am-form-icon ">
                                    <form:select path="type" class="input-medium" style="width:50px;">
                                        <form:option value="0" label="全部"/>
                                        <form:options items="${fns:getDictList('md_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                    </form:select>
                                </div>
                                <label>状态:</label>
                                <div class="am-form-group am-form-icon ">
                                    <form:select path="state" class="input-medium" style="width:80px;">
                                        <form:option value="0" label="全部"/>
                                        <form:options items="${fns:getDictList('md_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                    </form:select>
                                </div>
                                <div class="am-form-group am-form-icon ">
                                    <button type="submit"  id="btnsubmit" class="am-btn am-btn-primary  am-input-sm tpl-btn-bg-color-success ">查询</button>
                                </div>
                            </form:form>
                            <sys:messageWeb content="${message}"/>
                        </div>

                    </div>
                    <div class="widget-body  widget-body-lg am-fr">
                        <div class="am-scrollable-horizontal ">
                            <table width="100%"  class="am-table am-table-compact am-table-bordered am-table-radius am-table-striped tpl-table-black "
                                   id="example-r">
                                <thead>
                                <tr>

                                    <th>id</th>

                                    <th>明星编号</th>

                                    <%--<th>用户id</th>--%>

                                    <th>用户名</th>

                                    <th>单价</th>

                                    <th>发布数量</th>

                                    <th>剩余数量</th>

                                    <th>交易类型</th>

                                    <%--<th>购买保证金</th>--%>

                                    <th>状态</th>

                                    <th>发布日期</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${page.list}" var="mdTrade">
                                    <tr class="gradeX">
                                    <tr>

                                        <td>
                                                ${mdTrade.id}
                                        </td>
                                        <td>
                                                ${mdTrade.groupId}
                                        </td>
                                        <%--<td>--%>
                                                <%--${mdTrade.userId}--%>
                                        <%--</td>--%>
                                        <td>
                                                ${mdTrade.userName}
                                        </td>
                                        <td>
                                                ${mdTrade.price}
                                        </td>
                                        <td>
                                                ${mdTrade.publishNum}
                                        </td>
                                        <td>
                                                ${mdTrade.remainNum}
                                        </td>
                                        <td>
                                                ${fns:getDictLabel(mdTrade.type, 'md_type', '')}
                                        </td>
                                        <%--<td>--%>
                                                <%--${mdTrade.bond}--%>
                                        <%--</td>--%>
                                        <td>
                                                ${fns:getDictLabel(mdTrade.state, 'md_status', '')}
                                        </td>
                                        <td>
                                            <fmt:formatDate value="${mdTrade.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </td>
                                    </tr>
                                    </tr>

                                </c:forEach>

                                </tbody>
                            </table>

                            <style>
                                .am-pagination>li{margin-right:0;}
                                .am-pagination>li>a{color:#fff;background:none;}
                                .am-pagination>.am-disabled>a{color:#fff;background:none;}
                                .am-pagination>.am-active>a{border-color:#fff;background:none;}
                                .am-pagination>li:last-child>a{background:none;border:none;}
                                .am-pagination>li>a>input{width:20px;border:none;background:none; }
                            </style>
                            <div class="pagination">${page}</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>



    </div>

</div>

<script type="text/javascript">
    $(document).ready(function() {

    });
    function page(n,s){
        $("#pageNo").val(n);
        $("#pageSize").val(s);
        $("#searchForm").submit();
        return false;
    }

    layui.use(['layer','qyForm'], function(){
        var layer = layui.layer;

    });
</script>

</body>
</html>
