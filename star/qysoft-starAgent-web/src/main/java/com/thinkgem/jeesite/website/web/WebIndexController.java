/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.website.web;

import com.thinkgem.jeesite.common.config.EnumUtil;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.security.shiro.session.SessionDAO2;
import com.thinkgem.jeesite.common.utils.CookieUtils;
import com.thinkgem.jeesite.common.utils.DateUtils2;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.common.utils.VerifyUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.cms.entity.Site;
import com.thinkgem.jeesite.modules.cms.utils.CmsUtils;
import com.thinkgem.jeesite.modules.md.entity.MdTrade;
import com.thinkgem.jeesite.modules.md.entity.MdTradeLog;
import com.thinkgem.jeesite.modules.md.entity.TradeType;
import com.thinkgem.jeesite.modules.md.service.MdTradeLogService;
import com.thinkgem.jeesite.modules.md.service.MdTradeService;
import com.thinkgem.jeesite.modules.star.entity.EverydayHold;
import com.thinkgem.jeesite.modules.star.service.EverydayHoldService;
import com.thinkgem.jeesite.modules.star.service.UserOutGoldService;
import com.thinkgem.jeesite.modules.sys.entity.Principal;
import com.thinkgem.jeesite.modules.sys.utils.UserInfoUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.modules.trans.entity.TransGoods;
import com.thinkgem.jeesite.modules.trans.entity.TransOrder;
import com.thinkgem.jeesite.modules.trans.service.TransGoodsService;
import com.thinkgem.jeesite.modules.trans.service.TransOrderService;
import com.thinkgem.jeesite.modules.user.dao.UserUserinfoDao;
import com.thinkgem.jeesite.modules.user.entity.UserAccountchange;
import com.thinkgem.jeesite.modules.user.entity.UserUserinfo;
import com.thinkgem.jeesite.modules.user.service.UserAccountchangeService;
import com.thinkgem.jeesite.modules.user.service.UserExStarService;
import com.thinkgem.jeesite.modules.user.service.UserUserinfoService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

/**
 * 网站Controller
 * @author ThinkGem
 * @version 2013-5-29
 */
@Controller
@RequestMapping(value = "${frontPath}")
public class WebIndexController extends BaseController{
	@Autowired
	private SessionDAO2 sessionDAO;
	@Autowired
	private UserUserinfoService userUserinfoService;
	@Autowired
	private UserAccountchangeService userAccountchangeService;
	@Autowired
	private TransGoodsService transGoodsService;
	@Autowired
	private EverydayHoldService everydayHoldService;
	@Autowired
	private UserOutGoldService userOutGoldService;
	@Autowired
	private MdTradeLogService mdTradeLogService;
	@Autowired
	private MdTradeService mdTradeService;
	@Autowired
	private TransOrderService transOrderService ;
	@Autowired
	private UserExStarService userExStarService;
	@Autowired
	private UserUserinfoDao userUserinfoDao;


	@ModelAttribute
	public Site init( Model model,@RequestParam(required = false) String menu) {
		Site site = CmsUtils.getSite(Site.defaultSiteId());
		UserUserinfo userinfo = userUserinfoService.get(UserInfoUtils.getUser().getId());
		if(userinfo  == null){
			model.addAttribute("message", "登录超时,请稍后重试!");
			return site;
		}
		model.addAttribute("userInfo",userinfo );

		UserUserinfo userUserinfo = new UserUserinfo();
		userUserinfo.setUserName(UserInfoUtils.getUser().getUserName());
		Page <UserUserinfo> page = mdTradeLogService.findAgentVolume(new Page<UserUserinfo>(),new MdTradeLog(),userUserinfo);
		model.addAttribute("site", site);
		BigDecimal price = BigDecimal.ZERO;
		if(page.getList().size()>0){
			price = page.getList().get(0).getMoney6();
		}
		model.addAttribute("userLoginVolume",price);
		return site;
	}

	/**
	 * 网站首页
	 */
	@RequestMapping(value = "")
	public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
		Site site = CmsUtils.getSite(Site.defaultSiteId());
		Principal principal = UserInfoUtils.getPrincipal();
		if(principal == null){
			return "redirect:" + frontPath + "/login";
		}
		// 登录成功后，验证码计算器清零
		WebLoginController.isValidateCodeLogin(principal.getLoginName(), false, true);
		
		if (logger.isDebugEnabled()){
			logger.debug("show index, active session size: {}", sessionDAO.getActiveSessions(false).size());
		}
		
		// 如果已登录，再次访问主页，则退出原账号。
		if (Global.TRUE.equals(Global.getConfig("notAllowRefreshIndex"))){
			String logined = CookieUtils.getCookie(request, "LOGINED");
			if (StringUtils2.isBlank(logined) || "false".equals(logined)){
				CookieUtils.setCookie(response, "LOGINED", "true");
			}else if (StringUtils2.equals(logined, "true")){
				UserInfoUtils.getSubject().logout();
				return "redirect:" + frontPath + "/login";
			}
		}
		
		
		// 如果是手机登录，则返回JSON字符串
		if (principal.isMobileLogin()){
			return success("登录成功!",response,model);

		}


		model.addAttribute("menu", 1);
		return themesPath+site.getTheme()+"/index";
	}

	/**
	 * 我的团队列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = {"/user/userTeamList"})
	public String userTeamList( UserUserinfo userUserinfo,HttpServletRequest request, HttpServletResponse response, Model model) throws ValidationException {
		Site site = CmsUtils.getSite(Site.defaultSiteId());
		//查询子级
		if(StringUtils2.isBlank(userUserinfo.getParentName())){
			model.addAttribute("message", "上级用户不存在!");
			return "redirect:" + frontPath + "/login";
		}
		UserUserinfo prent = userUserinfoService.getByName(userUserinfo.getParentName());
		Page<UserUserinfo> page = userUserinfoService.findPage(new Page<UserUserinfo>(request, response), userUserinfo);
		model.addAttribute("parentUser", prent);
		model.addAttribute("page", page);
		model.addAttribute("menu", 2);
		return themesPath+site.getTheme()+"/userTeamList";
	}

	/**
	 * 会员持仓列表查询
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/user/userHoldGoods" )
	public String userHoldGoods(TransGoods goods , HttpServletRequest request, HttpServletResponse response, Model model) {

		Site site = CmsUtils.getSite(Site.defaultSiteId());
		String userName = UserInfoUtils.getUser().getUserName();
		UserUserinfo userinfo = userUserinfoService.getByName(userName);
		Page<TransGoods> page = null;
		if(userinfo != null){
			if(EnumUtil.YesNO.YES.toString().equals(userinfo.getIsUsercenter())){
				goods.setGroupId(userinfo.getUsercenterAddress());
			}
			if(!EnumUtil.YesNO.NO.toString().equals(userinfo.getUserType())){
				goods.setParentListLike(userinfo.getId());
		}
			String userRemark = request.getParameter("userRemark");
			goods.setStatus(EnumUtil.GoodsType.hold.toString());
			goods.setUserRemark(userRemark);
			page = transGoodsService.findPage(new Page<TransGoods>(request, response),goods);
		}
		model.addAttribute("page", page);
		model.addAttribute("menu", 4);
		return themesPath+site.getTheme()+"/userHoldGoodsList";
	}

	/**
	 * 承销商出金
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/user/outGold" )
	public String outGold(HttpServletRequest request, HttpServletResponse response, Model model) {
		String userName = UserInfoUtils.getUser().getUserName();
		String money = request.getParameter("money");
		if (StringUtils2.isBlank(userName)) {
			model.addAttribute("errorCode", 1002);
			return error("登录超时,请重新登录!", response, model);
		}
		try {
			userOutGoldService.userOutGold(userName,new BigDecimal(money));
			return success("出金成功!", response, model);
		} catch (ValidationException e) {
			return error(e.getMessage(), response, model);
		}
	}

	/**
	 * 指定下级代理等级
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/user/setUserType" )
	public String setUserType(HttpServletRequest request, HttpServletResponse response, Model model) {
		String userName = UserInfoUtils.getUser().getUserName();
		String upName = request.getParameter("upName");
		String userType = request.getParameter("userType");
		String  levelNumber = request.getParameter("levelNumber");
		if(!VerifyUtils.isNumeric(levelNumber)){
			return error("号段格式错误", response, model);
		}
		UserUserinfo userinfo = userUserinfoService.getByName(userName);
		if (userinfo == null) {
			return error("登录超时,请重新登录!", response, model);
		}
		UserUserinfo byName = userUserinfoService.getByName(upName);
		if (byName == null) {
			return error("升级用户不存在", response, model);
		}
		if(!EnumUtil.YesNO.NO.toString().equals(byName.getIsUsercenter())){
			return error("该用户已被指定为承销商,不可再设置为代理!", response, model);
		}
		try {
			userExStarService.updateUserType(byName.getId(),userType,levelNumber);
		} catch (Exception e) {
			return error("失败:"+e.getMessage(), response, model);
		}
		return success("指定代理成功!", response, model);
	}

	/**
	 * 日持仓列表查询
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/user/userDayHoldGoods" )
	public String userDayHoldGoods(EverydayHold everydayHold,HttpServletRequest request, HttpServletResponse response, Model model) {
		Site site = CmsUtils.getSite(Site.defaultSiteId());
		UserUserinfo userUserinfo = userUserinfoService.getByName(UserInfoUtils.getUser().getUserName());
		if(userUserinfo == null){
			model.addAttribute("message", "登录超时,请稍后重试!");
			return "redirect:" + frontPath + "/login";
		}
		if(EnumUtil.YesNO.YES.toString().equals(userUserinfo.getIsUsercenter())){
			everydayHold.setGroupId(userUserinfo.getUsercenterAddress());
		}
		if(!EnumUtil.YesNO.NO.toString().equals(userUserinfo.getUserType())){
			everydayHold.setParentName(userUserinfo.getUserName());
		}
		Page<EverydayHold> page = everydayHoldService.findPage(new Page<EverydayHold>(request, response),everydayHold);
		model.addAttribute("page", page);
		model.addAttribute("menu", 8);
		return themesPath+site.getTheme()+"/userDayHoldGoodsList";
	}

	/**
	 * 团队账变明细查询
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/user/userAcount" )
	public String userAcount(UserAccountchange userAccountchange, HttpServletRequest request, HttpServletResponse response, Model model) {

		Site site = CmsUtils.getSite(Site.defaultSiteId());
		String id = UserInfoUtils.getUser().getId();
		if(StringUtils2.isBlank(id)){
			model.addAttribute("message", "登录超时,请稍后重试!");
			return "redirect:" + frontPath + "/login";
		}
		UserUserinfo userUserinfo = userAccountchange.getUserUserinfo();
		if( userUserinfo == null){
			userUserinfo = new UserUserinfo();
		}
		userUserinfo.setParentListLike(id);
		userAccountchange.setUserUserinfo(userUserinfo);
		Page<UserAccountchange> page = userAccountchangeService.findPage(new Page<UserAccountchange>(request, response),userAccountchange);
		model.addAttribute("page", page);
		model.addAttribute("menu", 3);
		return themesPath+site.getTheme()+"/userAcountList";
	}

	/**
	 * 团队账变明细查询
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/user/teamAcount" )
	public String teamAcount(UserAccountchange userAccountchange, HttpServletRequest request, HttpServletResponse response, Model model) {

		Site site = CmsUtils.getSite(Site.defaultSiteId());
		String userName = UserInfoUtils.getUser().getUserName();
		if(StringUtils2.isBlank(userName)){
			model.addAttribute("message", "登录超时,请稍后重试!");
			return "redirect:" + frontPath + "/login";
		}
		userAccountchange.setUserName(userName);
		Page<UserAccountchange> page = userAccountchangeService.findPage(new Page<UserAccountchange>(request, response),userAccountchange);
		model.addAttribute("page", page);
		model.addAttribute("menu", 6);
		return themesPath+site.getTheme()+"/teamAcountList";
	}

	/**
	 * 团队交易额查询
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/user/teamVolume" )
	public String teamVolume(MdTradeLog mdTradeLog, HttpServletRequest request, HttpServletResponse response, Model model) {

		Site site = CmsUtils.getSite(Site.defaultSiteId());
		String userName = request.getParameter("userName");
		String id = UserInfoUtils.getUser().getId();
		if(StringUtils2.isBlank(id)){
			model.addAttribute("message", "登录超时,请稍后重试!");
			return "redirect:" + frontPath + "/login";
		}
		UserUserinfo userUserinfo = new UserUserinfo();
		userUserinfo.setParentListLike(id);
		userUserinfo.setUserName(userName);
		Page <UserUserinfo> page = mdTradeLogService.findAgentVolume(new Page<UserUserinfo>(request, response),mdTradeLog,userUserinfo);
		model.addAttribute("page", page);
		model.addAttribute("createDateBegin", DateUtils2.parseDate(mdTradeLog.getCreateDateBegin()));
		model.addAttribute("createDateEnd",  DateUtils2.parseDate(mdTradeLog.getCreateDateEnd()));
		model.addAttribute("menu", 11);
		return themesPath+site.getTheme()+"/teamVolumeList";
	}

	/**
	 * 交易记录
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/user/transLog" )
	public String transLog(MdTradeLog mdTradeLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Site site = CmsUtils.getSite(Site.defaultSiteId());
		String userName = UserInfoUtils.getUser().getUserName();
		UserUserinfo userinfo = userUserinfoService.getByName(userName);
		if(userinfo == null){
			model.addAttribute("message", "登录超时,请稍后重试!");
			return "redirect:" + frontPath + "/login";
		}
		mdTradeLog.setGroupId(userinfo.getUsercenterAddress());
		Page<MdTradeLog> page = mdTradeLogService.findPage(new Page<MdTradeLog>(request, response), mdTradeLog);
		model.addAttribute("page", page);
		model.addAttribute("menu", 5);
		return themesPath+site.getTheme()+"/transLogList";
	}

	/**
	 * 查询所有交易中的产品
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/user/transingLog" )
	public String transingLog(MdTrade mdTrade, HttpServletRequest request, HttpServletResponse response, Model model) {

		Site site = CmsUtils.getSite(Site.defaultSiteId());
		String userName = UserInfoUtils.getUser().getUserName();
		UserUserinfo userinfo = userUserinfoService.getByName(userName);
		if(userinfo == null){
			model.addAttribute("message", "登录超时,请稍后重试!");
			return "redirect:" + frontPath + "/login";
		}
		mdTrade.setGroupId(userinfo.getUsercenterAddress());
		Page<MdTrade> page = mdTradeService.findPage(new Page<MdTrade>(request, response), mdTrade);
		model.addAttribute("menu", 7);
		model.addAttribute("page", page);
		return themesPath+site.getTheme()+"/transingLogList";
	}

	/**
	 * 盈亏查询
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/user/profitAndLoss" )
	public String profitAndLoss(MdTradeLog mdTradeLog, HttpServletRequest request, HttpServletResponse response, Model model) {
		Site site = CmsUtils.getSite(Site.defaultSiteId());
		String userName = UserInfoUtils.getUser().getUserName();
		UserUserinfo userinfo = userUserinfoService.getByName(userName);
		if(userinfo == null){
			model.addAttribute("message", "登录超时,请稍后重试!");
			return "redirect:" + frontPath + "/login";
		}
		mdTradeLog.setParentListLike(userinfo.getId());
		Page<MdTradeLog> page = mdTradeLogService.findKunSunRanklist(new Page<MdTradeLog>(request, response),mdTradeLog);
		model.addAttribute("menu", 9);
		model.addAttribute("page", page);
		return themesPath+site.getTheme()+"/profitList";
	}

	/**
	 * 承销商查询兑换记录
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/user/findOrderList")
	public String findOrderList(TransOrder transOrder,HttpServletRequest request ,HttpServletResponse response , Model model){
		Site site = CmsUtils.getSite(Site.defaultSiteId());
		UserUserinfo userinfo = UserInfoUtils.getUser();
		if(userinfo == null){
			model.addAttribute("message", "登录超时,请稍后重试!");
			return "redirect:" + frontPath + "/login";
		}
		transOrder.setGroupId(userinfo.getUsercenterAddress());
		model.addAttribute("menu", 10);
		model.addAttribute("page",transOrderService.findPage(new Page<TransOrder>(request,response),transOrder));
		return themesPath+site.getTheme()+"/orderList";
	}

	/**
	 * 承销商修改兑换状态
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/user/updateOrderType")
	public String updateOrderType(HttpServletRequest request ,HttpServletResponse response , Model model){
		String id = request.getParameter("id");
		TransOrder transOrder = transOrderService.get(id);
		if(transOrder == null){
			return error("修改的订单不存在",response,model);
		}
		transOrder.setType(EnumUtil.CheckType.success.toString());
		transOrderService.updateType(transOrder);
		return success("操作成功",response,model);
	}

	/**
	 * 承销商修改兑换状态
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/user/updadeIsShop")
	public String updadeIsShop(HttpServletRequest request ,HttpServletResponse response , Model model) {
		String userName = request.getParameter("userName");
		String isShop = request.getParameter("isShop");
		userUserinfoDao.updateIsShop(userName, isShop);
		return success("操作成功",response,model);
	}
}
