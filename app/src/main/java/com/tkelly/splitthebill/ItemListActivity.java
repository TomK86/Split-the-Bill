package com.tkelly.splitthebill;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import java.text.ParseException;

/**
 * An activity which allows the user to add and remove line items from their bill
 *
 * @see ItemListAdapter
 */
public class ItemListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_activity_item_list);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final ItemListAdapter adapter = new ItemListAdapter(getApplicationContext(),
                R.layout.content_item_row, ((MyApplication) getApplication()).getItems());
        ListView item_list = (ListView) findViewById(android.R.id.list);
        item_list.setAdapter(adapter);

        item_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int idx = position;
                final Dialog dialog = new Dialog(ItemListActivity.this);
                dialog.setContentView(R.layout.dialog_confirm);
                dialog.setTitle("Remove a line item");
                dialog.setCancelable(true);

                TextView query_text = (TextView) dialog.findViewById(R.id.query_text);
                String query = String.format(getResources().getString(R.string.confirm_remove_item),
                        adapter.getItem(idx).getName());
                query_text.setText(query);

                Button yes_btn = (Button) dialog.findViewById(R.id.yes_btn);
                yes_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.remove(adapter.getItem(idx));
                        dialog.dismiss();
                    }
                });

                Button no_btn = (Button) dialog.findViewById(R.id.no_btn);
                no_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(ItemListActivity.this);
                dialog.setContentView(R.layout.dialog_add_item);
                dialog.setTitle("Add a new line item");
                dialog.setCancelable(true);

                final EditCurrency cost_edit = (EditCurrency) dialog.findViewById(R.id.cost_edit);
                final EditText name_edit = (EditText) dialog.findViewById(R.id.name_edit);
                final NumberPicker qty_edit = (NumberPicker) dialog.findViewById(R.id.qty_edit);
                qty_edit.setMinValue(1);
                qty_edit.setMaxValue(50);

                Button submit_btn = (Button) dialog.findViewById(R.id.submit_btn);
                submit_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            String name = name_edit.getText().toString();
                            int qty = qty_edit.getValue();
                            double cost = cost_edit.getTextAsDouble();
                            if (name.isEmpty()) {
                                makeToast(R.string.error_add_item_name_empty);
                            } else if (cost == 0) {
                                makeToast(R.string.error_add_item_cost_zero);
                            } else {
                                adapter.add(new Item(cost, qty, name));
                                dialog.dismiss();
                            }
                        } catch (ParseException e) {
                            makeToast(R.string.error_currency_format);
                        }
                    }
                });

                dialog.show();
            }
        });
    }

    protected void makeToast(int s) {
        Toast toast = Toast.makeText(getApplicationContext(),
                getResources().getString(s), Toast.LENGTH_SHORT);
        TextView toastText = (TextView) ((LinearLayout) toast.getView()).getChildAt(0);
        toastText.setGravity(Gravity.CENTER_HORIZONTAL);
        toast.show();
    }

}
