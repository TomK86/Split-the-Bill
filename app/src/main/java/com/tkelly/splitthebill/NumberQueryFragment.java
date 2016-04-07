package com.tkelly.splitthebill;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

/**
 * A fragment hosted by SplitActivity which allows the user to select a number of items to
 * assign payment to, then calls the onNumberPicked method implemented by SplitActivity
 *
 * @see SplitActivity
 */
public class NumberQueryFragment extends Fragment {

    private static final String ARG_QUERY = "query";
    private static final String ARG_MAX_VAL = "maxval";

    private String mQuery;
    private int mMaxVal;
    private TextView mQueryText;
    private NumberPicker mQtyPicker;
    private Button mSubmitBtn;
    private OnNumberPickedListener mListener;

    public static NumberQueryFragment newInstance(String query, int max_value) {
        NumberQueryFragment fragment = new NumberQueryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUERY, query);
        args.putInt(ARG_MAX_VAL, max_value);
        fragment.setArguments(args);
        return fragment;
    }

    public NumberQueryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mQuery = getArguments().getString(ARG_QUERY);
            mMaxVal = getArguments().getInt(ARG_MAX_VAL);
        } else if (savedInstanceState != null) {
            mQuery = savedInstanceState.getString(ARG_QUERY);
            mMaxVal = savedInstanceState.getInt(ARG_MAX_VAL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragment_view = inflater.inflate(R.layout.fragment_number_query, container, false);
        mQueryText = (TextView) fragment_view.findViewById(R.id.query_text);
        mQtyPicker = (NumberPicker) fragment_view.findViewById(R.id.qty_picker);
        mSubmitBtn = (Button) fragment_view.findViewById(R.id.submit_btn);
        return fragment_view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mQueryText.setText(mQuery);

        mQtyPicker.setMinValue(1);
        mQtyPicker.setMaxValue(mMaxVal);

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberPicked(mQtyPicker.getValue());
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(ARG_QUERY, mQuery);
        outState.putInt(ARG_MAX_VAL, mMaxVal);
    }

    public void onNumberPicked(int n) {
        if (mListener != null) {
            mListener.onNumberPicked(n);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnNumberPickedListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnNumberPickedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnNumberPickedListener {
        void onNumberPicked(int n);
    }

}
