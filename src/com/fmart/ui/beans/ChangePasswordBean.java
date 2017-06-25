package com.fmart.ui.beans;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.fmart.framework.hibernate.FMartGenericDAO;
import com.fmart.hibernate.pojos.EmpReport;
import com.fmart.hibernate.pojos.EmpRole;
import com.fmart.hibernate.pojos.Employee;
import com.fmart.hibernate.pojos.Report;
import com.fmart.hibernate.pojos.Roles;
import com.fmart.ui.utils.SecurityUtils;
import com.fmart.ui.utils.SessionInfo;

/**
 * 
 * @author Sachin A
 * 
 */

@ManagedBean
@ViewScoped
public class ChangePasswordBean implements Serializable{

	private String oldPassword;

	private String password;
	private String confirmPassword;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public void changePassword(ActionEvent actionEvent) {
		if (!password.equals(confirmPassword)) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
							"Password Mismatch!"));
			return;
		} else {
			try {
				FMartGenericDAO dao = new FMartGenericDAO();
				
				Employee emp = dao.findById(UserSession.getSession().getEmployeeId(),
						Employee.class);
				if (emp != null
						&& emp.getPassword().equalsIgnoreCase(SecurityUtils.encode(oldPassword))) {
					emp.setPassword(password);
					
					dao.update(emp,emp.getId());
				}
				else{
					FacesContext.getCurrentInstance().addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
									"Old Password is wrong"));
					return;
				}
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
								"Error Updating Employee Password"));
				return;
			}
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
							"Password Change Successfull!"));
		}

	}

}
