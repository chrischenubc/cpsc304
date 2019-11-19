package ca.ubc.cs304.model;
/*
VehicleType(vtname, features, wrate, drate, hrate, wirate, dirate, hirate, krate))
• Represents: entity set VehicleType
• Constraints: none

primary keys: vtname
 */
public class VehicleType {
    private final String vtname;
    private final String features;
    private final String wrate;
    private final String drate;
    private final String hrate;
    private final String wirate;
    private final String dirate;
    private final String hirate;
    private final String krate;

    public VehicleType(String vtname, String features, String wrate, String drate, String hrate, String wirate, String dirate, String hirate, String krate) {
        this.vtname = vtname;
        this.features = features;
        this.wrate = wrate;
        this.drate = drate;
        this.hrate = hrate;
        this.wirate = wirate;
        this.dirate = dirate;
        this.hirate = hirate;
        this.krate = krate;
    }

    public String getVtname() {
        return vtname;
    }

    public String getFeatures() {
        return features;
    }

    public String getWrate() {
        return wrate;
    }

    public String getDrate() {
        return drate;
    }

    public String getHrate() {
        return hrate;
    }

    public String getWirate() {
        return wirate;
    }

    public String getDirate() {
        return dirate;
    }

    public String getHirate() {
        return hirate;
    }

    public String getKrate() {
        return krate;
    }
}
