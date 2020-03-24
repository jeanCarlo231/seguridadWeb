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
import java.util.ArrayList;
import java.util.Collection;
import entidades.Almacen;
import entidades.Inventario;
import entidades.Proveedor;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Saul
 */
public class ProveedorJpaController implements Serializable {

    public ProveedorJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Proveedor proveedor) throws RollbackFailureException, Exception {
        if (proveedor.getComprasCollection() == null) {
            proveedor.setComprasCollection(new ArrayList<Compras>());
        }
        if (proveedor.getAlmacenCollection() == null) {
            proveedor.setAlmacenCollection(new ArrayList<Almacen>());
        }
        if (proveedor.getInventarioCollection() == null) {
            proveedor.setInventarioCollection(new ArrayList<Inventario>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Compras> attachedComprasCollection = new ArrayList<Compras>();
            for (Compras comprasCollectionComprasToAttach : proveedor.getComprasCollection()) {
                comprasCollectionComprasToAttach = em.getReference(comprasCollectionComprasToAttach.getClass(), comprasCollectionComprasToAttach.getIdCompras());
                attachedComprasCollection.add(comprasCollectionComprasToAttach);
            }
            proveedor.setComprasCollection(attachedComprasCollection);
            Collection<Almacen> attachedAlmacenCollection = new ArrayList<Almacen>();
            for (Almacen almacenCollectionAlmacenToAttach : proveedor.getAlmacenCollection()) {
                almacenCollectionAlmacenToAttach = em.getReference(almacenCollectionAlmacenToAttach.getClass(), almacenCollectionAlmacenToAttach.getIdAlmacen());
                attachedAlmacenCollection.add(almacenCollectionAlmacenToAttach);
            }
            proveedor.setAlmacenCollection(attachedAlmacenCollection);
            Collection<Inventario> attachedInventarioCollection = new ArrayList<Inventario>();
            for (Inventario inventarioCollectionInventarioToAttach : proveedor.getInventarioCollection()) {
                inventarioCollectionInventarioToAttach = em.getReference(inventarioCollectionInventarioToAttach.getClass(), inventarioCollectionInventarioToAttach.getIdInventario());
                attachedInventarioCollection.add(inventarioCollectionInventarioToAttach);
            }
            proveedor.setInventarioCollection(attachedInventarioCollection);
            em.persist(proveedor);
            for (Compras comprasCollectionCompras : proveedor.getComprasCollection()) {
                Proveedor oldIdProveedorOfComprasCollectionCompras = comprasCollectionCompras.getIdProveedor();
                comprasCollectionCompras.setIdProveedor(proveedor);
                comprasCollectionCompras = em.merge(comprasCollectionCompras);
                if (oldIdProveedorOfComprasCollectionCompras != null) {
                    oldIdProveedorOfComprasCollectionCompras.getComprasCollection().remove(comprasCollectionCompras);
                    oldIdProveedorOfComprasCollectionCompras = em.merge(oldIdProveedorOfComprasCollectionCompras);
                }
            }
            for (Almacen almacenCollectionAlmacen : proveedor.getAlmacenCollection()) {
                Proveedor oldIdProveedorOfAlmacenCollectionAlmacen = almacenCollectionAlmacen.getIdProveedor();
                almacenCollectionAlmacen.setIdProveedor(proveedor);
                almacenCollectionAlmacen = em.merge(almacenCollectionAlmacen);
                if (oldIdProveedorOfAlmacenCollectionAlmacen != null) {
                    oldIdProveedorOfAlmacenCollectionAlmacen.getAlmacenCollection().remove(almacenCollectionAlmacen);
                    oldIdProveedorOfAlmacenCollectionAlmacen = em.merge(oldIdProveedorOfAlmacenCollectionAlmacen);
                }
            }
            for (Inventario inventarioCollectionInventario : proveedor.getInventarioCollection()) {
                Proveedor oldIdProveedorOfInventarioCollectionInventario = inventarioCollectionInventario.getIdProveedor();
                inventarioCollectionInventario.setIdProveedor(proveedor);
                inventarioCollectionInventario = em.merge(inventarioCollectionInventario);
                if (oldIdProveedorOfInventarioCollectionInventario != null) {
                    oldIdProveedorOfInventarioCollectionInventario.getInventarioCollection().remove(inventarioCollectionInventario);
                    oldIdProveedorOfInventarioCollectionInventario = em.merge(oldIdProveedorOfInventarioCollectionInventario);
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

    public void edit(Proveedor proveedor) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Proveedor persistentProveedor = em.find(Proveedor.class, proveedor.getIdProveedor());
            Collection<Compras> comprasCollectionOld = persistentProveedor.getComprasCollection();
            Collection<Compras> comprasCollectionNew = proveedor.getComprasCollection();
            Collection<Almacen> almacenCollectionOld = persistentProveedor.getAlmacenCollection();
            Collection<Almacen> almacenCollectionNew = proveedor.getAlmacenCollection();
            Collection<Inventario> inventarioCollectionOld = persistentProveedor.getInventarioCollection();
            Collection<Inventario> inventarioCollectionNew = proveedor.getInventarioCollection();
            List<String> illegalOrphanMessages = null;
            for (Compras comprasCollectionOldCompras : comprasCollectionOld) {
                if (!comprasCollectionNew.contains(comprasCollectionOldCompras)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Compras " + comprasCollectionOldCompras + " since its idProveedor field is not nullable.");
                }
            }
            for (Inventario inventarioCollectionOldInventario : inventarioCollectionOld) {
                if (!inventarioCollectionNew.contains(inventarioCollectionOldInventario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Inventario " + inventarioCollectionOldInventario + " since its idProveedor field is not nullable.");
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
            proveedor.setComprasCollection(comprasCollectionNew);
            Collection<Almacen> attachedAlmacenCollectionNew = new ArrayList<Almacen>();
            for (Almacen almacenCollectionNewAlmacenToAttach : almacenCollectionNew) {
                almacenCollectionNewAlmacenToAttach = em.getReference(almacenCollectionNewAlmacenToAttach.getClass(), almacenCollectionNewAlmacenToAttach.getIdAlmacen());
                attachedAlmacenCollectionNew.add(almacenCollectionNewAlmacenToAttach);
            }
            almacenCollectionNew = attachedAlmacenCollectionNew;
            proveedor.setAlmacenCollection(almacenCollectionNew);
            Collection<Inventario> attachedInventarioCollectionNew = new ArrayList<Inventario>();
            for (Inventario inventarioCollectionNewInventarioToAttach : inventarioCollectionNew) {
                inventarioCollectionNewInventarioToAttach = em.getReference(inventarioCollectionNewInventarioToAttach.getClass(), inventarioCollectionNewInventarioToAttach.getIdInventario());
                attachedInventarioCollectionNew.add(inventarioCollectionNewInventarioToAttach);
            }
            inventarioCollectionNew = attachedInventarioCollectionNew;
            proveedor.setInventarioCollection(inventarioCollectionNew);
            proveedor = em.merge(proveedor);
            for (Compras comprasCollectionNewCompras : comprasCollectionNew) {
                if (!comprasCollectionOld.contains(comprasCollectionNewCompras)) {
                    Proveedor oldIdProveedorOfComprasCollectionNewCompras = comprasCollectionNewCompras.getIdProveedor();
                    comprasCollectionNewCompras.setIdProveedor(proveedor);
                    comprasCollectionNewCompras = em.merge(comprasCollectionNewCompras);
                    if (oldIdProveedorOfComprasCollectionNewCompras != null && !oldIdProveedorOfComprasCollectionNewCompras.equals(proveedor)) {
                        oldIdProveedorOfComprasCollectionNewCompras.getComprasCollection().remove(comprasCollectionNewCompras);
                        oldIdProveedorOfComprasCollectionNewCompras = em.merge(oldIdProveedorOfComprasCollectionNewCompras);
                    }
                }
            }
            for (Almacen almacenCollectionOldAlmacen : almacenCollectionOld) {
                if (!almacenCollectionNew.contains(almacenCollectionOldAlmacen)) {
                    almacenCollectionOldAlmacen.setIdProveedor(null);
                    almacenCollectionOldAlmacen = em.merge(almacenCollectionOldAlmacen);
                }
            }
            for (Almacen almacenCollectionNewAlmacen : almacenCollectionNew) {
                if (!almacenCollectionOld.contains(almacenCollectionNewAlmacen)) {
                    Proveedor oldIdProveedorOfAlmacenCollectionNewAlmacen = almacenCollectionNewAlmacen.getIdProveedor();
                    almacenCollectionNewAlmacen.setIdProveedor(proveedor);
                    almacenCollectionNewAlmacen = em.merge(almacenCollectionNewAlmacen);
                    if (oldIdProveedorOfAlmacenCollectionNewAlmacen != null && !oldIdProveedorOfAlmacenCollectionNewAlmacen.equals(proveedor)) {
                        oldIdProveedorOfAlmacenCollectionNewAlmacen.getAlmacenCollection().remove(almacenCollectionNewAlmacen);
                        oldIdProveedorOfAlmacenCollectionNewAlmacen = em.merge(oldIdProveedorOfAlmacenCollectionNewAlmacen);
                    }
                }
            }
            for (Inventario inventarioCollectionNewInventario : inventarioCollectionNew) {
                if (!inventarioCollectionOld.contains(inventarioCollectionNewInventario)) {
                    Proveedor oldIdProveedorOfInventarioCollectionNewInventario = inventarioCollectionNewInventario.getIdProveedor();
                    inventarioCollectionNewInventario.setIdProveedor(proveedor);
                    inventarioCollectionNewInventario = em.merge(inventarioCollectionNewInventario);
                    if (oldIdProveedorOfInventarioCollectionNewInventario != null && !oldIdProveedorOfInventarioCollectionNewInventario.equals(proveedor)) {
                        oldIdProveedorOfInventarioCollectionNewInventario.getInventarioCollection().remove(inventarioCollectionNewInventario);
                        oldIdProveedorOfInventarioCollectionNewInventario = em.merge(oldIdProveedorOfInventarioCollectionNewInventario);
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
                Integer id = proveedor.getIdProveedor();
                if (findProveedor(id) == null) {
                    throw new NonexistentEntityException("The proveedor with id " + id + " no longer exists.");
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
            Proveedor proveedor;
            try {
                proveedor = em.getReference(Proveedor.class, id);
                proveedor.getIdProveedor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The proveedor with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Compras> comprasCollectionOrphanCheck = proveedor.getComprasCollection();
            for (Compras comprasCollectionOrphanCheckCompras : comprasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Proveedor (" + proveedor + ") cannot be destroyed since the Compras " + comprasCollectionOrphanCheckCompras + " in its comprasCollection field has a non-nullable idProveedor field.");
            }
            Collection<Inventario> inventarioCollectionOrphanCheck = proveedor.getInventarioCollection();
            for (Inventario inventarioCollectionOrphanCheckInventario : inventarioCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Proveedor (" + proveedor + ") cannot be destroyed since the Inventario " + inventarioCollectionOrphanCheckInventario + " in its inventarioCollection field has a non-nullable idProveedor field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Almacen> almacenCollection = proveedor.getAlmacenCollection();
            for (Almacen almacenCollectionAlmacen : almacenCollection) {
                almacenCollectionAlmacen.setIdProveedor(null);
                almacenCollectionAlmacen = em.merge(almacenCollectionAlmacen);
            }
            em.remove(proveedor);
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

    public List<Proveedor> findProveedorEntities() {
        return findProveedorEntities(true, -1, -1);
    }

    public List<Proveedor> findProveedorEntities(int maxResults, int firstResult) {
        return findProveedorEntities(false, maxResults, firstResult);
    }

    private List<Proveedor> findProveedorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Proveedor.class));
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

    public Proveedor findProveedor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Proveedor.class, id);
        } finally {
            em.close();
        }
    }

    public int getProveedorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Proveedor> rt = cq.from(Proveedor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
