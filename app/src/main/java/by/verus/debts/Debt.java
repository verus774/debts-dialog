package by.verus.debts;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

@Table(name = "debts")
public class Debt extends Model {

    @Column(name = "title")
    public String title;

    @Column(name = "sum")
    public int sum;

    public Debt() {
        super();
    }

    public Debt(String title, int sum) {
        super();

        this.title = title;
        this.sum = sum;
    }

    public String getTitle() {
        return title;
    }

    public int getSum() {
        return sum;
    }

    public static List<Debt> getAll() {
        return new Select()
                .from(Debt.class)

                .orderBy("title ASC")
                .execute();
    }

    @Override
    public String toString() {
        return "debt: [" + this.title + ", " + this.sum + "]";
    }
}
