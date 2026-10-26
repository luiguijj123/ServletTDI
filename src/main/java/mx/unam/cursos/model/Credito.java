/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mx.unam.cursos.model;

/**
 *
 * @author luiguicruzsandiga
 */
public class Credito {
    private String fecha;
    private String moneda;
    private String monto;
    private String vencimiento; 
    private String cuota;
    private String tea;
    private String periodo; 
    
    public Credito(){
    }
    
    public String getFecha() {
        return fecha;
    }

    @Override
    public String toString() {
        return "Credito{" + "fecha=" + fecha + ", moneda=" + moneda + ", monto=" + monto + ", vencimiento=" + vencimiento + ", cuota=" + cuota + ", tea=" + tea + ", periodo=" + periodo + '}';
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(String vencimiento) {
        this.vencimiento = vencimiento;
    }

    public String getCuota() {
        return cuota;
    }

    public void setCuota(String cuota) {
        this.cuota = cuota;
    }

    public String getTea() {
        return tea;
    }

    public void setTea(String tea) {
        this.tea = tea;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }
       
}
