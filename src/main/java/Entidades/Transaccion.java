package Entidades;

import java.sql.Date;

public class Transaccion {

    private int idTransaccion;  
    private int Documento_idDocumento;
    private int Estado_idEstado;
    private Date fechaTransaccion;
    private String tipoTransaccion;
    private String descripcion;

    public Transaccion(int Documento_idDocumento, int Estado_idEstado, Date fechaTransaccion, String tipoTransaccion, String descripcion) {
        this.Documento_idDocumento = Documento_idDocumento;
        this.Estado_idEstado = Estado_idEstado;
        this.fechaTransaccion = fechaTransaccion;
        this.tipoTransaccion = tipoTransaccion;
        this.descripcion = descripcion;
    }

    public Transaccion(int idTransaccion){
        this.idTransaccion = idTransaccion;
    }
    public int getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(int idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public int getDocumento_idDocumento() {
        return Documento_idDocumento;
    }

    public void setDocumento_idDocumento(int documento_idDocumento) {
        Documento_idDocumento = documento_idDocumento;
    }

    public int getEstado_idEstado() {
        return Estado_idEstado;
    }

    public void setEstado_idEstado(int estado_idEstado) {
        Estado_idEstado = estado_idEstado;
    }

    public Date getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setFechaTransaccion(Date fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public String getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(String tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    @Override
    public String toString() {
        return "Tipo de transacción: " + this.tipoTransaccion;
    }
}
// CREATE TABLE Transaccion (
//     idTransaccion INT NOT NULL PRIMARY KEY,
//     Documento_idDocumento INT NOT NULL,
//     Estado_idEstado INT NOT NULL,
//     fechaTransaccion DATE NOT NULL,
//     tipoTransaccion VARCHAR(45) NOT NULL,
//     descripcion VARCHAR(255),
//     CONSTRAINT FK_Transaccion_Documento FOREIGN KEY (Documento_idDocumento) REFERENCES Documento(idDocumento),
//     CONSTRAINT FK_Transaccion_Estado FOREIGN KEY (Estado_idEstado) REFERENCES Estado(idEstado)
// );
