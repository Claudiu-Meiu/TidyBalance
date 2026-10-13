package com.claudiumeiu.tidybalance_backend;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class SpaController {

    @GetMapping({
            "/{path:^(?!api$|assets$)[^.]*}",
            "/{path:^(?!api$|assets$)[^.]*}/**"
    })
    public String forwardToAngular(HttpServletRequest request) {
        String path = request.getRequestURI()
                .substring(request.getContextPath().length());

        if (path.equals("/api") || path.startsWith("/api/")) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return "forward:/index.html";
    }
}
