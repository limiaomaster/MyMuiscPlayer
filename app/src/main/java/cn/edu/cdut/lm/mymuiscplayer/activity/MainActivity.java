package cn.edu.cdut.lm.mymuiscplayer.activity;

import android.content.Intent;
import android.os.Build;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.adapter.NavigationAdapter;
import cn.edu.cdut.lm.mymuiscplayer.fragments.DiscoFragment;
import cn.edu.cdut.lm.mymuiscplayer.fragments.FriendFragment;
import cn.edu.cdut.lm.mymuiscplayer.fragments.MusicFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {

    private static final String TAG = "MainActivity";
    private static final int NOTIFICATION_ID = 5709;
    private List<Fragment> fragmentList = new ArrayList<>(3);
    private ImageView iv_disco;
    private ImageView iv_music;
    private ImageView iv_friend;
    private ViewPager view_pager;
    private long time;
    private DrawerLayout drawerLayout;
    private Button bt_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate方法得到执行。");

        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setContentView(R.layout.activity_main_recycler_view);
        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //NavigationView navigationView = (NavigationView) findViewById(R.id.recyclerview_nav);
        //navigationView.setNavigationItemSelectedListener(this);



        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //  设置不显示title
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        iv_disco = (ImageView) findViewById(R.id.bar_disco);
        iv_music = (ImageView) findViewById(R.id.bar_music);
        iv_friend = (ImageView) findViewById(R.id.bar_friends);
        fragmentList.add(new DiscoFragment());
        fragmentList.add(new MusicFragment());
        fragmentList.add(new FriendFragment());


        iv_music.setSelected(true);

        view_pager = (ViewPager) findViewById(R.id.view_pager);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_nav);
        //1
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        //2
        NavigationAdapter navigationAdapter = new NavigationAdapter(this);
        recyclerView.setAdapter(navigationAdapter);
        //3
        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        //recyclerView.addItemDecoration(dividerItemDecoration);

        bt_exit = (Button) findViewById(R.id.bt_navi_exit);
        bt_exit.setOnClickListener(this);


        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        view_pager.setAdapter(myFragmentPagerAdapter);
        //  设置初始选中viewpager的第二页，
        // 但要注意写在 view_pager.setAdapter(myFragmentPagerAdapter);之后才能生效
        view_pager.setCurrentItem(1);
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
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



        iv_disco.setOnClickListener(myOnClickListener);
        iv_music.setOnClickListener(myOnClickListener);
        iv_friend.setOnClickListener(myOnClickListener);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_navi_setting:
                break;
            case R.id.bt_navi_exit:
                finish();
                break;
        }
    }


    MyOnClickListener myOnClickListener = new MyOnClickListener();
    class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
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

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        MyFragmentPagerAdapter(FragmentManager fm) {
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

        if (id == R.id.nav_1) {

        } else if (id == R.id.nav_2) {

        } else if (id == R.id.nav_3) {

        } else if (id == R.id.nav_4) {

        } else if (id == R.id.nav_5) {

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
                return true;
            } else if ((System.currentTimeMillis() - time > 1000)) {
                Toast.makeText(this, "再按一次返回桌面", Toast.LENGTH_SHORT).show();
                time = System.currentTimeMillis();
            } else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart方法得到执行。");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume方法得到执行。");
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
}
