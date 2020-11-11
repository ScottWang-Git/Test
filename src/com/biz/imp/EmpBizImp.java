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
			//��ȡ�����id
			Integer eid=daoservice.getEmpmapper().findMaxId();
			//����н��
			Salary sa=new Salary(eid,emp.getEmoney());
			daoservice.getSalarymapper().save(sa);
			//��ȡԱ����������
			String[] wids=emp.getWids();
			if(wids!=null&&wids.length>0){
				for(int i=0;i<wids.length;i++){
					//Ա����������һ��Ա��id��Ӧ�Ը�����id
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
			//����н��
			//��ȡԭ����н��
			Salary oldsa=daoservice.getSalarymapper().findSalaryByEid(emp.getEid());
			if(oldsa!=null&&oldsa.getEmoney()!=null){
				oldsa.setEmoney(emp.getEmoney());
				daoservice.getSalarymapper().updateByEid(oldsa);
			}else{
				Salary sa=new Salary(emp.getEid(),emp.getEmoney());
				daoservice.getSalarymapper().save(sa);
			}
			//����Ա������
			//��ȡԭ����Ա������
			List<Welfare> lswf=daoservice.getEmpwelfaremapper().findByEid(emp.getEid());
			if(lswf!=null&&lswf.size()>0){
				//�����Ϊ�գ�����ɾ��Ա������
				daoservice.getEmpwelfaremapper().delByEid(emp.getEid());
			}
			//���Ϊ�գ�������ɾ��ԭ��Ա������������Ӹ���
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
		//��ɾ���ӱ�(Ա���������н�ʱ�)
		daoservice.getEmpwelfaremapper().delByEid(eid);
		daoservice.getSalarymapper().delByEid(eid);
		//��ɾ������(Ա����)
		int code=daoservice.getEmpmapper().delById(eid);
		if(code>0){
			return true;
		}
		return false;
	}

	@Override
	public Emp findById(Integer eid) {
		Emp oldemp=daoservice.getEmpmapper().findById(eid);
		
		//��ȡн��
		Salary oldsa=daoservice.getSalarymapper().findSalaryByEid(oldemp.getEid());
		if(oldsa!=null&&oldsa.getEmoney()!=null){
			oldsa.setEmoney(oldemp.getEmoney());
			daoservice.getSalarymapper().updateByEid(oldsa);
		}else{
			Salary sa=new Salary(oldemp.getEid(),oldemp.getEmoney());
			daoservice.getSalarymapper().save(sa);
		}
		//����Ա������
		//��ȡԭ����Ա������
		List<Welfare> lswf=daoservice.getEmpwelfaremapper().findByEid(oldemp.getEid());
		if(lswf!=null&&lswf.size()>0){
		//������������
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
