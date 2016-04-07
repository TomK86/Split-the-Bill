package com.tkelly.splitthebill;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * A custom ArrayAdapter which updates the list in ItemSelectFragment
 *
 * @see ItemSelectFragment
 * @see ItemSelectViewHolder
 */
public class ItemSelectAdapter extends ArrayAdapter<Item> {

    private ArrayList<Item> mItems;
    private Context mContext;
    private int mResId;

    public ItemSelectAdapter(Context context, int resource, ArrayList<Item> items) {
        super(context, resource, items);
        mContext = context;
        mResId = resource;
        mItems = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ItemSelectViewHolder initializedViewHolder;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(mResId, parent, false);
            final ItemSelectViewHolder viewHolder = new ItemSelectViewHolder();

            viewHolder.nameText = (TextView) v.findViewById(R.id.item_row_name);
            viewHolder.qtyText = (TextView) v.findViewById(R.id.item_row_qty);
            viewHolder.costText = (TextView) v.findViewById(R.id.item_row_cost);

            v.setTag(viewHolder);
            initializedViewHolder = viewHolder;
        } else {
            initializedViewHolder = (ItemSelectViewHolder) v.getTag();
        }

        if (mItems.get(position).isCompleted()) {
            initializedViewHolder.nameText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.check_mark, 0, 0, 0);
        } else {
            initializedViewHolder.nameText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }

        String name = mItems.get(position).getName();
        String qty = Integer.toString(mItems.get(position).getQty());
        String cost = NumberFormat.getCurrencyInstance().format(mItems.get(position).getCost());

        initializedViewHolder.nameText.setText(name);
        initializedViewHolder.qtyText.setText(qty);
        initializedViewHolder.costText.setText(cost);

        return v;
    }
}
