package com.github.adminfaces.starter.bean;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Named
@ApplicationScoped
public class ReflectionHelper {

    public List<String> getFieldNames(Object obj) {
        List<String> fieldNames = new ArrayList<>();
        if (obj == null) {
            return fieldNames;  // Devuelve una lista vacía si el objeto es null
        }
        for (Field field : obj.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            fieldNames.add(field.getName());
        }
        return fieldNames;
    }

    public Object getFieldValue(Object obj, String fieldName) {
        if (obj == null || fieldName == null || fieldName.isEmpty()) {
            return null;  // Manejar caso de objeto o nombre de campo nulo
        }
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
