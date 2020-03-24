/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author Saul
 */
@Entity
@Table(name = "facturacion")
@NamedQueries({
    @NamedQuery(name = "Facturacion.findAll", query = "SELECT f FROM Facturacion f"),
    @NamedQuery(name = "Facturacion.findByIdFacturacion", query = "SELECT f FROM Facturacion f WHERE f.idFacturacion = :idFacturacion"),
    @NamedQuery(name = "Facturacion.findByNombre", query = "SELECT f FROM Facturacion f WHERE f.nombre = :nombre"),
    @NamedQuery(name = "Facturacion.findByDescripcion", query = "SELECT f FROM Facturacion f WHERE f.descripcion = :descripcion"),
    @NamedQuery(name = "Facturacion.findByFecha", query = "SELECT f FROM Facturacion f WHERE f.fecha = :fecha"),
    @NamedQuery(name = "Facturacion.findByRfc", query = "SELECT f FROM Facturacion f WHERE f.rfc = :rfc")})
public class Facturacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_facturacion")
    private Integer idFacturacion;
    @Size(max = 45)
    @Column(name = "nombre")
    private String nombre;
    @Size(max = 45)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Size(max = 45)
    @Column(name = "rfc")
    private String rfc;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idFactura")
    private Collection<Compras> comprasCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idFactura")
    private Collection<Ventas> ventasCollection;

    public Facturacion() {
    }

    public Facturacion(Integer idFacturacion) {
        this.idFacturacion = idFacturacion;
    }

    public Integer getIdFacturacion() {
        return idFacturacion;
    }

    public void setIdFacturacion(Integer idFacturacion) {
        this.idFacturacion = idFacturacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public Collection<Compras> getComprasCollection() {
        return comprasCollection;
    }

    public void setComprasCollection(Collection<Compras> comprasCollection) {
        this.comprasCollection = comprasCollection;
    }

    public Collection<Ventas> getVentasCollection() {
        return ventasCollection;
    }

    public void setVentasCollection(Collection<Ventas> ventasCollection) {
        this.ventasCollection = ventasCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFacturacion != null ? idFacturacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Facturacion)) {
            return false;
        }
        Facturacion other = (Facturacion) object;
        if ((this.idFacturacion == null && other.idFacturacion != null) || (this.idFacturacion != null && !this.idFacturacion.equals(other.idFacturacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Facturacion[ idFacturacion=" + idFacturacion + " ]";
    }
    
}
