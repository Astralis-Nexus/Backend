package exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final int statusCode;
    private final String timeStamp;

    public ApiException(int statusCode, String message, String timeStamp) {
        super(message);
        this.statusCode = statusCode;
        this.timeStamp = timeStamp;
    }
}