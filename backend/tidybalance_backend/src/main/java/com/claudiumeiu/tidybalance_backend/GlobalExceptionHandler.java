
package com.claudiumeiu.tidybalance_backend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.claudiumeiu.tidybalance_backend.user.exception.IncorrectPasswordException;
import com.claudiumeiu.tidybalance_backend.user.exception.NewPasswordMustBeDifferentException;
import com.claudiumeiu.tidybalance_backend.user.exception.RetypedNewPasswordDoesntMatchException;
import com.claudiumeiu.tidybalance_backend.user.exception.NicknameAlreadyExistsException;
import com.claudiumeiu.tidybalance_backend.user.exception.PublicUserProfileNotFoundException;
import com.claudiumeiu.tidybalance_backend.user.exception.UsernameAlreadyExistsException;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(IncorrectPasswordException.class)
        public ProblemDetail handleIncorrectPassword(IncorrectPasswordException exception) {
                ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED,
                                exception.getMessage());

                problem.setTitle("Unauthorized");
                problem.setProperty("code", "INCORRECT_PASSWORD");

                return problem;
        }

        @ExceptionHandler(NewPasswordMustBeDifferentException.class)
        public ProblemDetail handleNewPasswordMustBeDifferent(
                        NewPasswordMustBeDifferentException exception) {
                ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                                exception.getMessage());

                problem.setTitle("Bad Request");
                problem.setProperty("code", "NEW_PASSWORD_MUST_BE_DIFFERENT");

                return problem;
        }

        @ExceptionHandler(RetypedNewPasswordDoesntMatchException.class)
        public ProblemDetail handleNewPasswordsDontMatch(
                        RetypedNewPasswordDoesntMatchException exception) {
                ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                                exception.getMessage());

                problem.setTitle("Bad Request");
                problem.setProperty("code", "RETYPED_NEW_PASSWORD_DOESNT_MATCH");

                return problem;
        }

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

        @ExceptionHandler(PublicUserProfileNotFoundException.class)
        public ProblemDetail handlePublicUserProfileNotFound(
                        PublicUserProfileNotFoundException exception) {

                ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                                HttpStatus.NOT_FOUND,
                                exception.getMessage());

                problem.setTitle("Not found");
                problem.setProperty("code", "USER_ID_NOT_FOUND");

                return problem;
        }
}
