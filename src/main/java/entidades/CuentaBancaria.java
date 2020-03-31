/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Saul
 */
@Entity
@Table(name = "cuenta_bancaria")
@NamedQueries({
    @NamedQuery(name = "CuentaBancaria.findAll", query = "SELECT c FROM CuentaBancaria c"),
    @NamedQuery(name = "CuentaBancaria.findByIdCuenta", query = "SELECT c FROM CuentaBancaria c WHERE c.idCuenta = :idCuenta"),
    @NamedQuery(name = "CuentaBancaria.findByNombreBanco", query = "SELECT c FROM CuentaBancaria c WHERE c.nombreBanco = :nombreBanco"),
    @NamedQuery(name = "CuentaBancaria.findByNumeroTarjeta", query = "SELECT c FROM CuentaBancaria c WHERE c.numeroTarjeta = :numeroTarjeta"),
    @NamedQuery(name = "CuentaBancaria.findByCvc", query = "SELECT c FROM CuentaBancaria c WHERE c.cvc = :cvc"),
    @NamedQuery(name = "CuentaBancaria.findByFechaExpiracion", query = "SELECT c FROM CuentaBancaria c WHERE c.fechaExpiracion = :fechaExpiracion")})
public class CuentaBancaria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_cuenta")
    private Integer idCuenta;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "nombre_banco")
    private String nombreBanco;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "numero_tarjeta")
    private long numeroTarjeta;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "cvc")
    private int cvc;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_expiracion")
    @Temporal(TemporalType.DATE)
    private Date fechaExpiracion;

    public CuentaBancaria() {
    }

    public CuentaBancaria(Integer idCuenta) {
        this.idCuenta = idCuenta;
    }

    public CuentaBancaria(Integer idCuenta, String nombreBanco, long numeroTarjeta, int cvc, Date fechaExpiracion) {
        this.idCuenta = idCuenta;
        this.nombreBanco = nombreBanco;
        this.numeroTarjeta = numeroTarjeta;
        this.cvc = cvc;
        this.fechaExpiracion = fechaExpiracion;
    }

    public Integer getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(Integer idCuenta) {
        this.idCuenta = idCuenta;
    }

    public String getNombreBanco() {
        return nombreBanco;
    }

    public void setNombreBanco(String nombreBanco) {
        this.nombreBanco = nombreBanco;
    }

    public long getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(long numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public int getCvc() {
        return cvc;
    }

    public void setCvc(int cvc) {
        this.cvc = cvc;
    }

    public Date getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(Date fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCuenta != null ? idCuenta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CuentaBancaria)) {
            return false;
        }
        CuentaBancaria other = (CuentaBancaria) object;
        if ((this.idCuenta == null && other.idCuenta != null) || (this.idCuenta != null && !this.idCuenta.equals(other.idCuenta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.CuentaBancaria[ idCuenta=" + idCuenta + " ]";
    }

}
