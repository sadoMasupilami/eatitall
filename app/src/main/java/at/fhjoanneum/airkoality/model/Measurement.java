package at.fhjoanneum.airkoality.model;

public class Measurement {
    private String parameter;
    private double value;
    private String unit;

    public Measurement(String parameter, double value, String unit) {
        this.parameter = parameter;
        this.value = value;
        this.unit = unit;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
