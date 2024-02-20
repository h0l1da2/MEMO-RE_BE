package sori.jakku.kkunkkyu.memore.common.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import sori.jakku.kkunkkyu.memore.common.annotation.constraint.MemoValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {MemoValidator.class})
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface Username {
    String message() default "아이디 형식이 다릅니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
