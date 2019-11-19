package ca.ubc.cs304.model;

/*
Is_for(etname, vtname) (or we can give it a better name like EforV(etname, vtname) )
• Represents: relationships set Is_for
• Foreign Keys:
o (etname) references EquipType
o (vtname) references VehicleType
• Other constraints: none
 */
public class EforV {
    private final String etname;
    private final String vtname;

    public EforV(String etname, String vtname) {
        this.etname = etname;
        this.vtname = vtname;
    }

    public String getEtname() {
        return etname;
    }

    public String getVtname() {
        return vtname;
    }
}
