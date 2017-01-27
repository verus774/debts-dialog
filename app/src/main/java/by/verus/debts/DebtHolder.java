package by.verus.debts;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Locale;

public class DebtHolder extends RecyclerView.ViewHolder {

    private TextView nameTv;
    private TextView sumTv;
    private TextView dateTv;

    public DebtHolder(View itemView) {
        super(itemView);

        nameTv = (TextView) itemView.findViewById(R.id.nameTv);
        sumTv = (TextView) itemView.findViewById(R.id.sumTv);
        dateTv = (TextView) itemView.findViewById(R.id.dateTv);
    }

    public void bindDebt(Debt debt) {
        nameTv.setText(debt.getName());
        sumTv.setText(String.valueOf(debt.getSum()));

        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH);
        String strDate = df.format(debt.getTimestamp());
        dateTv.append(strDate);
    }

}
