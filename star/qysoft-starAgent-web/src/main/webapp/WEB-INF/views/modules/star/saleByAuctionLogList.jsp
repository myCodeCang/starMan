<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>拍卖3管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/star/saleByAuctionLog/">拍卖纪录列表</a></li>
		<%--<shiro:hasPermission name="user:saleByAuctionLog:edit"><li><a href="${ctx}/star/saleByAuctionLog/form">拍卖3添加</a></li></shiro:hasPermission>--%>
	</ul>
	<%--<form:form id="searchForm" modelAttribute="saleByAuctionLog" action="${ctx}/star/saleByAuctionLog/" method="post" class="breadcrumb form-search">--%>
		<%--<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>--%>
		<%--<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>--%>
		<%--<ul class="ul-form">--%>
			<%--&lt;%&ndash;<li><label>拍卖编号：</label>&ndash;%&gt;--%>
				<%--&lt;%&ndash;<form:input path="auctionId" htmlEscape="false" maxlength="11" class="input-medium"/>&ndash;%&gt;--%>
			<%--&lt;%&ndash;</li>&ndash;%&gt;--%>
			<%--<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>--%>
			<%--<li class="clearfix"></li>--%>
		<%--</ul>--%>
	<%--</form:form>--%>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>

					
				<%--<th>id</th>--%>
					
				<th>拍卖编号</th>
					
				<th>竞拍价格</th>
					
				<th>竞拍状态</th>
					
				<th>竞拍机构编号</th>
					
				<%--<th>create_by</th>--%>
					<%----%>
					<%----%>
					<%----%>
				<th>竞拍时间</th>
					
				<%--<th>remarks</th>--%>
					
				<%--<shiro:hasPermission name="user:saleByAuctionLog:edit"><th>操作</th></shiro:hasPermission>--%>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="saleByAuctionLog">
			<tr>

				<%--<td><a href="${ctx}/star/saleByAuctionLog/form?id=${saleByAuctionLog.id}">--%>
					<%--${saleByAuctionLog.id}--%>
				<%--</a></td>--%>
				<td>
					${saleByAuctionLog.auctionId}
				</td>
				<td>
					${saleByAuctionLog.auctionMoney}
				</td>
				<td>
					${saleByAuctionLog.status=='1'?'竞拍成功':'竞拍失败'}
				</td>
				<td>
					${saleByAuctionLog.auctionUser}
				</td>
				<%--<td>--%>
					<%--${saleByAuctionLog.createBy.id}--%>
				<%--</td>--%>
				<td>
					<fmt:formatDate value="${saleByAuctionLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<%--<td>--%>
					<%--${saleByAuctionLog.remarks}--%>
				<%--</td>--%>
				<%--<shiro:hasPermission name="user:saleByAuctionLog:edit"><td>--%>
    				<%--<a href="${ctx}/star/saleByAuctionLog/form?id=${saleByAuctionLog.id}">修改</a>--%>
					<%--<a href="${ctx}/star/saleByAuctionLog/delete?id=${saleByAuctionLog.id}" onclick="return confirmx('确认要删除该拍卖3吗？', this.href)">删除</a>--%>
				<%--</td></shiro:hasPermission>--%>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>