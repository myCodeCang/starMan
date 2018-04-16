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
                <div class="page-header-heading"><span class="am-icon-home page-header-heading-icon"></span> 账变明细
                    
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

                            <form:form id="searchForm" modelAttribute="userAccountchange" action="${ctx}/user/teamAcount/" method="post" class="am-form tpl-form-border-form tpl-form-border-br am-form-inline">
                                <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                                <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                                <%--<label>用户名：</label>--%>
                                    <%--<div class="am-form-group am-form-icon ">--%>
                                    <%--<form:input path="userName" htmlEscape="false" maxlength="100" class="input-medium"/>--%>
                                    <%--</div>--%>
                                <label>账变类型</label>
                                    <div class="am-form-group am-form-icon ">
                                    <form:select path="changeType" class="input-medium">
                                        <form:option value="" label="全部"/>
                                        <form:options items="${fns:getDictList('change_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                    </form:select>
                                    </div>
                                <label>消费类型</label>
                                    <div class="am-form-group am-form-icon ">
                                    <form:select path="moneyType" class="input-medium" style="width:80px;">
                                        <form:option value="" label="全部"/>
                                        <form:options items="${fns:getDictList('money_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                    </form:select>
                                    </div>
                                <label  class="am-form-label">日期: </label>
                                <div class="am-form-group am-form-icon ">
                                    <input name="createDateBegin" type="date"  htmlEscape="false" maxlength="100" class="input-medium" value="<fmt:formatDate value="${userAccountchange.createDateBegin}" pattern="yyyy-MM-dd"/>"/>
                                </div>
                                --
                                <div class="am-form-group am-form-icon">
                                    <input name="createDateEnd" type="date"  htmlEscape="false" maxlength="100" class="input-medium" value="<fmt:formatDate value="${userAccountchange.createDateEnd}" pattern="yyyy-MM-dd"/>"/>
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
                                    <th>编号</th>
                                    <th>用户名</th>
                                    <th>真实姓名</th>
                                    <th>号段</th>
                                    <th>账变类型</th>
                                    <th>消费类型</th>
                                    <th>账变金额</th>
                                    <th>账变前金额</th>
                                    <th>账变后金额</th>
                                    <th>创建时间</th>
                                    <th width="220">备注</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${page.list}" var="userAccountchange">
                                    <tr class="gradeX">
                                    <tr>
                                        <td>
                                                ${userAccountchange.id}
                                        </td>
                                        <td>
                                                ${userAccountchange.userName}
                                        </td>
                                        <td>
                                                ${userAccountchange.userUserinfo.trueName}
                                        </td>
                                    <td>
                                            ${userAccountchange.userUserinfo.shopId}
                                    </td>
                                        <td>
                                                ${fns:getDictLabel(userAccountchange.changeType, 'change_type', '')}

                                        </td>
                                        <td>
                                                ${fns:getDictLabel(userAccountchange.moneyType, 'money_type', '')}

                                        </td>
                                        <td>
                                                ${userAccountchange.changeMoney}
                                        </td>
                                        <td>
                                                ${userAccountchange.beforeMoney}
                                        </td>
                                        <td>
                                                ${userAccountchange.afterMoney}
                                        </td>
                                        <td>
                                            <fmt:formatDate value="${userAccountchange.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </td>
                                        <td>
                                                ${userAccountchange.commt}
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
