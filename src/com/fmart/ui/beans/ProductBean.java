package com.fmart.ui.beans;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.UploadedFile;

import com.fmart.hibernate.dao.ExpenseDao;
import com.fmart.hibernate.dao.ProductDao;
import com.fmart.hibernate.pojos.Employee;
import com.fmart.hibernate.pojos.Expense;
import com.fmart.hibernate.pojos.LazySorter;
import com.fmart.hibernate.pojos.Product;
import com.fmart.hibernate.pojos.ProductGrp;
import com.fmart.ui.utils.ImageUtils;
import com.fmart.ui.utils.SessionInfo;

/**
 * 
 * @author Manju
 */
@ManagedBean
@ViewScoped
public class ProductBean implements Serializable {

	/**
	 * 
	 */

	private Image image;

	private static final long serialVersionUID = 1L;
	Product newProduct = new Product();
	Product selectedProduct = new Product();
	private Product[] selectedProducts;
	private LazyDataModel<Product> products = null;

	public Product getSelectedProduct() {
		return selectedProduct;
	}

	public void setSelectedProduct(Product selectedProduct) {
		this.selectedProduct = selectedProduct;
	}

	public LazyDataModel<Product> getProducts() {
		if (products == null) {
			products = new LazyDataModel<Product>() {

				private static final long serialVersionUID = 1L;

				@Override
				public List<Product> load(int first, int pageSize,
						String sortField, SortOrder sortOrder,
						Map<String, Object> filters) {
					ProductDao dao = new ProductDao();
					Map<String, String> properties = new HashMap<String, String>();
					properties.put("id", "id");
					properties.put("name", "name");
					properties.put("unitPrice", "unitPrice");
					properties.put("productGrp", "productGrp");
					List<String> fetchMode = new ArrayList<String>();
					fetchMode.add("productGrp");
					List<Product> productList = dao.findByCriteria(properties,first, pageSize);
					// sort
					if (sortField != null) {
						Collections.sort(productList, new LazySorter(
								sortField, sortOrder));
					}

					// rowCount
					this.setRowCount(dao.countByProperty());
					return productList;
				}
			};

		}
		return this.products;
	}

	public void setProducts(LazyDataModel<Product> Products) {
		this.products = Products;
	}

	public Product[] getSelectedProducts() {
		return selectedProducts;
	}

	public void setSelectedProducts(Product[] selectedProducts) {
		this.selectedProducts = selectedProducts;
	}

	public Product getNewProduct() {
		return newProduct;
	}

	public void setNewProduct(Product newProduct) {
		this.newProduct = newProduct;
	}

	public void saveProduct(ActionEvent actionEvent) {
		ProductDao dao = new ProductDao();

		try {
			if (image != null) {
				if (image.getFilename() != null)
					newProduct.setImage(newProduct.getName()+".jpg");
				if (image.getImagesrc() != null)
					newProduct.setImagesrc(image.getImagesrc());
			}
			SessionInfo sessionInfo = UserSession.getSession();
			newProduct.setCreated(new Date());
			newProduct.setUpdated(new Date());			
			newProduct.setEmployeeByCreatedBy(new Employee(sessionInfo.getEmployeeId()));
			newProduct.setEmployeeByUpdatedBy(new Employee(sessionInfo.getEmployeeId()));
			dao.save(newProduct);
			setNewProduct(new Product());
			image = new Image();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!",
							"Error Saving Product"));
			return;
		}
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
						"Product  Saved successfully"));

	}

	public void updateProduct(ActionEvent actionEvent) {

		try {
			if (image != null) {
				if (image.getFilename() != null)
					selectedProduct.setImage(selectedProduct.getName()+".jpg");
				if (image.getImagesrc() != null)
					selectedProduct.setImagesrc(image.getImagesrc());
			}

			ProductDao dao = new ProductDao();
			SessionInfo sessionInfo = UserSession.getSession();				
			selectedProduct.setUpdated(new Date());			
			selectedProduct.setEmployeeByUpdatedBy(new Employee(sessionInfo.getEmployeeId()));
			selectedProduct.setCreated(new Date());			
			selectedProduct.setEmployeeByCreatedBy(new Employee(sessionInfo.getEmployeeId()));
			dao.update(selectedProduct, selectedProduct.getId());
			setSelectedProduct(new Product());
			image = new Image();
			setProducts(null);
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
						"Product  Updated successfully"));
	}

	private static final int BUFFER_SIZE = 6124;

	public void handleFileUpload(FileUploadEvent event) {

		UploadedFile file = event.getFile();
		InputStream is = null;
		try {
			long size = file.getSize();
			if (size < 0 || size > 554288) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"File not attached or Size is more than 500kb",
								file.getFileName()));
			}
			image = new Image();
			image.setFilename(file.getFileName());
			image.setFileSize(file.getSize());

			is = file.getInputstream();
			byte[] bytes = IOUtils.toByteArray(is);
			bytes = ImageUtils.resizeImage(bytes);
			image.setImagesrc(bytes);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Error Uploading", file.getFileName()));

		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", file
						.getFileName() + "  Uploaded Succesfully"));
	}

	public void doBefore() {
		// check if it is jpef file
		System.out.println("do before");
	}

}
