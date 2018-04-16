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
        <div class="row">+
            <div class="am-u-sm-12 am-u-md-12 am-u-lg-9">
                <div class="page-header-heading"><span class="am-icon-home page-header-heading-icon"></span> 下级会员成交额
                    
                </div>
            </div>

        </div>

    </div>



    <div class="row-content am-cf">


        <div class="row">
            <div class="am-u-sm-12 am-u-md-12 am-u-lg-12">
                <div class="widget am-cf">
                    <div class="widget-head am-cf">
                        <div class="widget-body am-fr">

                            <form:form id="searchForm" modelAttribute="mdTradeLog" action="${ctx}/user/teamVolume/" method="post" class="am-form tpl-form-border-form tpl-form-border-br am-form-inline">
                                <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                                <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                                <label  class="am-form-label">用户名: </label>
                                <div class="am-form-group am-form-icon ">
                                <form:input path="userName" htmlEscape="false" maxlength="255" class="input-medium"/>
                                </div>
                                <label  class="am-form-label">日期: </label>
                                <div class="am-form-group am-form-icon ">
                                    <input name="createDateBegin" type="date"  htmlEscape="false" maxlength="100" class="input-medium" value="<fmt:formatDate value="${createDateBegin}" pattern="yyyy-MM-dd"/>"/>
                                </div>
                                --
                                <div class="am-form-group am-form-icon">
                                    <input name="createDateEnd" type="date"  htmlEscape="false" maxlength="100" class="input-medium" value="<fmt:formatDate value="${createDateEnd}" pattern="yyyy-MM-dd"/>"/>
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
                                    <th>用户名</th>
                                    <th>真实姓名</th>
                                    <th>号段</th>
                                    <th>交易额</th>
                                    <th>代理等级</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${page.list}" var="mdTradeLog">
                                    <tr class="gradeX">
                                    <tr>

                                        <td>
                                                ${mdTradeLog.userName}
                                        </td>
                                        <td>
                                                ${mdTradeLog.trueName}
                                        </td>
                                        <td>
                                                ${mdTradeLog.shopId}
                                        </td>
                                        <td>
                                            <fmt:formatNumber type="number" value="${mdTradeLog.money6}" pattern="0.00" maxFractionDigits="2"/>

                                        </td>
                                        <td>
                                                ${mdTradeLog.userType==0?"普通会员":mdTradeLog.userType==1?"二级代理":mdTradeLog.userType==2?"一级代理":"运营中心"}
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
