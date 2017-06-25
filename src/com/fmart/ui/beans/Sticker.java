package com.fmart.ui.beans;

import java.util.Random;

import com.fmart.hibernate.pojos.Product;

public class Sticker{
	int id;
	Product product;
	String productGrpName;
	String productName;
	int quantity;
	
	public Sticker(){
		Random randomGenerator = new Random();
		id=randomGenerator.nextInt();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	public String getProductGrpName() {
		return productGrpName;
	}
	
	public void setProductGrpName(String productGrpName) {
		this.productGrpName=productGrpName;
	}
	
	public String getProductName() {
		return productName;
	}
	
	public void setProductName(String productName) {
		this.productName=productName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
