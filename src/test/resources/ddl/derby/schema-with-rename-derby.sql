CREATE TABLE tab_offices
(
  officecode VARCHAR(10) NOT NULL,
  id_city VARCHAR(50) NOT NULL,
  id_phone VARCHAR(50) NOT NULL,
  addressline1 VARCHAR(50) NOT NULL,
  addressline2 VARCHAR(50),
  state VARCHAR(50),
  country VARCHAR(50) NOT NULL,
  postalcode VARCHAR(15) NOT NULL,
  territory VARCHAR(10) NOT NULL
);
CREATE TABLE productlines
(
  productline VARCHAR(50) NOT NULL,
  textdescription VARCHAR(4000),
  htmldescription CLOB,
  image BLOB
);
CREATE TABLE products
(
  productcode VARCHAR(15) NOT NULL,
  productname VARCHAR(70) NOT NULL,
  productline VARCHAR(50) NOT NULL,
  productscale VARCHAR(10) NOT NULL,
  productvendor VARCHAR(50) NOT NULL,
  productdescription VARCHAR(50),
  quantityinstock VARCHAR(50) NOT NULL,
  buyprice VARCHAR(50) NOT NULL,
  msrp VARCHAR(50) NOT NULL
);
CREATE TABLE test_table
(
  test_2 INT NOT NULL
);
CREATE TABLE customers
(
  customernumber INT NOT NULL,
  customername VARCHAR(50) NOT NULL,
  contactlastname VARCHAR(50) NOT NULL,
  contactfirstname VARCHAR(50) NOT NULL,
  id_phone VARCHAR(50) NOT NULL,
  addressline1 VARCHAR(50) NOT NULL,
  addressline2 VARCHAR(50),
  id_city VARCHAR(50) NOT NULL,
  state VARCHAR(50),
  postalcode VARCHAR(15),
  country VARCHAR(50) NOT NULL,
  salesrepemployeenumber INT,
  creditlimit VARCHAR(50)
);
CREATE TABLE tab_orders
(
  ordernumber INT NOT NULL,
  orderdate DATE NOT NULL,
  requireddate DATE NOT NULL,
  shippeddate DATE,
  status VARCHAR(15) NOT NULL,
  comments VARCHAR(50),
  customernumber INT NOT NULL
);
CREATE TABLE orderdetails
(
  ordernumber INT NOT NULL,
  productcode VARCHAR(15) NOT NULL,
  quantityordered INT NOT NULL,
  priceeach VARCHAR(50) NOT NULL,
  orderlinenumber SMALLINT NOT NULL
);
CREATE TABLE payments
(
  customernumber INT NOT NULL,
  checknumber VARCHAR(50) NOT NULL,
  paymentdate DATE NOT NULL,
  amount VARCHAR(50) NOT NULL
);
CREATE TABLE employees
(
  employeenumber INT NOT NULL,
  lastname VARCHAR(50) NOT NULL,
  firstname VARCHAR(50) NOT NULL,
  extension VARCHAR(10) NOT NULL,
  email VARCHAR(100) NOT NULL,
  officecode VARCHAR(10) NOT NULL,
  reportsto INT,
  jobtitle VARCHAR(50) NOT NULL
);

CREATE TABLE FOO_DATA
(
   ID bigint, -- PRIMARY KEY
   SOME_DATA BLOB
);

ALTER TABLE customers ADD CONSTRAINT PK_customers_1 PRIMARY KEY (customernumber);
ALTER TABLE employees ADD CONSTRAINT PK_employees_1 PRIMARY KEY (employeenumber);
ALTER TABLE tab_offices ADD CONSTRAINT PK_offices_1 PRIMARY KEY (officecode);
ALTER TABLE orderdetails ADD CONSTRAINT PK_orderdetails_1 PRIMARY KEY (ordernumber, productcode);
ALTER TABLE tab_orders ADD CONSTRAINT PK_orders_1 PRIMARY KEY (ordernumber);
ALTER TABLE payments ADD CONSTRAINT PK_payments_1 PRIMARY KEY (customernumber, checknumber);
ALTER TABLE productlines ADD CONSTRAINT PK_productlines_1 PRIMARY KEY (productline);
ALTER TABLE products ADD CONSTRAINT PK_products_1 PRIMARY KEY (productcode);
ALTER TABLE test_table ADD CONSTRAINT PK_test_table_1 PRIMARY KEY (test_2);
ALTER TABLE customers ADD CONSTRAINT FK_customers_salesrepemployeenumber_employeenumber_1 FOREIGN KEY (salesrepemployeenumber) REFERENCES employees(employeenumber);
ALTER TABLE employees ADD CONSTRAINT FK_employees_officecode_officecode_1 FOREIGN KEY (officecode) REFERENCES tab_offices(officecode);
ALTER TABLE employees ADD CONSTRAINT FK_employees_reportsto_employeenumber_2 FOREIGN KEY (reportsto) REFERENCES employees(employeenumber);
ALTER TABLE orderdetails ADD CONSTRAINT FK_orderdetails_ordernumber_ordernumber_1 FOREIGN KEY (ordernumber) REFERENCES tab_orders(ordernumber);
ALTER TABLE orderdetails ADD CONSTRAINT FK_orderdetails_productcode_productcode_2 FOREIGN KEY (productcode) REFERENCES products(productcode);
ALTER TABLE tab_orders ADD CONSTRAINT FK_orders_customernumber_customernumber_1 FOREIGN KEY (customernumber) REFERENCES customers(customernumber);
ALTER TABLE payments ADD CONSTRAINT FK_payments_customernumber_customernumber_1 FOREIGN KEY (customernumber) REFERENCES customers(customernumber);
ALTER TABLE products ADD CONSTRAINT FK_products_productline_productline_1 FOREIGN KEY (productline) REFERENCES productlines(productline);
CREATE UNIQUE INDEX IDX_DX_ID_DX_IDXRIMAUSOMERS_1USTOMR__CUSTOMERS__CTMR_1_customer1 ON customers(customernumber);
CREATE UNIQUE INDEX IDX_IDXIIDX_PK_CUSTOMER_1_CUSTOMER2CUSTOMERS2_CSTOER_2_cusmers_2 ON customers(customernumber);
CREATE UNIQUE INDEX IDX_IDX_IDX_PRIMARY_CUSTOMERS_3_CUSTOMERS_3_customers_3 ON customers(customernumber);
CREATE UNIQUE INDEX IDX_IDX_PK_CUSTOMERS_1_CUSTOMERS_4_customers_4 ON customers(customernumber);
CREATE UNIQUE INDEX IDX_PRIMARY_customers_5 ON customers(customernumber);
CREATE INDEX IDX_IIDXID_IDX_AEPEPUMBCUSTOMR2CSORS_3CUSORS__CTOES_5_cusomers_6 ON customers(salesrepemployeenumber);
CREATE UNIQUE INDEX IDX_IX_ID_IDX_DX_PRARY_EMPLOES__EMPLOYES_1EMLYES_1_EMEES_epoyes1 ON employees(employeenumber);
CREATE UNIQUE INDEX IDX_IDX_IDX_IX_PKLOYEES1_EMPLOEES2_EMOYEES_2_EPLYEES_2_emploee_2 ON employees(employeenumber);
CREATE UNIQUE INDEX IDX_IDX_IDX_PRIMARY_EMPLOYEES_3_EMPLOYEES_3_employees_3 ON employees(employeenumber);
CREATE UNIQUE INDEX IDX_IDX_PK_EMPLOYEES_1_EMPLOYEES_4_employees_4 ON employees(employeenumber);
CREATE UNIQUE INDEX IDX_PRIMARY_employees_5 ON employees(employeenumber);
CREATE INDEX IDX_ID_IX_DX_IDX_REPOTST_EPLOEE_MPYES4LYEE5_MPLOYES__employees_6 ON employees(reportsto);
CREATE INDEX IDX_D_IXIDX_IDXOFFICDEMPOYEE_EPLOE_3EMPOYES_4_EMLOYES_5_eploys_7 ON employees(officecode);
CREATE UNIQUE INDEX IDX_ID_IX_IDX_DX_PRMRY_OFFCE1_OFFICES_1FFICES_1_OFFICS1_offics_1 ON tab_offices(officecode);
CREATE UNIQUE INDEX IDX_IDX_IX_IDX_PK_FFICES_1_OFFICES_2_FFICES_2_OFFICES_2_office_2 ON tab_offices(officecode);
CREATE UNIQUE INDEX IDX_IDX_IDX_PRIMARY_OFFICES_3_OFFICES_3_offices_3 ON tab_offices(officecode);
CREATE UNIQUE INDEX IDX_IDX_PK_OFFICES_1_OFFICES_4_offices_4 ON tab_offices(officecode);
CREATE UNIQUE INDEX IDX_PRIMARY_offices_5 ON tab_offices(officecode);
CREATE UNIQUE INDEX IDX_IDX_D_DID_PRIMAYREDTA_1_RERDALS__OERDTLS_ORDREIL1_odrdtils_1 ON orderdetails(ordernumber, productcode);
CREATE UNIQUE INDEX IDX_IDX_IDX_PRIMARY_ORDERDETAILS_3_ORDERDETAILS_3_orderdetails_2 ON orderdetails(ordernumber, productcode);
CREATE UNIQUE INDEX IDX_IDXDXX_P_ORDEDETALS__ODERETIS_OREDETS_2_ORDTAI__rerdetails_3 ON orderdetails(ordernumber, productcode);
CREATE UNIQUE INDEX IDX_IDX_PK_ORDERDETAILS_1_ORDERDETAILS_4_orderdetails_4 ON orderdetails(ordernumber, productcode);
CREATE UNIQUE INDEX IDX_PRIMARY_orderdetails_5 ON orderdetails(ordernumber, productcode);
CREATE INDEX IDX_DX_IIDX_IX_PRODUCTCODEEAI_2_RDRDEIS3RDEETS__DET5_orderdail_6 ON orderdetails(productcode);
CREATE UNIQUE INDEX IDX_IDX_IDX_IDX_IDX_PRIMRY_OR_1ORDERS_1_ORDERS_1ORDERS_1_order_1 ON tab_orders(ordernumber);
CREATE UNIQUE INDEX IDX_IDX_IDX_IDX_PK_ORDERS_1_ORDERS_2_ORDERS_2_ORDERS_2_orders_2 ON tab_orders(ordernumber);
CREATE UNIQUE INDEX IDX_IDX_IDX_PRIMARY_ORDERS_3_ORDERS_3_orders_3 ON tab_orders(ordernumber);
CREATE UNIQUE INDEX IDX_IDX_PK_ORDERS_1_ORDERS_4_orders_4 ON tab_orders(ordernumber);
CREATE UNIQUE INDEX IDX_PRIMARY_orders_5 ON tab_orders(ordernumber);
CREATE INDEX IDX_IDX_IDX_IX_IXCUSTOMEUER_DERS_2_RDERS_3_ORERS_4RDERS_5_rdrs_6 ON tab_orders(customernumber);
CREATE UNIQUE INDEX IDX_D_IDX_ID_ID_PRIMRY_PAENTS_1_PAYMENTS1YMNT_1_AYMTS_1_payens_1 ON payments(customernumber, checknumber);
CREATE UNIQUE INDEX IDX_DX_IDXIDPK_PAYMENTS_1_AYMETS_2_PYMENTS2_PAYMENS_2_payments_2 ON payments(customernumber, checknumber);
CREATE UNIQUE INDEX IDX_IDX_IDX_PRIMARY_PAYMENTS_3_PAYMENTS_3_payments_3 ON payments(customernumber, checknumber);
CREATE UNIQUE INDEX IDX_IDX_PK_PAYMENTS_1_PAYMENTS_4_payments_4 ON payments(customernumber, checknumber);
CREATE UNIQUE INDEX IDX_PRIMARY_payments_5 ON payments(customernumber, checknumber);
CREATE UNIQUE INDEX IDX_DXDX_IX_IXPRMRPRODULN_1_PROUCINES_PRLINSPOULIES_oductlines_1 ON productlines(productline);
CREATE UNIQUE INDEX IDX_ID_DX_IDXPK_PRDUCTLIN_RDUCLES_RDULIE_2_DUCLIE_2_produtlines2 ON productlines(productline);
CREATE UNIQUE INDEX IDX_IDX_IDX_PRIMARY_PRODUCTLINES_3_PRODUCTLINES_3_productlines_3 ON productlines(productline);
CREATE UNIQUE INDEX IDX_IDX_PK_PRODUCTLINES_1_PRODUCTLINES_4_productlines_4 ON productlines(productline);
CREATE UNIQUE INDEX IDX_PRIMARY_productlines_5 ON productlines(productline);
CREATE UNIQUE INDEX IDX_IDX_D_DXIDX_PRMARY_PODUTS_1_ROUCT1PRODUCS1_PROUCTS_1prodct_1 ON products(productcode);
CREATE UNIQUE INDEX IDX_ID_IDX_IDX_PK_ODUCT_1_PRODUCTS_2_PRODUCTS_2_PROUC_2_pducts_2 ON products(productcode);
CREATE UNIQUE INDEX IDX_IDX_IDX_PRIMARY_PRODUCTS_3_PRODUCTS_3_products_3 ON products(productcode);
CREATE UNIQUE INDEX IDX_IDX_PK_PRODUCTS_1_PRODUCTS_4_products_4 ON products(productcode);
CREATE UNIQUE INDEX IDX_PRIMARY_products_5 ON products(productcode);
CREATE INDEX IDX_IDXIDXI_ID_RDCTINEPROUCTS_2_PROUCT_PRDUC_4PROUCTS_products_6 ON products(productline);
CREATE UNIQUE INDEX IDX_IDXDX_IDX_IDX_PMAYS_ABLE_1_TE_BE_ETALE__TEST_TABE1_tst_ble_1 ON test_table(test_2);
CREATE UNIQUE INDEX IDX_IDX_IDX_PRIMARY_TEST_TABLE_3_TEST_TABLE_3_test_table_2 ON test_table(test_2);
CREATE UNIQUE INDEX IDX_IDX_IIDXPKTESTBLE_1_TES_ALE2TEST_TBE2_TESTTBL_2_test_table_3 ON test_table(test_2);
CREATE UNIQUE INDEX IDX_IDX_PK_TEST_TABLE_1_TEST_TABLE_4_test_table_4 ON test_table(test_2);
CREATE UNIQUE INDEX IDX_PRIMARY_test_table_5 ON test_table(test_2);