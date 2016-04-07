package com.tkelly.splitthebill;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * An activity which allows the user to split a bill evenly among all of the party members.
 */
public class EvenSplitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_even_split);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_activity_even_split);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final TextView result_text = (TextView) findViewById(R.id.result_text);
        final EditCurrency total_edit = (EditCurrency) findViewById(R.id.total_edit);
        final NumberPicker payers_edit = (NumberPicker) findViewById(R.id.payers_edit);
        payers_edit.setMinValue(1);
        payers_edit.setMaxValue(100);

        Button submit_btn = (Button) findViewById(R.id.submit_btn);
        submit_btn.setActivated(true);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    double amount = total_edit.getTextAsDouble() / payers_edit.getValue();
                    if (amount == 0d) {
                        makeToast(R.string.error_total_zero);
                    } else {
                        result_text.setText(Html.fromHtml("<b>Each person owes " +
                                NumberFormat.getCurrencyInstance().format(amount) +
                                "</b><br/>10% tip ... " +
                                NumberFormat.getCurrencyInstance().format(amount * 0.1d) +
                                "<br/>15% tip ... " +
                                NumberFormat.getCurrencyInstance().format(amount * 0.15d) +
                                "<br/>20% tip ... " +
                                NumberFormat.getCurrencyInstance().format(amount * 0.2d) +
                                "<br/><br/><b>Don't forget to tip your server!</b>"));
                    }
                } catch (ParseException e) {
                    makeToast(R.string.error_currency_format);
                }
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
