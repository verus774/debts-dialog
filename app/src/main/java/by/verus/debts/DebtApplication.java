package by.verus.debts;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

public class DebtApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }
}
