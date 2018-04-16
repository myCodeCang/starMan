<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/website/include/taglib.jsp" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <title>会员管理系统</title>
    <%@include file="/WEB-INF/views/website/themes/securities/basic/layouts/head.jsp" %>


</head>

<body>
<script src="${ctxStatic}/themes/basic/js/theme.js"></script>
<div class="am-g tpl-g">
    <!-- 头部 -->
    <header>
        <!-- logo -->
        <div class="am-fl tpl-header-logo" style="text-align: center;line-height: 57px;">
            <a href="javascript:;">
                <span style="font-size:26px;font-weight: bold;color:#fff;">明星经纪人</span>
                <%--<img src="${ctxStatic}/themes/basic/img/logo.png" alt="" style="width:100px;">--%>
            </a>
        </div>
        <!-- 右侧内容 -->
        <div class="tpl-header-fluid">
            <!-- 侧边切换 -->
            <div class="am-fl tpl-header-switch-button am-icon-list">
                    <span>

                </span>
            </div>
            <%--<!-- 搜索 -->--%>
            <%--<div class="am-fl tpl-header-search">--%>
            <%--<form class="tpl-header-search-form" action="javascript:;">--%>
            <%--<button class="tpl-header-search-btn am-icon-search"></button>--%>
            <%--<input class="tpl-header-search-box" type="text" placeholder="搜索内容...">--%>
            <%--</form>--%>
            <%--</div>--%>
            <!-- 其它功能-->
            <div class="am-fr tpl-header-navbar">
                <ul>
                    <!-- 欢迎语 -->
                    <li class="am-text-sm tpl-header-navbar-welcome">
                        <a href="javascript:;">欢迎您: ${userInfo.trueName}, <span>${userInfo.userName}</span> </a>
                    </li>

                    <!-- 新邮件 -->
                    <%--<li class="am-dropdown tpl-dropdown" data-am-dropdown>--%>
                    <%--<a href="javascript:;" class="am-dropdown-toggle tpl-dropdown-toggle" data-am-dropdown-toggle>--%>
                    <%--<i class="am-icon-envelope"></i>--%>
                    <%--<span class="am-badge am-badge-success am-round item-feed-badge">4</span>--%>
                    <%--</a>--%>
                    <%--<!-- 弹出列表 -->--%>
                    <%--<ul class="am-dropdown-content tpl-dropdown-content">--%>
                    <%--<li class="tpl-dropdown-menu-messages">--%>
                    <%--<a href="javascript:;" class="tpl-dropdown-menu-messages-item am-cf">--%>
                    <%--<div class="menu-messages-ico">--%>
                    <%--<img src="${ctxStatic}/themes/basic/img/user04.png" alt="">--%>
                    <%--</div>--%>
                    <%--<div class="menu-messages-time">--%>
                    <%--3小时前--%>
                    <%--</div>--%>
                    <%--<div class="menu-messages-content">--%>
                    <%--<div class="menu-messages-content-title">--%>
                    <%--<i class="am-icon-circle-o am-text-success"></i>--%>
                    <%--<span>夕风色</span>--%>
                    <%--</div>--%>
                    <%--<div class="am-text-truncate"> Amaze UI 的诞生，依托于 GitHub 及其他技术社区上一些优秀的资源；Amaze UI 的成长，则离不开用户的支持。 </div>--%>
                    <%--<div class="menu-messages-content-time">2016-09-21 下午 16:40</div>--%>
                    <%--</div>--%>
                    <%--</a>--%>
                    <%--</li>--%>

                    <%--<li class="tpl-dropdown-menu-messages">--%>
                    <%--<a href="javascript:;" class="tpl-dropdown-menu-messages-item am-cf">--%>
                    <%--<div class="menu-messages-ico">--%>
                    <%--<img src="${ctxStatic}/themes/basic/img/user02.png" alt="">--%>
                    <%--</div>--%>
                    <%--<div class="menu-messages-time">--%>
                    <%--5天前--%>
                    <%--</div>--%>
                    <%--<div class="menu-messages-content">--%>
                    <%--<div class="menu-messages-content-title">--%>
                    <%--<i class="am-icon-circle-o am-text-warning"></i>--%>
                    <%--<span>禁言小张</span>--%>
                    <%--</div>--%>
                    <%--<div class="am-text-truncate"> 为了能最准确的传达所描述的问题， 建议你在反馈时附上演示，方便我们理解。 </div>--%>
                    <%--<div class="menu-messages-content-time">2016-09-16 上午 09:23</div>--%>
                    <%--</div>--%>
                    <%--</a>--%>
                    <%--</li>--%>
                    <%--<li class="tpl-dropdown-menu-messages">--%>
                    <%--<a href="javascript:;" class="tpl-dropdown-menu-messages-item am-cf">--%>
                    <%--<i class="am-icon-circle-o"></i> 进入列表…--%>
                    <%--</a>--%>
                    <%--</li>--%>
                    <%--</ul>--%>
                    <%--</li>--%>

                    <!-- 新提示 -->
                    <%--<li class="am-dropdown" data-am-dropdown>--%>
                    <%--<a href="javascript:;" class="am-dropdown-toggle" data-am-dropdown-toggle>--%>
                    <%--<i class="am-icon-bell"></i>--%>
                    <%--<span class="am-badge am-badge-warning am-round item-feed-badge">5</span>--%>
                    <%--</a>--%>

                    <%--<!-- 弹出列表 -->--%>
                    <%--<ul class="am-dropdown-content tpl-dropdown-content">--%>
                    <%--<li class="tpl-dropdown-menu-notifications">--%>
                    <%--<a href="javascript:;" class="tpl-dropdown-menu-notifications-item am-cf">--%>
                    <%--<div class="tpl-dropdown-menu-notifications-title">--%>
                    <%--<i class="am-icon-line-chart"></i>--%>
                    <%--<span> 有6笔新的销售订单</span>--%>
                    <%--</div>--%>
                    <%--<div class="tpl-dropdown-menu-notifications-time">--%>
                    <%--12分钟前--%>
                    <%--</div>--%>
                    <%--</a>--%>
                    <%--</li>--%>
                    <%--<li class="tpl-dropdown-menu-notifications">--%>
                    <%--<a href="javascript:;" class="tpl-dropdown-menu-notifications-item am-cf">--%>
                    <%--<div class="tpl-dropdown-menu-notifications-title">--%>
                    <%--<i class="am-icon-star"></i>--%>
                    <%--<span> 有3个来自人事部的消息</span>--%>
                    <%--</div>--%>
                    <%--<div class="tpl-dropdown-menu-notifications-time">--%>
                    <%--30分钟前--%>
                    <%--</div>--%>
                    <%--</a>--%>
                    <%--</li>--%>
                    <%--<li class="tpl-dropdown-menu-notifications">--%>
                    <%--<a href="javascript:;" class="tpl-dropdown-menu-notifications-item am-cf">--%>
                    <%--<div class="tpl-dropdown-menu-notifications-title">--%>
                    <%--<i class="am-icon-folder-o"></i>--%>
                    <%--<span> 上午开会记录存档</span>--%>
                    <%--</div>--%>
                    <%--<div class="tpl-dropdown-menu-notifications-time">--%>
                    <%--1天前--%>
                    <%--</div>--%>
                    <%--</a>--%>
                    <%--</li>--%>


                    <%--<li class="tpl-dropdown-menu-notifications">--%>
                    <%--<a href="javascript:;" class="tpl-dropdown-menu-notifications-item am-cf">--%>
                    <%--<i class="am-icon-bell"></i> 进入列表…--%>
                    <%--</a>--%>
                    <%--</li>--%>
                    <%--</ul>--%>
                    <%--</li>--%>

                    <!-- 退出 -->
                    <li class="am-text-sm">
                        <a href="${ctx}/logout">
                            <span class="am-icon-sign-out"></span> 退出
                        </a>
                    </li>
                </ul>
            </div>
        </div>

    </header>
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
    <!-- 侧边导航栏 -->
    <div class="left-sidebar">
        <!-- 用户信息 -->
        <div class="tpl-sidebar-user-panel">
            <div class="tpl-user-panel-slide-toggleable">
                <div class="tpl-user-panel-profile-picture">
                    <img src="${ctxStatic}/themes/basic/img/user04.png" alt="">
                </div>
                <span class="user-panel-logged-in-text">
                      <i class="am-icon-circle-o am-text-success tpl-user-panel-status-icon"></i>
                     ${userInfo.trueName}
                  </span>

                <span class="user-panel-logged-in-text">
                      <i class="am-icon-circle-o am-text-success tpl-user-panel-status-icon"></i>
                    <c:if test="${userInfo.userType>0}">
                        ${userInfo.areaId}
                    </c:if>
                    <c:if test="${userInfo.isUsercenter==1}">
                        承销商
                    </c:if>
                  </span>
                <%--<a href="javascript:;" class="tpl-user-panel-action-link"> <span class="am-icon-pencil"></span> 账号设置</a>--%>
            </div>
        </div>

        <!-- 菜单 -->
        <ul class="sidebar-nav">
            <%--<li class="sidebar-nav-heading">Components <span class="sidebar-nav-heading-info"> 附加组件</span></li>--%>
            <li class="sidebar-nav-link">
                <a href="${ctx}" id="index">
                    <i class="am-icon-home sidebar-nav-link-logo"></i> 首页
                </a>
            </li>
            <c:if test="${userInfo.userType >= 1}">
                <li class="sidebar-nav-link">
                    <a href="${ctx}/user/userTeamList?parentName=${userInfo.userName}" id="myteam">
                        <i class="am-icon-table sidebar-nav-link-logo"></i> 会员管理
                    </a>
                </li>
                <li class="sidebar-nav-link">
                    <a href="${ctx}/user/userAcount" id="userAcount">
                        <i class="am-icon-table sidebar-nav-link-logo"></i> 下级会员账变查询
                    </a>
                </li>
                <li class="sidebar-nav-link">
                    <a href="${ctx}/user/profitAndLoss" id="profit">
                        <i class="am-icon-table sidebar-nav-link-logo"></i> 盈亏查询
                    </a>
                </li>
                <li class="sidebar-nav-link">
                    <a href="${ctx}/user/teamVolume" id="teamVolume">
                        <i class="am-icon-table sidebar-nav-link-logo"></i> 下级会员交易额
                    </a>
                </li>
            </c:if>
                <li class="sidebar-nav-link">
                    <a href="${ctx}/user/userHoldGoods" id="teamgrid">
                        <i class="am-icon-table sidebar-nav-link-logo"></i> 持仓查询
                    </a>
                </li>
                <li class="sidebar-nav-link">
                    <a href="${ctx}/user/teamAcount" id="teamAcount">
                        <i class="am-icon-table sidebar-nav-link-logo"></i> 会员账变查询
                    </a>
                </li>
            <c:if test="${userInfo.isUsercenter == 1}">
                <li class="sidebar-nav-link">
                    <a href="${ctx}/user/transLog" id="transLog">
                        <i class="am-icon-table sidebar-nav-link-logo"></i> 平仓查询
                    </a>
                </li>
                <li class="sidebar-nav-link">
                    <a href="${ctx}/user/transingLog" id="transingLog">
                        <i class="am-icon-table sidebar-nav-link-logo"></i> 交易查询
                    </a>
                </li>
                <li class="sidebar-nav-link">
                    <a href="${ctx}/user/findOrderList" id="duihuanLog">
                        <i class="am-icon-table sidebar-nav-link-logo"></i> 兑换记录查询
                    </a>
                </li>
            </c:if>

            <c:if test="${not empty(userInfo.usercenterAddress)}">
                <li class="sidebar-nav-link">
                    <a href="${ctx}/user/userDayHoldGoods" id="userreport">
                        <i class="am-icon-table sidebar-nav-link-logo"></i> 日持仓查询
                    </a>
                </li>
            </c:if>
                <%--<li class="sidebar-nav-link">--%>
                    <%--<a href="${ctx}/user/userGrid" id="teamgrid">--%>
                        <%--<i class="am-icon-table sidebar-nav-link-logo"></i> 团队网络--%>
                    <%--</a>--%>
                <%--</li>--%>
                <%--<li class="sidebar-nav-link">--%>
                    <%--<a href="${ctx}/user/userReport" id="userreport">--%>
                        <%--<i class="am-icon-table sidebar-nav-link-logo"></i> 个人报表--%>
                    <%--</a>--%>
                <%--</li>--%>
                <%--<li class="sidebar-nav-link">--%>
                    <%--<a href="${ctx}/user/teamReport" id="teamreport">--%>
                        <%--<i class="am-icon-table sidebar-nav-link-logo"></i> 团队报表--%>
                    <%--</a>--%>
                <%--</li>--%>
        </ul>
    </div>

    <sitemesh:body/>

</div>
<script src="${ctxStatic}/themes/basic/js/amazeui.min.js"></script>
<script src="${ctxStatic}/themes/basic/js/amazeui.datatables.min.js"></script>
<script src="${ctxStatic}/themes/basic/js/dataTables.responsive.min.js"></script>
<script src="${ctxStatic}/themes/basic/js/app.js"></script>
<script>
    if(${menu == 1}){
        $("#index").addClass('active');
    }
    if(${menu == 2}){
        $("#myteam").addClass('active');
    }
    if(${menu == 3}){
        $("#userAcount").addClass('active');
    }
    if(${menu == 4}){
        $("#teamgrid").addClass('active');
    }
    if(${menu == 5}){
        $("#transLog").addClass('active');
    }
    if(${menu == 6}){
        $("#teamAcount").addClass('active');
    }if(${menu == 7}){
        $("#transingLog").addClass('active');
    }
    if(${menu == 8}){
        $("#userreport").addClass('active');
    }
    if(${menu == 9}){
        $("#profit").addClass('active');
    }
    if(${menu == 10}){
        $("#duihuanLog").addClass('active');
    }
    if(${menu == 11}){
        $("#teamVolume").addClass('active');
    }

</script>


</body>

</html>