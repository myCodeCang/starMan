<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>交易品管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
                $("#btnExport").click(function(){
                    top.$.jBox.confirm("确认要导出用户数据吗？","系统提示",function(v,h,f){
                        if(v=="ok"){
                            $("#searchForm").attr("action","${ctx}/trans/transGoods/exports?order=${transGoodsKuiSun.order}");
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
            $("#searchForm").attr("action","${ctx}/trans/transGoods/userKunSunRanklist?order=${transGoodsKuiSun.order}");
            $("#searchForm").submit();
            return false;

        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<c:if test="${transGoodsKuiSun.order==0}">
		<li class="active"><a href="##">亏损列表</a></li>
		</c:if>
		<c:if test="${transGoodsKuiSun.order==1}">
		<li class="active"><a href="##">盈利列表</a></li>
		</c:if>
	</ul>
	<form:form id="searchForm" modelAttribute="transGoodsKuiSun" action="${ctx}/trans/transGoods/userKunSunRanklist?oreder=${transGoodsKuiSun.order}" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>用户名：</label>
				<form:input path="userName" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label>出售时间：</label>
				<input name="beginSelltime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${transGoodsKuiSun.beginSelltime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endSelltime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${transGoodsKuiSun.endSelltime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
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
									
				<th>用户名</th>

				<th>真实姓名</th>
					
				<th>买入艺术品价格总计</th>
					
				<th>出售艺术品价格总计</th>

				<c:if test="${transGoodsKuiSun.order==0}">
					<th>亏损金额</th>
				</c:if>
				<c:if test="${transGoodsKuiSun.order==1}">
					<th>盈利金额</th>
				</c:if>				

			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="transGoods">
			<tr>
				<td>
					${transGoods.userName}
				</td>

				<td>
					${transGoods.trueName}
				</td>

				<td>
					${transGoods.buyMoney}
				</td>
				<td>
					${transGoods.sellMoney}
				</td>
				<td>${transGoods.kuiSunMoney}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>