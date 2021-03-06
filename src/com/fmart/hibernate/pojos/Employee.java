package com.fmart.hibernate.pojos;

// Generated Apr 28, 2015 3:08:12 PM by Hibernate Tools 3.4.0.CR1

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonManagedReference;

import com.fmart.ui.utils.SecurityUtils;

/**
 * Employee generated by hbm2java
 */
@Entity
@Table(name = "employee", schema = "fmart", uniqueConstraints = {
		@UniqueConstraint(columnNames = "username"),
		@UniqueConstraint(columnNames = "mobile") })
public class Employee implements java.io.Serializable {

	private int id;
	private boolean admin;
	private Company company = new Company();
	private String name;
	private String address;
	private String city;
	private String state;
	private Date startDate;
	private String email;
	private Long mobile;
	private String designation;
	private String password;
	private String username;
	private BigDecimal salePercent;
	private Boolean active;
	private Set<Payment> paymentsForUpdatedBy = new HashSet<Payment>(0);
	private Set<Purchase> purchasesForUpdatedBy = new HashSet<Purchase>(0);
	private Set<Inventory> inventoriesForCreatedBy = new HashSet<Inventory>(0);
	private Set<Product> productsForUpdatedBy = new HashSet<Product>(0);
	private Set<SaleDetail> saleDetailsForUpdatedBy = new HashSet<SaleDetail>(0);
	private Set<SaleDetail> saleDetailsForCreatedBy = new HashSet<SaleDetail>(0);
	private Set<ExpenseType> expenseTypesForCreatedBy = new HashSet<ExpenseType>(
			0);
	private Set<PurchaseDetail> purchaseDetailsForUpdatedBy = new HashSet<PurchaseDetail>(
			0);
	private Set<Customer> customersForCreatedby = new HashSet<Customer>(0);
	private Set<Product> productsForCreatedBy = new HashSet<Product>(0);
	private Set<ProductGrp> productGrpsForUpdatedBy = new HashSet<ProductGrp>(0);
	private Set<ExpenseType> expenseTypesForUpdatedBy = new HashSet<ExpenseType>(
			0);
	private Set<Dealer> dealersForCreatedBy = new HashSet<Dealer>(0);
	private Set<Sale> salesForCreatedBy = new HashSet<Sale>(0);
	private Set<Sale> salesForUpdatedBy = new HashSet<Sale>(0);
	private Set<Customer> customersForUpdatedby = new HashSet<Customer>(0);
	private Set<Purchase> purchasesForCreatedBy = new HashSet<Purchase>(0);
	private Set<Receipt> receiptsForUpdatedBy = new HashSet<Receipt>(0);
	private Set<Expense> expensesForUpdatedBy = new HashSet<Expense>(0);
	private Set<Inventory> inventoriesForUpdatedBy = new HashSet<Inventory>(0);
	private Set<Dealer> dealersForUpdatedBy = new HashSet<Dealer>(0);
	private Set<ProductGrp> productGrpsForCreatedBy = new HashSet<ProductGrp>(0);
	private Set<Receipt> receiptsForCreatedBy = new HashSet<Receipt>(0);
	private Set<PurchaseDetail> purchaseDetailsForCreatedBy = new HashSet<PurchaseDetail>(
			0);
	private Set<Payment> paymentsForCreatedBy = new HashSet<Payment>(0);
	private Set<Expense> expensesForEmployeeId = new HashSet<Expense>(0);
	private Set<Expense> expensesForCreatedBy = new HashSet<Expense>(0);
	private List<EmpRole> empRoles = new ArrayList<EmpRole>(0);
	private List<EmpReport> empReports = new ArrayList<EmpReport>(0);
	private Set<Godown> godownsForCreatedBy = new HashSet<Godown>(0);
	private Set<Godown> godownsForUpdatedBy = new HashSet<Godown>(0);
	public Employee() {
	}

	public Employee(int id) {
		this.id = id;
	}

	public Employee(int id, Company company, String name, String address,
			String city, String state, Date startDate, String email,
			Long mobile, String designation, String password, String username,
			BigDecimal salePercent, Boolean active,
			Set<Payment> paymentsForUpdatedBy,
			Set<Purchase> purchasesForUpdatedBy,
			Set<Inventory> inventoriesForCreatedBy,
			Set<Product> productsForUpdatedBy,
			Set<SaleDetail> saleDetailsForUpdatedBy,
			Set<SaleDetail> saleDetailsForCreatedBy,
			Set<ExpenseType> expenseTypesForCreatedBy,
			Set<PurchaseDetail> purchaseDetailsForUpdatedBy,
			Set<Customer> customersForCreatedby,
			Set<Product> productsForCreatedBy,
			Set<ProductGrp> productGrpsForUpdatedBy,
			Set<ExpenseType> expenseTypesForUpdatedBy,
			Set<Dealer> dealersForCreatedBy, Set<Sale> salesForCreatedBy,
			Set<Sale> salesForUpdatedBy, Set<Customer> customersForUpdatedby,
			Set<Purchase> purchasesForCreatedBy,
			Set<Receipt> receiptsForUpdatedBy,
			Set<Expense> expensesForUpdatedBy,
			Set<Inventory> inventoriesForUpdatedBy,
			Set<Dealer> dealersForUpdatedBy,
			Set<ProductGrp> productGrpsForCreatedBy,
			Set<Receipt> receiptsForCreatedBy,
			Set<PurchaseDetail> purchaseDetailsForCreatedBy,
			Set<Payment> paymentsForCreatedBy,
			Set<Expense> expensesForEmployeeId,
			Set<Expense> expensesForCreatedBy, List<EmpRole> empRoles,
			List<EmpReport> empReports) {
		this.id = id;
		this.company = company;
		this.name = name;
		this.address = address;
		this.city = city;
		this.state = state;
		this.startDate = startDate;
		this.email = email;
		this.mobile = mobile;
		this.designation = designation;
		this.password = password;
		this.username = username;
		this.salePercent = salePercent;
		this.active = active;
		this.paymentsForUpdatedBy = paymentsForUpdatedBy;
		this.purchasesForUpdatedBy = purchasesForUpdatedBy;
		this.inventoriesForCreatedBy = inventoriesForCreatedBy;
		this.productsForUpdatedBy = productsForUpdatedBy;
		this.saleDetailsForUpdatedBy = saleDetailsForUpdatedBy;
		this.saleDetailsForCreatedBy = saleDetailsForCreatedBy;
		this.expenseTypesForCreatedBy = expenseTypesForCreatedBy;
		this.purchaseDetailsForUpdatedBy = purchaseDetailsForUpdatedBy;
		this.customersForCreatedby = customersForCreatedby;
		this.productsForCreatedBy = productsForCreatedBy;
		this.productGrpsForUpdatedBy = productGrpsForUpdatedBy;
		this.expenseTypesForUpdatedBy = expenseTypesForUpdatedBy;
		this.dealersForCreatedBy = dealersForCreatedBy;
		this.salesForCreatedBy = salesForCreatedBy;
		this.salesForUpdatedBy = salesForUpdatedBy;
		this.customersForUpdatedby = customersForUpdatedby;
		this.purchasesForCreatedBy = purchasesForCreatedBy;
		this.receiptsForUpdatedBy = receiptsForUpdatedBy;
		this.expensesForUpdatedBy = expensesForUpdatedBy;
		this.inventoriesForUpdatedBy = inventoriesForUpdatedBy;
		this.dealersForUpdatedBy = dealersForUpdatedBy;
		this.productGrpsForCreatedBy = productGrpsForCreatedBy;
		this.receiptsForCreatedBy = receiptsForCreatedBy;
		this.purchaseDetailsForCreatedBy = purchaseDetailsForCreatedBy;
		this.paymentsForCreatedBy = paymentsForCreatedBy;
		this.expensesForEmployeeId = expensesForEmployeeId;
		this.expensesForCreatedBy = expensesForCreatedBy;
		this.empRoles = empRoles;
		this.empReports = empReports;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Transient
	@XmlTransient
	@JsonIgnore
	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	@JsonBackReference
	 @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	public Company getCompany() {
		return this.company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	@Column(name = "name", length = 30)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "address", length = 50)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "city", length = 20)
	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "state", length = 20)
	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "start_date", length = 13)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name = "email", length = 30)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "mobile", unique = true, precision = 10, scale = 0)
	public Long getMobile() {
		return this.mobile;
	}

	public void setMobile(Long mobile) {
		this.mobile = mobile;
	}

	@Column(name = "designation", length = 15)
	public String getDesignation() {
		return this.designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	@XmlTransient
	@JsonIgnore
	@Column(name = "password", length = 250)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		try {
			if (password.length() > 15) {
				this.password = password;
			} else {
				this.password = SecurityUtils.encode(password);
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Column(name = "username", unique = true, length = 15)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "sale_percent", precision = 4)
	public BigDecimal getSalePercent() {
		return this.salePercent;
	}

	public void setSalePercent(BigDecimal salePercent) {
		this.salePercent = salePercent;
	}

	@Column(name = "active")
	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	@XmlTransient
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employeeByUpdatedBy")
	public Set<Payment> getPaymentsForUpdatedBy() {
		return this.paymentsForUpdatedBy;
	}

	public void setPaymentsForUpdatedBy(Set<Payment> paymentsForUpdatedBy) {
		this.paymentsForUpdatedBy = paymentsForUpdatedBy;
	}
	@XmlTransient
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employeeByUpdatedBy")
	public Set<Purchase> getPurchasesForUpdatedBy() {
		return this.purchasesForUpdatedBy;
	}

	public void setPurchasesForUpdatedBy(Set<Purchase> purchasesForUpdatedBy) {
		this.purchasesForUpdatedBy = purchasesForUpdatedBy;
	}
	@XmlTransient
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employeeByCreatedBy")
	public Set<Inventory> getInventoriesForCreatedBy() {
		return this.inventoriesForCreatedBy;
	}

	public void setInventoriesForCreatedBy(
			Set<Inventory> inventoriesForCreatedBy) {
		this.inventoriesForCreatedBy = inventoriesForCreatedBy;
	}

	@XmlTransient
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employeeByUpdatedBy")
	public Set<Product> getProductsForUpdatedBy() {
		return this.productsForUpdatedBy;
	}

	public void setProductsForUpdatedBy(Set<Product> productsForUpdatedBy) {
		this.productsForUpdatedBy = productsForUpdatedBy;
	}
	@XmlTransient
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employeeByUpdatedBy")
	public Set<SaleDetail> getSaleDetailsForUpdatedBy() {
		return this.saleDetailsForUpdatedBy;
	}

	public void setSaleDetailsForUpdatedBy(
			Set<SaleDetail> saleDetailsForUpdatedBy) {
		this.saleDetailsForUpdatedBy = saleDetailsForUpdatedBy;
	}
	@XmlTransient
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employeeByCreatedBy")
	public Set<SaleDetail> getSaleDetailsForCreatedBy() {
		return this.saleDetailsForCreatedBy;
	}

	public void setSaleDetailsForCreatedBy(
			Set<SaleDetail> saleDetailsForCreatedBy) {
		this.saleDetailsForCreatedBy = saleDetailsForCreatedBy;
	}
	@XmlTransient
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employeeByCreatedBy")
	public Set<ExpenseType> getExpenseTypesForCreatedBy() {
		return this.expenseTypesForCreatedBy;
	}

	public void setExpenseTypesForCreatedBy(
			Set<ExpenseType> expenseTypesForCreatedBy) {
		this.expenseTypesForCreatedBy = expenseTypesForCreatedBy;
	}
	@XmlTransient
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employeeByUpdatedBy")
	public Set<PurchaseDetail> getPurchaseDetailsForUpdatedBy() {
		return this.purchaseDetailsForUpdatedBy;
	}

	public void setPurchaseDetailsForUpdatedBy(
			Set<PurchaseDetail> purchaseDetailsForUpdatedBy) {
		this.purchaseDetailsForUpdatedBy = purchaseDetailsForUpdatedBy;
	}
	@XmlTransient
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employeeByCreatedby")
	public Set<Customer> getCustomersForCreatedby() {
		return this.customersForCreatedby;
	}

	public void setCustomersForCreatedby(Set<Customer> customersForCreatedby) {
		this.customersForCreatedby = customersForCreatedby;
	}
	@XmlTransient
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employeeByCreatedBy")
	public Set<Product> getProductsForCreatedBy() {
		return this.productsForCreatedBy;
	}

	public void setProductsForCreatedBy(Set<Product> productsForCreatedBy) {
		this.productsForCreatedBy = productsForCreatedBy;
	}
	
	@XmlTransient
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employeeByUpdatedBy")
	public Set<ProductGrp> getProductGrpsForUpdatedBy() {
		return this.productGrpsForUpdatedBy;
	}

	public void setProductGrpsForUpdatedBy(
			Set<ProductGrp> productGrpsForUpdatedBy) {
		this.productGrpsForUpdatedBy = productGrpsForUpdatedBy;
	}
	@XmlTransient
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employeeByUpdatedBy")
	public Set<ExpenseType> getExpenseTypesForUpdatedBy() {
		return this.expenseTypesForUpdatedBy;
	}

	public void setExpenseTypesForUpdatedBy(
			Set<ExpenseType> expenseTypesForUpdatedBy) {
		this.expenseTypesForUpdatedBy = expenseTypesForUpdatedBy;
	}
	@XmlTransient
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employeeByCreatedBy")
	public Set<Dealer> getDealersForCreatedBy() {
		return this.dealersForCreatedBy;
	}

	public void setDealersForCreatedBy(Set<Dealer> dealersForCreatedBy) {
		this.dealersForCreatedBy = dealersForCreatedBy;
	}
	@XmlTransient
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employeeByCreatedBy")
	public Set<Sale> getSalesForCreatedBy() {
		return this.salesForCreatedBy;
	}

	public void setSalesForCreatedBy(Set<Sale> salesForCreatedBy) {
		this.salesForCreatedBy = salesForCreatedBy;
	}
	@XmlTransient
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employeeByUpdatedBy")
	public Set<Sale> getSalesForUpdatedBy() {
		return this.salesForUpdatedBy;
	}

	public void setSalesForUpdatedBy(Set<Sale> salesForUpdatedBy) {
		this.salesForUpdatedBy = salesForUpdatedBy;
	}
	@XmlTransient
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employeeByUpdatedby")
	public Set<Customer> getCustomersForUpdatedby() {
		return this.customersForUpdatedby;
	}

	public void setCustomersForUpdatedby(Set<Customer> customersForUpdatedby) {
		this.customersForUpdatedby = customersForUpdatedby;
	}
	@XmlTransient
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employeeByCreatedBy")
	public Set<Purchase> getPurchasesForCreatedBy() {
		return this.purchasesForCreatedBy;
	}

	public void setPurchasesForCreatedBy(Set<Purchase> purchasesForCreatedBy) {
		this.purchasesForCreatedBy = purchasesForCreatedBy;
	}
	@XmlTransient
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employeeByUpdatedBy")
	public Set<Receipt> getReceiptsForUpdatedBy() {
		return this.receiptsForUpdatedBy;
	}

	public void setReceiptsForUpdatedBy(Set<Receipt> receiptsForUpdatedBy) {
		this.receiptsForUpdatedBy = receiptsForUpdatedBy;
	}
	@XmlTransient
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employeeByUpdatedBy")
	public Set<Expense> getExpensesForUpdatedBy() {
		return this.expensesForUpdatedBy;
	}

	public void setExpensesForUpdatedBy(Set<Expense> expensesForUpdatedBy) {
		this.expensesForUpdatedBy = expensesForUpdatedBy;
	}
	@XmlTransient
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employeeByUpdatedBy")
	public Set<Inventory> getInventoriesForUpdatedBy() {
		return this.inventoriesForUpdatedBy;
	}

	public void setInventoriesForUpdatedBy(
			Set<Inventory> inventoriesForUpdatedBy) {
		this.inventoriesForUpdatedBy = inventoriesForUpdatedBy;
	}
	@XmlTransient
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employeeByUpdatedBy")
	public Set<Dealer> getDealersForUpdatedBy() {
		return this.dealersForUpdatedBy;
	}

	public void setDealersForUpdatedBy(Set<Dealer> dealersForUpdatedBy) {
		this.dealersForUpdatedBy = dealersForUpdatedBy;
	}
	@XmlTransient
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employeeByCreatedBy")
	public Set<ProductGrp> getProductGrpsForCreatedBy() {
		return this.productGrpsForCreatedBy;
	}

	public void setProductGrpsForCreatedBy(
			Set<ProductGrp> productGrpsForCreatedBy) {
		this.productGrpsForCreatedBy = productGrpsForCreatedBy;
	}
	@XmlTransient
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employeeByCreatedBy")
	public Set<Receipt> getReceiptsForCreatedBy() {
		return this.receiptsForCreatedBy;
	}

	public void setReceiptsForCreatedBy(Set<Receipt> receiptsForCreatedBy) {
		this.receiptsForCreatedBy = receiptsForCreatedBy;
	}
	@XmlTransient
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employeeByCreatedBy")
	public Set<PurchaseDetail> getPurchaseDetailsForCreatedBy() {
		return this.purchaseDetailsForCreatedBy;
	}

	public void setPurchaseDetailsForCreatedBy(
			Set<PurchaseDetail> purchaseDetailsForCreatedBy) {
		this.purchaseDetailsForCreatedBy = purchaseDetailsForCreatedBy;
	}
	@XmlTransient
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employeeByCreatedBy")
	public Set<Payment> getPaymentsForCreatedBy() {
		return this.paymentsForCreatedBy;
	}

	public void setPaymentsForCreatedBy(Set<Payment> paymentsForCreatedBy) {
		this.paymentsForCreatedBy = paymentsForCreatedBy;
	}
	@XmlTransient
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employeeByEmployeeId")
	public Set<Expense> getExpensesForEmployeeId() {
		return this.expensesForEmployeeId;
	}

	public void setExpensesForEmployeeId(Set<Expense> expensesForEmployeeId) {
		this.expensesForEmployeeId = expensesForEmployeeId;
	}
	@XmlTransient
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employeeByCreatedBy")
	public Set<Expense> getExpensesForCreatedBy() {
		return this.expensesForCreatedBy;
	}

	public void setExpensesForCreatedBy(Set<Expense> expensesForCreatedBy) {
		this.expensesForCreatedBy = expensesForCreatedBy;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
	public List<EmpRole> getEmpRoles() {
		return this.empRoles;
	}

	public void setEmpRoles(List<EmpRole> empRoles) {
		this.empRoles = empRoles;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
	public List<EmpReport> getEmpReports() {
		return this.empReports;
	}

	public void setEmpReports(List<EmpReport> empReports) {
		this.empReports = empReports;
	}
	
	@XmlTransient
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employeeByCreatedBy")
	public Set<Godown> getGodownsForCreatedBy() {
		return this.godownsForCreatedBy;
	}

	public void setGodownsForCreatedBy(Set<Godown> godownsForCreatedBy) {
		this.godownsForCreatedBy = godownsForCreatedBy;
	}

}
