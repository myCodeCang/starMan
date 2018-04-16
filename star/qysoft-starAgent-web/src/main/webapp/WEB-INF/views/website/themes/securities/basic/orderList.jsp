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
                <div class="page-header-heading"><span class="am-icon-home page-header-heading-icon"></span> 兑换记录
                    
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

                            <form:form id="searchForm" modelAttribute="transOrder" action="${ctx}/user/findOrderList/" method="post" class="am-form tpl-form-border-form tpl-form-border-br am-form-inline">
                                <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                                <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                                <label  class="am-form-label">手机号：</label>
                                <div class="am-form-group am-form-icon ">
                                <form:input path="mobile" htmlEscape="false" maxlength="255" class="input-medium"/>
                                </div>
                                <label  class="am-form-label">兑换人姓名：</label>
                                <div class="am-form-group am-form-icon ">
                                    <form:input path="consignee" htmlEscape="false" maxlength="255" class="input-medium"/>
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

                                    <th>明星编号</th>

                                    <th>产品名称</th>

                                    <th>消费时间</th>

                                    <th>手机号</th>

                                    <th>兑换人姓名</th>
                                    <th>兑换时间</th>

                                    <%--<th>收货地址</th>--%>
                                    <%----%>
                                    <%--<th>邮编</th>--%>

                                    <th>订单状态</th>

                                   <th>操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${page.list}" var="transOrder">
                                    <tr>
                                        <td>
                                                ${transOrder.id}
                                        </td>
                                        <td>

                                                ${transOrder.userName}
                                        </td>
                                        <td>
                                                ${transOrder.groupId}
                                        </td>
                                        <td>
                                                ${transOrder.goodsName}
                                        </td>
                                        <td>
                                                ${transOrder.num}
                                        </td>
                                        <td>
                                                ${transOrder.mobile}
                                        </td>
                                        <td>
                                                ${transOrder.consignee}
                                        </td>
                                        <td>
                                            <fmt:formatDate value="${transOrder.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>

                                        </td>
                                            <%--<td>--%>
                                            <%--${transOrder.address}--%>
                                            <%--</td>--%>
                                            <%--<td>--%>
                                            <%--${transOrder.postCode}--%>
                                            <%--</td>--%>
                                        <td>
                                                ${fns:getDictLabel(transOrder.type, 'qy_shoporder_type', '')}
                                        </td>
                                        <td>
                                            <c:if test="${transOrder.type==0}">
                                                <a class="am-btn am-btn-primary  am-input-sm tpl-btn-bg-color-success" href="javascript:void(0)" onclick="setOrderType(${transOrder.id})" class="list" style="background:none;color:#fff;border-color:#fff;">同意</a>
                                            </c:if>
                                            <c:if test="${transOrder.type==1}">
                                                已消费
                                            </c:if>
                                            <c:if test="${transOrder.type==2}">
                                                消费取消
                                            </c:if>

                                        </td>
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
    var saveFlag = false;
    $(document).ready(function() {

    });
    function page(n,s){
        $("#pageNo").val(n);
        $("#pageSize").val(s);
        $("#searchForm").submit();
        return false;
    }

    layui.use(['layer','qyForm','qyWin'], function(){
        var layer = layui.layer;

    });
    
    function setOrderType(id) {
        if(saveFlag){
            return;
        }
        layui.layer.open({
            title:"同意消费",
            content:"您确定要同意本次消费吗?",
            btn:["确定","取消"],
            yes:function(index){
                saveFlag = true;
                layui.qyForm.qyajax("${ctx}/user/updateOrderType",{"id":id},function (data) {
                    layui.qyWin.toast(data.info);
                    setTimeout("location.reload()",1000);
                    saveFlag = false;
                },function (ret) {
                    saveFlag = false;
                });
                layui.layer.close(index);

            }
        });

    }
    

</script>

</body>
</html>
