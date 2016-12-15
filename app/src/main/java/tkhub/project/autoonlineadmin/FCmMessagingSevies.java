package tkhub.project.autoonlineadmin;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import tkhub.project.autoonlineadmin.Layout.Splash;


/**
 * Created by Himanshu on 10/8/2016.
 */

public class FCmMessagingSevies extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String titel =remoteMessage.getNotification().getTitle();
        String message =remoteMessage.getNotification().getBody();
        String dsd =remoteMessage.getNotification().getTag();
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);



        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(titel)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentText(message);

        Intent resultIntent = new Intent(this, Splash.class);


        TaskStackBuilder stackBuilder = null;
        PendingIntent resultPendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(Splash.class);

            stackBuilder.addNextIntent(resultIntent);
            resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        }else {

            resultPendingIntent= PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_ONE_SHOT);

        }



        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(0, mBuilder.build());



        super.onMessageReceived(remoteMessage);
    }
}
