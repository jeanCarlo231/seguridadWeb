/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans.sessions;

import entidades.Compras;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Saul
 */
@Stateless
public class ComprasFacade extends AbstractFacade<Compras> {

    @PersistenceContext(unitName = "com.mycompany_Proveedores_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ComprasFacade() {
        super(Compras.class);
    }
    
}
