package cn.edu.cdut.lm.mymuiscplayer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.innerfragment.AlbumFragment;
import cn.edu.cdut.lm.mymuiscplayer.innerfragment.FolderFragment;
import cn.edu.cdut.lm.mymuiscplayer.innerfragment.SingerFragment;
import cn.edu.cdut.lm.mymuiscplayer.innerfragment.SingleSongFragment;

/**
 * Created by LimiaoMaster on 2016/8/13 0013.
 */

public class LocalMusicFragment extends Fragment implements View.OnClickListener , AdapterView.OnItemClickListener{

    private TabLayout tablayout ;
    private ViewPager viewPager;

    private SingleSongFragment singleSongFragment;
    private SingerFragment singerFragment;
    private AlbumFragment albumFragment;
    private FolderFragment folderFragment;

    private List<String> tabNameList = new ArrayList<>(4);
    private List<Fragment> fragmentList = new ArrayList<>(4);

    private ImageView back ;


    private TextView tv_title_of_music;
    private TextView tv_artist_of_music;

    /*private HomeReceiver homeReceiver;*/

    public static final String UPDATE_TITLE_ARTIST = "cn.edu.cdut.lm.mymusicplayer.UPDATE_TITLE_ARTIST";    //  设置曲名和艺术家



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        /*registerMyReceiver();*/
        super.onCreate(savedInstanceState);

    }




    private void addView() {
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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_music,container,false);

        tv_title_of_music = (TextView) view.findViewById(R.id.title_of_music);
        tv_artist_of_music = (TextView) view.findViewById(R.id.artist_of_music);

        addView();

        tablayout = (TabLayout) view.findViewById(R.id.tab_localmusic);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager_localmusic);
        back = (ImageView) view.findViewById(R.id.arrow);
        back.setOnClickListener(this);

        MyAdapter myAdapter = new MyAdapter(getFragmentManager());
        myAdapter.notifyDataSetChanged();
        Log.v("notifyDataSetChanged","刷新数据。。。。。。。。。。。。");
        viewPager.setAdapter(myAdapter);
        //viewPager.setOffscreenPageLimit(4);

        tablayout.setTabMode(TabLayout.MODE_FIXED);
        tablayout.setupWithViewPager(viewPager);



        return view;
    }

    @Override
    public void onClick(View v) {
        Log.v("onClick","您点击了返回键！");
        //getActivity().onBackPressed();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.remove(this);

        fragmentManager.popBackStack();


        fragmentTransaction.commit();


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    class MyAdapter extends FragmentPagerAdapter{
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.v("instantiateItem","该方法得到调用，位置是："+position);
           /* if(position==0||position==1) {
                Object object = fragmentList.get(0);
                destroyItem(container, position, object);
            }*/
            return super.instantiateItem(container, position);
        }

        @Override
        public int getCount() {
            Log.v("getCount","该方法得到调用");
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Log.v("getPageTitle","该方法得到调用,位置是："+position);
            return tabNameList.get(position);
        }

        /*@Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.v("destroyItem","该方法得到调用,销毁的位置是："+position);
            super.destroyItem(container, position, object);
        }*/

        @Override
        public Fragment getItem(int position) {
            Log.v("getItem","该方法得到调用,目前的显示的是："+position);
            return fragmentList.get(position);
        }
    }

    /*private void registerMyReceiver(){
        homeReceiver = new HomeReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UPDATE_TITLE_ARTIST);
        getActivity().registerReceiver(homeReceiver,intentFilter);
    }*/





    /*public class HomeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("onReceive()","收到Service发来的广播！");

            String action = intent.getAction();
            if (action.equals(UPDATE_TITLE_ARTIST)){
                Log.e("onReceive()","是更新曲目和艺术家的Action");
                String title = intent.getStringExtra("title");
                String artist = intent.getStringExtra("artist");
                Log.e("onReceive()","准备更新，曲目是："+title+"艺术家是："+artist);
                tv_title_of_music.setText(title);
                tv_artist_of_music.setText(artist);
                Log.e("onReceive()","更新完成，，，，，，，");
            }
        }
    }*/
}
