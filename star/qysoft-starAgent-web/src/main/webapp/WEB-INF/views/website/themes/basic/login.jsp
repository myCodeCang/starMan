
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/website/include/taglib.jsp"%>

<!DOCTYPE html>
<html lang="en">

<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>明星经纪人</title>
	<meta name="description" content="这是一个 index 页面">
	<meta name="keywords" content="index">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="renderer" content="webkit">
	<meta http-equiv="Cache-Control" content="no-siteapp" />
	<link rel="icon" type="image/png" href="${ctxStatic}/themes/basic/i/favicon.png">
	<link rel="apple-touch-icon-precomposed" href="${ctxStatic}/themes/basic/i/app-icon72x72@2x.png">
	<meta name="apple-mobile-web-app-title" content="Amaze UI" />
	<link rel="stylesheet" href="${ctxStatic}/themes/basic/css/amazeui.min.css" />
	<link rel="stylesheet" href="${ctxStatic}/themes/basic/css/amazeui.datatables.min.css" />
	<link rel="stylesheet" href="${ctxStatic}/themes/basic/css/app.css">
	<style>
		.theme-black .tpl-login-logo {
			max-width: 300px;
			background-size: contain;
		}
	</style>
	<script src="${ctxStatic}/themes/basic/js/jquery.min.js"></script>

	<script type="text/javascript" src="${ctxStatic}/qyScript/web/layer/layui.js"></script>
	<script type="text/javascript" src="${ctxStatic}/qyScript/web/qyconfig.js"></script>
</head>
<sys:messageWeb content="${message}"/>
<body data-type="login" style="background-image: url('${ctxStatic}/themes/basic/images/back.jpg')">
<script src="${ctxStatic}/themes/basic/js/theme.js"></script>
<div class="am-g tpl-g">
	<!-- 风格切换 -->
	<%--<div class="tpl-skiner">--%>
		<%--<div class="tpl-skiner-toggle am-icon-cog">--%>
		<%--</div>--%>
		<%--<div class="tpl-skiner-content">--%>
			<%--<div class="tpl-skiner-content-title">--%>
				<%--选择主题--%>
			<%--</div>--%>
			<%--<div class="tpl-skiner-content-bar">--%>
				<%--<span class="skiner-color skiner-white" data-color="theme-white"></span>--%>
				<%--<span class="skiner-color skiner-black" data-color="theme-black"></span>--%>
			<%--</div>--%>
		<%--</div>--%>
	<%--</div>--%>
	<div class="tpl-login">
		<div class="tpl-login-content">
			<div class="tpl-login-logo">

			</div>

			<form class="am-form tpl-form-line-form" action="${ctx}/login" method="post" id="loginForm" >
				<input type="hidden" id="userType" name="userType" value="website">
				<div class="am-form-group">
					<input type="text" id="username" name="username" class="tpl-form-input"  placeholder="请输入账号">
				</div>
				<div class="am-form-group">
					<input type="password" id="password" name="password" class="tpl-form-input" placeholder="请输入密码">
				</div>
				<%--<div class="am-form-group tpl-login-remember-me">--%>
					<%--<input id="remember-me" type="checkbox">--%>
					<%--<label for="remember-me">--%>
						<%--记住密码--%>
					<%--</label>--%>
				<%--</div>--%>
				<div class="am-form-group">

					<button type="button" class="am-btn am-btn-primary  am-btn-block tpl-btn-bg-color-success  tpl-login-btn" onclick="verify()">登录</button>

				</div>
			</form>
		</div>
	</div>
</div>
<script src="${ctxStatic}/themes/basic/js/amazeui.min.js"></script>
<script src="${ctxStatic}/themes/basic/js/app.js"></script>

</body>


<script type="text/javascript">
	var flag = false;
    //框架初始化
    qySoftInit("${ctxStatic}/qyScript/web/");
    layui.use(['qyWin','qyForm','layer'], initpage);
    function initpage() {
		
    }
    function verify() {
        var mobile = $("#username").val();
        var password = $("#password").val();
        if(!mobile || !password){
            layui.qyWin.toast("用户名或密码不能为空!!");
            return false;
        }
        layui.qyForm.qyajax("${ctx}/getUserByMobile",{mobile:mobile},function (ret) {
            if(ret.user.userType <= 0 && ret.user.isUsercenter != '1'){
                layui.qyWin.toast("你没有权限登录!!");
                return;
            }
            $("#loginForm").submit();
        },function (error) {
            layui.qyWin.toast("登陆失败!!");
        });
    }

</script>


</html>