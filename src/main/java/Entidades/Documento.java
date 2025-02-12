package Entidades;

public class Documento {
    int idDocumento;
    int idCaja;
    int idCajero;
    String idEmpresa;
    int idMotivo;
    String tipoDocumento;
    String descripcion;
    float monto;
    
    public Documento(int idCaja, int idCajero, String idEmpresa, int idMotivo, String descripcion,
            float monto) {
        this.idCaja = idCaja;
        this.idCajero = idCajero;
        this.idEmpresa = idEmpresa;
        this.idMotivo = idMotivo;
        this.descripcion = descripcion;
        this.monto = monto;
    }
    public Documento(int idCaja, int idCajero, String idEmpresa, int idMotivo,
            float monto) {
        this.idCaja = idCaja;
        this.idCajero = idCajero;
        this.idEmpresa = idEmpresa;
        this.idMotivo = idMotivo;
        this.monto = monto;
    }
    
    public Documento(){
    }

    public int getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }

    public int getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(int idCaja) {
        this.idCaja = idCaja;
    }

    public int getIdCajero() {
        return idCajero;
    }

    public void setIdCajero(int idCajero) {
        this.idCajero = idCajero;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public int getIdMotivo() {
        return idMotivo;
    }

    public void setIdMotivo(int idMotivo) {
        this.idMotivo = idMotivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    

}
