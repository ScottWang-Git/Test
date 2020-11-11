package com.biz.imp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biz.IEmpBiz;
import com.po.Emp;
import com.po.EmpWelfare;
import com.po.PageBean;
import com.po.Salary;
import com.po.Welfare;
import com.service.DaoService;
@Service("EmpBiz")
@Transactional
public class EmpBizImp implements IEmpBiz {

	@Resource(name="DaoService")
	private DaoService daoservice;
	
	public DaoService getDaoservice() {
		return daoservice;
	}
	public void setDaoservice(DaoService daoservice) {
		this.daoservice = daoservice;
	}
	
	@Override
	public boolean save(Emp emp) {
		int code=daoservice.getEmpmapper().save(emp);
		if(code>0){
			//获取保存的id
			Integer eid=daoservice.getEmpmapper().findMaxId();
			//保存薪资
			Salary sa=new Salary(eid,emp.getEmoney());
			daoservice.getSalarymapper().save(sa);
			//获取员工福利数组
			String[] wids=emp.getWids();
			if(wids!=null&&wids.length>0){
				for(int i=0;i<wids.length;i++){
					//员工福利表中一个员工id对应对个福利id
					EmpWelfare ewf=new EmpWelfare(eid,new Integer(wids[i]));
					daoservice.getEmpwelfaremapper().save(ewf);
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean update(Emp emp) {
		int code=daoservice.getEmpmapper().update(emp);
		if(code>0){
			//更新薪资
			//获取原来的薪资
			Salary oldsa=daoservice.getSalarymapper().findSalaryByEid(emp.getEid());
			if(oldsa!=null&&oldsa.getEmoney()!=null){
				oldsa.setEmoney(emp.getEmoney());
				daoservice.getSalarymapper().updateByEid(oldsa);
			}else{
				Salary sa=new Salary(emp.getEid(),emp.getEmoney());
				daoservice.getSalarymapper().save(sa);
			}
			//更新员工福利
			//获取原来的员工福利
			List<Welfare> lswf=daoservice.getEmpwelfaremapper().findByEid(emp.getEid());
			if(lswf!=null&&lswf.size()>0){
				//如果不为空，则先删除员工福利
				daoservice.getEmpwelfaremapper().delByEid(emp.getEid());
			}
			//如果为空，或者是删除原来员工福利后，则添加更新
			String[] wids=emp.getWids();
			if(wids!=null&&wids.length>0){
				for(int i=0;i<wids.length;i++){
					EmpWelfare ewf=new EmpWelfare(emp.getEid(),new Integer(wids[i]));
					daoservice.getEmpwelfaremapper().save(ewf);
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean delById(Integer eid) {
		//先删除子表(员工福利表和薪资表)
		daoservice.getEmpwelfaremapper().delByEid(eid);
		daoservice.getSalarymapper().delByEid(eid);
		//再删除主表(员工表)
		int code=daoservice.getEmpmapper().delById(eid);
		if(code>0){
			return true;
		}
		return false;
	}

	@Override
	public Emp findById(Integer eid) {
		Emp oldemp=daoservice.getEmpmapper().findById(eid);
		
		//获取薪资
		Salary oldsa=daoservice.getSalarymapper().findSalaryByEid(oldemp.getEid());
		if(oldsa!=null&&oldsa.getEmoney()!=null){
			oldsa.setEmoney(oldemp.getEmoney());
			daoservice.getSalarymapper().updateByEid(oldsa);
		}else{
			Salary sa=new Salary(oldemp.getEid(),oldemp.getEmoney());
			daoservice.getSalarymapper().save(sa);
		}
		//更新员工福利
		//获取原来的员工福利
		List<Welfare> lswf=daoservice.getEmpwelfaremapper().findByEid(oldemp.getEid());
		if(lswf!=null&&lswf.size()>0){
		//创建福利数组
			String[] wids=new String[lswf.size()];
			for(int i=0;i<wids.length;i++){
				Welfare wf=lswf.get(i);
				wids[i]=wf.getWid().toString();
			}
			oldemp.setWids(wids);
		}
		oldemp.setLswf(lswf);
		return oldemp;
	}

	@Override
	public List<Emp> findPageAll(PageBean pb) {
		if(pb!=null){
			return daoservice.getEmpmapper().findPageAll(pb);
		}
		return null;
	}

	@Override
	public int findMaxRows() {
		// TODO Auto-generated method stub
		return daoservice.getEmpmapper().findMaxRows();
	}

}
