package org.example.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpWeather {
    private static final String URI="https://api.open-meteo.com/v1/forecast?latitude=%f&longitude=%f&hourly=temperature_2m&current=temperature_2m,is_day,relative_humidity_2m,precipitation,rain&forecast_days=1";
    private final HttpClient client;

    public HttpWeather() {
        client= HttpClient.newHttpClient();
    }

    public String fetch(Double lat, Double lon){
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(URI.formatted(lat,lon)))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
