package com.juvare.ipms.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Calendar;
import java.util.Date;

public class PastOrCurrentDateValidator implements ConstraintValidator<PastOrCurrentDate, Date> {

    public boolean isValid(Date value, ConstraintValidatorContext constraintValidatorContext) {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 23);
        now.set(Calendar.MINUTE, 59);
        now.set(Calendar.SECOND, 59);

        return value == null || value.before(now.getTime());
    }
}
