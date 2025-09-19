
import java.io.*;
import java.util.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;

// Assuming Cliente, Usuario, and specific exceptions are in the controller package or imported.

/**
 * Manages clients and system users, implementing the Singleton pattern.
 */
public class GestorDeUsuarios implements InterfazGestorDeUsuario {

    private static GestorDeUsuarios instancia;
    private HashMap<String, Cliente> clientes; // Key: documento of the client
    private HashMap<String, Usuario> usuariosDelSistema; // Key: mail of the user

    /**
     * Private constructor for Singleton pattern.
     * Initializes the storage for clients and system users.
     */
    private GestorDeUsuarios() {
        this.clientes = new HashMap<>();
        this.usuariosDelSistema = new HashMap<>();
    }

    /**
     * Provides the global access point to the GestorDeUsuarios instance.
     * @return The single instance of GestorDeUsuarios.
     */
    public static GestorDeUsuarios getInstancia() {
        if (instancia == null) {
            instancia = new GestorDeUsuarios();
        }
        return instancia;
    }

    // --- Client Management ---

    @Override
    public void registrarCliente(Cliente cliente) throws DuplicateClienteException {
        if (cliente == null || cliente.getDocumento() == null || cliente.getDocumento().isEmpty()) {
            throw new IllegalArgumentException("El cliente o su documento no pueden ser nulos o vacíos.");
        }
        if (this.clientes.containsKey(cliente.getDocumento())) {
            throw new DuplicateClienteException("Cliente con documento " + cliente.getDocumento() + " ya existe.");
        }
        this.clientes.put(cliente.getDocumento(), cliente);
        System.out.println("Cliente registrado: " + cliente.getNombre() + " (Documento: " + cliente.getDocumento() + ")");
    }

    @Override
    public Cliente getCliente(String documento) throws ClienteNotFoundException {
        if (documento == null || documento.isEmpty()) {
            throw new IllegalArgumentException("El documento no puede ser nulo o vacío para buscar cliente.");
        }
        Cliente cliente = this.clientes.get(documento);
        if (cliente == null) {
            throw new ClienteNotFoundException("Cliente con documento " + documento + " no encontrado.");
        }
        return cliente;
    }

    @Override
    public void actualizarCliente(Cliente cliente) throws ClienteNotFoundException {
        if (cliente == null || cliente.getDocumento() == null || cliente.getDocumento().isEmpty()) {
            throw new IllegalArgumentException("El cliente o su documento no pueden ser nulos o vacíos para actualizar.");
        }
        if (!this.clientes.containsKey(cliente.getDocumento())) {
            throw new ClienteNotFoundException("Cliente con documento " + cliente.getDocumento() + " no encontrado para actualizar.");
        }
        this.clientes.put(cliente.getDocumento(), cliente);
        System.out.println("Cliente actualizado: " + cliente.getNombre() + " (Documento: " + cliente.getDocumento() + ")");
    }

    @Override
    public void eliminarCliente(String documento) throws ClienteNotFoundException {
        if (documento == null || documento.isEmpty()) {
            throw new IllegalArgumentException("El documento no puede ser nulo o vacío para eliminar cliente.");
        }
        Cliente clienteRemovido = this.clientes.remove(documento);
        if (clienteRemovido == null) {
            throw new ClienteNotFoundException("Cliente con documento " + documento + " no encontrado para eliminar.");
        }
        System.out.println("Cliente eliminado: " + clienteRemovido.getNombre() + " (Documento: " + documento + ")");
    }

    @Override
    public Collection<Cliente> getTodosLosClientes() {
        return new ArrayList<>(this.clientes.values());
    }

    // --- System User Management ---

    @Override
    public void registrarUsuarioSistema(Usuario usuario) throws DuplicateUsuarioException {
        if (usuario == null || usuario.getMail() == null || usuario.getMail().isEmpty()) {
            throw new IllegalArgumentException("El usuario del sistema o su mail no pueden ser nulos o vacíos.");
        }
        if (this.usuariosDelSistema.containsKey(usuario.getMail())) {
            throw new DuplicateUsuarioException("Usuario del sistema con mail " + usuario.getMail() + " ya existe.");
        }
        this.usuariosDelSistema.put(usuario.getMail(), usuario);
        System.out.println("Usuario del sistema registrado: " + usuario.getNombre() + " (Mail: " + usuario.getMail() + ")");
    }

    @Override
    public Usuario getUsuarioSistema(String mail) throws UsuarioNotFoundException {
        if (mail == null || mail.isEmpty()) {
            throw new IllegalArgumentException("El mail no puede ser nulo o vacío para buscar usuario del sistema.");
        }
        Usuario usuario = this.usuariosDelSistema.get(mail);
        if (usuario == null) {
            throw new UsuarioNotFoundException("Usuario del sistema con mail " + mail + " no encontrado.");
        }
        return usuario;
    }

    @Override
    public void actualizarUsuarioSistema(Usuario usuario) throws UsuarioNotFoundException {
        if (usuario == null || usuario.getMail() == null || usuario.getMail().isEmpty()) {
            throw new IllegalArgumentException("El usuario del sistema o su mail no pueden ser nulos o vacíos para actualizar.");
        }
        if (!this.usuariosDelSistema.containsKey(usuario.getMail())) {
            throw new UsuarioNotFoundException("Usuario del sistema con mail " + usuario.getMail() + " no encontrado para actualizar.");
        }
        this.usuariosDelSistema.put(usuario.getMail(), usuario);
        System.out.println("Usuario del sistema actualizado: " + usuario.getNombre() + " (Mail: " + usuario.getMail() + ")");
    }

    @Override
    public void eliminarUsuarioSistema(String mail) throws UsuarioNotFoundException {
        if (mail == null || mail.isEmpty()) {
            throw new IllegalArgumentException("El mail no puede ser nulo o vacío para eliminar usuario del sistema.");
        }
        Usuario usuarioRemovido = this.usuariosDelSistema.remove(mail);
        if (usuarioRemovido == null) {
            throw new UsuarioNotFoundException("Usuario del sistema con mail " + mail + " no encontrado para eliminar.");
        }
        System.out.println("Usuario del sistema eliminado: " + usuarioRemovido.getNombre() + " (Mail: " + mail + ")");
    }

    @Override
    public Collection<Usuario> getTodosLosUsuariosSistema() {
        return new ArrayList<>(this.usuariosDelSistema.values());
    }

    // Old placeholder methods like crearUsuario(), actualizarUsuario(), etc., are removed.
}