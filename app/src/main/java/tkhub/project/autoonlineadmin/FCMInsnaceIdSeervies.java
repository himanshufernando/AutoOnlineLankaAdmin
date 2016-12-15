package tkhub.project.autoonlineadmin;

import android.os.AsyncTask;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONObject;




public class FCMInsnaceIdSeervies extends FirebaseInstanceIdService {

    String resToken;
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        resToken= FirebaseInstanceId.getInstance().getToken();
        System.out.println("Reg Token : "+resToken);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
