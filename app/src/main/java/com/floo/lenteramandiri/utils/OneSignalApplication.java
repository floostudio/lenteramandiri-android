package com.floo.lenteramandiri.utils;

import android.app.Application;
import android.util.Log;

import com.floo.lenteramandiri.utils.database.DBNotification;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import com.floo.lenteramandiri.adapter.Notifi;

public class OneSignalApplication extends Application {
   DBNotification dbNotification;



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

         dbNotification = new DBNotification(getApplicationContext());

         try {
            //Log.d("length", String.valueOf(message.length()));
            //Log.d("length", String.valueOf(additionalData.length()));
            Notifi notifi = new Notifi();
            notifi.setTITLE(message);
            notifi.setCONTENT(additionalData.getString("title"));
            notifi.setDATE((int) DataManager.dateToEpoch(DataManager.getDatesNow()));
            //notifi.setDATE((int)(DataManager.dateToEpoch(DataManager.getDatesNow())-(2678400+86400)));
            dbNotification.addNotification(notifi);
         } catch (Throwable t) {
            t.printStackTrace();
         }

         /*Notifi notifi = new Notifi();
         try {
            if (additionalData != null) {
               if (additionalData.has("actionSelected"))
                  additionalMessage += "Pressed ButtonID: " + additionalData.getString("actionSelected");


               additionalMessage = message + "\nFull additionalData:\n" + additionalData.toString();
            }
            notifi.setTITLE(message);
            notifi.setCONTENT(additionalData.getString("title"));
            dbNotification.addNotification(notifi);
         Log.d("OneSignalExample", "message:\n" + message + "\nadditionalMessage:\n" + additionalMessage);
         } catch (Throwable t) {
            t.printStackTrace();
         }*/
      }
   }
}
