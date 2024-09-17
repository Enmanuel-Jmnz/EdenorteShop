package com.github.adminfaces.starter.renderer;

import com.github.adminfaces.starter.components.AutoColumnDataTable;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.FacesRenderer;
import org.primefaces.component.datatable.DataTableRenderer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

@FacesRenderer(componentFamily = "org.primefaces.component", rendererType = "com.github.adminfaces.starter.renderer.AutoColumnDataTableRenderer")
public class AutoColumnDataTableRenderer extends DataTableRenderer {

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        AutoColumnDataTable table = (AutoColumnDataTable) component;
        ResponseWriter writer = context.getResponseWriter();

        // Renderizar el comienzo del DataTable
        writer.startElement("table", table);
        writer.writeAttribute("id", table.getClientId(context), "id");
        writer.writeAttribute("class", "ui-widget datatable-custom", "styleClass");

        // Obtener datos y propiedades
        List<?> data = (List<?>) table.getValue();
        if (data != null && !data.isEmpty()) {
            Object sampleObject = data.get(0);

            // Renderizar encabezados de columna
            writer.startElement("thead", table);
            writer.startElement("tr", table);

            for (Field field : sampleObject.getClass().getDeclaredFields()) {
                writer.startElement("th", table);
                writer.writeText(field.getName(), null);
                writer.endElement("th");
            }

            writer.endElement("tr");
            writer.endElement("thead");

            // Renderizar filas de datos
            writer.startElement("tbody", table);
            for (Object item : data) {
                writer.startElement("tr", table);

                for (Field field : sampleObject.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    try {
                        Object fieldValue = field.get(item);

                        writer.startElement("td", table);
                        writer.writeText(fieldValue != null ? fieldValue.toString() : "", null);
                        writer.endElement("td");
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

                writer.endElement("tr");
            }
            writer.endElement("tbody");
        }

        writer.endElement("table");
    }
}
