package cn.edu.cdut.lm.mymuiscplayer.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
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
 * Created by LimiaoMaster on 2016/8/13 0013.
 */

public class LocalMusicFragment extends Fragment implements View.OnClickListener {

    private TabLayout tablayout ;
    private ViewPager viewPager;

    private SingleSongFragment singleSongFragment;
    private SingerFragment singerFragment;
    private AlbumFragment albumFragment;
    private FolderFragment folderFragment;

    private List<String> tabNameList = new ArrayList<>(4);
    private List<Fragment> fragmentList = new ArrayList<>(4);

    private ImageView back ;

    private MusicFragment musicFragment;


    public static LocalMusicFragment newInstance(Bundle agrs){

        LocalMusicFragment localMusicFragment = new LocalMusicFragment();
        localMusicFragment.setArguments(agrs);
        return localMusicFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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
        fragmentManager.popBackStack();
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
}
