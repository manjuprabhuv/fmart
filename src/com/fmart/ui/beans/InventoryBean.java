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
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.fmart.hibernate.dao.CompanyDao;
import com.fmart.hibernate.dao.InventoryDao;
import com.fmart.hibernate.pojos.Company;
import com.fmart.hibernate.pojos.Inventory;

/**
 * 
 * @author Sachin
 */
@ManagedBean
@ViewScoped
public class InventoryBean implements Serializable {

	public InventoryBean() {
	}

	private static final long serialVersionUID = 1L;

	private List<Inventory> inventoriesByCompany = new ArrayList<Inventory>();

	private Inventory printInventory = new Inventory();
	private Date printDate = new Date();
	private Company selectedCompanyForPrint = new Company();

	public Company getSelectedCompanyForPrint() {
		return selectedCompanyForPrint;
	}

	public void setSelectedCompanyForPrint(Company selectedCompanyForPrint) {
		this.selectedCompanyForPrint = selectedCompanyForPrint;
	}

	public List<Inventory> getInventoriesByCompany() {
		InventoryDao dao = new InventoryDao();
		CompanyDao CompDao = new CompanyDao();
		if (this.inventoriesByCompany.isEmpty()) {
			selectedCompanyForPrint = CompDao.findById(UserSession.getSession()
					.getInventoryPrintCompanyId());
			if (selectedCompanyForPrint != null) {
				this.inventoriesByCompany = dao.findByProperty("company.id",
						(UserSession.getSession().getInventoryPrintCompanyId()));
				if (this.inventoriesByCompany.isEmpty()) {
					FacesContext.getCurrentInstance().addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!",
									"Inventory is not available for Company: "
											+ selectedCompanyForPrint.getName()));
					setInventoriesByCompany(new ArrayList<Inventory>());
					return new ArrayList<Inventory>();
				}
			}else{
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
								"Please select the company!"));
				setInventoriesByCompany(new ArrayList<Inventory>());
				return new ArrayList<Inventory>();
			}
			
		}
		return inventoriesByCompany;
	}

	public void setInventoriesByCompany(List<Inventory> inventoriesByCompany) {
		this.inventoriesByCompany = inventoriesByCompany;
	}

	public Inventory getPrintInventory() {
		return printInventory;
	}

	public void setPrintInventory(Inventory printInventory) {
		this.printInventory = printInventory;
	}

	public Date getPrintDate() {
		return printDate;
	}

	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}

}
