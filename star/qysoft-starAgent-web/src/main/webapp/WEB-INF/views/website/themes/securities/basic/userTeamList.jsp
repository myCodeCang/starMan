<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/website/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>首页</title>
    <meta name="decorator" content="cms_default_${site.theme}"/>
    <meta name="description" content="JeeSite ${site.description}"/>
    <meta name="keywords" content="JeeSite ${site.keywords}"/>

</head>
<body >

<!-- 内容区域 -->

<div class="tpl-content-wrapper">

    <div class="container-small am-cf">
        <div class="row">
            <div class="am-u-sm-12 am-u-md-12 am-u-lg-9">
                <div class="page-header-heading"><span class="am-icon-home page-header-heading-icon"></span> 会员管理
                    <small>-会员管理列表</small>
                </div>
            </div>

        </div>

    </div>



    <div class="row-content am-cf">


        <div class="row">
            <div class="am-u-sm-12 am-u-md-12 am-u-lg-12">
                <div class="widget am-cf">
                    <div class="widget-head am-cf">
                        <div class="widget-body am-fr">

                            <form:form id="searchForm" modelAttribute="userUserinfo" action="${ctx}/user/userTeamList/" method="post" class="am-form tpl-form-border-form tpl-form-border-br am-form-inline">
                                <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                                <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>

                                <label  class="am-form-label">推荐人编号: </label>
                                <div class="am-form-group am-form-icon ">
                                    <form:input path="parentName" value="${userName}" htmlEscape="false" maxlength="255" class="input-medium" readonly="true"/>
                                </div>

                                <label  class="am-form-label">经纪人: </label>
                                <div class="am-form-group am-form-icon ">
                                    <form:input path="remarks" htmlEscape="false" maxlength="255" class="input-medium"/>
                                </div>

                                <label  class="am-form-label">经纪人: </label>
                                <style>
                                    .sel option{
                                        color:#000;
                                    }
                                </style>
                                <div class="am-form-group am-form-icon ">
                                    <form:select path="isShop" class="input-medium sel" style="width:50px;">
                                        <form:option value="" label="全部"/>
                                        <form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                    </form:select>
                                </div>

                                <%--<label  class="am-form-label">账号: </label>--%>
                                <%--<div class="am-form-group am-form-icon ">--%>
                                    <%--<form:input path="userName" htmlEscape="false" maxlength="255" class="input-medium"/>--%>
                                <%--</div>--%>

                                <%--<label  class="am-form-label">手机号: </label>--%>
                                <%--<div class="am-form-group am-form-icon ">--%>
                                    <%--<form:input path="mobile" htmlEscape="false" maxlength="255" class="input-medium"/>--%>
                                <%--</div>--%>

                                <%--<label  class="am-form-label">真实姓名: </label>--%>
                                <%--<div class="am-form-group am-form-icon ">--%>
                                    <%--<form:input path="trueName" htmlEscape="false" maxlength="255" class="input-medium"/>--%>
                                <%--</div>--%>
                                <div class="am-form-group am-form-icon ">
                                    <button type="submit"  id="btnSubmit" class="am-btn am-btn-primary  am-input-sm tpl-btn-bg-color-success ">查询</button>
                                </div>

                            </form:form>

                        </div>

                    </div>
                    <div class="widget-body  widget-body-lg am-fr">
                        <div class="am-scrollable-horizontal ">
                            <table width="100%"  class="am-table am-table-compact am-table-bordered am-table-radius am-table-striped tpl-table-black "
                                   id="example-r">
                                <thead>
                                <tr>
                                    <th>编号</th>
                                    <th>账号</th>
                                    <th>号段</th>
                                    <th>手机号</th>
                                    <th>真实姓名</th>
                                    <%--<th>归属机构</th>--%>
                                    <th>经纪人</th>
                                    <th>用户代理等级</th>
                                    <th>推荐人</th>
                                    <%--<th>推广人</th>--%>
                                    <th>创建时间</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${page.list}" var="userUserinfo">
                                    <tr class="gradeX">
                                        <td>
                                                ${userUserinfo.id}
                                        </td>
                                        <td>
                                                ${userUserinfo.userName}
                                        </td>
                                        <td>
                                                ${userUserinfo.shopId}
                                        </td>
                                        <td id="userMobile">
                                                ${fn:substring(userUserinfo.mobile,0,3)}****${fn:substring(userUserinfo.mobile,7,11)}
                                        </td>
                                        <td>
                                                ${userUserinfo.trueName}
                                        </td>
                                        <td>
                                                ${empty userUserinfo.remarks?"--":userUserinfo.remarks}
                                        </td>
                                        <%--<td>--%>
                                                <%--${userUserinfo.office.code}--%>
                                        <%--</td>--%>
                                        <td>
                                                ${userUserinfo.areaId}

                                        </td>
                                        <td>
                                                ${userUserinfo.parentName}
                                        </td>
                                        <%--<td  style="${userUserinfo.isUsercenter==1?'color:#F3523B':''}" >--%>

                                                <%--${fns:getDictLabel(userUserinfo.isUsercenter, 'yes_no', '')}--%>
                                        <%--</td>--%>
                                        <td>
                                            <fmt:formatDate value="${userUserinfo.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </td>
                                        <td>
                                            <div class="tpl-table-black-operation">
                                                <c:if test="${userUserinfo.userType > 0}">
                                                    <a href="${ctx}/user/userTeamList?parentName=${userUserinfo.userName}" class="list" style="background:none;color:#fff;border-color:#fff;">下级会员列表</a>
                                                </c:if>

                                                <c:if test="${userUserinfo.userType == 0}">
                                                    <c:if test="${userUserinfo.isShop == 0}">
                                                        <a href="javascript:void(0);" onclick="openAgent('${userUserinfo.userName}')" class="list" style="background:none;color:#fff;border-color:#fff;">指定为经纪人</a>
                                                    </c:if>
                                                </c:if>

                                                <c:if test="${userUserinfo.userType == 0}">
                                                    <c:if test="${userUserinfo.isShop == 1}">
                                                        <a href="javascript:void(0);" onclick="cancelAgent('${userUserinfo.userName}')" class="list" style="background:none;color:#fff;border-color:#fff;">撤销经纪人</a>
                                                    </c:if>
                                                </c:if>

                                                <c:if test="${ userUserinfo.userType== '0'}">
                                                    <c:if test="${parentUser.userType > 1}">
                                                        <c:if test="${userUserinfo.isShop == 0}">
                                                        <%--<a href="javascript:void(0)" onclick="setUserType(${userInfo.userType},'${userUserinfo.userName}')" class="list" style="background:none;color:#fff;border-color:#fff;">指定代理等级</a>--%>
                                                        <a href="javascript:void(0);" onclick="openlevel('${userUserinfo.userName}','${userUserinfo.trueName}','${parentUser.userType -1}')" class="list" style="background:none;color:#fff;border-color:#fff;">指定代理等级</a>
                                                        </c:if>
                                                    </c:if>
                                                </c:if>
                                            </div>
                                        </td>
                                    </tr>

                                </c:forEach>

                                </tbody>
                            </table>
                            <style>
                                .am-pagination>li{margin-right:0;}
                                .am-pagination>li>a{color:#fff;background:none;}
                                .am-pagination>.am-disabled>a{color:#fff;background:none;}
                                .am-pagination>.am-active>a{border-color:#fff;background:none;}
                                .am-pagination>li:last-child>a{background:none;border:none;cursor:default;}
                                .am-pagination>li>a>input{width:20px;border:none;background:none;text-align: center;}
                            </style>
                            <div class="pagination">${page}</div>
                        </div>
                    </div>
                </div>
            </div>
        </div>



    </div>

</div>
<style>
    .layui-form-item {
        margin-bottom: 0px;
        clear: both;
    }
    .layui-form-label {

        padding: 9px 15px;
        width: 100px;
    }
</style>
<script type="text/javascript">
    var saveFlag = false;
    $(document).ready(function() {

    });
    function page(n,s){
        $("#pageNo").val(n);
        $("#pageSize").val(s);
        $("#searchForm").submit();
        return false;
    }

    layui.use(['layer','qyForm','qyWin'], function(){
        var layer = layui.layer;

    });

    function setUserType(userType,upName){
        if(saveFlag){
            return;
        }
        layui.layer.open({
            title:"指定代理",
            content:"您确定要指定为代理吗?",
            btn:["确定","取消"],
            yes:function(index){
                    saveFlag = true;
                    layui.qyForm.qyajax("${ctx}/user/setUserType",{"userType":userType-1,"upName":upName},function (data) {
                        layui.qyWin.toast(data.info);
                        setTimeout("location.reload()",2500) ;
                        saveFlag = false;
                    },function (ret) {
                        saveFlag = false;
                    });
                     layui.layer.close(index);
                }
            });
    }



    function openlevel(userName,trueName,usreType) {

        var levelname='二级代理';
        if(usreType == 2){
            levelname='一级代理';
        }
        var content ='<div class="layui-form-item">';

        content +=' <label class="layui-form-label" >指定用户</label>';
        content +='    <div class="layui-input-inline">';
        content +='    <input type="text" name="title" readonly  value="'+trueName+'" required  lay-verify="required" placeholder="请输入标题" autocomplete="off" class="layui-input">';
        content +='     </div>';
        content +='     </div>';
        content +='     <div class="layui-form-item">';
        content +='     <label class="layui-form-label">升级等级</label>';
        content +='     <div class="layui-input-inline">';
        content +='   <input type="text" name="title" readonly  value="'+levelname+'" lay-verify="required" placeholder="请输入标题" autocomplete="off" class="layui-input">';
        content +='    </div>';
        content +='    </div>';
        content +='     <div class="layui-form-item">';
        content +='     <label class="layui-form-label">代理号段</label>';
        content +='     <div class="layui-input-inline">';
        content +='   <input type="text" id="levelNumber" onkeyup="value=value.replace(/[^\\d]/g,\'\') " ng-pattern="/[^a-zA-Z]/"  required  lay-verify="required" placeholder="请输入2位号段" autocomplete="off" class="layui-input">';
        content +='    </div>';
        content +='    </div>';
        if(saveFlag){
            return;
        }
        layui.layer.open({
            title:"指定代理",
            content:content,
            skin: 'layui-layer-lan',
            area: ['350px', '250px'],
            btn:["确定","取消"],
            yes:function(index){
                var number  = $("#levelNumber").val();
                if(!number){
                    layui.qyWin.toast("请输入号段");
                    return;
                }
                saveFlag = true;
                layui.qyForm.qyajax("${ctx}/user/setUserType",{"userType":usreType,"upName":userName,levelNumber:number},function (data) {
                    layui.qyWin.toast(data.info);
                    setTimeout("location.reload()",2500) ;
                    saveFlag = false;
                },function (ret) {
                    saveFlag = false;
                });
                layui.layer.close(index);
            }
        });

    }

    function openAgent(userName){
        if(saveFlag){
            return;
        }
        layui.layer.open({
            title:"指定经纪人",
            content:"您确定要指定该会员为经纪人吗?",
            btn:["确定","取消"],
            yes:function(index){
                saveFlag = true;
                layui.qyForm.qyajax("${ctx}/user/updadeIsShop",{"userName":userName,"isShop":"1"},function (data) {
                    layui.qyWin.toast(data.info);
                    setTimeout("location.reload()",2500) ;
                    saveFlag = false;
                },function (ret) {
                    saveFlag = false;
                });
                layui.layer.close(index);
            }
        });
    }

    function cancelAgent(userName){
        if(saveFlag){
            return;
        }
        layui.layer.open({
            title:"指定经纪人",
            content:"您确定要撤销该会员经纪人资格吗?",
            btn:["确定","取消"],
            yes:function(index){
                saveFlag = true;
                layui.qyForm.qyajax("${ctx}/user/updadeIsShop",{"userName":userName,"isShop":"0"},function (data) {
                    layui.qyWin.toast(data.info);
                    setTimeout("location.reload()",2500) ;
                    saveFlag = false;
                },function (ret) {
                    saveFlag = false;
                });
                layui.layer.close(index);
            }
        });
    }

</script>

</body>
</html>
