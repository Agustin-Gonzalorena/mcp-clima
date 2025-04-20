package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import org.example.utils.HttpHelper;

public class Main {
    public static void main(String[] args) {
        // Crear el transportador de entrada/salida (stdin y stdout)
        StdioServerTransportProvider transportProvider = new StdioServerTransportProvider(new ObjectMapper());

        // Crea el servidor sincronico MCP
        McpSyncServer server = McpServer.sync(transportProvider)
                .serverInfo("my-server", "1.0.0")
                .capabilities(McpSchema.ServerCapabilities.builder().tools(true).build())
                .build();

        // schema para recibir una ciudad
        String schema = """
                        {
                          "type":"object",
                          "properties":{
                                "city": {"type": "string","describe": "City name"}
                          },
                          "required":["city"]
                        }
                        """;

        // herramienta
        var cityTool = new McpServerFeatures.SyncToolSpecification(
                new McpSchema.Tool(
                        "CityWeather", //titulo de la herramienta
                        "Devuelve clima de una ciudad", //descripcion de la herramienta
                        schema // schema con los input de la herramienta
                ),
                (exchange, arguments) -> {
                    // Obtener la ciudad del Map
                    String city = (String) arguments.get("city");

                    //obtener coordenadas de la ciudad
                    //"https://geocoding-api.open-meteo.com/v1/search?name=Berlin&count=10&language=en&format=json"
                    try {
                        String response = new HttpHelper().fetch("https://geocoding-api.open-meteo.com/v1/search?name="+city+"&count=10&language=en&format=json");
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode root = mapper.readTree(response);

                        JsonNode firstResult = root.get("results").get(0);
                        double lat = firstResult.get("latitude").asDouble();
                        double lon = firstResult.get("longitude").asDouble();
                        // Lógica de respuesta hardcodeada
                        String schemaResult= """
                                            {
                                                "type":"text",
                                                "text":"El clima de ${city} es nublado, esta lloviendo y la latidud es ${lat}",
                                            }
                                            """
                                .replace("${city}",city)
                                .replace("${lat}",String.valueOf(lat));
                        // Retornar el resultado
                        return new McpSchema.CallToolResult(schemaResult, false);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }


                }
        );

        // Agregar la herramienta al servidor
        server.addTool(cityTool);


    }
}
