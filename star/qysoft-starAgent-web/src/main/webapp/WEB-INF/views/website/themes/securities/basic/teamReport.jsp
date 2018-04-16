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
                <div class="page-header-heading"><span class="am-icon-home page-header-heading-icon"></span> 
                团队资产报表
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

                            <form:form id="searchForm" modelAttribute="userReport" action="${ctx}/user/teamReport/" method="post" class="am-form tpl-form-border-form tpl-form-border-br am-form-inline">
                                <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                                <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>

                                <label  class="am-form-label">账号: </label>
                                <div class="am-form-group am-form-icon ">
                                    <form:input path="userName" htmlEscape="false" maxlength="100" class="input-medium"/>
                                </div>

                                 <label  class="am-form-label">月份: </label>
                                <div class="am-form-group am-form-icon ">
                                    <form:input path="createDate" type="month"  htmlEscape="false" maxlength="100" class="input-medium"/>
                                </div>

                               
                                <div class="am-form-group am-form-icon ">
                                    <button type="submit"  id="btnSubmit" class="am-btn am-btn-primary  am-input-sm tpl-btn-bg-color-success ">查询</button>
                                </div>

                            </form:form>
                            <sys:messageWeb content="${message}"/>
                        </div>

                    </div>
                    <div class="widget-body  widget-body-lg am-fr">
                        <form class="am-form am-form-horizontal">
                            <div class="am-form-group">
                                <label for="doc-ipt-3" class="am-u-sm-12 am-u-lg-2 am-u-md-2 am-form-label">用户名:</label>
                                <div class="am-u-sm-12 am-u-lg-3 am-u-md-3 am-u-end">
                                    <input type="text" readonly="true"  value="${team.userUserinfo.userName}" id="doc-ipt-3" placeholder="" style="background:none;border:1px solid rgba(255, 255, 255, 0.2);color:#fff;">
                                </div>
                            </div>

                            <div class="am-form-group">
                                <label for="doc-ipt-3" class="am-u-sm-12 am-u-lg-2 am-u-md-2 am-form-label">真实姓名:</label>
                                <div class="am-u-sm-12 am-u-lg-3 am-u-md-3 am-u-end">
                                    <input type="text"  readonly="true" value="${team.userUserinfo.trueName}" id="doc-ipt-3" placeholder="" style="background:none;border:1px solid rgba(255, 255, 255, 0.2);color:#fff;">
                                </div>
                            </div>

                            <div class="am-form-group">
                                <label for="doc-ipt-3" class="am-u-sm-12 am-u-lg-2 am-u-md-2 am-form-label">统计人数:</label>
                                <div class="am-u-sm-12 am-u-lg-3 am-u-md-3 am-u-end">
                                    <input type="text" readonly="true"  value="${team.userName}"  id="doc-ipt-3" placeholder="" style="background:none;border:1px solid rgba(255, 255, 255, 0.2);color:#fff;">
                                </div>
                            </div>

                            <div class="am-form-group">
                                <label for="doc-ipt-3" class="am-u-sm-12 am-u-lg-2 am-u-md-2 am-form-label">充值金额统计:</label>
                                <div class="am-u-sm-12 am-u-lg-3 am-u-md-3 am-u-end">
                                    <input type="text" readonly="true"  value="${team.dividendOne}" id="doc-ipt-3" placeholder="" style="background:none;border:1px solid rgba(255, 255, 255, 0.2);color:#fff;">
                                </div>
                            </div>

                            <div class="am-form-group">
                                <label for="doc-ipt-3" class="am-u-sm-12 am-u-lg-2 am-u-md-2 am-form-label">提现金额统计:</label>
                                <div class="am-u-sm-12 am-u-lg-3 am-u-md-3 am-u-end">
                                    <input type="text" readonly="true"  value="${team.dividendTwo}" id="doc-ipt-3" placeholder="" style="background:none;border:1px solid rgba(255, 255, 255, 0.2);color:#fff;">
                                </div>
                            </div>

                            <div class="am-form-group">
                                <label for="doc-ipt-3" class="am-u-sm-12 am-u-lg-2 am-u-md-2 am-form-label">买入金额统计:</label>
                                <div class="am-u-sm-12 am-u-lg-3 am-u-md-3 am-u-end">
                                    <input type="text" readonly="true"  value="${team.dividendThree}" id="doc-ipt-3" placeholder="" style="background:none;border:1px solid rgba(255, 255, 255, 0.2);color:#fff;">
                                </div>
                            </div>

                             <div class="am-form-group">
                                <label for="doc-ipt-3" class="am-u-sm-12 am-u-lg-2 am-u-md-2 am-form-label">卖出金额统计:</label>
                                <div class="am-u-sm-12 am-u-lg-3 am-u-md-3 am-u-end">
                                    <input type="text" readonly="true"  value="${team.dividendFour}" id="doc-ipt-3" placeholder="" style="background:none;border:1px solid rgba(255, 255, 255, 0.2);color:#fff;">
                                </div>
                            </div>

                            <div class="am-form-group">
                                <label for="doc-ipt-3" class="am-u-sm-12 am-u-lg-2 am-u-md-2 am-form-label">盈亏金额统计:</label>
                                <div class="am-u-sm-12 am-u-lg-3 am-u-md-3 am-u-end">
                                    <input type="text" readonly="true"  value="${team.dividendFive}" id="doc-ipt-3" placeholder="" style="background:none;border:1px solid rgba(255, 255, 255, 0.2);color:#fff;">
                                </div>
                            </div>


                            <div class="am-form-group">
                                <label for="doc-ipt-3" class="am-u-sm-12 am-u-lg-2 am-u-md-2 am-form-label">订货金额统计:</label>
                                <div class="am-u-sm-12 am-u-lg-3 am-u-md-3 am-u-end">
                                    <input type="text" readonly="true"  value="${team.dividendSix}" id="doc-ipt-3" placeholder="" style="background:none;border:1px solid rgba(255, 255, 255, 0.2);color:#fff;">
                                </div>
                            </div>

                            <div class="am-form-group">
                                <label for="doc-ipt-3" class="am-u-sm-12 am-u-lg-2 am-u-md-2 am-form-label">手续费统计:</label>
                                <div class="am-u-sm-12 am-u-lg-3 am-u-md-3 am-u-end">
                                    <input type="text" readonly="true"  value="${team.dividendSeven}" id="doc-ipt-3" placeholder="" style="background:none;border:1px solid rgba(255, 255, 255, 0.2);color:#fff;">
                                </div>
                            </div>

                            <div class="am-form-group">
                                <label for="doc-ipt-3" class="am-u-sm-12 am-u-lg-2 am-u-md-2 am-form-label">艺术品资格购买统计:</label>
                                <div class="am-u-sm-12 am-u-lg-3 am-u-md-3 am-u-end">
                                    <input type="text" readonly="true"  value="${team.dividendEight}" id="doc-ipt-3" placeholder="" style="background:none;border:1px solid rgba(255, 255, 255, 0.2);color:#fff;">
                                </div>
                            </div>

                            <div class="am-form-group">
                                <label for="doc-ipt-3" class="am-u-sm-12 am-u-lg-2 am-u-md-2 am-form-label">积分商城统计:</label>
                                <div class="am-u-sm-12 am-u-lg-3 am-u-md-3 am-u-end">
                                    <input type="text" readonly="true"  value="${team.dividendNine}" id="doc-ipt-3" placeholder="" style="background:none;border:1px solid rgba(255, 255, 255, 0.2);color:#fff;">
                                </div>
                            </div>

                            <div class="am-form-group">
                                <label for="doc-ipt-3" class="am-u-sm-12 am-u-lg-2 am-u-md-2 am-form-label">苏陕商城统计:</label>
                                <div class="am-u-sm-12 am-u-lg-3 am-u-md-3 am-u-end">
                                    <input type="text" readonly="true"  value="${team.dividendTen}" id="doc-ipt-3" placeholder="" style="background:none;border:1px solid rgba(255, 255, 255, 0.2);color:#fff;">
                                </div>
                            </div>
                        </form>
                    </div>

                    
                </div>
            </div>
        </div>

    </div>

</div>

<script type="text/javascript">
    $(document).ready(function() {

    });
    function page(n,s){
        $("#pageNo").val(n);
        $("#pageSize").val(s);
        $("#searchForm").submit();
        return false;
    }

    layui.use(['layer','qyForm'], function(){
        var layer = layui.layer;

    });
</script>

</body>
</html>
