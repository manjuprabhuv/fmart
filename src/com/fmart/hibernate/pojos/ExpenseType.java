package com.fmart.hibernate.pojos;

// Generated Mar 19, 2015 11:56:59 PM by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import java.util.HashSet;
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
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonManagedReference;
/**
 * ProductGrp generated by hbm2java
 */
@Entity
@Table(name = "expense_type", schema = "fmart")
public class ExpenseType  implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private Set<Expense> expenses = new HashSet<Expense>(0);

	public ExpenseType() {
	}

	public ExpenseType(int id) {
		this.id = id;
	}

	public ExpenseType(int id, String name, Set<Expense> expenses) {
		this.id = id;
		this.name = name;
		this.expenses = expenses;
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

	@Column(name = "name", length = 30)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name.toLowerCase();
	}
	@XmlTransient
	@JsonIgnore
	@JsonManagedReference@OneToMany(fetch = FetchType.LAZY, mappedBy = "expenseType")
	public Set<Expense> getExpenses() {
		return this.expenses;
	}

	public void setExpenses(Set<Expense> expenses) {
		this.expenses = expenses;
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