package AirQualityMap;

public class App {
    public static void main(String[] args) {
        DataFetcher dataFetcher = new DataFetcher("15709");
        System.out.println(dataFetcher.getData().toString());
    }
}
