package com.github.adminfaces.starter.components;

import jakarta.faces.component.FacesComponent;
import jakarta.faces.component.UIComponentBase;

@FacesComponent("com.github.adminfaces.starter.components.DataTableComponent")
public class DataTableComponent extends UIComponentBase {

    @Override
    public String getFamily() {
        return "javax.faces.Data";
    }

    // Define los atributos necesarios
    public String getValue() {
        return (String) getStateHelper().eval("value");
    }

    public void setValue(String value) {
        getStateHelper().put("value", value);
    }

    public String getVar() {
        return (String) getStateHelper().eval("var");
    }

    public void setVar(String var) {
        getStateHelper().put("var", var);
    }

    public String getId() {
        return (String) getStateHelper().eval("id");
    }

    public void setId(String id) {
        getStateHelper().put("id", id);
    }

    public int getRows() {
        return (Integer) getStateHelper().eval("rows", 10);
    }

    public void setRows(int rows) {
        getStateHelper().put("rows", rows);
    }
}

