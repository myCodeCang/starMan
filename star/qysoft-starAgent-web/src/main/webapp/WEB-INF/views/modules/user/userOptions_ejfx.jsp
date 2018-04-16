<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>配置管理</title>
    <meta name="decorator" content="default"/>
    <script type="text/javascript">
        $(document).ready(function () {
            //$("#name").focus();
            $("[name=inputForm]").validate({
                submitHandler: function (form) {
                    loading('正在提交，请稍等...');
                    form.submit();
                },
                errorContainer: "#messageBox",
                errorPlacement: function (error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
            if(${options.system_trans.trans_open}){
                $("#isOpenSwitch").attr("checked",true);
                $("#transOpenSetDiv").hide();
            }else{
                $("#isOpenSwitch").attr("checked",false);
                $("#transOpenSetDiv").show();
            }
        });


    </script>


</head>
<body>
<sys:message content="${message}"/>
<div class="layui-tab layui-tab-brief" lay-filter="optionlayuitab">
    <ul class="layui-tab-title">
        <c:forEach items="${optionList}" var="dic" varStatus="status">

            <li lay-id="${dic.optionName}"
                class="${dic.optionName  eq selOptLabel ? 'layui-this':''}">${dic.optionLabel } [${dic.optionName }]
            </li>
        </c:forEach>
    </ul>
    <div class="layui-tab-content">

        <div class="layui-tab-item layui-show">

            <form name="inputForm" modelAttribute="optionValue"
                  action="${ctx}/user/userOptions/saveEjfxGroup" method="post"
                  class="form-horizontal layui-form">
                <input type="hidden" name="optName" value="system_ejfx"/>

                <blockquote class="layui-elem-quote">推荐奖金配置</blockquote>
                <div class="control-group">
                    <label class="control-label">直推业绩奖励：</label>
                    <div class="controls">
                        <input type="number" step="0.01" name="performance_one"
                               value="${options.system_ejfx.performance_one }" htmlEscape="false"
                               class="input-xlarge required"/>
                        <span class="help-inline"><font
                                color="red">*</font> 标识:[performance_one] </span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label">直推商城消费奖励：</label>
                    <div class="controls">
                        <input type="number" step="0.01" name="shop_one"
                               value="${options.system_ejfx.shop_one }" htmlEscape="false"
                               class="input-xlarge required"/>
                        <span class="help-inline"><font
                                color="red">*</font> 标识:[shop_one] </span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label">二代业绩奖励：</label>
                    <div class="controls">
                        <input type="number" step="0.01" name="performance_two"
                               value="${options.system_ejfx.performance_two }" htmlEscape="false"
                               class="input-xlarge required"/>
                        <span class="help-inline"><font
                                color="red">*</font> 标识:[performance_two] </span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label">二代商城消费奖励：</label>
                    <div class="controls">
                        <input type="number" step="0.01" name="shop_two"
                               value="${options.system_ejfx.shop_two }" htmlEscape="false"
                               class="input-xlarge required"/>
                        <span class="help-inline"><font
                                color="red">*</font> 标识:[shop_two] </span>
                    </div>
                </div>

                <blockquote class="layui-elem-quote">团队奖金配置</blockquote>

                <div class="control-group">
                    <label class="control-label">一星会员所需直推人数：</label>
                    <div class="controls">
                        <input type="number" step="0.01" name="one_star_direct"
                               value="${options.system_ejfx.one_star_direct}" htmlEscape="false"
                               class="input-xlarge required"/>
                        <span class="help-inline"><font
                                color="red">*</font> 标识:[one_star_direct] </span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label">一星会员所需二代人数：</label>
                    <div class="controls">
                        <input type="number" step="0.01" name="one_star_two"
                               value="${options.system_ejfx.one_star_two}" htmlEscape="false"
                               class="input-xlarge required"/>
                        <span class="help-inline"><font
                                color="red">*</font> 标识:[one_star_two] </span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label">一星会员所需团队总人数：</label>
                    <div class="controls">
                        <input type="number" step="0.01" name="one_star_all"
                               value="${options.system_ejfx.one_star_all}" htmlEscape="false"
                               class="input-xlarge required"/>
                        <span class="help-inline"><font
                                color="red">*</font> 标识:[one_star_all] </span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label">一星会员享受业绩提成：</label>
                    <div class="controls">
                        <input type="number" step="0.01" name="one_star_performance"
                               value="${options.system_ejfx.one_star_performance}" htmlEscape="false"
                               class="input-xlarge required"/>
                        <span class="help-inline"><font
                                color="red">*</font> 标识:[one_star_performance] </span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label">一星会员业绩日封顶：</label>
                    <div class="controls">
                        <input type="number" step="0.01" name="one_star_dayCap"
                               value="${options.system_ejfx.one_star_dayCap}" htmlEscape="false"
                               class="input-xlarge required"/>
                        <span class="help-inline"><font
                                color="red">*</font> 标识:[one_star_dayCap] </span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">一星会员商城消费奖励：</label>
                    <div class="controls">
                        <input type="number" step="0.01" name="one_star_shop"
                               value="${options.system_ejfx.one_star_shop}" htmlEscape="false"
                               class="input-xlarge required"/>
                        <span class="help-inline"><font
                                color="red">*</font> 标识:[one_star_shop] </span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label">二星会员业绩提成：</label>
                    <div class="controls">
                        <input type="number" step="0.01" name="two_star_performance"
                               value="${options.system_ejfx.two_star_performance}" htmlEscape="false"
                               class="input-xlarge required"/>
                        <span class="help-inline"><font
                                color="red">*</font> 标识:[two_star_performance] </span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label">二星会员业绩日封顶：</label>
                    <div class="controls">
                        <input type="number" step="0.01" name="two_star_dayCap"
                               value="${options.system_ejfx.two_star_dayCap}" htmlEscape="false"
                               class="input-xlarge required"/>
                        <span class="help-inline"><font
                                color="red">*</font> 标识:[two_star_dayCap] </span>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">二星会员商城消费奖励：</label>
                    <div class="controls">
                        <input type="number" step="0.01" name="two_star_shop"
                               value="${options.system_ejfx.two_star_shop}" htmlEscape="false"
                               class="input-xlarge required"/>
                        <span class="help-inline"><font
                                color="red">*</font> 标识:[two_star_shop] </span>
                    </div>
                </div>

                <div class="control-group">
                    <label class="control-label">董事商城消费奖励：</label>
                    <div class="controls">
                        <input type="number" step="0.01" name="director_shop"
                               value="${options.system_ejfx.director_shop}" htmlEscape="false"
                               class="input-xlarge required"/>
                        <span class="help-inline"><font
                                color="red">*</font> 标识:[director_shop] </span>
                    </div>
                </div>


                <div class="form-actions">
                    <shiro:hasPermission name="user:userUserinfo:edit">
                        <input id="btnSubmit" class="btn btn-primary" type="submit"
                               value="保 存 配 置"/>&nbsp;</shiro:hasPermission>
                </div>

            </form>
        </div>
        <div class="layui-tab-item">
        </div>
        <div class="layui-tab-item">其他设置</div>
        <div class="layui-tab-item">4</div>
        <div class="layui-tab-item">5</div>
        <div class="layui-tab-item">6</div>
    </div>
</div>



<script type="text/javascript">
    var form = null;
    layui.use(['element', 'form', 'template'], function () {
        var element = layui.element();
        form = layui.form();
        element.tabChange('optionlayuitab', '${selOptLabel}'); //假设当前地址为：http://a.com#test1=222，那么选项卡会自动切换到“发送消息”这一项


        form.on('switch(shopOpenStatus)', function(data){
            if(data.elem.checked){
                $("#transOpenSetDiv").hide();

                $("#istransopen").val("1");
            }
            else{
                $("#transOpenSetDiv").show();
                $("#istransopen").val("0");
            }
        });
    });
</script>
</body>
</html>