package tbstudio.cute.popup.notifier;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.github.clans.fab.FloatingActionButton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;


import java.util.ArrayList;

import tbstudio.cute.popup.notifier.utils.ButtonSquare;
import tbstudio.cute.popup.notifier.utils.GeneralMethod;

public class AnimationActivity extends BaseActivity implements OnClickListener
{

    private ButtonSquare btnAnimation, btnMusic;
    private int[] animationXML = {
            R.anim.rotate1, R.anim.rotate2, R.anim.move, R.anim.rotate
    };
    private Animation performAnimation;
    private ImageView imgCharacter;
    private ArrayList<String> lstPackage;
    private String number;
    private boolean isSocial;
    private int mode = 0;
    private String path;
    private DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private Toolbar toolbar;
    FloatingActionButton btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        options = new DisplayImageOptions.Builder().imageScaleType(ImageScaleType.EXACTLY).showImageOnLoading(R.drawable.loading).cacheInMemory(true).bitmapConfig(Bitmap.Config.ARGB_8888).build();

        handleIntent();
        initialView();
    }

    private void initialView()
    {
        btnAnimation = (ButtonSquare) findViewById(R.id.btnAnimation);
        btnMusic = (ButtonSquare) findViewById(R.id.btnMusic);
        btnAnimation.setOnClickListener(this);
        btnMusic.setOnClickListener(this);
        imgCharacter = (ImageView) findViewById(R.id.imgCharacter);
        imageLoader.displayImage("file://" + path, imgCharacter, options);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        btnDone = (FloatingActionButton)findViewById(R.id.btnDone);

        btnDone.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleComplete();
            }
        });

        if (toolbar != null)
        {
            toolbar.setTitle("Popup Notifier");
            setSupportActionBar(toolbar);
        }
    }

    private void handleIntent()
    {
        path = getIntent().getExtras().getString("path");
        isSocial = getIntent().getExtras().getBoolean("isSocial");

        if (isSocial)
        {
            lstPackage = getIntent().getStringArrayListExtra("packages");
        }
        else
        {
            number = getIntent().getExtras().getString("number");
        }

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnAnimation:
                showDialogOption();
                break;

            case R.id.btnMusic:
                Intent intent = new Intent(AnimationActivity.this, SongActivity.class);
                if (isSocial)
                {
                    intent.putStringArrayListExtra("packages", lstPackage);
                    intent.putExtra("isSocial", true);
                }
                else
                {
                    intent.putExtra("number", number);
                    intent.putExtra("isSocial", false);
                }

                startActivityForResult(intent, 2000);

                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.action_settings:
                handleComplete();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDialogOption()
    {

        CharSequence options[] = {
                "None", "Rotate", "Rotate 1", "Shake", "Move"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Option").setItems(options, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {

                mode = which;
                if (which != 0)
                {
                    performAnimation = AnimationUtils.loadAnimation(AnimationActivity.this, animationXML[which - 1]);
                    imgCharacter.startAnimation(performAnimation);
                }
                else
                {
                    imgCharacter.clearAnimation();
                }
            }
        });
        builder.create();
        builder.show();
    }

    private void handleComplete()
    {

        if (isSocial)
        {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.GINGERBREAD_MR1)
            {

                if (GeneralMethod.isAccessibilitySettingsOn(this))
                {
                    intentToFinalActivity();
                }
                else
                {
                    showDialog();
                }

            }
            else
            {
                if (GeneralMethod.isAccessibilitySettingsOnNotifiListen(this))
                {
                    intentToFinalActivity();
                }
                else
                {
                    showDialog();
                }
            }
        }
        else
        {
            intentToFinalActivity();
        }

    }

    private void intentToFinalActivity()
    {
        Intent intent = new Intent(this, FinalActivity.class);
        startActivity(intent);
    }

    private void showDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("To be able to monitor third party apps such as Facebook or WhatsApp, you have to enable notification access for Animal PopUp Notifier").setPositiveButton("Enable", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2)
                {
                    Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivityForResult(intent, 0);
                }
                else
                {
                    Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                    startActivity(intent);
                }
            }
        });

        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }


}
