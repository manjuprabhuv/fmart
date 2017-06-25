package com.fmart.ui.beans;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.fmart.framework.hibernate.FMartGenericDAO;
import com.fmart.hibernate.dao.ExpenseDao;
import com.fmart.hibernate.dao.SaleDao;
import com.fmart.hibernate.pojos.Company;
import com.fmart.hibernate.pojos.Dealer;
import com.fmart.hibernate.pojos.Employee;
import com.fmart.hibernate.pojos.Expense;
import com.fmart.hibernate.pojos.ExpenseType;
import com.fmart.hibernate.pojos.LazySorter;
import com.fmart.hibernate.pojos.Sale;
import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

/**
 * 
 * @author Sachin
 */
@ManagedBean
@ViewScoped
public class ExpenseBean implements Serializable {

	/**
	 * 
	 */

	private Company selectedCompany = new Company();
	private Employee selectedEmployee = new Employee();
	private ExpenseType selectedExpenseType = new ExpenseType();
	private static final long serialVersionUID = 1L;
	Expense newExpense = new Expense();
	Expense selectedExpense = new Expense();
	private Expense[] selectedExpenses;
	private LazyDataModel<Expense> expenses = null;
	private List<ExpenseType> expenseTypes = new ArrayList<ExpenseType>();
	private boolean salaryFlag = true;
	private boolean salaryFlagForUpdate = true;

	private Integer oldAmount;

	public ExpenseBean() {
		selectedEmployee.setId(UserSession.getSession().getEmployeeId());
		selectedCompany.setId(UserSession.getSession().getCompanyId());
	}

	public Integer getOldAmount() {
		return oldAmount;
	}

	public void setOldAmount(Integer oldAmount) {
		this.oldAmount = oldAmount;
	}

	public boolean isSalaryFlag() {
		return salaryFlag;
	}

	public boolean isSalaryFlagForUpdate() {
		if (selectedExpense.getExpenseType() != null
				&& selectedExpense.getExpenseType().getName()
						.equalsIgnoreCase("Salary"))
			return false;
		else
			return true;
	}

	public void setSalaryFlagForUpdate(boolean salaryFlagForUpdate) {
		this.salaryFlagForUpdate = salaryFlagForUpdate;
	}

	public void setSalaryFlag(boolean salaryFlag) {
		this.salaryFlag = salaryFlag;
	}

	public Company getSelectedCompany() {
		return selectedCompany;
	}

	public void setSelectedCompany(Company selectedCompany) {
		this.selectedCompany = selectedCompany;
	}

	public Expense getNewExpense() {
		return newExpense;
	}

	public void setNewExpense(Expense newExpense) {
		this.newExpense = newExpense;
	}

	public Expense getSelectedExpense() {
		return selectedExpense;
	}

	public void setSelectedExpense(Expense selectedExpense) {
		setOldAmount(selectedExpense.getAmount());
		this.selectedExpense = selectedExpense;
	}

	public Expense[] getSelectedExpenses() {
		return selectedExpenses;
	}

	public void setSelectedExpenses(Expense[] selectedExpenses) {
		this.selectedExpenses = selectedExpenses;
	}
	
	public LazyDataModel<Expense> getExpenses() {
		if (expenses == null) {
			expenses = new LazyDataModel<Expense>() {

				private static final long serialVersionUID = 1L;

				@Override
				public List<Expense> load(int first, int pageSize,
						String sortField, SortOrder sortOrder,
						Map<String, Object> filters) {
					ExpenseDao dao = new ExpenseDao();
					List<Expense> expenseList = dao.findAll(first, pageSize);
					// sort
					if (sortField != null) {
						Collections.sort(expenseList, new LazySorter(
								sortField, sortOrder));
					}

					// rowCount
					this.setRowCount(dao.countByProperty(UserSession
							.getSession().getCompanyId()));
					return expenseList;
				}
			};

		}
		return this.expenses;
	}

	public void setExpenses(LazyDataModel<Expense> expenses) {
		this.expenses = expenses;
	}

	public ExpenseType getSelectedExpenseType() {
		return selectedExpenseType;
	}

	public void setSelectedExpenseType(ExpenseType selectedExpenseType) {
		this.selectedExpenseType = selectedExpenseType;
	}

	public List<ExpenseType> getExpenseTypes() {
		return expenseTypes;
	}

	public void setExpenseTypes(List<ExpenseType> expenseTypes) {
		this.expenseTypes = expenseTypes;
	}

	public Employee getSelectedEmployee() {
		return selectedEmployee;
	}

	public void setSelectedEmployee(Employee selectedEmployee) {
		this.selectedEmployee = selectedEmployee;
	}

	public List<ExpenseType> populateExpenseTypes(String query) {
		ExpenseDao dao = new ExpenseDao();
		return dao.findAllNameContains(query, null);
	}
	public void saveExpense(ActionEvent actionEvent) {
		String result = null;
		ExpenseDao dao = new ExpenseDao();
		try {
			newExpense.setCompany(selectedCompany);
			newExpense.setEmployeeByCreatedBy(selectedEmployee);
			newExpense.setUpdated(new Date());
			newExpense.setCreated(new Date());
			newExpense.setEmployeeByUpdatedBy(selectedEmployee);
			result = dao.saveExpense(newExpense, selectedCompany);
			setNewExpense(new Expense());
			setExpenses(null);
		} catch (Exception e) {
			String Result = "Expense Entry : Amount should be less Cash in Hand!";
			if (e.getMessage().contains("Cash in Hand")) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
								Result));
			} else {

				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
								"Error Saving Expense"));
			}
			return;
		}
		setNewExpense(new Expense());
		setExpenses(null);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", result));

	}

	public void updateExpense(ActionEvent actionEvent) {
		try {
			ExpenseDao dao = new ExpenseDao();
			selectedExpense.setCompany(selectedCompany);
			selectedExpense.setEmployeeByCreatedBy(selectedEmployee);
			/*if (selectedExpense.getEmployeeByEmployeeId() == null) {
				selectedExpense.setEmployeeByEmployeeId(selectedEmployee);
			}*/
			selectedExpense.setUpdated(new Date());			
			selectedExpense.setEmployeeByUpdatedBy(selectedEmployee);
			dao.updateExpense(selectedExpense, selectedCompany, oldAmount);
			setSelectedExpense(new Expense());
			setOldAmount(0);
			setExpenses(null);
		} catch (Exception e) {
			String result = "Expense Update : Amount should be less Cash in Hand!";
			if (e.getMessage().contains("Cash in Hand")) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
								result));
			} else {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
								"Error Updating Expense"));
			}
			setOldAmount(0);
			return;
		}
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
						"Expense Updated successfully"));
	}


	public void expsenseTypeChangeListener() {
		if (newExpense.getExpenseType() != null
				&& newExpense.getExpenseType().getName()
						.equalsIgnoreCase("Salary")) {
			setSalaryFlag(false);
		} else {
			newExpense.setEmployeeByEmployeeId(null);
			setSalaryFlag(true);
		}
	}

	public void expsenseTypeChangeListenerForUpdate() {
		if (selectedExpense.getExpenseType() != null
				&& selectedExpense.getExpenseType().getName()
						.equalsIgnoreCase("Salary")) {
			setSalaryFlagForUpdate(false);
		} else {
			selectedExpense.setEmployeeByEmployeeId(null);
			setSalaryFlagForUpdate(true);
		}
	}

}
