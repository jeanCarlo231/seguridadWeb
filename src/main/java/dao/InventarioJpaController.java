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
import entidades.Almacen;
import entidades.Producto;
import entidades.Proveedor;
import entidades.Categoria;
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
public class InventarioJpaController implements Serializable {

    public InventarioJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Inventario inventario) throws RollbackFailureException, Exception {
        if (inventario.getCategoriaCollection() == null) {
            inventario.setCategoriaCollection(new ArrayList<Categoria>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Almacen idAlmacen = inventario.getIdAlmacen();
            if (idAlmacen != null) {
                idAlmacen = em.getReference(idAlmacen.getClass(), idAlmacen.getIdAlmacen());
                inventario.setIdAlmacen(idAlmacen);
            }
            Producto idProducto = inventario.getIdProducto();
            if (idProducto != null) {
                idProducto = em.getReference(idProducto.getClass(), idProducto.getIdProducto());
                inventario.setIdProducto(idProducto);
            }
            Proveedor idProveedor = inventario.getIdProveedor();
            if (idProveedor != null) {
                idProveedor = em.getReference(idProveedor.getClass(), idProveedor.getIdProveedor());
                inventario.setIdProveedor(idProveedor);
            }
            Collection<Categoria> attachedCategoriaCollection = new ArrayList<Categoria>();
            for (Categoria categoriaCollectionCategoriaToAttach : inventario.getCategoriaCollection()) {
                categoriaCollectionCategoriaToAttach = em.getReference(categoriaCollectionCategoriaToAttach.getClass(), categoriaCollectionCategoriaToAttach.getIdCategoria());
                attachedCategoriaCollection.add(categoriaCollectionCategoriaToAttach);
            }
            inventario.setCategoriaCollection(attachedCategoriaCollection);
            em.persist(inventario);
            if (idAlmacen != null) {
                idAlmacen.getInventarioCollection().add(inventario);
                idAlmacen = em.merge(idAlmacen);
            }
            if (idProducto != null) {
                idProducto.getInventarioCollection().add(inventario);
                idProducto = em.merge(idProducto);
            }
            if (idProveedor != null) {
                idProveedor.getInventarioCollection().add(inventario);
                idProveedor = em.merge(idProveedor);
            }
            for (Categoria categoriaCollectionCategoria : inventario.getCategoriaCollection()) {
                Inventario oldIdInventarioOfCategoriaCollectionCategoria = categoriaCollectionCategoria.getIdInventario();
                categoriaCollectionCategoria.setIdInventario(inventario);
                categoriaCollectionCategoria = em.merge(categoriaCollectionCategoria);
                if (oldIdInventarioOfCategoriaCollectionCategoria != null) {
                    oldIdInventarioOfCategoriaCollectionCategoria.getCategoriaCollection().remove(categoriaCollectionCategoria);
                    oldIdInventarioOfCategoriaCollectionCategoria = em.merge(oldIdInventarioOfCategoriaCollectionCategoria);
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

    public void edit(Inventario inventario) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Inventario persistentInventario = em.find(Inventario.class, inventario.getIdInventario());
            Almacen idAlmacenOld = persistentInventario.getIdAlmacen();
            Almacen idAlmacenNew = inventario.getIdAlmacen();
            Producto idProductoOld = persistentInventario.getIdProducto();
            Producto idProductoNew = inventario.getIdProducto();
            Proveedor idProveedorOld = persistentInventario.getIdProveedor();
            Proveedor idProveedorNew = inventario.getIdProveedor();
            Collection<Categoria> categoriaCollectionOld = persistentInventario.getCategoriaCollection();
            Collection<Categoria> categoriaCollectionNew = inventario.getCategoriaCollection();
            List<String> illegalOrphanMessages = null;
            for (Categoria categoriaCollectionOldCategoria : categoriaCollectionOld) {
                if (!categoriaCollectionNew.contains(categoriaCollectionOldCategoria)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Categoria " + categoriaCollectionOldCategoria + " since its idInventario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idAlmacenNew != null) {
                idAlmacenNew = em.getReference(idAlmacenNew.getClass(), idAlmacenNew.getIdAlmacen());
                inventario.setIdAlmacen(idAlmacenNew);
            }
            if (idProductoNew != null) {
                idProductoNew = em.getReference(idProductoNew.getClass(), idProductoNew.getIdProducto());
                inventario.setIdProducto(idProductoNew);
            }
            if (idProveedorNew != null) {
                idProveedorNew = em.getReference(idProveedorNew.getClass(), idProveedorNew.getIdProveedor());
                inventario.setIdProveedor(idProveedorNew);
            }
            Collection<Categoria> attachedCategoriaCollectionNew = new ArrayList<Categoria>();
            for (Categoria categoriaCollectionNewCategoriaToAttach : categoriaCollectionNew) {
                categoriaCollectionNewCategoriaToAttach = em.getReference(categoriaCollectionNewCategoriaToAttach.getClass(), categoriaCollectionNewCategoriaToAttach.getIdCategoria());
                attachedCategoriaCollectionNew.add(categoriaCollectionNewCategoriaToAttach);
            }
            categoriaCollectionNew = attachedCategoriaCollectionNew;
            inventario.setCategoriaCollection(categoriaCollectionNew);
            inventario = em.merge(inventario);
            if (idAlmacenOld != null && !idAlmacenOld.equals(idAlmacenNew)) {
                idAlmacenOld.getInventarioCollection().remove(inventario);
                idAlmacenOld = em.merge(idAlmacenOld);
            }
            if (idAlmacenNew != null && !idAlmacenNew.equals(idAlmacenOld)) {
                idAlmacenNew.getInventarioCollection().add(inventario);
                idAlmacenNew = em.merge(idAlmacenNew);
            }
            if (idProductoOld != null && !idProductoOld.equals(idProductoNew)) {
                idProductoOld.getInventarioCollection().remove(inventario);
                idProductoOld = em.merge(idProductoOld);
            }
            if (idProductoNew != null && !idProductoNew.equals(idProductoOld)) {
                idProductoNew.getInventarioCollection().add(inventario);
                idProductoNew = em.merge(idProductoNew);
            }
            if (idProveedorOld != null && !idProveedorOld.equals(idProveedorNew)) {
                idProveedorOld.getInventarioCollection().remove(inventario);
                idProveedorOld = em.merge(idProveedorOld);
            }
            if (idProveedorNew != null && !idProveedorNew.equals(idProveedorOld)) {
                idProveedorNew.getInventarioCollection().add(inventario);
                idProveedorNew = em.merge(idProveedorNew);
            }
            for (Categoria categoriaCollectionNewCategoria : categoriaCollectionNew) {
                if (!categoriaCollectionOld.contains(categoriaCollectionNewCategoria)) {
                    Inventario oldIdInventarioOfCategoriaCollectionNewCategoria = categoriaCollectionNewCategoria.getIdInventario();
                    categoriaCollectionNewCategoria.setIdInventario(inventario);
                    categoriaCollectionNewCategoria = em.merge(categoriaCollectionNewCategoria);
                    if (oldIdInventarioOfCategoriaCollectionNewCategoria != null && !oldIdInventarioOfCategoriaCollectionNewCategoria.equals(inventario)) {
                        oldIdInventarioOfCategoriaCollectionNewCategoria.getCategoriaCollection().remove(categoriaCollectionNewCategoria);
                        oldIdInventarioOfCategoriaCollectionNewCategoria = em.merge(oldIdInventarioOfCategoriaCollectionNewCategoria);
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
                Integer id = inventario.getIdInventario();
                if (findInventario(id) == null) {
                    throw new NonexistentEntityException("The inventario with id " + id + " no longer exists.");
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
            Inventario inventario;
            try {
                inventario = em.getReference(Inventario.class, id);
                inventario.getIdInventario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The inventario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Categoria> categoriaCollectionOrphanCheck = inventario.getCategoriaCollection();
            for (Categoria categoriaCollectionOrphanCheckCategoria : categoriaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Inventario (" + inventario + ") cannot be destroyed since the Categoria " + categoriaCollectionOrphanCheckCategoria + " in its categoriaCollection field has a non-nullable idInventario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Almacen idAlmacen = inventario.getIdAlmacen();
            if (idAlmacen != null) {
                idAlmacen.getInventarioCollection().remove(inventario);
                idAlmacen = em.merge(idAlmacen);
            }
            Producto idProducto = inventario.getIdProducto();
            if (idProducto != null) {
                idProducto.getInventarioCollection().remove(inventario);
                idProducto = em.merge(idProducto);
            }
            Proveedor idProveedor = inventario.getIdProveedor();
            if (idProveedor != null) {
                idProveedor.getInventarioCollection().remove(inventario);
                idProveedor = em.merge(idProveedor);
            }
            em.remove(inventario);
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

    public List<Inventario> findInventarioEntities() {
        return findInventarioEntities(true, -1, -1);
    }

    public List<Inventario> findInventarioEntities(int maxResults, int firstResult) {
        return findInventarioEntities(false, maxResults, firstResult);
    }

    private List<Inventario> findInventarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Inventario.class));
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

    public Inventario findInventario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Inventario.class, id);
        } finally {
            em.close();
        }
    }

    public int getInventarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Inventario> rt = cq.from(Inventario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
