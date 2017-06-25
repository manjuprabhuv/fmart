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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.fmart.hibernate.dao.ProductDao;
import com.fmart.hibernate.dao.ReceiptDao;
import com.fmart.hibernate.pojos.Company;
import com.fmart.hibernate.pojos.Customer;
import com.fmart.hibernate.pojos.LazySorter;
import com.fmart.hibernate.pojos.Product;
import com.fmart.hibernate.pojos.Sale;
import com.fmart.hibernate.pojos.Employee;
import com.fmart.hibernate.pojos.Expense;
import com.fmart.hibernate.pojos.ExpenseType;
import com.fmart.hibernate.pojos.Receipt;
import com.fmart.hibernate.pojos.Sale;

/**
 * 
 * @author jay
 */
@ManagedBean
@ViewScoped
public class ReceiptBean implements Serializable {

	public ReceiptBean() {
		selectedCompany.setId(UserSession.getSession().getCompanyId());
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Receipt newReceipt = new Receipt();
	private Receipt selectedReceipt = new Receipt();
	private Company selectedCompany = new Company();
	private Employee selectedEmployee = new Employee();

	private Receipt[] selectedReceipts;
	private LazyDataModel<Receipt> receipts = null;
	private BigDecimal oldAmount = BigDecimal.ZERO;
	private BigDecimal pendingAmount;
	private Sale selectedSale;
	private Sale oldSelectedSale;

	public Receipt getNewReceipt() {
		return newReceipt;
	}

	public void setNewReceipt(Receipt newReceipt) {
		setPendingAmount(BigDecimal.ZERO);
		this.newReceipt = newReceipt;
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

	public Sale getSelectedSale() {
		return selectedSale;
	}

	public void setSelectedSale(Sale selectedSale) {
		this.selectedSale = selectedSale;
	}

	public Sale getOldSelectedSale() {
		return oldSelectedSale;
	}

	public void setOldSelectedSale(Sale oldSelectedSale) {
		this.oldSelectedSale = oldSelectedSale;
	}

	public Receipt getSelectedReceipt() {
		return selectedReceipt;
	}

	public void saleIdChangeListener() {
		ReceiptDao dao = new ReceiptDao();
		setPendingAmount(dao.getPendingAmountForSale(selectedSale.getId()));
	}

	public void setSelectedReceipt(Receipt selectedReceipt) {
		ReceiptDao dao = new ReceiptDao();
		setOldAmount(selectedReceipt.getAmount());
		setSelectedSale(selectedReceipt.getSale());
		setOldSelectedSale(selectedReceipt.getSale());
		setSelectedCompany(selectedReceipt.getCompany());
		setPendingAmount(dao.getPendingAmountForSale(selectedSale.getId()));
		this.selectedReceipt = selectedReceipt;
	}

	public Receipt[] getSelectedReceipts() {
		return selectedReceipts;
	}

	public void setSelectedReceipts(Receipt[] selectedReceipts) {
		this.selectedReceipts = selectedReceipts;
	}

	public LazyDataModel<Receipt> getReceipts() {
		if (receipts == null) {
			receipts = new LazyDataModel<Receipt>() {

				private static final long serialVersionUID = 1L;

				@Override
				public List<Receipt> load(int first, int pageSize,
						String sortField, SortOrder sortOrder,
						Map<String, Object> filters) {
					ReceiptDao dao = new ReceiptDao();
					
					List<Receipt> receiptList = dao.findByProperty("company.id", UserSession
							.getSession().getCompanyId(),first, pageSize);
					// sort
					if (sortField != null) {
						Collections.sort(receiptList, new LazySorter(
								sortField, sortOrder));
					}

					// rowCount
					this.setRowCount(dao.countByProperty( UserSession
							.getSession().getCompanyId()));
					return receiptList;
				}
			};

		}
		return this.receipts;
	}


	public void setReceipts(LazyDataModel<Receipt> receipts) {
		this.receipts = receipts;
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

	public List<Sale> populateSaleIds(String query) {
		ReceiptDao dao = new ReceiptDao();
		return dao.findAllNameContains(Sale.class,query, "company_id="
				+ UserSession.getSession().getCompanyId());
	}

	public void saveReceipt(ActionEvent actionEvent) {
		String result = null;
		ReceiptDao dao = new ReceiptDao();
		try {
			newReceipt.setCompany(selectedCompany);
			newReceipt.setSale(selectedSale);
			selectedEmployee.setId(UserSession.getSession().getEmployeeId());
			newReceipt.setEmployeeByCreatedBy(selectedEmployee);
			newReceipt.setEmployeeByUpdatedBy(selectedEmployee);
			newReceipt.setCreated(new Date());
			newReceipt.setUpdated(new Date());
			result = dao.saveReceipt(newReceipt);
			setNewReceipt(new Receipt());
			setReceipts(null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							"Error Saving Receipt"));
			return;
		}
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", result));

	}

	public void updateReceipt(ActionEvent actionEvent) {
		try {
			ReceiptDao dao = new ReceiptDao();
			selectedReceipt.setCompany(selectedCompany);
			if (selectedSale == null)
				selectedReceipt.setSale(oldSelectedSale);
			else
				selectedReceipt.setSale(selectedSale);
			selectedEmployee.setId(UserSession.getSession().getEmployeeId());
			selectedReceipt.setEmployeeByUpdatedBy(selectedEmployee);			
			selectedReceipt.setUpdated(new Date());
			dao.updateReceipt(selectedReceipt, oldAmount);
			setReceipts(null);
		} catch (Exception e) {
			String res = null;
			if (e.getMessage().contains("Cash in Hand"))
				res = e.getMessage();
			if (res != null) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
								res));
			} else {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
								"Error Updating Receipt"));
			}
			return;
		}
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
						"Expense Updated successfully"));
	}

}
