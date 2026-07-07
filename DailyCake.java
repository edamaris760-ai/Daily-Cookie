import javax.swing.*;
import java.awt.*;

public class DailyCake {
    public static void main(String[] args) {
        // 1. Creamos la ventana principal (JFrame)
        JFrame ventana = new JFrame();
        ventana.setTitle("=== DAILY CAKE - PANEL DE CONTROL GRÁFICO ===");
        ventana.setSize(1100, 550); // Ancho suficiente para ver ambas gráficas lado a lado
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLocationRelativeTo(null); // Centra la ventana en la pantalla
        ventana.setLayout(new GridLayout(1, 2, 15, 0)); // Divide la pantalla en 2 secciones

        // 2. Creamos los lienzos personalizados con tus modelos reales
        LienzoBeneficio lienzoB = new LienzoBeneficio();
        LienzoLogistico lienzoL = new LienzoLogistico();

        // 3. Agregamos los lienzos a la ventana
        ventana.add(lienzoB);
        ventana.add(lienzoL);

        // 4. Hacemos visible la ventana
        ventana.setVisible(true);
    }
}

// ==========================================================
// 📊 LIENZO 1: OPTIMIZACIÓN DE LA FUNCIÓN DE BENEFICIO B(x)
// ==========================================================
class LienzoBeneficio extends JPanel {

    // B(x) = -0.12x² + 40x - 900
    private double funcionBeneficio(double x) {
        if (x < 0) return Double.NaN;
        return -0.12 * Math.pow(x, 2) + 40.0 * x - 900.0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int ancho = getWidth();
        int alto = getHeight();
        int margen = 50;

        // Fondo limpio
        g2.setColor(new Color(248, 249, 250));
        g2.fillRect(0, 0, ancho, alto);

        // Cuadrícula gris de fondo
        g2.setColor(new Color(222, 226, 230));
        for(int i = margen; i < ancho - margen; i += 40) g2.drawLine(i, margen, i, alto - margen);
        for(int j = margen; j < alto - margen; j += 40) g2.drawLine(margen, j, ancho - margen, j);

        // Configuración de origen y ejes cartesianos
        int origenX = margen;
        int origenY = alto - margen - 60; // Margen desplazado para notar el área de pérdidas negativas (-900)

        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2.0f));
        g2.drawLine(origenX, origenY, ancho - margen, origenY); // Eje X (Unidades)
        g2.drawLine(origenX, margen, origenX, alto - margen);     // Eje Y (Bs)

        // Títulos de la sección
        g2.setFont(new Font("Arial", Font.BOLD, 13));
        g2.drawString("1. Optimización del Beneficio B(x) = -0.12x² + 40x - 900", margen, margen - 15);
        g2.setFont(new Font("Arial", Font.PLAIN, 11));
        g2.drawString("Eje Y: Beneficio (Bs)", margen + 10, margen + 15);
        g2.drawString("Eje X: Unidades (x)", ancho - margen - 100, origenY + 20);

        // Rangos máximos de simulación matemática para el mapeo visual
        double maxUnits = 350.0;
        double maxBeneficio = 3000.0;

        // DIBUJAR LA CURVA DE BENEFICIO PARABÓLICA
        g2.setColor(new Color(220, 53, 69)); // Rojo institucional/académico
        g2.setStroke(new BasicStroke(2.5f));

        Integer pixelAnteriorX = null;
        Integer pixelAnteriorY = null;

        for (int pixelX = origenX; pixelX < ancho - margen; pixelX++) {
            double xReal = ((double)(pixelX - origenX) / (ancho - 2 * margen)) * maxUnits;
            double yReal = funcionBeneficio(xReal);

            if (!Double.isNaN(yReal)) {
                // Mapeo proporcional de variables a pixeles reales en pantalla
                int pixelY = origenY - (int) ((yReal / maxBeneficio) * (origenY - margen));

                if (pixelY >= margen && pixelY <= alto - margen) {
                    if (pixelAnteriorX != null) {
                        g2.drawLine(pixelAnteriorX, pixelAnteriorY, pixelX, pixelY);
                    }
                    pixelAnteriorX = pixelX;
                    pixelAnteriorY = pixelY;
                } else {
                    pixelAnteriorX = null;
                    pixelAnteriorY = null;
                }
            }
        }

        // MARCAR PUNTOS CRÍTICOS IMPORTANTES
        g2.setFont(new Font("Arial", Font.BOLD, 11));

        // Punto de Cese de Actividades (Costo Fijo)
        int pX0 = origenX;
        int pY0 = origenY - (int) ((-900.0 / maxBeneficio) * (origenY - margen));
        g2.setColor(Color.DARK_GRAY);
        g2.fillOval(pX0 - 4, pY0 - 4, 8, 8);
        g2.drawString("Cese de Actividad (0, -900 Bs)", pX0 + 10, pY0 + 4);

        // Vértice Óptimo Máximo (Primera Derivada = 0)
        double xOptimo = 40.0 / 0.24; // ~166.67 unidades
        double yOptimo = funcionBeneficio(xOptimo); // ~2433.33 Bs
        int pXOptimo = origenX + (int)((xOptimo / maxUnits) * (ancho - 2 * margen));
        int pYOptimo = origenY - (int)((yOptimo / maxBeneficio) * (origenY - margen));

        g2.setColor(new Color(40, 167, 69)); // Verde para el Éxito Operacional
        g2.fillOval(pXOptimo - 5, pYOptimo - 5, 10, 10);
        g2.drawString(String.format("Máximo Óptimo (%.0f u. , %.0f Bs)", xOptimo, yOptimo), pXOptimo - 90, pYOptimo - 12);

        // Líneas punteadas de proyección técnica hacia los ejes
        g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{5.0f}, 0.0f));
        g2.setColor(Color.GRAY);
        g2.drawLine(pXOptimo, pYOptimo, pXOptimo, origenY);
        g2.drawLine(pXOptimo, pYOptimo, origenX, pYOptimo);
    }
}

// ==========================================================
// 📈 LIENZO 2: CRECIMIENTO LOGÍSTICO DE CLIENTES U(t)
// ==========================================================
class LienzoLogistico extends JPanel {

    // U(t) = 1200 / (1 + 5e^(-0.25t))
    private double funcionLogistica(double t) {
        if (t < 0) return Double.NaN;
        return 1200.0 / (1.0 + 5.0 * Math.exp(-0.25 * t));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int ancho = getWidth();
        int alto = getHeight();
        int margen = 50;

        // Fondo limpio
        g2.setColor(new Color(248, 249, 250));
        g2.fillRect(0, 0, ancho, alto);

        // Cuadrícula gris de fondo
        g2.setColor(new Color(222, 226, 230));
        for(int i = margen; i < ancho - margen; i += 40) g2.drawLine(i, margen, i, alto - margen);
        for(int j = margen; j < alto - margen; j += 40) g2.drawLine(margen, j, ancho - margen, j);

        // Ejes cartesianos
        int origenX = margen;
        int origenY = alto - margen;

        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2.0f));
        g2.drawLine(origenX, origenY, ancho - margen, origenY); // Eje T (Tiempo)
        g2.drawLine(origenX, margen, origenX, origenY);         // Eje U (Clientes)

        // Títulos de la sección
        g2.setFont(new Font("Arial", Font.BOLD, 13));
        g2.drawString("2. Crecimiento de Clientes U(t) = 1200 / (1 + 5e^(-0.25t))", margen, margen - 15);
        g2.setFont(new Font("Arial", Font.PLAIN, 11));
        g2.drawString("Eje Y: Clientes Recurrentes", margen + 10, margen + 15);
        g2.drawString("Eje T: Tiempo (Semanas)", ancho - margen - 130, origenY - 10);

        // Límites y proporciones visuales
        double maxSemanas = 30.0;
        double rangoYMax = 1400.0;

        // Línea Asintótica Azul - Techo del Mercado (Análisis del límite t -> infinito)
        double capMaxima = 1200.0;
        int pYAsintota = origenY - (int)((capMaxima / rangoYMax) * (alto - 2 * margen));

        g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]{6.0f}, 0.0f));
        g2.setColor(new Color(0, 80, 190));
        g2.drawLine(origenX, pYAsintota, ancho - margen, pYAsintota);
        g2.drawString("Techo Asintótico L = 1200 clientes", ancho - margen - 180, pYAsintota - 6);

        // DIBUJAR LA CURVA SIGMOIDE LOGÍSTICA
        g2.setColor(new Color(0, 123, 255)); // Azul brillante para flujo operacional
        g2.setStroke(new BasicStroke(2.5f));

        Integer pixelAnteriorX = null;
        Integer pixelAnteriorY = null;

        for (int pixelX = origenX; pixelX < ancho - margen; pixelX++) {
            double tReal = ((double)(pixelX - origenX) / (ancho - 2 * margen)) * maxSemanas;
            double utReal = funcionLogistica(tReal);

            if (!Double.isNaN(utReal)) {
                int pixelY = origenY - (int) ((utReal / rangoYMax) * (alto - 2 * margen));

                if (pixelY >= margen && pixelY <= alto - margen) {
                    if (pixelAnteriorX != null) {
                        g2.drawLine(pixelAnteriorX, pixelAnteriorY, pixelX, pixelY);
                    }
                    pixelAnteriorX = pixelX;
                    pixelAnteriorY = pixelY;
                }
            }
        }
    }
}