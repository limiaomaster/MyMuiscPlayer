package cn.edu.cdut.lm.mymuiscplayer.innerfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.adapter.SingleSongRVAdapter;
import cn.edu.cdut.lm.mymuiscplayer.module.Mp3Info;
import cn.edu.cdut.lm.mymuiscplayer.utilities.MediaUtil;
import cn.edu.cdut.lm.mymuiscplayer.widget.DividerItemDecoration;

/**
 * Created by LimiaoMaster on 2016/8/24 18:26
 */

public class SingleSongFragmentOnRV extends Fragment {
    private RecyclerView recyclerView;
    private List<Mp3Info> list ;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inner_fragment_single_music_recycler_view,container,false);

        list = MediaUtil.getMp3List(getContext());
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_localMusic);
        //1
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //2
        SingleSongRVAdapter singleSongRVAdapter = new SingleSongRVAdapter(getActivity(),getContext(),list);
        recyclerView.setAdapter(singleSongRVAdapter);
        //3
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(dividerItemDecoration);
        return view;
    }
}
