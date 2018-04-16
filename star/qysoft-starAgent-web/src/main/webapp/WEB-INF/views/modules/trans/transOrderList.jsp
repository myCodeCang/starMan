<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>产品兑换管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
                $("#btnExport").click(function(){
                    top.$.jBox.confirm("确认要导出用户数据吗？","系统提示",function(v,h,f){
                        if(v=="ok"){
                            $("#searchForm").attr("action","${ctx}/trans/transOrder/export");
                            $("#searchForm").submit();
                        }
                    },{buttonsFocus:1});
                    top.$('.jbox-body .jbox-icon').css('top','55px');
                });
            $("#btnImport").click(function(){
                $.jBox($("#importBox").html(), {title:"导入数据", buttons:{"关闭":true},
                    bottomText:"导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！"});
            });
		});
		function page(n,s){
            if(n) $("#pageNo").val(n);
            if(s) $("#pageSize").val(s);
            $("#searchForm").attr("action","${ctx}/trans/transOrder/list");
            $("#searchForm").submit();
            return false;

        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/trans/transOrder/">产品兑换列表</a></li>
		
	</ul>
	<form:form id="searchForm" modelAttribute="transOrder" action="${ctx}/trans/transOrder/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>用户名：</label>
				<form:input path="userName" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label>手机号：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label>兑换人姓名：</label>
				<form:input path="consignee" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/></li>
			<%--<li class="btns"><input id="btnExport" class="btn btn-primary" type="button" value="导出"/></li>--%>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
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

				<shiro:hasPermission name="user:transOrder:edit"><th>操作</th></shiro:hasPermission>
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
						<input class="btn btn-primary" type="button" value="同意" onclick="return confirmx('确定要同意本次消费吗?','${ctx}/trans/transOrder/updateType?id=${transOrder.id}&type=1')"/>
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
	<div class="pagination">${page}</div>
	<script type="text/javascript">
        layui.use('qyframe', function() {
            var qyframe = layui.qyframe;
        });
	</script>
</body>
</html>