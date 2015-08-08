package tbstudio.cute.popup.notifier.service;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;

import tbstudio.cute.popup.notifier.MainActivity;
import tbstudio.cute.popup.notifier.utils.BundleExtra;

public class SmsListener extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		if (intent.getAction()
				.equals("android.provider.Telephony.SMS_RECEIVED")) {
			Bundle bundle = intent.getExtras();// ---get the SMS message passed
												// in---
			SmsMessage[] msgs = null;
			String msg_from = "";
			String msgBody = "";
			if (bundle != null) {
				// ---retrieve the SMS message received---
				try {
					Object[] pdus = (Object[]) bundle.get("pdus");
					msgs = new SmsMessage[pdus.length];
					for (int i = 0; i < msgs.length; i++) {
						msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
						msg_from = msgs[i].getOriginatingAddress();
						msgBody = msgBody + msgs[i].getMessageBody();
					}

					Intent intentMain = new Intent(context, MainActivity.class);
					intentMain.putExtra(BundleExtra.MSG_FROM, msg_from);
					intentMain.putExtra(BundleExtra.MSG_CONTENT, msgBody);
					intentMain.putExtra("package", "android.provider.Telephony.SMS_RECEIVED");
					intentMain.putExtra(BundleExtra.MSG_SOCIAL, false);
					intentMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intentMain);
					LocalBroadcastManager.getInstance(context).sendBroadcast(intentMain);
				} catch (Exception e) {
					// Log.d("Exception caught",e.getMessage());
				}

			}
		}
	}

}