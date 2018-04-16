<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>j管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
                $("#btnExport").click(function(){
                    top.$.jBox.confirm("确认要导出用户数据吗？","系统提示",function(v,h,f){
                        if(v=="ok"){
                            $("#searchForm").attr("action","${ctx}/trans/transGoods/export?status=${transGoods.status}");
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
            $("#searchForm").attr("action","${ctx}/trans/transGoods/list?status=${transGoods.status}");
            $("#searchForm").submit();
            return false;

        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/trans/transGoods/">用户持仓列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="transGoods" action="${ctx}/trans/transGoods?status=${transGoods.status}&buyId=${transGoods.buyId}" method="post" class="breadcrumb form-search">

		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>明星编号：</label>
				<form:input path="groupId" htmlEscape="false" maxlength="13" class="input-medium"/>
			</li>
			<%--<li><label>产品编号：</label>--%>
				<%--<form:input path="groupId" htmlEscape="false" maxlength="5" class="input-medium"/>--%>
			<%--</li>--%>
			<li><label>用户名：</label>
				<form:input path="userName" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label>真实姓名：</label>
				<form:input path="trueName" htmlEscape="false" maxlength="255" class="input-medium"/>
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


				<%--<th>商品编号</th>--%>


				<%--<th>交易编号</th>--%>


				<th>明星编号</th>
					
				<th>明星名称</th>
					
				<th>用户名</th>

				<th>真实姓名</th>
					
				<th>状态</th>
					
				<%--<th>买入价格</th>--%>
					
				<%--<th>出售价格</th>--%>

				<th>持有秒数</th>
					
				<th>创建时间</th>
					
				<%--<th>出售时间</th>--%>



				<%--<th>备注</th>--%>

			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="transGoods">
			<c:if test="${transGoods.num != 0}">
			<tr>

				<%--<td>--%>
					<%--${transGoods.goodsNo}--%>
				<%--</a></td>--%>

				<%--<c:choose>--%>
					<%--<c:when test="${transGoods.status == 0}" >--%>
						<%--<td>${transGoods.buyId}</td>--%>
					<%--</c:when>--%>
					<%--<c:otherwise>--%>
						<%--<td>-</td>--%>
					<%--</c:otherwise>--%>
				<%--</c:choose>--%>


				<td>
						${transGoods.groupId}
				</td>
				<td>
					${transGoods.name}
				</td>
				<td>
					${transGoods.userName}
				</td>
				<td>
						${transGoods.trueName}
				</td>
				<td>
						${fns:getDictLabel(transGoods.status, 'qy_trans_status', '')}
				</td>
				<%--<td>--%>
					<%--${transGoods.buyMoney}--%>
				<%--</td>--%>
				<%--<td>--%>
					<%--${transGoods.sellMoney}--%>
				<%--</td>--%>
				<td>
					${transGoods.num}
				</td>
				<td>
					<fmt:formatDate value="${transGoods.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<%--<td>--%>
					<%--<fmt:formatDate value="${transGoods.sellTime}" pattern="yyyy-MM-dd HH:mm:ss"/>--%>
				<%--</td>--%>
				<%--<td>--%>
					<%--${transGoods.remarks}--%>
				<%--</td>--%>

			</tr>
			</c:if>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>