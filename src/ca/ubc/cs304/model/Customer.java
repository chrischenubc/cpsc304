package ca.ubc.cs304.model;


/*
Customer (cellphone, name, address, dlicense)
• Represents the entity set Customer
• Additional constraints: none
 */
public class Customer {
    private final String cellphone;
    private final String name;
    private final String address;
    private final String dlicense;

    public Customer(String cellphone, String name, String address, String dlicense) {
        this.cellphone = cellphone;
        this.name = name;
        this.address = address;
        this.dlicense = dlicense;
    }

    public String getCellphone() {
        return cellphone;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getDlicense() {
        return dlicense;
    }
}
