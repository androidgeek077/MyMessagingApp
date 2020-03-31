package Models;

public class ContactusModel {
    String name, phone, comment;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ContactusModel() {
    }

    public ContactusModel(String name, String phone, String comment) {
        this.name = name;
        this.phone = phone;
        this.comment = comment;
    }
}
