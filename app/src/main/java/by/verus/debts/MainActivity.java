package by.verus.debts;

import android.content.Context;
import android.os.Bundle;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import java.util.List;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class MainActivity extends AppCompatActivity {

    private EditText titleEt;
    private EditText sumEt;
    private RecyclerView debtsRw;
    private CoordinatorLayout mCoordinatorLayout;
    private AwesomeValidation mAwesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActiveAndroid.initialize(this);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        debtsRw = (RecyclerView) findViewById(R.id.debtsRw);

        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(debtsRw.getContext(), lm.getOrientation());
        debtsRw.setLayoutManager(lm);
        debtsRw.addItemDecoration(dividerItemDecoration);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Context context = view.getContext();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View formElementsView = View.inflate(context, R.layout.dialog_add_debt, null);

                titleEt = (EditText) formElementsView.findViewById(R.id.titleEt);
                sumEt = (EditText) formElementsView.findViewById(R.id.sumEt);

                mAwesomeValidation = new AwesomeValidation(BASIC);
                mAwesomeValidation.addValidation(titleEt, RegexTemplate.NOT_EMPTY, getString(R.string.err_required));
                mAwesomeValidation.addValidation(sumEt, RegexTemplate.NOT_EMPTY, getString(R.string.err_required));

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
                            new Debt(titleEt.getText().toString(), Integer.parseInt(sumEt.getText().toString())).save();
                            updateList();
                            dialog.dismiss();

                            Snackbar snackbar = Snackbar.make(mCoordinatorLayout, "Debt added", Snackbar.LENGTH_SHORT);
                            View snackBarView = snackbar.getView();
                            snackBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_success));
                            snackbar.show();
                        }
                    }
                });

            }
        });

        Button clearBtn = (Button) findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Delete().from(Debt.class).execute();
                updateList();
            }
        });

        updateList();
    }

    private void updateList() {
        List<Debt> storedDebts = Debt.getAll();
        debtsRw.setAdapter(new RecyclerViewAdapter(storedDebts));
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
