<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>订货限制管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("input[name=id]").each(function(){
				var articleSelect = null;
				if (top.mainFrame.cmsMainFrame){
					articleSelect = top.mainFrame.cmsMainFrame.transApplyCondition;
				}else{
					articleSelect = top.mainFrame.transApplyCondition;
				}
				alert(articleSelect);
				for (var i=0; i<articleSelect.length; i++){
					if (articleSelect[i][0]==$(this).val()){
						this.checked = true;
					}
				}
				$(this).click(function(){
					alert(123132);
					var id = $(this).val();
					if (top.mainFrame.cmsMainFrame){
						top.mainFrame.cmsMainFrame.articleSelectAddOrDel(id);
					}else{
						top.mainFrame.articleSelectAddOrDel(id);
					}
				});
			});
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
		<li class="active"><a href="${ctx}/trans/transApplyCondition/">订货限制列表</a></li>
		<shiro:hasPermission name="user:transApplyCondition:edit"><li><a href="${ctx}/trans/transApplyCondition/form">订货限制添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="transApplyCondition" action="${ctx}/trans/transApplyCondition/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>订货表编号：</label>
				<form:input path="applyId" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>


				<th>选择</th>

				<th>订货表编号</th>

				<th>1:  订货需要持有某票</th>

				<th>需要持有票的group id</th>

				<th>需要持有票的数量</th>


				<th>创建时间</th>



				<th>备注</th>

				<shiro:hasPermission name="user:transApplyCondition:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="transApplyCondition">
			<tr>

				<td>
					<input type="checkbox" name="id" value="${transApplyCondition.id}" title="${fns:abbr(article.title,40)}" />
				</td>
				<td>
					${transApplyCondition.applyId}
				</td>
				<td>
					${transApplyCondition.type}
				</td>
				<td>
					${transApplyCondition.holdGroupId}
				</td>
				<td>
					${transApplyCondition.holdGroupNum}
				</td>
				<td>
					<fmt:formatDate value="${transApplyCondition.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${transApplyCondition.remarks}
				</td>
				<shiro:hasPermission name="user:transApplyCondition:edit"><td>
    				<a href="${ctx}/trans/transApplyCondition/form?id=${transApplyCondition.id}">修改</a>
					<a href="${ctx}/trans/transApplyCondition/delete?id=${transApplyCondition.id}" onclick="return confirmx('确认要删除该订货限制吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>