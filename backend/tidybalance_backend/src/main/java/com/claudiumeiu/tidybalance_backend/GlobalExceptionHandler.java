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

        private static ProblemDetail unauthorized(RuntimeException exception, String code) {
                ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED,
                                exception.getMessage());

                problem.setTitle("Unauthorized");
                problem.setProperty("code", code);

                return problem;
        }

        private static ProblemDetail notFound(RuntimeException exception, String code) {
                ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                                HttpStatus.NOT_FOUND,
                                exception.getMessage());

                problem.setTitle("Not found");
                problem.setProperty("code", code);

                return problem;
        }

        private static ProblemDetail badRequest(RuntimeException exception, String code) {
                ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                                exception.getMessage());

                problem.setTitle("Bad Request");
                problem.setProperty("code", code);

                return problem;
        }

        private static ProblemDetail conflict(RuntimeException exception, String code) {
                ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                                HttpStatus.CONFLICT,
                                exception.getMessage());

                problem.setTitle("Conflict");
                problem.setProperty("code", code);

                return problem;
        }

        @ExceptionHandler(IncorrectPasswordException.class)
        public ProblemDetail handleIncorrectPassword(IncorrectPasswordException exception) {
                return unauthorized(exception, "INCORRECT_PASSWORD");
        }

        @ExceptionHandler(NewPasswordMustBeDifferentException.class)
        public ProblemDetail handleNewPasswordMustBeDifferent(
                        NewPasswordMustBeDifferentException exception) {
                return badRequest(exception, "NEW_PASSWORD_MUST_BE_DIFFERENT");
        }

        @ExceptionHandler(RetypedNewPasswordDoesntMatchException.class)
        public ProblemDetail handleRetypedNewPasswordDoesntMatch(
                        RetypedNewPasswordDoesntMatchException exception) {
                return badRequest(exception, "RETYPED_NEW_PASSWORD_DOESNT_MATCH");
        }

        @ExceptionHandler(NicknameAlreadyExistsException.class)
        public ProblemDetail handleNicknameAlreadyExists(
                        NicknameAlreadyExistsException exception) {
                return conflict(exception, "NICKNAME_TAKEN");
        }

        @ExceptionHandler(UsernameAlreadyExistsException.class)
        public ProblemDetail handleUsernameAlreadyExists(
                        UsernameAlreadyExistsException exception) {
                return conflict(exception, "USERNAME_TAKEN");
        }

        @ExceptionHandler(PublicUserProfileNotFoundException.class)
        public ProblemDetail handlePublicUserProfileNotFound(
                        PublicUserProfileNotFoundException exception) {
                return notFound(exception, "USER_ID_NOT_FOUND");
        }
}
