package tbstudio.cute.popup.notifier.adapter;

import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import tbstudio.cute.popup.notifier.R;
import tbstudio.cute.popup.notifier.model.Sticker;


public class SocialAdapter extends BaseAdapter {

	private Context _mcontext;
	private LayoutInflater mInflater;

	private int columWidth = 90;

	private List<Sticker> fileName;

	private DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	public SocialAdapter(Context context, List<Sticker> fileName,
			int columnWidth) {
		this._mcontext = context;
		this.mInflater = LayoutInflater.from(context);
		this.columWidth = columnWidth;
		this.fileName = fileName;

	}

	@Override
	public int getCount() {
		int count = (fileName == null) ? 0 : fileName.size();
		return count;
	}

	public void setData(List<Sticker> fileName) {
		this.fileName = fileName;

		notifyDataSetChanged();
	}
	
	public List<Sticker> getSticker(){
		return fileName;
	}

	@Override
	public Object getItem(int position) {
		return fileName.get(position);
	}

	@Override
	public long getItemId(int arg0) {

		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = this.mInflater.inflate(R.layout.row_social, null);
			holder = new ViewHolder();
			holder.imageViews = (ImageView) convertView
					.findViewById(R.id.imgIcon);
			holder.txtAppName = (TextView) convertView
					.findViewById(R.id.txtAppName);
			holder.chkCheckBox = (CheckBox) convertView
					.findViewById(R.id.chkSocial);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		try {
			Drawable icon = _mcontext.getPackageManager().getApplicationIcon(
					fileName.get(position).getPackageName());
			holder.imageViews.setImageDrawable(icon);
			holder.txtAppName.setText(fileName.get(position).getAppName());

			holder.chkCheckBox
					.setChecked(fileName.get(position).getIsChecked());
			holder.chkCheckBox.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(holder.chkCheckBox.isChecked()){
						fileName.get(position).setIsChecked(true);
					}else{
						fileName.get(position).setIsChecked(false);
					}
				}
			});

		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return convertView;
	}

	public class ViewHolder {
		ImageView imageViews;
		TextView txtAppName;
		CheckBox chkCheckBox;
	}

}
