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






import com.fmart.hibernate.dao.ProductGrpDao;
import com.fmart.hibernate.pojos.Employee;
import com.fmart.hibernate.pojos.ProductGrp;
import com.fmart.ui.utils.SessionInfo;

/**
 * 
 * @author Sachin
 */
@ManagedBean
@ViewScoped
public class ViewProductGroupManagedBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ProductGrp newProductGroup = new ProductGrp();
	ProductGrp selectedProductGroup = new ProductGrp();
	private ProductGrp[] selectedProductGroups;
	private List<ProductGrp> productGroups = null;
	
	public ProductGrp getSelectedProductGroup() {
		return selectedProductGroup;
	}

	public void setSelectedProductGroup(ProductGrp selectedProductGroup) {
		this.selectedProductGroup = selectedProductGroup;
	}

	public List<ProductGrp> getProductGroups() {
		if(productGroups == null){
			ProductGrpDao dao = new ProductGrpDao();
			this.productGroups = dao.findAll();
		}
		return productGroups;
	}

	public void setProductGroups(List<ProductGrp> productGroups) {
		this.productGroups = productGroups;
	}

	public ProductGrp[] getSelectedProductGroups() {
		return selectedProductGroups;
	}

	public void setSelectedProductGroups(ProductGrp[] selectedProductGroups) {
		this.selectedProductGroups = selectedProductGroups;
	}

	public ProductGrp getNewProductGroup() {
		return newProductGroup;
	}

	public void setNewProductGroup(ProductGrp newProductGroup) {
		this.newProductGroup = newProductGroup;
	}
	
	//@PostConstruct
	public List<ProductGrp> populateProductGroupList() {
		ProductGrpDao dao = new ProductGrpDao();
		this.productGroups = dao.findAll();
		return this.productGroups;
	}

	public void saveProductGroup(ActionEvent actionEvent) {
		ProductGrpDao dao = new ProductGrpDao();
		try {
			SessionInfo sessionInfo = UserSession.getSession();
			newProductGroup.setCreated(new Date());
			newProductGroup.setUpdated(new Date());			
			newProductGroup.setEmployeeByCreatedBy(new Employee(sessionInfo.getEmployeeId()));
			newProductGroup.setEmployeeByUpdatedBy(new Employee(sessionInfo.getEmployeeId()));
			dao.save(newProductGroup);
			setNewProductGroup(new ProductGrp());
			setProductGroups(null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							"Error Saving Product Group"));
			return;
		}
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
						"Product Group Saved successfully"));

	}

	public void updateProductGroup(ActionEvent actionEvent) {
		try {
			ProductGrpDao dao = new ProductGrpDao();
			SessionInfo sessionInfo = UserSession.getSession();
			getSelectedProductGroup().setEmployeeByUpdatedBy(new Employee(sessionInfo.getEmployeeId()));
			getSelectedProductGroup().setUpdated(new Date());
			dao.update(getSelectedProductGroup(),getSelectedProductGroup().getId());
			setNewProductGroup(new ProductGrp());
			setProductGroups(null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							"Error Updating Product Group"));
			return;
		}
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
						"Product Group Updated successfully"));
	}


}
