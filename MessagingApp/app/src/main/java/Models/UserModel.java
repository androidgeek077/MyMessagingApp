package Models;

import java.io.Serializable;

public class UserModel implements Serializable {

    private String userid;

    public UserModel() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public UserModel(String userid, String name, String email, String password, String phone, String imageUrl, String usertype) {
        this.userid = userid;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        ImageUrl = imageUrl;
        this.usertype = usertype;
    }

    private String name;
    private String email;
    private String password;

    private String phone;
    private String ImageUrl;
    private String usertype;

}