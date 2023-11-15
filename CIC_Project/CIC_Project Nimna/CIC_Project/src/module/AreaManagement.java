package module;

public class AreaManagement {


        private String date;
        private String block_name;
        private String plant_qty;
        private String plant_type;

    public String getPlant_type() {
        return plant_type;
    }

    public void setPlant_type(String plant_type) {
        this.plant_type = plant_type;
    }

    public AreaManagement(String date, String block_name, String plant_qty, String plant_type) {
        this.date = date;
        this.block_name = block_name;
        this.plant_qty = plant_qty;
        this.plant_type = plant_type;
    }

    public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getBlock_name() {
            return block_name;
        }

        public void setBlock_name(String block_name) {
            this.block_name = block_name;
        }

        public String getPlant_qty() {
            return plant_qty;
        }

        public void setPlant_qty(String plant_qty) {
            this.plant_qty = plant_qty;
        }

}

