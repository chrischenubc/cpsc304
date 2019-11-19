package ca.ubc.cs304.model;

/*
Rent(rid, vid, cellphone, fromDate, fromTime, toDate, toTime, odometer, cardName, cardNo,
ExpDate, confNo)
• Represents: the entity set Rent and the relationship sets RecordRent and the
relationship sets IsRented, Signs, Rented_for, and Rent-Reserve
• Foreign Keys:
o (vid) references ForRent
o (cellphone) references Customer
o (fromDate, fromTime, toDate, toTime) references TimePeriod
o (confNo) references Reservation ; it can be null
• Additional constraints: none
 */
public class Rent {
    private final String rid;
    private final String vid;
    private final String cellphone;
    private final String fromData;
    private final String fromTime;
    private final String toDate;
    private final String toTime;
    private final String odometer;
    private final String cardName;
    private final String cardNo;
    private final String ExpDate;
    private final String confNo;

    public Rent(String rid, String vid, String cellphone, String fromData, String fromTime, String toDate, String toTime, String odometer, String cardName, String cardNo, String expDate, String confNo) {
        this.rid = rid;
        this.vid = vid;
        this.cellphone = cellphone;
        this.fromData = fromData;
        this.fromTime = fromTime;
        this.toDate = toDate;
        this.toTime = toTime;
        this.odometer = odometer;
        this.cardName = cardName;
        this.cardNo = cardNo;
        ExpDate = expDate;
        this.confNo = confNo;
    }

    public String getRid() {
        return rid;
    }

    public String getVid() {
        return vid;
    }

    public String getCellphone() {
        return cellphone;
    }

    public String getFromData() {
        return fromData;
    }

    public String getFromTime() {
        return fromTime;
    }

    public String getToDate() {
        return toDate;
    }

    public String getToTime() {
        return toTime;
    }

    public String getOdometer() {
        return odometer;
    }

    public String getCardName() {
        return cardName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public String getExpDate() {
        return ExpDate;
    }

    public String getConfNo() {
        return confNo;
    }
}
