-- This is a test
-- --

CREATE TABLE FOO_USER (
   ID bigint /* nonsense comment*/ PRIMARY KEY,
   -- this is a column
   COMPANY_NAME VARCHAR(255)
);

/**************************************/

INSERT INTO FOO_USER VALUES(1, 'Un''fug');

SELECT *
FROM FOO_USER
;
/**
 * 
 *
 * the end
 */