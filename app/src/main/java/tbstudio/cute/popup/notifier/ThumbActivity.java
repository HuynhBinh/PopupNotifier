package tbstudio.cute.popup.notifier;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;

import tbstudio.cute.popup.notifier.adapter.NavDrawAdapter;
import tbstudio.cute.popup.notifier.adapter.ThumbAdapter;
import tbstudio.cute.popup.notifier.model.CharacterPopup;
import tbstudio.cute.popup.notifier.utils.Constant;
import tbstudio.cute.popup.notifier.utils.GeneralMethod;


public class ThumbActivity extends BaseActivity implements AbsListView.OnScrollListener
{

    private List<CharacterPopup> lstCharacter;
    private GridView grvCharacter;
    private Toolbar toolbar;
    protected DrawerLayout drawerLayout;
    protected ActionBarDrawerToggle drawerToggle;
    protected ListView leftDrawerList;
    private ThumbAdapter tAdapter;


    public static final int state_dark = 0;
    public static final int state_half = 1;
    public static final int state_light = 2;

    int currentState = state_dark;

    boolean isOpenDrawer = false;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thumb);
        initialView();
        initialData(Constant.CATEGORY_GENERAL, Constant.BACK_GENERAL);

        if (GeneralMethod.isAccessibilitySettingsOnNotifiListen(this))
            showDialog();
    }

    private void initialView()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        grvCharacter = (GridView) findViewById(R.id.grvPhoto);
        grvCharacter.setOnScrollListener(this);
        grvCharacter.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(ThumbActivity.this, CharacterDetailActivity.class);
                intent.putExtra("name", lstCharacter.get(position).name);
                intent.putExtra("category", lstCharacter.get(position).category);
                startActivity(intent);
            }
        });

        if (toolbar != null)
        {
            toolbar.setTitle("Kute Popup");
            setSupportActionBar(toolbar);
        }

        initNavView();
        initDrawer();
    }

    private void initialData(String category, int background)
    {
        lstCharacter = GeneralMethod.getListData(this, category, background);
        if (tAdapter == null)
        {
            tAdapter = new ThumbAdapter(this, lstCharacter, columnWidth);
            grvCharacter.setAdapter(tAdapter);
        }
        else
        {
            tAdapter.setData(lstCharacter);
        }
    }

    protected void initNavView()
    {

        leftDrawerList = (ListView) findViewById(R.id.left_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        NavDrawAdapter adapter = new NavDrawAdapter(this);

        leftDrawerList.setAdapter(adapter);

        leftDrawerList.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                if (position == Constant.NAV_GENERAL)
                {
                    initialData(Constant.CATEGORY_GENERAL, Constant.BACK_GENERAL);
                }
                else if (position == Constant.NAV_FOREST)
                {
                    initialData(Constant.CATEGORY_FOREST, Constant.BACK_FOREST);
                }
                else if (position == Constant.NAV_DESERT)
                {
                    initialData(Constant.CATEGORY_DESERT, Constant.BACK_DESERT);
                }
                else if (position == Constant.NAV_FARM)
                {
                    initialData(Constant.CATEGORY_FARM, Constant.BACK_FARM);
                }
                else if (position == Constant.NAV_JUNGLE)
                {
                    initialData(Constant.CATEGORY_JUNGLE, Constant.BACK_JUNGLE);
                }
                drawerLayout.closeDrawers();
            }
        });

    }

    protected void initDrawer()
    {

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close)
        {

            @Override
            public void onDrawerClosed(View drawerView)
            {
                super.onDrawerClosed(drawerView);
                if (currentState == state_dark)
                {
                    toolbar.setBackgroundColor(getResources().getColor(R.color.color1));
                    toolbar.setTitle("Kute Popup");
                    currentState = state_dark;
                }
                if (currentState == state_half)
                {
                    toolbar.setBackgroundColor(getResources().getColor(R.color.color2));
                    toolbar.setTitle("Kute Popup");
                    currentState = state_half;
                }

                if (currentState == state_light)
                {
                    toolbar.setBackgroundColor(getResources().getColor(R.color.color4));
                    toolbar.setTitle("");
                    currentState = state_light;
                }

                isOpenDrawer = false;

            }

            @Override
            public void onDrawerOpened(View drawerView)
            {
                isOpenDrawer = true;

                if (currentState != state_dark)
                {
                    toolbar.setBackgroundColor(getResources().getColor(R.color.color1));
                    toolbar.setTitle("Kute Popup");
                }


                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (drawerToggle != null && drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        if (drawerToggle != null)
            drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        if (drawerToggle != null)
            drawerToggle.onConfigurationChanged(newConfig);
    }

    private void showDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("To be able to monitor third party apps such as Facebook or WhatsApp, you have to enable notification access for Animal PopUp Notifier").setPositiveButton("Enable", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2)
                {
                    Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivityForResult(intent, 0);
                }
                else
                {
                    Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                    startActivity(intent);
                }
            }
        }).setNegativeButton("Later", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                // User cancelled the dialog
            }
        });
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        if (isOpenDrawer == false)
        {
            Log.e("ABC", "" + firstVisibleItem + "-" + visibleItemCount + "-" + totalItemCount);
            String alpha = "";
            if (firstVisibleItem == 0)
            {
                toolbar.setBackgroundColor(getResources().getColor(R.color.color1));
                toolbar.setTitle("Kute Popup");
                currentState = state_dark;
            }
            if (firstVisibleItem == 2)
            {
                toolbar.setBackgroundColor(getResources().getColor(R.color.color2));
                toolbar.setTitle("Kute Popup");
                currentState = state_half;
            }

            if (firstVisibleItem == 4)
            {
                toolbar.setBackgroundColor(getResources().getColor(R.color.color4));
                toolbar.setTitle("");
                currentState = state_light;
            }
            if (firstVisibleItem >= 4)
            {
                toolbar.setBackgroundColor(getResources().getColor(R.color.color4));
                toolbar.setTitle("");
                currentState = state_light;
            }
        }


    }
}
