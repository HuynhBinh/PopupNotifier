package tbstudio.cute.popup.notifier.model;

import android.graphics.Bitmap;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

public class Sender extends SugarRecord<Sender> {

	public String contactNumber;
	public String imagePath;
	public long soundID;
	public String packageName;
	public int animationMode;
	public boolean isSocial;
	public boolean isInternal;
	public String content;
	@Ignore
	public Bitmap avatar;
	public String name;

	public Sender() {
	}

	public Sender(String contactNumber, String imagePath, long soundPath,
			String packageName, int animationMode,boolean isSocial, boolean isInternal) {
		this.contactNumber = contactNumber;
		this.imagePath = imagePath;
		this.soundID = soundPath;
		this.packageName = packageName;
		this.animationMode = animationMode;
		this.isSocial = isSocial;
		this.isInternal = isInternal;
	}
}
