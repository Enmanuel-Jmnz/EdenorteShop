package com.github.adminfaces.starter.bean;

import com.github.adminfaces.starter.ejb.BilldetailFacade;
import com.github.adminfaces.starter.entities.Billdetail;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import software.xdev.chartjs.model.charts.BarChart;
import software.xdev.chartjs.model.charts.LineChart;
import software.xdev.chartjs.model.charts.PieChart;
import software.xdev.chartjs.model.color.Color;
import software.xdev.chartjs.model.data.BarData;
import software.xdev.chartjs.model.data.LineData;
import software.xdev.chartjs.model.data.PieData;
import software.xdev.chartjs.model.dataset.BarDataset;
import software.xdev.chartjs.model.dataset.LineDataset;
import software.xdev.chartjs.model.dataset.PieDataset;

@Named
@RequestScoped
public class ChartView implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private BilldetailFacade billdetailFacade;

    private String selectedChartType;
    private String pieModel;
    private String barModel;
    private String lineModel;
    private String pieModelSecondary;
    private String barModelSecondary;
    private String lineModelSecondary;
    private String chartModel;
    private List<String> chartTypes;
    
    @PostConstruct
    public void init() {
        chartTypes = Arrays.asList("Pie", "Bar", "Line");
        selectedChartType = chartTypes.get(0); // Establece un valor por defecto, como el primero en la lista
        createPieModel();
        createSecondaryPieModel(); 
    }

    private void createPieModel() {
        List<Billdetail> billdetails = billdetailFacade.findBySaleID(2);

        if (billdetails == null || billdetails.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "No data found for the given Sale ID"));
            return;
        } 

        PieDataset dataset = new PieDataset();
        PieData pieData = new PieData();

        for (Billdetail detail : billdetails) {
            dataset.addData(detail.getTotalPrice());
            pieData.addLabel(detail.getProduct().getName());
        }

        dataset.addBackgroundColors(new Color(255, 99, 132), new Color(54, 162, 235), new Color(255, 205, 86));

        pieData.addDataset(dataset);
        pieModel = new PieChart().setData(pieData).toJson();
    }

    private void createSecondaryPieModel() {
        
        List<Billdetail> billdetails = billdetailFacade.findBySaleID(3); 

        if (billdetails == null || billdetails.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "No data found for the given Sale ID"));
            return;
        }

        PieDataset dataset = new PieDataset();
        PieData pieData = new PieData();

        for (Billdetail detail : billdetails) {
            dataset.addData(detail.getTotalPrice());
            pieData.addLabel(detail.getProduct().getName());
        }

        dataset.addBackgroundColors(new Color(255, 99, 132), new Color(54, 162, 235), new Color(255, 205, 86));

        pieData.addDataset(dataset);
        pieModelSecondary = new PieChart().setData(pieData).toJson();
    }

    private void createBarModel() {
        
        List<Billdetail> billdetails = billdetailFacade.findBySaleID(2);
        if (billdetails == null || billdetails.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "No data found for the given Sale ID"));
            return;
        }

        BarDataset dataset = new BarDataset();
        BarData barData = new BarData();

        List<String> labels = new ArrayList<>();
        List<Number> data = new ArrayList<>();
        List<Color> colors = Arrays.asList(new Color(255, 99, 132), new Color(54, 162, 235), new Color(255, 205, 86));

        for (int i = 0; i < billdetails.size(); i++) {
            Billdetail detail = billdetails.get(i);
            data.add(detail.getTotalPrice());
            labels.add(detail.getProduct().getName());
        }

        dataset.setData(data);
        dataset.setBackgroundColor(colors.subList(0, billdetails.size()));
        dataset.setBorderColor(new Color(0, 0, 0));

        barData.setLabels(labels);
        barData.addDataset(dataset);
        barModel = new BarChart().setData(barData).toJson();
    }

    private void createSecondaryBarModel() {
        
        List<Billdetail> billdetails = billdetailFacade.findBySaleID(3); 
        if (billdetails == null || billdetails.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "No data found for the given Sale ID"));
            return;
        }

        BarDataset dataset = new BarDataset();
        BarData barData = new BarData();

        List<String> labels = new ArrayList<>();
        List<Number> data = new ArrayList<>();
        List<Color> colors = Arrays.asList(new Color(255, 99, 132), new Color(54, 162, 235), new Color(255, 205, 86));

        for (int i = 0; i < billdetails.size(); i++) {
            Billdetail detail = billdetails.get(i);
            data.add(detail.getTotalPrice());
            labels.add(detail.getProduct().getName());
        }

        dataset.setData(data);
        dataset.setBackgroundColor(colors.subList(0, billdetails.size()));
        dataset.setBorderColor(new Color(0, 0, 0));

        barData.setLabels(labels);
        barData.addDataset(dataset);
        barModelSecondary = new BarChart().setData(barData).toJson();
    }

    private void createLineModel() {
        List<Billdetail> billdetails = billdetailFacade.findBySaleID(2);

        if (billdetails == null || billdetails.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "No data found for the given Sale ID"));
            return;
        }

        LineDataset dataset = new LineDataset();
        LineData lineData = new LineData();

        for (Billdetail detail : billdetails) {
            dataset.addData(detail.getTotalPrice());
            lineData.addLabel(detail.getProduct().getName());
        }

        dataset.setBackgroundColor(new Color(75, 192, 192));
        dataset.setBorderColor(new Color(0, 0, 0));

        lineData.addDataset(dataset);
        lineModel = new LineChart().setData(lineData).toJson();
    }

    private void createSecondaryLineModel() {
        List<Billdetail> billdetails = billdetailFacade.findBySaleID(3); // Cambiar ID si es necesario

        if (billdetails == null || billdetails.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "No data found for the given Sale ID"));
            return;
        }

        LineDataset dataset = new LineDataset();
        LineData lineData = new LineData();

        for (Billdetail detail : billdetails) {
            dataset.addData(detail.getTotalPrice());
            lineData.addLabel(detail.getProduct().getName());
        }

        dataset.setBackgroundColor(new Color(75, 192, 192));
        dataset.setBorderColor(new Color(0, 0, 0));

        lineData.addDataset(dataset);
        lineModelSecondary = new LineChart().setData(lineData).toJson();
    }

    public String getSelectedChartType() {
        return selectedChartType;
    }

    public void setSelectedChartType(String selectedChartType) {
        this.selectedChartType = selectedChartType;
    }

    public String getPieModel() {
        return pieModel;
    }

    public void setPieModel(String pieModel) {
        this.pieModel = pieModel;
    }

    public String getBarModel() {
        return barModel;
    }

    public void setBarModel(String barModel) {
        this.barModel = barModel;
    }

    public String getLineModel() {
        return lineModel;
    }

    public void setLineModel(String lineModel) {
        this.lineModel = lineModel;
    }

    public String getPieModelSecondary() {
        return pieModelSecondary;
    }

    public void setPieModelSecondary(String pieModelSecondary) {
        this.pieModelSecondary = pieModelSecondary;
    }

    public String getBarModelSecondary() {
        return barModelSecondary;
    }

    public void setBarModelSecondary(String barModelSecondary) {
        this.barModelSecondary = barModelSecondary;
    }

    public String getLineModelSecondary() {
        return lineModelSecondary;
    }

    public void setLineModelSecondary(String lineModelSecondary) {
        this.lineModelSecondary = lineModelSecondary;
    }

    public String getChartModel() {
        return chartModel;
    }

    public void setChartModel(String chartModel) {
        this.chartModel = chartModel;
    }

    public List<String> getChartTypes() {
        return chartTypes;
    }

    public void setChartTypes(List<String> chartTypes) {
        this.chartTypes = chartTypes;
    }

    public void onChartTypeChange() {
        switch (selectedChartType) {
            case "Pie":
                createPieModel();
                createSecondaryPieModel();
                break;
            case "Bar":
                createBarModel();
                createSecondaryBarModel();
                break;
            case "Line":
                createLineModel();
                createSecondaryLineModel();
                break;
            default:
                break;
        }
    }
}
