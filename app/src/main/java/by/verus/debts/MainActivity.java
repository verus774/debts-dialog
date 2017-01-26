package by.verus.debts;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView debtsTv;
    private EditText titleEt;
    private EditText sumEt;
    private RecyclerView debtsRw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActiveAndroid.initialize(this);

        debtsTv = (TextView) findViewById(R.id.debtsTv);

        debtsRw = (RecyclerView) findViewById(R.id.debtsRw);
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(debtsRw.getContext(), lm.getOrientation());
        debtsRw.setLayoutManager(lm);
        debtsRw.addItemDecoration(dividerItemDecoration);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context context = view.getContext();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View formElementsView = View.inflate(context, R.layout.dialog_add_debt, null);

                titleEt = (EditText) formElementsView.findViewById(R.id.titleEt);
                sumEt = (EditText) formElementsView.findViewById(R.id.sumEt);

                new AlertDialog.Builder(context)
                        .setView(formElementsView)
                        .setTitle("Create debt")
                        .setPositiveButton("Add",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        new Debt(titleEt.getText().toString(), Integer.parseInt(sumEt.getText().toString())).save();
                                        updateList();
                                        clearForm();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })
                        .show();
            }
        });

        Button clearBtn = (Button) findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Delete().from(Debt.class).execute();
                updateList();
                debtsTv.setText("");
            }
        });

        updateList();
    }

    private void updateList() {
        List<Debt> storedDebts = Debt.getAll();
        debtsRw.setAdapter(new RecyclerViewAdapter(storedDebts));
    }

    public void clearForm() {
        titleEt.setText("");
        sumEt.setText("");
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
