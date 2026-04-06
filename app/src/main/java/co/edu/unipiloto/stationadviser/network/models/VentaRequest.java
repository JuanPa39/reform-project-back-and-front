package co.edu.unipiloto.stationadviser.network.models;

public class VentaRequest {
    private String tipoCombustible;
    private double cantidad;
    private double precioUnitario;
    private double montoTotal;

    public VentaRequest(String tipoCombustible, double cantidad, double precioUnitario, double montoTotal) {
        this.tipoCombustible = tipoCombustible;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.montoTotal = montoTotal;
    }

    public String getTipoCombustible() { return tipoCombustible; }
    public void setTipoCombustible(String tipoCombustible) { this.tipoCombustible = tipoCombustible; }
    public double getCantidad() { return cantidad; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }
    public double getMontoTotal() { return montoTotal; }
    public void setMontoTotal(double montoTotal) { this.montoTotal = montoTotal; }
}