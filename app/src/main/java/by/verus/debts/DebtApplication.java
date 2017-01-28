package by.verus.debts;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

import java.util.Date;

public class DebtApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);

        ActiveAndroid.beginTransaction();
        try {
            for (int i = 1; i <= 20; i++) {
                new Debt("Vasya " + i, 100, new Date()).save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }
}
