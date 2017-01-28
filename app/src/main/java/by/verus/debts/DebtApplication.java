package by.verus.debts;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.activeandroid.ActiveAndroid;

public class DebtApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstRun = prefs.getBoolean("first_run", true);

        if (isFirstRun) {
            Debt.generateDebts(10);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("first_run", false);
            editor.apply();
        }
    }
}
