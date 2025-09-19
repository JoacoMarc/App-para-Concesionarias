
import java.io.*;
import java.util.*;

/**
 * 
 */
import java.util.Collection;
// Assuming Cliente, Usuario, and specific exceptions are in the controller package or imported.

public interface InterfazGestorDeUsuario {

    // Client Management
    void registrarCliente(Cliente cliente) throws DuplicateClienteException;
    Cliente getCliente(String documento) throws ClienteNotFoundException;
    void actualizarCliente(Cliente cliente) throws ClienteNotFoundException;
    void eliminarCliente(String documento) throws ClienteNotFoundException;
    Collection<Cliente> getTodosLosClientes();

    // System User Management
    void registrarUsuarioSistema(Usuario usuario) throws DuplicateUsuarioException;
    Usuario getUsuarioSistema(String mail) throws UsuarioNotFoundException;
    void actualizarUsuarioSistema(Usuario usuario) throws UsuarioNotFoundException;
    void eliminarUsuarioSistema(String mail) throws UsuarioNotFoundException;
    Collection<Usuario> getTodosLosUsuariosSistema();
}