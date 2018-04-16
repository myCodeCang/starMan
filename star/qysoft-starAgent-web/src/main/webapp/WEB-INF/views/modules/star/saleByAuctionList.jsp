<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>拍卖产品管理</title>
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
		<li class="active"><a href="${ctx}/star/saleByAuction/">拍卖产品列表</a></li>
		<shiro:hasPermission name="user:saleByAuction:edit"><li><a href="${ctx}/star/saleByAuction/form">拍卖产品添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="saleByAuction" action="${ctx}/star/saleByAuction/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>拍卖编号：</label>
				<form:input path="id" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>拍卖状态：</label>
				<form:select path="status" class="input-medium">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('qy_auction_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>

					
				<th>拍卖产品编号</th>
					
				<th>明星产品</th>
					
				<th>产品时间数</th>
					
				<th>拍卖价格</th>
					
				<th>标准竞拍价</th>
					
				<th>开始时间</th>
					
				<th>结束时间</th>
					
				<th>竞拍状态</th>
					
				<th>当前竞拍人</th>

				<th>添加时间</th>
					
				<%--<th>remarks</th>--%>

				<shiro:hasPermission name="user:saleByAuction:edit"><th style="width:300px;">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="saleByAuction">
			<tr>

				<td>
					${saleByAuction.id}
				</td>
				<td>
					${saleByAuction.baseGoodsGroup.name}
				</td>
				<td>
					${saleByAuction.num}
				</td>
				<td>
					${saleByAuction.money}
				</td>
				<td>
					${saleByAuction.normMoney}
				</td>
				<td>
					<fmt:formatDate value="${saleByAuction.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${saleByAuction.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
						${fns:getDictLabel(saleByAuction.status,'qy_auction_status','')}
				</td>
				<td>
					${saleByAuction.succeedUser}
				</td>
				<td>
					<fmt:formatDate value="${saleByAuction.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<%--<td>--%>
					<%--${saleByAuction.remarks}--%>
				<%--</td>--%>
				<shiro:hasPermission name="user:saleByAuction:edit"><td>
					<a  class="layui-btn layui-btn-mini layui-btn-normal"  onclick="viewComment('${ctx}/star/saleByAuctionLog/list?auctionId=${saleByAuction.id}')">拍卖纪录</a>
					<a  class="layui-btn layui-btn-mini layui-btn-normal"  onclick="viewComment('${ctx}//star/auctionGuarantee/list?auctionId=${saleByAuction.id}')">保证金</a>
					<c:if test="${saleByAuction.status == 0}">
						<a class="layui-btn layui-btn-mini layui-btn-normal"  href="${ctx}/star/saleByAuction/auctionSuccess?id=${saleByAuction.id}" onclick="return confirmx('确认要停止该拍卖吗？', this.href)">终止</a>
						<a class="layui-btn layui-btn-mini layui-btn-normal" href="${ctx}/star/saleByAuction/form?id=${saleByAuction.id}">修改</a>
						<a class="layui-btn layui-btn-mini layui-btn-normal"  href="${ctx}/star/saleByAuction/delete?id=${saleByAuction.id}" onclick="return confirmx('确认要删除该拍卖吗？', this.href)">删除</a>
					</c:if>

				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
		<script type="text/javascript">
            function viewComment(href){
                top.$.jBox.open('iframe:'+href,'查看详情',$(top.document).width()-220,$(top.document).height()-180,{
                    buttons:{"关闭":true},
                    loaded:function(h){
                        $(".jbox-content", top.document).css("overflow-y","hidden");
                        $(".nav,.form-actions,[class=btn]", h.find("iframe").contents()).hide();
                        $("body", h.find("iframe").contents()).css("margin","10px");
                    }
                });
                return false;
            }
		</script>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>