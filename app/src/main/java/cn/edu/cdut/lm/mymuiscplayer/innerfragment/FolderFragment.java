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
import cn.edu.cdut.lm.mymuiscplayer.adapter.FolderRVAdapter;
import cn.edu.cdut.lm.mymuiscplayer.module.FolderInfo;
import cn.edu.cdut.lm.mymuiscplayer.utilities.FolderUtil;
import cn.edu.cdut.lm.mymuiscplayer.widget.DividerItemDecoration;

/**
 * Created by LimiaoMaster on 2016/8/14 0014 下午 10:07
 */

public class FolderFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inner_fragment_folder, container , false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_folderFragment);

        List<FolderInfo> folderInfoList = FolderUtil.getFolderInfoList(getContext());
        //1
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        //2
        FolderRVAdapter folderRVAdapter = new FolderRVAdapter(getActivity(),getContext(),folderInfoList);
        recyclerView.setAdapter(folderRVAdapter);
        //3
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(dividerItemDecoration);
        return view;
    }
}
