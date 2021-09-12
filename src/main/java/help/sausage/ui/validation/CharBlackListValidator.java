package help.sausage.ui.validation;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.validator.AbstractValidator;

public class CharBlackListValidator extends AbstractValidator<String> {

    private final String blackListChar;

    public CharBlackListValidator(String blackListChar) {
        super("Illegal character {0}");
        this.blackListChar = blackListChar;
    }

    @Override
    public ValidationResult apply(String value, ValueContext context) {
        if (value == null) {
            return toResult(value, true);
        }
        for (char c: blackListChar.toCharArray()) {
           if (value.contains(""+c)) {
               return toResult(""+c, false);
           }
        }
        return toResult(value, true);
    }
}
