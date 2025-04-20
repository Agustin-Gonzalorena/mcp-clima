package org.example.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpHelper {
    HttpClient client;

    public HttpHelper(){
        client= HttpClient.newHttpClient();
    }

    public String fetch (String uri) throws Exception {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(uri))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
            return response.body();
        }catch (Exception e){
            throw new Exception("Error al obtener resultados de apis");
        }
    }
}
