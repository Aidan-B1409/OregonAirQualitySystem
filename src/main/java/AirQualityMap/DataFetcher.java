package AirQualityMap;

import com.google.gson.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataFetcher {
    private double id;
    private String sensorURL;
    private String baseURL = "https://www.purpleair.com/json?show=";
    private URL url;
    private SensorData data;

    public DataFetcher(double id){
        this.id = id;
        this.sensorURL = baseURL + id;
        try {
            url = new URL(sensorURL);
        }
        catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }
    public HttpURLConnection connectToSensor(){
        try {
            HttpURLConnection request = (HttpURLConnection)url.openConnection();
            int responseCode = request.getResponseCode();
            if(responseCode != 200){
                throw new RuntimeException("HttpResponseCode:" +responseCode);
            }
            else{
                request.connect();
                return request;
            }
        }
        catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public JsonElement processInputStream(){
        JsonParser parser = new JsonParser();
        try {
            JsonElement root = parser.parse(new InputStreamReader((InputStream)connectToSensor().getContent()));
            data = ParseResults(root);
            return root;
        }
        catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private SensorData ParseResults(JsonElement json){
        double lat, lon, PM2_5Value,ID;
        String label;
        JsonObject primaryResults = json.getAsJsonObject().get("results").getAsJsonArray().get(0).getAsJsonObject();
        SensorData data;

        lat = primaryResults.get("Lat").getAsDouble();
        lon = primaryResults.get("Lon").getAsDouble();
        label = primaryResults.get("Label").getAsString();
        PM2_5Value = primaryResults.get("PM2_5Value").getAsDouble();
        ID = primaryResults.get("ID").getAsDouble();

        data = new SensorData.Builder(ID)
                .ofName(label)
                .atGeo(lat, lon)
                .withValue(PM2_5Value)
                .build();
        return data;
    }

    public SensorData getData(){
        processInputStream();
        return data;
    }
}
