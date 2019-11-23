-------------------------------------------------------------------------------
-- CPSC304 Introduction to Relational Databases
-- Project Part 3: Implementation
-- Group 60 (Ziyu Xie, Yu Chen, and Annie Kim)
--
-- customerTransactions.sql
-- performs transactions for customers
------------------------------------------------------------------------------
SET SQLBLANKLINES ON

-- View the number of available vehicles
-- No information provided
SELECT COUNT(*)
FROM Vehicles
WHERE status = 'available';

SELECT *
FROM Vehicles
WHERE status = 'available'
ORDER BY vtname;

-- Only car type is provided (ex. SUV)
SELECT COUNT(*)
FROM Vehicles
WHERE status = 'available' AND vtname = 'SUV';

SELECT *
FROM Vehicles
WHERE status = 'available' AND vtname = 'SUV';

-- Only location is provided (ex. 1250 Granville St)
SELECT COUNT(*)
FROM Vehicles
WHERE status = 'available' AND location = '1250 Granville St';

SELECT *
FROM Vehicles
WHERE status = 'available' AND location = '1250 Granville St';

-- Only time interval is provided (ex. 2019-12-14 13:00 ~ 2019-12-28 17:45)
SELECT COUNT(*)
FROM Vehicles
WHERE Vehicles.vlicense NOT IN(
    SELECT Rentals.vlicense
    FROM Vehicles, Rentals
    WHERE (Vehicles.vlicense = Rentals.vlicense) AND
        (Rentals.fromDateTime < TO_TIMESTAMP('2019-12-18 17:45', 'YYYY-MM-DD HH24:MI')) OR
        ((Rentals.fromDateTime < TO_TIMESTAMP('2019-12-14 13:00', 'YYYY-MM-DD HH24:MI')) AND
            TO_TIMESTAMP('2019-12-18 17:45', 'YYYY-MM-DD HH24:MI') < Rentals.toDateTime) OR
        (TO_TIMESTAMP('2019-12-14 13:00', 'YYYY-MM-DD HH24:MI') < Rentals.toDateTime)
);

SELECT *
FROM Vehicles
WHERE Vehicles.vlicense NOT IN(
    SELECT Rentals.vlicense
    FROM Vehicles, Rentals
    WHERE (Vehicles.vlicense = Rentals.vlicense) AND
        (Rentals.fromDateTime < TO_TIMESTAMP('2019-12-18 17:45', 'YYYY-MM-DD HH24:MI')) OR
        ((Rentals.fromDateTime < TO_TIMESTAMP('2019-12-14 13:00', 'YYYY-MM-DD HH24:MI')) AND
            TO_TIMESTAMP('2019-12-18 17:45', 'YYYY-MM-DD HH24:MI') < Rentals.toDateTime) OR
        (TO_TIMESTAMP('2019-12-14 13:00', 'YYYY-MM-DD HH24:MI') < Rentals.toDateTime)
);

-- Returns 0 if a customer is new (ex. 'Annie Kim', '5083451849')
SELECT COUNT(*)
FROM Customers
WHERE cellphone = '5083451849' AND name = 'Annie Kim';
