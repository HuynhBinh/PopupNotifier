package tbstudio.cute.popup.notifier.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import tbstudio.cute.popup.notifier.R;
import tbstudio.cute.popup.notifier.model.CharacterPopup;
import tbstudio.cute.popup.notifier.utils.Constant;
import tbstudio.cute.popup.notifier.utils.GeneralMethod;


public class ThumbAdapter extends BaseAdapter
{

    private Context _mcontext;
    private LayoutInflater mInflater;

    private int columWidth = 90;

    private List<CharacterPopup> fileName;

    private DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    public ThumbAdapter(Context context, List<CharacterPopup> fileName,
                        int columnWidth)
    {
        this._mcontext = context;
        this.mInflater = LayoutInflater.from(context);
        this.columWidth = columnWidth;
        this.fileName = fileName;

        options = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY)
                .showImageOnLoading(R.drawable.loading).cacheInMemory(true).cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888).build();

    }

    @Override
    public int getCount()
    {
        int count = (fileName == null) ? 0 : fileName.size();
        return count;
    }

    public void setData(List<CharacterPopup> fileName)
    {
        this.fileName = fileName;

        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position)
    {
        return fileName.get(position);
    }

    @Override
    public long getItemId(int arg0)
    {

        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            convertView = this.mInflater.inflate(R.layout.row_category, null);
            holder = new ViewHolder();
            holder.roots = (LinearLayout) convertView.findViewById(R.id.root);
            holder.imageViews = (ImageView) convertView
                    .findViewById(R.id.imgThumb);
            LinearLayout.LayoutParams ln = new LinearLayout.LayoutParams(
                    columWidth, columWidth);
            holder.imageViews.setLayoutParams(ln);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == 0)
        {
            holder.roots.setPadding(0, 110, 0, 40);
        }
        else if (position == 1)
        {

            holder.roots.setPadding(0, 110, 0, 40);
        }
        else
        {

            holder.roots.setPadding(0, 40, 0, 40);
        }

        String uri = GeneralMethod.getPath(_mcontext, Constant.MODE_THUMB, fileName.get(position).name, fileName.get(position).category);
        imageLoader.displayImage(uri, holder.imageViews, options);

        return convertView;
    }

    public class ViewHolder
    {
        LinearLayout roots;
        ImageView imageViews;
    }

}
