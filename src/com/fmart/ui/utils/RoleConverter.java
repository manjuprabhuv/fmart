package com.fmart.ui.utils;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.fmart.framework.hibernate.FMartGenericDAO;
import com.fmart.hibernate.pojos.Company;
import com.fmart.hibernate.pojos.Roles;
import com.fmart.ui.beans.ConstantsBean;
@FacesConverter("com.fmart.ui.utils.RoleConverter")
public class RoleConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		FMartGenericDAO dao = new FMartGenericDAO();
		List<Roles> roles=  dao.findAll(Roles.class);
		for(Roles role : roles){
			if(role.getName().equalsIgnoreCase(arg2)){
			return role.getId();
			}
		}
		
		return "";
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		FMartGenericDAO dao = new FMartGenericDAO();
		List<Roles> roles=  dao.findAll(Roles.class);
		for(Roles role : roles){
			if(arg2 instanceof String){
				if(role.getName().equalsIgnoreCase((String)arg2)){
					return role.getName();
				}	
			}else if( arg2 instanceof Integer){
			if(role.getId()==(Integer)arg2){
				return role.getName();
			}
			}
		}
		
		return "";
	}

}
