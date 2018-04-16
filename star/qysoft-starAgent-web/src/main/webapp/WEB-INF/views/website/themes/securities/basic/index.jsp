<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/website/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>首页</title>
	<meta name="decorator" content="cms_default_${site.theme}"/>
	<meta name="description" content="JeeSite ${site.description}" />
	<meta name="keywords" content="JeeSite ${site.keywords}" />
</head>
<body>

<!-- 内容区域 -->
<div class="tpl-content-wrapper">

	<div class="container-fluid am-cf">
		<div class="row">
			<div class="am-u-sm-12 am-u-md-12 am-u-lg-9">
				<div class="page-header-heading"><span class="am-icon-home page-header-heading-icon"></span> 系统首页
					<small>明星经纪人</small>
				</div>
				<p class="page-header-description">欢迎登录会员管理系统,本系统用来管理您的个人团队!</p>
			</div>
	<c:if test="${userInfo.isUsercenter == 1}">
			<div class="am-u-lg-3 tpl-index-settings-button" style="margin-bottom:5px;">
				<button type="button" class="page-header-button" style="font-size: 18px;">
					所得拍卖品:
						${not empty userInfo.usercenterAddress?userInfo.auctionName:""}
					${not empty userInfo.usercenterAddress?(userInfo.usercenterAddress):"暂无竞拍产品"}
				</button>
			</div>
	</c:if>
			<div class="am-u-lg-3 tpl-index-settings-button">
				<button type="button" class="page-header-button" style="font-size: 18px;">
					号段:
					${not empty userInfo.shopId?(userInfo.shopId):"暂无号段"}
				</button>
			</div>
		</div>

	</div>

	<div class="row-content am-cf">
		<div class="row  am-cf">
			<c:if test="${userInfo.isUsercenter==1}">
			<div class="am-u-sm-12 am-u-md-12 am-u-lg-4">
				<div class="widget  widget-primary red am-cf">
					<div class="widget-statistic-header">
						资金账户金额
					</div>
					<div class="widget-statistic-body">
						<div class="widget-statistic-value">
							￥${userInfo.money2}
						</div>
						<div class="am-form-group am-form-icon ">
							<button onclick="chujin()" class="am-btn am-btn-primary  am-input-sm tpl-btn-bg-color-success ">出金</button>
						</div>
						<span class="widget-statistic-icon am-icon-credit-card-alt"></span>
					</div>
				</div>
			</div>
			</c:if>
			<c:if test="${userInfo.userType>0}">
				<div class="am-u-sm-12 am-u-md-12 am-u-lg-4">
					<div class="widget  widget-primary red am-cf">
						<div class="widget-statistic-header">
							成交额
						</div>
						<div class="widget-statistic-body">
							<div class="widget-statistic-value">
								￥<fmt:formatNumber type="number" value="${userLoginVolume}" pattern="0.00" maxFractionDigits="2"/>
							</div>
						</div>
					</div>
				</div>
			</c:if>
			<div class="am-u-sm-12 am-u-md-6 am-u-lg-4">
				<div class="widget widget-primary am-cf">
					<div class="widget-statistic-header">
						积分账户余额
					</div>
					<div class="widget-statistic-body">
						<div class="widget-statistic-value">
							￥${userInfo.score}
						</div>
						<span class="widget-statistic-icon am-icon-credit-card-alt"></span>
					</div>
				</div>
			</div>
			<div class="am-u-sm-12 am-u-md-6 am-u-lg-4">
				<div class="widget widget-purple am-cf">
					<div class="widget-statistic-header">
						出金账户余额
					</div>
					<div class="widget-statistic-body">
						<div class="widget-statistic-value">
							￥${userInfo.money}
						</div>
						<span class="widget-statistic-icon am-icon-support"></span>
					</div>
				</div>
			</div>
			<c:if test="${userInfo.isUsercenter == 1}">
				<div class="am-u-sm-12 am-u-md-6 am-u-lg-4">
					<div class="widget widget-purple am-cf">
						<div class="widget-statistic-header">
							保证金账户余额
						</div>
						<div class="widget-statistic-body">
							<div class="widget-statistic-value">
								￥${userInfo.money3}
							</div>
							<span class="widget-statistic-icon am-icon-support"></span>
						</div>
					</div>
				</div>
			</c:if>
		</div>

		<%--<c:if test="${userInfo.isUsercenter == 1}">--%>
			<%--<div class="row am-cf">--%>

				<%--<div class="am-u-sm-12 am-u-md-12 am-u-lg-4 widget-margin-bottom-lg ">--%>
					<%--<div class="tpl-user-card am-text-center widget-body-lg">--%>
						<%--<div class="tpl-user-card-title">--%>
							<%--推荐机构:${userInfo.office.name}--%>
						<%--</div>--%>

						<%--<div class="tpl-user-card-title">--%>
							<%--机构编码:${userInfo.office.code}--%>
						<%--</div>--%>

						<%--&lt;%&ndash;<div class="achievement-subheading">&ndash;%&gt;--%>
							<%--&lt;%&ndash;二维码地址&ndash;%&gt;--%>
						<%--&lt;%&ndash;</div>&ndash;%&gt;--%>
						<%--<div id="code" align="center">--%>

						<%--</div>--%>

						<%--<div class="tpl-user-card-title" style="word-wrap: break-word;">--%>
							<%--http://${pageContext.request.getServerName()}:${pageContext.request.getServerPort()}${ctx}/register?tj=${userInfo.getUserName()}--%>
						<%--</div>--%>

					<%--</div>--%>
				<%--</div>--%>

			<%--</div>--%>
		<%--</c:if>--%>



	</div>
</div>
<style>
	.odiv-input input{width:100%;height:35px;padding-left:10px;font-size:14px;}
</style>
<script type="text/javascript" src="${ctxStatic}/jquery-plugin/jquery.qrcode.min.js"></script>
<script>
    var flag=false;
    //框架初始化
    <%--qySoftInit("${ctxStatic}/qyScript/web/");--%>
    layui.use(['qyWin','qyForm','layer'], initpage);
    function initpage() {

    }
    $(document).ready(function(){
        $("#code").qrcode({
            render: "table", //table方式
            width: 150, //宽度
            height:150, //高度
            text: "http://${pageContext.request.getServerName()}:${pageContext.request.getServerPort()}${ctx}/register?tj=${userInfo.getUserName()}" //任意内容
        });
    });

	function chujin(){
	    layui.layer.open({
			title:"输入出金金额",
			content:'<div class="odiv-input"><input id="money" type="number" min="0.01" onKeyUp="amount(this)" onBlur="overFormat(this)" placeholder="请输入出金金额"/></div>',
			btn:["确定","取消"],
			yes:function(index){
                if(flag){
                    return;
                }
                var money = $("#money").val();
                if(!money){
                    layui.layer.close(index);
					layui.qyWin.toast("请输入正确的出金金额");
                    return;
				}
                flag = true;
                layui.qyForm.qyajax("${ctx}/user/outGold",{money:money},function (ret) {
                    layui.qyWin.toast(ret.info);
                    if(ret.info){
                        setTimeout("location.reload()",1000);
					}
                   setTimeout(function () {
                       flag =  false;
                   },1000);
                },function (error) {

                    setTimeout(function () {
                        flag =  false;
                    },1000);
                },{time:20000});
                layui.layer.close(index);

			}
		});
	}

    //限制输入金额
    function amount(th){
        var regStrs = [
            ['^0(\\d+)$', '$1'], //禁止录入整数部分两位以上，但首位为0
            ['[^\\d\\.]+$', ''], //禁止录入任何非数字和点
            ['\\.(\\d?)\\.+', '.$1'], //禁止录入两个以上的点
            ['^(\\d+\\.\\d{2}).+', '$1'] //禁止录入小数点后两位以上
        ];
        for(var i=0; i<regStrs.length; i++){
            var reg = new RegExp(regStrs[i][0]);
            th.value = th.value.replace(reg, regStrs[i][1]);
        }
    }
    function overFormat(th){
        var v = th.value;
        if(v === ''){
            v = '0.00';
        }else if(v === '0'){
            v = '0.00';
        }else if(v === '0.'){
            v = '0.00';
        }else if(/^0+\d+\.?\d*.*$/.test(v)){
            v = v.replace(/^0+(\d+\.?\d*).*$/, '$1');
            v = inp.getRightPriceFormat(v).val;
        }else if(/^0\.\d$/.test(v)){
            v = v + '0';
        }else if(!/^\d+\.\d{2}$/.test(v)){
            if(/^\d+\.\d{2}.+/.test(v)){
                v = v.replace(/^(\d+\.\d{2}).*$/, '$1');
            }else if(/^\d+$/.test(v)){
                v = v + '.00';
            }else if(/^\d+\.$/.test(v)){
                v = v + '00';
            }else if(/^\d+\.\d$/.test(v)){
                v = v + '0';
            }else if(/^[^\d]+\d+\.?\d*$/.test(v)){
                v = v.replace(/^[^\d]+(\d+\.?\d*)$/, '$1');
            }else if(/\d+/.test(v)){
                v = v.replace(/^[^\d]*(\d+\.?\d*).*$/, '$1');
                ty = false;
            }else if(/^0+\d+\.?\d*$/.test(v)){
                v = v.replace(/^0+(\d+\.?\d*)$/, '$1');
                ty = false;
            }else{
                v = '0.00';
            }
        }
        th.value = v;
    }
</script>

</body>
</html>
