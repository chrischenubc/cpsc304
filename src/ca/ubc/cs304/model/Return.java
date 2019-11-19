package ca.ubc.cs304.model;

/*
Return(rid, date, time, odometer, fulltank, value)
• Represents: the weak entity set Return and its defining relationship set Return-Rent
• Foreign Keys:
o (rid) references Rent
• Additional constraints: none
 */
public class Return {
    private final String rid;
    private final String data;
    private final String time;
    private final String odometer;
    private final String fulltank;
    private final String value;

    public Return(String rid, String data, String time, String odometer, String fulltank, String value) {
        this.rid = rid;
        this.data = data;
        this.time = time;
        this.odometer = odometer;
        this.fulltank = fulltank;
        this.value = value;
    }

    public String getRid() {
        return rid;
    }

    public String getData() {
        return data;
    }

    public String getTime() {
        return time;
    }

    public String getOdometer() {
        return odometer;
    }

    public String getFulltank() {
        return fulltank;
    }

    public String getValue() {
        return value;
    }
}
