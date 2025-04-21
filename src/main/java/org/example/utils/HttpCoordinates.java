package org.example.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.presentation.CityCoordinatesDTO;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpCoordinates {
    private static final String URI="https://geocoding-api.open-meteo.com/v1/search?name=";
    private final HttpClient client;

    public HttpCoordinates(){
        client= HttpClient.newHttpClient();
    }

    public CityCoordinatesDTO fetch (String city) throws Exception {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(URI+city+"&count=10&language=en&format=json"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());

            if(root.has("results") && !root.get("results").isEmpty()) {
                JsonNode firstResult = root.get("results").get(0);
                double lat = firstResult.get("latitude").asDouble();
                double lon = firstResult.get("longitude").asDouble();
                return new CityCoordinatesDTO(city,lat,lon);
            }else {
                throw new RuntimeException("No hay resultados");
            }
        }catch (Exception e){
            throw new Exception("Error al obtener resultados de apis");
        }
    }
}
