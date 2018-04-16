<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE >
<html>
<head>
    <title>充值</title>
    <meta charset="utf-8">
    <title>${fns:getConfig('productName')}</title>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/qyScript/web/layer/css/layer-mobile.css" />


    <script type="text/javascript" src="${ctxStatic}/qyScript/web/layer/layui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/qyScript/web/qyconfig.js"></script>

    <script type="text/javascript" src="${ctxStatic}/qyScript/web/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/qyScript/web/jquery/jquery.qrcode.min.js"></script>

</head>
    <style type="text/css">
        .erweima{position: absolute;top:2.25rem;bottom:5rem;left:0;right:0;display:flex;display:-webkit-flex;justify-content: center;-webkit-justify-content: center;align-items: center;-webkit-align-items: center;}
        .erweima>div{width:100%;}
        .erweima>div div{margin:0 auto;width:60%;}
        .erweima>div p{text-align: center;font-size:0.65rem;margin:10px 0;}
        #code{padding-bottom:0.75rem;margin-top:1rem;}
        #code *{margin:0 auto;}
        .erweima>div img{display: block;margin:0 auto;}
    </style>
    <div class="aui-content erweima">
        <div>
            <p>打开支付宝,扫码支付</p>
            <div id="code" >
            </div>
            <img id="imgCode"  />
        </div>

    </div>

    </body>
    <script type="text/javascript">
        qySoftInit("${ctxStatic}/qyScript/web/");


        //apicloud 准备完成

        layui.use(['qyWin','qyForm'], initpage);


        function initpage(){
            var qyWin  = layui.qyWin;
            loadData();

        }

        function loadData(){
            var qrcodeOne = $("#code").qrcode({
                width: 200, //宽度
                height:200, //高度
                text: "${url}"
            }).hide();
            var canvas = qrcodeOne.find('canvas').get(0);
            $('#imgCode').attr('src',canvas.toDataURL('image/jpg'));
        }
    </script>

</html>

