package tkhub.project.autoonlineadmin.Servies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Himanshu on 11/13/2016.
 */

public class NetworkAvailable {

    Context con;
    public NetworkAvailable(Context c) {
        this.con=c;
    }

    public boolean CheckNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

}
