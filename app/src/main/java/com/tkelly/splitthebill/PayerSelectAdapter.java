package com.tkelly.splitthebill;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * A custom ArrayAdapter which updates the list in PayerSelectFragment
 *
 * @see PayerSelectFragment
 * @see PayerSelectViewHolder
 */
public class PayerSelectAdapter extends ArrayAdapter<Payer> {

    private ArrayList<Payer> mPayers;
    private boolean[] mChecked;
    private Context mContext;
    private int mResId;

    public PayerSelectAdapter(Context context, int resource, ArrayList<Payer> payers) {
        super(context, resource, payers);
        mContext = context;
        mResId = resource;
        mPayers = payers;
        mChecked = new boolean[payers.size()];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PayerSelectViewHolder initializedViewHolder;
        View v = convertView;
        final int idx = position;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(mResId, parent, false);
            final PayerSelectViewHolder viewHolder = new PayerSelectViewHolder();

            viewHolder.checkBox = (CheckBox) v.findViewById(R.id.payer_check);
            viewHolder.textView = (TextView) v.findViewById(R.id.payer_name);

            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mChecked[idx] = viewHolder.checkBox.isChecked();
                }
            });
            viewHolder.checkBox.setFocusable(false);

            viewHolder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = viewHolder.checkBox.isChecked();
                    viewHolder.checkBox.setChecked(!checked);
                    mChecked[idx] = !checked;
                }
            });

            v.setTag(viewHolder);
            initializedViewHolder = viewHolder;
        } else {
            initializedViewHolder = (PayerSelectViewHolder) v.getTag();
        }

        initializedViewHolder.textView.setText(mPayers.get(idx).getName());

        return v;
    }

    public boolean[] getChecked() { return mChecked; }

}
