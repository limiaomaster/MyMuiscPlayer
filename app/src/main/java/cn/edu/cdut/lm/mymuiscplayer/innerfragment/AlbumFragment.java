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
import cn.edu.cdut.lm.mymuiscplayer.adapter.AlbumRVAdapter;
import cn.edu.cdut.lm.mymuiscplayer.module.AlbumInfo;
import cn.edu.cdut.lm.mymuiscplayer.utilities.AlbumUtil;
import cn.edu.cdut.lm.mymuiscplayer.widget.DividerItemDecoration;

/**
 * Created by LimiaoMaster on 2016/8/14 0014 下午 10:05
 */

public class AlbumFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inner_fragment_album, container , false);
        List<AlbumInfo> albumInfoList = AlbumUtil.getAlbumInfoList(getContext());
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_albumFragment);
        //1
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //2
        AlbumRVAdapter albumRVAdapter = new AlbumRVAdapter(getActivity(), getContext(), albumInfoList);
        recyclerView.setAdapter(albumRVAdapter);
        //3
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(dividerItemDecoration);
        return view;
    }
}
