package cn.edu.cdut.lm.mymuiscplayer.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.Fragments.DiscoFragment;
import cn.edu.cdut.lm.mymuiscplayer.Fragments.FriendFragment;
import cn.edu.cdut.lm.mymuiscplayer.Fragments.MusicFragment;
import cn.edu.cdut.lm.mymuiscplayer.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private List<Fragment> fragmentList = new ArrayList<>(3);
    private ImageView iv_disco;
    private ImageView iv_music;
    private ImageView iv_friend;
    private ViewPager view_pager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        //  设置不显示title
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentList.add(new DiscoFragment());
        fragmentList.add(new MusicFragment());
        fragmentList.add(new FriendFragment());

        iv_disco = (ImageView) findViewById(R.id.bar_disco);
        iv_music = (ImageView) findViewById(R.id.bar_music);
        iv_friend = (ImageView) findViewById(R.id.bar_friends);

        iv_music.setSelected(true);


        view_pager = (ViewPager) findViewById(R.id.view_pager);


        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        view_pager.setAdapter(myFragmentPagerAdapter);


        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        iv_disco.setSelected(true);
                        iv_music.setSelected(false);
                        iv_friend.setSelected(false);
                        break;
                    case 1:
                        iv_disco.setSelected(false);
                        iv_music.setSelected(true);
                        iv_friend.setSelected(false);
                        break;
                    case 2:
                        iv_disco.setSelected(false);
                        iv_music.setSelected(false);
                        iv_friend.setSelected(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //  设置初始选中viewpager的第二页，但要注意写在 view_pager.setCurrentItem(1)；之后才能生效
        view_pager.setCurrentItem(1);

        iv_disco.setOnClickListener(myOnClickListener);
        iv_music.setOnClickListener(myOnClickListener);
        iv_friend.setOnClickListener(myOnClickListener);

    }

    MyOnClickListener myOnClickListener = new MyOnClickListener();

    class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.bar_disco:
                    iv_disco.setSelected(true);
                    iv_music.setSelected(false);
                    iv_friend.setSelected(false);
                    view_pager.setCurrentItem(0);
                    break;
                case R.id.bar_music:
                    iv_disco.setSelected(false);
                    iv_music.setSelected(true);
                    iv_friend.setSelected(false);
                    view_pager.setCurrentItem(1);
                    break;
                case R.id.bar_friends:
                    iv_disco.setSelected(false);
                    iv_music.setSelected(false);
                    iv_friend.setSelected(true);
                    view_pager.setCurrentItem(2);
                    break;
            }
        }
    }


    class MyFragmentPagerAdapter extends FragmentPagerAdapter{

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }










    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    //  以下两个方法是创建标题栏右边的菜单键，注释掉的话就没有菜单键，
    // 可以自己编写自己的按键，比如网易云音乐的搜索等。
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
