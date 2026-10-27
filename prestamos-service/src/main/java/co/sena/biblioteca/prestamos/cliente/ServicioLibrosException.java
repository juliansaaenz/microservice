package co.sena.biblioteca.prestamos.cliente;

/** Error al hablar con libros-service (no responde, libro inexistente, sin ejemplares...). */
public class ServicioLibrosException extends RuntimeException {

    public ServicioLibrosException(String mensaje) {
        super(mensaje);
    }
}
