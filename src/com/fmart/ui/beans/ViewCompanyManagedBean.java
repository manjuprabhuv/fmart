package com.fmart.ui.beans;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.fmart.hibernate.dao.CompanyDao;
import com.fmart.hibernate.pojos.Company;

/**
 * 
 * @author mvprabhu
 */
@ManagedBean
@ViewScoped
public class ViewCompanyManagedBean implements Serializable {

	Company newCompany = new Company();
	Company selectedCompany = new Company();
	private Company[] selectedCompanies;
	private List<Company> companies = null;
	

	public Company[] getSelectedCompanies() {
		return selectedCompanies;
	}

	public void setSelectedCompanies(Company[] selectedCompanies) {
		this.selectedCompanies = selectedCompanies;
	}

	public Company getNewCompany() {
		return newCompany;
	}

	public void setNewCompany(Company newCompany) {
		this.newCompany = newCompany;
	}

	public Company getSelectedCompany() {
		return selectedCompany;
	}

	public void setSelectedCompany(Company selectedCompany) {
		this.selectedCompany = selectedCompany;
	}

	public List<Company> getCompanies() {
		if(companies == null){
			CompanyDao dao = new CompanyDao();
			this.companies = dao.findAll();	
		}
		return companies;
	}

	public void setCompanies(List<Company> companies) {
		this.companies = companies;
	}

	public void saveCompany(ActionEvent actionEvent) {

		CompanyDao dao = new CompanyDao();
		try {
			dao.save(newCompany);
			setCompanies(null);
			setNewCompany(new Company());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							"Error Saving Company"));
			return;
		}
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
						"Company Saved successfully"));

	}

	public void updateCompany(ActionEvent actionEvent) {
		try {
			CompanyDao dao = new CompanyDao();			
			dao.update(getSelectedCompany(),getSelectedCompany().getId());
			setNewCompany(new Company());
			setCompanies(null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							"Error Updating Company"));
			return;
		}
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
						"Company Updated successfully"));
	}

}
