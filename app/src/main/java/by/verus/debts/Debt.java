package by.verus.debts;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Date;
import java.util.List;

@Table(name = "debts")
public class Debt extends Model {

    @Column(name = "name")
    private String name;

    @Column(name = "sum")
    private int sum;

    @Column(name = "timestamp", index = true)
    private Date timestamp;


    public Debt() {
        super();
    }

    public Debt(String name, int sum, Date timestamp) {
        super();

        this.name = name;
        this.sum = sum;
        this.timestamp = timestamp;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }


    public static List<Debt> getAll() {
        return new Select()
                .from(Debt.class)
                .orderBy("timestamp DESC")
                .execute();
    }

    public static Debt findById(String id) {
        return new Select()
                .from(Debt.class)
                .where("guid = ?", id)
                .executeSingle();
    }

    @Override
    public String toString() {
        return "debt: [" + this.name + ", " + this.sum + "]";
    }
}
