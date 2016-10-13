package cn.edu.cdut.lm.mymuiscplayer.dialogfragment;

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
import cn.edu.cdut.lm.mymuiscplayer.adapter.MoreInformationAdapter;
import cn.edu.cdut.lm.mymuiscplayer.module.Mp3Info;
import cn.edu.cdut.lm.mymuiscplayer.utilities.MoreInfoUtil;
import cn.edu.cdut.lm.mymuiscplayer.widget.DividerItemDecoration;

/**
 * Created by LimiaoMaster on 2016/8/23 19:26
 */

public class MoreInfoSingleSongFragment extends DialogFragment {

    private List<Map<String, Object>> list;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private String artist;
    private String title;
    private String album;
    private Mp3Info mp3Info;

    public static MoreInfoSingleSongFragment newInstance(Mp3Info mp3Info, int startFrom) {
        MoreInfoSingleSongFragment moreInfoSingleSongFragment = new MoreInfoSingleSongFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("Mp3Info", mp3Info);
        bundle.putInt("type",startFrom);
        moreInfoSingleSongFragment.setArguments(bundle);
        return moreInfoSingleSongFragment;
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
        double heightPercent = 0.62;
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

        getMp3InfoFromParcel();

        View view = inflater.inflate(R.layout.layout_fragment_overflow,container);
        list = MoreInfoUtil.getMoreInfoOnSingleMusic();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_localMusic_moreInformation);
        //1
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        //2
        MoreInformationAdapter moreInformationAdapter = new MoreInformationAdapter(getContext(),mp3Info,list,this);
        recyclerView.setAdapter(moreInformationAdapter);
        //3
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);


        return view;
    }

    private void getMp3InfoFromParcel() {
        mp3Info = getArguments().getParcelable("Mp3Info");
    }
}