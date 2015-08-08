package tbstudio.cute.popup.notifier.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import tbstudio.cute.popup.notifier.R;
import tbstudio.cute.popup.notifier.model.Song;


public class SongAdapter extends BaseAdapter {
 
    private Context _mcontext;
    private LayoutInflater mInflater;
 
    private List<Song> listSong;
 
    public SongAdapter(Context context, List<Song> tlistSong) {
 
        this._mcontext = context;
        this.mInflater = LayoutInflater.from(context);
 
        this.listSong = tlistSong;
    }
 
    @Override
    public int getCount() {
        int count = (listSong == null) ? 0 : listSong.size();
        return count;
    }
 
    @Override
    public Object getItem(int position) {
 
        return listSong.get(position);
    }
 
    @Override
    public long getItemId(int arg0) {
 
        return 0;
    }
 
    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = this.mInflater.inflate(R.layout.row_song, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView
                    .findViewById(R.id.txtTitle);
            holder.flSong = (FrameLayout) convertView.findViewById(R.id.flRoot);
            holder.btnPlay = (ImageView) convertView.findViewById(R.id.btnPlay);
 
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
         
        holder.txtTitle.setText(Html.fromHtml(listSong.get(position).getTitle()));
 
        if(listSong.get(position).isPlay()){
            holder.btnPlay.setVisibility(View.VISIBLE);
            holder.flSong.setBackgroundColor(Color.parseColor("#16a085"));
        }else{
            holder.btnPlay.setVisibility(View.GONE);
            holder.flSong.setBackgroundColor(Color.parseColor("#00000000"));
        }
                 
        return convertView;
    }
 
    public class ViewHolder {
 
        ImageView btnPlay;
        TextView txtTitle;
        FrameLayout flSong;
    }
 
}
