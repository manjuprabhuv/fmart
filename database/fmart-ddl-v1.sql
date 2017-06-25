-- Table: fmart.version

-- DROP TABLE fmart.version;

CREATE TABLE fmart.version
(
  id serial NOT NULL,
  component varchar(30)
)
WITH (
  OIDS=FALSE
);  


-- Table: fmart.company

-- DROP TABLE fmart.company;

CREATE TABLE fmart.company
(
  id serial NOT NULL,
  name varchar(30),
  address varchar(50),
  city varchar(20),
  state varchar(20),
  start_date date, 
  cash_in_hand integer,
  CONSTRAINT primary_key_comp PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);  

 -- Table: fmart.roles
-- DROP TABLE fmart.roles;

CREATE TABLE fmart.roles
(
  id serial NOT NULL,
  name varchar(15),
  CONSTRAINT primary_key_role PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);

-- Table: fmart.report
-- DROP TABLE fmart.report;

CREATE TABLE fmart.report
(
  id serial NOT NULL,
  name varchar(40),
  CONSTRAINT primary_key_report PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);


-- Table: fmart.employee

-- DROP TABLE fmart.employee;

CREATE TABLE fmart.employee
(
  id serial NOT NULL,
  name character varying(30),
  address character varying(50),
  city character varying(20),
  state character varying(20),
  start_date date,
  email character varying(30),
  mobile numeric(10,0),
  designation character varying(15),
  password character varying(250),
  username character varying(15),
  company_id integer,
  sale_percent numeric(4,2),
  active boolean DEFAULT true,
  CONSTRAINT primary_key_emp PRIMARY KEY (id),
  CONSTRAINT foreign_key_emp_comp_id FOREIGN KEY (company_id)
      REFERENCES fmart.company (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT employee_mobile_uk UNIQUE (mobile),
  CONSTRAINT employee_username_uk UNIQUE (username)
)
WITH (
  OIDS=FALSE
);

-- Table: fmart.emp_role

-- DROP TABLE fmart.emp_role;

CREATE TABLE fmart.emp_role
(
  id serial NOT NULL,
  emp_id integer,
  role_id integer,
  CONSTRAINT emp_role_pk PRIMARY KEY (id),
  CONSTRAINT emprole_emp_fk FOREIGN KEY (emp_id)
      REFERENCES fmart.employee (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT emprole_role_fk FOREIGN KEY (role_id)
      REFERENCES fmart.roles (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);




-- Table: fmart.emp_report

-- DROP TABLE fmart.emp_report;

CREATE TABLE fmart.emp_report
(
  emp_id integer,
  report_id integer,
  id serial NOT NULL,
  CONSTRAINT emp_report_pk PRIMARY KEY (id),
  CONSTRAINT empreport_emp_fk FOREIGN KEY (emp_id)
      REFERENCES fmart.employee (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT empreport_report_fk FOREIGN KEY (report_id)
      REFERENCES fmart.report (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);


-- Table: fmart.product_grp
-- DROP TABLE fmart.product_grp;

CREATE TABLE fmart.product_grp
(
  id serial NOT NULL,
  name varchar(30),
  updated_by integer,
  created timestamp with time zone,
  updated timestamp with time zone,
  created_by integer,
  CONSTRAINT  primary_key_pg PRIMARY KEY (id),
  CONSTRAINT prodgrp_createdby_fk FOREIGN KEY (created_by)
      REFERENCES fmart.employee (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT productgrp_updatedby_fk FOREIGN KEY (updated_by)
      REFERENCES fmart.employee (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

-- Table: fmart.product
-- DROP TABLE fmart.product;

CREATE TABLE fmart.product
(
  id serial NOT NULL,
  name character varying(40),
  image character varying(40),
  unit_price integer,
  product_grp_id integer,
  imagesrc bytea,
  updated_by integer,
  created timestamp with time zone,
  updated timestamp with time zone,
  created_by integer,
  CONSTRAINT primary_key_prod PRIMARY KEY (id),
  CONSTRAINT foreign_key_prod_pg_id FOREIGN KEY (product_grp_id)
      REFERENCES fmart.product_grp (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT prod_createdby_fk FOREIGN KEY (created_by)
      REFERENCES fmart.employee (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT prod_updatedby_fk FOREIGN KEY (updated_by)
      REFERENCES fmart.employee (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
   

 
-- Table: fmart.dealer
-- DROP TABLE fmart.dealer;

CREATE TABLE fmart.dealer
(
  id serial NOT NULL,
  name varchar(30),
  address varchar(50),
  city varchar(20),
  state varchar(20),
  start_date date,
  mobile numeric(10,0),
  updated_by integer,
  created timestamp with time zone,
  updated timestamp with time zone,
  created_by integer,
  CONSTRAINT primary_key_dealer PRIMARY KEY (id),
  CONSTRAINT dealer_createdby_fk FOREIGN KEY (created_by)
      REFERENCES fmart.employee (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT dealer_updatedby_fk FOREIGN KEY (updated_by)
      REFERENCES fmart.employee (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
  
-- Table: fmart.customer
-- DROP TABLE fmart.customer;

CREATE TABLE fmart.customer
(
	id serial NOT NULL,
	name character(100),
	address character varying(100),
	phone numeric(10,0),
	phone_string character(12),
	createdby integer,
	updatedby integer,
	company_id integer,
	created time with time zone,
	updated time with time zone,
	CONSTRAINT customer_pk PRIMARY KEY (id),
	CONSTRAINT foreign_key_emp_cust_id FOREIGN KEY (company_id)
		  REFERENCES fmart.company (id) MATCH SIMPLE
		  ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT customer_createdby_fk FOREIGN KEY (createdby)
	      REFERENCES fmart.employee (id) MATCH SIMPLE
	      ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT customer_updated_by FOREIGN KEY (updatedby)
	      REFERENCES fmart.employee (id) MATCH SIMPLE
	      ON UPDATE NO ACTION ON DELETE NO ACTION
	)
	WITH (
	  OIDS=FALSE
	);  
-- Table: fmart.expense_type
-- DROP TABLE fmart.expense_type;

CREATE TABLE fmart.expense_type
(
  id serial NOT NULL,
  name character varying(30),
  updated_by integer,
  created timestamp with time zone,
  updated timestamp with time zone,
  created_by integer,
  CONSTRAINT primary_key_et PRIMARY KEY (id),
  CONSTRAINT dasd UNIQUE (name),
  CONSTRAINT exptype_createdby_fk FOREIGN KEY (created_by)
      REFERENCES fmart.employee (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT exptype_updatedby_fk FOREIGN KEY (updated_by)
      REFERENCES fmart.employee (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

-- Table: fmart.expense
-- DROP TABLE fmart.expense;

CREATE TABLE fmart.expense
(
  id serial NOT NULL,
  type_id integer,
  particulars varchar(50),
  amount integer,
  created_by integer,
  company_id integer,
  employee_id integer,
  updated_by integer,
  created timestamp with time zone,
  updated timestamp with time zone,  
  CONSTRAINT primary_key_expense PRIMARY KEY (id),
  CONSTRAINT foreign_key_expense_emp_id FOREIGN KEY (employee_id)
      REFERENCES fmart.employee (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT foreign_key_expense_company_id FOREIGN KEY (company_id)
      REFERENCES fmart.company (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT foreign_key_expense_type_id FOREIGN KEY (type_id)
      REFERENCES fmart.expense_type (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT expense_createdby_fk FOREIGN KEY (created_by)
      REFERENCES fmart.employee (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT expense_updatedby_fk FOREIGN KEY (updated_by)
      REFERENCES fmart.employee (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION

)
WITH (
  OIDS=FALSE
);

-- Table: fmart.inventory
-- DROP TABLE fmart.inventory;

CREATE TABLE fmart.inventory
(
  product_id integer NOT NULL,
  company_id integer NOT NULL,
  customised boolean NOT NULL,
  quantity integer,
  updated_by integer,
  created timestamp with time zone,
  updated timestamp with time zone,
  created_by integer,
  CONSTRAINT primary_key_inventory PRIMARY KEY (product_id, company_id, customised),
  CONSTRAINT foreign_key_inv_comp_id FOREIGN KEY (company_id)
      REFERENCES fmart.company (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT foreign_key_inv_prod_id FOREIGN KEY (product_id)
      REFERENCES fmart.product (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT inventory_createdby_fk FOREIGN KEY (created_by)
      REFERENCES fmart.employee (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT inventory_updatedby_fk FOREIGN KEY (updated_by)
      REFERENCES fmart.employee (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

-- Table: fmart.purchase
-- DROP TABLE fmart.purchase;

CREATE TABLE fmart.purchase
(
  id serial NOT NULL,
  dealer_id integer,
  company_id integer,
  amount numeric(10,2),
  status varchar(15),
  updated_by integer,
  created timestamp with time zone,
  updated timestamp with time zone,
  created_by integer,
  description varchar(50),
  CONSTRAINT primary_key_purchase PRIMARY KEY (id),
  CONSTRAINT foreign_key_pur_dealer_id FOREIGN KEY (dealer_id)
      REFERENCES fmart.dealer (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT foreign_key_pur_company_id FOREIGN KEY (company_id)
      REFERENCES fmart.company (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT purchase_createdby_fk FOREIGN KEY (created_by)
      REFERENCES fmart.employee (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT purchase_updatedby_fk FOREIGN KEY (updated_by)
      REFERENCES fmart.employee (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

-- Table: fmart.purchase_detail
-- DROP TABLE fmart.purchase_detail;

CREATE TABLE fmart.purchase_detail
(
  id serial NOT NULL,
  purchase_id integer,
  product_id integer,
  quantity integer,
  amount numeric(10,2),
  remarks varchar(50),
  returned boolean,
  rate numeric(10,2),
  customised boolean,
  updated_by integer,
  created timestamp with time zone,
  updated timestamp with time zone,
  created_by integer,
  CONSTRAINT primary_key_purdet PRIMARY KEY (id),
  CONSTRAINT foreign_key_purdet_prod_id FOREIGN KEY (product_id)
      REFERENCES fmart.product (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT foreign_key_purdet_pur_id FOREIGN KEY (purchase_id)
      REFERENCES fmart.purchase (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT purdet_createdby_fk FOREIGN KEY (created_by)
      REFERENCES fmart.employee (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT purdet_updatedby_fk FOREIGN KEY (updated_by)
      REFERENCES fmart.employee (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

-- Table: fmart.payment
-- DROP TABLE fmart.payment;

CREATE TABLE fmart.payment
(
  id serial NOT NULL,
  dealer_id integer,
  company_id integer,
  amount numeric(10,2),
  updated_by integer,
  created timestamp with time zone,
  updated timestamp with time zone,
  created_by integer,
  CONSTRAINT primary_key_pay PRIMARY KEY (id),
  CONSTRAINT foreign_key_pay_dealer_id FOREIGN KEY (dealer_id)
      REFERENCES fmart.dealer (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT foreign_key_pay_company_id FOREIGN KEY (company_id)
      REFERENCES fmart.company (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT payment_createdby_fk FOREIGN KEY (created_by)
      REFERENCES fmart.employee (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT payment_updatedby_fk FOREIGN KEY (updated_by)
      REFERENCES fmart.employee (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

-- Table: fmart.sale
-- DROP TABLE fmart.sale;

CREATE TABLE fmart.sale
(
	  id serial NOT NULL,
	  company_id integer,
	  amount numeric(10,2),
	  advance numeric(10,2),
	  status character varying(20),
	  updated_by integer,
	  created timestamp with time zone,
	  updated timestamp with time zone,
	  created_by integer,
	  customer_id integer,
	  CONSTRAINT primary_key_sale PRIMARY KEY (id),
	  CONSTRAINT foreign_key_sale_company_id FOREIGN KEY (company_id)
	      REFERENCES fmart.company (id) MATCH SIMPLE
	      ON UPDATE CASCADE ON DELETE CASCADE,
	  CONSTRAINT sale_createdby_fk FOREIGN KEY (created_by)
	      REFERENCES fmart.employee (id) MATCH SIMPLE
	      ON UPDATE CASCADE ON DELETE CASCADE,
	  CONSTRAINT sale_customer_fk FOREIGN KEY (customer_id)
	      REFERENCES fmart.customer (id) MATCH SIMPLE
	      ON UPDATE NO ACTION ON DELETE NO ACTION,
	  CONSTRAINT sale_updatedby_fk FOREIGN KEY (updated_by)
	      REFERENCES fmart.employee (id) MATCH SIMPLE
	      ON UPDATE NO ACTION ON DELETE NO ACTION
)
	WITH (
	  OIDS=FALSE
);
  
-- Table: fmart.sale_detail,
-- DROP TABLE fmart.sale_detail;

CREATE TABLE fmart.sale_detail
(
  id serial NOT NULL,
  sale_id integer,
  product_id integer,
  quantity integer,
  amount numeric(10,2),
  returned boolean,
  remarks varchar(50),
  customised boolean,
  rate numeric(10,2),
  dispatched boolean,
  updated_by integer,
  created timestamp with time zone,
  updated timestamp with time zone,
  created_by integer,
  CONSTRAINT primary_key_saledet PRIMARY KEY (id),
  CONSTRAINT foreign_key_saledet_prod_id FOREIGN KEY (product_id)
      REFERENCES fmart.product (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT foreign_key_saledet_sale_id FOREIGN KEY (sale_id)
      REFERENCES fmart.sale (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT saledet_createdby_fk FOREIGN KEY (created_by)
      REFERENCES fmart.employee (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT saledet_updatedby_fk FOREIGN KEY (updated_by)
      REFERENCES fmart.employee (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
 
-- Table: fmart.receipt
-- DROP TABLE fmart.receipt;

CREATE TABLE fmart.receipt
(
  id serial NOT NULL,
  sale_id integer,
  company_id integer,
  amount numeric(10,2),
  particulars varchar(25),
  updated_by integer,
  created timestamp with time zone,
  updated timestamp with time zone,
  created_by integer,
  CONSTRAINT primary_key_receipt PRIMARY KEY (id),      
  CONSTRAINT foreign_key_receipt_sale_id FOREIGN KEY (sale_id)
      REFERENCES fmart.sale (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT foreign_key_receipt_company_id FOREIGN KEY (company_id)
      REFERENCES fmart.company (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT receipt_createdby_fk FOREIGN KEY (created_by)
      REFERENCES fmart.employee (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT receipt_updatedby_fk FOREIGN KEY (updated_by)
      REFERENCES fmart.employee (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);

-- Table: fmart.notifications

-- DROP TABLE fmart.notifications;

CREATE TABLE fmart.notifications
(
  from_val character varying(5000),
  to_val character varying(5000),
  type character varying(30),
  update_by character varying(50),
  id serial NOT NULL,
  action character varying(10),
  date date,
  CONSTRAINT notification_pk PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);

-- Table: fmart.db_backup_job

-- DROP TABLE fmart.db_backup_job;

CREATE TABLE fmart.db_backup_job
(
  id serial NOT NULL,
  cron_name character(100),
  directory character(200),
  status character(50),
  startts timestamp with time zone,
  endts timestamp with time zone,
  full_bck_filename character(100),
  prod_bck_filename character(100),
  CONSTRAINT corn_pk PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);


 -- Table: fmart.godown

-- DROP TABLE fmart.godown;

CREATE TABLE fmart.godown
(
  id serial NOT NULL,
  name character varying(30),
  address character varying(50),
  company_id integer,
  updated_by integer,
  created timestamp with time zone,
  updated timestamp with time zone,
  created_by integer,
  CONSTRAINT primary_key_godown PRIMARY KEY (id),
  CONSTRAINT godown_createdby_fk FOREIGN KEY (created_by)
      REFERENCES fmart.employee (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT godown_updatedby_fk FOREIGN KEY (updated_by)
      REFERENCES fmart.employee (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT foreign_key_godown_comp_id FOREIGN KEY (company_id)
      REFERENCES fmart.company (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);

  
-- Table: fmart.godown_inventory

-- DROP TABLE fmart.godown_inventory;
  
  
CREATE TABLE fmart.godown_inventory
(
  product_id integer NOT NULL,
  godown_id integer NOT NULL,
  count integer ,
  customised boolean NOT NULL,
  CONSTRAINT godown_primary_key_inventory PRIMARY KEY (product_id, godown_id, customised),
    CONSTRAINT godown_inventory_godown_id FOREIGN KEY (godown_id)
      REFERENCES fmart.godown (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION, 
   CONSTRAINT foreign_key__god_inv_prod_id FOREIGN KEY (product_id)
      REFERENCES fmart.product (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE    
)
WITH (
  OIDS=FALSE
);


