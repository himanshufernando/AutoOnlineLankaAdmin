package tkhub.project.autoonlineadmin.Adapter;

/**
 * Created by Himanshu on 11/17/2016.
 */

public class OrderItem {

    public int orderId;
    public int userID;
    public String userName;
    public int number;
    public String make;
    public String model;
    public String chassi;
    public int year;
    public String city;
    public String discription;
    public String date;
    public String status;


    public int usertype;
    public int approvedID;
    public String approve;

    public String regUserNumber;
    public int imageID;



    public OrderItem(int orderId, int userID, String userName, int number, String make, String model, String chassi, int year, String city, String discription, String date, String status, int usertype, int approvedID, String approve,String regUserNumber,int imID) {
        this.orderId = orderId;
        this.userID = userID;
        this.userName = userName;
        this.number = number;
        this.make = make;
        this.model = model;
        this.chassi = chassi;
        this.year = year;
        this.city = city;
        this.discription = discription;
        this.date = date;
        this.status = status;
        this.usertype = usertype;
        this.approvedID = approvedID;
        this.approve = approve;
        this.regUserNumber = regUserNumber;
        this.imageID=imID;
    }






}
