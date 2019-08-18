package AirQualityMap;

import de.micromata.opengis.kml.v_2_2_0.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class KMLgen {
    private ArrayList<SensorData> sensorDataList;
    final Kml kml = new Kml();
    private Document doc;
    private Folder folder;

    public KMLgen(ArrayList<SensorData> sensorDataList){
        this.sensorDataList = sensorDataList;
        doc = kml.createAndSetDocument().withName("ExampleKML").withOpen(true);
        folder = doc.createAndAddFolder();
        folder.withName("Current Air Quality Sensor Readings").withOpen(true);
    }

    public void makeKML() throws FileNotFoundException{
        for(SensorData sensor : sensorDataList){
            makeDataPoint(sensor);
        }
        kml.marshal(new File("exampleKML.kml"));
    }

    private void makeDataPoint(SensorData sensorData){
        Icon icon = new Icon();
        Style style = doc.createAndAddStyle();

        style.withId("style_"+sensorData.getName())
                .createAndSetIconStyle().withScale(5.0).withIcon(icon);
        style.createAndSetLabelStyle().withColor("ff43b3ff").withScale(5.0);

        Placemark placemark = folder.createAndAddPlacemark();
        placemark.withName(sensorData.getName())
                .withStyleUrl("#style_" + sensorData.getName())
                .withDescription("<![CDATA[<img src=\"http://chart.apis.google.com/chart?chs=430x200&chd=t:" + sensorData.getLat() + "," + sensorData.getLon() + "&cht=p3&chl=" + sensorData.getName() + sensorData.getPM2_5()+ "/>")
                .createAndSetLookAt().withLongitude(sensorData.getLon()).withLatitude(sensorData.getLat()).withAltitude(0).withRange(120000);
        placemark.createAndSetPoint().addToCoordinates(sensorData.getLat(), sensorData.getLon());

    }
}
