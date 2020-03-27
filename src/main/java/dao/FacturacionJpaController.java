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
import entidades.Compras;
import entidades.Facturacion;
import java.util.ArrayList;
import java.util.Collection;
import entidades.Ventas;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Saul
 */
public class FacturacionJpaController implements Serializable {

    public FacturacionJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Facturacion facturacion) throws RollbackFailureException, Exception {
        if (facturacion.getComprasCollection() == null) {
            facturacion.setComprasCollection(new ArrayList<Compras>());
        }
        if (facturacion.getVentasCollection() == null) {
            facturacion.setVentasCollection(new ArrayList<Ventas>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Compras> attachedComprasCollection = new ArrayList<Compras>();
            for (Compras comprasCollectionComprasToAttach : facturacion.getComprasCollection()) {
                comprasCollectionComprasToAttach = em.getReference(comprasCollectionComprasToAttach.getClass(), comprasCollectionComprasToAttach.getIdCompras());
                attachedComprasCollection.add(comprasCollectionComprasToAttach);
            }
            facturacion.setComprasCollection(attachedComprasCollection);
            Collection<Ventas> attachedVentasCollection = new ArrayList<Ventas>();
            for (Ventas ventasCollectionVentasToAttach : facturacion.getVentasCollection()) {
                ventasCollectionVentasToAttach = em.getReference(ventasCollectionVentasToAttach.getClass(), ventasCollectionVentasToAttach.getIdVentas());
                attachedVentasCollection.add(ventasCollectionVentasToAttach);
            }
            facturacion.setVentasCollection(attachedVentasCollection);
            em.persist(facturacion);
            for (Compras comprasCollectionCompras : facturacion.getComprasCollection()) {
                Facturacion oldIdFacturaOfComprasCollectionCompras = comprasCollectionCompras.getIdFactura();
                comprasCollectionCompras.setIdFactura(facturacion);
                comprasCollectionCompras = em.merge(comprasCollectionCompras);
                if (oldIdFacturaOfComprasCollectionCompras != null) {
                    oldIdFacturaOfComprasCollectionCompras.getComprasCollection().remove(comprasCollectionCompras);
                    oldIdFacturaOfComprasCollectionCompras = em.merge(oldIdFacturaOfComprasCollectionCompras);
                }
            }
            for (Ventas ventasCollectionVentas : facturacion.getVentasCollection()) {
                Facturacion oldIdFacturaOfVentasCollectionVentas = ventasCollectionVentas.getIdFactura();
                ventasCollectionVentas.setIdFactura(facturacion);
                ventasCollectionVentas = em.merge(ventasCollectionVentas);
                if (oldIdFacturaOfVentasCollectionVentas != null) {
                    oldIdFacturaOfVentasCollectionVentas.getVentasCollection().remove(ventasCollectionVentas);
                    oldIdFacturaOfVentasCollectionVentas = em.merge(oldIdFacturaOfVentasCollectionVentas);
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

    public void edit(Facturacion facturacion) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Facturacion persistentFacturacion = em.find(Facturacion.class, facturacion.getIdFacturacion());
            Collection<Compras> comprasCollectionOld = persistentFacturacion.getComprasCollection();
            Collection<Compras> comprasCollectionNew = facturacion.getComprasCollection();
            Collection<Ventas> ventasCollectionOld = persistentFacturacion.getVentasCollection();
            Collection<Ventas> ventasCollectionNew = facturacion.getVentasCollection();
            List<String> illegalOrphanMessages = null;
            for (Compras comprasCollectionOldCompras : comprasCollectionOld) {
                if (!comprasCollectionNew.contains(comprasCollectionOldCompras)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Compras " + comprasCollectionOldCompras + " since its idFactura field is not nullable.");
                }
            }
            for (Ventas ventasCollectionOldVentas : ventasCollectionOld) {
                if (!ventasCollectionNew.contains(ventasCollectionOldVentas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ventas " + ventasCollectionOldVentas + " since its idFactura field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Compras> attachedComprasCollectionNew = new ArrayList<Compras>();
            for (Compras comprasCollectionNewComprasToAttach : comprasCollectionNew) {
                comprasCollectionNewComprasToAttach = em.getReference(comprasCollectionNewComprasToAttach.getClass(), comprasCollectionNewComprasToAttach.getIdCompras());
                attachedComprasCollectionNew.add(comprasCollectionNewComprasToAttach);
            }
            comprasCollectionNew = attachedComprasCollectionNew;
            facturacion.setComprasCollection(comprasCollectionNew);
            Collection<Ventas> attachedVentasCollectionNew = new ArrayList<Ventas>();
            for (Ventas ventasCollectionNewVentasToAttach : ventasCollectionNew) {
                ventasCollectionNewVentasToAttach = em.getReference(ventasCollectionNewVentasToAttach.getClass(), ventasCollectionNewVentasToAttach.getIdVentas());
                attachedVentasCollectionNew.add(ventasCollectionNewVentasToAttach);
            }
            ventasCollectionNew = attachedVentasCollectionNew;
            facturacion.setVentasCollection(ventasCollectionNew);
            facturacion = em.merge(facturacion);
            for (Compras comprasCollectionNewCompras : comprasCollectionNew) {
                if (!comprasCollectionOld.contains(comprasCollectionNewCompras)) {
                    Facturacion oldIdFacturaOfComprasCollectionNewCompras = comprasCollectionNewCompras.getIdFactura();
                    comprasCollectionNewCompras.setIdFactura(facturacion);
                    comprasCollectionNewCompras = em.merge(comprasCollectionNewCompras);
                    if (oldIdFacturaOfComprasCollectionNewCompras != null && !oldIdFacturaOfComprasCollectionNewCompras.equals(facturacion)) {
                        oldIdFacturaOfComprasCollectionNewCompras.getComprasCollection().remove(comprasCollectionNewCompras);
                        oldIdFacturaOfComprasCollectionNewCompras = em.merge(oldIdFacturaOfComprasCollectionNewCompras);
                    }
                }
            }
            for (Ventas ventasCollectionNewVentas : ventasCollectionNew) {
                if (!ventasCollectionOld.contains(ventasCollectionNewVentas)) {
                    Facturacion oldIdFacturaOfVentasCollectionNewVentas = ventasCollectionNewVentas.getIdFactura();
                    ventasCollectionNewVentas.setIdFactura(facturacion);
                    ventasCollectionNewVentas = em.merge(ventasCollectionNewVentas);
                    if (oldIdFacturaOfVentasCollectionNewVentas != null && !oldIdFacturaOfVentasCollectionNewVentas.equals(facturacion)) {
                        oldIdFacturaOfVentasCollectionNewVentas.getVentasCollection().remove(ventasCollectionNewVentas);
                        oldIdFacturaOfVentasCollectionNewVentas = em.merge(oldIdFacturaOfVentasCollectionNewVentas);
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
                Integer id = facturacion.getIdFacturacion();
                if (findFacturacion(id) == null) {
                    throw new NonexistentEntityException("The facturacion with id " + id + " no longer exists.");
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
            Facturacion facturacion;
            try {
                facturacion = em.getReference(Facturacion.class, id);
                facturacion.getIdFacturacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The facturacion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Compras> comprasCollectionOrphanCheck = facturacion.getComprasCollection();
            for (Compras comprasCollectionOrphanCheckCompras : comprasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Facturacion (" + facturacion + ") cannot be destroyed since the Compras " + comprasCollectionOrphanCheckCompras + " in its comprasCollection field has a non-nullable idFactura field.");
            }
            Collection<Ventas> ventasCollectionOrphanCheck = facturacion.getVentasCollection();
            for (Ventas ventasCollectionOrphanCheckVentas : ventasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Facturacion (" + facturacion + ") cannot be destroyed since the Ventas " + ventasCollectionOrphanCheckVentas + " in its ventasCollection field has a non-nullable idFactura field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(facturacion);
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

    public List<Facturacion> findFacturacionEntities() {
        return findFacturacionEntities(true, -1, -1);
    }

    public List<Facturacion> findFacturacionEntities(int maxResults, int firstResult) {
        return findFacturacionEntities(false, maxResults, firstResult);
    }

    private List<Facturacion> findFacturacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Facturacion.class));
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

    public Facturacion findFacturacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Facturacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getFacturacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Facturacion> rt = cq.from(Facturacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
