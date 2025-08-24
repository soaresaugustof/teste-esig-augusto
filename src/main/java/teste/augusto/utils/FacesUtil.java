package teste.augusto.utils;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class FacesUtil {

    private FacesUtil() {}

    public static void mensagemDeInfo(String titulo, String detalhe) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, titulo, detalhe));
    }

    public static void mensagemDeErro(String titulo, String detalhe) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, titulo, detalhe));
    }
}
