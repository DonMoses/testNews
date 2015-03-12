package com.example.moses.mosesnews.contents;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.moses.mosesnews.R;

/**
 * Created by Moses on 2015
 */
public class EmptyFragment extends Fragment {
    String emptyInfo;
    TextView mEmptyTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empty, container, false);
        mEmptyTxt = (TextView) view.findViewById(R.id.empty_fragment_txt);
        mEmptyTxt.setText(emptyInfo);
        return view;
    }

    public static EmptyFragment newInstance(String emptyInfo) {
        EmptyFragment emptyFragment = new EmptyFragment();
        Bundle args = new Bundle();
        args.putString("empty_info", emptyInfo);
        emptyFragment.setArguments(args);
        return emptyFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        emptyInfo = bundle.getString("empty_info");
    }
}
