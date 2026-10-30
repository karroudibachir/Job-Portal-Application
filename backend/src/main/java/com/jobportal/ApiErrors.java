package com.jobportal;
import org.springframework.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice class ApiErrors {
 @ExceptionHandler(AuthenticationException.class) ResponseEntity<ProblemDetail> authentication(AuthenticationException e){return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED,"Invalid credentials or authentication token"));}
 @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<ProblemDetail> validation(MethodArgumentNotValidException e){String detail=e.getBindingResult().getFieldErrors().stream().map(x->x.getField()+": "+x.getDefaultMessage()).findFirst().orElse("Request validation failed");return ResponseEntity.badRequest().body(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,detail));}
 @ExceptionHandler(ResponseStatusException.class) ResponseEntity<ProblemDetail> status(ResponseStatusException e){HttpStatusCode status=e.getStatusCode();return ResponseEntity.status(status).body(ProblemDetail.forStatusAndDetail(status,e.getReason()==null?"Request failed":e.getReason()));}
}
