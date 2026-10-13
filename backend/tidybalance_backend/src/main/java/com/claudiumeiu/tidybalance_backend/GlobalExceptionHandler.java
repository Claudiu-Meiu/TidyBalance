
package com.claudiumeiu.tidybalance_backend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.claudiumeiu.tidybalance_backend.user.exception.NicknameAlreadyExistsException;
import com.claudiumeiu.tidybalance_backend.user.exception.UsernameAlreadyExistsException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NicknameAlreadyExistsException.class)
    public ProblemDetail handleNicknameAlreadyExists(
            NicknameAlreadyExistsException exception) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                exception.getMessage());

        problem.setTitle("Conflict");
        problem.setProperty("code", "NICKNAME_TAKEN");

        return problem;
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ProblemDetail handleUsernameAlreadyExists(
            UsernameAlreadyExistsException exception) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                exception.getMessage());

        problem.setTitle("Conflict");
        problem.setProperty("code", "USERNAME_TAKEN");

        return problem;
    }
}
