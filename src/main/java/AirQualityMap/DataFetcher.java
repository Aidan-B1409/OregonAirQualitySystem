package AirQualityMap;

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
    URL url;

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
            System.out.println(responseCode);

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
            return root.getAsJsonObject();
        }
        catch(IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /*public String[] getData(){
        String lat = processInputStream().get("results").getAsJsonArray().get(1).getAsJsonObject().get("Lat").getAsString();
        //System.out.println(processInputStream().getAsString());
        String lon = processInputStream().get("Lon").getAsString();
        String name = processInputStream().get("Label").getAsString();
        String value = processInputStream().get("PM2_5Value").getAsString();

        String[] output = new String[]{lon, name, value};
        return output;
    }*/
}
