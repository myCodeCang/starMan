<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>订货表管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
                $("#btnExport").click(function(){
                    top.$.jBox.confirm("确认要导出用户数据吗？","系统提示",function(v,h,f){
                        if(v=="ok"){
                            $("#searchForm").attr("action","${ctx}/trans/transApply/export");
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
            $("#searchForm").attr("action","${ctx}/trans/transApply/list");
            $("#searchForm").submit();
            return false;

        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/trans/transApply/">艺术品订货列表</a></li>
		<shiro:hasPermission name="user:transApply:edit"><li><a href="${ctx}/trans/transApply/form">艺术品订货添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="transApply" action="${ctx}/trans/transApply/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>标题：</label>
				<form:input path="title" htmlEscape="false" maxlength="255" class="input-medium"/>
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

					
				<th>编号</th>
					
				<th>标题</th>
					
				<th>艺术品编号</th>

				<th>艺术品名称</th>
					
				<th>总订货数量</th>
					
				<th>剩余订货数量</th>
					
				<th>状态</th>

				<th>亏损积分活动</th>

				<th>订货持票活动</th>

				<th>描述</th>

				<th>开始时间</th>

				<th>结束时间</th>

				<shiro:hasPermission name="user:transApply:edit"><th style="padding-right:100px;">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="transApply">
			<tr>

				<td><a href="${ctx}/trans/transApply/form?id=${transApply.id}">
					${transApply.id}
				</a></td>
				<td>
					${transApply.title}
				</td>
				<td>
					${transApply.groupId}
				</td>
				<td>
				    ${transApply.transGoodsGroup.name}
				</td>

				<td>
					${transApply.allNum}
				</td>
				<td>
					${transApply.nowNum}
				</td>
				<td>
						${fns:getDictLabel(transApply.status, 'qy_start_end', '')}
				</td>
				<td>
						${fns:getDictLabel(transApply.ifKuisunStatus, 'qy_trans_switch', '')}
				</td>
				<td>
						${fns:getDictLabel(transApply.ifHoldGroupStatus, 'qy_trans_switch', '')}
				</td>
				<td>
					${transApply.message}
				</td>
				<td>
					<fmt:formatDate value="${transApply.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${transApply.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="user:transApply:edit"><td>
    				<a class="layui-btn layui-btn-mini layui-btn-normal" href="${ctx}/trans/transApply/form?id=${transApply.id}">修改</a> 
					<a class="layui-btn layui-btn-mini layui-btn-normal" href="${ctx}/trans/transApply/delete?id=${transApply.id}" onclick="return confirmx('确认要删除该订货表吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>