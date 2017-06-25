package com.fmart.ui.beans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.fmart.ui.utils.SessionInfo;


@ManagedBean
@SessionScoped
public class UserSession implements Serializable {
	public static SessionInfo getSession(){
		return (SessionInfo) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user.session");
	}
	
	public String poll(){
		return "alive";
	}

}
