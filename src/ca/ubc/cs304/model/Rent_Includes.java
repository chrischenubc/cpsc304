package ca.ubc.cs304.model;

/*
Rent_Includes (rid, eid)
• Represents: the relationship set Includes
• Foreign Keys:
o (rid) references Rent
o (eid) references Equipment
• Additional constraints: none
 */
public class Rent_Includes {
    private final String rid;
    private final String eid;

    public Rent_Includes(String rid, String eid) {
        this.rid = rid;
        this.eid = eid;
    }

    public String getRid() {
        return rid;
    }

    public String getEid() {
        return eid;
    }
}
