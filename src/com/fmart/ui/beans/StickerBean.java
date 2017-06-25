package com.fmart.ui.beans;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;

import com.fmart.hibernate.dao.PurchaseDao;
import com.fmart.hibernate.pojos.Product;
import com.fmart.hibernate.pojos.ProductGrp;

/**
 * 
 * @author jay
 */
@ManagedBean
@ViewScoped
public class StickerBean implements Serializable {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	private Sticker newSticker = new Sticker();
	private List<Sticker> stickerDetails = new ArrayList<Sticker>();
	private List<Sticker> selectedStickers = new ArrayList<Sticker>();
	private List<ProductGrp> productGrps = new ArrayList<ProductGrp>();
	private List<Product> products = new ArrayList<Product>();
	private Product selectedProduct = new Product();
	private ProductGrp selectedProductGroup = new ProductGrp();
	private List<Sticker> saveStickers = new ArrayList<Sticker>();

	public Sticker getNewSticker() {
		return newSticker;
	}

	public void setNewSticker(Sticker newSticker) {
		this.newSticker = newSticker;
	}

	public List<Sticker> getStickerDetails() {
		return stickerDetails;
	}

	public void setStickerDetails(List<Sticker> stickerDetails) {
		this.stickerDetails = stickerDetails;
	}

	public List<Sticker> getSelectedStickers() {
		return selectedStickers;
	}

	public void setSelectedStickers(List<Sticker> selectedStickers) {
		this.selectedStickers = selectedStickers;
	}

	public List<ProductGrp> getProductGrps() {
		return productGrps;
	}

	public void setProductGrps(List<ProductGrp> productGrps) {
		this.productGrps = productGrps;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
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
				"product_grp_id="
						+ selectedProductGroup.getId());
		return products;
	}

	public Product getSelectedProduct() {
		return selectedProduct;
	}

	public void setSelectedProduct(Product selectedProduct) {
		this.selectedProduct = selectedProduct;
	}

	public ProductGrp getSelectedProductGroup() {
		return selectedProductGroup;
	}

	public void setSelectedProductGroup(ProductGrp selectedProductGroup) {
		this.selectedProductGroup = selectedProductGroup;
		selectedProduct = null;
	}
	
	public List<Sticker> getSaveStickers() {
		return saveStickers;
	}

	public void setSaveStickers(List<Sticker> saveStickers) {
		this.saveStickers = saveStickers;
	}

	public void addSticker(ActionEvent actionEvent) {
		if (selectedStickers == null)
			selectedStickers = new ArrayList<Sticker>();

		newSticker.setProductGrpName(selectedProductGroup.getName());
		newSticker.setProductName(selectedProduct.getName());
		
		newSticker.setProduct(selectedProduct);
		stickerDetails.add(newSticker);
		setNewSticker(new Sticker());
		setSelectedProduct(new Product());
		setSelectedProductGroup(new ProductGrp());
		
	}

	public void deleteStickerDetails() {

		try {
			if (getSelectedStickers() == null
					|| (getSelectedStickers() != null && getSelectedStickers()
							.size() == 0)) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
								"Nothing to Delete"));
				return;
			}
			for (Sticker s : getSelectedStickers())
				stickerDetails.remove(s);
			setSelectedStickers(null);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							"Error Deleting Sticker Details"));
		}
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
						"Sticker Details deleted successfully"));
	}

	public void clearStickerDetails(ActionEvent actionEvent) {
		this.stickerDetails.clear();
		this.newSticker = new Sticker();
	}
	
	public void saveStickersInfo(ActionEvent actionEvent){
		int count=1;
		for (Sticker sticker : stickerDetails) {
			for(int i=0;i<sticker.getQuantity();i++){
				Sticker newSticker=new Sticker();
				newSticker.setProductGrpName(sticker.getProductGrpName());
				newSticker.setProductName(sticker.getProductName());
				newSticker.setQuantity(count++);
				saveStickers.add(newSticker);
			}			
		}
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    ec.getFlash().put("stickers", saveStickers);
	}
}
