-------------------------------------------------------------------------------
-- CPSC304 Introduction to Relational Databases
-- Project Part 3: Implementation
-- Group 60 (Ziyu Xie, Yu Chen, and Annie Kim)
--
-- initDB.sql
-- creates tables for use
-------------------------------------------------------------------------------
SET SQLBLANKLINES ON

-- Drop existing tables
BEGIN
    EXECUTE IMMEDIATE 'DROP TABLE Branches CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE VehicleTypes CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE Vehicles CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE Customers CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE Reservations CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE Rentals CASCADE CONSTRAINTS';
    EXECUTE IMMEDIATE 'DROP TABLE Returns CASCADE CONSTRAINTS';
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
    wirate NUMBER(9,2),
    dirate NUMBER(9,2),
    hirate NUMBER(9,2),
    krate NUMBER(9,2),

    PRIMARY KEY(vtname)
);

CREATE TABLE Vehicles(
    -- vid has been removed as a vehicle is identified by its plate number
    vlicense VARCHAR2(255),
    make VARCHAR2(255),
    model VARCHAR2(255),
    year VARCHAR2(255),
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
    name    VARCHAR2(255),
    address VARCHAR2(255),
    dlicense VARCHAR2(255),

    PRIMARY KEY(dlicense)
);

CREATE TABLE Reservations(
    confNo NUMBER(9,0),
    vtname VARCHAR2(255),
    cellphone VARCHAR2(255),
    fromDate DATE,
    fromTime TIMESTAMP,
    toDate DATE,
    toTime TIMESTAMP,

    PRIMARY KEY(confNo),

    FOREIGN KEY(vtname)
        REFERENCES VehicleTypes
        ON DELETE CASCADE,
    FOREIGN KEY(cellphone)
        REFERENCES Customers
        ON DELETE CASCADE
);

CREATE TABLE Rentals(
    rid NUMBER(9,0),
    vlicense VARCHAR2(255),
    cellphone VARCHAR2(255),
    fromDate DATE,
    fromTime TIMESTAMP,
    toDate DATE,
    toTime TIMESTAMP,
    odometer NUMBER(9,0),
    cardName VARCHAR2(255),
    cardNo VARCHAR2(255),
    ExpDate Date,
    confNo NUMBER(9,0),

    PRIMARY KEY(rid),

    FOREIGN KEY(vlicense)
        REFERENCES Vehicles
        ON DELETE CASCADE,
    FOREIGN KEY(cellphone)
        REFERENCES Customers
        ON DELETE CASCADE,
    FOREIGN KEY(confNo)
        REFERENCES Reservations
        ON DELETE CASCADE
);

CREATE TABLE Returns(
    rid NUMBER(9,0),
    returnDate DATE,
    returnTime TIMESTAMP,
    odometer NUMBER(9,0),
    fullTank NUMBER(1),
    value NUMBER(9,2),

    PRIMARY KEY(rid),

    FOREIGN KEY(rid)
        REFERENCES Rentals
);


/*
insert into Branch values('3274  Silver St','Vancouver');
insert into Branch values('3493  Reserve St','New York');
insert into Branch values('1579  Robson St','Seattle');
insert into Branch values('194  Yonge Street','Richmond');
insert into Branch values('280  Nelson Street','Burnaby');

insert into VehicleType values('Economy','lightweight, and inexpensive', 600, 100, 10, 300, 50, 5, 10);
insert into VehicleType values('Compact','small and compact', 400, 80, 8, 300, 40, 8, 8);
insert into VehicleType values('Mid-size','mid size vehicle ideally for 4 people', 700, 120, 12, 350, 70, 12, 15);
insert into VehicleType values('Standard',' four to five people can sit comfortably', 700, 110, 15, 300, 60, 7, 12);
insert into VehicleType values('SUV','sport utility vehicle', 1000, 160, 25, 500, 80, 16, 14);
insert into VehicleType values('Full-size','bigger and more spacious', 800, 140, 20, 400, 60, 12, 6);
insert into VehicleType values('Truck','designed to carry freight or goods', 1200, 180, 30, 800, 100, 30, 15);

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
