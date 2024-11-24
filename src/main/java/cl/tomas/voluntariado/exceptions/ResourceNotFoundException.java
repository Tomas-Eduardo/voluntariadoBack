package cl.tomas.voluntariado.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    // Constructor que acepta un mensaje
    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Constructor que acepta mensaje y causa
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
