
import java.io.*;
import java.util.*;

/**
 * 
 */
public class Transferencia extends FormaDePago {

    /**
     * Default constructor
     */
    public Transferencia() {
    }

    /**
     * 
     */
    private String tipoDeTransferencia;

    /**
     * 
     */
    private String banco;

    public Transferencia(String tipoDeTransferencia, String banco) {
        this.tipoDeTransferencia = tipoDeTransferencia;
        this.banco = banco;
    }

    /**
     * 
     */
    public String getTipoDeTransferencia() {
        return tipoDeTransferencia;
    }

    /**
     * 
     */
    public String getBanco() {
        return banco;
    }

    /**
     * 
     */
    public void setTipoDeTransferencia(String tipoDeTransferencia) {
        this.tipoDeTransferencia = tipoDeTransferencia;
    }

    /**
     * 
     */
    public void setBanco(String banco) {
        this.banco = banco;
    }

    /**
     * @param montoTotal
     */
    @Override
    public void procesarPago(double montoTotal) {
        // Logic to process transfer payment using this.tipoDeTransferencia and this.banco
        // For example:
        System.out.println("Procesando pago por transferencia: " + montoTotal +
                           " tipo: " + this.tipoDeTransferencia + " con el banco " + this.banco);
        // TODO: Implement actual payment processing logic
    }

}