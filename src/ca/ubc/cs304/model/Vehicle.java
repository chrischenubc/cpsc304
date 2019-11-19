package ca.ubc.cs304.model;

/*
 Vehicle (vid, vlicense, make, model, year, color, odometer, status, vtname, location, city)
• Represents: entity set Vehicle and relationship sets Vkeeps and V_is_of
• Foreign Keys:
o (location, city) references Branch
o (vtname) references VehicleType
• Other constraints:
o status can take the values ( for_ rent, for_sale)
this will be used to define the ForSale and ForRent subsets.
• In this case the tables for the entity sets ForSale and ForRent will be defined as two
VIEWS:
 */
public class Vehicle {
    private final String vid;
    private final String vlicense;
    private final String make;
    private final String model;
    private final String year;
    private final String color;
    private final String odometer;
    private final String status;
    private final String vtname;
    private final String location;
    private final String city;

    public Vehicle(String vid, String vlicense, String make, String model, String year, String color, String odometer, String status, String vtname, String location, String city) {
        this.vid = vid;
        this.vlicense = vlicense;
        this.make = make;
        this.model = model;
        this.year = year;
        this.color = color;
        this.odometer = odometer;
        this.status = status;
        this.vtname = vtname;
        this.location = location;
        this.city = city;
    }

    public String getVid() {
        return vid;
    }

    public String getVlicense() {
        return vlicense;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getYear() {
        return year;
    }

    public String getColor() {
        return color;
    }

    public String getOdometer() {
        return odometer;
    }

    public String getStatus() {
        return status;
    }

    public String getVtname() {
        return vtname;
    }

    public String getLocation() {
        return location;
    }

    public String getCity() {
        return city;
    }
}
