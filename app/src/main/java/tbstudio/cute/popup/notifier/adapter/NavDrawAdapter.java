package tbstudio.cute.popup.notifier.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tbstudio.cute.popup.notifier.R;


public class NavDrawAdapter extends BaseAdapter
{

    private Context _mcontext;
    private LayoutInflater mInflater;
    private String menuName[] = {
            "General", "Desert", "Jungle", "Farm",
            "Forest"
    };
    private int menuIcon[] = {
            R.drawable.g_cao, R.drawable.g_catwhite,
            R.drawable.g_cow, R.drawable.g_donkey,
            R.drawable.g_cow
    };

    public NavDrawAdapter(Context context)
    {
        this._mcontext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount()
    {
        int count = (this.menuName == null) ? 0 : this.menuName.length;
        return count;
    }

    @Override
    public Object getItem(int position)
    {
        return this.menuName[position];
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    public void setNotifyDataChange()
    {
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;

        if (convertView == null)
        {
            convertView = this.mInflater.inflate(R.layout.row_navdrawer, null);
            holder = new ViewHolder();
            holder.roots = (RelativeLayout)convertView.findViewById(R.id.root);
            holder.txtMenuName = (TextView) convertView
                    .findViewById(R.id.title);
            holder.imgIcon = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }



        holder.txtMenuName.setText(menuName[position]);
        holder.imgIcon.setImageResource(menuIcon[position]);
        return convertView;
    }

    public class ViewHolder
    {
        RelativeLayout roots;
        ImageView imgIcon;
        TextView txtMenuName;
    }

}
