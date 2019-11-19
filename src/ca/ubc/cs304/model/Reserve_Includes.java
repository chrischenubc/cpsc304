package ca.ubc.cs304.model;

/*
Reserve_Includes (confNo, etname)
• Represents: the relationship set Is_forE
• Foreign Keys:
o (confNo) references Reservation
o (etname) references EquipType
• Additional constraints: none
 */
public class Reserve_Includes {
    private final String confNo;
    private final String etname;

    public Reserve_Includes(String confNo, String etname) {
        this.confNo = confNo;
        this.etname = etname;
    }

    public String getConfNo() {
        return confNo;
    }

    public String getEtname() {
        return etname;
    }
}
