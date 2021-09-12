package help.sausage.ui.validation;

import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.validator.AbstractValidator;
import java.util.List;

public class DelegateValidator<T> extends AbstractValidator<T> {

    private List<Validator<T>> delegates;

    /**
     * Constructs a validator with the given error message. The substring "{0}" is replaced by the value
     * that failed validation.
     *
     * @param errorMessage the message to be included in a failed result, not null
     */
    public DelegateValidator(String errorMessage, List<Validator<T>> delegates) {
        super(errorMessage);
        this.delegates = delegates;
    }

    public DelegateValidator(List<Validator<T>> delegates) {
        this("Error while validating {0}", delegates);
    }


    @Override
    public ValidationResult apply(T value, ValueContext context) {
        return delegates.stream().map(d -> d.apply(value, context))
                .reduce(this::accumulateResult)
                .orElseGet(ValidationResult::ok);
    }

    private ValidationResult accumulateResult(ValidationResult acc, ValidationResult res) {
        if (!res.isError()) {
            return acc;
        }

        if (!acc.isError()) {
            return res;
        }
        String msg = acc.getErrorMessage() + " --- " + res.getErrorMessage();
        return ValidationResult.error(msg);
    }

}
