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
                <div class="page-header-heading"><span class="am-icon-home page-header-heading-icon"></span> 交易记录
                    
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

                            <form:form id="searchForm" modelAttribute="mdTradeLog" action="${ctx}/user/transLog/" method="post" class="am-form tpl-form-border-form tpl-form-border-br am-form-inline">
                                <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                                <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                                <label  class="am-form-label">用户名: </label>
                                <div class="am-form-group am-form-icon ">
                                <form:input path="userName" htmlEscape="false" maxlength="255" class="input-medium"/>
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

                                    <th>交易用户名</th>

                                    <th>明星编号</th>

                                    <th>交易方向</th>

                                    <th>交易数量</th>

                                    <th>单价金额</th>

                                    <th>手续费</th>

                                    <th>描述信息</th>

                                    <th>创建时间</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${page.list}" var="mdTradeLog">
                                    <tr class="gradeX">
                                    <tr>

                                        <td>
                                                ${mdTradeLog.id}
                                        </td>
                                        <td>
                                                ${mdTradeLog.userName}
                                        </td>
                                        <td>
                                                ${mdTradeLog.groupId}
                                        </td>
                                        <td>
                                                ${fns:getDictLabel(mdTradeLog.type, 'md_type', '')}
                                        </td>
                                        <td>
                                                ${mdTradeLog.amount}
                                        </td>
                                        <td>
                                                ${mdTradeLog.price}
                                        </td>
                                        <td>
                                                ${mdTradeLog.profit}
                                        </td>
                                        <td>
                                                ${mdTradeLog.message}
                                        </td>
                                        <td>
                                            <fmt:formatDate value="${mdTradeLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
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
