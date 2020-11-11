package com.mapper;

import java.util.List;

import org.springframework.stereotype.Service;

import com.po.EmpWelfare;
import com.po.Welfare;



@Service("EmpWelfareDAO")
public interface IEmpWelfareMapper {

	//����Ա������
	public int save(EmpWelfare ewf);
	//����Ա����ţ���ѯ����
	public List<Welfare> findByEid(Integer eid);
	//����Ա����ţ�ɾ������
	public int delByEid(Integer eid);
}