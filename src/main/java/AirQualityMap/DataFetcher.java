package AirQualityMap;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataFetcher {
    private String id, sensorURL;
    private String baseURL = "https://www.purpleair.com/json?show=";
    private URL url;
    private SensorData data;

    public DataFetcher(String id){
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


    public JsonObject processInputStream(){
        JsonParser parser = new JsonParser();
        try {
            JsonElement root = parser.parse(new InputStreamReader((InputStream)connectToSensor().getContent()));
            System.out.println(root.toString());
            return root.getAsJsonObject();
        }
        catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public String[] getData(){
        String lat = processInputStream().get("results").getAsJsonArray().get(0).getAsJsonObject().get("Lat").getAsString();
        String lon = processInputStream().get("results").getAsJsonArray().get(0).getAsJsonObject().get("Lon").getAsString();
        String name = processInputStream().get("results").getAsJsonArray().get(0).getAsJsonObject().get("Label").getAsString();
        String value = processInputStream().get("results").getAsJsonArray().get(0).getAsJsonObject().get("PM2_5Value").getAsString();

        String[] output = new String[]{lat, lon, name, value};
        return output;
    }
}
