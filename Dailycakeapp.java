import java.util.InputMismatchException;
import java.util.Scanner;

public class Dailycakeapp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== PROYECTO SEMESTRAL - CALCULO I ===");
        System.out.println("Modelado, Análisis y Optimización - \"DAILY CAKE\"");
        System.out.println("------------------------------------------------");

        // Lectura de datos con validación anti-letras
        double x = leerDouble(scanner, "Ingrese las unidades vendidas semanalmente (x): ");
        double t = leerDouble(scanner, "Ingrese el tiempo en semanas para proyección de clientes (t): ");
        double precioUnidad = leerDouble(scanner, "Ingrese el precio base por unidad de queque en Bs (p): ");
        double yInversion = leerDouble(scanner, "Ingrese la inversión en marketing digital semanal en Bs (y): ");

        System.out.println("\n--- RESULTADOS ---");

        // 1. Modelado Funcional & Cálculos Base
        // Crecimiento de clientes (Modelo Logístico)
        double ut = 1200.0 / (1.0 + 5.0 * Math.exp(-0.25 * t));

        // Ingreso Total: I(x) = 60x - 0.1x^2
        double ingresoTotal = 60.0 * x - 0.1 * Math.pow(x, 2);

        // Costo Total: C(x) = 0.02x^2 + 20x + 900
        double costoTotal = 0.02 * Math.pow(x, 2) + 20.0 * x + 900.0;

        // Beneficio Actual: B(x) = I(x) - C(x)
        double beneficioActual = -0.12 * Math.pow(x, 2) + 40.0 * x - 900.0;

        // 2. Límites y Continuidad (Escenarios t -> oo y x -> 0)
        double limiteClientesInfinito = 1200.0;
        double beneficioCeseActividad = -900.0;

        // 3. Optimización (Primera y Segunda Derivada)
        double xOptimo = 40.0 / 0.24;
        double beneficioOptimoMaximo = -0.12 * Math.pow(xOptimo, 2) + 40.0 * xOptimo - 900.0;
        double segundaDerivada = -0.24;

        // 4. Modelo Multivariable
        double multivariableF = -0.12 * Math.pow(x, 2) + 40.0 * x + (4.0 * x * Math.sqrt(yInversion)) - yInversion - 900.0;

        // --- DESPLIEGUE DE RESULTADOS (Modificado a %.0f para mostrar enteros) ---
        System.out.printf("Crecimiento de clientes proyectado U(t): %.0f clientes%n", ut);
        System.out.printf("Ingreso semanal actual I(x): %.0f Bs%n", ingresoTotal);
        System.out.printf("Costo semanal calculado C(x): %.0f Bs%n", costoTotal);
        System.out.printf("Beneficio semanal actual B(x): %.0f Bs%n", beneficioActual);

        System.out.println("\n[LÍMITES Y CONTINUIDAD]");
        System.out.printf("• Flujo de clientes a largo plazo (t -> ∞): %.0f clientes recurrentes.%n", limiteClientesInfinito);
        System.out.printf("• Beneficio mínimo en caso de cese de actividades (x -> 0): %.0f Bs (Pérdida por costos fijos).%n", beneficioCeseActividad);

        System.out.println("\n[OPTIMIZACIÓN Y DERIVADAS]");
        System.out.printf("• Volumen óptimo de producción (Primera derivada = 0): %.0f unidades%n", xOptimo);
        System.out.printf("• Beneficio máximo realizable: %.0f Bs%n", beneficioOptimoMaximo);
        System.out.printf("• Segunda derivada B''(x): %.2f%n", segundaDerivada); // Se mantiene decimal por ser una constante analítica crítica (-0.24)
        if (segundaDerivada < 0) {
            System.out.println("  -> Concavidad negativa (Concomitante a un MÁXIMO ABSOLUTO geométrico).");
        }

        System.out.println("\n[MODELO MULTIVARIABLE]");
        System.out.printf("• Beneficio neto considerando pauta publicitaria F(x, y): %.0f Bs%n", multivariableF);

        System.out.println("\n------------------------------------------------");
        System.out.println("INTERPRETACIÓN TÉCNICA:");
        System.out.printf("• U(t) representa el techo asintótico del mercado en Santa Cruz de la Sierra debido a la densidad demográfica.%n");
        if (x < xOptimo) {
            System.out.printf("• La empresa opera por DEBAJO de su óptimo matemático (%.0f vs %.0f unidades). ¡Incrementar producción aumentará utilidades!%n", x, xOptimo);
        } else {
            System.out.printf("• La empresa supera el óptimo matemático (%.0f vs %.0f unidades), incurriendo en ineficiencias por saturación de hornos.%n", x, xOptimo);
        }
        System.out.println("• El análisis multivariable demuestra el alto retorno y elasticidad marginal de la inversión publicitaria en redes sociales.");

        System.out.println("\nLIMITACIONES DEL MODELO:");
        System.out.println("• Inflación local: El modelo matemático es estático y no prevé incrementos súbitos en los costos de las materias primas.");
        System.out.println("• Saturación Logística: No incluye los costos variables de delivery o apps de entrega indispensables entre los anillos de la ciudad.");

        scanner.close();
    }

    /**
     * Método auxiliar para validar que el usuario ingrese únicamente números.
     */
    private static double leerDouble(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return scanner.nextDouble();
            } catch (InputMismatchException e) {
                System.out.println("error intentelo de nuevo, ingrese solo números");
                scanner.nextLine(); // Limpiar el buffer
                System.out.println();
            }
        }
    }
}
