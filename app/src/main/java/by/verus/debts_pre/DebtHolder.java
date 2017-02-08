package by.verus.debts_pre;


import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.activeandroid.query.Delete;

import by.verus.debts_pre.fragment.AddDebtFragment;
import by.verus.debts_pre.util.DateUtils;
import by.verus.debts_pre.util.Utils;

public class DebtHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView mNameTv;
    private TextView mSumTv;
    private TextView mMoreTv;
    private TextView mDateTv;
    private Debt mDebt;

    public DebtHolder(View itemView) {
        super(itemView);

        mNameTv = (TextView) itemView.findViewById(R.id.nameTv);
        mSumTv = (TextView) itemView.findViewById(R.id.sumTv);
        mDateTv = (TextView) itemView.findViewById(R.id.dateTv);
        mMoreTv = (TextView) itemView.findViewById(R.id.moreTv);

        itemView.setOnClickListener(this);
    }

    public void bindDebt(Debt debt) {
        mDebt = debt;

        mNameTv.setText(debt.getName());
        mSumTv.setText(String.valueOf(debt.getSum()));
        mDateTv.setText(DateUtils.getStrFromDate(debt.getTimestamp()));

        mMoreTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Context context = v.getContext();

                PopupMenu popup = new PopupMenu(context, v);
                popup.inflate(R.menu.menu_item_debt);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_delete:
                                new Delete()
                                        .from(Debt.class)
                                        .where("Id=?", mDebt.getId())
                                        .execute();

                                MainActivity.updateList();

                                View cl = ((MainActivity) context).findViewById(R.id.coordinatorLayout);
                                Utils.showSuccessSnakbar(context, cl, context.getString(R.string.success_deleted));
                                break;
                        }
                        return false;
                    }
                });

                popup.show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        final Context context = v.getContext();
        FragmentManager fm = ((MainActivity) context).getSupportFragmentManager();
        AddDebtFragment addDebtFragment = AddDebtFragment.newInstance(context.getString(R.string.edit_debt), mDebt.getId());
        addDebtFragment.show(fm, "editDebtDialog");
    }
}
