package tbstudio.cute.popup.notifier.service;



import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import tbstudio.cute.popup.notifier.MainActivity;
import tbstudio.cute.popup.notifier.utils.BundleExtra;

public class NotificationServiceUnder18 extends AccessibilityService {

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {

		if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {

			
			Intent intentMain = new Intent(this, MainActivity.class);
			intentMain.putExtra("package", event.getPackageName());
			intentMain.putExtra("text", event.toString() );
			intentMain.putExtra(BundleExtra.MSG_SOCIAL, true);
			startActivity(intentMain);
			
			Intent msgrcv = new Intent("Msg");
			msgrcv.putExtra("package", event.getPackageName());
			msgrcv.putExtra("text", event.toString());
			msgrcv.putExtra(BundleExtra.MSG_SOCIAL, true);
			LocalBroadcastManager.getInstance(this).sendBroadcast(msgrcv);
			
			
			Log.e("notification: " + event.toString(),
					"notification: " + event.getPackageName());
		}
	}	

	@Override
	protected void onServiceConnected() {

		AccessibilityServiceInfo info = new AccessibilityServiceInfo();
		info.eventTypes = AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED;
		info.notificationTimeout = 100;
		info.feedbackType = AccessibilityEvent.TYPES_ALL_MASK;

		setServiceInfo(info);
	}

	@Override
	public void onInterrupt() {
		System.out.println("onInterrupt");
	}
}
