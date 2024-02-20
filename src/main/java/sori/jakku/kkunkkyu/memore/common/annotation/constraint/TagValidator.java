package sori.jakku.kkunkkyu.memore.common.annotation.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import sori.jakku.kkunkkyu.memore.common.annotation.Tag;

import java.text.MessageFormat;

public class TagValidator implements ConstraintValidator<Tag, String> {

    private final int MIN_SIZE = 1;
    private final int MAX_SIZE = 20;
    private final String REGEXP = ".*[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z\\d+.]+.*";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank() || value.isEmpty() || value.length() > MAX_SIZE + 1 || !value.matches(REGEXP)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    MessageFormat.format("태그 길이는 {0}자 이상, {1}자 이하여야 하며, 한글 또는 영대소문자만 사용 가능합니다.", MIN_SIZE, MAX_SIZE)
            ).addConstraintViolation();
            return false;
        }

        return true;
    }
}
