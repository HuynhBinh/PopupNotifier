package tbstudio.cute.popup.notifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import tbstudio.cute.popup.notifier.model.Sender;
import tbstudio.cute.popup.notifier.utils.ButtonSquare;
import tbstudio.cute.popup.notifier.utils.Constant;
import tbstudio.cute.popup.notifier.utils.GeneralMethod;


public class CharacterDetailActivity extends BaseActivity implements OnClickListener
{

    private String category, name, path, localPath;
    private boolean isExist = false;
    private ButtonSquare btnSMS, btnSocial, btnOne, btnEveryOne;
    private ImageView imgCharacter;
    private LinearLayout lnlSMS, lnlMenu;
    static final int PICK_CONTACT_REQUEST = 1;
    private DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_detail);
        initialView();
        handleIntent();
    }

    private void initialView()
    {

        lnlSMS = (LinearLayout) findViewById(R.id.lnlSMS);
        lnlMenu = (LinearLayout) findViewById(R.id.lnlMenu);

        imgCharacter = (ImageView) findViewById(R.id.imgCharacter);
        btnSMS = (ButtonSquare) findViewById(R.id.btnSMS);
        btnSocial = (ButtonSquare) findViewById(R.id.btnSocial);
        btnOne = (ButtonSquare) findViewById(R.id.btnOne);
        btnEveryOne = (ButtonSquare) findViewById(R.id.btnEveryOne);
        btnSMS.setOnClickListener(this);
        btnSocial.setOnClickListener(this);
        btnOne.setOnClickListener(this);
        btnEveryOne.setOnClickListener(this);

        options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY).showImageOnLoading(R.drawable.loading).cacheInMemory(true).bitmapConfig(Bitmap.Config.ARGB_8888).build();

    }

    private void handleIntent()
    {
        category = getIntent().getExtras().getString("category");
        name = getIntent().getExtras().getString("name");
        path = GeneralMethod.getPath(this, Constant.MODE_THUMB, name, category);


        if (checkExist(this, name))
        {
            isExist = true;
            localPath = dataPath(this) + "/" + category + "/" + name + ".png";
            imageLoader.displayImage("file://" + localPath, imgCharacter, options);
        }
        else
        {
            imageLoader.displayImage(path, imgCharacter, options);
            if (isNetworkConnected())
            {
                new SavePhotoOrginalDobackground().execute();
            }
            else
            {
                Toast.makeText(this, "Network is not connected.Please check again", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public boolean checkExist(Context context, String name)
    {

        String str5 = dataPath(context) + category + "/" + name + ".png";
        File localFile6 = new File(str5);

        if (localFile6.exists())
        {
            return true;
        }
        return false;
    }

    public static String dataPath(Context context)
    {
        return new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())).append("/Android/data/").append(context.getPackageName()).toString() + "/tmp/";
    }

    public class SavePhotoOrginalDobackground extends AsyncTask<String, String, String>
    {

        boolean isSuccess = false;

        @Override
        protected void onPreExecute()
        {
            isSuccess = false;
        }

        @Override
        protected String doInBackground(String... arg0)
        {

            saveImage();

            return "";
        }

        @Override
        protected void onPostExecute(String result)
        {
            if (isSuccess)
            {
                isExist = true;
            }
            else
            {
                Toast.makeText(CharacterDetailActivity.this, "Initial data can not load. Please check your network.", Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected void onCancelled()
        {
            super.onCancelled();
        }

        private void saveImage()
        {

            String prefixCloud = path;

            try
            {

                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(prefixCloud);
                HttpResponse response = httpClient.execute(httpget);
                HttpEntity entity = response.getEntity();

                String root = dataPath(CharacterDetailActivity.this);
                File myDir = new File(root + category);
                myDir.mkdirs();
                String fname = name + ".png";
                File file = new File(myDir, fname);

                if (file.exists())
                    file.delete();
                try
                {
                    FileOutputStream out = new FileOutputStream(file);
                    InputStream inputStream = entity.getContent();

                    // create a buffer...
                    byte[] buffer = new byte[1024];
                    int bufferLength = 0; // used to store a temporary size of
                    // the
                    // buffer

                    // now, read through the input buffer and write the contents
                    // to
                    // the file
                    while ((bufferLength = inputStream.read(buffer)) > 0)
                    {
                        // add the data in the buffer to the file in the file
                        // output
                        // stream (the file on the sd card
                        out.write(buffer, 0, bufferLength);
                        // add up the size so we know how much is downloaded

                    }
                    // close the output stream when done
                    out.close();
                    isSuccess = true;
                    localPath = file.getPath();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private void pickContact()
    {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(Phone.CONTENT_TYPE);
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnSMS:
                lnlMenu.setVisibility(View.GONE);
                final Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
                final Animation animationFadeout = AnimationUtils.loadAnimation(this, R.anim.fadeout);
                //lnlMenu.startAnimation(animationFadeout);
                lnlSMS.startAnimation(animationFadeIn);

                lnlSMS.setVisibility(View.VISIBLE);
                break;
            case R.id.btnSocial:
                Intent intent = new Intent(CharacterDetailActivity.this, SocialAppActivity.class);
                intent.putExtra("path", localPath);
                startActivity(intent);
                break;
            case R.id.btnOne:
                pickContact();
                break;
            case R.id.btnEveryOne:
                setSender(Constant.everyOne);
                break;

            default:
                break;
        }

    }

    private void setSender(String number)
    {

        List<Sender> lstSender = Sender.listAll(Sender.class);
        String packageSms = "package for sms";
        boolean isExist = false;

        for (Sender sender : lstSender)
        {

            if (PhoneNumberUtils.compare(number, sender.contactNumber))
            {
                sender.contactNumber = number;
                sender.imagePath = localPath;
                sender.packageName = packageSms;
                sender.save();
                isExist = true;
                break;
            }
        }

        if (!isExist)
        {
            Sender sender = new Sender();
            sender.contactNumber = number;
            sender.imagePath = localPath;
            sender.packageName = packageSms;
            sender.isSocial = false;
            sender.save();
        }

        Intent intent = new Intent(this, AnimationActivity.class);
        intent.putExtra("number", number);
        intent.putExtra("isSocial", false);
        intent.putExtra("path", localPath);
        startActivity(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        // Check which request it is that we're responding to
        if (requestCode == PICK_CONTACT_REQUEST)
        {
            // Make sure the request was successful
            if (resultCode == RESULT_OK)
            {
                // Get the URI that points to the selected contact
                Uri contactUri = data.getData();
                // We only need the NUMBER column, because there will be only
                // one row in the result
                String[] projection = {Phone.NUMBER};

                // Consider using CursorLoader to perform the query.
                Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);
                cursor.moveToFirst();

                // Retrieve the phone number from the NUMBER column
                int column = cursor.getColumnIndex(Phone.NUMBER);
                String number = cursor.getString(column);

                setSender(number);


            }

        }
    }

}
