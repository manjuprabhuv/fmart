package com.fmart.ui.beans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fmart.framework.hibernate.FMartGenericDAO;
import com.fmart.hibernate.dao.EmployeeDao;
import com.fmart.hibernate.pojos.EmpReport;
import com.fmart.hibernate.pojos.EmpRole;
import com.fmart.hibernate.pojos.Employee;
import com.fmart.hibernate.pojos.Report;
import com.fmart.hibernate.pojos.Roles;
import com.fmart.ui.utils.SecurityUtils;
import com.fmart.ui.utils.SessionInfo;

@ManagedBean
@SessionScoped
public class LoginBean implements Serializable {
	private String username;
	private String password;

	/**
	 * Creates a new instance of LoginController
	 */
	public LoginBean() {
	}

	// Getters and Setters
	/**
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Listen for button clicks on the #{loginController.login} action,
	 * validates the username and password entered by the user and navigates to
	 * the appropriate page.
	 * 
	 * @param actionEvent
	 */
	public void login(ActionEvent actionEvent) {

		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context
				.getExternalContext().getRequest();
		HttpServletResponse response = (HttpServletResponse) context
				.getExternalContext().getResponse();
		try {
			String navigateString = "";			
			EmployeeDao dao = new EmployeeDao();
			boolean userExists = false;
			List<Employee> emp = dao.findByProperty("username", username);
			SessionInfo sessioinfo = new SessionInfo();
			for (Employee employee : emp) {
				if (employee.getPassword().equalsIgnoreCase(
						SecurityUtils.encode(password))
						&& employee.getActive()) {
					navigateString = "/home.xhtml";
					userExists = true;
					sessioinfo.setUsername(username);
					Set<Roles> rolesSet = new HashSet<Roles>();
					for (EmpRole roles : employee.getEmpRoles()) {
						rolesSet.add(roles.getRoles());
					}
					sessioinfo.setRoles(rolesSet);

					Set<Report> reportSet = new HashSet<Report>();
					for (EmpReport roles : employee.getEmpReports()) {
						reportSet.add(roles.getReport());
					}
					sessioinfo.setReportRoles(reportSet);
					sessioinfo.setCompanyId(employee.getCompany().getId());
					sessioinfo.setEmployeeId(employee.getId());
					break;
				}

			}

			if (userExists) {
				HttpSession session = request.getSession(true);
				session.setAttribute("user.session", sessioinfo);
				Cookie cookie = null;
				cookie = new Cookie("fmart.logincheck", "true");
				response.addCookie(cookie);
				for(Roles roles :sessioinfo.getRoles()){
					if("Super Admin".equalsIgnoreCase(roles.getName())|| "Admin".equalsIgnoreCase(roles.getName())){
						cookie = new Cookie("admin.login", "true");
						cookie.setMaxAge(30*24 * 60 * 60);
						response.addCookie(cookie);
						break;
					}
				}				
				context.getExternalContext().redirect(
						request.getContextPath() + navigateString);
			} else {

				context.addMessage(
						null,
						new FacesMessage("Error!",
								"The username or password you provided does not match our records."));
			}
		} catch (Exception e) {

			context.addMessage(
					null,
					new FacesMessage("Error!",
							"The username or password you provided does not match our records."));
		}
	}

	/**
	 * Listen for logout button clicks on the #{loginController.logout} action
	 * and navigates to login screen.
	 */
	public void logout() {

		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(false);
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();

		if (session != null) {
			session.invalidate();
		}
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			context.getExternalContext().redirect(
					request.getContextPath() + "/Login.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(),
		// null, "/Login.xhtml?faces-redirect=true");
	}
}
