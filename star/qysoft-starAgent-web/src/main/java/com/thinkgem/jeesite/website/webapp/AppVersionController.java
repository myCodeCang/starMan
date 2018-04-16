package com.thinkgem.jeesite.website.webapp;

import com.alibaba.druid.support.json.JSONUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.utils.annotations.RecordLog;
import com.thinkgem.jeesite.modules.user.entity.UserOptions;
import com.thinkgem.jeesite.modules.user.service.UserOptionsService;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * App升级
 * Created by yankai on 2017/6/8.
 */
@Controller
@RequestMapping(value = "${frontPath}/app/version")
public class AppVersionController extends BaseController {
    @Resource
    private UserOptionsService userOptionsService;

    @RecordLog(logTitle = "常规配置-系统参数配置-App版本设置")
    @RequestMapping(value = "/getVersion", method = RequestMethod.POST)
    public String getVersion(HttpServletRequest request, HttpServletResponse response, Model model) {
        UserOptions appVersionInfo = userOptionsService.getByOptionName("system_app_version");
        if (null == appVersionInfo) {
            return success("成功!!", response, model);
        }

        String optionValue = appVersionInfo.getOptionValue();
        if (optionValue == null) {
            return success("成功!!", response, model);
        }

        Map<String, String> result = (Map<String, String>) JSONUtils.parse(optionValue);
        if (MapUtils.isEmpty(result)) {
            return success("成功!!", response, model);
        }

        model.addAttribute("data", result);
        return success("成功!!", response, model);
    }
}
