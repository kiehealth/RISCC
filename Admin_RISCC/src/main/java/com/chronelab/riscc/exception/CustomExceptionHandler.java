package com.chronelab.riscc.exception;

import com.chronelab.riscc.dto.ServiceResponse;
import com.chronelab.riscc.util.PropertiesUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice
public class CustomExceptionHandler {

    private static final Logger LOG = LogManager.getLogger();

    private final PropertiesUtil propertiesUtil;
    private final ErrorMessageUtil errorMessageUtil;

    @Autowired
    public CustomExceptionHandler(PropertiesUtil propertiesUtil, ErrorMessageUtil errorMessageUtil) {
        this.propertiesUtil = propertiesUtil;
        this.errorMessageUtil = errorMessageUtil;
    }

    @ExceptionHandler(value = CustomException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ServiceResponse> handleException(CustomException e) {
        LOG.error("----- " + errorMessageUtil.getMessage(e.getErrorCode()) + " -----");
        e.printStackTrace();
        ServiceResponse serviceResponse = new ServiceResponse()
                .setStatus(false)
                .setMessage(errorMessageUtil.getMessage(e.getErrorCode()));
        if (e.getFieldName() != null) {
            serviceResponse.addData("field", e.getFieldName());
        }
        if (e.getRow() != null) {
            serviceResponse.setMessage(serviceResponse.getMessage() + " (Row: " + e.getRow() + ")");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(serviceResponse);
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ServiceResponse> handleException(BadCredentialsException e) {
        LOG.error("----- " + e.getMessage() + " -----");
        e.printStackTrace();
        ServiceResponse serviceResponse = new ServiceResponse()
                .setStatus(false)
                .setMessage(errorMessageUtil.getMessage("ERR002"));
        return ResponseEntity.status(HttpStatus.OK).body(serviceResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ServiceResponse> handleException(AccessDeniedException e) {
        LOG.error("----- " + e.getMessage() + " -----");
        e.printStackTrace();
        ServiceResponse serviceResponse = new ServiceResponse()
                .setStatus(false)
                .setMessage(errorMessageUtil.getMessage("ERR003"));
        return ResponseEntity.status(HttpStatus.OK).body(serviceResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ServiceResponse> handleValidation(MethodArgumentNotValidException e) {
        LOG.error("----- " + e.getMessage() + " -----");
        e.printStackTrace();

        List<String> errors = e.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());

        ServiceResponse serviceResponse = new ServiceResponse().setStatus(false)
                .setMessage(errors.toString());

        return ResponseEntity.status(HttpStatus.OK).body(serviceResponse);
    }

    @ExceptionHandler(value = InvalidDataAccessApiUsageException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ServiceResponse> handleException(InvalidDataAccessApiUsageException e) {
        LOG.error("----- " + e.getMessage() + " -----");
        e.printStackTrace();
        ServiceResponse serviceResponse = new ServiceResponse()
                .setStatus(false)
                .setMessage(errorMessageUtil.getMessage("ERR001"));
        return ResponseEntity.status(HttpStatus.OK).body(serviceResponse);
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ServiceResponse> handleException(DataIntegrityViolationException e) {
        LOG.error("----- " + e.getMessage() + " -----");
        e.printStackTrace();
        ServiceResponse serviceResponse = new ServiceResponse()
                .setStatus(false)
                .setMessage(errorMessageUtil.getMessage("ERR007"));
        return ResponseEntity.status(HttpStatus.OK).body(serviceResponse);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ServiceResponse> handleException(Exception e) {
        LOG.error("----- " + e.getMessage() + " -----");
        e.printStackTrace();
        ServiceResponse serviceResponse = new ServiceResponse()
                .setStatus(false)
                .setMessage(errorMessageUtil.getMessage("ERR001"));
        return ResponseEntity.status(HttpStatus.OK).body(serviceResponse);
    }
}
