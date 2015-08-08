package tbstudio.cute.popup.notifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import tbstudio.cute.popup.notifier.adapter.SocialAdapter;
import tbstudio.cute.popup.notifier.model.Sender;
import tbstudio.cute.popup.notifier.model.Sticker;


public class SocialAppActivity extends BaseActivity implements OnClickListener {

	private GridView grvSocial;
	private SocialAdapter adapter;
	private Toolbar toolbar;
	private String localPath;
	private ArrayList<String> lstPackage;
	private List<Sticker> lstSticker;
	private Button btnSelectAll, btnUnSelectAll;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_social);
		initialIntent();
		initialView();
	}
	
	private void initialIntent(){
	
		localPath = getIntent().getExtras().getString("path");
	}

	private void initialView() {
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		grvSocial = (GridView) findViewById(R.id.grvSocial);
		lstSticker = getListApp();
		adapter = new SocialAdapter(this, lstSticker, comlumWidth2Column);
		grvSocial.setAdapter(adapter);

		if (toolbar != null) {
			toolbar.setTitle("Popup Notifier");
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}

		btnSelectAll = (Button) findViewById(R.id.btnSelectAll);
		btnUnSelectAll = (Button) findViewById(R.id.btnUnSelectAll);
		btnSelectAll.setOnClickListener(this);
		btnUnSelectAll.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		switch (id) {
		case R.id.action_settings:
			handleSocialSet();
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	private void handleSocialSet() {
		lstPackage = new ArrayList<String>();
		for (Sticker sticker : adapter.getSticker()) {

			if (sticker.getIsChecked()) {
				lstPackage.add(sticker.getPackageName());
				try {
					List<Sender> lstSender = Sender.find(Sender.class,
							"Package_Name=?", sticker.getPackageName());

					if (lstSender != null && lstSender.size() > 0) {
						Log.e("update", sticker.getAppName());
						Sender sender = lstSender.get(0);
						sender.imagePath = localPath;
						sender.save();
					} else {
						Log.e("new", sticker.getAppName());
						Sender sender = new Sender();
						sender.imagePath = localPath;
						sender.packageName = sticker.getPackageName();
						sender.contactNumber = sticker.getAppName();
						sender.isSocial = true;
						sender.save();
					}

				} catch (Exception e) {

				}

			}

		}

		if (lstPackage != null && lstPackage.size() > 0) {
			Intent intent = new Intent(this, AnimationActivity.class);
			intent.putStringArrayListExtra("packages", lstPackage);
			intent.putExtra("isSocial", true);
			intent.putExtra("path", localPath);
			startActivity(intent);
		}

	}

	private List<Sticker> getListApp() {
		List<Sticker> lstSticker = new ArrayList<Sticker>();

		final PackageManager pm = getPackageManager();
		// get a list of installed apps.
		List<ApplicationInfo> packages = pm
				.getInstalledApplications(PackageManager.GET_META_DATA);

		for (ApplicationInfo packageInfo : packages) {

			if (!isSystemPackage(packageInfo)) {
				Sticker sticker = new Sticker(packageInfo.packageName, pm
						.getApplicationLabel(packageInfo).toString(), true);
				lstSticker.add(sticker);
			}
		}

		Collections.sort(lstSticker);

		return lstSticker;

	}

	private boolean isSystemPackage(ApplicationInfo applicationInfo) {
		return ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSelectAll:
			setCheckPackage(true);
			break;
		case R.id.btnUnSelectAll:
			setCheckPackage(false);
			break;
		default:
			break;
		}
	}

	private void setCheckPackage(boolean checked) {
		if (lstSticker != null && lstSticker.size() > 0) {
			for (Sticker sticker : lstSticker) {
				sticker.setIsChecked(checked);
			}
			if (adapter != null)
				adapter.setData(lstSticker);
		}
	}
}
