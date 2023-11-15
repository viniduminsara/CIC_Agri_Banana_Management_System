package module;

public class HarvestSummary {

    private String areaName;

    private Double full_harvest;

    private Double wastage_harvest;

    private Double stock_harvest;

    private Double profit;

    public HarvestSummary(String areaName, Double full_harvest, Double wastage_harvest, Double stock_harvest, Double profit) {
        this.areaName = areaName;
        this.full_harvest = full_harvest;
        this.wastage_harvest = wastage_harvest;
        this.stock_harvest = stock_harvest;
        this.profit = profit;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Double getFull_harvest() {
        return full_harvest;
    }

    public void setFull_harvest(Double full_harvest) {
        this.full_harvest = full_harvest;
    }

    public Double getWastage_harvest() {
        return wastage_harvest;
    }

    public void setWastage_harvest(Double wastage_harvest) {
        this.wastage_harvest = wastage_harvest;
    }

    public Double getStock_harvest() {
        return stock_harvest;
    }

    public void setStock_harvest(Double stock_harvest) {
        this.stock_harvest = stock_harvest;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }
}
