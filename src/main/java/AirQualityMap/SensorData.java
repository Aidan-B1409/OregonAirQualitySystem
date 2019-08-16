package AirQualityMap;

public class SensorData {
    public static class Builder{
        private double PM2_5;
        private double lat;
        private double lon;
        private String name;
        private double id;

        public Builder(double id){
            this.id = id;
        }
        public Builder atGeo(double lat, double lon){
            this.lat = lat;
            this.lon = lon;
            return this;
        }
        public Builder ofName(String name){
            this.name = name;
            return this;
        }
        public Builder withValue(double PM2_5){
            this.PM2_5 = PM2_5;
            return this;
        }

        public SensorData build(){
            SensorData data = new SensorData();
            data.name = this.name;
            data.id = this.id;
            data.lat = this.lat;
            data.lon = this.lon;
            data.PM2_5 = this.PM2_5;

            return data;
        }
    }

    private double PM2_5;
    private double lat;
    private double lon;
    private String name;
    private double id;

    private SensorData(){

    }

    public double getPM2_5(){
        return PM2_5;
    }
    public double getLat(){
        return lat;
    }
    public double getLon(){
        return lon;
    }
    public String getName(){
        return name;
    }
    public double getID(){
        return id;
    }

    @Override
    public String toString(){
        return "ID: " + id + "\n" +
                "Label: " + name + "\n" +
                "Lat: " + lat + "\n" +
                "Lon: " + lon + "\n" +
                "PM2_5Value: " + PM2_5;
    }
}
