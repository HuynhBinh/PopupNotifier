package tbstudio.cute.popup.notifier;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by VuPhan on 6/2/15.
 */
public class LayoutStickerActivity extends Activity {
    private LinearLayout lnlRoot;
    private FrameLayout frameSticker;
    private Button btnLeft, btnCenter, btnRight;
    FrameLayout.LayoutParams params;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_design);


        params =  new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        frameSticker= (FrameLayout) findViewById(R.id.stickerFrame);

        btnLeft = (Button) findViewById(R.id.btnLeft);
        btnCenter = (Button) findViewById(R.id.btnCenter);
        btnRight = (Button) findViewById(R.id.btnRight);

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params.gravity = Gravity.LEFT;
                frameSticker.setLayoutParams(params);
            }
        });
        btnCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                params.gravity = Gravity.CENTER_HORIZONTAL;
                frameSticker.setLayoutParams(params);
            }
        });
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                params.gravity = Gravity.RIGHT;
                frameSticker.setLayoutParams(params);
            }
        });

    }
}
