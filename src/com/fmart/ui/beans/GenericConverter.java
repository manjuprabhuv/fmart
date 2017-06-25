package com.fmart.ui.beans;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import com.fmart.framework.hibernate.FMartGenericDAO;
import com.fmart.hibernate.pojos.Customer;
import com.fmart.hibernate.pojos.Dealer;
import com.fmart.hibernate.pojos.Employee;
import com.fmart.hibernate.pojos.ExpenseType;
import com.fmart.hibernate.pojos.Product;
import com.fmart.hibernate.pojos.ProductGrp;
import com.fmart.hibernate.pojos.Sale;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;

@FacesConverter(value = "genericConverter")
public class GenericConverter implements Converter {
	private FMartGenericDAO dao = new FMartGenericDAO();

	public Object getAsObject(FacesContext facesContext, UIComponent component,
			String submittedValue) {
		String errorMsg = "";
		if (submittedValue.trim().equals("")) {
			return null;
		} else {
			try {

				int number;
				if (component.getClientId().contains("dealer")) {
					errorMsg = "INVALID DEALER";
					number = Integer.parseInt(submittedValue);
					for (Dealer p : dao.findAll(Dealer.class)) {
						if (p.getId() == number) {
							return p;
						}
					}
				} else if (component.getClientId().contains("productGrpP")) {
					errorMsg = "INVALID PRODUCT GROUP";
					number = Integer.parseInt(submittedValue);
					for (ProductGrp p : dao.findAll(ProductGrp.class)) {
						if (p.getId() == number) {
							return p;
						}
					}
				} else if (component.getClientId().contains("productP")) {
					errorMsg = "INVALID PRODUCT";
					number = Integer.parseInt(submittedValue);
					for (Product p : dao.findAll(Product.class)) {
						if (p.getId() == number) {
							return p;
						}
					}
				} else if (component.getClientId().contains("expenseTypeList")) {
					errorMsg = "INVALID EXPENSETYPE";
					number = Integer.parseInt(submittedValue);
					for (ExpenseType p : dao.findAll(ExpenseType.class)) {
						if (p.getId() == number) {
							return p;
						}
					}
				} else if (component.getClientId().contains("empList")  ) {
					errorMsg = "INVALID EMPLOYEE";
					number = Integer.parseInt(submittedValue);
					for (Employee p : dao.findAll(Employee.class)) {
						if (p.getId() == number) {
							return p;
						}
					}
				} 
				//for sale in receipts : customer name
				else if (component.getClientId().contains("customer")) {
					errorMsg = "INVALID CUSTOMER";
					number = Integer.parseInt(submittedValue);
					for (Sale p : dao.findAll(Sale.class)) {
						if (p.getId() == number) {
							return p;
						}
					}
				}
				//for print dispatch ids
				else if (component.getClientId().contains("dispatchPrintId")) {
					errorMsg = "INVALID SALE-ID";
					number = Integer.parseInt(submittedValue);
					return number;
				}
				//for print dispatch dates
				else if (component.getClientId().contains("dispatchPrintDate")) {
					errorMsg = "INVALID SALE-DATE";
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Date date=null;
					java.sql.Date dt=null;
					try {
						date = format.parse(submittedValue);
						dt = new java.sql.Date(date.getTime());
					} catch (ParseException e) {
						e.printStackTrace();
					}
					return dt;
				}
				else if(component.getClientId().contains("SalePhone")){
					errorMsg = "INVALID CUSTOMER ID";
					number = Integer.parseInt(submittedValue);
					for (Customer c : dao.findAll(Customer.class)) {
						if (c.getId() == number) {
							return c;
						}
					}
				}else if (component.getClientId().contains("selectEmployee") ) {
					errorMsg = "INVALID EMPLOYEE";
					number = Integer.parseInt(submittedValue);
					for (Employee p : dao.findAll(Employee.class)) {
						if (p.getId() == number) {
							return p.getId();
						}
					}
				} else if (component.getClientId().contains("selectExpenseType") ) {
					errorMsg = "INVALID EXPENSETYPE";
					number = Integer.parseInt(submittedValue);
					for (ExpenseType e : dao.findAll(ExpenseType.class)) {
						if (e.getId() == number) {
							return e.getId();
						}
					}
				}

			} catch (NumberFormatException exception) {
				throw new ConverterException(new FacesMessage(
						FacesMessage.SEVERITY_ERROR, "Conversion Error",
						errorMsg));
			}
		}

		return null;
	}

	public String getAsString(FacesContext facesContext, UIComponent component,
			Object value) {
		if (value == null || value.equals("")
				|| value.toString().contains("com.fmart.hibernate.pojos")) {
			return "";
		} else {
			return value.toString();
			// return String.valueOf(((Dealer) value).getName());
		}
	}
}