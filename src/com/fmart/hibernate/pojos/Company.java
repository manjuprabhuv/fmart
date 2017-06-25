package com.fmart.hibernate.pojos;
// default package
// Generated Mar 17, 2015 12:21:20 AM by Hibernate Tools 4.0.0

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Company generated by hbm2java
 */
@Entity
@Table(name = "company", schema = "fmart")
public class Company implements java.io.Serializable {

	private Integer id;
	private String name;
	private String address;
	private String city;
	private String state;
	private Date startDate;
	private Integer cashInHand;

	public Company() {
		
		this.startDate= new Date();
	}

	public Company(Integer id) {
		this.id = id;
	}

	public Company(Integer id, String name, String address, String city,
			String state, Date startDate, Integer cashInHand) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.city = city;
		this.state = state;
		this.startDate = startDate;
		this.cashInHand = cashInHand;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	//@SequenceGenerator(name="company_seq",sequenceName="fmart.company_seq", allocationSize=1)
	//@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="company_seq")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	
	@Column(name = "start_date", length = 29)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name = "cash_in_hand")
	public Integer getCashInHand() {
		return this.cashInHand;
	}

	public void setCashInHand(Integer cashInHand) {
		this.cashInHand = cashInHand;
	}

}
