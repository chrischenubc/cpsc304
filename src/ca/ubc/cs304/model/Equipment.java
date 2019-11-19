package ca.ubc.cs304.model;

/*
Equipment (eid, etname, status, location, city)
• Represents: entity set Equipment and relationship sets Ekeeps and E_is_of
• Foreign Keys:
o (location, city) references Branch
o (etname) references EquipType
• Other constraints:
o status can take the values ( available, rented, not_available)
 */

public class Equipment {
    private final String eid;
    private final String etname;
    private final String status;
    private final String location;
    private final String city;

    public Equipment(String eid, String etname, String status, String location, String city) {
        this.eid = eid;
        this.etname = etname;
        this.status = status;
        this.location = location;
        this.city = city;
    }

    public String getEid() {
        return eid;
    }

    public String getEtname() {
        return etname;
    }

    public String getStatus() {
        return status;
    }

    public String getLocation() {
        return location;
    }

    public String getCity() {
        return city;
    }
}
