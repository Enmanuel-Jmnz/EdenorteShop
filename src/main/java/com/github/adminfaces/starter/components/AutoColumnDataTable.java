package com.github.adminfaces.starter.components;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.column.Column;
import jakarta.faces.context.FacesContext;
import jakarta.el.ValueExpression;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class AutoColumnDataTable extends DataTable {

    private List<?> data;

    public void setData(List<?> data) {
        this.data = data;
        createDynamicColumns();
    }

    public List<?> getData() {
        return data;
    }

    private void createDynamicColumns() {
        this.getChildren().clear();
        if (data != null && !data.isEmpty()) {
            Object firstRow = data.get(0);
            Map<String, Object> properties = getProperties(firstRow);
            for (String key : properties.keySet()) {
                Column column = new Column();
                column.setHeaderText(key);
                column.setValueExpression("value", createValueExpression("#{row['" + key + "']}"));
                this.getChildren().add(column);
            }
        }
    }

    private Map<String, Object> getProperties(Object bean) {
        Map<String, Object> properties = new HashMap<>();
        try {
            for (PropertyDescriptor pd : Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors()) {
                if (pd.getReadMethod() != null && !"class".equals(pd.getName())) {
                    properties.put(pd.getName(), pd.getReadMethod().invoke(bean));
                }
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return properties;
    }

    private ValueExpression createValueExpression(String expression) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getExpressionFactory()
                .createValueExpression(facesContext.getELContext(), expression, Object.class);
    }
}

