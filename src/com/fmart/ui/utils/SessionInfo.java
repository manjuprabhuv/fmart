package com.fmart.ui.utils;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import com.fmart.hibernate.pojos.Company;
import com.fmart.hibernate.pojos.Report;
import com.fmart.hibernate.pojos.Roles;

public class SessionInfo implements Serializable {
	String username;
	Set<Roles> roles;
	Set<Report> reportRoles;
	Integer companyId;
	Integer employeeId;
	
	int purchaseId;
	int bookingId;
	int inventoryPrintCompanyId;
	
	public int getInventoryPrintCompanyId() {
		return inventoryPrintCompanyId;
	}

	public void setInventoryPrintCompanyId(int inventoryPrintCompanyId) {
		this.inventoryPrintCompanyId = inventoryPrintCompanyId;
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

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set<Roles> getRoles() {
		return roles;
	}

	public void setRoles(Set<Roles> roles) {
		this.roles = roles;
	}

	public Set<Report> getReportRoles() {
		return reportRoles;
	}

	public void setReportRoles(Set<Report> reportRoles) {
		this.reportRoles = reportRoles;
	}

	public boolean isRole(String role) {
		for (Roles r : getRoles())
			if (r.getName().toUpperCase().contains(role.toUpperCase()))
				return true;
		return false;
	}

	public boolean isAuthorized(String role) {
		for (Roles r : getRoles()) {
			if ((r.getName().equalsIgnoreCase("Admin") || r.getName()
					.equalsIgnoreCase("Super Admin"))
					&& (role.equalsIgnoreCase("Company") || role
							.equalsIgnoreCase("Profit And Loss") || role.equalsIgnoreCase("Amount"))) {
				return false;
			}
			if ((r.getName().equalsIgnoreCase("Admin")
					|| r.getName().equalsIgnoreCase("Super Admin") || r
					.getName().equalsIgnoreCase("Manager"))
					&& role.equalsIgnoreCase("Employee")) {
				return false;
			}
			if ((r.getName().equalsIgnoreCase("Admin")
					|| r.getName().equalsIgnoreCase("Super Admin")
					|| r.getName().equalsIgnoreCase("Manager") || r.getName()
					.equalsIgnoreCase("Purchase"))
					&& role.equalsIgnoreCase("Dealer")) {
				return false;
			}
			if ((r.getName().equalsIgnoreCase("Admin") || r.getName()
					.equalsIgnoreCase("Super Admin"))
					|| r.getName().equalsIgnoreCase("Manager")
					&& (role.equalsIgnoreCase("Purchase")
							|| role.equalsIgnoreCase("Sales") || role
								.equalsIgnoreCase("Expenses"))) {
				return false;
			}
			if ((r.getName().equalsIgnoreCase("Admin")
					|| r.getName().equalsIgnoreCase("Super Admin")
					|| r.getName().equalsIgnoreCase("Manager") || r.getName()
					.equalsIgnoreCase("Purchase") || r.getName().equalsIgnoreCase("Sales&Purchase"))
					&& (role.equalsIgnoreCase("Purchase") || role.equalsIgnoreCase("Payments"))) {
				return false;
			}
			if ((r.getName().equalsIgnoreCase("Admin")
					|| r.getName().equalsIgnoreCase("Super Admin")
					|| r.getName().equalsIgnoreCase("Manager") || r.getName()
					.equalsIgnoreCase("Sales") || r.getName().equalsIgnoreCase("Sales&Purchase"))
					&& (role.equalsIgnoreCase("Sales") || role.equalsIgnoreCase("Receipts") )) {
				return false;
			}
			if ((r.getName().equalsIgnoreCase("Admin")
					|| r.getName().equalsIgnoreCase("Super Admin"))
					&& (role.equalsIgnoreCase("Notifications") || role.equalsIgnoreCase("ResetPassword"))) {
				return false;
			}

		}
		return true;
	}
	

	public boolean disableReport(String role){
		for(Report report : getReportRoles()){
				if(report.getName().equalsIgnoreCase(role)){
					return false;
				}
		}
		for (Roles r : getRoles()) {
			if (r.getName()
					.equalsIgnoreCase("Super Admin")) {
				return false;
			}		
		}
		return true;
	}
	
	
}
