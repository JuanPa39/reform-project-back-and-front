package co.edu.unipiloto.stationadviser.network.models;

public class DisponibilidadResponse {
    private double cantidadDisponible;
    private boolean aplicaSubsidio;

    public double getCantidadDisponible() { return cantidadDisponible; }
    public void setCantidadDisponible(double cantidadDisponible) { this.cantidadDisponible = cantidadDisponible; }
    public boolean isAplicaSubsidio() { return aplicaSubsidio; }
    public void setAplicaSubsidio(boolean aplicaSubsidio) { this.aplicaSubsidio = aplicaSubsidio; }
}