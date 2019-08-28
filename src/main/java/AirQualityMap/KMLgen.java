package AirQualityMap;

import de.micromata.opengis.kml.v_2_2_0.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.EnumSet;

public class KMLgen {
    private enum AqiLevels{

        Good("http://files.airnowtech.org/airnow/today/aqi0circle.png", 1, 50, 0.01, 12.0),
        Moderate("http://files.airnowtech.org/airnow/today/aqi1circle.png", 51 , 100, 12.1, 35.4),
        Sensitive("http://files.airnowtech.org/airnow/today/aqi2circle.png", 101, 150, 35.5, 55.4),
        Unhealthy("http://files.airnowtech.org/airnow/today/aqi3circle.png", 151, 200, 55.5, 150.4),
        Very_Unhealthy("http://files.airnowtech.org/airnow/today/aqi4circle.png", 201, 300, 150.5, 250.4),
        Hazardous("http://files.airnowtech.org/airnow/today/aqi5circle.png", 301, 500, 250.5, 500.4),
        Unknown("http://files.airnowtech.org/airnow/today/undefined_circle.png", -1, 0, -1, 0.0);

        public final String href;
        public final double aqiMin, aqiMax, concMin, concMax;
        AqiLevels(String href, double aqiMin, double aqiMax, double concMin, double concMax){
            this.href = href;
            this.aqiMin = aqiMin;
            this.aqiMax = aqiMax;
            this.concMin = concMin;
            this.concMax = concMax;
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
        AqiLevels qualityLevel = checkAqiRange(sensorData.getPM2_5());
        String styleUrl = qualityLevel.toString();
        double qualityValue = Math.round(toAqi(sensorData.getPM2_5(), qualityLevel));

        Placemark placemark = folder.createAndAddPlacemark();
        placemark.withName(sensorData.getName())
                .withStyleUrl("#"+styleUrl)
                .withDescription("AQI Value = " + qualityValue +"\n" + "PM2.5 Value = " + sensorData.getPM2_5())
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
            if((value >= level.concMin) && (value <= level.concMax)){
                return level;
            }
        }
        return AqiLevels.Unknown;
    }

    private double toAqi(double PM2_5, AqiLevels level){
        double value = (((level.aqiMax - level.aqiMin) / (level.concMax - level.concMin)) * (PM2_5 - level.concMin) + level.aqiMin);
        return value;
    }
}
