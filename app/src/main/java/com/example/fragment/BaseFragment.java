package com.example.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.example.interfaces.OnToggleListener;

import org.greenrobot.eventbus.EventBus;


public abstract class BaseFragment extends Fragment {
    protected Activity mActivity;
    private OnToggleListener mToggleListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
        if(getActivity() instanceof OnToggleListener){
            mToggleListener = (OnToggleListener) getActivity();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        afterActivityCreated(savedInstanceState);

    }

    protected void toggle(){
        mToggleListener.toggle();
    }


    protected abstract void afterActivityCreated(Bundle savedInstanceState);

}
