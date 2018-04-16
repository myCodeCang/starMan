<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>艺术品交易管理</title>
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
		<li class="active"><a href="${ctx}/trans/transBuy/">艺术品交易列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="transBuy" action="${ctx}/trans/transBuy/showlist?buyGroup=${transBuy.buyGroup}&show=1" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>交易编号：</label>
				<form:input path="id" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>售卖人名称：</label>
				<form:input path="sellUserName" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label>分组编号：</label>
				<form:input path="groupId" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>交易编号</th>
				<th>商品编号</th>

				<th>售卖人名称</th>

				<th>商品名称</th>
					
				<th>单价</th>
					
				<th>状态</th>

				<th>创建时间</th>
					
					

			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="transBuy">
			<tr>
				<td>
						${transBuy.buyGroup}
				</td>
				<td>
						${transBuy.goodsId}
				</td>
				<td>
					${transBuy.sellUserName}
				</td>

				<td>
					${transBuy.goodsName}
				</td>

				<td>
					${transBuy.money}
				</td>

				<td>
						${fns:getDictLabel(transBuy.status, 'qy_goods_status', '')}

				</td>
				<td>
					<fmt:formatDate value="${transBuy.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>

			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>