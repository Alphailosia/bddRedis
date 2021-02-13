import java.util.List;

public class Customer {

    public String id;
    public String firstName;
    public String lastName;
    public String gender;
    public String birthday;
    public String creationDate;
    public String locationIP;
    public String browserUsed;
    public String place;

    public Customer(String id, String firstName, String lastName, String gender, String birthday, String creationDate, String locationIP, String browserUsed, String place) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthday = birthday;
        this.creationDate = creationDate;
        this.locationIP = locationIP;
        this.browserUsed = browserUsed;
        this.place = place;
    }

    @Override
    public String toString() {
        return "Customer{" + "\n" +
                "\tid='" + id + '\'' + ",\n" +
                "\tfirstName='" + firstName + '\'' + ",\n" +
                "\tlastName='" + lastName + '\'' + ",\n" +
                "\tgender='" + gender + '\'' + ",\n" +
                "\tbirthday='" + birthday + '\'' + ",\n" +
                "\tcreationDate='" + creationDate + '\'' + ",\n" +
                "\tlocationIP='" + locationIP + '\'' + ",\n" +
                "\tbrowserUsed='" + browserUsed + '\'' +",\n" +
                "\tplace='" + place + '\'' + "\n" +
                '}';
    }
}
