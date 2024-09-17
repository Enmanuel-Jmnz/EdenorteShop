package com.github.adminfaces.starter.bean;

import java.io.Serializable;
import java.util.List;

public class ExcelRow implements Serializable {
    private List<String> columns;  // Lista de columnas de cada fila

    public ExcelRow(List<String> columns) {
        this.columns = columns;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public String getColumn(int index) {
        return columns.get(index);
    }
}
