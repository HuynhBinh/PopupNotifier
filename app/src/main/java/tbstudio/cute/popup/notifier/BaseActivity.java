package tbstudio.cute.popup.notifier;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class BaseActivity extends AppCompatActivity
{

    public int columnWidth = 90, comlumWidth2Column;
    public float padding = 0;
    public static final int NUM_OF_COLUMNS = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        Resources r = getResources();
        padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0,
                r.getDisplayMetrics());
        columnWidth = (int) ((getScreenWidth() - ((NUM_OF_COLUMNS + 1) * padding)) / NUM_OF_COLUMNS);
        comlumWidth2Column = (int) ((getScreenWidth() - ((2 + 1) * padding)) / 2);
    }

    @TargetApi(16)
    protected void setBackgroundV16Plus(View view, Bitmap bitmap)
    {
        view.setBackground(new BitmapDrawable(getResources(), bitmap));

    }

    @SuppressWarnings("deprecation")
    protected void setBackgroundV16Minus(View view, Bitmap bitmap)
    {
        view.setBackgroundDrawable(new BitmapDrawable(bitmap));
    }

    public int getScreenWidth()
    {
        int columnWidth;
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try
        {
            display.getSize(point);
        }
        catch (NoSuchMethodError ignore)
        { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        return columnWidth;
    }

    protected Boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null)
        {
            return false;
        }
        else
            return true;
    }


}
