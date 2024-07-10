package Entidades;

public class Caja {
    int idCaja;
    int Area_idArea;
    double monto;
    double topeMovimiento;
    
    public Caja(int idCaja, int Area_idArea, double monto, double topeMovimiento) {
        this.idCaja = idCaja;
        this.Area_idArea = Area_idArea;
        this.monto = monto;
        this.topeMovimiento = topeMovimiento;
    }

    public Caja(int Area_idArea, double monto, double topeMovimiento) {
        this.Area_idArea = Area_idArea;
        this.monto = monto;
        this.topeMovimiento = topeMovimiento;
    }

    public int getIdCaja() {
        return idCaja;
    }

    public void setIdCaja(int idCaja) {
        this.idCaja = idCaja;
    }

    public int getArea_idArea() {
        return Area_idArea;
    }

    public void setArea_idArea(int Area_idArea) {
        this.Area_idArea = Area_idArea;
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
