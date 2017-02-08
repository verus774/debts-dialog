package by.verus.debts_pre.util;


import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

import by.verus.debts_pre.R;

public class Utils {

    public static void showSuccessSnakbar(Context context, View rootView, String message) {
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_success));
        snackbar.show();
    }

}
