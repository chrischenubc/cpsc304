-------------------------------------------------------------------------------
-- CPSC304 Introduction to Relational Databases
-- Project Part 3: Implementation
-- Group 60 (Ziyu Xie, Yu Chen, and Annie Kim)
--
-- clerkTransactions.sql
-- performs transactions from clerks
------------------------------------------------------------------------------
SET SQLBLANKLINES ON

-- View a reservation given a confirmation number(e.g 000000001)
SELECT *
FROM Reservations
WHERE confNo = 000000001;

-- View if the car type reserved is still available on this branch given a confirmation number (e.g 000000001)

SELECT COUNT(*)
FROM Reservations R, Vehicles V 
WHERE R.confNo = 000000001 AND  R.vtname = V.vtname AND V.location = '1250 Granville St'  AND V.status = 'available';

-- Make a reservation prior to the rental(Every rental is required to pair with a reservation even though the customer just drop in and rent the car) 
INSERT INTO Reservations
VALUES(seq_confNo.nextval, 'Standard', 'TP355M1', TO_TIMESTAMP('2019-12-14 13:00', 'YYYY-MM-DD HH24:MI'), TO_TIMESTAMP('2019-12-18 17:45', 'YYYY-MM-DD HH24:MI'));

-- Update a car's odometer reading
Update Vehicles
SET odometer =111111111
Where vlicense = 'EF084D';

-- Rent a car given the vlicense and a confirmation number(e.g. 000000001)
INSERT INTO Rentals
VALUES(seq_rentalId.nextval,'896REN',TO_TIMESTAMP('2019-12-15 17:45', 'YYYY-MM-DD HH24:MI'),TO_TIMESTAMP('2019-12-18 17:45', 'YYYY-MM-DD HH24:MI'),33443,'MasterCard',0001223212323,TO_DATE('2023-07', 'YYYY-MM'),000000004);

-- Update the car status
Update Vehicles
SET status ='rented'
Where vlicense = '896REN';


-- Check if a car can be returned given a vlicense(if not then 0 is returned) 
SELECT COUNT(*)
FROM Vehicles 
Where vlicense='896REN' AND status ='rented';

-- Fetch the necessary details(not including the calculation) for the customer after returning a car
SELECT fromDateTime,odometer,confNo,vtname,feature,wrate,drate,hrate,krate,wirate,dirate,hirate
FROM Vehicles V, Rentals R, VehicleTypes T,
Where V.status = 'rented' AND V.vlicense = R.vlicense AND V.vlicense = '896REN' AND V.vtname = T.vtname;

-- Return a car and update the car's status and odometer
Update Vehicles
SET status ='available', odometer = 123333223
Where vlicense ='896REN';

-- Return a car and Generating a return tuble given a rental id(e.g 000000001), a odometer reading(123321122),fulltank(1) and value (12222.34)
INSERT INTO Returns
VALUES(000000001,TO_TIMESTAMP('2019-12-18 17:45', 'YYYY-MM-DD HH24:MI'),123321122,1,12222.34);

-- Daily rental report for all branches

-- Generate daily report of all rental infomation for all branches given a specific date
SELECT location, city, vtname
FROM Vehicles V, Rentals R 
WHERE trunc(R.fromDateTime,'YYYY-MM-DD') = to_date('2019-12-15', 'YYYY-MM-DD') AND V.vlicense = R.vlicense
GROUP BY V.location,V.city,V.vtname;

-- Generate daily report for number of rentals per category across all branches given a specific date
SELECT COUNT(*) vtname
FROM Vehicles V, Rentals R 
WHERE to_date(R.fromDateTime,'YYYY-MM-DD') = to_date('2019-12-15', 'YYYY-MM-DD') AND V.vlicense = R.vlicense
GROUP BY V.vtname;

-- Generate daily report for total number of rentals at each branch
SELECT COUNT(*)
FROM Vehicles V, Rentals R 
WHERE trunc(R.fromDateTime) = to_date('2019-12-15', 'YYYY-MM-DD') AND V.vlicense = R.vlicense
GROUP BY V.location,V.city;

-- Generate daily report for total number of rentals across the compnay
SELECT COUNT(*)
FROM Rentals R 
WHERE trunc(R.fromDateTime) = to_date('2019-12-15', 'YYYY-MM-DD') AND V.vlicense = R.vlicense;

-- Daily rental report specific to a branch

-- Generate daily report of all rental infomation for a specific branch given a specific date
SELECT * 
FROM Vehicles V, Rentals R 
WHERE trunc(R.fromDateTime) = to_date('2019-12-15', 'YYYY-MM-DD') AND V.location = '1250 Granville St' AND V.city='Vancouver' AND V.vlicense = R.vlicense;
GROUP BY V.vtname;

-- Generate daily report for number of rentals per category across all branches given a specific date
SELECT COUNT(*)
FROM Vehicles V, Rentals R 
WHERE trunc(R.fromDateTime) = to_date('2019-12-15', 'YYYY-MM-DD') AND V.location = '1250 Granville St' AND V.city='Vancouver' AND V.vlicense = R.vlicense;
GROUP BY V.vtname;

-- Generate daily report for total number of rentals at a specific branch
SELECT COUNT(*)
FROM Vehicles V, Rentals R 
WHERE trunc(R.fromDateTime) = to_date('2019-12-15', 'YYYY-MM-DD') AND V.location = '1250 Granville St' AND V.city='Vancouver' AND V.vlicense = R.vlicense；



-- Daily return report for all branches

-- Generate daily report of all return infomation for all branches given a specific date
SELECT * 
FROM Vehicles V, Returns R, Rentals R2
WHERE trunc(R.returnDateTime) = to_date('2019-12-18', 'YYYY-MM-DD') AND V.vlicense = R2.vlicense AND R.rid = R2.rid
GROUP BY V.location,V.city,V.vtname;

-- Generate daily report for number of renturns per category across all branches given a specific date
SELECT COUNT(*)
FROM Vehicles V, Returns R, Rentals R2 
WHERE trunc(R.returnDateTime) = to_date('2019-12-18', 'YYYY-MM-DD') AND V.vlicense = R2.vlicense AND R.rid = R2.rid
GROUP BY V.vtname;

-- Generate daily report for revenue per category of renturns aross all branches
SELECT SUM(R.value)
FROM Vehicles V, Returns R, Rentals R2 
WHERE trunc(R.returnDateTime) = to_date('2019-12-18', 'YYYY-MM-DD') AND V.vlicense = R2.vlicense AND R.rid = R2.rid
GROUP BY V.vtname;

-- Generate daily report for number of vehicles return per branches
SELECT COUNT(*)
FROM Vehicles V, Returns R, Rentals R2 
WHERE trunc(R.returnDateTime) = to_date('2019-12-18', 'YYYY-MM-DD') AND V.vlicense = R2.vlicense AND R.rid = R2.rid
GROUP BY V.location, V.city;

-- Generate daily report for subtotal revenue per branches
SELECT SUM(R.value)
FROM Vehicles V, Returns R, Rentals R2 
WHERE trunc(R.returnDateTime) = to_date('2019-12-18', 'YYYY-MM-DD') AND V.vlicense = R2.vlicense AND R.rid = R2.rid
GROUP BY V.location, V.city;

-- Generate daily report for grand total revenue across the company
SELECT COUNT(*)
SELECT SUM(R.value)
FROM Vehicles V, Returns R, Rentals R2 
WHERE trunc(R.returnDateTime) = to_date('2019-12-18', 'YYYY-MM-DD') AND V.vlicense = R2.vlicense AND R.rid = R2.rid


-- Daily return report for a specific branch

-- Generate daily report of all return infomation a specific branch given a specific date
SELECT * 
FROM Vehicles V, Returns R, Rentals R2
WHERE trunc(R.returnDateTime) = to_date('2019-12-18', 'YYYY-MM-DD') AND V.vlicense = R2.vlicense AND R.rid = R2.rid AND V.location ='burrad 2020' AND V.city ='Vancouver'
GROUP BY V.vtname;

-- Generate daily report for number of renturns per category at a specific branch given a specific date
SELECT COUNT(*)
FROM Vehicles V, Returns R, Rentals R2 
WHERE trunc(R.returnDateTime) = to_date('2019-12-18', 'YYYY-MM-DD') AND V.vlicense = R2.vlicense AND R.rid = R2.rid AND V.location ='burrad 2020' AND V.city ='Vancouver'
GROUP BY V.vtname;

-- Generate daily report for revenue per category of at a specifc branch
SELECT SUM(R.value)
FROM Vehicles V, Returns R, Rentals R2 
WHERE trunc(R.returnDateTime) = to_date('2019-12-18', 'YYYY-MM-DD') AND V.vlicense = R2.vlicense AND R.rid = R2.rid AND V.location ='burrad 2020' AND V.city ='Vancouver'
GROUP BY V.vtname;

-- Generate daily report for number of vehicles return at a specific branch
SELECT COUNT(*)
FROM Vehicles V, Returns R, Rentals R2 
WHERE trunc(R.returnDateTime) = to_date('2019-12-18', 'YYYY-MM-DD') AND V.vlicense = R2.vlicense AND R.rid = R2.rid AND V.location ='burrad 2020' AND V.city ='Vancouver';

-- Generate daily report for the revenue at a specific branch
SELECT SUM(R.value)
FROM Vehicles V, Returns R, Rentals R2 
WHERE trunc(R.returnDateTime) = to_date('2019-12-18', 'YYYY-MM-DD') AND V.vlicense = R2.vlicense AND R.rid = R2.rid AND V.location ='burrad 2020' AND V.city ='Vancouver';
