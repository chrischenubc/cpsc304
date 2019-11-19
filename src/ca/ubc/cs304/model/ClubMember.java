package ca.ubc.cs304.model;

/*
ClubMember (cellphone, points, fees)
• Represents: entity set ClubMember
• Foreign Keys:
o (cellphone) references Customer
• Additional constraints: none
 */
public class ClubMember {
    private final String cellphone;
    private final int points;
    private final int fees;

    public ClubMember(String cellphone, int points, int fees) {
        this.cellphone = cellphone;
        this.points = points;
        this.fees = fees;
    }

    public String getCellphone() {
        return cellphone;
    }

    public int getPoints() {
        return points;
    }

    public int getFees() {
        return fees;
    }
}
