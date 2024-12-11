package com.datapar.ecommerce.Config;


import com.datapar.ecommerce.Exceptions.AppException;
import com.datapar.ecommerce.Exceptions.ErrorDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(value = {AppException.class})
    @ResponseBody
    public ResponseEntity<ErrorDTO> AppException(AppException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(new ErrorDTO(e.getMessage()));
    }
}
