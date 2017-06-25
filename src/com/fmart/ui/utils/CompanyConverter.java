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
@FacesConverter("com.fmart.ui.utils.CompanyConverter")
public class CompanyConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		FMartGenericDAO dao = new FMartGenericDAO();
		List<Company> list = dao.findAll(Company.class);
		for(Company company : list){
			if(company.getName().equalsIgnoreCase(arg2)){
			return company.getId();
			}
		}
		
		return "";
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {		
		FMartGenericDAO dao = new FMartGenericDAO();
		List<Company> list = dao.findAll(Company.class);
		for(Company company : list){
			if(arg2 instanceof String){
				if(company.getName().equalsIgnoreCase((String)arg2)){
					return company.getName();
				}	
			}else if( arg2 instanceof Integer){
			if(company.getId()==(Integer)arg2){
				return company.getName();
			}
			}
		}
		
		return "";
	}

}
