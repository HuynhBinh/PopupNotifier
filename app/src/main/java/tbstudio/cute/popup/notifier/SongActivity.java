package tbstudio.cute.popup.notifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ContentUris;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import tbstudio.cute.popup.notifier.adapter.SongAdapter;
import tbstudio.cute.popup.notifier.model.Sender;
import tbstudio.cute.popup.notifier.model.Song;
import tbstudio.cute.popup.notifier.utils.GeneralMethod;


public class SongActivity extends BaseActivity
{

    private ListView lstSong;
    private MediaPlayer mMediaPlayer;
    private List<Song> lstSongs;
    private SongAdapter adapter;
    private Button btnCancel, btnOk;
    private ArrayList<String> lstPackage;
    private String number;
    private boolean isSocial;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_song);

        handleIntent();
        initialView();
    }

    private void handleIntent()
    {

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

    private void initialView()
    {
        lstSong = (ListView) findViewById(R.id.lstSong);
        lstSongs = new ArrayList<Song>();
        lstSongs.addAll(GeneralMethod.getMusicsInternal(this));
        lstSongs.addAll(GeneralMethod.getMusicsExtenal(this));
        Collections.sort(lstSongs);

        adapter = new SongAdapter(this, lstSongs);
        lstSong.setAdapter(adapter);

        lstSong.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3)
            {

                new Thread(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        if (mMediaPlayer != null)
                        {

                            if (mMediaPlayer.isPlaying())
                            {
                                mMediaPlayer.stop();
                                mMediaPlayer.release();
                                mMediaPlayer = null;
                            }
                        }

                        playSong(lstSongs.get(position).getId(), lstSongs.get(position).isIntenal());

                        for (Song s : lstSongs)
                        {
                            if (s.isPlay())
                            {
                                s.setPlay(false);
                                break;
                            }
                        }
                        lstSongs.get(position).setPlay(true);

                        runOnUiThread(new Runnable()
                        {

                            @Override
                            public void run()
                            {
                                adapter.notifyDataSetChanged();
                            }
                        });

                    }

                }).start();

            }
        });

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnOk = (Button) findViewById(R.id.btnOk);

        btnCancel.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        btnOk.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                buttonOk();
            }
        });

    }

    private void buttonOk()
    {

        for (int i = 0; i < lstSongs.size(); i++)
        {
            if (lstSongs.get(i).isPlay())
            {

                boolean internal = true;
                if (!lstSongs.get(i).isIntenal())
                {
                    internal = false;
                }

                if (isSocial)
                {
                    for (String pakage : lstPackage)
                    {
                        List<Sender> lstSender = Sender.find(Sender.class, "Package_Name=?", pakage);

                        if (lstSender != null && lstSender.size() > 0)
                        {
                            Sender sender = lstSender.get(0);
                            sender.soundID = lstSongs.get(i).getId();
                            sender.isInternal = internal;
                            sender.save();
                        }
                    }
                }
                else
                {
                    List<Sender> lstSender = Sender.find(Sender.class, "Is_Social=?", "0");

                    for (Sender sender : lstSender)
                    {

                        if (PhoneNumberUtils.compare(number, sender.contactNumber))
                        {
                            sender.contactNumber = number;
                            sender.soundID = lstSongs.get(i).getId();
                            sender.isInternal = internal;
                            sender.save();

                            break;
                        }
                    }

                }

                break;
            }
        }

        finish();
    }

    @Override
    protected void onPause()
    {
        // TODO Auto-generated method stub
        super.onPause();
        if (mMediaPlayer != null && mMediaPlayer.isPlaying())
        {
            mMediaPlayer.stop();
            mMediaPlayer = null;
        }
    }

    private void playSong(long id, boolean isInternal)
    {
        Uri contentUri;
        if (isInternal)
        {
            contentUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.INTERNAL_CONTENT_URI, id);
        }
        else
        {
            contentUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
        }

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try
        {
            mMediaPlayer.setDataSource(getApplicationContext(), contentUri);
            mMediaPlayer.prepare();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        mMediaPlayer.start();

        mMediaPlayer.setOnCompletionListener(new OnCompletionListener()
        {

            @Override
            public void onCompletion(MediaPlayer mp)
            {
                if (mMediaPlayer != null && mMediaPlayer.isPlaying())
                {
                    mMediaPlayer.stop();
                    mMediaPlayer.reset();
                    mMediaPlayer = null;
                }
            }
        });

    }

}