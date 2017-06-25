package com.fmart.ui.beans;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.hibernate.Hibernate;

import com.fmart.framework.hibernate.FMartGenericDAO;
import com.fmart.hibernate.dao.EmpReportDao;
import com.fmart.hibernate.dao.EmpRoleDao;
import com.fmart.hibernate.dao.EmployeeDao;
import com.fmart.hibernate.dao.RolesDao;
import com.fmart.hibernate.pojos.EmpReport;
import com.fmart.hibernate.pojos.EmpRole;
import com.fmart.hibernate.pojos.Employee;
import com.fmart.hibernate.pojos.Report;
import com.fmart.hibernate.pojos.Roles;

/**
 * 
 * @author mvprabhu
 */
@ManagedBean
@ViewScoped
public class EmployeeBean implements Serializable {

	private Long id; // +setter

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Employee newEmployee = new Employee();
	private Employee selectedEmployee = new Employee();
	private Employee[] selectedEmployees;
	private List<Employee> employees = null;

	private String[] selectedReport;
	private String selectedRoleEmp;

	private int selectedRole;
	
	public int getSelectedRole() {
		return selectedRole;
	}

	public void setSelectedRole(int selectedRole) {
		this.selectedRole = selectedRole;
	}

	public String getSelectedRoles() {
		return selectedRoleEmp;
	}

	public void setSelectedRoles(String selectedRoleEmp) {
		this.selectedRoleEmp = selectedRoleEmp;
	}

	public String[] getSelectedReport() {
		return selectedReport;
	}

	public void setSelectedReport(String[] selectedReport) {
		this.selectedReport = selectedReport;
	}

	public Employee getNewEmployee() {
		return newEmployee;
	}

	public void setNewEmployee(Employee newEmployee) {
		this.newEmployee = newEmployee;
	}

	public Employee getSelectedEmployee() {

		return selectedEmployee;
	}

	public void setSelectedEmployee(Employee selectedEmployee) {
		if (selectedEmployee != null) {
			List<EmpRole> roles = selectedEmployee.getEmpRoles();
			List<EmpReport> reports = selectedEmployee.getEmpReports();
			selectedReport = new String[reports.size()];
			int i = 0;
			for (EmpReport empReport : reports) {
				selectedReport[i] = empReport.getReport().getId() + "";
				i++;
			}
			i = 0;
			for (EmpRole emproles : roles) {
				selectedRoleEmp = emproles.getRoles().getName();
				if(selectedRoleEmp.equalsIgnoreCase("Admin"))
					selectedRole=2;
				else if(selectedRoleEmp.equalsIgnoreCase("Manager"))
					selectedRole=3;
				else if(selectedRoleEmp.equalsIgnoreCase("Sales"))
					selectedRole=4;
				else if(selectedRoleEmp.equalsIgnoreCase("Purchase"))
					selectedRole=5;
				else if(selectedRoleEmp.equalsIgnoreCase("Sales&Purchase"))
					selectedRole=6;
				
			}
		}
		this.selectedEmployee = selectedEmployee;
	}

	public Employee[] getSelectedEmployees() {
		return selectedEmployees;
	}

	public void setSelectedEmployees(Employee[] selectedEmployees) {
		this.selectedEmployees = selectedEmployees;
	}

	public List<Employee> getEmployees() {
		if (employees == null) {
			EmployeeDao dao = new EmployeeDao();
			if (UserSession.getSession().isRole("Super Admin")) {
				this.employees = dao.findAll();
			} else {
				this.employees = dao.findByProperty("company.id", UserSession
						.getSession().getCompanyId());
			}
			if (UserSession.getSession().isRole("Manager")) {
				for (Employee emp : employees) {
					for (EmpRole r : emp.getEmpRoles())
						if (r.getRoles().getName().contains("Admin"))
							emp.setAdmin(true);
				}
			}
		}
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public boolean isViewLinkRendered() {
		return !(this.selectedEmployee.getEmpReports().isEmpty());
	}

	public List<Roles> getRolesForCurrentUser() {
		List<Roles> roles = new ArrayList<Roles>();
		RolesDao dao = new RolesDao();
		roles = dao.findAll();
		if (UserSession.getSession().isRole("Super Admin")
				|| UserSession.getSession().isRole("Admin")) {
			roles.remove(0);
		} else if (UserSession.getSession().isRole("Manager")) {
			roles.subList(0, 3).clear();
		}
		return roles;
	}

	public List<Report> getReports(){
		List<Report> reports = new ArrayList<Report>();
		FMartGenericDAO dao = new FMartGenericDAO();
		for(Report report: dao.findAll(Report.class)){
			if(UserSession.getSession().isRole("Manager") && report.getName().equalsIgnoreCase("Sales Commission") || report.getName().equalsIgnoreCase("Profit And Loss")){}
			else{
				reports.add(report);
			}
		}
		switch(selectedRole){
		case 2: //Admin
			setSelectedReport(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12"});
			break;
		case 3:	//Manager
			setSelectedReport(new String[]{"1","2","3","4","5","6","7","8","9","10","11"});
			break;
		case 6: //Sales and Purchases
			setSelectedReport(new String[]{"1","2","3","4","5","6","7","8","9","10"});
			break;
		case 4: //Sales
			setSelectedReport(new String[]{"1","2","3","4","5","6"});
			break;
		case 5: //Purchase
			setSelectedReport(new String[]{"7","8","9","10"});
			break;
		}
		return reports;
	}
	
	public List<Report> getReportsForUpdate(){
		List<Report> reports = new ArrayList<Report>();
		FMartGenericDAO dao = new FMartGenericDAO();
		for(Report report: dao.findAll(Report.class)){
			if(UserSession.getSession().isRole("Manager") && report.getName().equalsIgnoreCase("Sales Commission") || report.getName().equalsIgnoreCase("Profit And Loss")){}
			else{
				reports.add(report);
			}
		}
		return reports;
	}
	
	public List<Employee> populateEmployeeList() {
		EmployeeDao dao = new EmployeeDao();
		this.employees = dao.findAll();
		if (UserSession.getSession().isRole("Manager")) {
			for (Employee emp : employees) {
				for (EmpRole r : emp.getEmpRoles())
					if (r.getRoles().getName().contains("Admin"))
						emp.setAdmin(true);
			}
		}
		return this.employees;
	}

	public void saveEmployee(ActionEvent actionEvent) {

		EmployeeDao dao = new EmployeeDao();
		// newEmployee.setId(null);
		try {
			newEmployee.setPassword(newEmployee.getUsername());
			newEmployee.setActive(true);
			dao.save(newEmployee);
			saveEmpReportRoles(newEmployee.getId());
			saveEmpRoles(newEmployee.getId());
			newEmployee = new Employee();
			setEmployees(null);		
			selectedReport = null;
			selectedRoleEmp = null;
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							"Error Saving Employee"));
			return;
		}
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
						"Employee Saved successfully"));

	}

	private void saveEmpRoles(Integer id) {
		EmpRoleDao dao = new EmpRoleDao();
			EmpRole roles = new EmpRole();
			Employee e = new Employee();
			e.setId(id);
			Roles r = new Roles();
			r.setId(selectedRole);
			roles.setEmployee(e);
			roles.setRoles(r);
			dao.save(roles);
	}

	private void saveEmpReportRoles(Integer id) {
		EmpReportDao dao = new EmpReportDao();
		for (String reportRole : selectedReport) {
			EmpReport er = new EmpReport();
			Employee e = new Employee();
			e.setId(id);
			Report r = new Report();
			r.setId(Integer.parseInt(reportRole));
			er.setEmployee(e);
			er.setReport(r);
			dao.save(er);
		}

	}

	public void updateEmployee(ActionEvent actionEvent) {
		try {
			EmployeeDao dao = new EmployeeDao();
			String hql = "delete from EmpReport report where report.employee.id=:id";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("id", selectedEmployee.getId());
			dao.executeHQLUpdate(hql, params);

			hql = "delete from EmpRole role where role.employee.id=:id";
			params.clear();
			params = new HashMap<String, Object>();
			params.put("id", selectedEmployee.getId());
			dao.executeHQLUpdate(hql, params);
			dao.update(selectedEmployee, selectedEmployee.getId());
			// populateEmployeeList();

			saveEmpReportRoles(selectedEmployee.getId());
			saveEmpRoles(selectedEmployee.getId());
			setSelectedEmployee(new Employee());

			selectedReport = null;
			selectedRoleEmp = null;
			setEmployees(null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							"Error Updating Employee"));
			return;
		}
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
						"Employee Updated successfully"));
	}

}
