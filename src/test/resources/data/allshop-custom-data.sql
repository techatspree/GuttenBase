INSERT INTO offices (officecode, city, phone, addressline1, addressline2, state, country, postalcode, territory) VALUES ('1', 'San Francisco', '+1 650 219 4782', '100 Market Street', 'Suite 300', 'CA', 'USA', '94080', 'NA');
INSERT INTO offices (officecode, city, phone, addressline1, addressline2, state, country, postalcode, territory) VALUES ('2', 'Boston', '+1 215 837 0825', '1550 Court Place', 'Suite 102', 'MA', 'USA', '02107', 'NA');
INSERT INTO offices (officecode, city, phone, addressline1, addressline2, state, country, postalcode, territory) VALUES ('3', 'NYC', '+1 212 555 3000', '523 East 53rd Street', 'apt. 5A', 'NY', 'USA', '10022', 'NA');

INSERT INTO productlines (productline, textdescription, htmldescription, image) VALUES ('Classic Cars', 'Attention car enthusiasts', NULL, NULL);
INSERT INTO productlines (productline, textdescription, htmldescription, image) VALUES ('Motorcycles', 'Our motorcycles are state of the art replicas of classic as well as contemporary as motorcycle legends such as Harley Davidson, Ducati and Vespa.', NULL, NULL);
INSERT INTO productlines (productline, textdescription, htmldescription, image) VALUES ('Planes', 'Unique, diecast airplane and helicopter replicas suitable for collections, as well as home, office or classroom decorations.', NULL, NULL);

INSERT INTO products (productcode, productname, productline, productscale, productvendor, productdescription, quantityinstock, buyprice, msrp) VALUES ('S10_1949', '1952 Alpine Renault 1300', 'Classic Cars', '1:10', 'Classic Metal Creations', 'Turnable front wheels', '7305', '99', '214');
INSERT INTO products (productcode, productname, productline, productscale, productvendor, productdescription, quantityinstock, buyprice, msrp) VALUES ('S10_2016', '1996 Moto Guzzi 1100i', 'Motorcycles', '1:10', 'Highway 66 Mini Classics', 'Official Moto Guzzi logos and insignias', '6625', '69', '119');
INSERT INTO products (productcode, productname, productline, productscale, productvendor, productdescription, quantityinstock, buyprice, msrp) VALUES ('S10_4698', '2003 Harley-Davidson Eagle Drag Bike', 'Motorcycles', '1:10', 'Red Start Diecast', 'Model features, official Harley Davidson logos', '5582', '91', '194');

INSERT INTO test_table (test_2) VALUES (1);
INSERT INTO test_table (test_2) VALUES (2);
INSERT INTO test_table (test_2) VALUES (3);

INSERT INTO employees (employeenumber, lastname, firstname, extension, email, officecode, reportsto, jobtitle) VALUES (1188, 'Firrelli', 'Jeff', 'x9273', 'jfirrelli@classicmodelcars.com', '1', NULL, 'VP Marketing');
INSERT INTO employees (employeenumber, lastname, firstname, extension, email, officecode, reportsto, jobtitle) VALUES (1076, 'Firrelli', 'Julie', 'x2173', 'jfirrelli@classicmodelcars.com', '2',NULL, 'Sales Rep');
INSERT INTO employees (employeenumber, lastname, firstname, extension, email, officecode, reportsto, jobtitle) VALUES (1286, 'Tseng', 'Foon Yue', 'x2248', 'ftseng@classicmodelcars.com', '3', NULL, 'Sales Rep');

INSERT INTO customers (customernumber, customername, contactlastname, contactfirstname, phone, addressline1, addressline2, city, state, postalcode, country, salesrepemployeenumber, creditlimit) VALUES (114, 'Australian', 'Ferguson', 'Peter', '0395', 'Kilda Road', 'Level 3', 'Melbourne', 'Victoria', '3004', 'Australia', 1188, '117');
INSERT INTO customers (customernumber, customername, contactlastname, contactfirstname, phone, addressline1, addressline2, city, state, postalcode, country, salesrepemployeenumber, creditlimit) VALUES (128, 'Blauer', 'Keitel', 'Roland', '4969', 'Lyonerstr. 34', NULL, 'Frankfurt', NULL, '60528', 'Germany', 1076, '597');
INSERT INTO customers (customernumber, customername, contactlastname, contactfirstname, phone, addressline1, addressline2, city, state, postalcode, country, salesrepemployeenumber, creditlimit) VALUES (181, 'Vitachrome', 'Frick', 'Michael', '2125', 'Kingston', 'Suite 101', 'NYC', 'NY', '10022', 'USA', 1286, '764');

INSERT INTO orders (ordernumber, orderdate, requireddate, shippeddate, status, comments, customernumber) VALUES (10102, '2003-01-08', '2003-01-16', '2003-01-12', 'Shipped', NULL, 114);
INSERT INTO orders (ordernumber, orderdate, requireddate, shippeddate, status, comments, customernumber) VALUES (10101, '2003-01-07', '2003-01-16', '2003-01-09', 'Shipped', 'Check on availability.', 128);
INSERT INTO orders (ordernumber, orderdate, requireddate, shippeddate, status, comments, customernumber) VALUES (10103, '2003-01-27', '2003-02-05', '2003-01-31', 'Shipped', NULL, 181);

INSERT INTO orderdetails (ordernumber, productcode, quantityordered, priceeach, orderlinenumber) VALUES (10101, 'S10_1949', 50, '55', 2);
INSERT INTO orderdetails (ordernumber, productcode, quantityordered, priceeach, orderlinenumber) VALUES (10102, 'S10_2016', 22, '75', 4);
INSERT INTO orderdetails (ordernumber, productcode, quantityordered, priceeach, orderlinenumber) VALUES (10103, 'S10_4698', 49, '35', 1);

INSERT INTO payments (customernumber, checknumber, paymentdate, amount) VALUES (128, 'JM555205', '2003-06-03', '14571');
INSERT INTO payments (customernumber, checknumber, paymentdate, amount) VALUES (114, 'OM314933', '2004-12-16', '1676');
INSERT INTO payments (customernumber, checknumber, paymentdate, amount) VALUES (181, 'BO864823', '2004-12-15', '14191');


