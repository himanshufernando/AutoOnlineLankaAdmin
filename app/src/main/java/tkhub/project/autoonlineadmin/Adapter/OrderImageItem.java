package tkhub.project.autoonlineadmin.Adapter;

/**
 * Created by Himanshu on 11/17/2016.
 */

public class OrderImageItem {

    public int imageId;
    public String imageurl;

    public OrderImageItem(int imageId, String imageurl) {
        this.imageId = imageId;
        this.imageurl = imageurl;
    }

    public OrderImageItem(String imageurl) {
        this.imageurl = imageurl;
    }
}
