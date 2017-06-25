package com.fmart.ui.beans;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.fmart.hibernate.dao.CustomerDao;
import com.fmart.hibernate.dao.PurchaseDao;
import com.fmart.hibernate.dao.SaleDao;
import com.fmart.hibernate.dao.SaleDetailDao;
import com.fmart.hibernate.pojos.Company;
import com.fmart.hibernate.pojos.Customer;
import com.fmart.hibernate.pojos.Employee;
import com.fmart.hibernate.pojos.Inventory;
import com.fmart.hibernate.pojos.InventoryId;
import com.fmart.hibernate.pojos.LazySorter;
import com.fmart.hibernate.pojos.Product;
import com.fmart.hibernate.pojos.ProductGrp;
import com.fmart.hibernate.pojos.Purchase;
import com.fmart.hibernate.pojos.PurchaseDetail;
import com.fmart.hibernate.pojos.Receipt;
import com.fmart.hibernate.pojos.Roles;
import com.fmart.hibernate.pojos.SaleDetail;
import com.fmart.hibernate.pojos.Sale;
import com.fmart.ui.utils.SessionInfo;

/**
 * 
 * @author jay
 */
@ManagedBean
@ViewScoped
public class SaleBean implements Serializable {

	public SaleBean() {
		this.selectedCompany.setId(UserSession.getSession().getCompanyId());
		this.newSale.setAdvance(BigDecimal.valueOf(0.0));
	}

	private static final long serialVersionUID = 1L;

	String page;
	private Product selectedProduct = new Product();
	private Company selectedCompany = new Company();
	private ProductGrp selectedProductGroup = new ProductGrp();

	private List<Product> products = new ArrayList<Product>();
	private List<ProductGrp> productGrps = new ArrayList<ProductGrp>();

	private Sale newSale = new Sale();
	private LazyDataModel<Sale> sales = null;
	private List<SaleDetail> saleDetails = new ArrayList<SaleDetail>();
	private Inventory inventory = new Inventory();
	private Set<InventoryId> inventories = new HashSet<InventoryId>();

	private transient CommandButton pageTypeButton;

	// for add product
	private SaleDetail newSaleDetail = new SaleDetail();
	// for delete product
	private SaleDetail[] selectedSaleDetails;
	// for delete sale
	private Sale[] selectedSales;

	// for print functionality
	private Sale printSale = new Sale();
	private Date printDate = new Date();
	private List<SaleDetail> saleDetailsById = new ArrayList<SaleDetail>();
	private List<SaleDetail> dispatchDetails = new ArrayList<SaleDetail>();

	// customer details
	private Customer newCustomer = new Customer();
	private Customer selectedCustomer = new Customer();
	private Customer saveCustomer = new Customer();
	private List<Customer> customers = new ArrayList<Customer>();

	public Customer getSaveCustomer() {
		return saveCustomer;
	}

	public void setSaveCustomer(Customer saveCustomer) {
		this.saveCustomer = saveCustomer;
	}

	public Customer getSelectedCustomer() {
		return selectedCustomer;
	}

	public void setSelectedCustomer(Customer selectedCustomer) {
		setSaveCustomer(selectedCustomer);
		this.selectedCustomer = selectedCustomer;
	}

	public Customer getNewCustomer() {
		return newCustomer;
	}

	public void setNewCustomer(Customer newCustomer) {
		this.newCustomer = newCustomer;
	}

	public List<Customer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}

	public List<SaleDetail> getDispatchDetails() {
		 ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		 dispatchDetails = (List<SaleDetail>) ec.getFlash().get("dispatchDetails"); 
		 setPrintSale((Sale) ec.getFlash().get("printSale"));
		return dispatchDetails;
	}

	public void setDispatchDetails(List<SaleDetail> dispatchDetails) {
		this.dispatchDetails = dispatchDetails;
	}

	public List<SaleDetail> getSaleDetailsById() {
		if (saleDetailsById == null || saleDetailsById.size() == 0) {
			SaleDao dao = new SaleDao();
			this.printSale = dao.findById(UserSession.getSession()
					.getBookingId());
			if (printSale != null) {
				setSaleDetailsById(printSale.getSaleDetails());
			} else {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
								"Booking is not available for the Id : "
										+ UserSession.getSession()
												.getBookingId()));
				setSaleDetailsById(new ArrayList<SaleDetail>());
				return new ArrayList<SaleDetail>();
			}
		}
		return saleDetailsById;
	}

	public void setSaleDetailsById(List<SaleDetail> saleDetailsById) {
		if (saleDetailsById != null)
			for (SaleDetail sd : saleDetailsById)
				if (sd.getReturned() == false && sd.getDispatched() == false)
					this.saleDetailsById.add(sd);
	}

	public Date getPrintDate() {
		return printDate;
	}

	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}

	public Sale getPrintSale() {
		return printSale;
	}

	public void setPrintSale(Sale printSale) {
		this.printSale = printSale;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public List<ProductGrp> getProductGrps() {
		return productGrps;
	}

	public void setProductGrps(List<ProductGrp> productGrps) {
		this.productGrps = productGrps;
	}

	public Sale getNewSale() {
		return newSale;
	}

	public List<SaleDetail> getSaleDetails() {
		return saleDetails;
	}

	public void setSaleDetails(List<SaleDetail> saleDetails) {
		this.saleDetails = saleDetails;
		for (SaleDetail sd : saleDetails)
			sd.queryDispatchedQuantity();
	}

	public SaleDetail getNewSaleDetail() {
		return newSaleDetail;
	}

	public void setNewSaleDetail(SaleDetail newSaleDetail) {
		this.newSaleDetail = newSaleDetail;
	}

	public SaleDetail[] getSelectedSaleDetails() {
		return selectedSaleDetails;
	}

	public void setSelectedSaleDetails(SaleDetail[] selectedSaleDetails) {
		this.selectedSaleDetails = selectedSaleDetails;
	}

	public Sale[] getSelectedSales() {
		return selectedSales;
	}

	public void setSelectedSales(Sale[] selectedSales) {
		this.selectedSales = selectedSales;
	}

	public void setNewSale(Sale newSale) {
		SaleDao dao = new SaleDao();
		newSale = dao.findById(newSale.getId());
		this.newSale = newSale;
		page = pageTypeButton.getId();

		if ("sale_update".equalsIgnoreCase(page)) {

			setSaleDetails(newSale.getSaleDetails());
			List<SaleDetail> sd = new ArrayList<SaleDetail>();
			for (SaleDetail saleDetail : newSale.getSaleDetails()) {

				if (!saleDetail.getReturned() && !saleDetail.getDispatched()) {
					sd.add(saleDetail);
					InventoryId newInventoryId = new InventoryId();
					newInventoryId.setCompanyId(selectedCompany.getId());
					newInventoryId
							.setProductId(saleDetail.getProduct().getId());
					newInventoryId.setCustomised(saleDetail.getCustomised());
					inventories.add(newInventoryId);
				}
			}
			setSaleDetails(sd);
		}
		if ("sale_return".equalsIgnoreCase(page)
				|| "sale_dispatch".equalsIgnoreCase(page)) {
			setSelectedCompany(newSale.getCompany());
			List<SaleDetail> sd = new ArrayList<SaleDetail>();
			for (SaleDetail saleDetail : newSale.getSaleDetails()) {
				if (!saleDetail.getReturned() && !saleDetail.getDispatched())
					sd.add(saleDetail);
			}
			setSaleDetails(sd);
		}
	}

	public Set<InventoryId> getInventories() {
		return inventories;
	}

	public void setInventories(Set<InventoryId> inventories) {
		this.inventories = inventories;
	}

	public Product getSelectedProduct() {
		return selectedProduct;
	}

	public Company getSelectedCompany() {
		return selectedCompany;
	}

	public void setSelectedCompany(Company selectedCompany) {
		this.selectedCompany = selectedCompany;
	}

	public void setSelectedProduct(Product selectedProduct) {
		this.selectedProduct = selectedProduct;
	}

	public ProductGrp getSelectedProductGroup() {
		return selectedProductGroup;
	}

	public void setSelectedProductGroup(ProductGrp selectedProductGroup) {
		this.selectedProductGroup = selectedProductGroup;
		this.selectedProduct = null;
	}

	public CommandButton getPageTypeButton() {
		return pageTypeButton;
	}

	public void setPageTypeButton(CommandButton pageTypeButton) {
		this.pageTypeButton = pageTypeButton;
	}

	public LazyDataModel<Sale> populateSaleList() {
		if (sales == null) {
			sales = new LazyDataModel<Sale>() {

				private static final long serialVersionUID = 1L;

				@Override
				public List<Sale> load(int first, int pageSize,
						String sortField, SortOrder sortOrder,
						Map<String, Object> filters) {
					SaleDao dao = new SaleDao();
					Object employeeId=null;
					employeeId=UserSession.getSession().getEmployeeId();
					for(Roles role: UserSession.getSession().getRoles()){
						if(role.getName().equalsIgnoreCase("Admin") || role.getName().equalsIgnoreCase("Super Admin"))
							employeeId=null;
					}
						
					List<Sale> saleList = dao.findByProperty("company.id",
							UserSession.getSession().getCompanyId(),"created_by",employeeId, first,
							pageSize);
					// sort
					if (sortField != null) {
						Collections.sort(saleList, new LazySorter(sortField,
								sortOrder));
					}

					// rowCount
					this.setRowCount(dao.countByProperty(UserSession
							.getSession().getCompanyId()));
					return saleList;
				}
			};

		}
		return this.sales;
	}

	public List<ProductGrp> populateProductGroups(String query) {
		SaleDao dao = new SaleDao();
		this.productGrps = dao.findAllNameContains(ProductGrp.class, query,
				null);
		return productGrps;
	}

	public List<Product> populateProducts(String query) {
		SaleDao dao = new SaleDao();
		this.products = dao.findAllNameContains(Product.class, query,
				"product_grp_id=" + selectedProductGroup.getId());
		for (Product p : products)
			p.queryInventory();
		return products;
	}

	public List<Customer> populateCustomers(String query) {
		SaleDao dao = new SaleDao();
		this.customers = dao.findAllNameContains(Customer.class, query,
				"company.id=" + UserSession.getSession().getCompanyId());
		return customers;
	}

	public void clearSaleDetails(ActionEvent actionEvent) {
		this.saleDetails = new ArrayList<SaleDetail>();
	}

	public void addSaleDetail(ActionEvent actionEvent) {
		InventoryId newInventoryId = new InventoryId();
		newInventoryId.setCompanyId(selectedCompany.getId());
		newInventoryId.setProductId(selectedProduct.getId());
		newInventoryId.setCustomised(newSaleDetail.getCustomised());

		if (!inventories.contains(newInventoryId)) {
			newSaleDetail.setProduct(selectedProduct);
			newSaleDetail.getProduct().setProductGrp(selectedProductGroup);
			newSaleDetail.setRate(BigDecimal.valueOf(selectedProduct
					.getUnitPrice() * 1.5));
			saleDetails.add(newSaleDetail);

			inventories.add(newInventoryId);
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							"Product already exist in the list"));
		}
		setSelectedProduct(new Product());
		setSelectedProductGroup(new ProductGrp());
		setNewSaleDetail(new SaleDetail());
	}

	public void deleteSaleDetails() {
		boolean flag = true;
		try {
			if (getSelectedSaleDetails() == null
					|| (getSelectedSaleDetails() != null && getSelectedSaleDetails().length == 0)) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
								"Nothing to Delete"));
				return;
			}
			for (SaleDetail saleDetail : getSelectedSaleDetails()) {
				if(saleDetail.getDispatchedQuantity()>0){
					flag = false;
					break;
				}
			}
			if (flag) {
				for (SaleDetail saleDetail : getSelectedSaleDetails()) {
					saleDetails.remove(saleDetail);

					InventoryId newInventoryId = new InventoryId();
					newInventoryId.setCompanyId(selectedCompany.getId());
					newInventoryId
							.setProductId(saleDetail.getProduct().getId());
					newInventoryId.setCustomised(saleDetail.getCustomised());

					inventories.remove(newInventoryId);
				}
				setSelectedSaleDetails(null);
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							"Error Deleting Purchase Details"));
		}
		if(flag)
			FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
						"Sale Details deleted successfully"));
		else
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							"Sale Details could not be deleted : Dispatched Items present"));
	}

	public void cancelSaleEntry(ActionEvent actionEvent) {
		SaleDao dao = new SaleDao();
		String result = null;
		try {
			dao.cancelBooking(newSale);
			setSaleDetails(new ArrayList<SaleDetail>());
			setInventories(new HashSet<InventoryId>());
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							" Error Cancelling Booking"));
			return;
		}
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
						"Booking Cancelled"));

	}

	public void saveSaleEntry(ActionEvent actionEvent) {

		page = pageTypeButton.getId();
		if ("sale_entry".equalsIgnoreCase(page)) {
			newSale.setStatus("NEW BOOKING");
		} else if ("sale_update".equalsIgnoreCase(page)) {
			newSale.setStatus("UPDATED BOOKING");
		} else if ("sale_return".equalsIgnoreCase(page)) {
			newSale.setStatus("RETURN BOOKING");
		}

		SaleDao dao = new SaleDao();
		List<Inventory> inventoriesToSave = new ArrayList<Inventory>();
		BigDecimal amount = BigDecimal.ZERO;
		String result = null;
		this.newSale.setSaleDetails(saleDetails);
		this.newSale.setCompany(selectedCompany);

		if (this.newSale.getSaleDetails().size() == 0) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							"Empty Booking Entry. Please Enter Products"));
			return;
		}

		try {
			// New Booking Entry
			Employee emp = new Employee();
			emp.setId(UserSession.getSession().getEmployeeId());
			if ("sale_entry".equalsIgnoreCase(page)) {
				for (SaleDetail sd : saleDetails) {

					// amount for individual SALE DETAILS
					sd.setAmount(sd.getRate().multiply(
							new BigDecimal(sd.getQuantity())));

					String remark = sd.getRemarks();
					if (remark == null || remark.isEmpty() || remark == "")
						sd.setRemarks(null);
					sd.setReturned(false);
					sd.setDispatched(false);

					// amount for SALE
					amount = amount.add(sd.getRate().multiply(
							new BigDecimal(sd.getQuantity())));
					sd.setCreated(new Date());
					sd.setUpdated(new Date());
					sd.setEmployeeByCreatedBy(emp);
					sd.setEmployeeByUpdatedBy(emp);
				}

				this.newSale.setAmount(amount);

				this.newSale.setEmployeeByUpdatedBy(emp);
				this.newSale.setEmployeeByCreatedBy(emp);
				newSale.setCreated(new Date());
				newSale.setUpdated(new Date());

				Receipt rec = new Receipt();
				rec.setAmount(newSale.getAdvance());
				rec.setCompany(selectedCompany);
				rec.setEmployeeByCreatedBy(emp);
				rec.setParticulars("ADVANCE");
				rec.setEmployeeByCreatedBy(emp);
				rec.setEmployeeByUpdatedBy(emp);
				rec.setCreated(new Date());
				rec.setUpdated(new Date());
				Set<Receipt> receipts = new HashSet<Receipt>();
				receipts.add(rec);

				newSale.setCustomer(saveCustomer);
				newSale.setReceipts(receipts);

				// Save and Clear UI Components
				result = dao.saveSale(newSale, page);
				newSale = new Sale();
				saleDetails.clear();
				inventories.clear();
			} else if ("sale_update".equalsIgnoreCase(page)) {
				for (SaleDetail sd : saleDetails) {
					// amount for individual SALE DETAILS
					sd.setAmount(sd.getRate().multiply(
							new BigDecimal(sd.getQuantity())));
					String remark = sd.getRemarks();
					if (remark == null || remark.isEmpty() || remark == "")
						sd.setRemarks(null);

					sd.setDispatched(false);
					sd.setReturned(false);

					// amount for SALE
					amount = amount.add(sd.getRate().multiply(
							new BigDecimal(sd.getQuantity())));

					if (sd.getDispatchedQuantity() != null
							&& sd.getQuantity() < sd.getDispatchedQuantity()) {
						throw new Exception(
								"Quantity is lesser than already Dispatched Quantity for Product :"
										+ sd.getProduct().getName());
					}

				}

				this.newSale.setAmount(amount);

				result = dao.saveSale(newSale, page);
				saleDetails.clear();
				inventories.clear();
			} else if ("sale_return".equalsIgnoreCase(page)) {
				List<SaleDetail> returnSaleDetails = new ArrayList<SaleDetail>();
				for (SaleDetail sd : saleDetails) {
					if (sd.getDummyQuantity() == 0)
						continue;
					returnSaleDetails.add(sd);
					sd.setQuantity(sd.getDummyQuantity());

					// amount for individual SALE DETAILS
					sd.setAmount(sd.getRate().multiply(
							new BigDecimal(sd.getQuantity())));

					// create inventory items
					InventoryId inventoryId = new InventoryId();
					inventoryId.setCompanyId(selectedCompany.getId());
					inventoryId.setProductId(sd.getProduct().getId());
					inventoryId.setCustomised(sd.getCustomised());

					inventory.setQuantity(sd.getQuantity() * -1);
					inventory.setId(inventoryId);
					inventoriesToSave.add(inventory);
					setInventory(new Inventory());
					// amount for SALE
					amount = amount.add(sd.getRate().multiply(
							new BigDecimal(sd.getQuantity())));

					sd.setReturned(true);
					sd.setDispatched(false);
				}

				this.newSale.setAmount(amount);

				// Save Sale Returns
				dao.updateInventory(inventoriesToSave);
				newSale.setSaleDetails(returnSaleDetails);
				result = dao.saveSale(newSale, page);
				inventoriesToSave.clear();
			} else if ("sale_dispatch".equalsIgnoreCase(page)) {
				List<SaleDetail> dispatchSaleDetails = new ArrayList<SaleDetail>();
				for (SaleDetail sd : saleDetails) {
					if (sd.getDummyQuantity() == 0)
						continue;
					else
						dispatchSaleDetails.add(sd);
					sd.setQuantity(sd.getDummyQuantity());

					// amount for individual SALE DETAILS
					sd.setAmount(sd.getRate().multiply(
							new BigDecimal(sd.getQuantity())));

					// create inventory items
					InventoryId inventoryId = new InventoryId();
					inventoryId.setCompanyId(selectedCompany.getId());
					inventoryId.setProductId(sd.getProduct().getId());
					inventoryId.setCustomised(sd.getCustomised());

					inventory.setQuantity(sd.getQuantity() * 1);
					inventory.setId(inventoryId);
					inventoriesToSave.add(inventory);
					setInventory(new Inventory());
					// amount for SALE
					amount = amount.add(sd.getRate().multiply(
							new BigDecimal(sd.getQuantity())));

					sd.setReturned(false);
					sd.setDispatched(true);
				}

				this.newSale.setAmount(amount);

				// Save Sale Dispatched
				newSale.setSaleDetails(dispatchSaleDetails);
				dao.updateInventory(inventoriesToSave);
				result = dao.saveSale(newSale, page);
				inventoriesToSave.clear();
				
				//Sale_dispatch print logic
				ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			    ec.getFlash().put("dispatchDetails", dispatchSaleDetails);
			    ec.getFlash().put("printSale", newSale);
			    
				FacesContext.getCurrentInstance()
				   .getExternalContext().redirect("dispatch_print.xhtml");
				return;
				
			}
		} catch (Exception e) {
			String res = null;
			if (e.getMessage().contains("In-Sufficient Inventory"))
				res = e.getMessage();
			else if (e.getMessage().contains("Inventory not available"))
				res = e.getMessage();
			else if (e.getMessage().contains("Dispatched Quantity for Product"))
				res = e.getMessage();

			if (res != null)
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
								res));
			else
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
								" Error Saving Booking/Sale"));
			return;
		}
		newSale = new Sale();
		newSale.setAdvance(BigDecimal.valueOf(0.0));
		saleDetails.clear();
		inventories.clear();
		inventoriesToSave.clear();
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", result));

	}

	public void saveCustomer(ActionEvent actionEvent) {

		CustomerDao dao = new CustomerDao();
		try {
			Company comp = new Company();
			comp.setId(UserSession.getSession().getCompanyId());
			newCustomer.setPhoneString(newCustomer.getPhone().toString());
			newCustomer.setCompany(comp);
			dao.save(newCustomer);
			customers.add(newCustomer);
			setSelectedCustomer(newCustomer);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							"Error Saving Customer"));
			return;
		}
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
						"Customer Saved successfully"));

	}
}
