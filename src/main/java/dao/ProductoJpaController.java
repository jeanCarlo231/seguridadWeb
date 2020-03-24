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
import entidades.Categoria;
import entidades.Compras;
import java.util.ArrayList;
import java.util.Collection;
import entidades.Ventas;
import entidades.Inventario;
import entidades.Producto;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Saul
 */
public class ProductoJpaController implements Serializable {

    public ProductoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Producto producto) throws RollbackFailureException, Exception {
        if (producto.getComprasCollection() == null) {
            producto.setComprasCollection(new ArrayList<Compras>());
        }
        if (producto.getVentasCollection() == null) {
            producto.setVentasCollection(new ArrayList<Ventas>());
        }
        if (producto.getInventarioCollection() == null) {
            producto.setInventarioCollection(new ArrayList<Inventario>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Categoria idCategoria = producto.getIdCategoria();
            if (idCategoria != null) {
                idCategoria = em.getReference(idCategoria.getClass(), idCategoria.getIdCategoria());
                producto.setIdCategoria(idCategoria);
            }
            Collection<Compras> attachedComprasCollection = new ArrayList<Compras>();
            for (Compras comprasCollectionComprasToAttach : producto.getComprasCollection()) {
                comprasCollectionComprasToAttach = em.getReference(comprasCollectionComprasToAttach.getClass(), comprasCollectionComprasToAttach.getIdCompras());
                attachedComprasCollection.add(comprasCollectionComprasToAttach);
            }
            producto.setComprasCollection(attachedComprasCollection);
            Collection<Ventas> attachedVentasCollection = new ArrayList<Ventas>();
            for (Ventas ventasCollectionVentasToAttach : producto.getVentasCollection()) {
                ventasCollectionVentasToAttach = em.getReference(ventasCollectionVentasToAttach.getClass(), ventasCollectionVentasToAttach.getIdVentas());
                attachedVentasCollection.add(ventasCollectionVentasToAttach);
            }
            producto.setVentasCollection(attachedVentasCollection);
            Collection<Inventario> attachedInventarioCollection = new ArrayList<Inventario>();
            for (Inventario inventarioCollectionInventarioToAttach : producto.getInventarioCollection()) {
                inventarioCollectionInventarioToAttach = em.getReference(inventarioCollectionInventarioToAttach.getClass(), inventarioCollectionInventarioToAttach.getIdInventario());
                attachedInventarioCollection.add(inventarioCollectionInventarioToAttach);
            }
            producto.setInventarioCollection(attachedInventarioCollection);
            em.persist(producto);
            if (idCategoria != null) {
                idCategoria.getProductoCollection().add(producto);
                idCategoria = em.merge(idCategoria);
            }
            for (Compras comprasCollectionCompras : producto.getComprasCollection()) {
                Producto oldIdProductoOfComprasCollectionCompras = comprasCollectionCompras.getIdProducto();
                comprasCollectionCompras.setIdProducto(producto);
                comprasCollectionCompras = em.merge(comprasCollectionCompras);
                if (oldIdProductoOfComprasCollectionCompras != null) {
                    oldIdProductoOfComprasCollectionCompras.getComprasCollection().remove(comprasCollectionCompras);
                    oldIdProductoOfComprasCollectionCompras = em.merge(oldIdProductoOfComprasCollectionCompras);
                }
            }
            for (Ventas ventasCollectionVentas : producto.getVentasCollection()) {
                Producto oldIdProductoOfVentasCollectionVentas = ventasCollectionVentas.getIdProducto();
                ventasCollectionVentas.setIdProducto(producto);
                ventasCollectionVentas = em.merge(ventasCollectionVentas);
                if (oldIdProductoOfVentasCollectionVentas != null) {
                    oldIdProductoOfVentasCollectionVentas.getVentasCollection().remove(ventasCollectionVentas);
                    oldIdProductoOfVentasCollectionVentas = em.merge(oldIdProductoOfVentasCollectionVentas);
                }
            }
            for (Inventario inventarioCollectionInventario : producto.getInventarioCollection()) {
                Producto oldIdProductoOfInventarioCollectionInventario = inventarioCollectionInventario.getIdProducto();
                inventarioCollectionInventario.setIdProducto(producto);
                inventarioCollectionInventario = em.merge(inventarioCollectionInventario);
                if (oldIdProductoOfInventarioCollectionInventario != null) {
                    oldIdProductoOfInventarioCollectionInventario.getInventarioCollection().remove(inventarioCollectionInventario);
                    oldIdProductoOfInventarioCollectionInventario = em.merge(oldIdProductoOfInventarioCollectionInventario);
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

    public void edit(Producto producto) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Producto persistentProducto = em.find(Producto.class, producto.getIdProducto());
            Categoria idCategoriaOld = persistentProducto.getIdCategoria();
            Categoria idCategoriaNew = producto.getIdCategoria();
            Collection<Compras> comprasCollectionOld = persistentProducto.getComprasCollection();
            Collection<Compras> comprasCollectionNew = producto.getComprasCollection();
            Collection<Ventas> ventasCollectionOld = persistentProducto.getVentasCollection();
            Collection<Ventas> ventasCollectionNew = producto.getVentasCollection();
            Collection<Inventario> inventarioCollectionOld = persistentProducto.getInventarioCollection();
            Collection<Inventario> inventarioCollectionNew = producto.getInventarioCollection();
            List<String> illegalOrphanMessages = null;
            for (Compras comprasCollectionOldCompras : comprasCollectionOld) {
                if (!comprasCollectionNew.contains(comprasCollectionOldCompras)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Compras " + comprasCollectionOldCompras + " since its idProducto field is not nullable.");
                }
            }
            for (Ventas ventasCollectionOldVentas : ventasCollectionOld) {
                if (!ventasCollectionNew.contains(ventasCollectionOldVentas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ventas " + ventasCollectionOldVentas + " since its idProducto field is not nullable.");
                }
            }
            for (Inventario inventarioCollectionOldInventario : inventarioCollectionOld) {
                if (!inventarioCollectionNew.contains(inventarioCollectionOldInventario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Inventario " + inventarioCollectionOldInventario + " since its idProducto field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idCategoriaNew != null) {
                idCategoriaNew = em.getReference(idCategoriaNew.getClass(), idCategoriaNew.getIdCategoria());
                producto.setIdCategoria(idCategoriaNew);
            }
            Collection<Compras> attachedComprasCollectionNew = new ArrayList<Compras>();
            for (Compras comprasCollectionNewComprasToAttach : comprasCollectionNew) {
                comprasCollectionNewComprasToAttach = em.getReference(comprasCollectionNewComprasToAttach.getClass(), comprasCollectionNewComprasToAttach.getIdCompras());
                attachedComprasCollectionNew.add(comprasCollectionNewComprasToAttach);
            }
            comprasCollectionNew = attachedComprasCollectionNew;
            producto.setComprasCollection(comprasCollectionNew);
            Collection<Ventas> attachedVentasCollectionNew = new ArrayList<Ventas>();
            for (Ventas ventasCollectionNewVentasToAttach : ventasCollectionNew) {
                ventasCollectionNewVentasToAttach = em.getReference(ventasCollectionNewVentasToAttach.getClass(), ventasCollectionNewVentasToAttach.getIdVentas());
                attachedVentasCollectionNew.add(ventasCollectionNewVentasToAttach);
            }
            ventasCollectionNew = attachedVentasCollectionNew;
            producto.setVentasCollection(ventasCollectionNew);
            Collection<Inventario> attachedInventarioCollectionNew = new ArrayList<Inventario>();
            for (Inventario inventarioCollectionNewInventarioToAttach : inventarioCollectionNew) {
                inventarioCollectionNewInventarioToAttach = em.getReference(inventarioCollectionNewInventarioToAttach.getClass(), inventarioCollectionNewInventarioToAttach.getIdInventario());
                attachedInventarioCollectionNew.add(inventarioCollectionNewInventarioToAttach);
            }
            inventarioCollectionNew = attachedInventarioCollectionNew;
            producto.setInventarioCollection(inventarioCollectionNew);
            producto = em.merge(producto);
            if (idCategoriaOld != null && !idCategoriaOld.equals(idCategoriaNew)) {
                idCategoriaOld.getProductoCollection().remove(producto);
                idCategoriaOld = em.merge(idCategoriaOld);
            }
            if (idCategoriaNew != null && !idCategoriaNew.equals(idCategoriaOld)) {
                idCategoriaNew.getProductoCollection().add(producto);
                idCategoriaNew = em.merge(idCategoriaNew);
            }
            for (Compras comprasCollectionNewCompras : comprasCollectionNew) {
                if (!comprasCollectionOld.contains(comprasCollectionNewCompras)) {
                    Producto oldIdProductoOfComprasCollectionNewCompras = comprasCollectionNewCompras.getIdProducto();
                    comprasCollectionNewCompras.setIdProducto(producto);
                    comprasCollectionNewCompras = em.merge(comprasCollectionNewCompras);
                    if (oldIdProductoOfComprasCollectionNewCompras != null && !oldIdProductoOfComprasCollectionNewCompras.equals(producto)) {
                        oldIdProductoOfComprasCollectionNewCompras.getComprasCollection().remove(comprasCollectionNewCompras);
                        oldIdProductoOfComprasCollectionNewCompras = em.merge(oldIdProductoOfComprasCollectionNewCompras);
                    }
                }
            }
            for (Ventas ventasCollectionNewVentas : ventasCollectionNew) {
                if (!ventasCollectionOld.contains(ventasCollectionNewVentas)) {
                    Producto oldIdProductoOfVentasCollectionNewVentas = ventasCollectionNewVentas.getIdProducto();
                    ventasCollectionNewVentas.setIdProducto(producto);
                    ventasCollectionNewVentas = em.merge(ventasCollectionNewVentas);
                    if (oldIdProductoOfVentasCollectionNewVentas != null && !oldIdProductoOfVentasCollectionNewVentas.equals(producto)) {
                        oldIdProductoOfVentasCollectionNewVentas.getVentasCollection().remove(ventasCollectionNewVentas);
                        oldIdProductoOfVentasCollectionNewVentas = em.merge(oldIdProductoOfVentasCollectionNewVentas);
                    }
                }
            }
            for (Inventario inventarioCollectionNewInventario : inventarioCollectionNew) {
                if (!inventarioCollectionOld.contains(inventarioCollectionNewInventario)) {
                    Producto oldIdProductoOfInventarioCollectionNewInventario = inventarioCollectionNewInventario.getIdProducto();
                    inventarioCollectionNewInventario.setIdProducto(producto);
                    inventarioCollectionNewInventario = em.merge(inventarioCollectionNewInventario);
                    if (oldIdProductoOfInventarioCollectionNewInventario != null && !oldIdProductoOfInventarioCollectionNewInventario.equals(producto)) {
                        oldIdProductoOfInventarioCollectionNewInventario.getInventarioCollection().remove(inventarioCollectionNewInventario);
                        oldIdProductoOfInventarioCollectionNewInventario = em.merge(oldIdProductoOfInventarioCollectionNewInventario);
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
                Integer id = producto.getIdProducto();
                if (findProducto(id) == null) {
                    throw new NonexistentEntityException("The producto with id " + id + " no longer exists.");
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
            Producto producto;
            try {
                producto = em.getReference(Producto.class, id);
                producto.getIdProducto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The producto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Compras> comprasCollectionOrphanCheck = producto.getComprasCollection();
            for (Compras comprasCollectionOrphanCheckCompras : comprasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the Compras " + comprasCollectionOrphanCheckCompras + " in its comprasCollection field has a non-nullable idProducto field.");
            }
            Collection<Ventas> ventasCollectionOrphanCheck = producto.getVentasCollection();
            for (Ventas ventasCollectionOrphanCheckVentas : ventasCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the Ventas " + ventasCollectionOrphanCheckVentas + " in its ventasCollection field has a non-nullable idProducto field.");
            }
            Collection<Inventario> inventarioCollectionOrphanCheck = producto.getInventarioCollection();
            for (Inventario inventarioCollectionOrphanCheckInventario : inventarioCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the Inventario " + inventarioCollectionOrphanCheckInventario + " in its inventarioCollection field has a non-nullable idProducto field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Categoria idCategoria = producto.getIdCategoria();
            if (idCategoria != null) {
                idCategoria.getProductoCollection().remove(producto);
                idCategoria = em.merge(idCategoria);
            }
            em.remove(producto);
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

    public List<Producto> findProductoEntities() {
        return findProductoEntities(true, -1, -1);
    }

    public List<Producto> findProductoEntities(int maxResults, int firstResult) {
        return findProductoEntities(false, maxResults, firstResult);
    }

    private List<Producto> findProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Producto.class));
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

    public Producto findProducto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Producto> rt = cq.from(Producto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
