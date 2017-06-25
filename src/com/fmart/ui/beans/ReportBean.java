package com.fmart.ui.beans;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;

import org.apache.catalina.startup.SetContextPropertiesRule;
import org.primefaces.component.commandbutton.CommandButton;

import com.fmart.framework.hibernate.FMartGenericDAO;
import com.fmart.hibernate.dao.DealerDao;
import com.fmart.hibernate.dao.EmployeeDao;
import com.fmart.hibernate.dao.ExpenseTypeDao;
import com.fmart.hibernate.dao.ReportDao;
import com.fmart.hibernate.dao.SaleDao;
import com.fmart.hibernate.pojos.Company;
import com.fmart.hibernate.pojos.Dealer;
import com.fmart.hibernate.pojos.Employee;
import com.fmart.hibernate.pojos.Expense;
import com.fmart.hibernate.pojos.ExpenseType;
import com.fmart.hibernate.pojos.Inventory;
import com.fmart.hibernate.pojos.InventoryId;
import com.fmart.hibernate.pojos.Product;
import com.fmart.hibernate.pojos.ProductGrp;
import com.fmart.hibernate.pojos.Purchase;
import com.fmart.hibernate.pojos.PurchaseDetail;
import com.fmart.hibernate.pojos.Receipt;
import com.fmart.hibernate.pojos.Report;
import com.fmart.hibernate.pojos.Roles;
import com.fmart.hibernate.pojos.SaleDetail;
import com.fmart.hibernate.pojos.Sale;
import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

/**
 * 
 * @author jay
 */
@ManagedBean
@ViewScoped
public class ReportBean implements Serializable {

	public ReportBean() {
		if(UserSession.getSession().getEmployeeId() != 1){
			setSelectedCompanyId(UserSession.getSession().getCompanyId());
			Company newCompany= new Company();
			newCompany.setId(UserSession.getSession().getCompanyId());
			setSelectedCompany(newCompany);
		}
	}

	private static final long serialVersionUID = 1L;

	private List<Sale> sales = new ArrayList<Sale>();
	private List<Receipt> receipts = new ArrayList<Receipt>();
	private List<SaleDetail> saleDetails = new ArrayList<SaleDetail>();
	private List<Purchase> purchases = new ArrayList<Purchase>();
	private List<PurchaseDetail> purchaseDetails = new ArrayList<PurchaseDetail>();
	private List<Report> reports = new ArrayList<Report>();
	private List<Dealer> dealers = new ArrayList<Dealer>();
	private List<Expense> expenses = new ArrayList<Expense>();
	
	private Report selectedReport = new Report();
	private Company selectedCompany = new Company();
	private Dealer selectedDealer = new Dealer();
	private String timePeriod;
	private Date fromDate = new Date();
	private Date toDate = new Date();
	private boolean dateRange = true;
	private int selectedEmployeeId = 0;
	private int selectedCompanyId = 0;
	private int selectedExpenseTypeId = 0;
	private boolean disableEmployee = true;
	private boolean generateReportButton = false;
	private BigDecimal totalGrossProfitLoss = BigDecimal.ZERO;

	public boolean isDisableEmployee() {
		return disableEmployee;
	}

	public void setDisableEmployee(boolean disableEmployee) {
		this.disableEmployee = disableEmployee;
	}

	public int getSelectedExpenseTypeId() {
		return selectedExpenseTypeId;
	}

	public void setSelectedExpenseTypeId(int selectedExpenseTypeId) {
		ExpenseTypeDao dao = new ExpenseTypeDao();
		ExpenseType et = dao.findById(selectedExpenseTypeId);
		if(et != null && et.getName().equalsIgnoreCase("salary")){
			disableEmployee = false;
		}else{
			disableEmployee = true;
		}	
		this.selectedExpenseTypeId = selectedExpenseTypeId;
	}

	public List<Expense> getExpenses() {
		return expenses;
	}

	public void setExpenses(List<Expense> expenses) {
		this.expenses = expenses;
	}

	public boolean isGenerateReportButton() {
		return generateReportButton;
	}

	public void setGenerateReportButton(boolean generateReportButton) {
		this.generateReportButton = generateReportButton;
	}

	public int getSelectedCompanyId() {
		return selectedCompanyId;
	}

	public void setSelectedCompanyId(int selectedCompanyId) {
		this.selectedCompanyId = selectedCompanyId;
		if (this.selectedCompanyId > 0) {
			setGenerateReportButton(true);
		} else {
			setGenerateReportButton(false);
			selectedEmployeeId = 0;
		}
	}

	public int getSelectedEmployeeId() {
		return selectedEmployeeId;
	}

	public void setSelectedEmployeeId(int selectedEmployeeId) {
		this.selectedEmployeeId = selectedEmployeeId;
		if (this.selectedCompanyId > 0) {
			setGenerateReportButton(true);
		} else
			setGenerateReportButton(false);
	}

	public String getTimePeriod() {
		return timePeriod;
	}

	public void setTimePeriod(String timePeriod) {
		this.timePeriod = timePeriod;
	}

	public List<Sale> getSales() {
		return sales;
	}

	public void setSales(List<Sale> sales) {
		this.sales = sales;
	}

	public List<Receipt> getReceipts() {
		return receipts;
	}

	public void setReceipts(List<Receipt> receipts) {
		this.receipts = receipts;
	}

	public List<SaleDetail> getSaleDetails() {
		return saleDetails;
	}

	public void setSaleDetails(List<SaleDetail> saleDetails) {
		this.saleDetails = saleDetails;
	}

	public List<Purchase> getPurchases() {
		return purchases;
	}

	public void setPurchases(List<Purchase> purchases) {
		this.purchases = purchases;
	}

	public List<PurchaseDetail> getPurchaseDetails() {
		return purchaseDetails;
	}

	public void setPurchaseDetails(List<PurchaseDetail> purchaseDetails) {
		this.purchaseDetails = purchaseDetails;
	}

	public List<Dealer> getDealers() {
		return dealers;
	}

	public void setDealers(List<Dealer> dealers) {
		this.dealers = dealers;
	}

	public boolean isDateRange() {
		return dateRange;
	}

	public void setDateRange(boolean dateRange) {
		this.dateRange = dateRange;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public Company getSelectedCompany() {
		return selectedCompany;
	}

	public void setSelectedCompany(Company selectedCompany) {
		this.selectedCompany = selectedCompany;
	}

	public Dealer getSelectedDealer() {
		return selectedDealer;
	}

	public void setSelectedDealer(Dealer selectedDealer) {
		this.selectedDealer = selectedDealer;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Report getSelectedReport() {
		return selectedReport;
	}

	public void setSelectedReport(Report selectedReport) {
		this.selectedReport = selectedReport;
	}

	public List<Report> getReports() {
		return reports;
	}

	public void setReports(List<Report> reports) {
		this.reports = reports;
	}

	public void TimePeriodChangeListener(ValueChangeEvent event) {
		if (event.getNewValue().toString().equalsIgnoreCase("range")) {
			setDateRange(false);
		} else
			setDateRange(true);
	}

	public void companyChangeListener(ValueChangeEvent evt) {
		if (evt.getNewValue() != null)
			setSelectedCompanyId((Integer) evt.getNewValue());
		else
			setSelectedCompanyId(0);
	}

	public void generateTimePeriod() {
		if (timePeriod.equalsIgnoreCase("today")) {
			Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_MONTH, date.getDate());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			setFromDate(cal.getTime());
			setToDate(cal.getTime());
		} else if (timePeriod.equalsIgnoreCase("month")) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			setFromDate(cal.getTime());
			setToDate(new Date());
		} else if (timePeriod.equalsIgnoreCase("range")) {
			if (fromDate.compareTo(toDate) > 0) {
				Date temp = new Date();
				temp = fromDate;
				setFromDate(toDate);
				setToDate(temp);
			}
		}

		Calendar c = Calendar.getInstance();
		c.setTime(getToDate());
		c.add(Calendar.DATE, 1);
		setToDate(c.getTime());

	}

	public List<Sale> generateBookingReports(ActionEvent event) {
		ReportDao dao = new ReportDao();

		String result;
		try {
			generateTimePeriod();
			this.sales.clear();
			List<Sale> sales = new ArrayList<Sale>();
			sales = dao.generateReportsCreated(Sale.class,
					selectedCompany.getId(), fromDate, toDate,
					selectedEmployeeId);
			for (Sale s : sales)
				if (s.getStatus().contains("BOOK")
						&& s.getUpdated().compareTo(fromDate) >= 0
						&& s.getUpdated().compareTo(toDate) < 0)
					this.sales.add(s);
			if (this.sales.isEmpty()) {
				result = "No Records Found for Given Details!";
			} else
				result = "Booking Report Generated Successfully!";

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							" Error Generating Booking Report!"));
			return null;
		}
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", result));

		return this.sales;
	}

	public List<Receipt> generateBookingReceiptReports(ActionEvent event) {
		ReportDao dao = new ReportDao();
		String result;
		try {
			generateTimePeriod();
			this.receipts.clear();
			List<Receipt> receipts = new ArrayList<Receipt>();
			receipts = dao.generateReportsCreated(Receipt.class,
					selectedCompany.getId(), fromDate, toDate,
					selectedEmployeeId);
			for (Receipt r : receipts)
				if (r.getSale().getStatus().contains("BOOK")
						&& r.getUpdated().compareTo(fromDate) >= 0
						&& r.getUpdated().compareTo(toDate) < 0)
					this.receipts.add(r);
			if (this.receipts.isEmpty()) {
				result = "No Records Found for Given Details!";
			} else
				result = "Booking Receipt Report Generated Successfully!";

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							" Error Generating Booking Receipt Report!"));
			return null;
		}
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", result));

		return this.receipts;
	}

	public List<Sale> generateCancelledBookingReports(ActionEvent event) {
		ReportDao dao = new ReportDao();
		String result;
		try {
			generateTimePeriod();
			this.sales.clear();
			List<Sale> sales = new ArrayList<Sale>();
			sales = dao.generateReportsCreated(Sale.class,
					selectedCompany.getId(), fromDate, toDate,
					selectedEmployeeId);
			for (Sale s : sales)
				if (s.getStatus().contains("CANCEL")
						&& s.getUpdated().compareTo(fromDate) >= 0
						&& s.getUpdated().compareTo(toDate) < 0)
					this.sales.add(s);
			if (this.sales.isEmpty()) {
				result = "No Records Found for Given Details!";
			} else
				result = "Cancelled Booking Report Generated Successfully!";

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							" Error Generating Cancelled Booking Report!"));
			return null;
		}
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", result));
		return this.sales;
	}

	public List<Sale> generateSaleReports(ActionEvent event) {
		ReportDao dao = new ReportDao();
		String result;
		try {
			generateTimePeriod();
			this.sales.clear();
			List<Sale> sales = new ArrayList<Sale>();
			sales = dao.generateReportsCreated(Sale.class,
					selectedCompany.getId(), fromDate, toDate,
					selectedEmployeeId);
			for (Sale s : sales)
				if (s.getStatus().contains("CLEARED BOOKING")
						&& s.getCreated().compareTo(fromDate) >= 0
						&& s.getCreated().compareTo(toDate) < 0)
					this.sales.add(s);
			if (this.sales.isEmpty()) {
				result = "No Records Found for Given Details!";
			} else
				result = "Sale Report Generated Successfully!";

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							" Error Generating Sale Report!"));
			return null;
		}
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", result));

		return this.sales;
	}
	
	public List<Sale> generateProfitLossReports(ActionEvent event) {
		ReportDao dao = new ReportDao();
		String result;
		totalGrossProfitLoss = BigDecimal.ZERO;
		try {
			generateTimePeriod();
			this.sales.clear();
			List<Sale> sales = new ArrayList<Sale>();
			sales = dao.generateReportsCreated(Sale.class,
					selectedCompany.getId(), fromDate, toDate,
					selectedEmployeeId);
			for (Sale s : sales)
				if (s.getCreated().compareTo(fromDate) >= 0
						&& s.getCreated().compareTo(toDate) < 0){
					SaleDao saleDao = new SaleDao();
					Sale temp = saleDao.findById(s.getId());
					BigDecimal saleCostPrice = BigDecimal.ZERO;
					for(SaleDetail sd : temp.getSaleDetails()){
						if(sd.getReturned()==false && sd.getDispatched()==false){
							BigDecimal unitPrice = new BigDecimal(sd.getProduct().getUnitPrice());
							saleCostPrice = saleCostPrice.add(unitPrice.multiply(
									new BigDecimal(sd.getQuantity())));
						}
						totalGrossProfitLoss = totalGrossProfitLoss.add(sd.getAmount().subtract(saleCostPrice));
					}
					s.setCost_amount(saleCostPrice);
					this.sales.add(s);
				}
			if (this.sales.isEmpty()) {
				result = "No Records Found for Given Details!";
			} else
				result = "Sale Report Generated Successfully!";

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							" Error Generating Sale Report!"));
			return null;
		}
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", result));

		return this.sales;
	}
			
	public List<Sale> generateCommissionReports(ActionEvent event) {
		ReportDao dao = new ReportDao();
		EmployeeDao empDao = new EmployeeDao();
		BigDecimal salePercent = BigDecimal.ZERO;
		String result;
		try {
			generateTimePeriod();
			this.sales.clear();
			List<Sale> sales = new ArrayList<Sale>();
			sales = dao.generateReportsCreated(Sale.class,
					selectedCompany.getId(), fromDate, toDate,
					selectedEmployeeId);

			for (Sale s : sales)
				if (s.getStatus().contains("CLEARED BOOKING")
						&& s.getCreated().compareTo(fromDate) >= 0
						&& s.getCreated().compareTo(toDate) < 0){
					Employee e = empDao.findById(s.getEmployeeByCreatedBy().getId());
					if(e.getSalePercent()==null)
						salePercent = BigDecimal.ZERO;
					else 
						salePercent = e.getSalePercent();
					s.setCommission((s.getAmount().divide(new BigDecimal(100))).multiply(salePercent));
					this.sales.add(s);
				}
			if (this.sales.isEmpty()) {
				result = "No Records Found for Given Details!";
			} else
				result = "Sale-Commission Report Generated Successfully!";

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							" Error Generating Sale-Commission Report!"));
			return null;
		}
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", result));

		return this.sales;
	}
	
	public List<SaleDetail> generateBookingDispatchReport(ActionEvent event) {
		ReportDao dao = new ReportDao();
		String result;
		try {
			generateTimePeriod();
			this.saleDetails.clear();
			List<Sale> sales = new ArrayList<Sale>();
			sales = dao.generateReportsUpdated(Sale.class,
					selectedCompany.getId(), fromDate, toDate,
					selectedEmployeeId);
			for (Sale eachSale : sales) {
				for (SaleDetail sd : eachSale.getSaleDetails()) {
					if (sd.getDispatched().equals(true)
							&& sd.getUpdated().compareTo(fromDate) >= 0
							&& sd.getUpdated().compareTo(toDate) < 0)
						this.saleDetails.add(sd);
				}
			}
			if (this.saleDetails.isEmpty()) {
				result = "No Records Found for Given Details!";
			} else
				result = "Booking Dispatch Report Generated Successfully!";

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							" Error Generating Sale Return Report!"));
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", result));
		return this.saleDetails;
	}

	public List<SaleDetail> generateBookingReturnReports(ActionEvent event) {
		ReportDao dao = new ReportDao();
		String result;
		try {
			generateTimePeriod();
			this.saleDetails.clear();
			List<Sale> sales = new ArrayList<Sale>();
			sales = dao.generateReportsUpdated(Sale.class,
					selectedCompany.getId(), fromDate, toDate,
					selectedEmployeeId);
			for (Sale eachSale : sales) {
				for (SaleDetail sd : eachSale.getSaleDetails()) {
					if (sd.getReturned().equals(true)
							&& sd.getUpdated().compareTo(fromDate) >= 0
							&& sd.getUpdated().compareTo(toDate) < 0)
						this.saleDetails.add(sd);
				}
			}
			if (this.saleDetails.isEmpty()) {
				result = "No Records Found for Given Details!";
			} else
				result = "Booking Return Report Generated Successfully!";

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							" Error Generating Sale Return Report!"));
			return null;
		}

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", result));
		return this.saleDetails;
	}

	public List<Purchase> generatePurchaseReports(ActionEvent event) {
		ReportDao dao = new ReportDao();
		String result;
		try {
			generateTimePeriod();
			this.purchases.clear();
			this.purchases = dao.generateReportsCreated(Purchase.class,
					selectedCompany.getId(), fromDate, toDate,
					selectedEmployeeId);
			if (this.purchases.isEmpty()) {
				result = "No Records Found for Given Details!";
			} else
				result = "Purchase Report Generated Successfully!";

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							" Error Generating Purchase Report!"));
			return null;
		}
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", result));

		return purchases;
	}

	public List<PurchaseDetail> generatePurchaseReturnReports(ActionEvent event) {
		ReportDao dao = new ReportDao();
		String result;
		try {
			generateTimePeriod();
			this.purchaseDetails.clear();
			List<Purchase> purchases = new ArrayList<Purchase>();
			purchases = dao.generateReportsUpdated(Purchase.class,
					selectedCompany.getId(), fromDate, toDate,
					selectedEmployeeId);
			for (Purchase eachPurchase : purchases) {
				for (PurchaseDetail pd : eachPurchase.getPurchaseDetails()) {
					if (pd.getReturned().equals(true)
							&& pd.getUpdated().compareTo(fromDate) >= 0
							&& pd.getUpdated().compareTo(toDate) < 0)
						this.purchaseDetails.add(pd);
				}
			}
			if (this.purchaseDetails.isEmpty()) {
				result = "No Records Found for Given Details!";
			} else
				result = " Purchase Return Report Generated Successfully!";

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							" Error Generating Purchase Return Report!"));
			return null;
		}
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", result));

		return this.purchaseDetails;
	}

	public List<Purchase> generateSupplierPurchaseReports(ActionEvent event) {
		ReportDao dao = new ReportDao();
		String result;
		try {
			generateTimePeriod();
			this.purchases.clear();
			List<Purchase> purchases = new ArrayList<Purchase>();
			purchases = dao.generateReportsCreated(Purchase.class,
					selectedCompany.getId(), fromDate, toDate,
					selectedEmployeeId);
			for (Purchase p : purchases)
				if (p.getDealer().getId() == selectedDealer.getId()
						&& p.getUpdated().compareTo(fromDate) >= 0
						&& p.getUpdated().compareTo(toDate) < 0)
					this.purchases.add(p);
			if (this.purchases.isEmpty()) {
				result = "No Records Found for Given Details!";
			} else
				result = "Sale Receipts Report Generated Successfully!";

		} catch (Exception e) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Error!",
									"Supplier Purchase Report Generated Successfully!"));
			return null;
		}
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", result));

		return this.purchases;
	}

	public List<PurchaseDetail> generateSupplierPurchaseReturnReports(
			ActionEvent event) {
		ReportDao dao = new ReportDao();
		String result;
		try {
			generateTimePeriod();
			this.purchaseDetails.clear();
			List<Purchase> purchases = new ArrayList<Purchase>();
			purchases = dao.generateReportsUpdated(Purchase.class,
					selectedCompany.getId(), fromDate, toDate,
					selectedEmployeeId);
			for (Purchase eachPurchase : purchases) {
				if (eachPurchase.getDealer().getId() == selectedDealer.getId()) {
					for (PurchaseDetail pd : eachPurchase.getPurchaseDetails()) {
						if (pd.getReturned().equals(true)
								&& pd.getUpdated().compareTo(fromDate) >= 0
								&& pd.getUpdated().compareTo(toDate) < 0)
							this.purchaseDetails.add(pd);
					}
				}
			}
			if (this.purchaseDetails.isEmpty()) {
				result = "No Records Found for Given Details!";
			} else
				result = "Supplier Purchase Return Report Generated Successfully!";

		} catch (Exception e) {
			FacesContext
					.getCurrentInstance()
					.addMessage(
							null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"Error!",
									" Error Generating Supplier Purchase Return Report!"));
			return null;
		}
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", result));

		return this.purchaseDetails;
	}

	public List<Dealer> populateDealers(String query) {
		DealerDao dao = new DealerDao();
		this.dealers = dao.findAllNameContains(query, null);
		return dealers;
	}


	public List<Expense> generateExpenseReports(ActionEvent event) {
		ReportDao dao = new ReportDao();
		String result;
		try {
			generateTimePeriod();
			this.expenses.clear();
			this.expenses = dao.generateExpenseReports(Expense.class,
					selectedCompany.getId(), fromDate, toDate,
					selectedEmployeeId, selectedExpenseTypeId);
			if (this.expenses.isEmpty()) {
				result = "No Records Found for Given Details!";
			} else
				result = "Expense Report Generated Successfully!";

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							" Error Generating Expense Report!"));
			return null;
		}
		setSelectedEmployeeId(0);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", result));
		return this.expenses;
	}

	public List<Employee> populateEmployees() {
		List<Employee> employees = new ArrayList<Employee>();
		EmployeeDao dao = new EmployeeDao();
		if (selectedCompanyId != 0)
			employees = dao.findByProperty("company.id", selectedCompanyId);
		return employees;
	}
	
	
	public List<ExpenseType> populateExpenseType(){
		ExpenseTypeDao dao = new ExpenseTypeDao();
		List<ExpenseType> expenseTypes = dao.findAll();
		return expenseTypes;
	}

	public BigDecimal getTotalGrossProfitLoss() {
		return totalGrossProfitLoss;
	}

	public void setTotalGrossProfitLoss(BigDecimal totalGrossProfitLoss) {
		this.totalGrossProfitLoss = totalGrossProfitLoss;
	}
	
}
