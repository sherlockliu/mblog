package cn.magicstudio.java.mblog.web.controller;
import java.util.List;

import javax.annotation.Resource;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.magicstudio.mblog.model.Managers;
import cn.magicstuido.mblog.service.interfaces.IManagers;
import cn.wonhigh.retail.backend.utils.JsonUtils;

@Controller
@RequestMapping("/mblog/home")
public class IndexController {
	
	@Resource
	private IManagers managerService;

	/**
	 * 初始化科目类型
	 * @return
	 */
	@RequestMapping("/index")
	@ResponseBody
	public String index(){
		return "hello";
	}
	
	@RequestMapping("/list")
	@ResponseBody
	public String list(){
		String json = "";
		try{
		   List<Managers> list = this.managerService.getManagers();
		   json = JsonUtils.toJson(list);
		}catch(Exception ex){
			//log
		}
		return json;
	}
}
