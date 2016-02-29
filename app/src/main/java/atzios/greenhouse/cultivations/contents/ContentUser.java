package atzios.greenhouse.cultivations.contents;

/**
 * ContentUser class
 * Contains all properties of the user
 * Created by Atzios Vasilis on 16/12/2014.
 */
public class ContentUser {
    private int id;
    private String username;
    private String password;
    private boolean usesPass;
    private String name;
    private String lastname;
    private String phone;
    private String address;

    public void UserContent() {
        id = 0 ;
        username = null;
        password = null;
        usesPass = false;
        name = null;
        lastname = null;
        phone = null;
        address = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isUsesPass() {
        return usesPass;
    }

    public void setUsesPass(boolean usesPass) {
        this.usesPass = usesPass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
