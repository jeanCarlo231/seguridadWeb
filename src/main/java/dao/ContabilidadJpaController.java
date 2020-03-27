/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import entidades.Contabilidad;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Ventas;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Saul
 */
public class ContabilidadJpaController implements Serializable {

    public ContabilidadJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Contabilidad contabilidad) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Ventas idVenta = contabilidad.getIdVenta();
            if (idVenta != null) {
                idVenta = em.getReference(idVenta.getClass(), idVenta.getIdVentas());
                contabilidad.setIdVenta(idVenta);
            }
            em.persist(contabilidad);
            if (idVenta != null) {
                idVenta.getContabilidadCollection().add(contabilidad);
                idVenta = em.merge(idVenta);
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

    public void edit(Contabilidad contabilidad) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Contabilidad persistentContabilidad = em.find(Contabilidad.class, contabilidad.getIdContabilidad());
            Ventas idVentaOld = persistentContabilidad.getIdVenta();
            Ventas idVentaNew = contabilidad.getIdVenta();
            if (idVentaNew != null) {
                idVentaNew = em.getReference(idVentaNew.getClass(), idVentaNew.getIdVentas());
                contabilidad.setIdVenta(idVentaNew);
            }
            contabilidad = em.merge(contabilidad);
            if (idVentaOld != null && !idVentaOld.equals(idVentaNew)) {
                idVentaOld.getContabilidadCollection().remove(contabilidad);
                idVentaOld = em.merge(idVentaOld);
            }
            if (idVentaNew != null && !idVentaNew.equals(idVentaOld)) {
                idVentaNew.getContabilidadCollection().add(contabilidad);
                idVentaNew = em.merge(idVentaNew);
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
                Integer id = contabilidad.getIdContabilidad();
                if (findContabilidad(id) == null) {
                    throw new NonexistentEntityException("The contabilidad with id " + id + " no longer exists.");
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
            Contabilidad contabilidad;
            try {
                contabilidad = em.getReference(Contabilidad.class, id);
                contabilidad.getIdContabilidad();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The contabilidad with id " + id + " no longer exists.", enfe);
            }
            Ventas idVenta = contabilidad.getIdVenta();
            if (idVenta != null) {
                idVenta.getContabilidadCollection().remove(contabilidad);
                idVenta = em.merge(idVenta);
            }
            em.remove(contabilidad);
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

    public List<Contabilidad> findContabilidadEntities() {
        return findContabilidadEntities(true, -1, -1);
    }

    public List<Contabilidad> findContabilidadEntities(int maxResults, int firstResult) {
        return findContabilidadEntities(false, maxResults, firstResult);
    }

    private List<Contabilidad> findContabilidadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Contabilidad.class));
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

    public Contabilidad findContabilidad(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Contabilidad.class, id);
        } finally {
            em.close();
        }
    }

    public int getContabilidadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Contabilidad> rt = cq.from(Contabilidad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
