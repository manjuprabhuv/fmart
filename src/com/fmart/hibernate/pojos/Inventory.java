package com.fmart.hibernate.pojos;

// Generated Mar 19, 2015 11:56:59 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonManagedReference;
/**
 * Inventory generated by hbm2java
 */
@Entity
@Table(name = "inventory", schema = "fmart")
public class Inventory implements java.io.Serializable {

	private InventoryId id;
	private Company company;
	private Product product;
	private Integer quantity;

	public Inventory() {
	}

	public Inventory(InventoryId id, Company company, Product product) {
		this.id = id;
		this.company = company;
		this.product = product;
	}

	public Inventory(InventoryId id, Company company, Product product,
			Integer quantity) {
		this.id = id;
		this.company = company;
		this.product = product;
		this.quantity = quantity;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "productId", column = @Column(name = "product_id", nullable = false)),
			@AttributeOverride(name = "companyId", column = @Column(name = "company_id", nullable = false)),
			@AttributeOverride(name = "customised", column = @Column(name = "customised", nullable = false)) })
	public InventoryId getId() {
		return this.id;
	}

	public void setId(InventoryId id) {
		this.id = id;
	}

	 @JsonBackReference@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id", nullable = false, insertable = false, updatable = false)
	public Company getCompany() {
		return this.company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	 @JsonBackReference@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false, insertable = false, updatable = false)
	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Column(name = "quantity")
	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	private Employee employeeByUpdatedBy;
	private Employee employeeByCreatedBy;
	private Date created;
	private Date updated;

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by")
	public Employee getEmployeeByCreatedBy() {
		return this.employeeByCreatedBy;
	}

	public void setEmployeeByCreatedBy(Employee employeeByCreatedBy) {
		this.employeeByCreatedBy = employeeByCreatedBy;
	}

	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "updated_by")
	public Employee getEmployeeByUpdatedBy() {
		return this.employeeByUpdatedBy;
	}

	public void setEmployeeByUpdatedBy(Employee employeeByUpdatedBy) {
		this.employeeByUpdatedBy = employeeByUpdatedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created", length = 35)
	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated", length = 35)
	public Date getUpdated() {
		return this.updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

}
