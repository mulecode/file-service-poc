package uk.co.mulecode.fileservice.component.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = {MultipartFileValidator.class})
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidIngestionFile {

    String pattern() default ".*";

    String message() default "{jakarta.validation.constraints.ValidIngestionFile.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
