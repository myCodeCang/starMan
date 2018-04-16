<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>转让成交记录</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
                $("#btnExport").click(function(){
                    top.$.jBox.confirm("确认要导出用户数据吗？","系统提示",function(v,h,f){
                        if(v=="ok"){
                            $("#searchForm").attr("action","${ctx}/trans/transBuyLog/export");
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
            $("#searchForm").attr("action","${ctx}/trans/transBuyLog/list");
            $("#searchForm").submit();
            return false;

        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/trans/transBuyLog/">转让成交记录</a></li>
		<%--<shiro:hasPermission name="user:transBuyLog:edit"><li><a href="${ctx}/trans/transBuyLog/form">交易记录添加</a></li></shiro:hasPermission>--%>
	</ul>
	<form:form id="searchForm" modelAttribute="transBuyLog" action="${ctx}/trans/transBuyLog/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>用户名：</label>
				<form:input path="userName" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label>对方用户名：</label>
				<form:input path="transUserName" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label>产品编号：</label>
				<form:input path="groupId" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>

				<li><label>类型</label>
					<form:select path="type" class="input-medium">
						<form:option value="" label="全部"/>
						<form:options items="${fns:getDictList('trans_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<th>产品编号</th>

				<th>产品名称</th>
					
				<th>用户名</th>

				<th>真实姓名</th>

				<th>交易对方用户名</th>

				<th>交易对方真实姓名</th>

				<th>交易数量</th>
					
				<th>交易单价</th>
					
				<th>类型</th>

				<th>创建时间</th>

				<th>描述信息</th>
				<%--<th>操作</th>--%>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="transBuyLog">
			<tr>

				<c:choose>
					<c:when test="${transBuyLog.type != 3}" >
						<td>${transBuyLog.buyId}</td>
					</c:when>
					<c:otherwise>
						<td>-</td>
					</c:otherwise>
				</c:choose>

				<td>
						${transBuyLog.groupId}
				</td>
				<td>
						${transBuyLog.goodsName}
				</td>
				<td>
					${transBuyLog.userName}
				</td>
				<td>
						${transBuyLog.trueName}
				</td>
				<td>
					${transBuyLog.transUserName}
				</td>
				<c:choose>
					<c:when test="${transBuyLog.type != 3}" >
						<td>${transBuyLog.transUserTrueName}</td>
					</c:when>
					<c:otherwise>
						<td>-</td>
					</c:otherwise>
				</c:choose>
				<td>
					${transBuyLog.num}
				</td>
				<c:choose>
					<c:when test="${transBuyLog.type != 3}" >
						<td>
							<c:if test="${transBuyLog.money  < '0'}">
								${transBuyLog.money*(-1)}
							</c:if>
							<c:if test="${transBuyLog.money  > '0'}">
								${transBuyLog.money}
							</c:if>
						</td>
					</c:when>
					<c:otherwise>
						<td>-</td>
					</c:otherwise>
				</c:choose>


				<td>
					${fns:getDictLabel(transBuyLog.type, 'trans_type', '')}
				</td>
				<td>
					<fmt:formatDate value="${transBuyLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
						${transBuyLog.message}
				</td>
				<%--<c:choose>--%>
					<%--<c:when test="${transBuyLog.type != 3}" >--%>
						<%--<shiro:hasPermission name="user:transBuyLog:view"><td>--%>

							<%--<div class="controls" style="${transBuyLog.buyId == 0?'display:none;':'display:block'}">--%>
								<%--<input class="layui-btn layui-btn-mini layui-btn-normal"--%>
									   <%--type="button" value="详情" onclick="viewComment('${ctx}/trans/transGoods/list?buyId=${transBuyLog.buyId}')" />--%>
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
					<%--</c:when>--%>
					<%--<c:otherwise>--%>
						<%--<td></td>--%>
					<%--</c:otherwise>--%>
				<%--</c:choose>--%>

			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>