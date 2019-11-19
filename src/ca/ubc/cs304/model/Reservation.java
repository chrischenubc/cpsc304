package ca.ubc.cs304.model;

/*
Reservation (confNo, vtname, cellphone, fromDate, fromTime, toDate, toTime)
• Represents: the entity set Reservation and the relationship sets Is_forV, Makes,
Made_for
• Foreign Keys:
o (vtname) references VehicleType
o (cellphone) references Customer
o ( fromDate, fromTime, toDate, toTime) references TimePeriod
• Additional constraints: none
 */
public class Reservation {
    private final String confNo;
    private final String vtname;
    private final String cellphone;
    private final String fromDate;
    private final String fromTime;
    private final String toData;
    private final String toTime;

    public Reservation(String confNo, String vtname, String cellphone, String fromDate, String fromTime, String toData, String toTime) {
        this.confNo = confNo;
        this.vtname = vtname;
        this.cellphone = cellphone;
        this.fromDate = fromDate;
        this.fromTime = fromTime;
        this.toData = toData;
        this.toTime = toTime;
    }

    public String getConfNo() {
        return confNo;
    }

    public String getVtname() {
        return vtname;
    }

    public String getCellphone() {
        return cellphone;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getFromTime() {
        return fromTime;
    }

    public String getToData() {
        return toData;
    }

    public String getToTime() {
        return toTime;
    }
}
