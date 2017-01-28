package by.verus.debts;


import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Locale;

public class DebtHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView nameTv;
    private TextView sumTv;
    private TextView dateTv;
    private Debt mDebt;

    public DebtHolder(View itemView) {
        super(itemView);

        nameTv = (TextView) itemView.findViewById(R.id.nameTv);
        sumTv = (TextView) itemView.findViewById(R.id.sumTv);
        dateTv = (TextView) itemView.findViewById(R.id.dateTv);

        itemView.setOnClickListener(this);
    }

    public void bindDebt(Debt debt) {
        mDebt = debt;

        nameTv.setText(debt.getName());
        sumTv.setText(String.valueOf(debt.getSum()));

        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH);
        String strDate = df.format(debt.getTimestamp());
        dateTv.setText(strDate);
    }

    @Override
    public void onClick(View v) {
        View formElementsView = View.inflate(v.getContext(), R.layout.dialog_add_update_debt, null);

        EditText nameEt = (EditText) formElementsView.findViewById(R.id.nameEt);
        EditText sumEt = (EditText) formElementsView.findViewById(R.id.sumEt);

        nameEt.append(mDebt.getName());
        sumEt.append(String.valueOf(mDebt.getSum()));

        new AlertDialog.Builder(v.getContext())
                .setView(formElementsView)
                .setTitle("Edit debt")
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
