package ca.ubc.cs304.model;

/*
EquipType(etname, drate, hrate)
• Represents: entity set EquipType
• Constraints: none
 */
public class EquipType {
    private final String etName;
    private final String drate;
    private final String hrate;

    public EquipType(String etName, String drate, String hrate) {
        this.etName = etName;
        this.drate = drate;
        this.hrate = hrate;
    }

    public String getEtName() {
        return etName;
    }

    public String getDrate() {
        return drate;
    }

    public String getHrate() {
        return hrate;
    }
}
