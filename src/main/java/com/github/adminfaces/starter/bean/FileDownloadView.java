package com.github.adminfaces.starter.bean;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named
@RequestScoped
public class FileDownloadView {

    private StreamedContent file;
    public void saveChartImage() {
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    String imageData = request.getParameter("image");

    // Eliminar el prefijo "data:image/png;base64,"
    String base64Image = imageData.split(",")[1];
    byte[] imageBytes = Base64.getDecoder().decode(base64Image);

    // Guardar la imagen en algún lugar accesible
    // Por ejemplo, en el contexto de la aplicación
    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("chartImage", imageBytes);
}
    public FileDownloadView() {
    byte[] chartImage = (byte[]) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chartImage");

    InputStream stream = new ByteArrayInputStream(chartImage);

    file = DefaultStreamedContent.builder()
            .name("downloaded.png")
            .contentType("image/png")
            .stream(() -> stream)
            .build();
}


    public StreamedContent getFile() {
        return file;
    }
}
