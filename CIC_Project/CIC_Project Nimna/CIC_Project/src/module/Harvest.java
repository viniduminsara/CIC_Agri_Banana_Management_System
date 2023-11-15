package module;

public class Harvest {

    private String date;

    private String blockName;

    private Double fullHarvest;

    private Double wastageHarvest;

    private Double remainingHarvest;

    private Double pricePerKg;

    public Harvest(String date, String blockName, Double fullHarvest, Double wastageHarvest, Double remainingHarvest, Double pricePerKg) {
        this.date = date;
        this.blockName = blockName;
        this.fullHarvest = fullHarvest;
        this.wastageHarvest = wastageHarvest;
        this.remainingHarvest = remainingHarvest;
        this.pricePerKg = pricePerKg;
    }

    public Double getPricePerKg() {
        return pricePerKg;
    }

    public void setPricePerKg(Double pricePerKg) {
        this.pricePerKg = pricePerKg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public Double getFullHarvest() {
        return fullHarvest;
    }

    public void setFullHarvest(Double fullHarvest) {
        this.fullHarvest = fullHarvest;
    }

    public Double getWastageHarvest() {
        return wastageHarvest;
    }

    public void setWastageHarvest(Double wastageHarvest) {
        this.wastageHarvest = wastageHarvest;
    }

    public Double getRemainingHarvest() {
        return remainingHarvest;
    }

    public void setRemainingHarvest(Double remainingHarvest) {
        this.remainingHarvest = remainingHarvest;
    }
}
