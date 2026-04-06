package co.edu.unipiloto.stationadviser.network.models;

public class NotificacionResponse {
    private Long id;
    private String estacionNombre;
    private String mensaje;
    private String estado;
    private String fecha;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEstacionNombre() { return estacionNombre; }
    public void setEstacionNombre(String estacionNombre) { this.estacionNombre = estacionNombre; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}