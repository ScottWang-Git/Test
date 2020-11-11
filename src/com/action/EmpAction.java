package com.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.po.Emp;
import com.service.BizService;
import com.util.AjaxUtils;
@Controller
public class EmpAction implements IAction {

	@Resource(name="BizService")
	private BizService bizService;
	
	public BizService getBizService() {
		return bizService;
	}

	public void setBizService(BizService bizService) {
		this.bizService = bizService;
	}

	@Override
	@RequestMapping(value="save_emp.do")
	public String save(HttpServletRequest request, HttpServletResponse response, Emp emp) {
		boolean flag=bizService.getEmpBiz().save(emp);
		if(flag){
			AjaxUtils.printString(response, 1+"");
		}else{
			AjaxUtils.printString(response, 0+"");
		}
		return null;
	}

	@Override
	@RequestMapping(value="update_emp.do")
	public String update(HttpServletRequest request, HttpServletResponse response, Emp emp) {
		boolean flag=bizService.getEmpBiz().update(emp);
		if(flag){
			AjaxUtils.printString(response, 1+"");
		}else{
			AjaxUtils.printString(response, 0+"");
		}
		return null;
	}

	@Override
	@RequestMapping(value="delById_emp.do")
	public String delById(HttpServletRequest request, HttpServletResponse response, Integer eid) {
		boolean flag=bizService.getEmpBiz().delById(eid);
		if(flag){
			AjaxUtils.printString(response, 1+"");
		}else{
			AjaxUtils.printString(response, 0+"");
		}
		return null;
	}

	@Override
	@RequestMapping(value="findById_emp.do")
	public String findById(HttpServletRequest request, HttpServletResponse response, Integer eid) {
		Emp oldemp=bizService.getEmpBiz().findById(eid);
		//¹ýÂË
		PropertyFilter propertyFilter=AjaxUtils.filterProperts("birthday","pic");
		String jsonstr=JSONObject.toJSONString(oldemp,propertyFilter,SerializerFeature.DisableCircularReferenceDetect);
		AjaxUtils.printString(response, jsonstr);
		return null;
	}

	@Override
	@RequestMapping(value="findDetail_emp.do")
	public String findDetail(HttpServletRequest request, HttpServletResponse response, Integer eid) {
		Emp oldemp=bizService.getEmpBiz().findById(eid);
		//¹ýÂË
		PropertyFilter propertyFilter=AjaxUtils.filterProperts("birthday","pic");
		String jsonstr=JSONObject.toJSONString(oldemp,propertyFilter,SerializerFeature.DisableCircularReferenceDetect);
		AjaxUtils.printString(response, jsonstr);
		return null;
	}

	@Override
	@RequestMapping(value="findPageAll_emp.do")
	public String findPageAll(HttpServletRequest request, HttpServletResponse response, Integer page, Integer rows) {
		
		return null;
	}

	@Override
	@RequestMapping(value="doinit_emp.do")
	public String doinit(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

}
