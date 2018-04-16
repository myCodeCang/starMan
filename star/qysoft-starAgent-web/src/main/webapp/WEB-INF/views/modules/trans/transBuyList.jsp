<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>产品交易</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
                $("#btnExport").click(function(){
                    top.$.jBox.confirm("确认要导出用户数据吗？","系统提示",function(v,h,f){
                        if(v=="ok"){
                            $("#searchForm").attr("action","${ctx}/trans/transBuy/export");
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
            $("#searchForm").attr("action","${ctx}/trans/transBuy/list");
            $("#searchForm").submit();
            return false;

        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/trans/transBuy/">产品交易列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="transBuy" action="${ctx}/trans/transBuy/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>交易编号：</label>
				<form:input path="id" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>售卖人：</label>
				<form:input path="sellUserName" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label>产品编号：</label>
				<form:input path="groupId" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>类型</label>
				<form:select path="type" class="input-medium">
					<form:option value="" label="全部"/>
					<form:options items="${fns:getDictList('qy_trans_buy')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
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

				<th>交易编号</th>

				<th>售卖人用户名</th>

				<th>真实姓名</th>

				<th>产品编号</th>
					
				<th>产品名称</th>
					
				<th>交易类型</th>

				<th>商品状态</th>

				<th>单价</th>
					
				<th>售卖数量</th>

				<th>剩余数量</th>

				<th>下架数量</th>

				<th>创建时间</th>
					
					
				<%--<shiro:hasPermission name="user:transBuy:edit"><th>操作</th></shiro:hasPermission>--%>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="transBuy">
			<tr>

				<td>
					${transBuy.id}
				</td>
				<td>
						${transBuy.sellUserName}
				</td>

				<td>
						${transBuy.trueName}
				</td>
				<td>
						${transBuy.groupId}
				</td>

				<td>
						${transBuy.goodsName}
				</td>
				<td>
						${fns:getDictLabel(transBuy.type, 'qy_trans_buy', '')}
				</td>
				<td>
						${fns:getDictLabel(transBuy.status, 'qy_goods_status', '')}
				</td>
				<td>
					${transBuy.money}
				</td>
				<td>
					${transBuy.sellNum}
				</td>
				<td>
						${transBuy.nowNum}
				</td>
					<td>
							${transBuy.downNum}
					</td>

				<td>
					<fmt:formatDate value="${transBuy.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<%--<shiro:hasPermission name="user:transBuy:view"><td>--%>

					<%--<div class="controls">--%>
						<%--<input class="layui-btn layui-btn-mini layui-btn-normal"--%>
							   <%--type="button" value="详情" onclick="viewComment('${ctx}/trans/transGoods/list?buyId=${transBuy.id}')"/>--%>
						<%--<script type="text/javascript">--%>
                            <%--function viewComment(href){--%>
                                <%--top.$.jBox.open('iframe:'+href,'查看详情',$(top.document).width()-220,$(top.document).height()-180,{--%>
                                    <%--buttons:{"关闭":true},--%>
                                    <%--loaded:function(h){--%>
                                        <%--$(".jbox-content", top.document).css("overflow-y","hidden");--%>
                                        <%--$(".nav,.form-actions,[class=btn]", h.find("iframe").contents()).hide();--%>
                                        <%--$("body", h.find("iframe").contents()).css("margin","10px");--%>
                                    <%--}--%>
                                <%--});--%>
                                <%--return false;--%>
                            <%--}--%>
						<%--</script>--%>
					<%--</div>--%>
				<%--</td></shiro:hasPermission>--%>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>