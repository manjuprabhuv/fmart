package com.fmart.ui.beans;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.fmart.hibernate.dao.ExpenseTypeDao;
import com.fmart.hibernate.pojos.Employee;
import com.fmart.hibernate.pojos.ExpenseType;
import com.fmart.ui.utils.SessionInfo;

/**
 * 
 * @author Sachin
 */
@ManagedBean
@ViewScoped
public class ExpenseTypeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ExpenseType newExpenseType = new ExpenseType();
	ExpenseType selectedExpenseType = new ExpenseType();
	private ExpenseType[] selectedExpenseTypes;
	private List<ExpenseType> expenseTypes = null;
	
	public ExpenseType getNewExpenseType() {
		return newExpenseType;
	}

	public void setNewExpenseType(ExpenseType newExpenseType) {
		this.newExpenseType = newExpenseType;
	}

	public ExpenseType getSelectedExpenseType() {
		return selectedExpenseType;
	}

	public void setSelectedExpenseType(ExpenseType selectedExpenseType) {
		this.selectedExpenseType = selectedExpenseType;
	}
	
	public ExpenseType[] getSelectedExpenseTypes() {
		return selectedExpenseTypes;
	}

	public void setSelectedExpenseTypes(ExpenseType[] selectedExpenseTypes) {
		this.selectedExpenseTypes = selectedExpenseTypes;
	}

	public List<ExpenseType> getExpenseTypes() {
		if(expenseTypes == null){
			ExpenseTypeDao dao = new ExpenseTypeDao();
			this.expenseTypes = dao.findAll();
		}
		return expenseTypes;
	}

	public void setExpenseTypes(List<ExpenseType> expenseTypes) {
		this.expenseTypes = expenseTypes;
	}

	//@PostConstruct
	public List<ExpenseType> populateExpenseTypeList() {
		ExpenseTypeDao dao = new ExpenseTypeDao();
		this.expenseTypes = dao.findAll();
		return this.expenseTypes;
	}

	public void saveExpenseType(ActionEvent actionEvent) {
		ExpenseTypeDao dao = new ExpenseTypeDao();
		try {
			SessionInfo sessionInfo = UserSession.getSession();
			newExpenseType.setCreated(new Date());
			newExpenseType.setUpdated(new Date());			
			newExpenseType.setEmployeeByCreatedBy(new Employee(sessionInfo.getEmployeeId()));
			newExpenseType.setEmployeeByUpdatedBy(new Employee(sessionInfo.getEmployeeId()));
			dao.save(newExpenseType);
			setNewExpenseType(new ExpenseType());
			setExpenseTypes(null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							"Error Saving Expense Type"));
			return;
		}
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
						"Expense Type Saved successfully"));

	}

	public void updateExpenseType(ActionEvent actionEvent) {
		try {
			ExpenseTypeDao dao = new ExpenseTypeDao();
			SessionInfo sessionInfo = UserSession.getSession();			
			getSelectedExpenseType().setUpdated(new Date());			
			getSelectedExpenseType().setEmployeeByUpdatedBy(new Employee(sessionInfo.getEmployeeId()));
			dao.update(getSelectedExpenseType(),getSelectedExpenseType().getId());
			setNewExpenseType(new ExpenseType());
			setExpenseTypes(null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							"Error Updating Expense Type"));
			return;
		}
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
						"ExpenseType Updated successfully"));
	}



}
