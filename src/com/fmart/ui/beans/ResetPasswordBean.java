package com.fmart.ui.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.fmart.framework.hibernate.FMartGenericDAO;
import com.fmart.hibernate.pojos.Dealer;
import com.fmart.hibernate.pojos.Employee;
import com.fmart.hibernate.pojos.ProductGrp;

/**
 * 
 * @author Sachin
 */
@ManagedBean
@ViewScoped
public class ResetPasswordBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1830025391503108991L;
	private Employee[] employees;
	private Employee[] selectedEmployees;
	
	
	
	public Employee[] getSelectedEmployees() {
		return selectedEmployees;
	}
	public void setSelectedEmployees(Employee[] selectedEmployees) {
		this.selectedEmployees = selectedEmployees;
	}
	public Employee[] getEmployees() {
		return employees;
	}
	public void setEmployees(Employee[] employees) {
		this.employees = employees;
	}
	public List<Employee> getEmplList() {
		return emplList;
	}
	public void setEmplList(List<Employee> emplList) {
		this.emplList = emplList;
	}
	private List<Employee> emplList = new ArrayList<Employee>();
	
	public List<Employee> populateEmployeeList() {
		FMartGenericDAO dao = new FMartGenericDAO();
		this.emplList = dao.findAll(Employee.class);
		return this.emplList;
	}
	public void resetEmployeePassword(ActionEvent actionEvent){
		try {
			FMartGenericDAO dao = new FMartGenericDAO();
			for(Employee emp : getSelectedEmployees()){
				emp.setPassword(emp.getUsername());
				dao.update(emp,emp.getId());
			}
			
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							"Error Updating Employee Password"));
			return;
		}
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
						"Password Reset Successfull"));
	}
	

}
