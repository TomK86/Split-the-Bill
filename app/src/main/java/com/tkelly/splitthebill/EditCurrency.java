package com.tkelly.splitthebill;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * An extension of EditText that automatically formats the displayed text with NumberFormat
 * local currency formatting.  Uses CurrencyWatcher to listen for text changes and update as
 * necessary.
 *
 * @see CurrencyWatcher
 * @see java.text.NumberFormat
 */
public class EditCurrency extends EditText {

    public EditCurrency(Context context) {
        super(context);
        init();
    }

    public EditCurrency(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditCurrency(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * A method to get the currency-formatted text in the field as a double value
     *
     * @return The double value of the text in the EditCurrency field
     * @throws ParseException If the text in the EditCurrency field is not currency-formatted
     */
    public double getTextAsDouble() throws ParseException {
        String s = getText().toString();
        return NumberFormat.getCurrencyInstance().parse(s).doubleValue();
    }

    private void init() {
        String zero_currency = NumberFormat.getCurrencyInstance().format(0);
        setInputType(InputType.TYPE_CLASS_NUMBER);
        setText(zero_currency);
        setSelection(zero_currency.length());

        addTextChangedListener(new CurrencyWatcher(this));

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelection(getText().length());
            }
        });
    }

}
