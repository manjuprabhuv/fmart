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
import java.util.Set;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;





import com.fmart.framework.hibernate.FMartGenericDAO;
import com.fmart.hibernate.dao.PurchaseDao;
import com.fmart.hibernate.dao.PurchaseDetailDao;
import com.fmart.hibernate.pojos.Company;
import com.fmart.hibernate.pojos.Dealer;
import com.fmart.hibernate.pojos.Employee;
import com.fmart.hibernate.pojos.Inventory;
import com.fmart.hibernate.pojos.InventoryId;
import com.fmart.hibernate.pojos.LazySorter;
import com.fmart.hibernate.pojos.Product;
import com.fmart.hibernate.pojos.ProductGrp;
import com.fmart.hibernate.pojos.Purchase;
import com.fmart.hibernate.pojos.PurchaseDetail;
import com.fmart.hibernate.pojos.Roles;
import com.fmart.ui.utils.SessionInfo;

/**
 * 
 * @author jay
 */
@ManagedBean
@ViewScoped
public class ViewPurchaseManagedBean implements Serializable {

	/**
	 * 
	 */
	public ViewPurchaseManagedBean() {
		this.selectedCompany.setId(UserSession.getSession().getCompanyId());
	}

	private static final long serialVersionUID = 1L;
	String page;
	private Dealer selectedDealer = new Dealer();
	private Product selectedProduct = new Product();
	private Company selectedCompany = new Company();
	private Dealer selectedDealerCopy = new Dealer();
	private ProductGrp selectedProductGroup = new ProductGrp();

	private List<Dealer> dealers = new ArrayList<Dealer>();
	private List<Product> products = new ArrayList<Product>();
	private List<ProductGrp> productGrps = new ArrayList<ProductGrp>();

	private Purchase newPurchase = new Purchase();
	private LazyDataModel<Purchase> purchases = null;
	private List<PurchaseDetail> purchaseDetails = new ArrayList<PurchaseDetail>();
	private List<PurchaseDetail> oldPurchaseDetails = new ArrayList<PurchaseDetail>();
	private Inventory inventory = new Inventory();
	private Set<InventoryId> inventories = new HashSet<InventoryId>();

	private transient CommandButton pageTypeButton;

	// for add product
	private PurchaseDetail newPurchaseDetail = new PurchaseDetail();
	// for delete product
	private PurchaseDetail[] selectedPurchaseDetails;
	// for delete purchase
	private Purchase[] selectedPurchases;

	private Purchase printPurchase = new Purchase();
	private List<PurchaseDetail> purchaseDetailsById = null;
	private int purchaseId;
	private Date printDate = new Date();

	public Date getPrintDate() {
		return printDate;
	}

	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}

	public List<PurchaseDetail> getPurchaseDetailsById() {
		if (purchaseDetailsById == null) {
			PurchaseDao dao = new PurchaseDao();

			this.printPurchase = dao.findById(UserSession.getSession()
					.getPurchaseId());
			if (printPurchase != null) {
				setPurchaseDetailsById(printPurchase.getPurchaseDetails());
			} else {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
								"Purchase is not available for the Id : "
										+ UserSession.getSession()
												.getPurchaseId()));
				setPurchaseDetailsById(new ArrayList<PurchaseDetail>());
				return new ArrayList<PurchaseDetail>();
			}
		}
		return purchaseDetailsById;
	}

	public void setPurchaseDetailsById(List<PurchaseDetail> purchaseDetailsById) {
		this.purchaseDetailsById = purchaseDetailsById;
	}

	public int getPurchaseId() {
		return purchaseId;
	}

	public void setPurchaseId(int purchaseId) {
		this.purchaseId = purchaseId;
	}

	public Purchase getPrintPurchase() {
		return printPurchase;
	}

	public void setPrintPurchase(Purchase printPurchase) {
		setSelectedCompany(selectedCompany);
		this.printPurchase = printPurchase;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public PurchaseDetail getNewPurchaseDetail() {
		return newPurchaseDetail;
	}

	public void setNewPurchaseDetail(PurchaseDetail newPurchaseDetail) {
		this.newPurchaseDetail = newPurchaseDetail;
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

	public Purchase getNewPurchase() {
		return newPurchase;
	}

	public void setNewPurchase(Purchase newPurchase) {
		PurchaseDao dao = new PurchaseDao();
		newPurchase = dao.findById(newPurchase.getId());
		this.newPurchase = newPurchase;
		page = pageTypeButton.getId();
		if ("purchase_update".equalsIgnoreCase(page)) {

			setSelectedDealer(newPurchase.getDealer());
			setSelectedDealerCopy(newPurchase.getDealer());
			setSelectedCompany(newPurchase.getCompany());
			setPurchaseDetails(newPurchase.getPurchaseDetails());
			List<PurchaseDetail> pd = new ArrayList<PurchaseDetail>();
			for (PurchaseDetail purchaseDetail : newPurchase
					.getPurchaseDetails()) {

				if (!purchaseDetail.getReturned()
						&& purchaseDetail.getQuantity() > 0) {
					pd.add(purchaseDetail);
					InventoryId newInventoryId = new InventoryId();
					PurchaseDetail oldPurchaseDetail = new PurchaseDetail();

					newInventoryId.setCompanyId(selectedCompany.getId());
					newInventoryId.setProductId(purchaseDetail.getProduct()
							.getId());
					newInventoryId
							.setCustomised(purchaseDetail.getCustomised());

					oldPurchaseDetail.setProduct(purchaseDetail.getProduct());
					oldPurchaseDetail.setCustomised(purchaseDetail
							.getCustomised());
					oldPurchaseDetail.setQuantity(purchaseDetail.getQuantity()
							* -1);
					oldPurchaseDetails.add(oldPurchaseDetail);
					inventories.add(newInventoryId);
				}
			}
			setPurchaseDetails(pd);
		}
		if ("purchase_return".equalsIgnoreCase(page)) {
			setSelectedDealer(newPurchase.getDealer());
			setSelectedCompany(newPurchase.getCompany());
			List<PurchaseDetail> pd = new ArrayList<PurchaseDetail>();
			for (PurchaseDetail purchaseDetail : newPurchase
					.getPurchaseDetails()) {
				if (!purchaseDetail.getReturned()
						&& purchaseDetail.getQuantity() > 0)
					pd.add(purchaseDetail);

			}
			setPurchaseDetails(pd);
		}
	}

	public Set<InventoryId> getInventories() {
		return inventories;
	}

	public void setInventories(Set<InventoryId> inventories) {
		this.inventories = inventories;
	}

	public PurchaseDetail[] getSelectedPurchaseDetails() {
		return selectedPurchaseDetails;
	}

	public void setSelectedPurchaseDetails(
			PurchaseDetail[] selectedPurchaseDetails) {
		this.selectedPurchaseDetails = selectedPurchaseDetails;
	}

	public Dealer getSelectedDealer() {
		return selectedDealer;
	}

	public void setSelectedDealer(Dealer selectedDealer) {
		this.selectedDealer = selectedDealer;
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

	public Dealer getSelectedDealerCopy() {
		return selectedDealerCopy;
	}

	public void setSelectedDealerCopy(Dealer selectedDealerCopy) {
		this.selectedDealerCopy = selectedDealerCopy;
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

	public List<Dealer> getDealers() {
		return dealers;
	}

	public void setDealers(List<Dealer> dealers) {
		this.dealers = dealers;
	}

	public List<Dealer> populateDealers(String query) {
		PurchaseDao dao = new PurchaseDao();
		this.dealers = dao.findAllNameContains(Dealer.class, query, null);
		return dealers;
	}

	public List<PurchaseDetail> getPurchaseDetails() {
		return purchaseDetails;
	}

	public void setPurchaseDetails(List<PurchaseDetail> purchaseDetails) {
		this.purchaseDetails = purchaseDetails;
	}

	public List<PurchaseDetail> getOldPurchaseDetails() {
		return oldPurchaseDetails;
	}

	public void setOldPurchaseDetails(List<PurchaseDetail> oldPurchaseDetails) {
		this.oldPurchaseDetails = oldPurchaseDetails;
	}

	public CommandButton getPageTypeButton() {
		return pageTypeButton;
	}

	public void setPageTypeButton(CommandButton pageTypeButton) {
		this.pageTypeButton = pageTypeButton;
	}

	public LazyDataModel<Purchase> getPurchases() {
		if (purchases == null) {
			purchases = new LazyDataModel<Purchase>() {

				private static final long serialVersionUID = 1L;

				@Override
				public List<Purchase> load(int first, int pageSize,
						String sortField, SortOrder sortOrder,
						Map<String, Object> filters) {
					PurchaseDao dao = new PurchaseDao();
					
					Object employeeId=null;
					employeeId=UserSession.getSession().getEmployeeId();
					for(Roles role: UserSession.getSession().getRoles()){
						if(role.getName().equalsIgnoreCase("Admin") || role.getName().equalsIgnoreCase("Super Admin"))
							employeeId=null;
					}
					
					List<Purchase> purchaseList = dao.findByProperty(
							"company.id", UserSession.getSession()
									.getCompanyId(),"created_by",employeeId, first, pageSize);
					// sort
					if (sortField != null) {
						Collections.sort(purchaseList, new LazySorter(
								sortField, sortOrder));
					}

					// rowCount
					this.setRowCount(dao.countByProperty(UserSession
							.getSession().getCompanyId()));
					return purchaseList;
				}
			};

		}
		return this.purchases;
	}

	public void setPurchases(LazyDataModel<Purchase> purchases) {
		this.purchases = purchases;
	}

	public Purchase[] getSelectedPurchases() {
		return selectedPurchases;
	}

	public void setSelectedPurchases(Purchase[] selectedPurchases) {
		this.selectedPurchases = selectedPurchases;
	}

	public List<ProductGrp> populateProductGroups(String query) {
		PurchaseDao dao = new PurchaseDao();
		this.productGrps = dao.findAllNameContains(ProductGrp.class, query,
				null);
		return productGrps;
	}

	public List<Product> populateProducts(String query) {
		PurchaseDao dao = new PurchaseDao();
		this.products = dao.findAllNameContains(Product.class, query,
				"product_grp_id=" + selectedProductGroup.getId());
		return products;
	}

	public void clearPurchaseDetails(ActionEvent actionEvent) {
		this.purchaseDetails = new ArrayList<PurchaseDetail>();
		this.selectedDealer = new Dealer();
	}

	public void addPurchaseDetail(ActionEvent actionEvent) {
		InventoryId newInventoryId = new InventoryId();
		newInventoryId.setCompanyId(selectedCompany.getId());
		newInventoryId.setProductId(selectedProduct.getId());
		newInventoryId.setCustomised(newPurchaseDetail.getCustomised());

		if (!inventories.contains(newInventoryId)) {
			newPurchaseDetail.setProduct(selectedProduct);
			newPurchaseDetail.getProduct().setProductGrp(selectedProductGroup);
			newPurchaseDetail.setRate(BigDecimal.valueOf(selectedProduct
					.getUnitPrice()));
			purchaseDetails.add(newPurchaseDetail);

			inventories.add(newInventoryId);
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							"Product already exist in the list"));
		}
		setSelectedProduct(new Product());
		setSelectedProductGroup(new ProductGrp());
		setNewPurchaseDetail(new PurchaseDetail());
	}

	public void savePurchaseEntry(ActionEvent actionEvent) {

		PurchaseDao dao = new PurchaseDao();
		List<Inventory> inventoriesToSave = new ArrayList<Inventory>();
		BigDecimal amount = BigDecimal.ZERO;
		String result = null;
		if (selectedDealer == null && selectedDealerCopy != null)
			selectedDealer = selectedDealerCopy;
		this.newPurchase.setDealer(selectedDealer);
		this.newPurchase.setPurchaseDetails(purchaseDetails);
		this.newPurchase.setCompany(selectedCompany);

		if (selectedDealer == null || selectedDealer.getId() == 0
				|| purchaseDetails.size() == 0) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
							"Missing Dealer or Purchase Detail Info"));
			return;
		}

		try {
			// New Purchase Entry
			page = pageTypeButton.getId();
			Employee emp = new Employee();
			String description="";
			emp.setId(UserSession.getSession().getEmployeeId());
			if ("purchase_entry".equalsIgnoreCase(page)) {
				for (PurchaseDetail pd : purchaseDetails) {
					// create purchase detail items
					pd.setAmount(pd.getRate().multiply(
							new BigDecimal(pd.getQuantity())));

					// create inventory items
					inventory.setQuantity(pd.getQuantity());
					InventoryId inventoryId = new InventoryId();
					inventoryId.setCompanyId(selectedCompany.getId());
					inventoryId.setProductId(pd.getProduct().getId());
					inventoryId.setCustomised(pd.getCustomised());

					inventory.setId(inventoryId);
					inventoriesToSave.add(inventory);
					setInventory(new Inventory());
					amount = amount.add(pd.getRate().multiply(
							new BigDecimal(pd.getQuantity())));

					String remark = pd.getRemarks();
					if (remark == null || remark.isEmpty() || remark == "")
						pd.setRemarks(null);
					pd.setReturned(false);
					pd.setCreated(new Date());
					pd.setUpdated(new Date());
					pd.setEmployeeByCreatedBy(emp);
					pd.setEmployeeByUpdatedBy(emp);
					
					description=description.concat(pd.getProduct().getName()+" ,");
				}
				
				this.newPurchase.setDescription(description);
				this.newPurchase.setAmount(amount);
				
				this.newPurchase.setEmployeeByCreatedBy(emp);
				this.newPurchase.setEmployeeByUpdatedBy(emp);
				this.newPurchase.setStatus("NEW");
				newPurchase.setCreated(new Date());
				newPurchase.setUpdated(new Date());

				// Save and Clear UI Components
				result = dao.savePurchase(newPurchase, inventoriesToSave, page);
				setNewPurchase(new Purchase());
				setPurchaseDetails(new ArrayList<PurchaseDetail>());
				setSelectedDealer(new Dealer());
				inventories.clear();
			} else if ("purchase_update".equalsIgnoreCase(page)) {
				for (PurchaseDetail pd : purchaseDetails) {
					// create purchase detail items
					pd.setAmount(pd.getRate().multiply(
							new BigDecimal(pd.getQuantity())));

					// create inventory items
					inventory.setQuantity(pd.getQuantity());
					InventoryId inventoryId = new InventoryId();
					inventoryId.setCompanyId(selectedCompany.getId());
					inventoryId.setProductId(pd.getProduct().getId());
					inventoryId.setCustomised(pd.getCustomised());

					for (Iterator<PurchaseDetail> iterator = oldPurchaseDetails
							.iterator(); iterator.hasNext();) {
						PurchaseDetail oldPd = iterator.next();
						InventoryId oldInventoryId = new InventoryId();
						oldInventoryId.setCompanyId(selectedCompany.getId());
						oldInventoryId.setProductId(oldPd.getProduct().getId());
						oldInventoryId.setCustomised(oldPd.getCustomised());

						if (oldInventoryId.equals(inventoryId)) {
							inventory.setQuantity(pd.getQuantity()
									+ oldPd.getQuantity());
							iterator.remove();
						}
					}
					inventory.setId(inventoryId);
					inventoriesToSave.add(inventory);
					setInventory(new Inventory());
					amount = amount.add(pd.getRate().multiply(
							new BigDecimal(pd.getQuantity())));

					String remark = pd.getRemarks();
					if (remark == null || remark.isEmpty() || remark == "")
						pd.setRemarks(null);
				}
				for (PurchaseDetail oldPd : oldPurchaseDetails) {
					InventoryId oldInventoryId = new InventoryId();
					oldInventoryId.setCompanyId(selectedCompany.getId());
					oldInventoryId.setProductId(oldPd.getProduct().getId());
					oldInventoryId.setCustomised(oldPd.getCustomised());

					inventory.setQuantity(oldPd.getQuantity());
					inventory.setId(oldInventoryId);
					inventoriesToSave.add(inventory);
					setInventory(new Inventory());
				}

				this.newPurchase.setAmount(amount);			
				this.newPurchase.setUpdated(new Date());
				this.newPurchase.setEmployeeByUpdatedBy(emp);
				this.newPurchase.setStatus("UPDATE");

				// Save Purchase Updates
				result = dao.savePurchase(newPurchase, inventoriesToSave, page);
				setPurchases(null);
			} else if ("purchase_return".equalsIgnoreCase(page)) {
				List<PurchaseDetail> returnPurchaseDetails = new ArrayList<PurchaseDetail>();
				for (PurchaseDetail pd : purchaseDetails) {
					if(pd.getRet_quantity()==0)
						continue;
					returnPurchaseDetails.add(pd);
					pd.setQuantity(pd.getRet_quantity());

					// create purchase detail items
					pd.setAmount(pd.getRate().multiply(
							new BigDecimal(pd.getQuantity())));

					// create inventory items
					InventoryId inventoryId = new InventoryId();
					inventoryId.setCompanyId(selectedCompany.getId());
					inventoryId.setProductId(pd.getProduct().getId());
					inventoryId.setCustomised(pd.getCustomised());

					inventory.setQuantity(pd.getQuantity() * -1);
					inventory.setId(inventoryId);
					inventoriesToSave.add(inventory);
					setInventory(new Inventory());
					amount = amount.add(pd.getRate().multiply(
							new BigDecimal(pd.getQuantity())));

					pd.setReturned(true);
				}

				this.newPurchase.setAmount(amount);
				
				// this.newPurchase.setEmployeeByEmployeeId(emp);
				this.newPurchase.setStatus("RETURN");

				// Save Purchase Returns
				newPurchase.setPurchaseDetails(returnPurchaseDetails);
				result = dao.savePurchase(newPurchase, inventoriesToSave, page);
			}

		} catch (Exception e) {
			String res = null;
			if (e.getMessage().equalsIgnoreCase("In-Sufficient Inventory"))
				res = "Purchase Update Failed : In-sufficient Inventory";
			if (res != null)
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
								result));
			else
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
								" Error Saving Purchase"));
			return;
		}
		setPurchaseDetails(new ArrayList<PurchaseDetail>());
		setSelectedDealer(new Dealer());
		inventories.clear();
		setPurchases(null);
		
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", result));

	}

	public void deletePurchaseDetails() {

		try {
			if (getSelectedPurchaseDetails() == null
					|| (getSelectedPurchaseDetails() != null && getSelectedPurchaseDetails().length == 0)) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
								"Nothing to Delete"));
				return;
			}
			for (PurchaseDetail purchaseDetail : getSelectedPurchaseDetails()) {
				purchaseDetails.remove(purchaseDetail);

				InventoryId newInventoryId = new InventoryId();
				newInventoryId.setCompanyId(selectedCompany.getId());
				newInventoryId
						.setProductId(purchaseDetail.getProduct().getId());
				newInventoryId.setCustomised(purchaseDetail.getCustomised());

				inventories.remove(newInventoryId);
			}
			setSelectedPurchaseDetails(null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							"Error Deleting Purchase Details"));
		}
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
						"Purchase Details deleted successfully"));
	}

}
