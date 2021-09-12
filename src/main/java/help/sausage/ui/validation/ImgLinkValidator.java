package help.sausage.ui.validation;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;

public class ImgLinkValidator implements Validator<String> {
    private String imgUrl;
    private String errorMsg;

    public ImgLinkValidator(String imgUrl, String errorMsg) {
        this.imgUrl = imgUrl;
        this.errorMsg = errorMsg;
    }

    @Override
    public ValidationResult apply(String value, ValueContext context) {
        Image image = new Image();
        return null;
    }
}
