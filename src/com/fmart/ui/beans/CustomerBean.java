package com.fmart.ui.beans;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.fmart.hibernate.dao.CustomerDao;
import com.fmart.hibernate.pojos.Company;
import com.fmart.hibernate.pojos.Customer;
import com.fmart.hibernate.pojos.Employee;
import com.fmart.ui.utils.SessionInfo;

/**
 * 
 * @author Sachin
 */
@ManagedBean
@ViewScoped
public class CustomerBean implements Serializable {

	private Long id; // +setter

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Customer newCustomer = new Customer();
	private Customer selectedCustomer = new Customer();
	private Customer[] selectedCustomers;
	private List<Customer> customers = null;

	public Customer getNewCustomer() {
		return newCustomer;
	}

	public void setNewCustomer(Customer newCustomer) {
		this.newCustomer = newCustomer;
	}

	public Customer getSelectedCustomer() {
		return selectedCustomer;
	}

	public void setSelectedCustomer(Customer selectedCustomer) {
		this.selectedCustomer = selectedCustomer;
	}

	public Customer[] getSelectedCustomers() {
		return selectedCustomers;
	}

	public void setSelectedCustomers(Customer[] selectedCustomers) {
		this.selectedCustomers = selectedCustomers;
	}

	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}

	public List<Customer> getCustomers() {
		if (customers == null) {
			CustomerDao dao = new CustomerDao();
			this.customers = dao.findByProperty("company.id", UserSession
					.getSession().getCompanyId());
		}
		return customers;
	}

	public void saveCustomer(ActionEvent actionEvent) {

		CustomerDao dao = new CustomerDao();
		try {
			;
			Company comp = new Company();
			comp.setId(UserSession.getSession().getCompanyId());
			newCustomer.setPhoneString(newCustomer.getPhone().toString());
			newCustomer.setCompany(comp);
			SessionInfo sessionInfo = UserSession.getSession();
			newCustomer.setCreated(new Date());
			newCustomer.setUpdated(new Date());			
			newCustomer.setEmployeeByCreatedby(new Employee(sessionInfo.getEmployeeId()));
			newCustomer.setEmployeeByUpdatedby(new Employee(sessionInfo.getEmployeeId()));
			dao.save(newCustomer);
			setCustomers(null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							"Error Saving Customer"));
			return;
		}
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
						"Customer Saved successfully"));

	}

	public void updateCustomer(ActionEvent actionEvent) {
		try {
			CustomerDao dao = new CustomerDao();
			SessionInfo sessionInfo = UserSession.getSession();			
			selectedCustomer.setUpdated(new Date());					
			selectedCustomer.setEmployeeByUpdatedby(new Employee(sessionInfo.getEmployeeId()));
			dao.update(selectedCustomer, selectedCustomer.getId());
			
			setSelectedCustomer(new Customer());
			setCustomers(null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							"Error Updating Customer"));
			return;
		}
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
						"Customer Updated successfully"));
	}

}
