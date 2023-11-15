package module;

public class ManageBlockSummary {

    private String areaName;

    private String Date;

    private String fertilizer_type;

    private Double fertilizer_siize;

    private int plant_quantaty;

    public ManageBlockSummary(String areaName, String date, String fertilizer_type, Double fertilizer_siize, int plant_quantaty) {
        this.areaName = areaName;
        Date = date;
        this.fertilizer_type = fertilizer_type;
        this.fertilizer_siize = fertilizer_siize;
        this.plant_quantaty = plant_quantaty;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getFertilizer_type() {
        return fertilizer_type;
    }

    public void setFertilizer_type(String fertilizer_type) {
        this.fertilizer_type = fertilizer_type;
    }

    public Double getFertilizer_siize() {
        return fertilizer_siize;
    }

    public void setFertilizer_siize(Double fertilizer_siize) {
        this.fertilizer_siize = fertilizer_siize;
    }

    public int getPlant_quantaty() {
        return plant_quantaty;
    }

    public void setPlant_quantaty(int plant_quantaty) {
        this.plant_quantaty = plant_quantaty;
    }
}
