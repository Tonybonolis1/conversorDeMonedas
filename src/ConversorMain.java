import java.io.IOException;
import java.util.Scanner;

public class ConversorMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ConsultaMoneda consulta = new ConsultaMoneda();
        GeneradorDeArchivo generador = new GeneradorDeArchivo();
        System.out.println("Bienvenido al conversor. Monedas: ");
        System.out.println("Monedas disponibles: COP (Pesos Colombianos), EUR (Euro), USD (Dólar), JPY (Yen), RUB (Rublo), CNY (Yuan).");
        System.out.println("Para salir en cualquier momento, escriba 'SALIR'.");

        while (true) {
            String base = solicitarMoneda(scanner, "base");
            if (base == null) break;

            String destino;
            while (true) {
                destino = solicitarMoneda(scanner, "destino");
                if (destino == null) {  // Si el usuario elige salir
                    System.out.println("Programa finalizado.");
                    return;
                }

                if (!destino.equals(base)) {  // Asegurarse de que la moneda de destino es diferente de la base
                    break;
                } else {
                    System.out.println("La moneda de destino debe ser diferente de la base. Intente nuevamente.");
                }
            }

            Double monto = solicitarMonto(scanner);
            if (monto == null) break;

            try {
                double resultado = consulta.convertir(base, destino, monto);
                System.out.printf("Monto: %.2f %s = %.2f %s%n", monto, base, resultado, destino);
                generador.guardarJson(monto, resultado, obtenerOpcion(base, destino));
                System.out.println("Conversión guardada.");
            } catch (IOException e) {
                System.out.println("Error al guardar la conversión: " + e.getMessage());
            }
        }
        System.out.println("Programa finalizado.");
    }

    private static String solicitarMoneda(Scanner scanner, String tipo) {
        while (true) {
            System.out.printf("Ingrese la moneda %s (COP, EUR, USD, JPY, RUB, CNY) o 'SALIR' para finalizar: ", tipo);
            String moneda = scanner.nextLine().toUpperCase().trim();

            if (moneda.equals("SALIR")) {
                return null;
            }

            switch (moneda) {
                case "COP", "EUR", "USD", "JPY", "RUB", "CNY":
                    return moneda;
                default:
                    System.out.println("Moneda no válida. Intente nuevamente.");
            }
        }
    }

    private static Double solicitarMonto(Scanner scanner) {
        while (true) {
            System.out.print("Ingrese el monto que desea convertir o 'SALIR' para finalizar: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("SALIR")) {
                return null;
            }

            try {
                double monto = Double.parseDouble(input);
                if (monto > 0) {
                    return monto;
                } else {
                    System.out.println("El monto debe ser mayor que 0. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Monto no válido. Ingrese un número.");
            }
        }
    }

    private static int obtenerOpcion(String base, String destino) {
        return switch (base + destino) {
            case "USDCOP" -> 1;
            case "COPUSD" -> 2;
            default -> 0;
        };
    }
}
