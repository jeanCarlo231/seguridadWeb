/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import dao.exceptions.RollbackFailureException;
import entidades.Almacen;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entidades.Proveedor;
import entidades.Inventario;
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
public class AlmacenJpaController implements Serializable {

    public AlmacenJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Almacen almacen) throws RollbackFailureException, Exception {
        if (almacen.getInventarioCollection() == null) {
            almacen.setInventarioCollection(new ArrayList<Inventario>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Proveedor idProveedor = almacen.getIdProveedor();
            if (idProveedor != null) {
                idProveedor = em.getReference(idProveedor.getClass(), idProveedor.getIdProveedor());
                almacen.setIdProveedor(idProveedor);
            }
            Collection<Inventario> attachedInventarioCollection = new ArrayList<Inventario>();
            for (Inventario inventarioCollectionInventarioToAttach : almacen.getInventarioCollection()) {
                inventarioCollectionInventarioToAttach = em.getReference(inventarioCollectionInventarioToAttach.getClass(), inventarioCollectionInventarioToAttach.getIdInventario());
                attachedInventarioCollection.add(inventarioCollectionInventarioToAttach);
            }
            almacen.setInventarioCollection(attachedInventarioCollection);
            em.persist(almacen);
            if (idProveedor != null) {
                idProveedor.getAlmacenCollection().add(almacen);
                idProveedor = em.merge(idProveedor);
            }
            for (Inventario inventarioCollectionInventario : almacen.getInventarioCollection()) {
                Almacen oldIdAlmacenOfInventarioCollectionInventario = inventarioCollectionInventario.getIdAlmacen();
                inventarioCollectionInventario.setIdAlmacen(almacen);
                inventarioCollectionInventario = em.merge(inventarioCollectionInventario);
                if (oldIdAlmacenOfInventarioCollectionInventario != null) {
                    oldIdAlmacenOfInventarioCollectionInventario.getInventarioCollection().remove(inventarioCollectionInventario);
                    oldIdAlmacenOfInventarioCollectionInventario = em.merge(oldIdAlmacenOfInventarioCollectionInventario);
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

    public void edit(Almacen almacen) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Almacen persistentAlmacen = em.find(Almacen.class, almacen.getIdAlmacen());
            Proveedor idProveedorOld = persistentAlmacen.getIdProveedor();
            Proveedor idProveedorNew = almacen.getIdProveedor();
            Collection<Inventario> inventarioCollectionOld = persistentAlmacen.getInventarioCollection();
            Collection<Inventario> inventarioCollectionNew = almacen.getInventarioCollection();
            List<String> illegalOrphanMessages = null;
            for (Inventario inventarioCollectionOldInventario : inventarioCollectionOld) {
                if (!inventarioCollectionNew.contains(inventarioCollectionOldInventario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Inventario " + inventarioCollectionOldInventario + " since its idAlmacen field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idProveedorNew != null) {
                idProveedorNew = em.getReference(idProveedorNew.getClass(), idProveedorNew.getIdProveedor());
                almacen.setIdProveedor(idProveedorNew);
            }
            Collection<Inventario> attachedInventarioCollectionNew = new ArrayList<Inventario>();
            for (Inventario inventarioCollectionNewInventarioToAttach : inventarioCollectionNew) {
                inventarioCollectionNewInventarioToAttach = em.getReference(inventarioCollectionNewInventarioToAttach.getClass(), inventarioCollectionNewInventarioToAttach.getIdInventario());
                attachedInventarioCollectionNew.add(inventarioCollectionNewInventarioToAttach);
            }
            inventarioCollectionNew = attachedInventarioCollectionNew;
            almacen.setInventarioCollection(inventarioCollectionNew);
            almacen = em.merge(almacen);
            if (idProveedorOld != null && !idProveedorOld.equals(idProveedorNew)) {
                idProveedorOld.getAlmacenCollection().remove(almacen);
                idProveedorOld = em.merge(idProveedorOld);
            }
            if (idProveedorNew != null && !idProveedorNew.equals(idProveedorOld)) {
                idProveedorNew.getAlmacenCollection().add(almacen);
                idProveedorNew = em.merge(idProveedorNew);
            }
            for (Inventario inventarioCollectionNewInventario : inventarioCollectionNew) {
                if (!inventarioCollectionOld.contains(inventarioCollectionNewInventario)) {
                    Almacen oldIdAlmacenOfInventarioCollectionNewInventario = inventarioCollectionNewInventario.getIdAlmacen();
                    inventarioCollectionNewInventario.setIdAlmacen(almacen);
                    inventarioCollectionNewInventario = em.merge(inventarioCollectionNewInventario);
                    if (oldIdAlmacenOfInventarioCollectionNewInventario != null && !oldIdAlmacenOfInventarioCollectionNewInventario.equals(almacen)) {
                        oldIdAlmacenOfInventarioCollectionNewInventario.getInventarioCollection().remove(inventarioCollectionNewInventario);
                        oldIdAlmacenOfInventarioCollectionNewInventario = em.merge(oldIdAlmacenOfInventarioCollectionNewInventario);
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
                Integer id = almacen.getIdAlmacen();
                if (findAlmacen(id) == null) {
                    throw new NonexistentEntityException("The almacen with id " + id + " no longer exists.");
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
            Almacen almacen;
            try {
                almacen = em.getReference(Almacen.class, id);
                almacen.getIdAlmacen();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The almacen with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Inventario> inventarioCollectionOrphanCheck = almacen.getInventarioCollection();
            for (Inventario inventarioCollectionOrphanCheckInventario : inventarioCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Almacen (" + almacen + ") cannot be destroyed since the Inventario " + inventarioCollectionOrphanCheckInventario + " in its inventarioCollection field has a non-nullable idAlmacen field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Proveedor idProveedor = almacen.getIdProveedor();
            if (idProveedor != null) {
                idProveedor.getAlmacenCollection().remove(almacen);
                idProveedor = em.merge(idProveedor);
            }
            em.remove(almacen);
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

    public List<Almacen> findAlmacenEntities() {
        return findAlmacenEntities(true, -1, -1);
    }

    public List<Almacen> findAlmacenEntities(int maxResults, int firstResult) {
        return findAlmacenEntities(false, maxResults, firstResult);
    }

    private List<Almacen> findAlmacenEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Almacen.class));
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

    public Almacen findAlmacen(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Almacen.class, id);
        } finally {
            em.close();
        }
    }

    public int getAlmacenCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Almacen> rt = cq.from(Almacen.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
