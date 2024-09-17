/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.github.adminfaces.starter.components;

import jakarta.faces.component.FacesComponent;
import jakarta.faces.component.UIComponentBase;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;

@FacesComponent("customButton")
public class CustomButton extends UIComponentBase {

    @Override
    public String getFamily() {
        return "custom.components";
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
    writer.startElement("button", this);
    writer.writeAttribute("id", getClientId(context), null);
    writer.writeAttribute("class", "custom-button", null);

    String value = (String) getAttributes().get("value");
    writer.writeText(value != null ? value : "Default Button", null);

    // Obtener la lista de colores
    String colorList = (String) getAttributes().get("colorList");
    if (colorList != null) {
        // Separar la lista de colores por coma
        List<String> colors = List.of(colorList.split(","));
        // Aplicar la lógica de cambio de color (puede ser JavaScript)
        String script = "this.style.backgroundColor = '" + colors.get(0) + "';";
        for (int i = 1; i < colors.size(); i++) {
            script += " setTimeout(() => { this.style.backgroundColor = '" + colors.get(i) + "'; }, " + (i * 1000) + ");";
        }
        writer.writeAttribute("onclick", script, null);
    }

    writer.endElement("button");
    }

    private void renderColorChangeScript(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        List<String> colors = getColorList();
        if (colors != null && !colors.isEmpty()) {
            String clientId = getClientId(context);
            writer.startElement("script", null);
            writer.writeText("document.addEventListener('DOMContentLoaded', function() {", null);
            writer.writeText("var button = document.getElementById('" + clientId + "');", null);
            writer.writeText("var colors = " + colors.toString() + ";", null);
            writer.writeText("var index = 0;", null);
            writer.writeText("button.addEventListener('click', function() {", null);
            writer.writeText("index = (index + 1) % colors.length;", null);
            writer.writeText("button.style.backgroundColor = colors[index];", null);
            writer.writeText("});", null);
            writer.writeText("});", null);
            writer.endElement("script");
        }
    }

    @Override
    public void decode(FacesContext context) {
        // Handle decode if needed
    }

    public String getValue() {
        return (String) getStateHelper().eval("value", "Click me!");
    }

    public void setValue(String value) {
        getStateHelper().put("value", value);
    }

    @SuppressWarnings("unchecked")
    public List<String> getColorList() {
        return (List<String>) getStateHelper().eval("colorList", null);
    }

    public void setColorList(List<String> colorList) {
        getStateHelper().put("colorList", colorList);
    }
}