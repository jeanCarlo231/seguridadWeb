/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.clases;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author jaker
 */
@Named(value = "login")
@SessionScoped
public class login implements Serializable {

    private String usuario, contrasenia;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String entrar() {

        if (usuario.equals("almacen") && contrasenia.equals("almacen")) {
            return "/paginas/almacen/List";
        } else if (usuario.equals("categoria") && contrasenia.equals("categoria")) {
            return "/paginas/categoria/List";
        } else if (usuario.equals("cliente") && contrasenia.equals("cliente")) {
            return "/paginas/cliente/List";
        } else if (usuario.equals("compras") && contrasenia.equals("compras")) {
            return "/paginas/compras/List";
        } else if (usuario.equals("contablidad") && contrasenia.equals("contabilidad")) {
            return "/paginas/contabilidad/List";
        } else if (usuario.equals("banco") && contrasenia.equals("banco")) {
            return "/paginas/cuentaBancaria/List";
        } else if (usuario.equals("empleado") && contrasenia.equals("empleado")) {
            return "/paginas/empleado/List";
        } else if (usuario.equals("facturacion") && contrasenia.equals("facturacion")) {
            return "/paginas/facturacion/List";
        } else if (usuario.equals("inventario") && contrasenia.equals("inventario")) {
            return "/paginas/inventario/List";
        } else if (usuario.equals("pago") && contrasenia.equals("pago")) {
            return "/paginas/pago/List";
        } else if (usuario.equals("producto") && contrasenia.equals("producto")) {
            return "/paginas/producto/List";
        } else if (usuario.equals("proveedor") && contrasenia.equals("proveedor")) {
            return "/paginas/proveedor/List";
        } else if (usuario.equals("rol") && contrasenia.equals("rol")) {
            return "/paginas/rol/List";
        } else if (usuario.equals("usuario") && contrasenia.equals("usuario")) {
            return "/paginas/usuario/List";
        } else if (usuario.equals("ventas") && contrasenia.equals("ventas")) {
            return "/paginas/ventas/List";
        }

        return "login.xhtml";
    }

    public login() {

    }

}
