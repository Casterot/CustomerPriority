package com.customerpriority.sig.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalStateException.class)
    public String handleIllegalStateException(IllegalStateException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error";
        // Nombre de la plantilla de error
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFoundException(EntityNotFoundException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error";
        // Nombre de la plantilla de error
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrityViolationException(DataIntegrityViolationException e, Model model) {
        model.addAttribute("errorMessage", "No se puede eliminar el trabajador porque está asociado a otros datos.");
        return "error";
        // Nombre de la plantilla de error
    }
    // Otros manejadores de excepciones si es necesario
}