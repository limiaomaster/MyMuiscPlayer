package cn.edu.cdut.lm.mymuiscplayer.dialogfragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.module.Mp3Info;
import cn.edu.cdut.lm.mymuiscplayer.service.PlayerService;
import cn.edu.cdut.lm.mymuiscplayer.utilities.MediaUtil;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by LimiaoMaster on 2016/9/29 11:29
 */

public class SortOrderFragment extends DialogFragment implements View.OnClickListener {

    private String TAG = "SortOrderFragment";
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private ImageView[] imageViews = new ImageView[4];
    private int checkPosition;
    private static final int CHANGE_SORT_ORDER_INTENT = -5;
    private int lastCheckPosition;
    private List<Mp3Info> lastMp3List;
    private List<Mp3Info> newMp3List;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.layout_fragment_sort_order,container);
        imageView1 = (ImageView) view.findViewById(R.id.check1);
        imageView2 = (ImageView) view.findViewById(R.id.check2);
        imageView3 = (ImageView) view.findViewById(R.id.check3);
        imageView4 = (ImageView) view.findViewById(R.id.check4);
        imageViews[0] = imageView1;
        imageViews[1] = imageView2;
        imageViews[2] = imageView3;
        imageViews[3] = imageView4;

        RelativeLayout relativeLayout1 = (RelativeLayout) view.findViewById(R.id.rl_1);
        RelativeLayout relativeLayout2 = (RelativeLayout) view.findViewById(R.id.rl_2);
        RelativeLayout relativeLayout3 = (RelativeLayout) view.findViewById(R.id.rl_3);
        RelativeLayout relativeLayout4 = (RelativeLayout) view.findViewById(R.id.rl_4);
        relativeLayout1.setOnClickListener(this);
        relativeLayout2.setOnClickListener(this);
        relativeLayout3.setOnClickListener(this);
        relativeLayout4.setOnClickListener(this);

        getData();
        setCheckPosition(checkPosition);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置fragment高度 、宽度
        double heightPercent = 0.45;
        double widthPercent = 0.85;
        int dialogHeight = (int) (getActivity().getResources().getDisplayMetrics().heightPixels * heightPercent);
        int dialogWidth = (int) (getActivity().getResources().getDisplayMetrics().widthPixels * widthPercent);
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
        getDialog().setCanceledOnTouchOutside(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_1:
                checkPosition = 0;
                saveData();
                setCheckPosition(checkPosition);
                changeSortOrder(checkPosition);
                updateSpeakerPosition();
                break;
            case R.id.rl_2:
                checkPosition = 1;
                saveData();
                setCheckPosition(checkPosition);
                changeSortOrder(checkPosition);
                updateSpeakerPosition();
                break;
            case R.id.rl_3:
                checkPosition = 2;
                saveData();
                setCheckPosition(checkPosition);
                changeSortOrder(checkPosition);
                updateSpeakerPosition();
                break;
            case R.id.rl_4:
                checkPosition = 3;
                saveData();
                setCheckPosition(checkPosition);
                changeSortOrder(checkPosition);
                updateSpeakerPosition();
                break;
        }
    }

    private void getData(){
        SharedPreferences pref = getContext().getSharedPreferences("data", MODE_PRIVATE);
        checkPosition = pref.getInt("sort_order_check_position",0);
        lastCheckPosition = checkPosition;
    }



    private void saveData(){
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putInt("sort_order_check_position", checkPosition);
        editor.commit();
    }

    private void setCheckPosition(int checkPosition) {
        for (int i = 0 ; i<=3 ; i++){
            if (i == checkPosition){
                imageViews[i].setVisibility(View.VISIBLE);
            }else imageViews[i].setVisibility(View.INVISIBLE);
        }
    }


    private void changeSortOrder(int checkPosition) {
        Intent intent = new Intent();
        intent.putExtra("position", CHANGE_SORT_ORDER_INTENT);
        intent.putExtra("orderType",checkPosition);
        intent.setClass(getContext(), PlayerService.class);
        getContext().startService(intent);
    }

    private void updateSpeakerPosition(){
        lastMp3List = MediaUtil.getMp3ListFromMyDatabase(getContext() , lastCheckPosition);
        newMp3List = MediaUtil.getMp3ListFromMyDatabase(getContext() , checkPosition);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("data",MODE_PRIVATE);
        int lastSpeakerPosition = sharedPreferences.getInt("speakerPosition",0);
        int newSpeakerPosition = findSpeakerPositionInNewList(lastSpeakerPosition);

        SharedPreferences.Editor editor = getActivity().getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putInt("speakerPosition", newSpeakerPosition);
        editor.commit();
    }

    private int findSpeakerPositionInNewList(int position){
        int positionOfMatched = 0;
        for (Mp3Info mp3Info : newMp3List) {
            if (mp3Info.getMusicId() == lastMp3List.get(position).getMusicId()) {
                Log.e(TAG, lastMp3List.get(position).getMusicId()+"");
                positionOfMatched = mp3Info.getPositionInThisList();
                break;
            }
        }
        return positionOfMatched;
    }

}
