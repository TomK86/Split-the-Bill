package com.tkelly.splitthebill;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

/**
 * A custom ArrayAdapter which updates the list in ItemListActivity
 *
 * @see ItemListActivity
 */
public class ItemListAdapter extends ArrayAdapter<Item> {

    public ItemListAdapter(Context context, int resource, List<Item> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.content_item_row, parent, false);
        }

        Item item = getItem(position);

        if (item != null) {
            TextView name_text = (TextView) v.findViewById(R.id.item_row_name);
            TextView qty_text = (TextView) v.findViewById(R.id.item_row_qty);
            TextView cost_text = (TextView) v.findViewById(R.id.item_row_cost);

            if (name_text != null) {
                String name = item.getName();
                name_text.setText(name);
            }
            if (qty_text != null) {
                String qty = Integer.toString(item.getQty());
                qty_text.setText(qty);
            }
            if (cost_text != null) {
                String cost = NumberFormat.getCurrencyInstance().format(item.getCost());
                cost_text.setText(cost);
            }
        }

        return v;
    }

}
