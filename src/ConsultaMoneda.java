import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

public class ConsultaMoneda {

    private static final String API_KEY = "3dcc23eb8b7bd39b3e5b188e"; // Tu API Key aquí
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";
    private static final List<String> MONEDAS_PERMITIDAS = Arrays.asList("COP", "EUR", "USD", "JPY", "RUB", "CNY");

    public double convertir(String base, String destino, double monto) {
        if (!MONEDAS_PERMITIDAS.contains(base) || !MONEDAS_PERMITIDAS.contains(destino)) {
            throw new IllegalArgumentException("Moneda no soportada. Utilice una de las siguientes: COP, EUR, USD, JPY, RUB, CNY.");
        }

        double tasaCambio = obtenerTasaCambio(base, destino);
        return monto * tasaCambio;
    }

    private double obtenerTasaCambio(String base, String destino) {
        String apiUrl = BASE_URL + API_KEY + "/latest/" + base;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();

            // Verifica que la respuesta sea exitosa
            if (!jsonObject.get("result").getAsString().equals("success")) {
                throw new RuntimeException("No se pudo obtener la tasa de cambio.");
            }

            // Accede a la tasa de cambio específica de la moneda de destino
            JsonObject rates = jsonObject.getAsJsonObject("conversion_rates");
            return rates.get(destino).getAsDouble();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener la tasa de cambio: " + e.getMessage());
        }
    }
}
