package by.verus.debts;


import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.activeandroid.query.Delete;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DebtHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView nameTv;
    private TextView sumTv;
    private TextView moreTv;
    private TextView dateTv;
    private Debt mDebt;

    public DebtHolder(View itemView) {
        super(itemView);

        nameTv = (TextView) itemView.findViewById(R.id.nameTv);
        sumTv = (TextView) itemView.findViewById(R.id.sumTv);
        dateTv = (TextView) itemView.findViewById(R.id.dateTv);
        moreTv = (TextView) itemView.findViewById(R.id.moreTv);

        itemView.setOnClickListener(this);
    }

    public void bindDebt(Debt debt) {
        mDebt = debt;

        nameTv.setText(debt.getName());
        sumTv.setText(String.valueOf(debt.getSum()));

        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);
        String strDate = sdf.format(debt.getTimestamp());
        dateTv.setText(strDate);

        moreTv.setOnClickListener(new View.OnClickListener() {
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
                                MainActivity.showSuccessSnackbar(context, context.getString(R.string.success_deleted));
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
