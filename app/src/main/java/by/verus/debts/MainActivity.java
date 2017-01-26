package by.verus.debts;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
        titleEt = (EditText) findViewById(R.id.titleEt);
        sumEt = (EditText) findViewById(R.id.sumEt);
        debtsRw = (RecyclerView) findViewById(R.id.debtsRw);
        debtsRw.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Debt(titleEt.getText().toString(), Integer.parseInt(sumEt.getText().toString())).save();
                updateList();
                clearForm();
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
