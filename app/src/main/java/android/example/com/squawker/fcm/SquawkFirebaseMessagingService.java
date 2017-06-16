package android.example.com.squawker.fcm;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.example.com.squawker.MainActivity;
import android.example.com.squawker.R;
import android.example.com.squawker.provider.SquawkContract;
import android.example.com.squawker.provider.SquawkProvider;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class SquawkFirebaseMessagingService extends FirebaseMessagingService {

    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        final Map<String, String> data = remoteMessage.getData();
        if (data != null) {
            final Intent intent = new Intent(this, MainActivity.class);
            final PendingIntent pendingIntent = PendingIntent
                    .getActivity(this, 0, intent, 0);
            final Notification notification = new NotificationCompat.Builder(this)
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(getText(R.string.app_name))
                    .setContentText(data.get("message"))
                    .setContentIntent(pendingIntent)
                    .build();
            final NotificationManagerCompat notificationManager
                    = NotificationManagerCompat.from(this);
            notificationManager.notify(NOTIFICATION_ID, notification);

            final ContentValues values = new ContentValues();
            for (Map.Entry<String, String> entry : data.entrySet()) {
                values.put(entry.getKey(), entry.getValue());
            }
            getContentResolver().insert(SquawkProvider.SquawkMessages.CONTENT_URI, values);
        }
    }
}
