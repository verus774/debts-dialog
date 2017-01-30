package by.verus.debts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.activeandroid.query.Delete;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import java.util.Date;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class MainActivity extends AppCompatActivity {

    private EditText mNameEt;
    private EditText mSumEt;
    private ImageView mAddContactIv;
    private static RecyclerView mDebtsRv;
    private CoordinatorLayout mCoordinatorLayout;
    private AwesomeValidation mAwesomeValidation;
    private static RecyclerViewAdapter mAdapter = null;

    private final static int CONTACT_PICKER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        mDebtsRv = (RecyclerView) findViewById(R.id.debtsRv);

        LinearLayoutManager lm = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mDebtsRv.getContext(), lm.getOrientation());
        mDebtsRv.setLayoutManager(new LinearLayoutManager(this));
        mDebtsRv.addItemDecoration(dividerItemDecoration);
        updateList();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Context context = view.getContext();
                final View formElementsView = View.inflate(context, R.layout.dialog_add_update_debt, null);

                mNameEt = (EditText) formElementsView.findViewById(R.id.nameEt);
                mSumEt = (EditText) formElementsView.findViewById(R.id.sumEt);

                mAddContactIv = (ImageView) formElementsView.findViewById(R.id.addContactIv);
                mAddContactIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pickContact();
                        // TODO get contact name
                    }
                });

                mAwesomeValidation = new AwesomeValidation(BASIC);
                mAwesomeValidation.addValidation(mNameEt, RegexTemplate.NOT_EMPTY, getString(R.string.err_required));
                mAwesomeValidation.addValidation(mSumEt, RegexTemplate.NOT_EMPTY, getString(R.string.err_required));

                final AlertDialog dialog = new AlertDialog.Builder(context)
                        .setView(formElementsView)
                        .setTitle("Create debt")
                        .setPositiveButton("Save", null)
                        .setNegativeButton("Cancel", null)
                        .create();

                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mAwesomeValidation.validate()) {
                            new Debt(
                                    mNameEt.getText().toString(),
                                    Integer.parseInt(mSumEt.getText().toString()),
                                    new Date()
                            ).save();
                            dialog.dismiss();
                            updateList();

                            Snackbar snackbar = Snackbar.make(mCoordinatorLayout, "Debt added", Snackbar.LENGTH_SHORT);
                            View snackBarView = snackbar.getView();
                            snackBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_success));
                            snackbar.show();
                        }
                    }
                });

            }
        });

    }

    public static void updateList() {
        mAdapter = new RecyclerViewAdapter(Debt.getAll());
        mDebtsRv.setAdapter(mAdapter);

        /*if (mAdapter == null) {
            mAdapter = new RecyclerViewAdapter(Debt.getAll());
            mDebtsRv.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear_all) {
            new Delete().from(Debt.class).execute();
            updateList();
            return true;
        } else if (id == R.id.action_generate_debts) {
            Debt.generateDebts(10);
            updateList();
        }

        return super.onOptionsItemSelected(item);
    }

    public void pickContact() {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent, CONTACT_PICKER);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CONTACT_PICKER:
                    // TODO
                    break;
            }
        }
    }

}
