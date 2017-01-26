package by.verus.debts;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class DebtHolder extends RecyclerView.ViewHolder {

    private TextView titleTv;
    private TextView sumTv;

    public DebtHolder(View itemView) {
        super(itemView);

        titleTv = (TextView) itemView.findViewById(R.id.titleTv);
        sumTv = (TextView) itemView.findViewById(R.id.sumTv);
    }

    public void bindDebt(Debt debt) {
        titleTv.setText(debt.getTitle());
        sumTv.setText(String.valueOf(debt.getSum()));
    }

}
