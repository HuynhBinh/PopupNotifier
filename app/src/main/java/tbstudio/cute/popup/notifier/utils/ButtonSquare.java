package tbstudio.cute.popup.notifier.utils;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.Button;

public class ButtonSquare extends Button {

	private final double mScale = 1.0;

	public ButtonSquare(Context context) {
		super(context);
		
	}

	public ButtonSquare(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);

		if (width > (int) ((mScale * height) + 0.5)) {
			width = (int) ((mScale * height) + 0.5);
		} else {
			height = (int) ((width / mScale) + 0.5);
		}

		super.onMeasure(
				MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
				MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
	}
}
