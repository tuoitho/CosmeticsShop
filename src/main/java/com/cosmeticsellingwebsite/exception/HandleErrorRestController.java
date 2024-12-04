package com.cosmeticsellingwebsite.exception;

import com.cosmeticsellingwebsite.payload.response.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;

@ControllerAdvice
public class HandleErrorRestController
//        extends ResponseEntityExceptionHandler
{
    // Xử lý lỗi từ `@Valid` khi binding không thành công
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        BindingResult bindingResult = ex.getBindingResult();

        for (FieldError error : bindingResult.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
//        ApiResponse<Void> response = new ApiResponse<>(false, ex.getMessage(), null);
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//    }
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> handleGenericException(Exception ex) {
//        String error=ex.getMessage();
//
//        StackTraceElement[] lst= ex.getStackTrace();
//        StringBuilder errorDetails= new StringBuilder();
//        for (StackTraceElement element: lst) {
//            errorDetails.append(String.format("Error in %s at line %d: %s",
//                    element.getFileName(), element.getLineNumber(), ex.getMessage()));
//        }
//
//        // Tạo response với chi tiết lỗi
//        ApiResponse<?> response = new ApiResponse<>(false, error, errorDetails);
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//    }
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<?> handleGenericException(Exception ex) {
//        String error=ex.getMessage();
//        ex.printStackTrace();
//        StackTraceElement[] lst= ex.getStackTrace();
//        String message = ex.getMessage();
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
//    }
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(Exception e){

        e.printStackTrace();
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}