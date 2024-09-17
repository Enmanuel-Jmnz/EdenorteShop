package com.github.adminfaces.starter.bean;

import com.github.adminfaces.starter.ejb.ProductFacade;
import com.github.adminfaces.starter.entities.Product;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.export.ExcelOptions;

import org.primefaces.event.ColumnToggleEvent;
import org.primefaces.model.Visibility;

@Named(value = "productBean")
@SessionScoped
public class ProductBean implements Serializable {

    @Getter
    @Setter
    private Product product = new Product();

    @Getter
    @Setter
    private Product selectedProduct;

    @Getter
    @Setter
    private int spinnerValue = 1;  // Inicializar con un valor predeterminado

    @EJB
    private ProductFacade productFacade;

    @Inject
    private CartBean cartBean;

    @Getter
    @Setter
    private ExcelOptions excelOpt;

    @Getter
    @Setter
    private Map<Product, String> estadoProducto;

    @Getter
    @Setter
    private Map<Product, String> colorProducto = new HashMap<>();
    @Getter
    @Setter
    private Map<Integer, String> stockValues = new HashMap<>();

    @Getter
    @Setter
    private List<Product> products;

    @Getter
    @Setter
    private String searchName;

    @Getter
    @Setter
    private String filter;

    @Getter
    @Setter
    private List<Product> filteredProducts;

    @PostConstruct
    public void init() {
        products = productFacade.findAll();
        estadoProducto = new HashMap<>();
        products.forEach(product -> {
            estadoProducto.put(product, "Correcto"); // Estado inicial como "Correcto"
        });
    }
    
    public void actualizarEstado(Product product, String nuevoEstado) {
        estadoProducto.put(product, nuevoEstado);

    }

    public String obtenerClaseFila(Product product) {
        String estado = estadoProducto.get(product);
        if ("Incorrecto".equals(estado)) {
            System.out.println("Incorrectoooooo");
            return "row-incorrecto";

        } else {
            return "";
        }
    }

 public void onRowExportXLS(Object document) {
    SXSSFWorkbook wb = (SXSSFWorkbook) document;
    SXSSFSheet sheet = wb.getSheetAt(0);

    // Crear estilos
    CellStyle borderStyle = createBorderStyle(wb);
    CellStyle titleStyle = createTitleStyle(wb, (short) 18);
    CellStyle subtitleStyle = createTitleStyle(wb, (short) 12);
    CellStyle headerStyle = createHeaderStyle(wb);
    CellStyle correctStyle = createDataStyle(wb, HSSFColor.HSSFColorPredefined.BLACK.getIndex());
    CellStyle incorrectStyle = createDataStyle(wb, HSSFColor.HSSFColorPredefined.RED.getIndex());

    // Crear título
    createTitleRow(sheet, titleStyle, "Altas de contrato con errores", 0, 5);

    // Crear subtítulos
    long totalCount = estadoProducto.size();
    long incorrectCount = estadoProducto.values().stream().filter(estado -> "Incorrecto".equals(estado)).count();
    createSubtitleRow(sheet, subtitleStyle, "Cantidad de contratos dados de altas: " + totalCount, 1);
    createSubtitleRow(sheet, subtitleStyle, "Cantidad de contratos con errores: " + incorrectCount, 2);

    // Crear encabezados
    Map<Integer, String> headers = Map.of(
        0, "Code", 1, "Name", 2, "Description", 3, "Status", 4, "Stock", 5, "Price"
    );
    createHeaderRow(sheet, headerStyle, headers, 5);

    // Aplicar estilos a las filas de datos
    applyDataStyles(sheet, products, estadoProducto, correctStyle, incorrectStyle, 6);

    // Aplicar bordes a todas las celdas de la hoja a partir de la fila 6
    applyBorders(sheet, borderStyle, 6);
}

private CellStyle createBorderStyle(SXSSFWorkbook wb) {
    CellStyle style = wb.createCellStyle();
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    return style;
}

private CellStyle createTitleStyle(SXSSFWorkbook wb, short fontSize) {
    Font font = wb.createFont();
    font.setBold(true);
    font.setFontHeightInPoints(fontSize);

    CellStyle style = wb.createCellStyle();
    style.setFont(font);
    style.setAlignment(HorizontalAlignment.LEFT);
    style.setVerticalAlignment(VerticalAlignment.CENTER);
    return style;
}

private CellStyle createHeaderStyle(SXSSFWorkbook wb) {
    Font font = wb.createFont();
    font.setBold(true);
    font.setColor(IndexedColors.WHITE.getIndex());

    CellStyle style = wb.createCellStyle();
    style.setFont(font);
    style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    return style;
}

private CellStyle createDataStyle(SXSSFWorkbook wb, short fontColor) {
    Font font = wb.createFont();
    font.setColor(fontColor);

    CellStyle style = wb.createCellStyle();
    style.setFont(font);
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    return style;
}

private void createTitleRow(SXSSFSheet sheet, CellStyle style, String title, int startCol, int endCol) {
    Row row = sheet.createRow(0);
    Cell cell = row.createCell(0);
    cell.setCellValue(title);
    cell.setCellStyle(style);
    mergeCellsIfNotMerged(sheet, 0, 0, startCol, endCol);
}

private void createSubtitleRow(SXSSFSheet sheet, CellStyle style, String text, int rowIndex) {
    Row row = sheet.createRow(rowIndex);
    Cell cell = row.createCell(0);
    cell.setCellValue(text);
    cell.setCellStyle(style);
    mergeCellsIfNotMerged(sheet, rowIndex, rowIndex, 0, 5);
}

private void createHeaderRow(SXSSFSheet sheet, CellStyle style, Map<Integer, String> headers, int rowIndex) {
    Row row = sheet.createRow(rowIndex);
    headers.forEach((index, header) -> {
        Cell cell = row.createCell(index);
        cell.setCellValue(header);
        cell.setCellStyle(style);
    });
}

private void applyDataStyles(SXSSFSheet sheet, List<Product> products, Map<Product, String> estadoProducto, CellStyle correctStyle, CellStyle incorrectStyle, int startRow) {
    for (int rowIndex = startRow; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
        Row row = sheet.getRow(rowIndex);
        if (row != null && rowIndex - startRow < products.size()) {
            Product product = products.get(rowIndex - startRow);
            String estado = estadoProducto.get(product);
            CellStyle style = "Incorrecto".equals(estado) ? incorrectStyle : correctStyle;
            for (Cell cell : row) {
                cell.setCellStyle(style);
            }
        }
    }
}

private void applyBorders(SXSSFSheet sheet, CellStyle borderStyle, int startRow) {
    for (int rowIndex = startRow; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
        Row row = sheet.getRow(rowIndex);
        if (row != null) {
            for (int cellIndex = 0; cellIndex < row.getLastCellNum(); cellIndex++) {
                Cell cell = row.getCell(cellIndex);
                if (cell != null) {
                    CellStyle newStyle = sheet.getWorkbook().createCellStyle();
                    newStyle.cloneStyleFrom(cell.getCellStyle());
                    applyBorderStyle(newStyle, borderStyle);
                    cell.setCellStyle(newStyle);
                }
            }
        }
    }
}

private void applyBorderStyle(CellStyle targetStyle, CellStyle borderStyle) {
    targetStyle.setBorderTop(borderStyle.getBorderTop());
    targetStyle.setBorderRight(borderStyle.getBorderRight());
    targetStyle.setBorderBottom(borderStyle.getBorderBottom());
    targetStyle.setBorderLeft(borderStyle.getBorderLeft());
}

private void mergeCellsIfNotMerged(SXSSFSheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
    CellRangeAddress region = new CellRangeAddress(firstRow, lastRow, firstCol, lastCol);
    boolean alreadyMerged = sheet.getMergedRegions().stream().anyMatch(existingRegion -> existingRegion.intersects(region));
    if (!alreadyMerged) {
        sheet.addMergedRegion(region);
    }
}



    public void registerProduct() {
        productFacade.create(product);
        refreshProductList();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Product saved"));
    }

    public void updateProduct() {
        if (selectedProduct != null) {
            productFacade.edit(selectedProduct);
            refreshProductList();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Product updated"));
        }
    }

    public void deleteProduct() {
        if (selectedProduct != null) {
            productFacade.remove(selectedProduct);
            refreshProductList();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Product Deleted", "Product successfully deleted"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No product selected"));
        }
    }

    public void addToCart(Product product, int quantity) {
        CartItem cartItem = new CartItem(product, quantity);
        cartBean.addToCart(cartItem);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Added to Cart", "Product: " + product.getName() + " Quantity: " + quantity));
        this.spinnerValue = 1;  // Restablecer el valor del spinner
    }

    private void refreshProductList() {
        products = productFacade.findAll();
    }

    public void onToggle(ColumnToggleEvent e) {
        Integer index = (Integer) e.getData();
        UIColumn column = e.getColumn();
        Visibility visibility = e.getVisibility();
        String header = column.getAriaHeaderText() != null ? column.getAriaHeaderText() : column.getHeaderText();
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Column " + index + " toggled: " + header + " " + visibility, null);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
