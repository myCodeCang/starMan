<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>交易历史价格管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
                $("#btnExport").click(function(){
                    top.$.jBox.confirm("确认要导出用户数据吗？","系统提示",function(v,h,f){
                        if(v=="ok"){
                            $("#searchForm").attr("action","${ctx}/trans/transBuyDayTrend/export");
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
            $("#searchForm").attr("action","${ctx}/trans/transBuyDayTrend/list");
            $("#searchForm").submit();
            return false;

        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/trans/transBuyDayTrend/">交易历史价格列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="transBuyDayTrend" action="${ctx}/trans/transBuyDayTrend/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>艺术品编号：</label>
				<form:input path="groupId" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>艺术品名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="11" class="input-medium"/>
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
					
				<th>当前价</th>
					
				<th>开盘价</th>
					
				<th>收盘价</th>
					
				<th>最高价</th>
					
				<th>最低价</th>
					
				<%--<th>交易量</th>--%>
					
					
					
				<th>创建时间</th>
					
					
					
				<th>备注</th>

				<shiro:hasPermission name="user:transApply:edit"><th>操作</th></shiro:hasPermission>

			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="transBuyDayTrend">
			<tr>

				<td>
					${transBuyDayTrend.groupId}
				</td>
				<td>
					${transBuyDayTrend.name}
				</td>
				<td>
					${transBuyDayTrend.nowMoney}
				</td>
				<td>
					${transBuyDayTrend.startMoney}
				</td>
				<td>
					${transBuyDayTrend.endMoney}
				</td>
				<td>
					${transBuyDayTrend.higMoney}
				</td>
				<td>
					${transBuyDayTrend.lowMoney}
				</td>
				<%--<td>--%>
					<%--${transBuyDayTrend.amount}--%>
				<%--</td>--%>
				<td>
					<fmt:formatDate value="${transBuyDayTrend.addDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${transBuyDayTrend.remarks}
				</td>

				<shiro:hasPermission name="user:transBuyDayTrend:edit"><td>
					<a class="layui-btn layui-btn-mini layui-btn-normal" href="${ctx}/trans/transBuyDayTrend/form?id=${transBuyDayTrend.id}">修改</a>
				</td></shiro:hasPermission>

			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>