
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
        <title>充值成功</title>
        <link rel="stylesheet" type="text/css" href="../css/amazeui.min.css"/>
        <link rel="stylesheet" type="text/css" href="../css/admin.css"/>
        <style type="text/css">
                .register{padding-top:150px;}
                .register-img{margin:0 auto;display: block;width:90px;}
                .register-p{font-size:1.5rem;text-align: center;color:#33b936;font-weight: bold;}
                .register-pp{font-size:1.6rem;text-align: center;}
                .register .am-btn{border:none;font-size:1.8rem;border-radius: 50px;color:#fff;background-color:#f85255;padding: 0.5em 2em;display: block;margin:0 auto;margin-top:150px;}
        </style>
</head>
<body>
<div class="am-container register">
        <img src="${ctxStatic}/themes/basic/images/chongzhi.png" class="register-img"/>
        <p class="register-p">充值成功!!!</p>
        <p style="text-align: center;"><a href="${goIndex}">点击返回个人中心</a></p>
        <%--<p class="register-pp">您可以下载APP登录，就可以体验我们的商城平台</p>--%>
        <%--<button type="button" class="am-btn" onclick="location.href='${fns:getOption("system_app_version","android_download_url")}'">立即下载</button>--%>
</div>
</body>
</html>
