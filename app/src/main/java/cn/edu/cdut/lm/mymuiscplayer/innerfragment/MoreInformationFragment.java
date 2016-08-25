package cn.edu.cdut.lm.mymuiscplayer.innerfragment;


import android.support.v4.app.DialogFragment;

import cn.edu.cdut.lm.mymuiscplayer.module.Mp3Info;

/**
 * Created by LimiaoMaster on 2016/8/23 19:26
 */

public class MoreInformationFragment extends DialogFragment {

    public static MoreInformationFragment newInstance(Mp3Info mp3Info, int i) {
        MoreInformationFragment moreInformationFragment = new MoreInformationFragment();
       /* Bundle bundle = new Bundle();
        bundle.putParcelable("music", mp3Info);
        bundle.putInt("type",i);*/
        return moreInformationFragment;
    }
}