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
import javax.validation.constraints.Size;

/**
 *
 * @author Saul
 */
@Entity
@Table(name = "compras")
@NamedQueries({
    @NamedQuery(name = "Compras.findAll", query = "SELECT c FROM Compras c"),
    @NamedQuery(name = "Compras.findByIdCompras", query = "SELECT c FROM Compras c WHERE c.idCompras = :idCompras"),
    @NamedQuery(name = "Compras.findByCantidad", query = "SELECT c FROM Compras c WHERE c.cantidad = :cantidad"),
    @NamedQuery(name = "Compras.findByPrecioCompra", query = "SELECT c FROM Compras c WHERE c.precioCompra = :precioCompra"),
    @NamedQuery(name = "Compras.findByPrecioVenta", query = "SELECT c FROM Compras c WHERE c.precioVenta = :precioVenta"),
    @NamedQuery(name = "Compras.findByStatusCompra", query = "SELECT c FROM Compras c WHERE c.statusCompra = :statusCompra"),
    @NamedQuery(name = "Compras.findByFecha", query = "SELECT c FROM Compras c WHERE c.fecha = :fecha"),
    @NamedQuery(name = "Compras.findByIva", query = "SELECT c FROM Compras c WHERE c.iva = :iva"),
    @NamedQuery(name = "Compras.findBySubtotal", query = "SELECT c FROM Compras c WHERE c.subtotal = :subtotal"),
    @NamedQuery(name = "Compras.findByTotal", query = "SELECT c FROM Compras c WHERE c.total = :total")})
public class Compras implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_compras")
    private Integer idCompras;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cantidad")
    private int cantidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio_compra")
    private BigInteger precioCompra;
    @Basic(optional = false)
    @NotNull
    @Column(name = "precio_venta")
    private BigInteger precioVenta;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "status_compra")
    private String statusCompra;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "iva")
    private BigInteger iva;
    @Basic(optional = false)
    @NotNull
    @Column(name = "subtotal")
    private BigInteger subtotal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "total")
    private BigInteger total;
    @JoinColumn(name = "id_empleado", referencedColumnName = "id_empleado")
    @ManyToOne(optional = false)
    private Empleado idEmpleado;
    @JoinColumn(name = "id_factura", referencedColumnName = "id_facturacion")
    @ManyToOne(optional = false)
    private Facturacion idFactura;
    @JoinColumn(name = "id_pago", referencedColumnName = "id_pago")
    @ManyToOne(optional = false)
    private Pago idPago;
    @JoinColumn(name = "id_producto", referencedColumnName = "id_producto")
    @ManyToOne(optional = false)
    private Producto idProducto;
    @JoinColumn(name = "id_proveedor", referencedColumnName = "id_proveedor")
    @ManyToOne(optional = false)
    private Proveedor idProveedor;

    public Compras() {
    }

    public Compras(Integer idCompras) {
        this.idCompras = idCompras;
    }

    public Compras(Integer idCompras, int cantidad, BigInteger precioCompra, BigInteger precioVenta, String statusCompra, Date fecha, BigInteger iva, BigInteger subtotal, BigInteger total) {
        this.idCompras = idCompras;
        this.cantidad = cantidad;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.statusCompra = statusCompra;
        this.fecha = fecha;
        this.iva = iva;
        this.subtotal = subtotal;
        this.total = total;
    }

    public Integer getIdCompras() {
        return idCompras;
    }

    public void setIdCompras(Integer idCompras) {
        this.idCompras = idCompras;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigInteger getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(BigInteger precioCompra) {
        this.precioCompra = precioCompra;
    }

    public BigInteger getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigInteger precioVenta) {
        this.precioVenta = precioVenta;
    }

    public String getStatusCompra() {
        return statusCompra;
    }

    public void setStatusCompra(String statusCompra) {
        this.statusCompra = statusCompra;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigInteger getIva() {
        return iva;
    }

    public void setIva(BigInteger iva) {
        this.iva = iva;
    }

    public BigInteger getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigInteger subtotal) {
        this.subtotal = subtotal;
    }

    public BigInteger getTotal() {
        return total;
    }

    public void setTotal(BigInteger total) {
        this.total = total;
    }

    public Empleado getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Empleado idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Facturacion getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Facturacion idFactura) {
        this.idFactura = idFactura;
    }

    public Pago getIdPago() {
        return idPago;
    }

    public void setIdPago(Pago idPago) {
        this.idPago = idPago;
    }

    public Producto getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Producto idProducto) {
        this.idProducto = idProducto;
    }

    public Proveedor getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Proveedor idProveedor) {
        this.idProveedor = idProveedor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCompras != null ? idCompras.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Compras)) {
            return false;
        }
        Compras other = (Compras) object;
        if ((this.idCompras == null && other.idCompras != null) || (this.idCompras != null && !this.idCompras.equals(other.idCompras))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidades.Compras[ idCompras=" + idCompras + " ]";
    }

}
