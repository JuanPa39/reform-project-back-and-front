package co.edu.unipiloto.stationadviser.network.models;

public class NotificacionRequest {
    private Long estacionId;
    private String mensaje;

    public NotificacionRequest(Long estacionId, String mensaje) {
        this.estacionId = estacionId;
        this.mensaje = mensaje;
    }

    public Long getEstacionId() { return estacionId; }
    public void setEstacionId(Long estacionId) { this.estacionId = estacionId; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}