package com.fmart.ui.utils;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.fmart.hibernate.dao.GodownDao;
import com.fmart.hibernate.pojos.Godown;
@FacesConverter("com.fmart.ui.utils.GodownConverter")
public class GodownConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		GodownDao dao = new GodownDao();
		List<Godown> list = dao.findAll();
		for(Godown godown : list){
			if(godown.getName().equalsIgnoreCase(arg2)){
			return godown.getId();
			}
		}
		
		return "";
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {		
		GodownDao dao = new GodownDao();
		List<Godown> list = dao.findAll();
		for(Godown Godown : list){
			if(arg2 instanceof String){
				if(Godown.getName().equalsIgnoreCase((String)arg2)){
					return Godown.getName();
				}	
			}else if( arg2 instanceof Integer){
			if(Godown.getId()==(Integer)arg2){
				return Godown.getName();
			}
			}
		}
		
		return "";
	}

}
