package module;

public class Fertilizer {
    private String fertilizerType;

    public Fertilizer(String fertilizerType) {
        this.setFertilizerType(fertilizerType);
    }

    public String getFertilizerType() {
        return fertilizerType;
    }

    public void setFertilizerType(String fertilizerType) {
        this.fertilizerType = fertilizerType;
    }
}
