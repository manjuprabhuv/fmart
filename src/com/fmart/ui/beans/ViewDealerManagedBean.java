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

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;






import com.fmart.hibernate.dao.DealerDao;
import com.fmart.hibernate.pojos.Dealer;
import com.fmart.hibernate.pojos.Employee;
import com.fmart.ui.utils.SessionInfo;

/**
 * 
 * @author jay
 */
@ManagedBean
@ViewScoped
public class ViewDealerManagedBean implements Serializable {

	Dealer newDealer = new Dealer();
	Dealer selectedDealer = new Dealer();
	private Dealer[] selectedDealers;
	private List<Dealer> dealers = null;
	

	public Dealer[] getSelectedDealers() {
		return selectedDealers;
	}

	public void setSelectedDealers(Dealer[] selectedDealers) {
		this.selectedDealers = selectedDealers;
	}

	public Dealer getNewDealer() {
		return newDealer;
	}

	public void setNewDealer(Dealer newDealer) {
		this.newDealer = newDealer;
	}

	public Dealer getSelectedDealer() {
		return selectedDealer;
	}

	public void setSelectedDealer(Dealer selectedDealer) {
		this.selectedDealer = selectedDealer;
	}

	public List<Dealer> getDealers() {
		if(dealers == null){
			DealerDao dao = new DealerDao();
			this.dealers = dao.findAll();
		}
		return dealers;
	}

	public void setDealers(List<Dealer> dealers) {
		this.dealers = dealers;
	}


	public void saveDealer(ActionEvent actionEvent) {

		DealerDao dao = new DealerDao();
		// newDealer.setId(null);
		try {
			SessionInfo sessionInfo = UserSession.getSession();
			newDealer.setCreated(new Date());
			newDealer.setUpdated(new Date());			
			newDealer.setEmployeeByCreatedBy(new Employee(sessionInfo.getEmployeeId()));
			newDealer.setEmployeeByUpdatedBy(new Employee(sessionInfo.getEmployeeId()));
			dao.save(newDealer);
		//	populateDealerList();
			setNewDealer(new Dealer());
			setDealers(null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							"Error Saving Dealer"));
			return;
		}
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
						"Dealer Saved successfully"));

	}

	public void updateDealer(ActionEvent actionEvent) {
		try {
			DealerDao dao = new DealerDao();
			SessionInfo sessionInfo = UserSession.getSession();			
			selectedDealer.setUpdated(new Date());		
			selectedDealer.setEmployeeByUpdatedBy(new Employee(sessionInfo.getEmployeeId()));
			dao.update(getSelectedDealer(),getSelectedDealer().getId());
			//populateDealerList();
			setNewDealer(new Dealer());
			setDealers(null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							"Error Updating Dealer"));
			return;
		}
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
						"Dealer Updated successfully"));
	}


}
