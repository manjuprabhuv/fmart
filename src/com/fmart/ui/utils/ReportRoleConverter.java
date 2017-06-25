package com.fmart.ui.utils;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.fmart.framework.hibernate.FMartGenericDAO;
import com.fmart.hibernate.pojos.Company;
import com.fmart.hibernate.pojos.EmpReport;
import com.fmart.hibernate.pojos.Report;
import com.fmart.hibernate.pojos.Roles;
import com.fmart.ui.beans.ConstantsBean;
@FacesConverter("com.fmart.ui.utils.ReportRoleConverter")
public class ReportRoleConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		
		FMartGenericDAO dao = new FMartGenericDAO();
		List<Report> list = dao.findAll(Report.class);
		for (Report report : list) {
			if(report.getName().equalsIgnoreCase(arg2)){
				return report;
			}
		}
		//System.out.println(arg2);
		
		return "";
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		FMartGenericDAO dao = new FMartGenericDAO();
		List<Report> list = dao.findAll(Report.class);
		for (Report empReport : list) {
			if(empReport.getId()==(Integer)arg2){
				return empReport.getName();
			}
		}
		//System.out.println(arg2);
		
		return "";
	}

}
