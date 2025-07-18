package mb.iot.display;

public class DisplayException extends Exception {
    private static final long serialVersionUID = 1L;

    public DisplayException() {
    }

    public DisplayException(String message) {
        super(message);
    }

    public DisplayException(Throwable cause) {
        super(cause);
    }

    public DisplayException(String message, Throwable cause) {
        super(message, cause);
    }
}
