package sori.jakku.kkunkkyu.memore.common.annotation.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import sori.jakku.kkunkkyu.memore.common.annotation.Memo;

import java.text.MessageFormat;

public class MemoValidator implements ConstraintValidator<Memo, String> {

    private final int MIN_SIZE = 1;
    private final int MAX_SIZE = 20;
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank() || value.isEmpty() || value.length() > MAX_SIZE + 1) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    MessageFormat.format("메모 길이는 {0}자 이상, {1}자 이하여야 합니다.", MIN_SIZE, MAX_SIZE)
            ).addConstraintViolation();
            return false;
        }
        return true;
    }
}
