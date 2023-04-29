package example.end_course.util;

import example.end_course.model.TypeCourse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Validator {

    public static <T> List<String> validateObject(T object) {
        jakarta.validation.Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(object);

        List<String> errors = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        return errors;
    }
}
