package floo.com.mpm_mandiri.utils;

import android.app.Application;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.onesignal.OneSignal;

import org.json.JSONObject;

import floo.com.mpm_mandiri.adapter.DBHandler;
import floo.com.mpm_mandiri.adapter.Notifi;

public class OneSignalApplication extends Application {
   //DBHandler dbHandler;


   @Override
   public void onCreate() {
      super.onCreate();

      // Logging set to help debug issues, remove before releasing your app.
      OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.WARN);

      OneSignal.startInit(this)
          .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
          .setAutoPromptLocation(true)
          .init();
      OneSignal.enableNotificationsWhenActive(true);
      OneSignal.enableInAppAlertNotification(true);
      OneSignal.enableVibrate(true);
      OneSignal.enableSound(true);
      //OneSignal.promptLocation();
   }


   private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
      /**
       * Callback to implement in your app to handle when a notification is opened from the Android status bar or
       * a new one comes in while the app is running.
       * This method is located in this Application class as an example, you may have any class you wish implement NotificationOpenedHandler and define this method.
       *
       * @param message        The message string the user seen/should see in the Android status bar.
       * @param additionalData The additionalData key value pair section you entered in on onesignal.com.
       * @param isActive       Was the app in the foreground when the notification was received.
       */
      @Override
      public void notificationOpened(String message, JSONObject additionalData, boolean isActive) {
         String additionalMessage = "";
         Log.d("OneSignalExample", "message: " + message);
         Log.d("OneSignalExample", "additional data: " + additionalData);

         try {
            /*String strmsg = message;
            String strContent = additionalData.getString("title");
            Log.d("semua", strContent + " "+strmsg);

            Notifi notifi = new Notifi();
            notifi.setTITLE(message);
            notifi.setCONTENT(additionalData.getString("title"));
            dbHandler.addNotification(notifi);
            Log.d("semua", dbHandler.toString());
            if (additionalData != null) {
               if (additionalData.has("actionSelected"))
                  additionalMessage += "Pressed ButtonID: " + additionalData.getString("actionSelected");

               additionalMessage = message + "\nFull additionalData:\n" + additionalData.toString();
            }*/

         Log.d("OneSignalExample", "message:\n" + message + "\nadditionalMessage:\n" + additionalMessage);
         } catch (Throwable t) {
            t.printStackTrace();
         }
      }
   }
}
