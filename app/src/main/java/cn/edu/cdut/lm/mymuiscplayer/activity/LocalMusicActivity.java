package cn.edu.cdut.lm.mymuiscplayer.activity;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.adapter.SingleSongAdapter;
import cn.edu.cdut.lm.mymuiscplayer.innerfragment.AlbumFragment;
import cn.edu.cdut.lm.mymuiscplayer.innerfragment.ArtistFragment;
import cn.edu.cdut.lm.mymuiscplayer.innerfragment.FolderFragment;
import cn.edu.cdut.lm.mymuiscplayer.innerfragment.SingleSongFragment;


/**
 * Created by LimiaoMaster on 2016/8/20 8:30
 */

public class LocalMusicActivity extends AppCompatActivity implements View.OnClickListener, Toolbar.OnMenuItemClickListener {

    private static final String TAG = "LocalMusicActivity";
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private SingleSongFragment singleSongFragment;
    private ArtistFragment singerFragment;
    private AlbumFragment albumFragment;
    private FolderFragment folderFragment;

    private List<String> tabNameList = new ArrayList<>(4);
    private List<Fragment> fragmentList = new ArrayList<>(4);

    private ImageView back;
    private NotificationManager manager;
    private static final int NOTIFICATION_ID = 5709;
    public static SingleSongAdapter singleSongAdapter;


    @Override
    protected void onNewIntent(Intent intent) {
        Log.e(TAG, "onNewIntent方法得到执行。");
        super.onNewIntent(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreate方法得到执行。");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_localmusic);
        // Title
        //Toolbar的setTitle方法要在setSupportActionBar(toolbar)之前调用，否则不起作用
        toolbar.setTitle("本地音乐");
        toolbar.setTitleTextColor(getResources().getColor(R.color.toolbarTextColor));
        setSupportActionBar(toolbar);
        // 上级按钮 (upbutton)
        //toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        // APP图标
        //toolbar.setLogo(android.R.drawable.ic_menu_edit);
        // SubTitle
        //toolbar.setSubtitle("次标题");

        ActionBar actionBar = getSupportActionBar();
        //actionBar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        //设置显示HomeAsUp图标。
        actionBar.setDisplayHomeAsUpEnabled(true);

        toolbar.setOnMenuItemClickListener(this);
        //设置title坐标箭头的响应
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initTabAndPager();
        initView();
    }

    private void initTabAndPager() {
        tabNameList.add("单曲");
        tabNameList.add("歌手");
        tabNameList.add("专辑");
        tabNameList.add("文件夹");
        if (singleSongFragment == null) {
            singleSongFragment = new SingleSongFragment();
            fragmentList.add(singleSongFragment);
        }
        if (singerFragment == null) {
            singerFragment = new ArtistFragment();
            fragmentList.add(singerFragment);
        }
        if (albumFragment == null) {
            albumFragment = new AlbumFragment();
            fragmentList.add(albumFragment);
        }
        if (folderFragment == null) {
            folderFragment = new FolderFragment();
            fragmentList.add(folderFragment);
        }
    }

    public void initView(){
        tabLayout = (TabLayout) findViewById(R.id.tab_localmusic);
        viewPager = (ViewPager) findViewById(R.id.view_pager_localmusic);
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        myPagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(myPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);

        /*back = (ImageView) findViewById(R.id.arrow);
        back.setOnClickListener(this);*/
    }



    /*@Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        return super.onMenuOpened(featureId,menu);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /**
         *add()方法的四个参数，依次是：
         * 1、组别，如果不分组的话就写Menu.NONE,
         * 2、Id，这个很重要，Android根据这个Id来确定不同的菜单
         * 3、顺序，那个菜单现在在前面由这个参数的大小决定
         * 4、文本，菜单的显示文本
         */
        /*menu.add(Menu.NONE, Menu.FIRST + 1, 5, "删除").setIcon(android.R.drawable.ic_menu_delete);
        menu.add(Menu.NONE, Menu.FIRST + 2, 2, "保存").setIcon(android.R.drawable.ic_menu_edit);
        menu.add(Menu.NONE, Menu.FIRST + 3, 6, "帮助").setIcon(android.R.drawable.ic_menu_help);
        menu.add(Menu.NONE, Menu.FIRST + 4, 1, "添加").setIcon(android.R.drawable.ic_menu_add);
        menu.add(Menu.NONE, Menu.FIRST + 5, 4, "详细").setIcon(android.R.drawable.ic_menu_info_details);
        menu.add(Menu.NONE, Menu.FIRST + 6, 3, "发送").setIcon(android.R.drawable.ic_menu_send);
        return true;*/
        // 為了讓 Toolbar 的 Menu 有作用，這邊的程式不可以拿掉

        getMenuInflater().inflate(R.menu.menu_local_music, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        String msg = "";
        switch (item.getItemId()) {
            case R.id.action_scan:
                msg += "扫描歌曲";
                break;
            case R.id.action_order:
                msg += "选择排序方式";
                break;
            case R.id.action_get_lrc:
                msg += "一键获取封面歌词";
                break;
            case R.id.action_music_update:
                msg += "升级音质";
                break;
        }
        if(!msg.equals("")) {
            Toast.makeText(LocalMusicActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
        return true;
    }
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST + 1:
                Toast.makeText(this, "删除菜单被点击了", Toast.LENGTH_SHORT).show();
                break;
            case Menu.FIRST + 2:
                Toast.makeText(this, "保存菜单被点击了", Toast.LENGTH_SHORT).show();
                break;
            case Menu.FIRST + 3:
                Toast.makeText(this, "帮助菜单被点击了", Toast.LENGTH_SHORT).show();
                break;
            case Menu.FIRST + 4:
                Toast.makeText(this, "添加菜单被点击了", Toast.LENGTH_SHORT).show();
                break;
            case Menu.FIRST + 5:
                Toast.makeText(this, "详细菜单被点击了", Toast.LENGTH_SHORT).show();
                break;
            case Menu.FIRST + 6:
                Toast.makeText(this, "发送菜单被点击了", Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }*/

    /*@Override
    public void onOptionsMenuClosed(Menu menu) {
        Toast.makeText(this, "选项菜单关闭了", Toast.LENGTH_SHORT).show();
    }*/

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        /*Toast.makeText(this,
                "选项菜单显示之前onPrepareOptionsMenu方法会被调用，你可以用此方法来根据打当时的情况调整菜单",
                Toast.LENGTH_LONG).show();*/
        // 如果返回false，此方法就把用户点击menu的动作给消费了，onCreateOptionsMenu方法将不会被调用
        return true;
    }

    @Override
    public void onClick(View v) {
        /*switch (v.getId()){
            case R.id.arrow:
                onBackPressed();
                break;
        }*/
    }
    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.e("Activity","您按下了返回键！");
            moveTaskToBack(true);//true对任何Activity都适用
        }
        return super.onKeyDown(keyCode, event);
    }*/




    /*@Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }*/

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart方法得到执行。");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume方法得到执行。");

  /*      singleSongAdapter = new SingleSongAdapter(this,getApplicationContext());
        SingleSongAdapter.UpdateSpeakerReceiver updateSpeakerReceiver = singleSongAdapter.new UpdateSpeakerReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UPDATE_SPEAKER_LIST_POSITION);
        getApplicationContext().registerReceiver(updateSpeakerReceiver,intentFilter);*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause方法得到执行。");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop方法得到执行。");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart方法得到执行。");
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG,"onDestroy方法得到执行。");
        //关闭notification
        //PlayerService.manager.cancel(NOTIFICATION_ID);
        super.onDestroy();
    }







    private class MyPagerAdapter extends FragmentPagerAdapter {

        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
            public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

            @Override
            public int getCount() {
            return fragmentList.size();
        }

            @Override
            public CharSequence getPageTitle(int position) {
            return tabNameList.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            Log.v("getItem","该方法得到调用,目前的显示的是："+position);
            return fragmentList.get(position);
        }
    }
}


