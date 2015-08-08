package tbstudio.cute.popup.notifier;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import tbstudio.cute.popup.notifier.model.Sender;
import tbstudio.cute.popup.notifier.utils.BundleExtra;
import tbstudio.cute.popup.notifier.utils.GeneralMethod;


public class MainActivity extends Activity {

    private ImageView btnRead;
    private ImageView btnClose, imgAvatar;
    private LinearLayout lnlRoot;
    private Sender senderTmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialView();
        //handlerSMS();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice,
                new IntentFilter("Msg"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onNotice);
    }

    private void handlerSMS() {
        if (!getIntent().getExtras().getBoolean(BundleExtra.MSG_SOCIAL)) {

            String MSG_FROM = getIntent().getExtras().getString(
                    BundleExtra.MSG_FROM);
            String MSG_CONTENT = getIntent().getExtras().getString(
                    BundleExtra.MSG_CONTENT);

            if (isSenderExist(MSG_FROM, "android.provider.Telephony.SMS_RECEIVED")) {
                List<Sender> lstSender = Sender.listAll(Sender.class);

                for (Sender sender : lstSender) {

                    if (PhoneNumberUtils.compare(MSG_FROM, sender.contactNumber)) {

                        senderTmp = GeneralMethod.getAvatarContact(this, sender);
                        senderTmp.content = MSG_CONTENT;
                        if (sender.avatar != null) {
                            imgAvatar.setImageBitmap(senderTmp.avatar);
                        } else {
                            imgAvatar.setImageResource(R.drawable.icon_default);
                        }

                        break;
                    }

                }

            }

        } else {

            List<Sender> lstSender = Sender.listAll(Sender.class);

            String packageApp = getIntent().getExtras().getString(
                    "package");
            String text = getIntent().getExtras().getString(
                    "text");
            String ticker = getIntent().getExtras().getString(
                    "ticker");
            String title = getIntent().getExtras().getString(
                    "title");


            for (Sender sender : lstSender) {
                if (sender.packageName.equals(packageApp)) {

                    senderTmp = sender;
                    senderTmp.content = (title != null) ? title + " " + text : text;

                    if (ticker != null)
                        senderTmp.name = ticker;
                    try {

                        Bitmap iconTmp = GeneralMethod.drawableToBitmap(getPackageManager().getApplicationIcon(packageApp));
                        Bitmap icon = GeneralMethod.circleBitmap(iconTmp);
                        senderTmp.avatar = icon;
                        if (icon != null) {
                            imgAvatar.setImageBitmap(icon);
                        } else {
                            imgAvatar.setImageResource(R.drawable.icon_default);
                        }

                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }

                    break;

                }
            }


        }
    }

    private boolean isSenderExist(String number, String packageName) {

        List<Sender> lstSender = Sender.find(Sender.class,
                "contactNumber = ? ", number);

        if (lstSender != null && lstSender.size() > 0) {
            return true;
        }

        return false;
    }

    private void initialView() {
        btnRead = (ImageView) findViewById(R.id.btnRead);
        btnClose = (ImageView) findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnRead.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        ReplyActivity.class);
                startActivity(intent);
                finish();
            }
        });
        lnlRoot = (LinearLayout) findViewById(R.id.lnlRoot);
        lnlRoot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgAvatar = (ImageView) findViewById(R.id.imgAvatar);

    }

    private BroadcastReceiver onNotice = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String pack = intent.getStringExtra("package");
            String title = intent.getStringExtra("title");
            String text = intent.getStringExtra("text");
            Log.e("dsadasddsa", pack + "\n" + title + "\n" + text);
        }
    };
}
