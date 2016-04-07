package com.tkelly.splitthebill;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * An activity which allows the user to split up a bill by line item.  This activity hosts a
 * chain of fragments that allows the user to do a number of things:
 *  - Start PayerListActivity to add/remove members from their party (SplitFragment)
 *  - Start ItemListActivity to add/remove list items from their bill (SplitFragment)
 *  - Select a line item to assign payments to (ItemSelectFragment)
 *  - Select one or more party members to pay for a selected line item (PayerSelectFragment)
 *  - Choose a partial quantity of the selected line item to assign payments to (NumberQueryFragment)
 *
 *  @see SplitFragment
 *  @see ItemSelectFragment
 *  @see PayerSelectFragment
 *  @see NumberQueryFragment
 */
public class SplitActivity extends AppCompatActivity
        implements SplitFragment.OnSplitButtonPressedListener,
        NumberQueryFragment.OnNumberPickedListener,
        PayerSelectFragment.OnPayerSelectListener,
        ItemSelectFragment.OnItemSelectListener,
        ItemSelectFragment.OnFinishListener {

    private static final String ARG_SELECTED = "selected";
    private static final String ARG_RESELECTED = "reselected";
    private static final String ARG_CURRENT_ITEM = "current_item";
    private static final String ARG_CURRENT_QTY = "current_qty";
    private static final String ARG_INPUT_QTY = "input_qty";

    protected static final String ID_SPLIT = "split";
    protected static final String ID_ITEM_SELECT = "item_select";
    protected static final String ID_PAYER_SELECT = "payer_select";
    protected static final String ID_PAYER_RESELECT = "payer_reselect";
    protected static final String ID_NUMBER_QUERY = "number_query";

    private MyApplication mApp;
    private FragmentManager mMgr;
    private ArrayList<Integer> mSelected, mReselected;
    private int mCurrentItem, mCurrentQty, mInputQty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.title_activity_split);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mApp = (MyApplication) getApplication();
        mMgr = getSupportFragmentManager();

        if (savedInstanceState == null) {
            mSelected = new ArrayList<>();
            mReselected = new ArrayList<>();
            mCurrentItem = -1;
            mCurrentQty = 0;
            mInputQty = 0;
            mApp.clearAmtsOwed();
            mApp.clearAllPayments();
            mApp.clearCompleted();
            clearBackStack();
            startFragment(ID_SPLIT);
        } else {
            mSelected = arrayToList(savedInstanceState.getIntArray(ARG_SELECTED));
            mReselected = arrayToList(savedInstanceState.getIntArray(ARG_RESELECTED));
            mCurrentItem = savedInstanceState.getInt(ARG_CURRENT_ITEM);
            mCurrentQty = savedInstanceState.getInt(ARG_CURRENT_QTY);
            mInputQty = savedInstanceState.getInt(ARG_INPUT_QTY);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putIntArray(ARG_SELECTED, listToArray(mSelected));
        outState.putIntArray(ARG_RESELECTED, listToArray(mReselected));
        outState.putInt(ARG_CURRENT_ITEM, mCurrentItem);
        outState.putInt(ARG_CURRENT_QTY, mCurrentQty);
        outState.putInt(ARG_INPUT_QTY, mInputQty);
    }

    @Override
    public void onBackPressed() {
        if (mMgr.getBackStackEntryCount() > 1) {
            mMgr.popBackStack();
        } else {
            NavUtils.navigateUpFromSameTask(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mMgr.getBackStackEntryCount() > 1) {
                    mMgr.popBackStack();
                    return true;
                } else {
                    return super.onOptionsItemSelected(item);
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Callback method for SplitFragment - called when the user presses the "Split" button
     */
    @Override
    public void onSplitButtonPressed() {
        mCurrentItem = -1;
        mCurrentQty = 0;
        mInputQty = 0;
        startFragment(ID_ITEM_SELECT);
    }

    /**
     * Callback method for NumberQueryFragment - called when the user presses the "Next" button
     *
     * @param n The number chosen by the user
     */
    @Override
    public void onNumberPicked(int n) {
        mInputQty = n;
        startFragment(ID_PAYER_RESELECT);
    }

    /**
     * Callback method for PayerSelectFragment - called when the user presses the "Next" button
     *
     * @param checked The boolean status of the CheckBoxes next to each of the listed payers
     * @param init If true, checked represents all of the party members; If false, checked
     *             represents only the previously-selected party members
     */
    @Override
    public void onPayerSelect(boolean[] checked, boolean init) {
        if (init) {
            mSelected.clear();
            for (int i = 0; i < checked.length; i++) {
                if (checked[i]) { mSelected.add(i); }
            }
            paySelectedIfOk();
        } else {
            mReselected.clear();
            for (int i = 0; i < checked.length; i++) {
                if (checked[i]) { mReselected.add(mSelected.get(i)); }
            }
            payReselected();
        }
    }

    /**
     * Callback method for ItemSelectFragment - called when the user selects a line item
     *
     * @param idx The index of the selected line item
     */
    @Override
    public void onItemSelect(int idx) {
        Item item = mApp.getItem(idx);
        mCurrentItem = idx;
        mCurrentQty = item.getQty();
        item.setCompleted(false);
        item.clearPayments();
        startFragment(ID_PAYER_SELECT);
    }

    /**
     * Callback method for ItemSelectFragment - called when the user presses the "Finish" button
     */
    @Override
    public void onFinish() {
        mCurrentItem = -1;
        mCurrentQty = 0;
        mInputQty = 0;
        mSelected.clear();
        mReselected.clear();
        mApp.updateAmtsOwed();
        clearBackStack();
        startFragment(ID_SPLIT);
    }

    /**
     * Method to add a specified fragment to the activity's fragment container
     *
     * @param fragment_id The ID string of the fragment to start
     */
    protected void startFragment(String fragment_id) {
        if (findViewById(R.id.fragment_container) != null) {
            String query;
            Resources res = getResources();
            Item curItem = null;
            if (mCurrentItem >= 0) { curItem = mApp.getItem(mCurrentItem); }

            switch (fragment_id) {
                case ID_SPLIT:
                    SplitFragment split = SplitFragment.newInstance(mApp.getTax());
                    mMgr.beginTransaction()
                            .replace(R.id.fragment_container, split, fragment_id)
                            .addToBackStack(fragment_id)
                            .commit();
                    break;
                case ID_ITEM_SELECT:
                    if (mApp.allItemsAreCompleted()) {
                        query = res.getString(R.string.query_item_select_done);
                    } else if (mCurrentItem == -1) {
                        query = res.getString(R.string.query_item_select_first);
                    } else {
                        query = res.getString(R.string.query_item_select_next);
                    }
                    ItemSelectFragment item_select = ItemSelectFragment.newInstance(query);
                    mMgr.beginTransaction()
                            .replace(R.id.fragment_container, item_select, fragment_id)
                            .addToBackStack(fragment_id)
                            .commit();
                    break;
                case ID_PAYER_SELECT:
                    if (curItem == null) { break; }
                    query = String.format(res.getString(R.string.query_payer_select),
                            curItem.getName());
                    PayerSelectFragment payer_select = PayerSelectFragment.newInstance(query,
                            new int[0], true);
                    mMgr.beginTransaction()
                            .replace(R.id.fragment_container, payer_select, fragment_id)
                            .addToBackStack(fragment_id)
                            .commit();
                    break;
                case ID_PAYER_RESELECT:
                    if (curItem == null) { break; }
                    if (mInputQty == 1) {
                        query = String.format(res.getString(R.string.query_payer_reselect_one),
                                curItem.getName());
                    } else {
                        query = String.format(res.getString(R.string.query_payer_reselect_more),
                                mInputQty, curItem.getName());
                    }
                    PayerSelectFragment payer_reselect = PayerSelectFragment.newInstance(query,
                            listToArray(mSelected), false);
                    mMgr.beginTransaction()
                            .add(R.id.fragment_container, payer_reselect, fragment_id)
                            .addToBackStack(fragment_id)
                            .commit();
                    break;
                case ID_NUMBER_QUERY:
                    if (curItem == null) { break; }
                    query = String.format(res.getString(R.string.query_number), curItem.getName());
                    NumberQueryFragment number_query = NumberQueryFragment.newInstance(query,
                            mCurrentQty);
                    mMgr.beginTransaction()
                            .add(R.id.fragment_container, number_query, fragment_id)
                            .addToBackStack(fragment_id)
                            .commit();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Method to assign payment for the selected line item evenly among the party members
     * in mSelected
     */
    protected void paySelected() {
        if (mCurrentItem >= 0) {
            Item curItem = mApp.getItem(mCurrentItem);
            double costPerPayer = curItem.getCost() * mCurrentQty / mSelected.size();
            for (int payerIdx : mSelected) {
                curItem.addPayment(payerIdx, costPerPayer);
            }
            curItem.setCompleted(true);
            mCurrentQty = 0;
            mInputQty = 0;
            startFragment(ID_ITEM_SELECT);
        }
    }

    /**
     * Method which calls the paySelected method if the user has selected only one party
     * member to pay for the selected line item, or if the user indicates that they want to
     * split the payment for the selected line item evenly among the selected party members.
     * Otherwise, an instance of NumberQueryFragment is started.
     */
    protected void paySelectedIfOk() {
        if (mCurrentItem >= 0) {
            Item curItem = mApp.getItem(mCurrentItem);
            curItem.clearPayments();
            mCurrentQty = curItem.getQty();
            if (mSelected.size() == 1) {
                paySelected();
            } else {
                final Dialog dialog = new Dialog(SplitActivity.this);
                dialog.setContentView(R.layout.dialog_confirm);
                dialog.setTitle("Split evenly?");
                dialog.setCancelable(true);

                TextView query_text = (TextView) dialog.findViewById(R.id.query_text);
                String query = String.format(getResources().getString(R.string.query_even_split),
                        curItem.getQty(), curItem.getName(), mSelected.size());
                query_text.setText(query);

                Button yes_btn = (Button) dialog.findViewById(R.id.yes_btn);
                yes_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        paySelected();
                    }
                });

                Button no_btn = (Button) dialog.findViewById(R.id.no_btn);
                no_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (mCurrentQty == 1) {
                            mInputQty = 1;
                            startFragment(ID_PAYER_RESELECT);
                        } else {
                            startFragment(ID_NUMBER_QUERY);
                        }
                    }
                });

                dialog.show();
            }
        }
    }

    /**
     * Method to assign payment for the user-indicated quantity of the selected line item
     * evenly among the party members in mReselected
     */
    protected void payReselected() {
        if (mCurrentItem >= 0) {
            Item curItem = mApp.getItem(mCurrentItem);
            double costPerPayer = curItem.getCost() * mInputQty / mReselected.size();
            for (int payerIdx : mReselected) {
                curItem.addPayment(payerIdx, costPerPayer);
            }
            mCurrentQty -= mInputQty;
            if (mCurrentQty == 0) {
                curItem.setCompleted(true);
                mInputQty = 0;
                startFragment(ID_ITEM_SELECT);
            } else {
                startFragment(ID_NUMBER_QUERY);
            }
        }
    }

    /**
     * Method to clear the fragment back stack
     */
    protected void clearBackStack() {
        while (mMgr.getBackStackEntryCount() > 0) {
            mMgr.popBackStackImmediate();
        }
    }

    /**
     * Method to convert an ArrayList of Integer objects into an array of primitive ints
     *
     * @param list The ArrayList of Integers to convert
     * @return The converted array of ints
     */
    protected int[] listToArray(ArrayList<Integer> list) {
        int[] array = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    /**
     * Method to convert an array of primitive ints into an ArrayList of Integer objects
     *
     * @param array The array of ints to convert
     * @return The converted ArrayList of Integers
     */
    protected ArrayList<Integer> arrayToList(int[] array) {
        ArrayList<Integer> list = new ArrayList<>();
        if (array != null) {
            for (int i : array) {
                list.add(i);
            }
        }
        return list;
    }

}
