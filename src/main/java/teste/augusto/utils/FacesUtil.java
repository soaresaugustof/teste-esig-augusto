package teste.augusto.utils;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class FacesUtil {

    private FacesUtil() {}

    /**
     * Adiciona uma mensagem de informação.
     */
    public static void mensagemDeInfo(String titulo, String detalhe) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, titulo, detalhe));
    }

    /**
     * Adiciona uma mensagem de alerta.
     */
    public static void mensagemDeAviso(String titulo, String detalhe) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, titulo, detalhe));
    }

    /**
     * Adiciona uma mensagem de erro.
     */
    public static void mensagemDeErro(String titulo, String detalhe) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, titulo, detalhe));
    }
}
