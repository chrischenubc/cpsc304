-------------------------------------------------------------------------------
-- CPSC304 Introduction to Relational Databases
-- Project Part 3: Implementation
-- Group 60 (Ziyu Xie, Yu Chen, and Annie Kim)
--
-- initDB.sql
-- creates tables for use
-------------------------------------------------------------------------------
SET SQLBLANKLINES ON;

-- Drop existing tables
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE Branches CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE VehicleTypes CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE Vehicles CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE Customers CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE Reservations CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE Rentals CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE Returns CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_confNo';
    EXECUTE IMMEDIATE 'DROP SEQUENCE seq_rentalId';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE != -942 THEN
            RAISE;
        END IF;
END;
/

-- Add each table
CREATE TABLE Branches(
    location VARCHAR2(255),
    city VARCHAR2(255),

    PRIMARY KEY(location, city)
);

CREATE TABLE VehicleTypes(
    vtname VARCHAR2(255),
    features VARCHAR2(255),
    wrate NUMBER(9,2),
    drate NUMBER(9,2),
    hrate NUMBER(9,2),
    krate NUMBER(9,2),
    wirate NUMBER(9,2),
    dirate NUMBER(9,2),
    hirate NUMBER(9,2),

    PRIMARY KEY(vtname)
);

CREATE TABLE Vehicles(
    -- vid has been removed as a vehicle is identified by its plate number
    vlicense VARCHAR2(255),
    make VARCHAR2(255),
    model VARCHAR2(255),
    year NUMBER(4,0),
    color VARCHAR2(255),
    odometer NUMBER(9,0),
    status VARCHAR2(255),
    vtname VARCHAR2(255),
    location VARCHAR2(255),
    city VARCHAR2(255),

    PRIMARY KEY(vlicense),

    FOREIGN KEY(location, city)
        REFERENCES Branches
        ON DELETE CASCADE,
    FOREIGN KEY(vtname)
        REFERENCES VehicleTypes
        ON DELETE CASCADE
);

CREATE TABLE Customers(
    cellphone VARCHAR2(255),
    name VARCHAR2(255),
    address VARCHAR2(255),
    dlicense VARCHAR2(255),

    PRIMARY KEY(dlicense)
);

CREATE TABLE Reservations(
    confNo NUMBER(9,0),
    vtname VARCHAR2(255),
    -- cellphone has been replaced by dlicense
    dlicense VARCHAR2(255),
    fromDateTime TIMESTAMP,
    toDateTime TIMESTAMP,

    PRIMARY KEY(confNo),

    FOREIGN KEY(vtname)
        REFERENCES VehicleTypes
        ON DELETE CASCADE,
    FOREIGN KEY(dlicense)
        REFERENCES Customers
        ON DELETE CASCADE
);


CREATE SEQUENCE seq_confNo
MINVALUE 0
START WITH 76461
INCREMENT BY 1
CACHE 10;

CREATE TABLE Rentals(
    rid NUMBER(9,0),
    vlicense VARCHAR2(255),
    -- cellphone has been replaced by dlicense
    dlicense VARCHAR2(255),
    fromDateTime TIMESTAMP,
    toDateTime TIMESTAMP,
    odometer NUMBER(9,0),
    cardName VARCHAR2(255),
    cardNo VARCHAR2(255),
    ExpDate Date,
    confNo NUMBER(9,0),

    PRIMARY KEY(rid),

    FOREIGN KEY(vlicense)
        REFERENCES Vehicles
        ON DELETE CASCADE,
    FOREIGN KEY(dlicense)
        REFERENCES Customers
        ON DELETE CASCADE,
    FOREIGN KEY(confNo)
        REFERENCES Reservations
        ON DELETE CASCADE
);

CREATE SEQUENCE seq_rentalId
MINVALUE 0
START WITH 11
INCREMENT BY 1
CACHE 10;

CREATE TABLE Returns(
    rid NUMBER(9,0),
    returnDateTime TIMESTAMP,
    odometer NUMBER(9,0),
    fullTank NUMBER(1),
    value NUMBER(9,2),

    PRIMARY KEY(rid),

    FOREIGN KEY(rid)
        REFERENCES Rentals
);

-- Add tuples
-- location, city
INSERT INTO Branches VALUES('180 W Georgia St', 'Vancouver');
INSERT INTO Branches VALUES('1250 Granville St', 'Vancouver');
INSERT INTO Branches VALUES('10376 King George Blvd', 'Surrey');
INSERT INTO Branches VALUES('221 Thompson St', 'New York');
INSERT INTO Branches VALUES('1601 3rd Ave', 'Seattle');
INSERT INTO Branches VALUES('9051 Beckwith Rd', 'Richmond');
INSERT INTO Branches VALUES('5249 Regent St', 'Burnaby');
INSERT INTO Branches VALUES('3090 Westwood St', 'Port Coquitlam');
INSERT INTO Branches VALUES ('1321 3rd Ave', 'New Westminster');
INSERT INTO Branches VALUES('751 Marine Dr', 'North Vancouver');

--vtname, features, wrate, drate, hrate, krate, wirate, dirate, hirate
INSERT INTO VehicleTypes VALUES('Economy', 'hourly', 181.99, 26.04, NULL, 0.20, 47.42, NULL, NULL);
INSERT INTO VehicleTypes VALUES('Compact', 'hourly', 191.99, 27.47, NULL, 0.20, 48.79, NULL, NULL);
INSERT INTO VehicleTypes VALUES('Mid-size', 'hourly', 201.99, 28.90, NULL, NULL, 50.16, NULL, NULL);
INSERT INTO VehicleTypes VALUES('Standard', 'weekly', 211.99, 30.33, NULL, NULL, 51.54, NULL, NULL);
INSERT INTO VehicleTypes VALUES('Full-size', 'weekly', 221.99, 31.76, NULL, NULL, 52.91, NULL, NULL);
INSERT INTO VehicleTypes VALUES('SUV', 'weekly', 245.99, 35.19, NULL, NULL, 56.20, NULL, NULL);
INSERT INTO VehicleTypes VALUES('Truck', 'weekly', 331.99, 47.50, NULL, NULL, 67.99, NULL, NULL);

--vlicense, make, model, year, color, odometer, status, vtname, location, city
INSERT INTO Vehicles VALUES('CA762X', 'Chevrolet', 'Spark', 2018, 'White', 50765, 'available', 'Economy', '5249 Regent St', 'Burnaby');
INSERT INTO Vehicles VALUES('866MAB', 'Nissan', 'Micra', 2018, 'White', 29338, 'available', 'Economy', '180 W Georgia St', 'Vancouver');
INSERT INTO Vehicles VALUES('3IV5PO', 'Nissan', 'Micra', 2017, 'White', 68471, 'available', 'Economy', '221 Thompson St', 'New York');
INSERT INTO Vehicles VALUES('8QP59I', 'MINI', 'Cooper', 2009, 'Black', 64004, 'available', 'Economy', '221 Thompson St', 'New York');
INSERT INTO Vehicles VALUES('EF084D', 'Hyundai', 'Accent', 2017, 'White', 52692, 'rented', 'Compact', '1250 Granville St', 'Vancouver');
INSERT INTO Vehicles VALUES('62629C', 'Nissan', 'Versa Note', 2009, 'Blue', 168638, 'maintenance', 'Compact', '5249 Regent St', 'Burnaby');
INSERT INTO Vehicles VALUES('CS015A', 'Toyota', 'Yaris', 2018, 'Red', 52586, 'available', 'Compact', '9051 Beckwith Rd', 'Richmond');
INSERT INTO Vehicles VALUES('DE310T', 'Hyundai', 'Elantra', 2018, 'Red', 112, 'available', 'Mid-size', '221 Thompson St', 'New York');
INSERT INTO Vehicles VALUES('R1AV04', 'Hyundai', 'Elantra', 2018, 'Silver', 4561, 'available', 'Mid-size', '1321 3rd Ave', 'New Westminster');
INSERT INTO Vehicles VALUES('991MKS', 'Toyota', 'Corolla', 2015, 'Red', 82924, 'maintenance', 'Mid-size', '5249 Regent St', 'Burnaby');
INSERT INTO Vehicles VALUES('Y28613', 'Volkswagen', 'Jetta', 2016, 'Blue', 37467, 'available', 'Standard', '221 Thompson St', 'New York');
INSERT INTO Vehicles VALUES('A3E3X7', 'Kia', 'Soul', 2019, 'White', 40673, 'available', 'Standard', '180 W Georgia St', 'Vancouver');
INSERT INTO Vehicles VALUES('Q8N5DZ', 'Kia', 'Soul', 2019, 'Grey', 45386, 'available', 'Standard', '1250 Granville St', 'Vancouver');
INSERT INTO Vehicles VALUES('ML6469', 'Honda', 'Civic', 2017, 'Grey', 12811, 'rented', 'Standard', '1601 3rd Ave', 'Seattle');
INSERT INTO Vehicles VALUES('1HA7IT', 'Lincoln', 'Continental', 2017, 'White', 40687, 'available', 'Full-size', '1601 3rd Ave', 'Seattle');
INSERT INTO Vehicles VALUES('BMO34A', 'Chevrolet', 'Impala', 2013, 'White', 63867, 'available', 'Full-size', '1601 3rd Ave', 'Seattle');
INSERT INTO Vehicles VALUES('09CCU1', 'Hyundai', 'Sonata', 2018, 'Silver', 2000, 'available', 'Full-size', '751 Marine Dr', 'North Vancouver');
INSERT INTO Vehicles VALUES('XNK656', 'Chevrolet', 'Malibu', 2018, 'Red', 38085, 'rented', 'Full-size', '221 Thompson St', 'New York');
INSERT INTO Vehicles VALUES('241EKA', 'Nissan', 'Qashqai', 2015, 'White', 71611, 'rented', 'SUV', '10376 King George Blvd', 'Surrey');
INSERT INTO Vehicles VALUES('FT65HX', 'Toyota', 'FJ Cruiser', 2007, 'Silver', 231894, 'available', 'SUV', '751 Marine Dr', 'North Vancouver');
INSERT INTO Vehicles VALUES('896REN', 'Nissan', 'Rogue', 2019, 'White', 278, 'available', 'SUV', '1250 Granville St', 'Vancouver');
INSERT INTO Vehicles VALUES('837UVE', 'Nissan', 'Rogue', 2017, 'White', 243, 'available', 'SUV', '1250 Granville St', 'Vancouver');
INSERT INTO Vehicles VALUES('56KZNS', 'Ford', 'EcoSport', 2018, 'White', 5499, 'available', 'SUV', '1601 3rd Ave', 'Seattle');
INSERT INTO Vehicles VALUES('861AJP', 'Jeep', 'Grand Cherokee', 2017, 'White', 55281, 'available', 'SUV', '9051 Beckwith Rd', 'Richmond');
INSERT INTO Vehicles VALUES('CMV5RB', 'Lincoln', 'MKC', 2017, 'Brown', 58767, 'available', 'SUV', '9051 Beckwith Rd', 'Richmond');
INSERT INTO Vehicles VALUES('329RBL', 'Ford', 'F150 Super Crew', 2016, 'Black', 76461, 'rented', 'Truck', '10376 King George Blvd', 'Surrey');
INSERT INTO Vehicles VALUES('OKYZ6T', 'Nissan', 'Titan', 2018, 'White', 42, 'available', 'Truck', '3090 Westwood St', 'Port Coquitlam');
INSERT INTO Vehicles VALUES('DXR7LD', 'Toyota', 'Tacoma', 2017, 'White', 23060, 'available', 'Truck', '3090 Westwood St', 'Port Coquitlam');
INSERT INTO Vehicles VALUES('9JX1NV', 'Ford', 'F-150', 2017, 'Red', 37800, 'available', 'Truck', '1321 3rd Ave', 'New Westminster');
INSERT INTO Vehicles VALUES('N0C6RY', 'Volkswagen', 'New Beetle', 2003, 'Black', 197500, 'maintenance', 'Compact', '180 W Georgia St', 'Vancouver');
INSERT INTO Vehicles VALUES('0MB5OX', 'Chrysler', 'Neon', 2002, 'Blue', 118128, 'maintenance', 'Compact', '1321 3rd Ave', 'New Westminster');
INSERT INTO Vehicles VALUES('LGH8N7', 'Hyundai', 'Sonata', 2012, 'Grey', 141260, 'rented', 'Full-size', '751 Marine Dr', 'North Vancouver');

--cellphone, name, address, dlicense
INSERT INTO Customers VALUES('6136624852', 'Jose L Kappel', '2202 Pitt St, Cornwall, ON K6J3R2', 'NHL12506717');
INSERT INTO Customers VALUES('5718610645', 'Marina J Rojas', '418 Blanshard St, Victoria, BC V8W2H9', 'WDLJKMN580GF');
INSERT INTO Customers VALUES('9897069420', 'Brian C Mares', '110 Rene-Levesque Blvd, Montreal, QC H3B4W8', 'TP102445F');
INSERT INTO Customers VALUES('8922249372', 'Marilyn G Cheney', '3943 Elgin St, Cardinial, ON K0E1E0', 'P14245587924');
INSERT INTO Customers VALUES('7012557073', 'Dolores R Karle', '3164 King St, Chester, NS B0J1J0', 'WDLBCDF789GK');

--confNo, vtname, dlicense, fromDateTime, toDateTime
INSERT INTO Reservations VALUES(0, 'Truck', 'TP102445F', TO_TIMESTAMP('2019-11-15 09:30', 'YYYY-MM-DD HH24:MI'), TO_TIMESTAMP('2019-11-15 17:30', 'YYYY-MM-DD HH24:MI'));
INSERT INTO Reservations VALUES(1, 'Truck', 'NHL12506717', TO_TIMESTAMP('2019-11-20 08:30', 'YYYY-MM-DD HH24:MI'), TO_TIMESTAMP('2019-11-29 17:10', 'YYYY-MM-DD HH24:MI'));
INSERT INTO Reservations VALUES(2, 'Compact', 'WDLBCDF789GK', TO_TIMESTAMP('2019-12-14 17:45', 'YYYY-MM-DD HH24:MI'), TO_TIMESTAMP('2019-12-28 13:10', 'YYYY-MM-DD HH24:MI'));
INSERT INTO Reservations VALUES(3, 'SUV', 'P14245587924', TO_TIMESTAMP('2019-12-06 11:30', 'YYYY-MM-DD HH24:MI'), TO_TIMESTAMP('2019-12-08 13:00', 'YYYY-MM-DD HH24:MI'));

--rid, vlicense, dlicense, fromDateTime, toDateTime, odometer, cardName, cardNo, ExpDate, confNo
INSERT INTO Rentals
VALUES(
    10, '329RBL', 'TP102445F', TO_TIMESTAMP('2019-11-15 09:30', 'YYYY-MM-DD HH24:MI'), TO_TIMESTAMP('2019-11-15 17:30', 'YYYY-MM-DD HH24:MI'),
    76449, 'MasterCard', '5107317100135176', TO_DATE('2022-03', 'YYYY-MM'), 0
);
INSERT INTO Rentals
VALUES(
    11, '329RBL', 'NHL12506717', TO_TIMESTAMP('2019-11-20 08:30', 'YYYY-MM-DD HH24:MI'), TO_TIMESTAMP('2019-11-29 17:10', 'YYYY-MM-DD HH24:MI'),
    76461, 'Visa', '4485825952861926', TO_DATE('2022-10', 'YYYY-MM'), 1
);

--rid, returnDateTime, odometer, fullTank, value 
INSERT INTO Returns
VALUES(10, TO_TIMESTAMP('2019-11-15 17:30', 'YYYY-MM-DD HH24:MI'), 76461, 1, 380.00);

/*
insert into Vehicle values(060839453,'ka385r', 'Honda','Civic','2017','Grey', 21902, 'ForRent', 'Economy', '3274  Silver St','Vancouver');
insert into Vehicle values(112348546,'SP323z', 'Honda','Civic','2000','White', 83100, 'ForRent', 'Economy', '3493  Reserve St','New York');
insert into Vehicle values(115987938,'7f9693', 'Honda','Civic','2012','Grey', 31199, 'ForRent', 'Economy', '3493  Reserve St','New York');
insert into Vehicle values(132977562,'Oe699f', 'Honda','Accord','2015','Black', 40967, 'ForSale', 'Mid-size', '3274  Silver St','Vancouver');
insert into Vehicle values(998085667,'Qw332e', 'Honda','Accord','2013','White', 32010, 'ForRent', 'Mid-size','3493  Reserve St','New York');
insert into Vehicle values(119054004,'8q664C', 'Honda','Accord','2014','Dark Grey', 39778, 'ForRent', 'Mid-size', '3493  Reserve St','New York');
insert into Vehicle values(099354543,'rE988z','Toyota','Camry','2017','Black', 67860, 'ForSale', 'Compact', '3274  Silver St','Vancouver');
insert into Vehicle values(351565322,'PD564M','Toyota','Camry','2011','White', 35790, 'ForRent', 'Compact', '3493  Reserve St','New York');
insert into Vehicle values(552455318,'Xq099D','Toyota','Camry','1980','Black', 89900, 'ForSale', 'Compact', '1579  Robson St','Seattle');
insert into Vehicle values(142519864,'Jm023R','Toyota','Camry','1997','Black', 12890, 'ForRent', 'Compact', '1579  Robson St','Seattle');
insert into Vehicle values(242518965,'jU4699','BMW','X5','2017','Black', 12890, 'ForRent', 'Full-size', '3274  Silver St','Vancouver');
insert into Vehicle values(141582651,'al270o','BMW','X3','2015','Black', 19809, 'ForRent', 'Full-size', '3493  Reserve St','New York');
insert into Vehicle values(011564812,'J4062C','BMW','X1','2013','Black', 12890, 'ForRent', 'Full-size', '1579  Robson St','Seattle');
insert into Vehicle values(254099823,'WR823Z','Mercedes','CLA','2013','White', 19023, 'ForRent', 'SUV', '3274  Silver St','Vancouver');
insert into Vehicle values(356187925,'EK986F','Mercedes','GLE','2018','Black', 21908, 'ForRent', 'SUV', '3493  Reserve St','New York');
insert into Vehicle values(638306188,'Et598y','Mercedes','G','2019','Black', 12890, 'ForRent', 'SUV', '1579  Robson St','Seattle');
insert into Vehicle values(461143749,'ux202C','Ford','Focus','2013','White', 19023, 'ForRent', 'Standard', '3274  Silver St','Vancouver');
insert into Vehicle values(632017492,'N9727G','Ford','Ranger Raptor','2018','Black', 21908, 'ForRent', 'Truck', '3493  Reserve St','New York');
insert into Vehicle values(972403973,'lH564L','Ford','Explorer','2013','Black', 21908, 'ForSale', 'Truck', '3493  Reserve St','New York');
insert into Vehicle values(638317664,'Ql302Z','Ford','S-Max','2019','Black', 12890, 'ForRent', 'Mid-size', '1579  Robson St','Seattle');
insert into Vehicle values(098860775,'oz102j','Ford','Edge','2019','Black', 12890, 'For Sale', 'SUV', '1579  Robson St','Seattle');
insert into Vehicle values(397999999,'0L0362','Nissan','Leaf','2013','White', 19023, 'ForRent', 'Compact', '3274  Silver St','Vancouver');
insert into Vehicle values(293233711,'7A295z','Nissan','Maxima','2018','Black', 21908, 'ForRent', 'Economy', '3493  Reserve St','New York');
insert into Vehicle values(973787096,'EB912A','Nissan','Atlas','2013','Black', 21908, 'ForSale', 'Standard', '3493  Reserve St','New York');
insert into Vehicle values(406879419,'TD659x','Nissan','Juke','2019','Black', 12890, 'ForRent', 'Mid-size', '1579  Robson St','Seattle');
insert into Vehicle values(060479703,'V0701I','Nissan','Pathfinder','2019','Black', 12890, 'For Sale', 'SUV', '1579  Robson St','Seattle');

insert into Customer values ('7467576546', 'Kevin', '4992  Walsh Street, Thunder Bay', 99683944);
insert into Customer values ('8867650280', 'Nancy', '179  Bay Street, Toronto', 80698940);
insert into Customer values ('648987580', 'Joan D Janes', '4196  King George Hwy, Surrey', 66205611);
*/
