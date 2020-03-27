/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Saul
 */
@Entity
@Table(name = "contabilidad")
@NamedQueries({
    @NamedQuery(name = "Contabilidad.findAll", query = "SELECT c FROM Contabilidad c"),
    @NamedQuery(name = "Contabilidad.findByIdContabilidad", query = "SELECT c FROM Contabilidad c WHERE c.idContabilidad = :idContabilidad"),
    @NamedQuery(name = "Contabilidad.findByMontoTotal", query = "SELECT c FROM Contabilidad c WHERE c.montoTotal = :montoTotal"),
    @NamedQuery(name = "Contabilidad.findByFechaPago", query = "SELECT c FROM Contabilidad c WHERE c.fechaPago = :fechaPago")})
public class Contabilidad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_contabilidad")
    private Integer idContabilidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "monto_total")
    private BigInteger montoTotal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_pago")
    @Temporal(TemporalType.DATE)
    private Date fechaPago;
    @JoinColumn(name = "id_venta", referencedColumnName = "id_ventas")
    @ManyToOne(optional = false)
    private Ventas idVenta;

    public Contabilidad() {
    }

    public Contabilidad(Integer idContabilidad) {
        this.idContabilidad = idContabilidad;
    }

    public Contabilidad(Integer idContabilidad, BigInteger montoTotal, Date fechaPago) {
        this.idContabilidad = idContabilidad;
        this.montoTotal = montoTotal;
        this.fechaPago = fechaPago;
    }

    public Integer getIdContabilidad() {
        return idContabilidad;
    }

    public void setIdContabilidad(Integer idContabilidad) {
        this.idContabilidad = idContabilidad;
    }

    public BigInteger getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigInteger montoTotal) {
        this.montoTotal = montoTotal;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public Ventas getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Ventas idVenta) {
        this.idVenta = idVenta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idContabilidad != null ? idContabilidad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Contabilidad)) {
            return false;
        }
        Contabilidad other = (Contabilidad) object;
        if ((this.idContabilidad == null && other.idContabilidad != null) || (this.idContabilidad != null && !this.idContabilidad.equals(other.idContabilidad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Contabilidad[ idContabilidad=" + idContabilidad + " ]";
    }

}
