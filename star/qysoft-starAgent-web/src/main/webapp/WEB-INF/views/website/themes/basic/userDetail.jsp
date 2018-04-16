<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE >
<html>
<head>

    <meta charset="utf-8">
    <title>${fns:getOption("system_sys","site_name")}用户协议</title>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <%@ include file="/WEB-INF/views/website/themes/basic_app/layouts/apicloud_header.jsp" %>

    <style type="text/css">
        .home.aui-bar-nav {
            background-color: #091358;
            padding-top: 20px;
        }

        .zijinshuru {
            margin-top: 1rem;
        }

        .zhifufanshi .aui-list .aui-list-item {
            min-height: 2.5rem;
        }

        .zhifufanshi .aui-list-item-label {
            width: 100%;
        }

        .zhifufanshi .aui-list-item-label img {
            height: 1.6rem;
        }

        .zhifufanshi .aui-list-item-label span {
            margin-left: 0.5rem;
            font-size: 0.8rem;
        }

        .aui-radio:checked {
            background-color: #091358;
            border: solid 1px #091358;
        }

        .fanshi-but {
            padding: 0 0.75rem;
            margin: 1.5rem 0;
        }

        .fanshi-but .aui-btn {
            background: #091358;
            color: #fff;
            height: 2.25rem;
            line-height: 2.25rem;
        }
    </style>
</head>
<body>
<header class="aui-bar aui-bar-nav home">
    <a class="aui-pull-left aui-btn " href="javascript:window.history.back();">
        <span class="aui-iconfont aui-icon-left"></span>
    </a>
    <div class="aui-title">用户协议</div>
</header>
<style>
    .xieyi{padding:0.5rem 0.75rem;}
</style>
<div class="xieyi">
    ${agreement}
</div>



<%@ include file="/WEB-INF/views/website/themes/basic_app/layouts/apicloud_footer.jsp" %>

<script type="text/javascript">
    qySoftInit("${ctxStatic}/qyScript/app/");
    var qyWin;
    //var qyApi;
    //apicloud准备完成
    apiready = function () {
        layui.use(['qyWin'], function () {
            qyWin = layui.qyWin;
            //qyApi = layui.qyApi;
        });
    }



</script>

</body>
</html>

