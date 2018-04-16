<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
            $("#btnExport").click(function(){
                top.$.jBox.confirm("确认要导出用户数据吗？","系统提示",function(v,h,f){
                    if(v=="ok"){
                        $("#searchForm").attr("action","${ctx}/user/userUserinfo/userInfoExport");
                        $("#searchForm").submit();
                    }
                },{buttonsFocus:1});
                top.$('.jbox-body .jbox-icon').css('top','55px');
            });
		});
		function page(n,s){

            if(n) $("#pageNo").val(n);
            if(s) $("#pageSize").val(s);
            $("#searchForm").attr("action","${ctx}/user/userUserinfo");
            $("#searchForm").submit();
            return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/user/userUserinfo/">会员列表</a></li>
		<%--<shiro:hasPermission name="user:userUserinfo:edit"><li><a href="${ctx}/user/userUserinfo/form">会员添加</a></li></shiro:hasPermission>--%>
	</ul>
	<form:form id="searchForm" modelAttribute="userUserinfo" action="${ctx}/user/userUserinfo/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>账号：</label>
				<form:input path="userName" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label>手机号：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>真实姓名：</label>
				<form:input path="trueName" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>经纪人：</label>
				<form:input path="remarks" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>推荐人账号：</label>
				<form:input path="parentName" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<li><label>注册时间：</label>
				<input name="beginAddtime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					   value="<fmt:formatDate value="${userUserinfo.beginAddtime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> -
				<input name="endAddtime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					   value="<fmt:formatDate value="${userUserinfo.endAddtime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>

			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page()"/></li>
			<li class="btns"><input id="btnExport" class="btn btn-primary" type="button" value="导出"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>编号</th>
				<th>账号</th>
				<th>号段</th>
				<th>手机号</th>
				<th>真实姓名</th>
				<%--<th>昵称</th>--%>
				<th>用户代理等级</th>
				<th>经纪人</th>
				<%--<th>是否升级</th>--%>
				<th>出金账户余额</th>
				<th>资金账户余额</th>
				<th>保证金</th>
				<th>积分</th>
				<th>推荐人</th>
				<th>创建时间</th>
				<shiro:hasPermission name="user:userUserinfo:edit"><th style="">操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="userUserinfo">
			<tr>
				<td>
					${userUserinfo.id}
				</td>
				<td><a href="${ctx}/user/userUserinfo/form?id=${userUserinfo.id}">
					${userUserinfo.userName}
				</a></td>

				<td>
					${userUserinfo.shopId}
				</td>
				<td>
					${userUserinfo.mobile}
				</td>
				<td>
					${userUserinfo.trueName}
				</td>
				<%--<td>--%>
					<%--${userUserinfo.userNicename}--%>
				<%--</td>--%>
				<td>
						${userUserinfo.areaId}
				</td>

				<td>
						${empty userUserinfo.remarks?"--":userUserinfo.remarks}
				</td>
				<%--<td>--%>
					<%--${fns:getDictLabel(userUserinfo.activeStatus, 'yes_no', '')}--%>
					<%----%>
				<%--</td>--%>

				<td>
					${userUserinfo.money}
				</td>
				<td>
						${userUserinfo.money2}
				</td>
				<td>
						${userUserinfo.money3}
				</td>
				<td>
					${userUserinfo.score}
				</td>
				<td>
						${userUserinfo.walterName}
				</td>
				<td>
					<fmt:formatDate value="${userUserinfo.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="user:userUserinfo:edit"><td>
    				<a href="${ctx}/user/userUserinfo/form?id=${userUserinfo.id}" style="display:inline-block;float: left;" class="layui-btn layui-btn-mini layui-btn-normal">修改</a>
					<c:if test="${userUserinfo.isUsercenter=='0' && userUserinfo.userType==0 && userUserinfo.id > 1}">
						<a href="${ctx}/user/userUserinfo/updateUserType?id=${userUserinfo.id}" style="display:inline-block;float: left;" class="layui-btn layui-btn-mini layui-btn-normal" onclick="openlevel('报单中心升级','升级级别','${userUserinfo.trueName}',[{'label':'运营中心','value':'3','select':'1'},{'label':'一级代理','value':'2'},{'label':'二级代理','value':'1'}],'${userUserinfo.userType}',this.href);return  false;">设置代理等级</a>
					</c:if>
					<%--<a href="${ctx}/user/userUserinfo/delete?id=${userUserinfo.id}" onclick="return confirmx('确认要删除该会员吗？', this.href)" style="display:inline-block;float: left;" class="layui-btn layui-btn-mini layui-btn-normal">删除</a>--%>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	<script type="text/javascript">

        layui.use('qyframe', function() {
            var qyframe = layui.qyframe;
        });

        function openlevel(title,typeMsg,userName,dic,defaultDic,url){

            var layer = layui.layer;
            $("#openPromptMsgForm").remove();

            var promtStr = '<form id="openPromptMsgForm" modelAttribute="userUserinfo" action="" method="post" class="form-horizontal" style="display: none;">';
            promtStr += '<div class="control-group"></div>';
            promtStr += '<div class="control-group">';
            promtStr += '<label class="">升级用户：</label> ';
            promtStr += '<label class=""><b >'+userName+'</b></label> ';
            promtStr += '</div>';
            promtStr += '<div class="control-group">';
            promtStr += '<label class="">'+typeMsg+'：</label> <select name="promValue" required>';
            for ( var inx in dic) {
                if(defaultDic == dic[inx]['value']){
                    promtStr += "<option value='"+dic[inx]['value']+"' selected >"+dic[inx]['label']+"</option>";
                }
                else{
                    promtStr += "<option value='"+dic[inx]['value']+"'>"+dic[inx]['label']+"</option>";
                }
            }
            promtStr += '';
            promtStr += '</select>';
            promtStr += '</div>';
            promtStr += '<div class="control-group">';
            promtStr += '<label class="">等级号段： </label> ';
            promtStr +='<input name="levelNumber"  htmlEscape="false" maxlength="60" class="input-large required "   type="text" onkeyup="value=value.replace(/[^\\d]/g,\'\') " ng-pattern="/[^a-zA-Z]/" />';
            // promtStr += '<textarea htmlEscape="false" rows="4" maxlength=255 class="input-xlarge" name="promMsg"  required lay-verify="required"></textarea>';
            promtStr += '</div>';
            promtStr += '</form>';

            $("body").append(promtStr);

            $("#openPromptMsgForm").attr('action',url);
            $("#openPromptMsgForm").find("#openProUser").html(userName);

            //页面层
            layer.open({
                type: 1,
                zIndex:1000,
                title:title,
                skin: 'layui-layer-lan',
                area: ['452px', '250px'], //宽高
                btn: ['确认', '取消'],
                yes: function(index, layero){
                    $("#openPromptMsgForm").submit();
                },
                content: $("#openPromptMsgForm")
            });




            $("#openPromptMsgForm").validate({
                submitHandler: function(form){
                    loading('正在提交，请稍等...');
                    form.submit();
                },
                errorContainer: "#messageBox",
                errorPlacement: function(error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });

            $.validator.setDefaults({
                submitHandler: function() {
                    layer.close(index);
                }
            });
        }

	</script>

</body>
</html>