<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/website/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">

<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>用户注册</title>
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
	<script src="${ctxStatic}/themes/basic/js/jquery.min.js"></script>
	<script type="text/javascript" src="${ctxStatic}/qyScript/web/layer/layui.js"></script>
	<script type="text/javascript" src="${ctxStatic}/qyScript/web/qyconfig.js"></script>


	<script type="text/javascript">
        //框架初始化
        qySoftInit("${ctxStatic}/qyScript/web/");

	</script>

</head>

<body style="background-image: url('${ctxStatic}/themes/basic/images/back.jpg')">
<script src="${ctxStatic}/themes/basic/js/theme.js"></script>
<script src="assets/js/theme.js"></script>

<sys:messageWeb content="${message}"/>
<div class="am-g tpl-g">

	<div class="tpl-login">
		<div class="tpl-login-content">
			<div class="tpl-login-title">注册用户</div>
			<span class="tpl-login-content-info">
				<c:choose>
					<c:when  test="${empty tj}">
						当前推荐链接已失效,请向推荐人索取最新推广链接!
					</c:when>
					<c:otherwise>
						您的推荐人是 : [${office}]</br>
						机构编码 : [${officeCode}]
					</c:otherwise>
				</c:choose>


              </span>


			<form class="am-form tpl-form-line-form" action="${ctx}/register" method="post" id="myForm">
				<div class="am-form-group">
					<input type="hidden" class="tpl-form-input"  name="tj"  value="${tj}" placeholder="推荐人" >
				</div>

				<div class="am-form-group">
					<input type="text" class="tpl-form-input" name="mobile" id="mobile" placeholder="手机号" value="${mobile}">
				</div>

				<div class="am-form-group">
					<input type="text" class="tpl-form-input" name="trueName" placeholder="真实姓名" value="${trueName}">
				</div>

				<div class="am-form-group">
					<input type="password" class="tpl-form-input" name="pwd" placeholder="请输入密码" value="${pwd}">
				</div>

				<div class="am-form-group">
					<input type="password" class="tpl-form-input" name="pwd2"  placeholder="再次输入密码" value="${pwd2}">
				</div>
				<div class="am-form-group">
					<input type="text" class="tpl-form-input" name="verifyCode" placeholder="请输入验证码"  style="width: 40%;display: inline">
					<a class="am-btn am-btn-primary" id="getVerifyCode">点击获取验证码</a>
				</div>

				<div class="am-form-group tpl-login-remember-me">
					<input id="remember-me" name="agree" type="checkbox">
					<label for="remember-me">
						我已阅读并同意
					</label>
					<a href="${ctx}/transDetailByKeyword" target="blank" style="position: relative;top: -2px;cursor: pointer;">《用户注册协议》</a>
				</div>
				<div class="am-form-group">
					<button type="button" id="Subtton" class="am-btn am-btn-primary  am-btn-block tpl-btn-bg-color-success  tpl-login-btn" onclick="registerSubmit()">提交</button>
				</div>
			</form>
		</div>
	</div>
</div>
<script src="${ctxStatic}/themes/basic/js/amazeui.min.js"></script>
<script src="${ctxStatic}/themes/basic/js/app.js"></script>

<script type="text/javascript">
    var nums = 60;
    var clock = '';
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
            $("#getVerifyCode").text(nums + '秒后可重新获取');
            $("#getVerifyCode").css('background', '#aaa');
            $("#getVerifyCode").css('border', '#aaa');

            //一秒执行一次
            clock = setInterval(doLoop, 1000);
		});
    }

    function doLoop() {
        nums--;
        if (nums > 0) {
            $("#getVerifyCode").attr("disabled", true);
            $("#getVerifyCode").text(nums + '秒后可重新获取');
        } else {
            clearInterval(clock);
            //清除js定时器
            $("#getVerifyCode").removeAttr("disabled");
            $("#getVerifyCode").text('点击获取验证码');
            $("#getVerifyCode").css('background', '#0e90d2');
            $("#getVerifyCode").css('border', '#0e90d2');
            nums = 60;
            //重置时间

            $('#getVerifyCode').on('click', function() {
                getVerify();
            });
        }
    }

    function registerSubmit(){
    	$("#myForm").submit();
    	$("#Subtton").attr("disabled",true);
    }

</script>
</body>
</html>