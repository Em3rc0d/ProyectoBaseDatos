package Entidades;

public class Caja {
    int idCaja;
    int idArea;
    double monto;
    double topeMovimiento;
    
    public Caja(int idCaja, int idArea, double monto, double topeMovimiento) {
        this.idCaja = idCaja;
        this.idArea = idArea;
        this.monto = monto;
        this.topeMovimiento = topeMovimiento;
    }
    public Caja(int idArea, double monto, double topeMovimiento) {
        this.idArea = idArea;
        this.monto = monto;
        this.topeMovimiento = topeMovimiento;
    }

    public int getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(int idCaja) {
        this.idCaja = idCaja;
    }

    public int getIdArea() {
        return idArea;
    }

    public void setIdArea(int idArea) {
        this.idArea = idArea;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    public double getTopeMovimiento() {
        return topeMovimiento;
    }

    public void setTopeMovimiento(float topeMovimiento) {
        this.topeMovimiento = topeMovimiento;
    }

}
