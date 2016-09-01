package cn.edu.cdut.lm.mymuiscplayer.innerfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.util.List;
import java.util.Map;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.adapter.MoreInfoArtistAdapter;
import cn.edu.cdut.lm.mymuiscplayer.module.ArtistInfo;
import cn.edu.cdut.lm.mymuiscplayer.utilities.MoreInfoUtil;
import cn.edu.cdut.lm.mymuiscplayer.widget.DividerItemDecoration;

/**
 * Created by LimiaoMaster on 2016/9/1 9:45
 */
public class MoreInfoArtistFragment extends DialogFragment{

    private double heightPercent = 0.37;
    private List<Map<String,Object>> list;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ArtistInfo artistInfo;







    public static MoreInfoArtistFragment newInstance(ArtistInfo artistInfo) {
        MoreInfoArtistFragment fragment = new MoreInfoArtistFragment();
        Bundle args = new Bundle();
        args.putParcelable("ArtistInfo",artistInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDatePickerDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置fragment高度 、宽度
        int dialogHeight = (int) (getActivity().getResources().getDisplayMetrics().heightPixels * heightPercent);
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, dialogHeight);
        getDialog().setCanceledOnTouchOutside(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setAttributes(params);

        getArtistInfoFromParcel();

        View view = inflater.inflate(R.layout.layout_more_info_local_music,container);
        list = MoreInfoUtil.getMoreInfoOnArtist();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_localMusic_moreInformation);
        //1
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        //2
        MoreInfoArtistAdapter moreInfoArtistAdapter = new MoreInfoArtistAdapter(artistInfo,list);
        recyclerView.setAdapter(moreInfoArtistAdapter);
        //3
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);

        return view;
    }

    private void getArtistInfoFromParcel() {
        artistInfo = getArguments().getParcelable("ArtistInfo");
    }
}
