package tbstudio.cute.popup.notifier.service;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import tbstudio.cute.popup.notifier.MainActivity;
import tbstudio.cute.popup.notifier.utils.BundleExtra;

public class NotificationService extends NotificationListenerService {
	Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
	}

	@Override
	public void onNotificationPosted(StatusBarNotification sbn) {
		String pack = sbn.getPackageName();
		String ticker = sbn.getNotification().tickerText.toString();
		Bundle extras = sbn.getNotification().extras;
		String title = extras.getString("android.title");
		String text = extras.getCharSequence("android.text").toString();
		Log.e("Package", pack);
		Log.e("Ticker", ticker);
		Log.e("Title", title);
		Log.e("Text", text);
		Intent msgrcv = new Intent("Msg");
		msgrcv.putExtra("package", pack);
		msgrcv.putExtra("ticker", ticker);
		msgrcv.putExtra("title", title);
		msgrcv.putExtra("text", text);
		msgrcv.putExtra(BundleExtra.MSG_SOCIAL, true);
		LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
		
		Intent intentMain = new Intent(context, MainActivity.class);
		intentMain.putExtra("package", pack);
		intentMain.putExtra("ticker", ticker);
		intentMain.putExtra("title", title);
		intentMain.putExtra("text", text);
		intentMain.putExtra(BundleExtra.MSG_SOCIAL, true);
		startActivity(intentMain);
		
	}

	@Override
	public void onNotificationRemoved(StatusBarNotification sbn) {
		Log.i("Msg", "Notification Removed");
	}
}
