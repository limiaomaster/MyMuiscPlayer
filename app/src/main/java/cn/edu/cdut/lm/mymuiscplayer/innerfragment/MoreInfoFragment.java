package cn.edu.cdut.lm.mymuiscplayer.innerfragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.util.List;
import java.util.Map;

import cn.edu.cdut.lm.mymuiscplayer.R;
import cn.edu.cdut.lm.mymuiscplayer.adapter.MoreInfoFragmentAdapter;
import cn.edu.cdut.lm.mymuiscplayer.module.AlbumInfo;
import cn.edu.cdut.lm.mymuiscplayer.module.ArtistInfo;
import cn.edu.cdut.lm.mymuiscplayer.module.FolderInfo;
import cn.edu.cdut.lm.mymuiscplayer.utilities.MoreInfoUtil;
import cn.edu.cdut.lm.mymuiscplayer.widget.DividerItemDecoration;

/**
 * Created by LimiaoMaster on 2016/9/1 9:45
 */
public class MoreInfoFragment extends DialogFragment{

    private double heightPercent = 0.37;
    private List<Map<String,Object>> list;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ArtistInfo artistInfo;
    private AlbumInfo albumInfo;
    private FolderInfo folderInfo;

    private static String fragmentType;

    private String ARTIST_FRAGMENT = "artist_fragment";
    private String ALBUM_FRAGMENT = "album_fragment";
    private String FOLDER_FRAGMENT = "folder_fragment";



    public static MoreInfoFragment newInstance(ArtistInfo artistInfo, String type) {
        fragmentType = type;
        MoreInfoFragment fragment = new MoreInfoFragment();
        Bundle args = new Bundle();
        args.putParcelable("ArtistInfo",artistInfo);
        fragment.setArguments(args);
        return fragment;
    }

    public  static MoreInfoFragment newInstance(AlbumInfo albumInfo, String type) {
        fragmentType = type;
        MoreInfoFragment fragment =  new MoreInfoFragment();
        Bundle args = new Bundle();
        args.putParcelable("AlbumInfo",albumInfo);
        fragment.setArguments(args);
        return fragment;
    }


    public static MoreInfoFragment newInstance(FolderInfo folderInfo, String type) {
        fragmentType = type;
        MoreInfoFragment fragment =  new MoreInfoFragment();
        Bundle args = new Bundle();
        args.putParcelable("FolderInfo",folderInfo);
        fragment.setArguments(args);
        return fragment;
    }




    @Override
    public void onAttach(Activity activity) {
        Log.i("MoreInfoArtistFragment","onAttach正在执行-----"+fragmentType);
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("MoreInfoArtistFragment","onCreate正在执行-----"+fragmentType);
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDatePickerDialog);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i("MoreInfoArtistFragment","onActivityCreated正在执行-----"+fragmentType);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.i("MoreInfoArtistFragment","onStart正在执行-----"+fragmentType);
        super.onStart();
        //设置fragment高度 、宽度
        int dialogHeight = (int) (getActivity().getResources().getDisplayMetrics().heightPixels * heightPercent);
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, dialogHeight);
        getDialog().setCanceledOnTouchOutside(true);
    }

    @Override
    public void onResume() {
        Log.i("MoreInfoArtistFragment","onResume正在执行-----"+fragmentType);
        super.onResume();
    }

    @Override
    public void onPause() {
        /*fragmentType = null;*/
        Log.i("MoreInfoArtistFragment","onPause正在执行-----"+fragmentType);
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.i("MoreInfoArtistFragment","onStop正在执行-----"+fragmentType);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.i("MoreInfoArtistFragment","onDestroyView正在执行-----"+fragmentType);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.i("MoreInfoArtistFragment","onStop正在执行-----"+fragmentType);
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.i("MoreInfoArtistFragment","onDetach正在执行-----"+fragmentType);
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("MoreInfoArtistFragment","onCreateView正在执行-----"+fragmentType);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setAttributes(params);

        getArtistInfoFromParcel();
        getAlbumInfoFromParcel();
        getFolderInfoFromParcel();

        View view = inflater.inflate(R.layout.layout_more_info_local_music,container);
        list = MoreInfoUtil.getMoreInfoOnArtist();

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_localMusic_moreInformation);
        //1
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        //2
        if(fragmentType.equals(ARTIST_FRAGMENT) ){
            MoreInfoFragmentAdapter moreInfoArtistAdapter = new MoreInfoFragmentAdapter(artistInfo,list,ARTIST_FRAGMENT);
            recyclerView.setAdapter(moreInfoArtistAdapter);
        }else if (fragmentType.equals(ALBUM_FRAGMENT)){
            MoreInfoFragmentAdapter moreInfoArtistAdapter = new MoreInfoFragmentAdapter(albumInfo,list,ALBUM_FRAGMENT);
            recyclerView.setAdapter(moreInfoArtistAdapter);
        }else if (fragmentType.equals(FOLDER_FRAGMENT)){
            MoreInfoFragmentAdapter moreInfoArtistAdapter = new MoreInfoFragmentAdapter(folderInfo,list,FOLDER_FRAGMENT);
            recyclerView.setAdapter(moreInfoArtistAdapter);
        }

        //3
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(itemDecoration);

        return view;
    }





    private void getArtistInfoFromParcel() {
        artistInfo = getArguments().getParcelable("ArtistInfo");
    }

    private void getAlbumInfoFromParcel() {
        albumInfo = getArguments().getParcelable("AlbumInfo");
    }

    private void getFolderInfoFromParcel() {
        folderInfo = getArguments().getParcelable("FolderInfo");
    }

}
