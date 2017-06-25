package com.fmart.ui.beans;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.fmart.framework.hibernate.FMartGenericDAO;
import com.fmart.hibernate.dao.PaymentDao;
import com.fmart.hibernate.dao.ReceiptDao;
import com.fmart.hibernate.pojos.Company;
import com.fmart.hibernate.pojos.Dealer;
import com.fmart.hibernate.pojos.Employee;
import com.fmart.hibernate.pojos.Expense;
import com.fmart.hibernate.pojos.ExpenseType;
import com.fmart.hibernate.pojos.LazySorter;
import com.fmart.hibernate.pojos.Payment;
import com.fmart.hibernate.pojos.Receipt;
import com.fmart.hibernate.pojos.Sale;

/**
 * 
 * @author jay
 */
@ManagedBean
@ViewScoped
public class PaymentBean implements Serializable {

	public PaymentBean(){
		selectedCompany.setId(UserSession.getSession().getCompanyId());
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Payment newPayment = new Payment();
	private Payment selectedPayment = new Payment();
	private Company selectedCompany = new Company();
	private Employee selectedEmployee = new Employee();
	private Dealer selectedDealer = new Dealer();
	private Dealer oldSelectedDealer = new Dealer();

	private Payment[] selectedPayments;
	private LazyDataModel<Payment> payments = null;
	private BigDecimal oldAmount=BigDecimal.ZERO;
	private BigDecimal pendingAmount;
	
	public Payment getNewPayment() {
		return newPayment;
	}

	public void setNewPayment(Payment newPayment) {
		setPendingAmount(BigDecimal.ZERO);
		this.newPayment = newPayment;
	}

	public Company getSelectedCompany() {
		return selectedCompany;
	}

	public void setSelectedCompany(Company selectedCompany) {
		this.selectedCompany = selectedCompany;
	}

	public Employee getSelectedEmployee() {
		return selectedEmployee;
	}

	public void setSelectedEmployee(Employee selectedEmployee) {
		this.selectedEmployee = selectedEmployee;
	}

	public Dealer getSelectedDealer() {
		return selectedDealer;
	}

	public void setSelectedDealer(Dealer selectedDealer) {
		this.selectedDealer = selectedDealer;
	}

	public Dealer getOldSelectedDealer() {
		return oldSelectedDealer;
	}

	public void setOldSelectedDealer(Dealer oldSelectedDealer) {
		this.oldSelectedDealer = oldSelectedDealer;
	}

	public Payment getSelectedPayment() {
		return selectedPayment;
	}
	
	public void dealerChangeListener(){
		PaymentDao dao = new PaymentDao();
		setPendingAmount(dao.getPendingAmountForDealer(selectedDealer.getId(),selectedCompany.getId()));
	}

	public void setSelectedPayment(Payment selectedPayment) {
		PaymentDao dao = new PaymentDao();
		setOldAmount(selectedPayment.getAmount());
		setSelectedDealer(selectedPayment.getDealer());
		setOldSelectedDealer(selectedPayment.getDealer());
		setSelectedCompany(selectedPayment.getCompany());
		setPendingAmount(dao.getPendingAmountForDealer(selectedDealer.getId(),selectedCompany.getId()));
		this.selectedPayment = selectedPayment;
	}

	public Payment[] getSelectedPayments() {
		return selectedPayments;
	}

	public void setSelectedPayments(Payment[] selectedPayments) {
		this.selectedPayments = selectedPayments;
	}

	public LazyDataModel<Payment> getpayments() {
		if (payments == null) {
			payments = new LazyDataModel<Payment>() {

				private static final long serialVersionUID = 1L;

				@Override
				public List<Payment> load(int first, int pageSize,
						String sortField, SortOrder sortOrder,
						Map<String, Object> filters) {
					PaymentDao dao = new PaymentDao();
					
					List<Payment> paymentList = dao.findByProperty("company.id", UserSession
							.getSession().getCompanyId(),first, pageSize);
					// sort
					if (sortField != null) {
						Collections.sort(paymentList, new LazySorter(
								sortField, sortOrder));
					}

					// rowCount
					this.setRowCount(dao.countByProperty( UserSession
							.getSession().getCompanyId()));
					return paymentList;
				}
			};

		}
		return this.payments;
	}

	public void setPayments(LazyDataModel<Payment> payments) {
		this.payments = payments;
	}

	public BigDecimal getOldAmount() {
		return oldAmount;
	}

	public void setOldAmount(BigDecimal bigDecimal) {
		this.oldAmount = bigDecimal;
	}

	public BigDecimal getPendingAmount() {
		return pendingAmount;
	}

	public void setPendingAmount(BigDecimal pendingAmount) {
		this.pendingAmount = pendingAmount;
	}

	public List<Dealer> populateDealers(String query){
		PaymentDao dao = new PaymentDao();
		return dao.findAllNameContains(query,null);
	}

	public void savePayment(ActionEvent actionEvent) {
		String result = null;
		PaymentDao dao = new PaymentDao();
		try {
			newPayment.setCompany(selectedCompany);
			newPayment.setDealer(selectedDealer);
			selectedEmployee.setId(UserSession.getSession().getEmployeeId());
			newPayment.setEmployeeByCreatedBy(selectedEmployee);			
			newPayment.setCreated(new Date());
			newPayment.setUpdated(new Date());			
			newPayment.setEmployeeByUpdatedBy(selectedEmployee);
			result = dao.savePayment(newPayment);
			setNewPayment(new Payment());
			setPayments(null);
		} catch (Exception e) {			
			String res = null;
			if(e.getMessage().contains("Cash in Hand"))
				res=e.getMessage();
			if (res!=null) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
								res));
			} else {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
								"Error Saving Payment"));
			}
			return;
		}
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", result));

	}

	public void updatePayment(ActionEvent actionEvent) {
		try {
			PaymentDao dao = new PaymentDao();
			selectedPayment.setCompany(selectedCompany);
			if(selectedDealer==null)
				selectedPayment.setDealer(oldSelectedDealer);
			else
				selectedPayment.setDealer(selectedDealer);
			selectedEmployee.setId(UserSession.getSession().getEmployeeId());
			selectedPayment.setEmployeeByUpdatedBy(selectedEmployee);
			selectedPayment.setUpdated(new Date());
			dao.updatePayment(selectedPayment, oldAmount);
			setPayments(null);
		} catch (Exception e) {
			String res = null;
			if(e.getMessage().contains("Cash in Hand"))
				res=e.getMessage();
			if (res!=null) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
								res));
			} else {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
								"Error Updating Payment"));
			}
			return;
		}
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
						"Expense Updated successfully"));
	}


}
