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
import entidades.Pago;
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
public class PagoJpaController implements Serializable {

    public PagoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pago pago) throws RollbackFailureException, Exception {
        if (pago.getComprasCollection() == null) {
            pago.setComprasCollection(new ArrayList<Compras>());
        }
        if (pago.getVentasCollection() == null) {
            pago.setVentasCollection(new ArrayList<Ventas>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Compras> attachedComprasCollection = new ArrayList<Compras>();
            for (Compras comprasCollectionComprasToAttach : pago.getComprasCollection()) {
                comprasCollectionComprasToAttach = em.getReference(comprasCollectionComprasToAttach.getClass(), comprasCollectionComprasToAttach.getIdCompras());
                attachedComprasCollection.add(comprasCollectionComprasToAttach);
            }
            pago.setComprasCollection(attachedComprasCollection);
            Collection<Ventas> attachedVentasCollection = new ArrayList<Ventas>();
            for (Ventas ventasCollectionVentasToAttach : pago.getVentasCollection()) {
                ventasCollectionVentasToAttach = em.getReference(ventasCollectionVentasToAttach.getClass(), ventasCollectionVentasToAttach.getIdVentas());
                attachedVentasCollection.add(ventasCollectionVentasToAttach);
            }
            pago.setVentasCollection(attachedVentasCollection);
            em.persist(pago);
            for (Compras comprasCollectionCompras : pago.getComprasCollection()) {
                Pago oldIdPagoOfComprasCollectionCompras = comprasCollectionCompras.getIdPago();
                comprasCollectionCompras.setIdPago(pago);
                comprasCollectionCompras = em.merge(comprasCollectionCompras);
                if (oldIdPagoOfComprasCollectionCompras != null) {
                    oldIdPagoOfComprasCollectionCompras.getComprasCollection().remove(comprasCollectionCompras);
                    oldIdPagoOfComprasCollectionCompras = em.merge(oldIdPagoOfComprasCollectionCompras);
                }
            }
            for (Ventas ventasCollectionVentas : pago.getVentasCollection()) {
                Pago oldIdPagoOfVentasCollectionVentas = ventasCollectionVentas.getIdPago();
                ventasCollectionVentas.setIdPago(pago);
                ventasCollectionVentas = em.merge(ventasCollectionVentas);
                if (oldIdPagoOfVentasCollectionVentas != null) {
                    oldIdPagoOfVentasCollectionVentas.getVentasCollection().remove(ventasCollectionVentas);
                    oldIdPagoOfVentasCollectionVentas = em.merge(oldIdPagoOfVentasCollectionVentas);
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

    public void edit(Pago pago) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Pago persistentPago = em.find(Pago.class, pago.getIdPago());
            Collection<Compras> comprasCollectionOld = persistentPago.getComprasCollection();
            Collection<Compras> comprasCollectionNew = pago.getComprasCollection();
            Collection<Ventas> ventasCollectionOld = persistentPago.getVentasCollection();
            Collection<Ventas> ventasCollectionNew = pago.getVentasCollection();
            List<String> illegalOrphanMessages = null;
            for (Compras comprasCollectionOldCompras : comprasCollectionOld) {
                if (!comprasCollectionNew.contains(comprasCollectionOldCompras)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Compras " + comprasCollectionOldCompras + " since its idPago field is not nullable.");
                }
            }
            for (Ventas ventasCollectionOldVentas : ventasCollectionOld) {
                if (!ventasCollectionNew.contains(ventasCollectionOldVentas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ventas " + ventasCollectionOldVentas + " since its idPago field is not nullable.");
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
            pago.setComprasCollection(comprasCollectionNew);
            Collection<Ventas> attachedVentasCollectionNew = new ArrayList<Ventas>();
            for (Ventas ventasCollectionNewVentasToAttach : ventasCollectionNew) {
                ventasCollectionNewVentasToAttach = em.getReference(ventasCollectionNewVentasToAttach.getClass(), ventasCollectionNewVentasToAttach.getIdVentas());
                attachedVentasCollectionNew.add(ventasCollectionNewVentasToAttach);
            }
            ventasCollectionNew = attachedVentasCollectionNew;
            pago.setVentasCollection(ventasCollectionNew);
            pago = em.merge(pago);
            for (Compras comprasCollectionNewCompras : comprasCollectionNew) {
                if (!comprasCollectionOld.contains(comprasCollectionNewCompras)) {
                    Pago oldIdPagoOfComprasCollectionNewCompras = comprasCollectionNewCompras.getIdPago();
                    comprasCollectionNewCompras.setIdPago(pago);
                    comprasCollectionNewCompras = em.merge(comprasCollectionNewCompras);
                    if (oldIdPagoOfComprasCollectionNewCompras != null && !oldIdPagoOfComprasCollectionNewCompras.equals(pago)) {
                        oldIdPagoOfComprasCollectionNewCompras.getComprasCollection().remove(comprasCollectionNewCompras);
                        oldIdPagoOfComprasCollectionNewCompras = em.merge(oldIdPagoOfComprasCollectionNewCompras);
                    }
                }
            }
            for (Ventas ventasCollectionNewVentas : ventasCollectionNew) {
                if (!ventasCollectionOld.contains(ventasCollectionNewVentas)) {
                    Pago oldIdPagoOfVentasCollectionNewVentas = ventasCollectionNewVentas.getIdPago();
                    ventasCollectionNewVentas.setIdPago(pago);
                    ventasCollectionNewVentas = em.merge(ventasCollectionNewVentas);
                    if (oldIdPagoOfVentasCollectionNewVentas != null && !oldIdPagoOfVentasCollectionNewVentas.equals(pago)) {
                        oldIdPagoOfVentasCollectionNewVentas.getVentasCollection().remove(ventasCollectionNewVentas);
                        oldIdPagoOfVentasCollectionNewVentas = em.merge(oldIdPagoOfVentasCollectionNewVentas);
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
                Integer id = pago.getIdPago();
                if (findPago(id) == null) {
                    throw new NonexistentEntityException("The pago with id " + id + " no longer exists.");
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
            Pago pago;
            try {
                pago = em.getReference(Pago.class, id);
                pago.getIdPago();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pago with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Compras> comprasCollectionOrphanCheck = pago.getComprasCollection();
            for (Compras comprasCollectionOrphanCheckCompras : comprasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pago (" + pago + ") cannot be destroyed since the Compras " + comprasCollectionOrphanCheckCompras + " in its comprasCollection field has a non-nullable idPago field.");
            }
            Collection<Ventas> ventasCollectionOrphanCheck = pago.getVentasCollection();
            for (Ventas ventasCollectionOrphanCheckVentas : ventasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pago (" + pago + ") cannot be destroyed since the Ventas " + ventasCollectionOrphanCheckVentas + " in its ventasCollection field has a non-nullable idPago field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(pago);
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

    public List<Pago> findPagoEntities() {
        return findPagoEntities(true, -1, -1);
    }

    public List<Pago> findPagoEntities(int maxResults, int firstResult) {
        return findPagoEntities(false, maxResults, firstResult);
    }

    private List<Pago> findPagoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pago.class));
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

    public Pago findPago(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pago.class, id);
        } finally {
            em.close();
        }
    }

    public int getPagoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pago> rt = cq.from(Pago.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
