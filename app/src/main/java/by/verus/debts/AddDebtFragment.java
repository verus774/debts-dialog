package by.verus.debts;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.activeandroid.query.Update;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;


public class AddDebtFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private EditText mSumEt;
    private EditText mNameEt;
    private Button mDateBtn;
    private AwesomeValidation mAwesomeValidation;

    private final static int CONTACT_PICKER = 1;

    private static final String ARG_TITLE = "title";
    private static final String ARG_DEBT_ID = "debtId";


    public AddDebtFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString(ARG_TITLE);
        final long debtId = getArguments().getLong(ARG_DEBT_ID);

        View formElementsView = View.inflate(getActivity(), R.layout.fragment_add_debt, null);

        mNameEt = (EditText) formElementsView.findViewById(R.id.nameEt);
        mSumEt = (EditText) formElementsView.findViewById(R.id.sumEt);
        mDateBtn = (Button) formElementsView.findViewById(R.id.dateBtn);

        if (debtId != 0) {
            Debt debt = Debt.findById(debtId);

            mNameEt.append(debt.getName());
            mSumEt.append(String.valueOf(debt.getSum()));
            mDateBtn.setText(getDateStr(debt.getTimestamp()));
        } else {
            mDateBtn.setText(getDateStr(new Date()));
        }

        mDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(new Date());
            }
        });


        mAwesomeValidation = new AwesomeValidation(BASIC);
        mAwesomeValidation.addValidation(mNameEt, RegexTemplate.NOT_EMPTY, getString(R.string.err_required));
        mAwesomeValidation.addValidation(mSumEt, RegexTemplate.NOT_EMPTY, getString(R.string.err_required));

        ImageView mAddContactIb = (ImageView) formElementsView.findViewById(R.id.addContactIb);
        mAddContactIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickContact();
            }
        });

        final AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(formElementsView)
                .setTitle(title)
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAwesomeValidation.validate()) {
                    if (debtId == 0) {
                        new Debt(
                                mNameEt.getText().toString(),
                                Integer.parseInt(mSumEt.getText().toString()),
                                new Date()
                        ).save();
                    } else {
                        new Update(Debt.class)
                                .set("name=?," + "sum=?", mNameEt.getText(), Integer.valueOf(mSumEt.getText().toString()))
                                .where("Id=?", debtId)
                                .execute();
                    }

                    dialog.dismiss();
                    MainActivity.updateList();
                    MainActivity.showSuccessSnackbar(getActivity(), getString(R.string.debt_saved));
                }
            }
        });

        return dialog;
    }

    public static AddDebtFragment newInstance(String title) {
        AddDebtFragment fragment = new AddDebtFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        fragment.setArguments(args);

        return fragment;
    }

    public static AddDebtFragment newInstance(String title, long debtId) {
        AddDebtFragment fragment = new AddDebtFragment();
        Bundle args = new Bundle();

        args.putString(ARG_TITLE, title);
        args.putLong(ARG_DEBT_ID, debtId);

        fragment.setArguments(args);

        return fragment;
    }

    private void pickContact() {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent, CONTACT_PICKER);
    }

    private String getContactName(Intent data) {
        Uri contactData = data.getData();
        Cursor cursor = getActivity().getContentResolver().query(contactData, null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
            }
            cursor.close();
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CONTACT_PICKER:
                    mNameEt.setText("");
                    mNameEt.append(getContactName(data));
                    break;
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar newDate = Calendar.getInstance();
        newDate.set(year, month, dayOfMonth);

        mDateBtn.setText(getDateStr(newDate.getTime()));
    }

    private void showDatePickerDialog(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(getActivity(), this, year, month, day).show();
    }

    private String getDateStr(Date date) {
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH);
        return df.format(date);
    }

}