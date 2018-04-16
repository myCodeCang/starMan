
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/website/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<meta name="apple-mobile-web-app-title" content="Amaze UI">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="renderer" content="webkit">
		<title>注册成功</title>
		<link rel="stylesheet" type="text/css" href="../css/amazeui.min.css"/>
		<link rel="stylesheet" type="text/css" href="../css/admin.css"/>
		<style type="text/css">
			.register{padding-top:130px;}
			.register-img{margin:0 auto;display: block;width:240px;}
			.register-p{font-size:2rem;text-align: center;color:#33b936;font-weight: bold;}
			.register-pp{font-size:1.6rem;text-align: center;}
			.register .am-btn{font-size:1.8rem;border-radius: 50px;color:#fff;background-color:#f85255;padding: 0.5em 2em;display: block;margin:0 auto;margin-top:120px;}

			@media only screen and (max-width: 800px){
				.register-img{margin:0 auto;display: block;width:200px;}
				.register{padding-top:0px;}
				.register .am-btn{margin-top:70px;font-size:1.8rem;border-radius: 50px;color:#fff;background-color:#f85255;padding: 0.5em 1.8em;margin-bottom:20px;}
			}
		</style>
	</head>
	<body>
		<div class="am-container register">
			<img src="${ctxStatic}/themes/basic/images/jiaoyilogo.png" class="register-img"/>
			<p class="register-p">恭喜您注册成功!</p>
			<p class="register-pp">您可以下载APP登录，就可以体验我们的交易平台</p>
			<button type="button" class="am-btn" onclick="location.href='https://fir.im/hcj9'">立即下载</button>
		</div>
	</body>
</html>
