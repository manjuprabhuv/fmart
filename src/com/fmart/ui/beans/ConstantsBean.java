package com.fmart.ui.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.fmart.framework.hibernate.FMartGenericDAO;
import com.fmart.hibernate.dao.GodownDao;
import com.fmart.hibernate.pojos.Company;
import com.fmart.hibernate.pojos.Dealer;
import com.fmart.hibernate.pojos.EmpRole;
import com.fmart.hibernate.pojos.Employee;
import com.fmart.hibernate.pojos.ExpenseType;
import com.fmart.hibernate.pojos.Godown;
import com.fmart.hibernate.pojos.Product;
import com.fmart.hibernate.pojos.ProductGrp;
import com.fmart.hibernate.pojos.Report;
import com.fmart.hibernate.pojos.Roles;
import com.fmart.hibernate.pojos.Sale;

@ManagedBean
@SessionScoped
public class ConstantsBean implements Serializable {

	List<Roles> rolesList = new ArrayList<Roles>();	
	
	public List<Roles> getRolesList() {
		return rolesList;
	}
	
	public void setRolesList(List<Roles> rolesList) {
		this.rolesList = rolesList;
	}



	public void init(){
		FMartGenericDAO dao = new FMartGenericDAO();
		rolesList =  dao.findAll(Roles.class);
		
	}
	
	public List<Company> getCompanies(){
		FMartGenericDAO dao = new FMartGenericDAO();
		return dao.findAll(Company.class);
	}
	public List<Godown> getGodowns(){
		GodownDao dao = new GodownDao();
		return dao.findAll();
	}
	
	public List<Report> getReports(){
		List<Report> reports = new ArrayList<Report>();
		FMartGenericDAO dao = new FMartGenericDAO();
		for(Report report: dao.findAll(Report.class)){
			if(UserSession.getSession().isRole("Manager") && report.getName().equalsIgnoreCase("Sales Commission") || report.getName().equalsIgnoreCase("Profit And Loss")){}
			else
			reports.add(report);
		}
		return reports;
	}
	
	public List<Roles> getRoles(){
		if(rolesList.size()==0){
			init();
		}
		FMartGenericDAO dao = new FMartGenericDAO();
		return dao.findAll(Roles.class);
	}
	
	public List<ProductGrp> getProductGroup(){
		FMartGenericDAO dao = new FMartGenericDAO();
		return dao.findAll(ProductGrp.class);
	}
	
	public List<Employee> getEmployees(){
		FMartGenericDAO dao = new FMartGenericDAO();
		return dao.findByProperty(Employee.class,"company.id",UserSession.getSession().getCompanyId());
	}
	
}
