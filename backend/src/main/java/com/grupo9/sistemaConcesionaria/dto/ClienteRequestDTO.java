package com.grupo9.sistemaConcesionaria.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import com.grupo9.sistemaConcesionaria.model.TipoFacturacion;

/**
 * DTO para crear/actualizar clientes (compradores)
 */
public class ClienteRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede exceder 100 caracteres")
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    private String mail;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @NotBlank(message = "El documento es obligatorio")
    @Size(max = 20, message = "El documento no puede exceder 20 caracteres")
    private String documento;

    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String telefono;

    @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
    private String direccion;

    // Datos de facturación (opcionales)
    @Size(max = 150, message = "La razón social no puede exceder 150 caracteres")
    private String razonSocial;

    @Pattern(regexp = "^(20|23|24|27|30|33|34)[-]?\\d{8}[-]?\\d{1}$", 
             message = "CUIT/CUIL debe tener el formato válido argentino (ej: 20-12345678-9)")
    @Size(max = 15, message = "El CUIT/CUIL no puede exceder 15 caracteres")
    private String cuitCuil;

    @Size(max = 200, message = "La dirección de facturación no puede exceder 200 caracteres")
    private String direccionFacturacion;

    @Size(max = 100, message = "La ciudad de facturación no puede exceder 100 caracteres")
    private String ciudadFacturacion;

    @Size(max = 50, message = "La provincia de facturación no puede exceder 50 caracteres")
    private String provinciaFacturacion;

    @Pattern(regexp = "^[A-Z]?\\d{4}[A-Z]{3}$", 
             message = "Código postal debe tener formato argentino (ej: C1425, 1425ABC)")
    @Size(max = 10, message = "El código postal no puede exceder 10 caracteres")
    private String codigoPostalFacturacion;

    private TipoFacturacion tipoFacturacion = TipoFacturacion.CONSUMIDOR_FINAL;

    private boolean requiereFacturaA = false;

    // Constructors
    public ClienteRequestDTO() {}

    public ClienteRequestDTO(String nombre, String apellido, String mail, String password, 
                           String documento, String telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.mail = mail;
        this.password = password;
        this.documento = documento;
        this.telefono = telefono;
    }

    // Getters and Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getCuitCuil() {
        return cuitCuil;
    }

    public void setCuitCuil(String cuitCuil) {
        this.cuitCuil = cuitCuil;
    }

    public String getDireccionFacturacion() {
        return direccionFacturacion;
    }

    public void setDireccionFacturacion(String direccionFacturacion) {
        this.direccionFacturacion = direccionFacturacion;
    }

    public String getCiudadFacturacion() {
        return ciudadFacturacion;
    }

    public void setCiudadFacturacion(String ciudadFacturacion) {
        this.ciudadFacturacion = ciudadFacturacion;
    }

    public String getProvinciaFacturacion() {
        return provinciaFacturacion;
    }

    public void setProvinciaFacturacion(String provinciaFacturacion) {
        this.provinciaFacturacion = provinciaFacturacion;
    }

    public String getCodigoPostalFacturacion() {
        return codigoPostalFacturacion;
    }

    public void setCodigoPostalFacturacion(String codigoPostalFacturacion) {
        this.codigoPostalFacturacion = codigoPostalFacturacion;
    }

    public TipoFacturacion getTipoFacturacion() {
        return tipoFacturacion;
    }

    public void setTipoFacturacion(TipoFacturacion tipoFacturacion) {
        this.tipoFacturacion = tipoFacturacion;
    }

    public boolean isRequiereFacturaA() {
        return requiereFacturaA;
    }

    public void setRequiereFacturaA(boolean requiereFacturaA) {
        this.requiereFacturaA = requiereFacturaA;
    }
} 