package teste.augusto.utils;

// dentro de um novo pacote teste.augusto.validators

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("custom.passwordValidator")
public class PasswordValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        UIComponent passwordComponent = component.findComponent("senha");
        String senha = (String) passwordComponent.getAttributes().get("value");
        String confirmarSenha = (String) value;

        if (!senha.equals(confirmarSenha)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "As senhas não coincidem.", null);
            throw new ValidatorException(msg);
        }
    }
}