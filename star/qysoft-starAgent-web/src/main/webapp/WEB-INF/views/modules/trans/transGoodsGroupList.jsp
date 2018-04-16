<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>艺术品管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
                $("#btnExport").click(function(){
                    top.$.jBox.confirm("确认要导出用户数据吗？","系统提示",function(v,h,f){
                        if(v=="ok"){
                            $("#searchForm").attr("action","${ctx}/trans/transGoodsGroup/export");
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
            $("#searchForm").attr("action","${ctx}/trans/transGoodsGroup/list");
            $("#searchForm").submit();
            return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/trans/transGoodsGroup/">艺术品列表</a></li>
		<shiro:hasPermission name="user:transGoodsGroup:edit"><li><a href="${ctx}/trans/transGoodsGroup/form">艺术品添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="transGoodsGroup" action="${ctx}/trans/transGoodsGroup/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>艺术品编号：</label>
				<form:input path="id" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>艺术品名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/></li>
			<li class="btns"><input id="btnExport" class="btn btn-primary" type="button" value="导出"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>

				<th>艺术品编号</th>
					
				<th>艺术品名称</th>
					
				<th>标题</th>

				<th>状态</th>

				<th>创建时间</th>
					
				<shiro:hasPermission name="user:transGoodsGroup:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="transGoodsGroup">
			<tr>
				<td>
						${transGoodsGroup.id}
				</td>
				<td><a href="${ctx}/trans/transGoodsGroup/form?id=${transGoodsGroup.id}">
					${transGoodsGroup.name}
				</a></td>
				<td>
					${transGoodsGroup.title}
				</td>
				<td>
						${(transGoodsGroup.status == '1') ? "<span style='color: green'>上架</span>" : "<span style='color: red'>下架</span>"}

				</td>
				<td>
					<fmt:formatDate value="${transGoodsGroup.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>

				<shiro:hasPermission name="user:transGoodsGroup:edit"><td>
    				<a class="layui-btn layui-btn-mini layui-btn-normal" href="${ctx}/trans/transGoodsGroup/form?id=${transGoodsGroup.id}">修改</a>
					<a class="layui-btn layui-btn-mini layui-btn-normal" href="${ctx}/trans/transGoodsGroup/delete?id=${transGoodsGroup.id}" onclick="return confirmx('确认要删除该艺术品吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>