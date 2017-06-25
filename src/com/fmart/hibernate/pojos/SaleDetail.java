package com.fmart.hibernate.pojos;

// Generated Mar 19, 2015 11:56:59 PM by Hibernate Tools 3.4.0.CR1

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;

import com.fmart.framework.hibernate.FMartGenericDAO;
import com.fmart.hibernate.dao.SaleDao;
import com.fmart.ui.beans.UserSession;

/**
 * SaleDetail generated by hbm2java
 */
@Entity
@Table(name = "sale_detail", schema = "fmart")
public class SaleDetail  implements java.io.Serializable {

	private int id;
	private Sale sale;
	private Product product;
	private Integer quantity;
	private Integer dummyQuantity;
	private Integer dispatchedQuantity;
	private BigDecimal amount;
	private Boolean returned;
	private String remarks;
	private BigDecimal rate;
	private Boolean customised;
	private Boolean dispatched;

	public SaleDetail() {
		
		this.created= new Date();
	}

	public SaleDetail(int id) {
		this.id = id;
	}

	public SaleDetail(int id, Sale sale, Product product, Integer quantity,
			BigDecimal amount, Boolean returned,Boolean customised,Boolean dispatched, String remarks,
			BigDecimal rate) {
		this.id = id;
		this.sale = sale;
		this.product = product;
		this.quantity = quantity;
		this.amount = amount;
		this.returned = returned;
		this.remarks = remarks;
		this.rate = rate;
		this.customised = customised;
		this.dispatched = dispatched;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@JsonBackReference@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sale_id")
	public Sale getSale() {
		return this.sale;
	}

	public void setSale(Sale sale) {
		this.sale = sale;
	}

	@JsonBackReference@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
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
	
	@Transient
	@XmlTransient
	@JsonIgnore
	public Integer getDummyQuantity() {
		return dummyQuantity;
	}

	public void setDummyQuantity(Integer dummyQuantity) {
		this.dummyQuantity = dummyQuantity;
	}
	
	@Transient
	@XmlTransient
	@JsonIgnore
	public Integer getDispatchedQuantity() {
		return dispatchedQuantity;
	}

	public void setDispatchedQuantity(Integer dispatchedQuantity) {
		this.dispatchedQuantity = dispatchedQuantity;
	}

	@Column(name = "amount", precision = 10)
	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Column(name = "returned")
	public Boolean getReturned() {
		return this.returned;
	}

	public void setReturned(Boolean returned) {
		this.returned = returned;
	}

	@Column(name = "remarks", length = 50)
	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	
	@Column(name = "customised")
	public Boolean getCustomised() {
		if(this.customised==null || !this.customised)
			return false;
		return this.customised;
	}

	public void setCustomised(Boolean customised) {
		this.customised = customised;
	}
	
	@Column(name = "dispatched")
	public Boolean getDispatched() {
		if(this.dispatched==null || !this.dispatched)
			return false;
		return this.dispatched;
	}

	public void setDispatched(Boolean dispatched) {
		this.dispatched = dispatched;
	}

	@Column(name = "rate", precision = 10)
	public BigDecimal getRate() {
		return this.rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	
	public void queryDispatchedQuantity(){
		SaleDao dao = new SaleDao();
		dispatchedQuantity=dao.queryDispatchedQuantity(sale.getId(),UserSession.getSession().getCompanyId(),product.getId(),customised);
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
