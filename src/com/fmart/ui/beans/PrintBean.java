package com.fmart.ui.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.fmart.hibernate.dao.SaleDao;
import com.fmart.hibernate.pojos.Company;
import com.fmart.ui.utils.SessionInfo;

/**
 * 
 * @author Sachin
 */
@ViewScoped
@ManagedBean
public class PrintBean implements Serializable {

	/**
	 * 
	 */
	public PrintBean() {
	}

	private static final long serialVersionUID = 1L;

	private int purchaseId;
	private int bookingId;
	private int companyId;
	
	private List<Date> dates = new ArrayList<Date>();
	private List<Integer> saleIds = new ArrayList<Integer>();

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
		companyIdInSession();
	}
	
	public int getBookingId() {
		return bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public int getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(int purchaseId) {
		this.purchaseId = purchaseId;
	}

	public List<Integer> getSaleIds() {
		return saleIds;
	}

	public void setSaleIds(List<Integer> saleIds) {
		this.saleIds = saleIds;
	}
	
	public void companyChangeListener(ValueChangeEvent evt) {
		if (evt.getNewValue() != null)
			setCompanyId((Integer) evt.getNewValue());
		else
			setCompanyId(0);
	}

	public List<Date> populateDates() {
		return dates;
	}
	
	public List<Integer> populateSaleIds(String query) {
		SaleDao dao = new SaleDao();
		try {
			this.saleIds = dao.findAllSaleIDs(UserSession.getSession()
					.getCompanyId(),query);
		} catch (Exception e) {
		}
		return this.saleIds;
	}

	public void purchaseIdInSession(ActionEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context
				.getExternalContext().getRequest();
		SessionInfo sessionInfo = UserSession.getSession();
		sessionInfo.setPurchaseId(purchaseId);
		HttpSession session = request.getSession(true);
		session.setAttribute("user.session", sessionInfo);
	}

	public void bookingIdInSession(ActionEvent event) {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context
				.getExternalContext().getRequest();
		SessionInfo sessionInfo = UserSession.getSession();
		sessionInfo.setBookingId(bookingId);
		HttpSession session = request.getSession(true);
		session.setAttribute("user.session", sessionInfo);

	}
	
	public void companyIdInSession() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context
				.getExternalContext().getRequest();
		SessionInfo sessionInfo = UserSession.getSession();
		sessionInfo.setInventoryPrintCompanyId(companyId);
		HttpSession session = request.getSession(true);
		session.setAttribute("user.session", sessionInfo);

	}

}
