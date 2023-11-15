package module;

public class FertilizerTable {
    private String fertilizerType;
    private String fertilizerAmount;
    private String expenses;

    public FertilizerTable(String fertilizerType,String fertilizerAmount,String expenses) {
        this.fertilizerType = fertilizerType;
        this.fertilizerAmount = fertilizerAmount;
        this.expenses = expenses;
    }

    public String getFertilizerType() {
        return fertilizerType;
    }

    public void setFertilizerType(String fertilizerType) {
        this.fertilizerType = fertilizerType;
    }

    public String getFertilizerAmount() {
        return fertilizerAmount;
    }

    public void setFertilizerAmount(String fertilizerAmount) {
        this.fertilizerAmount = fertilizerAmount;
    }

    public String getExpenses() {
        return expenses;
    }

    public void setExpenses(String expenses) {
        this.expenses = expenses;
    }
}
