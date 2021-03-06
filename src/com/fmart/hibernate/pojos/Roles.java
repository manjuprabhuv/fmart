package com.fmart.hibernate.pojos;

// Generated Mar 19, 2015 11:56:59 PM by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonManagedReference;
/**
 * Roles generated by hbm2java
 */
@Entity
@Table(name = "roles", schema = "fmart")
public class Roles implements java.io.Serializable {

	private int id;
	private String name;
	private Set<EmpRole> empRoles = new HashSet<EmpRole>(0);
	

	public Roles() {
	}

	public Roles(int id) {
		this.id = id;
	}

	public Roles(int id, String name, Set<EmpRole> empRoles) {
		this.id = id;
		this.name = name;
		this.empRoles = empRoles;
		
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

	@Column(name = "name", length = 15)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@JsonManagedReference@OneToMany(fetch = FetchType.LAZY, mappedBy = "roles")
	public Set<EmpRole> getEmpRoles() {
		return this.empRoles;
	}

	public void setEmpRoles(Set<EmpRole> empRoles) {
		this.empRoles = empRoles;
	}




}
