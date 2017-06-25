--Table: fmart."roles"
INSERT INTO fmart.roles(id, name) VALUES (1, 'Super Admin');
INSERT INTO fmart.roles(id, name) VALUES (2, 'Admin');
INSERT INTO fmart.roles(id, name) VALUES (3, 'Manager');
INSERT INTO fmart.roles(id, name) VALUES (4, 'Sales');
INSERT INTO fmart.roles(id, name) VALUES (5, 'Purchase');
INSERT INTO fmart.roles(id, name) VALUES (6, 'Sales&Purchase');

--Table: fmart."report"
INSERT INTO fmart.report(id, name) VALUES (1,'Order Bookings');
INSERT INTO fmart.report(id, name) VALUES (2 ,'Booking Receipts'); 
INSERT INTO fmart.report(id, name) VALUES (3,'Cancelled Bookings'); 
INSERT INTO fmart.report(id, name) VALUES (4,'Booking Dispatches'); 
INSERT INTO fmart.report(id, name) VALUES (5,'Booking Returns'); 
INSERT INTO fmart.report(id, name) VALUES (6,'Cleared Sale'); 
INSERT INTO fmart.report(id, name) VALUES (7,'Purchase Inwards'); 
INSERT INTO fmart.report(id, name) VALUES (8,'Purchase Returns'); 
INSERT INTO fmart.report(id, name) VALUES (9,'Supplier - Purchases'); 
INSERT INTO fmart.report(id, name) VALUES (10,'Supplier - Returns'); 
INSERT INTO fmart.report(id, name) VALUES (11,'Expense Report'); 
INSERT INTO fmart.report(id, name) VALUES (12,'Sales Commission');
INSERT INTO fmart.report(id, name) VALUES (13,'Profit And Loss'); 

---Table: fmart:"company"
INSERT INTO fmart.company(
             name,  cash_in_hand)
    VALUES ( 'Default Company','0');

---Table: fmart:"employee"
INSERT INTO fmart.employee(
             name, 
            password, username, company_id)
    VALUES ('default','37a8eec1ce19687d132fe29051dca629d164e2c4958ba141d5f4133a33f0688f','default', 1);
    
---Table: fmart:"emp_role"        
INSERT INTO fmart.emp_role(
            emp_id, role_id)
    VALUES ( 1, 1);

---Table: fmart:"expense_type"        
INSERT INTO fmart.expense_type(name)
    VALUES ('salary');