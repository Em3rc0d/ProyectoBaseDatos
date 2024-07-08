package Entidades;

public class EmpresaReceptora {

    private int idEmpresaReceptora;
    private String RUC;
    private String nombre;
    private String tipo;

    public EmpresaReceptora(String RUC, String nombre, String tipo) {
        this.RUC = RUC;
        this.nombre = nombre;   
        this.tipo = tipo;
    }

    public int getIdEmpresaReceptora() {
        return idEmpresaReceptora;
    }

    public void setIdEmpresaReceptora(int idEmpresaReceptora) {
        this.idEmpresaReceptora = idEmpresaReceptora;
    }

    public String getRUC() {
        return RUC;
    }

    public void setRUC(String rUC) {
        RUC = rUC;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {  
        return "RUC: " + this.RUC + ", nombre: " + this.nombre + ", tipo: " + this.tipo;
    }
}