package cn.edu.cdut.lm.mymuiscplayer.activity;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.innerfragment.AlbumFragment;
import cn.edu.cdut.lm.mymuiscplayer.innerfragment.FolderFragment;
import cn.edu.cdut.lm.mymuiscplayer.innerfragment.SingerFragment;
import cn.edu.cdut.lm.mymuiscplayer.innerfragment.SingleSongFragment;


/**
 * Created by LimiaoMaster on 2016/8/20 8:30
 */

public class LocalMusicActivity extends AppCompatActivity implements View.OnClickListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private SingleSongFragment singleSongFragment;
    private SingerFragment singerFragment;
    private AlbumFragment albumFragment;
    private FolderFragment folderFragment;

    private List<String> tabNameList = new ArrayList<>(4);
    private List<Fragment> fragmentList = new ArrayList<>(4);

    private ImageView back;


    private ActionBar ab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_local_music);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_localmusic);
        // App Logo
        //toolbar.setLogo(R.drawable.banshouren);
        // Title
        //toolbar.setTitle("My Title");
        // Sub Title
        //toolbar.setSubtitle("Sub title");
        setSupportActionBar(toolbar);


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
            singerFragment = new SingerFragment();
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
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);

        back = (ImageView) findViewById(R.id.arrow);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.arrow:
                onBackPressed();
                break;
        }
    }




    private class MyPagerAdapter extends FragmentPagerAdapter {


        public MyPagerAdapter(FragmentManager fm) {
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


