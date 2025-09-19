package com.grupo9.sistemaConcesionaria.service;

import com.grupo9.sistemaConcesionaria.model.Cliente;
import com.grupo9.sistemaConcesionaria.model.Usuario;
import com.grupo9.sistemaConcesionaria.exception.DuplicateClienteException;
import com.grupo9.sistemaConcesionaria.exception.ClienteNotFoundException;
import com.grupo9.sistemaConcesionaria.exception.DuplicateUsuarioException;
import com.grupo9.sistemaConcesionaria.exception.UsuarioNotFoundException;
import java.util.Collection;

/**
 * Interfaz que define las operaciones para gestionar usuarios y clientes del sistema
 */
public interface InterfazGestorDeUsuario {

    // Gestión de Clientes
    void registrarCliente(Cliente cliente) throws DuplicateClienteException;
    Cliente getCliente(String documento) throws ClienteNotFoundException;
    void actualizarCliente(Cliente cliente) throws ClienteNotFoundException;
    void eliminarCliente(String documento) throws ClienteNotFoundException;
    Collection<Cliente> getTodosLosClientes();

    // Gestión de Usuarios del Sistema
    void registrarUsuarioSistema(Usuario usuario) throws DuplicateUsuarioException;
    Usuario getUsuarioSistema(String mail) throws UsuarioNotFoundException;
    void actualizarUsuarioSistema(Usuario usuario) throws UsuarioNotFoundException;
    void eliminarUsuarioSistema(String mail) throws UsuarioNotFoundException;
    Collection<Usuario> getTodosLosUsuariosSistema();
} 