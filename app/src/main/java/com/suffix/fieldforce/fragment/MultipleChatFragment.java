package com.suffix.fieldforce.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suffix.fieldforce.R;

public class MultipleChatFragment extends Fragment {


  public MultipleChatFragment() {

  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_multiple_chat, container, false);
    return view;
  }

}
