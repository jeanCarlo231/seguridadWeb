/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Cliente;
import entidades.Facturacion;
import entidades.Pago;
import entidades.Producto;
import entidades.Contabilidad;
import entidades.Ventas;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Saul
 */
public class VentasJpaController implements Serializable {

    public VentasJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ventas ventas) throws RollbackFailureException, Exception {
        if (ventas.getContabilidadCollection() == null) {
            ventas.setContabilidadCollection(new ArrayList<Contabilidad>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cliente idCliente = ventas.getIdCliente();
            if (idCliente != null) {
                idCliente = em.getReference(idCliente.getClass(), idCliente.getIdCliente());
                ventas.setIdCliente(idCliente);
            }
            Facturacion idFactura = ventas.getIdFactura();
            if (idFactura != null) {
                idFactura = em.getReference(idFactura.getClass(), idFactura.getIdFacturacion());
                ventas.setIdFactura(idFactura);
            }
            Pago idPago = ventas.getIdPago();
            if (idPago != null) {
                idPago = em.getReference(idPago.getClass(), idPago.getIdPago());
                ventas.setIdPago(idPago);
            }
            Producto idProducto = ventas.getIdProducto();
            if (idProducto != null) {
                idProducto = em.getReference(idProducto.getClass(), idProducto.getIdProducto());
                ventas.setIdProducto(idProducto);
            }
            Collection<Contabilidad> attachedContabilidadCollection = new ArrayList<Contabilidad>();
            for (Contabilidad contabilidadCollectionContabilidadToAttach : ventas.getContabilidadCollection()) {
                contabilidadCollectionContabilidadToAttach = em.getReference(contabilidadCollectionContabilidadToAttach.getClass(), contabilidadCollectionContabilidadToAttach.getIdContabilidad());
                attachedContabilidadCollection.add(contabilidadCollectionContabilidadToAttach);
            }
            ventas.setContabilidadCollection(attachedContabilidadCollection);
            em.persist(ventas);
            if (idCliente != null) {
                idCliente.getVentasCollection().add(ventas);
                idCliente = em.merge(idCliente);
            }
            if (idFactura != null) {
                idFactura.getVentasCollection().add(ventas);
                idFactura = em.merge(idFactura);
            }
            if (idPago != null) {
                idPago.getVentasCollection().add(ventas);
                idPago = em.merge(idPago);
            }
            if (idProducto != null) {
                idProducto.getVentasCollection().add(ventas);
                idProducto = em.merge(idProducto);
            }
            for (Contabilidad contabilidadCollectionContabilidad : ventas.getContabilidadCollection()) {
                Ventas oldIdVentaOfContabilidadCollectionContabilidad = contabilidadCollectionContabilidad.getIdVenta();
                contabilidadCollectionContabilidad.setIdVenta(ventas);
                contabilidadCollectionContabilidad = em.merge(contabilidadCollectionContabilidad);
                if (oldIdVentaOfContabilidadCollectionContabilidad != null) {
                    oldIdVentaOfContabilidadCollectionContabilidad.getContabilidadCollection().remove(contabilidadCollectionContabilidad);
                    oldIdVentaOfContabilidadCollectionContabilidad = em.merge(oldIdVentaOfContabilidadCollectionContabilidad);
                }
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

    public void edit(Ventas ventas) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Ventas persistentVentas = em.find(Ventas.class, ventas.getIdVentas());
            Cliente idClienteOld = persistentVentas.getIdCliente();
            Cliente idClienteNew = ventas.getIdCliente();
            Facturacion idFacturaOld = persistentVentas.getIdFactura();
            Facturacion idFacturaNew = ventas.getIdFactura();
            Pago idPagoOld = persistentVentas.getIdPago();
            Pago idPagoNew = ventas.getIdPago();
            Producto idProductoOld = persistentVentas.getIdProducto();
            Producto idProductoNew = ventas.getIdProducto();
            Collection<Contabilidad> contabilidadCollectionOld = persistentVentas.getContabilidadCollection();
            Collection<Contabilidad> contabilidadCollectionNew = ventas.getContabilidadCollection();
            List<String> illegalOrphanMessages = null;
            for (Contabilidad contabilidadCollectionOldContabilidad : contabilidadCollectionOld) {
                if (!contabilidadCollectionNew.contains(contabilidadCollectionOldContabilidad)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Contabilidad " + contabilidadCollectionOldContabilidad + " since its idVenta field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idClienteNew != null) {
                idClienteNew = em.getReference(idClienteNew.getClass(), idClienteNew.getIdCliente());
                ventas.setIdCliente(idClienteNew);
            }
            if (idFacturaNew != null) {
                idFacturaNew = em.getReference(idFacturaNew.getClass(), idFacturaNew.getIdFacturacion());
                ventas.setIdFactura(idFacturaNew);
            }
            if (idPagoNew != null) {
                idPagoNew = em.getReference(idPagoNew.getClass(), idPagoNew.getIdPago());
                ventas.setIdPago(idPagoNew);
            }
            if (idProductoNew != null) {
                idProductoNew = em.getReference(idProductoNew.getClass(), idProductoNew.getIdProducto());
                ventas.setIdProducto(idProductoNew);
            }
            Collection<Contabilidad> attachedContabilidadCollectionNew = new ArrayList<Contabilidad>();
            for (Contabilidad contabilidadCollectionNewContabilidadToAttach : contabilidadCollectionNew) {
                contabilidadCollectionNewContabilidadToAttach = em.getReference(contabilidadCollectionNewContabilidadToAttach.getClass(), contabilidadCollectionNewContabilidadToAttach.getIdContabilidad());
                attachedContabilidadCollectionNew.add(contabilidadCollectionNewContabilidadToAttach);
            }
            contabilidadCollectionNew = attachedContabilidadCollectionNew;
            ventas.setContabilidadCollection(contabilidadCollectionNew);
            ventas = em.merge(ventas);
            if (idClienteOld != null && !idClienteOld.equals(idClienteNew)) {
                idClienteOld.getVentasCollection().remove(ventas);
                idClienteOld = em.merge(idClienteOld);
            }
            if (idClienteNew != null && !idClienteNew.equals(idClienteOld)) {
                idClienteNew.getVentasCollection().add(ventas);
                idClienteNew = em.merge(idClienteNew);
            }
            if (idFacturaOld != null && !idFacturaOld.equals(idFacturaNew)) {
                idFacturaOld.getVentasCollection().remove(ventas);
                idFacturaOld = em.merge(idFacturaOld);
            }
            if (idFacturaNew != null && !idFacturaNew.equals(idFacturaOld)) {
                idFacturaNew.getVentasCollection().add(ventas);
                idFacturaNew = em.merge(idFacturaNew);
            }
            if (idPagoOld != null && !idPagoOld.equals(idPagoNew)) {
                idPagoOld.getVentasCollection().remove(ventas);
                idPagoOld = em.merge(idPagoOld);
            }
            if (idPagoNew != null && !idPagoNew.equals(idPagoOld)) {
                idPagoNew.getVentasCollection().add(ventas);
                idPagoNew = em.merge(idPagoNew);
            }
            if (idProductoOld != null && !idProductoOld.equals(idProductoNew)) {
                idProductoOld.getVentasCollection().remove(ventas);
                idProductoOld = em.merge(idProductoOld);
            }
            if (idProductoNew != null && !idProductoNew.equals(idProductoOld)) {
                idProductoNew.getVentasCollection().add(ventas);
                idProductoNew = em.merge(idProductoNew);
            }
            for (Contabilidad contabilidadCollectionNewContabilidad : contabilidadCollectionNew) {
                if (!contabilidadCollectionOld.contains(contabilidadCollectionNewContabilidad)) {
                    Ventas oldIdVentaOfContabilidadCollectionNewContabilidad = contabilidadCollectionNewContabilidad.getIdVenta();
                    contabilidadCollectionNewContabilidad.setIdVenta(ventas);
                    contabilidadCollectionNewContabilidad = em.merge(contabilidadCollectionNewContabilidad);
                    if (oldIdVentaOfContabilidadCollectionNewContabilidad != null && !oldIdVentaOfContabilidadCollectionNewContabilidad.equals(ventas)) {
                        oldIdVentaOfContabilidadCollectionNewContabilidad.getContabilidadCollection().remove(contabilidadCollectionNewContabilidad);
                        oldIdVentaOfContabilidadCollectionNewContabilidad = em.merge(oldIdVentaOfContabilidadCollectionNewContabilidad);
                    }
                }
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
                Integer id = ventas.getIdVentas();
                if (findVentas(id) == null) {
                    throw new NonexistentEntityException("The ventas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Ventas ventas;
            try {
                ventas = em.getReference(Ventas.class, id);
                ventas.getIdVentas();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ventas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Contabilidad> contabilidadCollectionOrphanCheck = ventas.getContabilidadCollection();
            for (Contabilidad contabilidadCollectionOrphanCheckContabilidad : contabilidadCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ventas (" + ventas + ") cannot be destroyed since the Contabilidad " + contabilidadCollectionOrphanCheckContabilidad + " in its contabilidadCollection field has a non-nullable idVenta field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Cliente idCliente = ventas.getIdCliente();
            if (idCliente != null) {
                idCliente.getVentasCollection().remove(ventas);
                idCliente = em.merge(idCliente);
            }
            Facturacion idFactura = ventas.getIdFactura();
            if (idFactura != null) {
                idFactura.getVentasCollection().remove(ventas);
                idFactura = em.merge(idFactura);
            }
            Pago idPago = ventas.getIdPago();
            if (idPago != null) {
                idPago.getVentasCollection().remove(ventas);
                idPago = em.merge(idPago);
            }
            Producto idProducto = ventas.getIdProducto();
            if (idProducto != null) {
                idProducto.getVentasCollection().remove(ventas);
                idProducto = em.merge(idProducto);
            }
            em.remove(ventas);
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

    public List<Ventas> findVentasEntities() {
        return findVentasEntities(true, -1, -1);
    }

    public List<Ventas> findVentasEntities(int maxResults, int firstResult) {
        return findVentasEntities(false, maxResults, firstResult);
    }

    private List<Ventas> findVentasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ventas.class));
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

    public Ventas findVentas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ventas.class, id);
        } finally {
            em.close();
        }
    }

    public int getVentasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ventas> rt = cq.from(Ventas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
