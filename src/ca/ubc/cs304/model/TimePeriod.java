package ca.ubc.cs304.model;

/*
TimePeriod(fromDate, fromTime, toDate, toTime)
• Represents: the entity set TimePeriod
• Additional constraints: none
 */
public class TimePeriod {
    private final String fromDate;
    private final String fromTime;
    private final String toDate;
    private final String toTime;

    public TimePeriod(String fromDate, String fromTime, String toDate, String toTime) {
        this.fromDate = fromDate;
        this.fromTime = fromTime;
        this.toDate = toDate;
        this.toTime = toTime;
    }

    public String getFromDate() {
        return fromDate;
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
}
