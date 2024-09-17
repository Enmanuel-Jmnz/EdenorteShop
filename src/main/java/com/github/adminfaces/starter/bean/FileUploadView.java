package com.github.adminfaces.starter.bean;

import java.io.Serializable;
import com.github.adminfaces.starter.bean.ExcelRow;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFSheet;

@Named
@ViewScoped
public class FileUploadView implements Serializable {

    private UploadedFile file;
    private String uploadedFilePath;
    private List<ExcelRow> excelData;
    private List<String> columnHeaders;

    public void handleFileUpload(FileUploadEvent event) {
        this.file = event.getFile();
        FacesMessage message = new FacesMessage("Successful", file.getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, message);

        // Verificar que el archivo subido es un archivo Excel
        if (!file.getFileName().endsWith(".xlsx")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El archivo subido no es un archivo Excel."));
            return;
        }

        // Guardar el archivo temporalmente en el servidor
        try (InputStream input = file.getInputStream()) {
            Path tempFilePath = Files.createTempFile("uploaded_", "_" + file.getFileName());
            Files.copy(input, tempFilePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            uploadedFilePath = tempFilePath.toString();
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al guardar el archivo."));
            e.printStackTrace();
        }
        readExcelFile();
    }

    public void downloadFile() {
        if (uploadedFilePath != null) {
            try {
                // Leer y modificar el archivo Excel
                Path path = Paths.get(uploadedFilePath);
                FileInputStream fileInputStream = new FileInputStream(path.toFile());
                Workbook workbook = new XSSFWorkbook(fileInputStream);

                // Crear una hoja y añadir un mensaje personalizado
                Sheet sheet = workbook.getSheetAt(0); // Obtener la primera hoja
                Row row = sheet.createRow(sheet.getLastRowNum() + 2); // Añadir una nueva fila al final
                Cell cell = row.createCell(0); // Añadir una celda a la fila
                cell.setCellValue("Esto es un mensaje para saber que el excel fue modificado."); // Escribir el mensaje

                // Guardar el archivo modificado en el servidor temporal
                File modifiedFile = Files.createTempFile("modified_", "_" + file.getFileName()).toFile();
                try (FileOutputStream outputStream = new FileOutputStream(modifiedFile)) {
                    workbook.write(outputStream);
                    workbook.close();
                }

                // Descargar el archivo modificado
                FacesContext facesContext = FacesContext.getCurrentInstance();
                facesContext.getExternalContext().setResponseContentType(Files.probeContentType(modifiedFile.toPath()));
                facesContext.getExternalContext().setResponseHeader("Content-Disposition", "attachment;filename=modified_" + this.file.getFileName());
                facesContext.getExternalContext().setResponseContentLength((int) modifiedFile.length());

                try (FileInputStream input = new FileInputStream(modifiedFile); OutputStream output = facesContext.getExternalContext().getResponseOutputStream()) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = input.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                    }
                    output.flush();
                }
                facesContext.responseComplete();
            } catch (IOException e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error al descargar el archivo."));
                e.printStackTrace();
            }
        }
    }

    public void readExcelFile() {
        excelData = new ArrayList<>();
        columnHeaders = new ArrayList<>();

        try (InputStream fileInputStream = file.getInputStream(); XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream)) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            for ( Iterator<Row> iterator = sheet.iterator(); iterator.hasNext();){
                Row row = iterator.next();
            }
                if (rowIterator.hasNext()) {
                    Row firstRow = rowIterator.next();  // Primera fila es la de los encabezados
                    for (Cell cell : firstRow) {
                        String cellValue = getCellValueAsString(cell);
                        System.out.println("Header: " + cellValue);  // Mensaje de depuración
                        columnHeaders.add(cellValue);  // Añadir cada celda como encabezado de columna
                    }
                }

            while (rowIterator.hasNext()) {  // Leer las filas restantes como datos
                Row row = rowIterator.next();
                List<String> rowData = new ArrayList<>();

                for (Cell cell : row) {
                    String cellValue = getCellValueAsString(cell);
                    rowData.add(cellValue);  // Añadir cada celda como dato de fila
                }

                excelData.add(new ExcelRow(rowData));  // Crear una nueva fila ExcelRow
            }

            // Mensaje de depuración para verificar los encabezados
            System.out.println("Column Headers: " + columnHeaders);
        } catch (IOException e) {
            e.printStackTrace();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to read Excel file.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    private String getCellValueAsString(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public List<ExcelRow> getExcelData() {
        return excelData;
    }

    public List<String> getColumnHeaders() {
        return columnHeaders;
    }
}
