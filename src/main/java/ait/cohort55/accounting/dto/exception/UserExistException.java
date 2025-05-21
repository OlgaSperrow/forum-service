package ait.cohort55.accounting.dto.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
@NoArgsConstructor
public class UserExistException extends RuntimeException {
    public UserExistException(String message) {
        super(message);
    }
}
