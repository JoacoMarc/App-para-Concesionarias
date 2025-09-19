package tests.controller;

import controller.ImpuestoAuto;
import controller.ImpuestoCamion;
import controller.ImpuestoCamioneta;
import controller.ImpuestoMoto;
import controller.InterfazImpuestoStrategy;

public class TaxCalculationTests {

    private static final double DELTA = 0.00001; // Tolerance for floating-point comparisons

    private static boolean assertEquals(double expected, double actual, double delta) {
        return Math.abs(expected - actual) < delta;
    }

    public static void testImpuestoAuto() {
        System.out.println("Testing ImpuestoAuto...");
        InterfazImpuestoStrategy impuestoAuto = new ImpuestoAuto();
        double precioBase1 = 10000.0;
        double expectedTax1 = 10000.0 * (0.21 + 0.05 + 0.01); // 2700.0
        double actualTax1 = impuestoAuto.calcularImpuesto(precioBase1);

        if (assertEquals(expectedTax1, actualTax1, DELTA)) {
            System.out.println("  PASS: Precio base " + precioBase1 + ", Expected: " + expectedTax1 + ", Actual: " + actualTax1);
        } else {
            System.out.println("  FAIL: Precio base " + precioBase1 + ", Expected: " + expectedTax1 + ", Actual: " + actualTax1);
        }

        double precioBase2 = 0.0;
        double expectedTax2 = 0.0;
        double actualTax2 = impuestoAuto.calcularImpuesto(precioBase2);

        if (assertEquals(expectedTax2, actualTax2, DELTA)) {
            System.out.println("  PASS: Precio base " + precioBase2 + ", Expected: " + expectedTax2 + ", Actual: " + actualTax2);
        } else {
            System.out.println("  FAIL: Precio base " + precioBase2 + ", Expected: " + expectedTax2 + ", Actual: " + actualTax2);
        }
        System.out.println("------------------------------------");
    }

    public static void testImpuestoCamioneta() {
        System.out.println("Testing ImpuestoCamioneta...");
        InterfazImpuestoStrategy impuestoCamioneta = new ImpuestoCamioneta();
        double precioBase = 20000.0;
        double expectedTax = 20000.0 * (0.10 + 0.05 + 0.02); // 17% = 3400.0
        double actualTax = impuestoCamioneta.calcularImpuesto(precioBase);

        if (assertEquals(expectedTax, actualTax, DELTA)) {
            System.out.println("  PASS: Precio base " + precioBase + ", Expected: " + expectedTax + ", Actual: " + actualTax);
        } else {
            System.out.println("  FAIL: Precio base " + precioBase + ", Expected: " + expectedTax + ", Actual: " + actualTax);
        }
        System.out.println("------------------------------------");
    }

    public static void testImpuestoMoto() {
        System.out.println("Testing ImpuestoMoto...");
        InterfazImpuestoStrategy impuestoMoto = new ImpuestoMoto();
        double precioBase = 5000.0;
        double expectedTax = 5000.0 * (0.00 + 0.05 + 0.01); // 6% = 300.0
        double actualTax = impuestoMoto.calcularImpuesto(precioBase);

        if (assertEquals(expectedTax, actualTax, DELTA)) {
            System.out.println("  PASS: Precio base " + precioBase + ", Expected: " + expectedTax + ", Actual: " + actualTax);
        } else {
            System.out.println("  FAIL: Precio base " + precioBase + ", Expected: " + expectedTax + ", Actual: " + actualTax);
        }
        System.out.println("------------------------------------");
    }

    public static void testImpuestoCamion() {
        System.out.println("Testing ImpuestoCamion...");
        InterfazImpuestoStrategy impuestoCamion = new ImpuestoCamion();
        double precioBase = 50000.0;
        double expectedTax = 50000.0 * (0.00 + 0.05 + 0.02); // 7% = 3500.0
        double actualTax = impuestoCamion.calcularImpuesto(precioBase);

        if (assertEquals(expectedTax, actualTax, DELTA)) {
            System.out.println("  PASS: Precio base " + precioBase + ", Expected: " + expectedTax + ", Actual: " + actualTax);
        } else {
            System.out.println("  FAIL: Precio base " + precioBase + ", Expected: " + expectedTax + ", Actual: " + actualTax);
        }
        System.out.println("------------------------------------");
    }

    public static void main(String[] args) {
        System.out.println("===== Iniciando Pruebas de Cálculo de Impuestos =====");
        testImpuestoAuto();
        testImpuestoCamioneta();
        testImpuestoMoto();
        testImpuestoCamion();
        System.out.println("===== Pruebas de Cálculo de Impuestos Finalizadas =====");
    }
}
