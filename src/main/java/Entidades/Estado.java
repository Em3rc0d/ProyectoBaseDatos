package Entidades;

public class Estado {
    int idEstado;
    String reporteEstado;

    public Estado(int idEstado, String reporteEstado) {
        this.idEstado = idEstado;
        this.reporteEstado = reporteEstado;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    public String getReporteEstado() {
        return reporteEstado;
    }

    public void setReporteEstado(String reporteEstado) {
        this.reporteEstado = reporteEstado;
    }
}
