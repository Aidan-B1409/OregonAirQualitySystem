package AirQualityMap;

import java.util.EnumSet;

public class App {
    public static void main(String[] args) {

        EnumSet<SensorList> sensorList = EnumSet.allOf(SensorList.class);
        for(SensorList sensor : sensorList){
            DataFetcher dataFetcher = new DataFetcher(sensor.IDRequest);
            System.out.println("\n\n" + dataFetcher.getData().toString());
        }
    }
}
