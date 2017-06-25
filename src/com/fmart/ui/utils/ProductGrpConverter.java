package com.fmart.ui.utils;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.fmart.framework.hibernate.FMartGenericDAO;
import com.fmart.hibernate.pojos.Company;
import com.fmart.hibernate.pojos.ProductGrp;
import com.fmart.hibernate.pojos.Roles;
import com.fmart.ui.beans.ConstantsBean;
@FacesConverter("productGrpConverter")
public class ProductGrpConverter implements Converter {
	private FMartGenericDAO dao = new FMartGenericDAO();
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String submittedValue) {
		if (submittedValue.trim().equals("")) 
			return null;
		int number;
		number = Integer.parseInt(submittedValue);
		for (ProductGrp p : dao.findAll(ProductGrp.class)) {
			if (p.getId() == number) {
				return p.getId();
			}
		}
		
		return "";
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		FMartGenericDAO dao = new FMartGenericDAO();
		List<ProductGrp> list = dao.findAll(ProductGrp.class);
		for(ProductGrp pg : list){
			if(arg2 instanceof String){
				if(pg.getName().equalsIgnoreCase((String)arg2)){
					return pg.getName();
				}	
			}else if( arg2 instanceof Integer){
			if(pg.getId()==(Integer)arg2){
				return pg.getName();
			}
			}
		}
		
		return "";
		
		
	}

}
