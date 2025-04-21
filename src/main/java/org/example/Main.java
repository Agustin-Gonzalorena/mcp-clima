package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema.*;
import org.example.presentation.CityCoordinatesDTO;
import org.example.utils.HttpCoordinates;
import org.example.utils.HttpWeather;

public class Main {
    public static void main(String[] args) {
        // Crear el transportador de entrada/salida (stdin y stdout)
        StdioServerTransportProvider transportProvider = new StdioServerTransportProvider(new ObjectMapper());

        // Crea el servidor sincronico MCP
        McpSyncServer server = McpServer.sync(transportProvider)
                .serverInfo("my-server", "1.0.0")
                .capabilities(ServerCapabilities.builder().tools(true).build())
                .build();

        //herramienta
        var cityTool = getSyncToolSpecification();

        // Agregar la herramienta al servidor
        server.addTool(cityTool);
    }

    private static SyncToolSpecification getSyncToolSpecification() {
        // schema para recibir una ciudad
        String schema = """
                {
                  "type":"object",
                  "properties":{
                        "city": {"type": "string"}
                  }
                }
                """;
        // herramienta
        return new SyncToolSpecification(
                new Tool("CityWeather", "Devuelve clima de una ciudad", schema),
                (exchange, arguments) -> {

                    // Obtener la ciudad del Map
                    String city = (String) arguments.get("city");
                    try {
                        //obtener coordenadas de la ciudad
                        HttpCoordinates httpCoordinates = new HttpCoordinates();
                        CityCoordinatesDTO cc = httpCoordinates.fetch(city);
                        //obtener informacion del clima
                        HttpWeather httpWeather = new HttpWeather();
                        String result = httpWeather.fetch(cc.getLat(), cc.getLon());
                        // Retornar el resultado
                        return new CallToolResult(result, false);
                    } catch (Exception e) {
                        throw new RuntimeException("Error Tool",e);
                    }
                }
        );
    }
}
