package AirQualityMap;

import de.micromata.opengis.kml.v_2_2_0.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.EnumSet;

public class KMLgen {
    private enum AqiIcons{

        Good("http://files.airnowtech.org/airnow/today/aqi0circle.png"),
        Moderate("http://files.airnowtech.org/airnow/today/aqi1circle.png"),
        Sensitive("http://files.airnowtech.org/airnow/today/aqi2circle.png"),
        Unhealthy("http://files.airnowtech.org/airnow/today/aqi3circle.png"),
        Very_Unhealthy("http://files.airnowtech.org/airnow/today/aqi4circle.png");


        public final String href;
        AqiIcons(String href){
            this.href = href;
        }
    }
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
        /*String styleUrl;
        if(sensorData.getPM2_5() <= 50){
            styleUrl = ("#Good");
        }
        else if((sensorData.getPM2_5() > 50) && (sensorData.getPM2_5() <= 100)){
            styleUrl = ("#Moderate");
        }
        else if((sensorData.getPM2_5() > 100) && (sensorData.getPM2_5() <=150)){
            styleUrl = ("#Sensitive");
        }*/
        Placemark placemark = folder.createAndAddPlacemark();
        placemark.withName(sensorData.getName())
                .withStyleUrl("temporary string")
                .withDescription("value = " + sensorData.getPM2_5())
                .createAndSetLookAt().withLongitude(sensorData.getLon()).withLatitude(sensorData.getLat()).withAltitude(0).withRange(1200);
        placemark.createAndSetPoint().addToCoordinates(sensorData.getLon(), sensorData.getLat()).setAltitudeMode(AltitudeMode.RELATIVE_TO_GROUND);
    }

    private void makeStyleIcons(){
        EnumSet<AqiIcons> styleList = EnumSet.allOf(AqiIcons.class);
        for(AqiIcons icon : styleList){
            Icon styleIcon = new Icon().withHref(icon.href);
            Style style = doc.createAndAddStyle();
            style.withId(icon.toString()).createAndSetIconStyle().withScale(1.0).withIcon(styleIcon);
            style.createAndSetLabelStyle().withColor("00000000").withScale(1.0);
            style.createAndSetBalloonStyle().withBgColor("ffffffff").withDisplayMode(DisplayMode.DEFAULT);
        }
    }
}
