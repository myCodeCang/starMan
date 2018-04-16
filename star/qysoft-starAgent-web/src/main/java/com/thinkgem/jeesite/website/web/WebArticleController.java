/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.website.web;

import com.thinkgem.jeesite.common.exception.ValidationException;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.security.shiro.session.SessionDAO2;
import com.thinkgem.jeesite.common.utils.StringUtils2;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.cms.entity.Article;
import com.thinkgem.jeesite.modules.cms.entity.Category;
import com.thinkgem.jeesite.modules.cms.entity.CmsArticleData;
import com.thinkgem.jeesite.modules.cms.service.ArticleService;
import com.thinkgem.jeesite.modules.cms.service.CmsArticleDataService;
import com.thinkgem.jeesite.modules.md.service.TestPublishService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 文章
 * @author ThinkGem
 * @version 2013-5-29
 */
@Controller
@RequestMapping(value = "${frontPath}/webArticle")
public class WebArticleController extends BaseController {
	@Resource
	private SessionDAO2 sessionDAO2;

	@Resource
	private ArticleService articleService;

	@Resource
	private CmsArticleDataService cmsArticleDataService;

	@Resource
	private TestPublishService testPublishService;


	/**
	 * 根据条件查询文章列表 JSON
	 * 表名:
	 * 条件:
	 * 其他:
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public String  list(HttpServletRequest request, HttpServletResponse response, Model model) {
		String categoryId = request.getParameter("categoryId");
		Article article = new Article();
		if(StringUtils2.isNotBlank(categoryId)){
			Category category = new Category();
			category.setId(categoryId);
			article.setCategory(category);
		}


		Page<Article> page =  articleService.findPage(new Page<Article>(request,response), article, true);
		model.addAttribute("article",page);
		return success("成功!!",response, model);
	}

	/**
	 * 根据文章id 查询文章详情 json
	 * 表名:
	 * 条件:
	 * 其他:
	 */
	@ResponseBody
	@RequestMapping(value = "/detail", method = RequestMethod.POST)
	public String detail(HttpServletRequest request, HttpServletResponse response, Model model) {

		CmsArticleData cmsArticleData = cmsArticleDataService.get(request.getParameter("articleId"));
		model.addAttribute("article",cmsArticleData);
		return success("成功!!",response, model);
	}



	/**
	 * 根据文章keyword 查询文章详情 json
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/detailByKeyword", method = RequestMethod.POST)
	public String detailByKeyword(HttpServletRequest request, HttpServletResponse response, Model model) {
		String keywords = request.getParameter ("keywords");
		model.addAttribute("agreement",articleService.getArticleDataByKeyword (keywords));
		return success("成功!!",response, model);
	}


	/**
	 * 测试批量数据
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/testData", method = RequestMethod.GET)
	public void testData(HttpServletRequest request, HttpServletResponse response, Model model) {
	do {
			try {
				testPublishService.doSomeThing();
				Thread.sleep(500);
			} catch (ValidationException e){
				continue;
			}catch (Exception e) {
				continue;
			}
		}  while (true);
	}

}
