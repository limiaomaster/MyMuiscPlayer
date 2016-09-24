package cn.edu.cdut.lm.mymuiscplayer.innerfragment;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.activity.LocalMusicActivity;
import cn.edu.cdut.lm.mymuiscplayer.adapter.SingleSongAdapter;
import cn.edu.cdut.lm.mymuiscplayer.widget.DividerItemDecoration;

import static cn.edu.cdut.lm.mymuiscplayer.service.PlayerService.UPDATE_SPEAKER_LIST_POSITION;

/**
 * Created by LimiaoMaster on 2016/8/24 18:26
 */

public class SingleSongFragment extends Fragment {
    private RecyclerView recyclerView;
    private static final String TAG = "SingleSongFragment";
    private SingleSongAdapter.UpdateSpeakerReceiver updateSpeakerReceiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e(TAG,"onCreateView正在执行-----");

        View view = inflater.inflate(R.layout.inner_fragment_single_music_recycler_view,container,false);


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_singleMusic);
        //1
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //2
        SingleSongAdapter singleSongAdapter = new SingleSongAdapter((LocalMusicActivity) getActivity(),getContext());
        recyclerView.setAdapter(singleSongAdapter);
        //在这里注册小喇叭的监听器，之前是在adapter的构造函数中注册的，现在改在这里，
        //因为方便利用该fragment的生命周期，取消注册。onDestroyView中取消注册的。
        updateSpeakerReceiver = singleSongAdapter.new UpdateSpeakerReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UPDATE_SPEAKER_LIST_POSITION);
        getActivity().registerReceiver(updateSpeakerReceiver,intentFilter);
        //3
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(dividerItemDecoration);
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
