package sori.jakku.kkunkkyu.memore.common.annotation.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import sori.jakku.kkunkkyu.memore.common.annotation.Password;

import java.text.MessageFormat;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    private final int MIN_SIZE = 6;
    private final int MAX_SIZE = 15;
    private final String REGEXP = "^[a-zA-Z0-9]+$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank() || value.length() < MIN_SIZE - 1 || value.length() > MAX_SIZE + 1 || !value.matches(REGEXP)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    MessageFormat.format("비밀번호 길이는 {0}자 이상, {1}자 이하이고 영어 대소문자, 숫자여야만 합니다.", MIN_SIZE, MAX_SIZE)
            ).addConstraintViolation();
            return false;
        }
        return true;
    }
}
