package by.verus.debts;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import by.verus.debts.fragment.AddDebtFragment;

public class MainActivity extends AppCompatActivity {

    private static RecyclerView mDebtsRv;
    private static RecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                showAddDebtDialog();
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
            Debt.deleteAll();
            updateList();
            return true;
        } else if (id == R.id.action_generate_debts) {
            Debt.generateDebts(10);
            updateList();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showAddDebtDialog() {
        FragmentManager fm = getSupportFragmentManager();
        AddDebtFragment addDebtFragment = AddDebtFragment.newInstance(getString(R.string.add_debt));
        addDebtFragment.show(fm, "addDebtDialog");
    }

}
