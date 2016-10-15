package cn.edu.cdut.lm.mymuiscplayer.innerfragment;

import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.activity.LocalMusicActivity;
import cn.edu.cdut.lm.mymuiscplayer.adapter.SingleSongAdapter;
import cn.edu.cdut.lm.mymuiscplayer.layout.QuickScrollBar;
import cn.edu.cdut.lm.mymuiscplayer.widget.DividerItemDecoration;

import static android.content.Context.MODE_PRIVATE;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static cn.edu.cdut.lm.mymuiscplayer.service.PlayerService.UPDATE_SORT_ORDER;
import static cn.edu.cdut.lm.mymuiscplayer.service.PlayerService.UPDATE_SPEAKER_LIST_POSITION;

/**
 * Created by LimiaoMaster on 2016/8/24 18:26
 */

public class SingleSongFragment extends Fragment {
    private RecyclerView recyclerView;
    private static final String TAG = "SingleSongFragment";
    private SingleSongAdapter.UpdateSpeakerReceiver updateSpeakerReceiver;
    private SingleSongAdapter.UpdateSortOrderReceiver updateSortOrderReceiver;
    private LinearLayoutManager linearLayoutManager;
    private int offset;
    private int scrolledItemPosition;
    private QuickScrollBar quickScrollBar;
    private TextView tv_alpha;
    private Handler handler;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG,"onCreateView正在执行-----");

        View view = inflater.inflate(R.layout.inner_fragment_single_music_recycler_view,container,false);
        quickScrollBar = (QuickScrollBar) view.findViewById(R.id.quickScrollBar);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_singleMusic);
        //1
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.scrollToPositionWithOffset(scrolledItemPosition,offset);

        recyclerView.setLayoutManager(linearLayoutManager);
        //2
        SingleSongAdapter singleSongAdapter = new SingleSongAdapter((LocalMusicActivity) getActivity(),getContext(),quickScrollBar);
        recyclerView.setAdapter(singleSongAdapter);


        //在这里注册小喇叭的监听器，之前是在adapter的构造函数中注册的，现在改在这里，
        //因为方便利用该fragment的生命周期，取消注册。onDestroyView中取消注册的。
        updateSpeakerReceiver = singleSongAdapter.new UpdateSpeakerReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UPDATE_SPEAKER_LIST_POSITION);
        getActivity().registerReceiver(updateSpeakerReceiver,intentFilter);

        updateSortOrderReceiver = singleSongAdapter.new UpdateSortOrderReceiver();
        IntentFilter intentFilter_order = new IntentFilter();
        intentFilter_order.addAction(UPDATE_SORT_ORDER);
        getActivity().registerReceiver(updateSortOrderReceiver,intentFilter_order);



        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1){
                    quickScrollBar.setVisibility(View.INVISIBLE);
                }
            }
        };


        //3
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(dividerItemDecoration);

        //4
        quickScrollBar.initBar(linearLayoutManager,view);

        //5
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                Log.e(TAG,"onScrollStateChanged方法执行-----------"+newState);
                handler.removeMessages(1);
                quickScrollBar.setVisibility(View.VISIBLE);
                //此处if判断属于优化，当滑动结束后才存储，防止反复存储，可以去了解下滑动的三种状态。
                if (newState == SCROLL_STATE_IDLE){
                    View topView = linearLayoutManager.getChildAt(0);          //获取可视的第一个view
                    scrolledItemPosition = linearLayoutManager.getPosition(topView);  //得到该View的数组位置
                    offset = topView.getTop();                                   //获取与该view的顶部的偏移量
                    Log.e(TAG,"第几个Item："+scrolledItemPosition+"  偏移量是："+offset);
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("data",MODE_PRIVATE).edit();
                    editor.putInt("scrolledItemPosition",scrolledItemPosition);
                    editor.putInt("offset",offset);
                    editor.commit();
                    handler.sendEmptyMessageDelayed(1,1000);
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                Log.e(TAG,"onScrolled方法执行-----------");
                handler.removeMessages(1);
            }
        });

        quickScrollBar.setVisibility(View.INVISIBLE);
        return view;
    }





    @Override
    public void onAttach(Context context) {
        Log.e(TAG,"onAttach正在执行-----");
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e(TAG,"onCreate正在执行-----");
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data",MODE_PRIVATE);
        scrolledItemPosition = sharedPreferences.getInt("scrolledItemPosition",0);
        offset = sharedPreferences.getInt("offset",0);
    }

    //onCreateView()

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.e(TAG,"onActivityCreated正在执行-----");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.e(TAG,"onStart正在执行-----");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.e(TAG,"onResume正在执行-----");
        super.onResume();

    }

    //Fragment is active

    @Override
    public void onPause() {
        /*fragmentType = null;*/
        Log.e(TAG,"onPause正在执行-----");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.e(TAG,"onStop正在执行-----");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.e(TAG,"onDestroyView正在执行-----");
        super.onDestroyView();
        getActivity().unregisterReceiver(updateSpeakerReceiver);
        getActivity().unregisterReceiver(updateSortOrderReceiver);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG,"onStop正在执行-----");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.e(TAG,"onDetach正在执行-----");
        super.onDetach();
    }
}
