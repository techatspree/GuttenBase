CREATE TABLE offices(
officecode varchar(10) NOT NULL PRIMARY KEY, 
city varchar(50) NOT NULL, 
phone varchar(50) NOT NULL, 
addressline1 varchar(50) NOT NULL, 
addressline2 varchar(50) DEFAULT NULL, 
state varchar(50) DEFAULT NULL, 
country varchar(50) NOT NULL, 
postalcode varchar(15) NOT NULL, 
territory varchar(10) NOT NULL);

CREATE TABLE employees(
employeenumber int(11) NOT NULL PRIMARY KEY, 
lastname varchar(50) NOT NULL, 
firstname varchar(50) NOT NULL,
 extension varchar(10) NOT NULL, 
email varchar(100) NOT NULL, 
officecode varchar(10) NOT NULL, 
reportsto int(11) DEFAULT NULL, 
jobtitle varchar(50) NOT NULL, 
CONSTRAINT FK_employees_officecode_officecode_1
FOREIGN KEY (officecode) REFERENCES offices (officecode), 
CONSTRAINT FK_employees_reportsto_employeenumber_2 FOREIGN KEY (reportsto) REFERENCES employees (employeenumber) );

CREATE TABLE customers(
customernumber int(11) NOT NULL PRIMARY KEY,
customername varchar(50) NOT NULL,
contactlastname varchar(50) NOT NULL,
contactfirstname varchar(50) NOT NULL,
phone varchar(50) NOT NULL,
addressline1 varchar(50) NOT NULL,
addressline2 varchar(50) DEFAULT NULL,
city varchar(50) NOT NULL,
state varchar(50) DEFAULT NULL,
postalcode varchar(15) DEFAULT NULL,
country varchar(50) NOT NULL,
salesrepemployeenumber int(11) DEFAULT NULL,
creditlimit decimal(10,0) DEFAULT NULL,
CONSTRAINT FK_customers_salesrepemployeenumber_employeenumber_1 FOREIGN KEY (salesrepemployeenumber) REFERENCES employees (employeenumber));

CREATE TABLE productlines(
productline varchar(50) NOT NULL PRIMARY KEY, 
textdescription varchar(4000) DEFAULT NULL, 
htmldescription longtext, 
image longblob);

CREATE TABLE FOO_DATA
(
   ID bigint, -- PRIMARY KEY
   SOME_DATA BLOB
);