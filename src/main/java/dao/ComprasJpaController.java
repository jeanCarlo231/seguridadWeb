/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import entidades.Compras;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Empleado;
import entidades.Facturacion;
import entidades.Pago;
import entidades.Producto;
import entidades.Proveedor;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Saul
 */
public class ComprasJpaController implements Serializable {

    public ComprasJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Compras compras) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Empleado idEmpleado = compras.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado = em.getReference(idEmpleado.getClass(), idEmpleado.getIdEmpleado());
                compras.setIdEmpleado(idEmpleado);
            }
            Facturacion idFactura = compras.getIdFactura();
            if (idFactura != null) {
                idFactura = em.getReference(idFactura.getClass(), idFactura.getIdFacturacion());
                compras.setIdFactura(idFactura);
            }
            Pago idPago = compras.getIdPago();
            if (idPago != null) {
                idPago = em.getReference(idPago.getClass(), idPago.getIdPago());
                compras.setIdPago(idPago);
            }
            Producto idProducto = compras.getIdProducto();
            if (idProducto != null) {
                idProducto = em.getReference(idProducto.getClass(), idProducto.getIdProducto());
                compras.setIdProducto(idProducto);
            }
            Proveedor idProveedor = compras.getIdProveedor();
            if (idProveedor != null) {
                idProveedor = em.getReference(idProveedor.getClass(), idProveedor.getIdProveedor());
                compras.setIdProveedor(idProveedor);
            }
            em.persist(compras);
            if (idEmpleado != null) {
                idEmpleado.getComprasCollection().add(compras);
                idEmpleado = em.merge(idEmpleado);
            }
            if (idFactura != null) {
                idFactura.getComprasCollection().add(compras);
                idFactura = em.merge(idFactura);
            }
            if (idPago != null) {
                idPago.getComprasCollection().add(compras);
                idPago = em.merge(idPago);
            }
            if (idProducto != null) {
                idProducto.getComprasCollection().add(compras);
                idProducto = em.merge(idProducto);
            }
            if (idProveedor != null) {
                idProveedor.getComprasCollection().add(compras);
                idProveedor = em.merge(idProveedor);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Compras compras) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Compras persistentCompras = em.find(Compras.class, compras.getIdCompras());
            Empleado idEmpleadoOld = persistentCompras.getIdEmpleado();
            Empleado idEmpleadoNew = compras.getIdEmpleado();
            Facturacion idFacturaOld = persistentCompras.getIdFactura();
            Facturacion idFacturaNew = compras.getIdFactura();
            Pago idPagoOld = persistentCompras.getIdPago();
            Pago idPagoNew = compras.getIdPago();
            Producto idProductoOld = persistentCompras.getIdProducto();
            Producto idProductoNew = compras.getIdProducto();
            Proveedor idProveedorOld = persistentCompras.getIdProveedor();
            Proveedor idProveedorNew = compras.getIdProveedor();
            if (idEmpleadoNew != null) {
                idEmpleadoNew = em.getReference(idEmpleadoNew.getClass(), idEmpleadoNew.getIdEmpleado());
                compras.setIdEmpleado(idEmpleadoNew);
            }
            if (idFacturaNew != null) {
                idFacturaNew = em.getReference(idFacturaNew.getClass(), idFacturaNew.getIdFacturacion());
                compras.setIdFactura(idFacturaNew);
            }
            if (idPagoNew != null) {
                idPagoNew = em.getReference(idPagoNew.getClass(), idPagoNew.getIdPago());
                compras.setIdPago(idPagoNew);
            }
            if (idProductoNew != null) {
                idProductoNew = em.getReference(idProductoNew.getClass(), idProductoNew.getIdProducto());
                compras.setIdProducto(idProductoNew);
            }
            if (idProveedorNew != null) {
                idProveedorNew = em.getReference(idProveedorNew.getClass(), idProveedorNew.getIdProveedor());
                compras.setIdProveedor(idProveedorNew);
            }
            compras = em.merge(compras);
            if (idEmpleadoOld != null && !idEmpleadoOld.equals(idEmpleadoNew)) {
                idEmpleadoOld.getComprasCollection().remove(compras);
                idEmpleadoOld = em.merge(idEmpleadoOld);
            }
            if (idEmpleadoNew != null && !idEmpleadoNew.equals(idEmpleadoOld)) {
                idEmpleadoNew.getComprasCollection().add(compras);
                idEmpleadoNew = em.merge(idEmpleadoNew);
            }
            if (idFacturaOld != null && !idFacturaOld.equals(idFacturaNew)) {
                idFacturaOld.getComprasCollection().remove(compras);
                idFacturaOld = em.merge(idFacturaOld);
            }
            if (idFacturaNew != null && !idFacturaNew.equals(idFacturaOld)) {
                idFacturaNew.getComprasCollection().add(compras);
                idFacturaNew = em.merge(idFacturaNew);
            }
            if (idPagoOld != null && !idPagoOld.equals(idPagoNew)) {
                idPagoOld.getComprasCollection().remove(compras);
                idPagoOld = em.merge(idPagoOld);
            }
            if (idPagoNew != null && !idPagoNew.equals(idPagoOld)) {
                idPagoNew.getComprasCollection().add(compras);
                idPagoNew = em.merge(idPagoNew);
            }
            if (idProductoOld != null && !idProductoOld.equals(idProductoNew)) {
                idProductoOld.getComprasCollection().remove(compras);
                idProductoOld = em.merge(idProductoOld);
            }
            if (idProductoNew != null && !idProductoNew.equals(idProductoOld)) {
                idProductoNew.getComprasCollection().add(compras);
                idProductoNew = em.merge(idProductoNew);
            }
            if (idProveedorOld != null && !idProveedorOld.equals(idProveedorNew)) {
                idProveedorOld.getComprasCollection().remove(compras);
                idProveedorOld = em.merge(idProveedorOld);
            }
            if (idProveedorNew != null && !idProveedorNew.equals(idProveedorOld)) {
                idProveedorNew.getComprasCollection().add(compras);
                idProveedorNew = em.merge(idProveedorNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = compras.getIdCompras();
                if (findCompras(id) == null) {
                    throw new NonexistentEntityException("The compras with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Compras compras;
            try {
                compras = em.getReference(Compras.class, id);
                compras.getIdCompras();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The compras with id " + id + " no longer exists.", enfe);
            }
            Empleado idEmpleado = compras.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado.getComprasCollection().remove(compras);
                idEmpleado = em.merge(idEmpleado);
            }
            Facturacion idFactura = compras.getIdFactura();
            if (idFactura != null) {
                idFactura.getComprasCollection().remove(compras);
                idFactura = em.merge(idFactura);
            }
            Pago idPago = compras.getIdPago();
            if (idPago != null) {
                idPago.getComprasCollection().remove(compras);
                idPago = em.merge(idPago);
            }
            Producto idProducto = compras.getIdProducto();
            if (idProducto != null) {
                idProducto.getComprasCollection().remove(compras);
                idProducto = em.merge(idProducto);
            }
            Proveedor idProveedor = compras.getIdProveedor();
            if (idProveedor != null) {
                idProveedor.getComprasCollection().remove(compras);
                idProveedor = em.merge(idProveedor);
            }
            em.remove(compras);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Compras> findComprasEntities() {
        return findComprasEntities(true, -1, -1);
    }

    public List<Compras> findComprasEntities(int maxResults, int firstResult) {
        return findComprasEntities(false, maxResults, firstResult);
    }

    private List<Compras> findComprasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Compras.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Compras findCompras(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Compras.class, id);
        } finally {
            em.close();
        }
    }

    public int getComprasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Compras> rt = cq.from(Compras.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
