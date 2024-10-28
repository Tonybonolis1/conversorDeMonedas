import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GeneradorDeArchivo {
    public void guardarJson(double monto, double resultado, int opcion) throws IOException {
        Map<String, Object> datos = new HashMap<>();
        datos.put("monto", monto);
        datos.put("resultado", resultado);
        datos.put("opcion", opcion);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter writer = new FileWriter("conversion.json");
        writer.write(gson.toJson(datos));
        writer.close();
    }
}
