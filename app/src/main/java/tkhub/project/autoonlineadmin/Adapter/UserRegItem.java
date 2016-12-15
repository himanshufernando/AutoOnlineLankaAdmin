package tkhub.project.autoonlineadmin.Adapter;

/**
 * Created by Himanshu on 11/17/2016.
 */

public class UserRegItem {

    public String userName;
    public String email;
    public String phone;
    public String nic;
    public String sex;
    public String date;


    public UserRegItem(String userName, String email, String phone, String nic, String sex, String date) {
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.nic = nic;
        this.sex = sex;
        this.date = date;
    }


    public UserRegItem(String phone, String userName) {
        this.phone = phone;
        this.userName = userName;
    }
}
