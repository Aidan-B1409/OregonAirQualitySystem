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
        makeStyleIcons();
        for(SensorData sensor : sensorDataList){
            makeDataPoint(sensor);
        }
        kml.marshal(new File("exampleKML.kml"));
    }

    private void makeDataPoint(SensorData sensorData){

        Placemark placemark = folder.createAndAddPlacemark();
        placemark.withName(sensorData.getName())
                .withStyleUrl("#good")
                .withDescription("value = " + sensorData.getPM2_5())
                .createAndSetLookAt().withLongitude(sensorData.getLon()).withLatitude(sensorData.getLat()).withAltitude(0).withRange(1200);
        placemark.createAndSetPoint().addToCoordinates(sensorData.getLat(), sensorData.getLon()).setAltitudeMode(AltitudeMode.RELATIVE_TO_GROUND);

    }

    private void makeStyleIcons(){
        Icon goodIcon = new Icon().withHref("http://files.airnowtech.org/airnow/today/aqi0circle.png");
        BalloonStyle goodBalloon = new BalloonStyle().withBgColor("ffffffff");
        Style goodStyle = doc.createAndAddStyle();
        goodStyle.withId("good").createAndSetIconStyle().withScale(1.0).withIcon(goodIcon);
        goodStyle.createAndSetLabelStyle().withColor("ff43b3ff").withScale(1.2);
        goodStyle.createAndSetBalloonStyle().withBgColor("ffffffff").withDisplayMode(DisplayMode.DEFAULT);

    }
}
