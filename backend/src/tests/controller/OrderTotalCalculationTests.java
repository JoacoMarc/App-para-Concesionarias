package tests.controller;

import controller.Pedido;
import controller.Vehiculo;
import controller.IVehiculo; // IVehiculo is used by Pedido
import controller.ImpuestoAuto;
import controller.ImpuestoCamioneta;
import controller.ImpuestoMoto;
import controller.InterfazImpuestoStrategy;
import controller.Cliente;
import controller.ICliente; // ICliente is used by Pedido
import controller.Usuario;
import controller.IFormaDePago; // IFormaDePago is used by Pedido
// No concrete FormaDePago needed if null is acceptable for tests not focusing on payment.
// No ServicioDePago or ServicioDeNotificacion needed as they are null in Pedido for these tests.

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderTotalCalculationTests {

    private static final double DELTA = 0.00001; // Tolerance for floating-point comparisons

    private static boolean assertEquals(double expected, double actual, double delta) {
        return Math.abs(expected - actual) < delta;
    }

    // Helper to create a basic Cliente
    private static ICliente createMockCliente() {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(1);
        cliente.setNombre("Cliente de Prueba");
        return cliente;
    }

    // Helper to create a basic Vehiculo
    private static IVehiculo createMockVehiculo(double precioBase) {
        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPrecioBase(precioBase);
        vehiculo.setMarca("MarcaTest");
        vehiculo.setModelo("ModeloTest");
        return vehiculo;
    }
    
    // Helper to create a basic Usuario (Vendedor)
    private static Usuario createMockVendedor() {
        Usuario vendedor = new Usuario();
        vendedor.setIdUsuario(100);
        vendedor.setNombre("Vendedor de Prueba");
        return vendedor;
    }

    public static void testBasicTotalCalculation() {
        System.out.println("Testing Basic Total Calculation...");
        IVehiculo vehiculo = createMockVehiculo(10000.0);
        InterfazImpuestoStrategy impuestoStrategy = new ImpuestoAuto(); // 27% tax
        ICliente cliente = createMockCliente();
        Usuario vendedor = createMockVendedor();
        IFormaDePago formaDePago = null; // Not relevant for costoTotal calculation itself

        Pedido pedido = new Pedido(
                1, 1, new Date(), cliente, vehiculo, "Creado",
                formaDePago, impuestoStrategy, vehiculo.getPrecioBase(),
                null, null, // ServicioDePago, ServicioDeNotificacion
                vendedor, "Datos Facturacion Test", "Ventas"
        );

        pedido.calcularCostoTotal();
        double expectedTotal = 10000.0 + (10000.0 * 0.27); // 12700.0
        double actualTotal = pedido.getCostoTotal();

        if (assertEquals(expectedTotal, actualTotal, DELTA)) {
            System.out.println("  PASS: Expected: " + expectedTotal + ", Actual: " + actualTotal);
        } else {
            System.out.println("  FAIL: Expected: " + expectedTotal + ", Actual: " + actualTotal);
        }
        System.out.println("------------------------------------");
    }

    public static void testTotalCalculationWithAdicionales() {
        System.out.println("Testing Total Calculation with Adicionales...");
        IVehiculo vehiculo = createMockVehiculo(20000.0);
        InterfazImpuestoStrategy impuestoStrategy = new ImpuestoCamioneta(); // 17% tax
        ICliente cliente = createMockCliente();
        Usuario vendedor = createMockVendedor();
        IFormaDePago formaDePago = null;

        Pedido pedido = new Pedido(
                2, 2, new Date(), cliente, vehiculo, "Creado",
                formaDePago, impuestoStrategy, vehiculo.getPrecioBase(),
                null, null, vendedor, "Datos Facturacion Test Adic", "Ventas"
        );

        List<String> adicionales = new ArrayList<>();
        adicionales.add("GPS");
        adicionales.add("Llantas Especiales");
        pedido.setConfiguracionesAdicionales(adicionales); // 2 items * 500.0 each = 1000.0

        pedido.calcularCostoTotal();
        // 20000 (base) + 20000*0.17 (tax 3400) + 2*500 (adicionales 1000) = 24400.0
        double expectedTotal = 20000.0 + (20000.0 * 0.17) + 1000.0; 
        double actualTotal = pedido.getCostoTotal();

        if (assertEquals(expectedTotal, actualTotal, DELTA)) {
            System.out.println("  PASS: Expected: " + expectedTotal + ", Actual: " + actualTotal);
        } else {
            System.out.println("  FAIL: Expected: " + expectedTotal + ", Actual: " + actualTotal);
        }
        System.out.println("------------------------------------");
    }

    public static void testTotalCalculationWithZeroBasePrice() {
        System.out.println("Testing Total Calculation with Zero Base Price...");
        IVehiculo vehiculo = createMockVehiculo(0.0);
        InterfazImpuestoStrategy impuestoStrategy = new ImpuestoMoto(); // 6% tax, but on 0.0 base
        ICliente cliente = createMockCliente();
        Usuario vendedor = createMockVendedor();
        IFormaDePago formaDePago = null;

        Pedido pedido = new Pedido(
                3, 3, new Date(), cliente, vehiculo, "Creado",
                formaDePago, impuestoStrategy, vehiculo.getPrecioBase(),
                null, null, vendedor, "Datos Facturacion Test Zero", "Ventas"
        );

        // No adicionales
        pedido.calcularCostoTotal();
        double expectedTotal = 0.0 + (0.0 * 0.06) + 0.0; // Should be 0.0
        double actualTotal = pedido.getCostoTotal();

        if (assertEquals(expectedTotal, actualTotal, DELTA)) {
            System.out.println("  PASS: Expected: " + expectedTotal + ", Actual: " + actualTotal);
        } else {
            System.out.println("  FAIL: Expected: " + expectedTotal + ", Actual: " + actualTotal);
        }
        System.out.println("------------------------------------");
    }

    public static void main(String[] args) {
        System.out.println("===== Iniciando Pruebas de Cálculo de Costo Total de Pedido =====");
        testBasicTotalCalculation();
        testTotalCalculationWithAdicionales();
        testTotalCalculationWithZeroBasePrice();
        System.out.println("===== Pruebas de Cálculo de Costo Total de Pedido Finalizadas =====");
    }
}
