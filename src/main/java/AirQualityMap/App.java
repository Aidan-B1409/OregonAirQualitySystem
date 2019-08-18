package AirQualityMap;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.EnumSet;

public class App {

    public static void main(String[] args) {
        ArrayList<SensorData>DataArray = new ArrayList<SensorData>();
        EnumSet<SensorList> sensorList = EnumSet.allOf(SensorList.class);

        for(SensorList sensor : sensorList){
            DataFetcher dataFetcher = new DataFetcher(sensor.IDRequest);
            System.out.println("\n\n" + dataFetcher.getData().toString());
            DataArray.add(dataFetcher.getData());
        }

        KMLgen kmlGen = new KMLgen(DataArray);
        try {
            kmlGen.makeKML();
        }
        catch(FileNotFoundException e){

        }
    }
}
