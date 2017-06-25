package com.fmart.ui.beans;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.model.LazyDataModel;

import com.fmart.hibernate.dao.GodownDao;
import com.fmart.hibernate.dao.GodownInventoryDao;
import com.fmart.hibernate.dao.SaleDao;
import com.fmart.hibernate.pojos.Company;
import com.fmart.hibernate.pojos.Employee;
import com.fmart.hibernate.pojos.Godown;
import com.fmart.hibernate.pojos.GodownInventory;
import com.fmart.hibernate.pojos.GodownInventoryId;
import com.fmart.hibernate.pojos.InventoryId;
import com.fmart.hibernate.pojos.Product;
import com.fmart.hibernate.pojos.ProductGrp;
import com.fmart.hibernate.pojos.Sale;
import com.fmart.hibernate.pojos.SaleDetail;
import com.fmart.ui.utils.SessionInfo;

/**
 * 
 * @author Manju
 */
@ManagedBean
@ViewScoped
public class GodownBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Godown newGodown = new Godown();
	Godown selectedGodown = new Godown();
	private Godown[] selectedGodowns;
	private List<Godown> godowns = null;
	String transaction = null;
	private Company selectedCompany = new Company();
	Date date = new Date();
	
	private List<GodownInventory> godownInventories = new ArrayList<GodownInventory>();
	private GodownInventory newGodownInventory = new GodownInventory();
	private Set<GodownInventoryId> inventories = new HashSet<GodownInventoryId>();
	private Product selectedProduct = new Product();
	private ProductGrp selectedProductGroup = new ProductGrp();
	// for delete product
	private GodownInventory[] selectedGodownInventories;
	private String action;
	private int selectedGodownId;
	
	private Godown printGodown = new Godown();
	private List<GodownInventory> printInvetory = new ArrayList<GodownInventory>(); 
	private Date printDate = new Date();
	
	
	public Date getPrintDate() {
		return printDate;
	}

	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}

	public Godown getPrintGodown() {
		return printGodown;
	}

	public void setPrintGodown(Godown printGodown) {
		this.printGodown = printGodown;
	}

	public List<GodownInventory> getPrintInvetory() {
		 ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		 printInvetory = (List<GodownInventory>) ec.getFlash().get("printInvetory"); 
		 setPrintGodown((Godown) ec.getFlash().get("printGodown"));
		return printInvetory;
	}

	public void setPrintInvetory(List<GodownInventory> printInvetory) {
		this.printInvetory = printInvetory;
	}

	public int getSelectedGodownId() {
		return selectedGodownId;
	}

	public void setSelectedGodownId(int selectedGodownId) {
		this.selectedGodownId = selectedGodownId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	private List<Product> products = new ArrayList<Product>();
	private List<ProductGrp> productGrps = new ArrayList<ProductGrp>();

	
	
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

	public GodownInventory[] getSelectedGodownInventories() {
		return selectedGodownInventories;
	}

	public void setSelectedGodownInventories(
			GodownInventory[] selectedGodownInventories) {
		this.selectedGodownInventories = selectedGodownInventories;
	}

	public ProductGrp getSelectedProductGroup() {
		return selectedProductGroup;
	}

	public void setSelectedProductGroup(ProductGrp selectedProductGrp) {
		this.selectedProductGroup = selectedProductGrp;
	}

	public GodownInventory getNewGodownInventory() {
		return newGodownInventory;
	}

	public void setNewGodownInventory(GodownInventory newGodownInventory) {
		this.newGodownInventory = newGodownInventory;
	}

	public Product getSelectedProduct() {
		return selectedProduct;
	}

	public void setSelectedProduct(Product selectedProduct) {
		this.selectedProduct = selectedProduct;
	}

	public List<GodownInventory> getGodownInventories() {
		return godownInventories;
	}

	public void setGodownInventories(List<GodownInventory> godownInventories) {
		this.godownInventories = godownInventories;
	}

	public Set<GodownInventoryId> getInventories() {
		return inventories;
	}

	public void setInventories(Set<GodownInventoryId> inventories) {
		this.inventories = inventories;
	}

	public String getTransaction() {
		return transaction;
	}

	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}

	public Company getSelectedCompany() {
		return selectedCompany;
	}

	public void setSelectedCompany(Company selectedCompany) {
		this.selectedCompany = selectedCompany;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Godown getSelectedGodown() {
		return selectedGodown;
	}

	public void setSelectedGodown(Godown selectedGodown) {
		this.selectedGodown = selectedGodown;
	}

	public List<Godown> getGodowns() {
		if (godowns == null) {
			GodownDao dao = new GodownDao();
			this.godowns = dao.findAll();
		}
		return godowns;
	}

	public void setGodowns(List<Godown> godowns) {
		this.godowns = godowns;
	}

	public Godown[] getSelectedGodowns() {
		return selectedGodowns;
	}

	public void setSelectedGodowns(Godown[] selectedGodowns) {
		this.selectedGodowns = selectedGodowns;
	}

	public Godown getNewGodown() {
		return newGodown;
	}

	public void setNewGodown(Godown newGodown) {
		this.newGodown = newGodown;
	}

	// @PostConstruct
	public List<Godown> populateGodownList() {
		GodownDao dao = new GodownDao();
		this.godowns = dao.findAll();
		return this.godowns;
	}

	public void saveGodown(ActionEvent actionEvent) {
		GodownDao dao = new GodownDao();
		try {
			SessionInfo sessionInfo = UserSession.getSession();
			newGodown.setCreated(new Date());
			newGodown.setUpdated(new Date());
			newGodown.setEmployeeByCreatedBy(new Employee(sessionInfo
					.getEmployeeId()));
			newGodown.setEmployeeByUpdatedBy(new Employee(sessionInfo
					.getEmployeeId()));
			dao.save(newGodown);
			setNewGodown(new Godown());
			setGodowns(null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							"Error Saving Godown "));
			return;
		}
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
						"Godown Saved successfully"));

	}

	public void updateGodown(ActionEvent actionEvent) {
		try {
			GodownDao dao = new GodownDao();
			SessionInfo sessionInfo = UserSession.getSession();
			getSelectedGodown().setEmployeeByUpdatedBy(
					new Employee(sessionInfo.getEmployeeId()));
			getSelectedGodown().setUpdated(new Date());
			dao.update(getSelectedGodown(), getSelectedGodown().getId());
			setNewGodown(new Godown());
			setGodowns(null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							"Error Updating Product Group"));
			return;
		}
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
						"Product Group Updated successfully"));
	}

	public void addGodownInventory(ActionEvent actionEvent) {
		GodownInventoryId newInventoryId = new GodownInventoryId();
		newInventoryId.setGodownId(selectedGodownId);
		newInventoryId.setProductId(selectedProduct.getId());
		newInventoryId.setCustomised(newGodownInventory.getId().isCustomised());

		if (!inventories.contains(newInventoryId)) {
			newGodownInventory.setProduct(selectedProduct);
			godownInventories.add(newGodownInventory);
			inventories.add(newInventoryId);
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							"Product already exist in the list"));
		}
		setNewGodownInventory(new GodownInventory());
		setSelectedProduct(new Product());
		setSelectedProductGroup(new ProductGrp());
	}

	public void deleteGodownInventories() {
		try {
			if (getSelectedGodownInventories() == null
					|| (getSelectedGodownInventories() != null && getSelectedGodownInventories().length == 0)) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
								"Nothing to Delete"));
				return;
			}
			for (GodownInventory gi : getSelectedGodownInventories()) {
				godownInventories.remove(gi);

				GodownInventoryId newInventoryId = new GodownInventoryId();
				newInventoryId.setGodownId(selectedGodown.getId());
				newInventoryId.setProductId(gi.getProduct().getId());
				newInventoryId.setCustomised(gi.getId().isCustomised());

				inventories.remove(newInventoryId);
			}
			setSelectedGodownInventories(null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							"Error Deleting Inventory Details"));
		}
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
						"Inventory Details deleted successfully"));

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
	public void clearSaleDetails(ActionEvent actionEvent) {
		this.godownInventories = new ArrayList<GodownInventory>();
	}
	
	public void saveGodownEntry(ActionEvent actionEvent) {
		List<GodownInventory> test = godownInventories;
		String action = this.action;
		GodownInventoryDao dao = new GodownInventoryDao();
		try {
			dao.saveInventory(test, action,selectedGodownId);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
							"Inventory Details added successfully"));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							"Error!",
							"Error Updating Inventory"));
		}finally{
			godownInventories.clear();
			action = null;
			selectedGodownId = 0;
			inventories.clear();
		}
		
		
	}
	
	public void printInventory(int godownId){
		System.out.println(godownId);
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		GodownInventoryDao inventoryDao = new GodownInventoryDao();
		GodownDao godownDao = new GodownDao();
	    ec.getFlash().put("printGodown", godownDao.findById(godownId));
	    ec.getFlash().put("printInvetory", inventoryDao.getInventoryForGodown(godownId));
	   
	    
		try {
			FacesContext.getCurrentInstance()
			   .getExternalContext().redirect("godown_print.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
 
}
