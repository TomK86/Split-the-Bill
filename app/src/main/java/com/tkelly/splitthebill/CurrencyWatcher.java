package com.tkelly.splitthebill;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * A custom TextWatcher that listens for changes to text in an EditCurrency field.
 * When a change occurs, CurrencyWatcher automatically converts the new text to
 * NumberFormat local currency formatting and sets the cursor to the end position.
 *
 * @see EditCurrency
 * @see java.text.NumberFormat
 */
public class CurrencyWatcher implements TextWatcher {
    private final WeakReference<EditText> editTextWeakReference;

    public CurrencyWatcher(EditText editText) {
        editTextWeakReference = new WeakReference<>(editText);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        EditText editText = editTextWeakReference.get();
        if (editText == null) return;
        String s = editable.toString();
        editText.removeTextChangedListener(this);
        String digits = s.replaceAll("[^0-9]", "");
        BigDecimal parsed = new BigDecimal(digits).setScale(2, BigDecimal.ROUND_FLOOR)
                .divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
        String formatted = NumberFormat.getCurrencyInstance().format(parsed);
        editText.setText(formatted);
        editText.setSelection(formatted.length());
        editText.addTextChangedListener(this);
    }
}
