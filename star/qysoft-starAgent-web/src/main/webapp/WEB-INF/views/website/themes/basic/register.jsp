<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/website/themes/basic/layouts/taglib.jsp" %>
<!DOCTYPE html>
<html lang="zh-cn">

<head>
    <title> 注册到${fns:getOption("system_sys","site_name" )}系统</title>
    <%@ include file="/WEB-INF/views/website/themes/basic/layouts/head.jsp" %>
    <!-- 自定义 -->
    <link rel="stylesheet" href="${ctxStatic}/qysoftui/css/register.css">
    <!-- 插件 -->
    <link rel="stylesheet" href="${ctxStatic}/qysoftui/vendor/animsition/animsition.css">
    <!-- 图标 -->
    <link rel="stylesheet" href="${ctxStatic}/qysoftui/fonts/web-icons/web-icons.css">
    <!-- 插件 -->
    <link rel="stylesheet" href="${ctxStatic}/qysoftui/vendor/formvalidation/formValidation.css">

    <!-- 插件 CSS -->
    <link rel="stylesheet" href="${ctxStatic}/qysoftui/vendor/animsition/animsition.css">
    <link rel="stylesheet" href="${ctxStatic}/qysoftui/vendor/toastr/toastr.css">

</head>

<body class="page-login layout-full page-dark">

<div class="page height-full">
    <div class="page-content height-full">
        <div class="page-brand-info vertical-align animation-slide-left hidden-xs">
            <div class="page-brand vertical-align-middle">
                <div class="brand">
                    <img class="brand-img" src="${fns:getOption("system_sys","site_logo")}" height="50"><span class="font-size-18">${fns:getOption("system_sys","site_name" )}</span>
                </div>
                <h3>${fns:getOption("system_sys","site_name" )}系统</h3>
                <ul class="list-icons">
                    <li>
                        <i class="wb-check" aria-hidden="true"></i> 一款非常适用于企业电子商务的营销型功能模块。采用「响应式设计」，能够自适应不同终端设备（手机、平板、电脑）；扁平化的界面和简洁的流程让用户购买体验极佳。
                    </li>
                    <li><i class="wb-check" aria-hidden="true"></i>功能管理极简，不用担心管理界面繁杂；源码开放、接口齐全，可轻松扩展自己所需要的功能。</li>
                    <li><i class="wb-check" aria-hidden="true"></i>该系统紧贴业务特性，涵盖了大量的常用组件和基础功能，最大程度上帮助企业节省时间成本和费用开支。
                    </li>
                </ul>
                <%--<div>--%>
                <%--<a href="http://admui.com" class="btn btn-primary btn-outline"><i class="icon wb-home"></i> 返回官网</a>--%>
                <%--<a href="" class="btn btn-primary btn-outline margin-left-5">联系客服</a>--%>
                <%--</div>--%>
            </div>
        </div>
        <div class="page-login-main animation-fade">
            <div class="vertical-align">
                <div class="vertical-align-middle">
                    <div class="brand visible-xs text-center">
                        <img class="brand-img" src="${fns:getOption("system_sys","site_logo")}<%--${ctxStatic}/qysoftui/images/pic-renlan.png--%>" height="50" alt="Admui">&nbsp;&nbsp;&nbsp;<span class="font-size-18">${fns:getOption("system_sys","site_name")}</span>
                    </div>
                    <h3 class="hidden-xs">注册</h3>
                    <p class="hidden-xs">${fns:getOption("system_sys","site_name")}系统</p>
                    <form class="login-form" action="${ctxStatic}/qysoftui/system/loginValidate" method="post" id="regionForm" style="margin: 0px 0 20px;">
                        <div class="form-group clearfix">
                            <c:if test="${ not empty  agent }">
                                您的经济人是: ${agent}
                            </c:if>
                        </div>
                        <input type="hidden" name="agentName" value="${agent}">
                        <div class="form-group">
                            <label class="sr-only">推荐人编号</label>
                            <input type="text" class="form-control"  value="${parentName}" name="walterName" data-fv-notempty="true"
                                   data-fv-notempty-message="请填写推荐人编号"  placeholder="请填写推荐人编号" ${ not empty  parentName ?"readonly":""}>
                        </div>
                        <div class="form-group">
                            <label class="sr-only" for="mobile">手机号</label>
                            <input type="text" class="form-control" id="mobile"  name="mobile" data-fv-notempty="true"
                                   data-fv-notempty-message="请输入手机号" placeholder="请输入手机号">
                        </div>
                        <div class="form-group">
                            <label class="sr-only" for="mobile">真实姓名</label>
                            <input type="text" class="form-control" id="trueName" name="trueName" data-fv-notempty="true"
                                   data-fv-notempty-message="真实姓名" placeholder="真实姓名一经填写,不能修改">
                        </div>
                        <%--<div class="form-group" style="height:42px;">--%>
                            <%--<label class="sr-only" for="mobile">所在地址</label>--%>

                            <%--<select class="form-control" style="width:33.33%;float: left;" id="province" onchange="getArea('province','city')">--%>
                                <%--<option value="0">请选择</option>--%>
                                <%--<c:forEach var="area" items="${areaList}">--%>
                                    <%--<option value="${area.id}">${area.name}</option>--%>
                                <%--</c:forEach>--%>
                            <%--</select>--%>
                            <%--<select class="form-control" style="width:33.33%;float: left;"id="city" onchange="getArea('city','county')">--%>
                                <%--<option>请选择</option>--%>
                            <%--</select>--%>
                            <%--<select class="form-control" style="width:33.33%;float: left;" id="county" onchange="getArea('county','')">--%>
                                <%--<option>请选择</option>--%>
                            <%--</select>--%>
                            <%--<input type="hidden" id="areaId" class="form-control" name="areaId" data-fv-notempty="true" data-fv-notempty-message="请输入地址">--%>
                        <%--</div>--%>
                        <div class="form-group">
                            <label class="sr-only" >密码</label>
                            <input type="password" class="form-control" name="password" data-fv-notempty="true"
                                   data-fv-notempty-message="请输入密码" <%--data-fv-identical="true"
                                   data-fv-identical-field="password2" data-fv-identical-message="两次输入内容不一致"--%> placeholder="请输入密码">
                        </div>
                        <div class="form-group">
                            <label class="sr-only" >确认密码</label>
                            <input type="password" class="form-control"  name="password2" data-fv-notempty="true"
                                   data-fv-notempty-message="请再次输入密码" data-fv-identical="true"
                                   data-fv-identical-field="password" data-fv-identical-message="两次输入内容不一致" placeholder="请再次输入密码">
                        </div>
                        <%--<div class="form-group">--%>
                            <%--<label class="sr-only" >二级密码</label>--%>
                            <%--<input type="password" class="form-control" name="zhuanquPass" data-fv-notempty="true"--%>
                                   <%--data-fv-notempty-message="请输入二级密码" &lt;%&ndash;data-fv-identical="true"--%>
                                   <%--data-fv-identical-field="password2" data-fv-identical-message="两次输入内容不一致"&ndash;%&gt; placeholder="请输入二级密码">--%>
                        <%--</div>--%>
                        <%--<div class="form-group">--%>
                            <%--<label class="sr-only" >确认二级密码</label>--%>
                            <%--<input type="password" class="form-control"  name="zhuanquPass2" data-fv-notempty="true"--%>
                                   <%--data-fv-notempty-message="请再次输入二级密码" data-fv-identical="true"--%>
                                   <%--data-fv-identical-field="zhuanquPass" data-fv-identical-message="两次输入内容不一致" placeholder="请再次输入二级密码">--%>
                        <%--</div>--%>
                        <style>
                            .checkbox-primary input[type="checkbox"]:checked + label::before, .checkbox-primary input[type="radio"]:checked + label::before {
                                background-color: #445e6d;
                                border-color: #445e6d;
                            }
                        </style>
                        <div class="checkbox-custom checkbox-primary">
                            <input type="checkbox" id="inputRadiosChecked" name="userDetail"  value="1" class="radio-custom radio-primary" checked>
                            <label for="inputRadiosChecked">我已阅读<a href="${ctx}/transDetailByKeyword" target="blank" style="position: relative;top: -2px;cursor: pointer;color: #445e6d;">《用户注册协议》</a></label>
                        </div>
                        <div class="form-group">
                            <label class="sr-only">短信验证码</label>
                            <div class="input-group">
                                <input type="text" class="form-control" name="validCode" data-fv-notempty="true"
                                       data-fv-notempty-message="请输入短信验证码" placeholder="请输入验证码">
                                <a class="input-group-addon padding-0 reload-vify" href="#" style="width:50%;border-left:1px solid #e4eaec;" id="getVerifyCode">
                                    <div class="btn btn-block" height="40"  >获取验证码</div>
                                </a>
                            </div>
                        </div>
                        <div class="" style="color:#ff0000;font-size:12px;">
                            <label for="inputRadiosChecked">* 提现密码默认与登录密码相同,可以进入系统自行修改提现密码</label>
                        </div>
                        <button type="submit" id="regBtn" class="btn btn-block margin-top-30" style="background:#445e6d;color:#fff;">注 册</button>
                    </form>
                </div>
            </div>
            <%--<footer class="page-copyright">--%>
                <%--<p>千叶软件</p>--%>
            <%--</footer>--%>
        </div>
    </div>
</div>


<!-- JS -->
<script src="${ctxStatic}/qysoftui/vendor/jquery/jquery.min.js"></script>
<script src="${ctxStatic}/qysoftui/vendor/bootstrap/bootstrap.min.js"></script>
<script src="${ctxStatic}/qysoftui/vendor/formvalidation/formValidation.min.js" data-name="formValidation"></script>
<script src="${ctxStatic}/qysoftui/vendor/formvalidation/framework/bootstrap.min.js" data-deps="formValidation"></script>
<script src="${ctxStatic}/qysoftui/themes/classic/base/js/app.js" data-name="app"></script>
<script src="${ctxStatic}/qysoftui/vendor/toastr/toastr.min.js"></script>

<script type="text/javascript" data-deps="formValidation">
    var nums = 60;
    var clock = '';
    function getArea(pname,name) {
       var  pid = $("#"+pname).val();
       if(pname == 'county'){
           $("#areaId").val(pid);
           return;
       }
        $.app.ajax( '${ctx}/getArea', {pid:pid}, function (data) {
            var option = "<option value='0'>请选择</option>";
            //alert(JSON.stringify(data.areaList.length));
            for(var i = 0;i<data.areaList.length;i++){

                option += '<option value="'+data.areaList[i].id+'">'+ data.areaList[i].name+'</option>';

            }
            $("#areaId").val("");
            $("#"+pname).nextAll().html('<option value="0">请选择</option>');
            if(pid != 0 ){
                $('#'+name).html(option);
            }

        },function () {

        });
    }

    $('#getVerifyCode').on('click', function() {
        getVerify();
    });

    function getVerify() {
        if ($("#mobile").val() == '' || $("#mobile").val() == 'undefined') {
            alert('手机号码不能为空');
            return;
        }
        var param = {
            mobile : $("#mobile").val()
        };
        $('#getVerifyCode').off('click');
        $.post("${pageContext.request.getContextPath()}/msm/lkMsm/sendVerifyCode", param, function(data) {
            $("#getVerifyCode").attr("disabled", true);
            $("#getVerifyCode").text(nums + '秒重新获取');
            //一秒执行一次
            clock = setInterval(doLoop, 1000);
        });
    }
    function doLoop() {
        nums--;
        if (nums > 0) {
            $("#getVerifyCode").attr("disabled", true);
            $("#getVerifyCode").text(nums + '秒重新获取');
        } else {
            clearInterval(clock);
            //清除js定时器
            $("#getVerifyCode").removeAttr("disabled");
            $("#getVerifyCode").text('获取验证码');
            nums = 60;
            //重置时间

            $('#getVerifyCode').on('click', function() {
                getVerify();
            });
        }
    }

    (function (document, window, $) {

        $('#regionForm').formValidation().on('success.form.fv', function (e) {
            e.preventDefault();
            $.app.ajax( '${ctx}/register', $("#reg\ionForm").serialize(), function (data) {

                toastr.info("恭喜你注册成功！");
                window.location.href="${ctx}/registerSuccess";
            },function () {
                $("#regBtn").attr("disabled",false);
                $('#regBtn').removeClass("disabled");
            });
        });

    })(document, window, jQuery);

</script>


</body>

</html>
