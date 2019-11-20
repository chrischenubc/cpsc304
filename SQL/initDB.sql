drop table Branch cascade constraints;
drop table Customer cascade constraints;
drop table Rent cascade constraints;
drop table Reservation cascade constraints;
drop table Vehicle cascade constraints;
drop table VehicleType cascade constraints;
drop table Returns cascade constraints;

create table Branch(
                       location varchar2(30),
                       city varchar2(30),
                       primary key(location,city)
);

create table VehicleType(
                            vtname varchar2(30),
                            features varchar2(60),
                            wrate varchar2(30),
                            drate varchar2(30),
                            hrate varchar2(30),
                            wirate varchar2(30),
                            dirate varchar2(30),
                            hirate varchar2(30),
                            krate varchar2(30),
                            primary key(vtname)
);

create table Vehicle(
                        vid number(9,0),
                        vlicense varchar2(30),
                        make varchar2(30),
                        model varchar2(30),
                        year varchar2(4),
                        color varchar2(30),
                        odometer number(9,0),
                        status varchar2(30),
                        vtname varchar2(30),
                        location varchar2(30),
                        city varchar2(30),

                        primary key(vid),
                        foreign key(location, city) references Branch,
                        foreign key(vtname) references VehicleType
);

create table Customer(
                         cellphone varchar2(30) primary key,
                         name varchar2(30),
                         address varchar2(50),
                         dlicense number(8,0)
);

create table Reservation(
                            confNo number(10,0) primary key,
                            vtname varchar2(30),
                            cellphone varchar2(30),
                            fromDate Date,
                            fromTime Date,
                            toDate Date,
                            toTime Date,

                            foreign key(vtname) references VehicleType,
                            foreign key(cellphone) references Customer
);

create table Rent(
                     rid number(9,0) primary key,
                     vid number(9,0),
                     cellphone varchar2(30),
                     fromDate Date,
                     fromTime Date,
                     toDate Date,
                     toTime Date,
                     odometer number(9,0),
                     cardName varchar2(30),
                     cardNo varchar2(30),
                     ExpDate Date,
                     confNo number(10,0)  NOT NULL,
                     foreign key(vid) references Vehicle,
                     foreign key(cellphone) references Customer,
                     foreign key(confNo) references Reservation
);

create table Returns(
                        rid number(9,0) primary key,
                        returnDate Date,
                        odometer number,
                        fulltank varchar2(30),
                        value number,
                        foreign key(rid) references Rent
);


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