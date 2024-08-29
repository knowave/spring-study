package board.board_spring.global.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final String message;
    private final HttpStatus status;

    public CustomException(String message, HttpStatus status) {
        super(message);
        this.message = message;
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
