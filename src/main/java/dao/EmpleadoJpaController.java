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
import entidades.Usuario;
import entidades.Compras;
import entidades.Empleado;
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
public class EmpleadoJpaController implements Serializable {

    public EmpleadoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empleado empleado) throws RollbackFailureException, Exception {
        if (empleado.getComprasCollection() == null) {
            empleado.setComprasCollection(new ArrayList<Compras>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuario idUsuario = empleado.getIdUsuario();
            if (idUsuario != null) {
                idUsuario = em.getReference(idUsuario.getClass(), idUsuario.getIdUsuario());
                empleado.setIdUsuario(idUsuario);
            }
            Collection<Compras> attachedComprasCollection = new ArrayList<Compras>();
            for (Compras comprasCollectionComprasToAttach : empleado.getComprasCollection()) {
                comprasCollectionComprasToAttach = em.getReference(comprasCollectionComprasToAttach.getClass(), comprasCollectionComprasToAttach.getIdCompras());
                attachedComprasCollection.add(comprasCollectionComprasToAttach);
            }
            empleado.setComprasCollection(attachedComprasCollection);
            em.persist(empleado);
            if (idUsuario != null) {
                idUsuario.getEmpleadoCollection().add(empleado);
                idUsuario = em.merge(idUsuario);
            }
            for (Compras comprasCollectionCompras : empleado.getComprasCollection()) {
                Empleado oldIdEmpleadoOfComprasCollectionCompras = comprasCollectionCompras.getIdEmpleado();
                comprasCollectionCompras.setIdEmpleado(empleado);
                comprasCollectionCompras = em.merge(comprasCollectionCompras);
                if (oldIdEmpleadoOfComprasCollectionCompras != null) {
                    oldIdEmpleadoOfComprasCollectionCompras.getComprasCollection().remove(comprasCollectionCompras);
                    oldIdEmpleadoOfComprasCollectionCompras = em.merge(oldIdEmpleadoOfComprasCollectionCompras);
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

    public void edit(Empleado empleado) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Empleado persistentEmpleado = em.find(Empleado.class, empleado.getIdEmpleado());
            Usuario idUsuarioOld = persistentEmpleado.getIdUsuario();
            Usuario idUsuarioNew = empleado.getIdUsuario();
            Collection<Compras> comprasCollectionOld = persistentEmpleado.getComprasCollection();
            Collection<Compras> comprasCollectionNew = empleado.getComprasCollection();
            List<String> illegalOrphanMessages = null;
            for (Compras comprasCollectionOldCompras : comprasCollectionOld) {
                if (!comprasCollectionNew.contains(comprasCollectionOldCompras)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Compras " + comprasCollectionOldCompras + " since its idEmpleado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idUsuarioNew != null) {
                idUsuarioNew = em.getReference(idUsuarioNew.getClass(), idUsuarioNew.getIdUsuario());
                empleado.setIdUsuario(idUsuarioNew);
            }
            Collection<Compras> attachedComprasCollectionNew = new ArrayList<Compras>();
            for (Compras comprasCollectionNewComprasToAttach : comprasCollectionNew) {
                comprasCollectionNewComprasToAttach = em.getReference(comprasCollectionNewComprasToAttach.getClass(), comprasCollectionNewComprasToAttach.getIdCompras());
                attachedComprasCollectionNew.add(comprasCollectionNewComprasToAttach);
            }
            comprasCollectionNew = attachedComprasCollectionNew;
            empleado.setComprasCollection(comprasCollectionNew);
            empleado = em.merge(empleado);
            if (idUsuarioOld != null && !idUsuarioOld.equals(idUsuarioNew)) {
                idUsuarioOld.getEmpleadoCollection().remove(empleado);
                idUsuarioOld = em.merge(idUsuarioOld);
            }
            if (idUsuarioNew != null && !idUsuarioNew.equals(idUsuarioOld)) {
                idUsuarioNew.getEmpleadoCollection().add(empleado);
                idUsuarioNew = em.merge(idUsuarioNew);
            }
            for (Compras comprasCollectionNewCompras : comprasCollectionNew) {
                if (!comprasCollectionOld.contains(comprasCollectionNewCompras)) {
                    Empleado oldIdEmpleadoOfComprasCollectionNewCompras = comprasCollectionNewCompras.getIdEmpleado();
                    comprasCollectionNewCompras.setIdEmpleado(empleado);
                    comprasCollectionNewCompras = em.merge(comprasCollectionNewCompras);
                    if (oldIdEmpleadoOfComprasCollectionNewCompras != null && !oldIdEmpleadoOfComprasCollectionNewCompras.equals(empleado)) {
                        oldIdEmpleadoOfComprasCollectionNewCompras.getComprasCollection().remove(comprasCollectionNewCompras);
                        oldIdEmpleadoOfComprasCollectionNewCompras = em.merge(oldIdEmpleadoOfComprasCollectionNewCompras);
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
                Integer id = empleado.getIdEmpleado();
                if (findEmpleado(id) == null) {
                    throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.");
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
            Empleado empleado;
            try {
                empleado = em.getReference(Empleado.class, id);
                empleado.getIdEmpleado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Compras> comprasCollectionOrphanCheck = empleado.getComprasCollection();
            for (Compras comprasCollectionOrphanCheckCompras : comprasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empleado (" + empleado + ") cannot be destroyed since the Compras " + comprasCollectionOrphanCheckCompras + " in its comprasCollection field has a non-nullable idEmpleado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Usuario idUsuario = empleado.getIdUsuario();
            if (idUsuario != null) {
                idUsuario.getEmpleadoCollection().remove(empleado);
                idUsuario = em.merge(idUsuario);
            }
            em.remove(empleado);
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

    public List<Empleado> findEmpleadoEntities() {
        return findEmpleadoEntities(true, -1, -1);
    }

    public List<Empleado> findEmpleadoEntities(int maxResults, int firstResult) {
        return findEmpleadoEntities(false, maxResults, firstResult);
    }

    private List<Empleado> findEmpleadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empleado.class));
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

    public Empleado findEmpleado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empleado.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpleadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empleado> rt = cq.from(Empleado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
