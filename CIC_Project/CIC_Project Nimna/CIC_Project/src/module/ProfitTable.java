package module;

public class ProfitTable {

    private String blockName;

    private Double plant_expenses;

    private Double additional_costs;

    private Double employee_salary;

    private Double fertilizer_Expenses;


    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public Double getPlant_expenses() {
        return plant_expenses;
    }

    public void setPlant_expenses(Double plant_expenses) {
        this.plant_expenses = plant_expenses;
    }

    public Double getAdditional_costs() {
        return additional_costs;
    }

    public void setAdditional_costs(Double additional_costs) {
        this.additional_costs = additional_costs;
    }

    public Double getEmployee_salary() {
        return employee_salary;
    }

    public void setEmployee_salary(Double employee_salary) {
        this.employee_salary = employee_salary;
    }

    public Double getFertilizer_Expenses() {
        return fertilizer_Expenses;
    }

    public void setFertilizer_Expenses(Double fertilizer_Expenses) {
        this.fertilizer_Expenses = fertilizer_Expenses;
    }

    public ProfitTable(String blockName, Double plant_expenses, Double additional_costs, Double employee_salary, Double fertilizer_Expenses) {
        this.blockName = blockName;
        this.plant_expenses = plant_expenses;
        this.additional_costs = additional_costs;
        this.employee_salary = employee_salary;
        this.fertilizer_Expenses = fertilizer_Expenses;
    }
}
