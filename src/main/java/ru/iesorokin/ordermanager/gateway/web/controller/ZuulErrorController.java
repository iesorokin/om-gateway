package ru.iesorokin.ordermanager.gateway.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.iesorokin.ordermanager.gateway.web.dto.ErrorResponse;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ZuulErrorController implements ErrorController {
    @Value("${error.path:/error}")
    private String errorPath;

    public String getErrorPath() {
        return errorPath;
    }

    @RequestMapping(value = "${error.path:/error}", produces = "application/vnd.error+json")
    public @ResponseBody
    ResponseEntity error(HttpServletRequest request) {
        final int status = getErrorStatus(request);
        final String errorMessage = getErrorMessage(request);
        return ResponseEntity.status(status).body(new ErrorResponse(errorMessage));
    }

    private int getErrorStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        return statusCode != null ? statusCode : HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    private String getErrorMessage(HttpServletRequest request) {
        final Throwable exc = (Throwable) request.getAttribute("javax.servlet.error.exception");
        return exc != null ? exc.getMessage() : "Unexpected error occurred";
    }
}
