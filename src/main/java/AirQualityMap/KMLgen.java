package AirQualityMap;

import de.micromata.opengis.kml.v_2_2_0.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.EnumSet;

public class KMLgen {
    private enum AqiLevels{

        Good("http://files.airnowtech.org/airnow/today/aqi0circle.png", 1, 50),
        Moderate("http://files.airnowtech.org/airnow/today/aqi1circle.png", 51 , 100),
        Sensitive("http://files.airnowtech.org/airnow/today/aqi2circle.png", 101, 150),
        Unhealthy("http://files.airnowtech.org/airnow/today/aqi3circle.png", 151, 200),
        Very_Unhealthy("http://files.airnowtech.org/airnow/today/aqi4circle.png", 201, 300),
        Hazardous("http://files.airnowtech.org/airnow/today/aqi5circle.png", 301, 500),
        Unknown("http://files.airnowtech.org/airnow/today/undefined_circle.png", -1, 0);

        public final String href;
        public final double minRange, maxRange;
        AqiLevels(String href, double minRange, double maxRange){
            this.href = href;
            this.minRange = minRange;
            this.maxRange = maxRange;
        }
    }
    private ArrayList<SensorData> sensorDataList;
    final Kml kml = new Kml();
    private Document doc;
    private Folder folder;


    public KMLgen(ArrayList<SensorData> sensorDataList){
        this.sensorDataList = sensorDataList;
        this.doc = kml.createAndSetDocument().withName("ExampleKML").withOpen(true);
        this.folder = doc.createAndAddFolder();
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
        String styleUrl = checkAqiRange(sensorData.getPM2_5()).toString();
        Placemark placemark = folder.createAndAddPlacemark();
        placemark.withName(sensorData.getName())
                .withStyleUrl("#"+styleUrl)
                .withDescription("value = " + sensorData.getPM2_5())
                .createAndSetLookAt().withLongitude(sensorData.getLon()).withLatitude(sensorData.getLat()).withAltitude(0).withRange(1200);
        placemark.createAndSetPoint().addToCoordinates(sensorData.getLon(), sensorData.getLat()).setAltitudeMode(AltitudeMode.RELATIVE_TO_GROUND);
    }

    private void makeStyleIcons(){
        EnumSet<AqiLevels> iconList = EnumSet.allOf(AqiLevels.class);
        for(AqiLevels icon : iconList){
            Icon styleIcon = new Icon().withHref(icon.href);
            Style style = doc.createAndAddStyle();
            style.withId(icon.toString()).createAndSetIconStyle().withScale(1.0).withIcon(styleIcon);
            style.createAndSetLabelStyle().withColor("00000000").withScale(1.0);
            style.createAndSetBalloonStyle().withBgColor("ffffffff").withDisplayMode(DisplayMode.DEFAULT);
        }
    }

    private AqiLevels checkAqiRange(double value){
        EnumSet<AqiLevels> levelList = EnumSet.range(AqiLevels.Good, AqiLevels.Hazardous);
        for(AqiLevels level : levelList){
            if((value >= level.minRange) && (value <= level.maxRange)){
                return level;
            }
        }
        return AqiLevels.Unknown;
    }
}
