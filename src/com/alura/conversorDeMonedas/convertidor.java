package com.alura.conversorDeMonedas;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Scanner;
import com.google.gson.Gson;

public class convertidor {
    private static final String API_KEY = "14555a0f7f8c4cc044573221";
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/USD";
    private static final String[] MONEDAS = {"ARS", "BOB", "BRL", "CLP", "COP", "USD"};

    public static tipoDeCambio obtenerTiposDeCambio() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return new Gson().fromJson(response.body(), tipoDeCambio.class);
    }

    public static double convertirMoneda(double cantidad, String desdeMoneda, String aMoneda) throws Exception {
        tipoDeCambio respuesta = obtenerTiposDeCambio();
        Map<String, Double> tasas = respuesta.getConversion_rates();
        double tasaDesde = tasas.get(desdeMoneda);
        double tasaA = tasas.get(aMoneda);
        return cantidad * (tasaA / tasaDesde);
    }

    public static void mostrarMenu() {
        System.out.println("Conversor de Monedas:");
        for (int i = 0; i < MONEDAS.length; i++) {
            System.out.println((i + 1) + ". " + MONEDAS[i]);
        }
        System.out.println("0. Salir");
        System.out.println("Escriba 'exit' en cualquier momento para salir.");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            mostrarMenu();
            System.out.print("Selecciona la moneda de origen: ");
            Integer opcionDesde = obtenerOpcion(scanner);
            if (opcionDesde == null) {
                break;
            }
            System.out.print("Selecciona la moneda de destino: ");
            Integer opcionA = obtenerOpcion(scanner);
            if (opcionA == null) {
                break;
            }
            System.out.print("Ingresa la cantidad a convertir: ");
            Double cantidad = obtenerCantidad(scanner);
            if (cantidad == null) {
                break;
            }

            String desdeMoneda = MONEDAS[opcionDesde - 1];
            String aMoneda = MONEDAS[opcionA - 1];
            try {
                double resultado = convertirMoneda(cantidad, desdeMoneda, aMoneda);
                System.out.printf("%.2f %s = %.2f %s%n", cantidad, desdeMoneda, resultado, aMoneda);
            } catch (Exception e) {
                System.err.println("Error al realizar la conversión: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static Integer obtenerOpcion(Scanner scanner) {
        while (true) {
            String entrada = scanner.next();
            if (entrada.equalsIgnoreCase("exit")) {
                return null;
            }
            try {
                int opcion = Integer.parseInt(entrada);
                if (opcion >= 0 && opcion <= 6) {
                    return opcion;
                } else {
                    System.out.print("Opción inválida. Por favor selecciona una opción válida: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Por favor ingresa un número o 'exit' para salir: ");
            }
        }
    }

    private static Double obtenerCantidad(Scanner scanner) {
        while (true) {
            String entrada = scanner.next();
            if (entrada.equalsIgnoreCase("exit")) {
                return null;
            }
            try {
                return Double.parseDouble(entrada);
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Por favor ingresa un número o 'exit' para salir: ");
            }
        }
    }
}
