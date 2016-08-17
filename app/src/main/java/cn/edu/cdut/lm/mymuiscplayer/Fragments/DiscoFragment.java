package cn.edu.cdut.lm.mymuiscplayer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.edu.cdut.lm.mymuiscplayer.R;

/**
 * Created by Administrator on 2016/8/12 0012.
 */

public class DiscoFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_disco, container, false);
    }
}
