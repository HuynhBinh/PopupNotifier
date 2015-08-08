package tbstudio.cute.popup.notifier.utils;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextUtils;
import android.util.Log;

import tbstudio.cute.popup.notifier.R;
import tbstudio.cute.popup.notifier.model.CharacterPopup;
import tbstudio.cute.popup.notifier.model.Sender;
import tbstudio.cute.popup.notifier.model.Song;


public class GeneralMethod {

    public static boolean isAccessibilitySettingsOn(Context mContext) {
        int accessibilityEnabled = 0;
        final String service = "com.tbstudio.animals.popupnotifier/com.tbstudio.animals.popupnotifier.service.NotificationServiceUnder18";
        boolean accessibilityFound = false;
        try {
            accessibilityEnabled = Settings.Secure.getInt(mContext
                            .getApplicationContext().getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);

        } catch (SettingNotFoundException e) {
            Log.e("Asccessing",
                    "Error finding setting, default accessibility to not found: "
                            + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(
                ':');

        if (accessibilityEnabled == 1) {
            Log.v("Asccessing",
                    "***ACCESSIBILIY IS ENABLED*** -----------------");
            String settingValue = Settings.Secure.getString(mContext
                            .getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
                splitter.setString(settingValue);
                while (splitter.hasNext()) {
                    String accessabilityService = splitter.next();

                    Log.v("Asccessing",
                            "-------------- > accessabilityService :: "
                                    + accessabilityService);
                    if (accessabilityService.equalsIgnoreCase(service)) {
                        Log.v("Asccessing",
                                "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            Log.v("Asccessing", "***ACCESSIBILIY IS DISABLED***");
        }

        return accessibilityFound;
    }

    private boolean isNotificationListener(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        String enabledNotificationListeners = Settings.Secure.getString(
                contentResolver, "enabled_notification_listeners");
        String packageName = context.getPackageName();

        // check to see if the enabledNotificationListeners String contains our
        // package name
        if (enabledNotificationListeners == null
                || !enabledNotificationListeners.contains(packageName)) {
            return false;
        }
        return true;
    }

    public static boolean isAccessibilitySettingsOnNotifiListen(Context context) {

        String enabled = Settings.Secure.getString(context.getContentResolver(),
                "enabled_notification_listeners");

        if (enabled != null && Settings.Secure.getString(context.getContentResolver(),
                "enabled_notification_listeners").contains(
                context.getPackageName())) {
            return true;
        } else {
            // service is not enabled try to enabled by calling...
            return false;
        }
    }

    public static void markMessageRead(Context context, String number,
                                       String body) {

        Uri uri = Uri.parse("content://sms/inbox");
        Cursor cursor = context.getContentResolver().query(uri, null, null,
                null, null);
        try {

            while (cursor.moveToNext()) {
                if ((cursor.getString(cursor.getColumnIndex("address"))
                        .equals(number))
                        && (cursor.getInt(cursor.getColumnIndex("read")) == 0)) {
                    if (cursor.getString(cursor.getColumnIndex("body"))
                            .startsWith(body)) {
                        String SmsMessageId = cursor.getString(cursor
                                .getColumnIndex("_id"));
                        ContentValues values = new ContentValues();
                        values.put("read", true);
                        context.getContentResolver().update(
                                Uri.parse("content://sms/inbox"), values,
                                "_id=" + SmsMessageId, null);
                        return;
                    }
                }
            }
        } catch (Exception e) {
            Log.e("Mark Read", "Error in Read: " + e.toString());
        }
    }

    public static String getPath(Context context, String mode, String name, String category) {

        String path = "http://" + Constant.key + Constant.service + "/"
                + context.getResources().getString(R.string.actionfol) + "/"
                + mode + "/" + category + "/" + name + ".png";

        return path;
    }

    public static List<CharacterPopup> getListData(Context ctx,
                                                   String category, int BACKGROUND_SIZE) {
        List<CharacterPopup> lstPhoto = new ArrayList<CharacterPopup>();

        for (int i = 1; i <= BACKGROUND_SIZE; i++) {
            CharacterPopup photo = new CharacterPopup();
            photo.name = String.valueOf(i);
            photo.category = category;
            lstPhoto.add(photo);
        }

        return lstPhoto;
    }

    public static List<Song> getMusicsInternal(Context context) {
        List<Song> listSong = new ArrayList<Song>();

        ContentResolver contentResolver = context.getContentResolver();

        Uri uri = android.provider.MediaStore.Audio.Media.INTERNAL_CONTENT_URI;

        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if (cursor == null) {
            // query failed, handle error.
        } else if (!cursor.moveToFirst()) {
            // no media on the device
        } else {
            int titleColumn = cursor
                    .getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = cursor
                    .getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            int idSize = cursor
                    .getColumnIndex(android.provider.MediaStore.Audio.Media.SIZE);


            do {
                long thisId = cursor.getLong(idColumn);
                String thisTitle = cursor.getString(titleColumn); // title album
                long size = cursor.getLong(idSize);

                if (size > 30000) {
                    byte ptext[];
                    String value = "";
                    try {
                        ptext = thisTitle.getBytes("ISO-8859-1");
                        value = new String(ptext, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Song song = new Song(thisId, value, false, true);
                    listSong.add(song);
                }

            } while (cursor.moveToNext());
        }


        return listSong;
    }

    public static List<Song> getMusicsExtenal(Context context) {
        List<Song> listSong = new ArrayList<Song>();

        ContentResolver contentResolver = context.getContentResolver();

        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if (cursor == null) {
            // query failed, handle error.
        } else if (!cursor.moveToFirst()) {
            // no media on the device
        } else {
            int titleColumn = cursor
                    .getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = cursor
                    .getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            int idSize = cursor
                    .getColumnIndex(android.provider.MediaStore.Audio.Media.SIZE);


            do {
                long thisId = cursor.getLong(idColumn);
                String thisTitle = cursor.getString(titleColumn); // title album
                long size = cursor.getLong(idSize);

                if (size > 30000) {
                    byte ptext[];
                    String value = "";
                    try {
                        ptext = thisTitle.getBytes("ISO-8859-1");
                        value = new String(ptext, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Song song = new Song(thisId, value, false, false);
                    listSong.add(song);
                }

            } while (cursor.moveToNext());
        }

        return listSong;
    }

    public static Sender getAvatarContact(Context context, Sender sender) {

        String contactId = "";
        String name = "";
        InputStream input = null;
        // define the columns I want the query to return
        String[] projection = new String[]{
                ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.PhoneLookup._ID};

        // encode the phone number and build the filter URI
        Uri contactUri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(sender.contactNumber));

        // query time
        Cursor cursor = context.getContentResolver().query(contactUri,
                projection, null, null, null);

        if (cursor.moveToFirst()) {

            // Get values from contacts database:
            contactId = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.PhoneLookup._ID));
            name = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
            sender.name = name;
            // Get photo of contactId as input stream:
            Uri uri = ContentUris.withAppendedId(
                    ContactsContract.Contacts.CONTENT_URI,
                    Long.parseLong(contactId));
            input = ContactsContract.Contacts.openContactPhotoInputStream(
                    context.getContentResolver(), uri);

            Bitmap avatar1 = null;
            if (input != null) {
                Bitmap avatar = BitmapFactory.decodeStream(input);
                if (avatar != null) {
                    avatar1 = circleBitmap(avatar);

                    sender.avatar = avatar1;

                    return sender;
                }
            }

        }

        return sender;
    }

    public static Bitmap circleBitmap(Bitmap bitmapimg) {
        Bitmap output = Bitmap.createBitmap(bitmapimg.getWidth(),
                bitmapimg.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmapimg.getWidth(),
                bitmapimg.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmapimg.getWidth() / 2, bitmapimg.getHeight() / 2,
                bitmapimg.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmapimg, rect, rect, paint);
        return output;
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

}
