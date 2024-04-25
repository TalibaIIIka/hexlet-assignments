package exercise.exception;

// BEGIN
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String id) {
        super("Product with id " + id + " not found");
    }
}
// END
