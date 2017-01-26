package by.verus.debts;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<DebtHolder> {

    private List<Debt> mDebts;

    public RecyclerViewAdapter(List<Debt> debts) {
        this.mDebts = debts;
    }

    @Override
    public DebtHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_debt, parent, false);
        return new DebtHolder(view);
    }

    @Override
    public void onBindViewHolder(DebtHolder holder, int position) {
        Debt debt = mDebts.get(position);
        holder.bindDebt(debt);
    }

    @Override
    public int getItemCount() {
        return mDebts.size();
    }
}
