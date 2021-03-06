/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.seguridad;

import forms.seguridad.UsuariosOpForm;
import forms.seguridad.UsuariosForm;
import forms.bean.seguridad.BeanUsuarios;
import forms.InicioForm;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import util.ConeccionMySql;

/**
 *
 * @author Mario
 */
public class GestionUsuarios extends ConeccionMySql {

    Connection cn = null;
    Statement st = null;

    public ArrayList<Object> IngresaUsuarios(UsuariosForm f, Boolean transac, Connection tCn) {

        int mod = -99;
        ArrayList<Object> resultado = new ArrayList<Object>();
        PreparedStatement psInsertar = null;

        try {

            if (transac == false) { //si no es una transaccion busca una nueva conexion

                ArrayList<Object> resultad = new ArrayList<Object>();
                resultad = (ArrayList) getConection();

                if ((Boolean) resultad.get(0) == false) { // si no hubo error al obtener la conexion

                    cn = (Connection) resultad.get(1);

                } else { //si hubo error al obtener la conexion retorna el error para visualizar

                    resultado.add(true);
                    resultado.add(resultad.get(1));
                    return resultado;

                }

            } else { //si es una transaccion asigna la conexion utilizada

                cn = tCn;

            }
            psInsertar = cn.prepareStatement("insert into susuario     (id, login, password, sroles_id, id_tipo_documento, identificacion, susuarios_id, fecha_modificacion) values (null,?,AES_ENCRYPT(?,'mundoodnum'),?,?,?,?,now())", PreparedStatement.RETURN_GENERATED_KEYS);
            psInsertar.setString(1, f.getLogin());
            psInsertar.setString(2, f.getPassword());
            psInsertar.setInt(3, f.getIdRol());
            psInsertar.setInt(4, f.getIdTipoDocumento());
            psInsertar.setInt(5, f.getIdentificacion());
            psInsertar.setInt(6, f.getIdUsu());
            psInsertar.executeUpdate(); // Se ejecuta la inserción.

            // Se obtiene la clave generada
            int claveGenerada = -1;
            ResultSet rs = psInsertar.getGeneratedKeys();
            while (rs.next()) {
                claveGenerada = rs.getInt(1);
            }
            mod = psInsertar.getUpdateCount();

            if (transac == false) { // si no es una transaccion cierra la conexion

                cn.close();

            }

            resultado.add(false); //si no hubo un error asigna false
            resultado.add(claveGenerada); // clave generada
            resultado.add(mod); // y el numero de registros consultados

        } catch (Exception e) {

            resultado.add(true); //si hubo error asigna true
            resultado.add(e); //y asigna el error para retornar y visualizar

            if (cn != null) {
                cn.rollback();
                cn.close();
            }

        } finally {

            return resultado;

        }

    }
    private ArrayList<Object> GR_USUARIO;
    private String descCargo = "";
    SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM/dd");

    public ArrayList<Object> BuscarUsuarios(InicioForm fo, Boolean transac, Connection tCn) {

        ArrayList<Object> resultado = new ArrayList<Object>();
        BeanUsuarios bu;
        bu = new BeanUsuarios();
        PreparedStatement psSelectConClave = null;

        try {

            if (transac == false) { //si no es una transaccion busca una nueva conexion

                ArrayList<Object> resultad = new ArrayList<Object>();
                resultad = (ArrayList) getConection();

                if ((Boolean) resultad.get(0) == false) { // si no hubo error al obtener la conexion

                    cn = (Connection) resultad.get(1);

                } else { //si hubo error al obtener la conexion retorna el error para visualizar

                    resultado.add(true);
                    resultado.add(resultad.get(1));
                    return resultado;

                }

            } else { //si es una transaccion asigna la conexion utilizada

                cn = tCn;

            }

            psSelectConClave = cn.prepareStatement("SELECT p.id, p.login, p.sroles_id, p.id_tipo_documento, p.identificacion, r.id, r.nombre, e.id_tipo_documento, e.identificacion, IF(e.primer_nombre <> NULL AND e.primer_apellido <> NULL, e.razon_social, CONCAT(IF(e.primer_nombre <> NULL,'',CONCAT(e.primer_nombre,' ')), IF(e.segundo_nombre <> NULL,'',CONCAT(e.segundo_nombre,' ')), IF(e.primer_apellido <> NULL,'',CONCAT(e.primer_apellido,' ')), IF(e.segundo_apellido <> NULL,'',CONCAT(e.segundo_apellido,' ')))) as nombreE FROM susuarios p INNER JOIN sroles r ON p.sroles_id = r.id INNER JOIN entidades e ON p.id_tipo_documento = e.id_tipo_documento AND p.identificacion = e.identificacion WHERE p.login = ? AND p.password = AES_ENCRYPT(?, 'mundoodnum')");
            psSelectConClave.setString(1, fo.getUsuario());
            psSelectConClave.setString(2, fo.getPassw());
            ResultSet rs = psSelectConClave.executeQuery();

            while (rs.next()) {
                bu = new BeanUsuarios();

                bu.setIdUsuario(rs.getObject("p.id"));
                bu.setIdRol(rs.getObject("p.sroles_id"));
                bu.setIdTipoDocumento(rs.getObject("p.id_tipo_documento"));
                bu.setIdentificacion(rs.getObject("p.identificacion"));
                bu.setLogin(rs.getObject("p.login"));
                bu.setNombre(rs.getObject("nombreE"));
                bu.setNombreRol(rs.getObject("r.nombre"));

            }

            if (transac == false) { // si no es una transaccion cierra la conexion

                cn.close();

            }

            resultado.add(false); //si no hubo un error asigna false
            resultado.add(bu); // y registros consultados

        } catch (Exception e) {

            resultado.add(true); //si hubo error asigna true
            resultado.add(e); //y asigna el error para retornar y visualizar

            if (cn != null) {
                cn.rollback();
                cn.close();
            }

        } finally {

            return resultado;

        }

    }

    public ArrayList<Object> BuscarUsuarios(String login, Boolean transac, Connection tCn) {

        ArrayList<Object> resultado = new ArrayList<Object>();
        BeanUsuarios bu;
        bu = new BeanUsuarios();
        boolean encontro = false;
        PreparedStatement psSelectConClave = null;

        try {

            if (transac == false) { //si no es una transaccion busca una nueva conexion

                ArrayList<Object> resultad = new ArrayList<Object>();
                resultad = (ArrayList) getConection();

                if ((Boolean) resultad.get(0) == false) { // si no hubo error al obtener la conexion

                    cn = (Connection) resultad.get(1);

                } else { //si hubo error al obtener la conexion retorna el error para visualizar

                    resultado.add(true);
                    resultado.add(resultad.get(1));
                    return resultado;

                }

            } else { //si es una transaccion asigna la conexion utilizada

                cn = tCn;

            }

            psSelectConClave = cn.prepareStatement("SELECT p.login FROM susuario p WHERE p.login = ?");
            psSelectConClave.setString(1, login);
            ResultSet rs = psSelectConClave.executeQuery();

            while (rs.next()) {
                bu = new BeanUsuarios();

                bu.setLogin(rs.getObject("p.login"));
                String p = (String) bu.getLogin();
                if (p.equals(login)) {
                    encontro = true;
                }


            }

            if (transac == false) { // si no es una transaccion cierra la conexion

                cn.close();

            }

            resultado.add(false); //si no hubo un error asigna false
            resultado.add(encontro); // y registros consultados

        } catch (Exception e) {

            resultado.add(true); //si hubo error asigna true
            resultado.add(e); //y asigna el error para retornar y visualizar

            if (cn != null) {
                cn.rollback();
                cn.close();
            }

        } finally {

            return resultado;

        }

    }

    public ArrayList<Object> MostrarUsuarios(UsuariosOpForm f, Boolean transac, Connection tCn) {

        ArrayList<Object> resultado = new ArrayList<Object>();
        PreparedStatement psSelectConClave = null;

        try {
            GR_USUARIO = new ArrayList<Object>();

            if (transac == false) { //si no es una transaccion busca una nueva conexion

                ArrayList<Object> resultad = new ArrayList<Object>();
                resultad = (ArrayList) getConection();

                if ((Boolean) resultad.get(0) == false) { // si no hubo error al obtener la conexion

                    cn = (Connection) resultad.get(1);

                } else { //si hubo error al obtener la conexion retorna el error para visualizar

                    resultado.add(true);
                    resultado.add(resultad.get(1));
                    return resultado;

                }

            } else { //si es una transaccion asigna la conexion utilizada

                cn = tCn;

            }

            String query = "SELECT p.id, p.login, p.sroles_id, p.id_tipo_documento, p.identificacion, r.nombre ";
            query += "FROM susuario p INNER JOIN sroles r ON p.roles_id = r.id";
            String query2 = "";
            if (f.getbLogin().isEmpty() != true) {
                query2 = "p.login LIKE CONCAT('%',?,'%')";
            }
            if (f.getbIdRol().isEmpty() != true) {
                if (query2.isEmpty() != true) {
                    query2 += "AND ";
                }
                query2 += "p.roles_id LIKE CONCAT('%',?,'%')";
            }
            if (f.getbIdTipoDocumento().isEmpty() != true) {
                if (query2.isEmpty() != true) {
                    query2 += "AND ";
                }
                query2 += "p.id_tipo_documento LIKE CONCAT('%',?,'%')";
            }
            if (f.getbIdentificacion().isEmpty() != true) {
                if (query2.isEmpty() != true) {
                    query2 += "AND ";
                }
                query2 += "p.identificacion LIKE CONCAT('%',?,'%')";
            }
            if (query2.isEmpty() != true) {
                query += " WHERE " + query2;
            }
            psSelectConClave = cn.prepareStatement(query);
            if (f.getbLogin().isEmpty() != true) {
                psSelectConClave.setString(1, f.getbLogin());
                if (f.getbIdRol().isEmpty() != true) {
                    psSelectConClave.setString(2, f.getbIdRol());
                    if (f.getbIdTipoDocumento().isEmpty() != true) {
                        psSelectConClave.setString(3, f.getbIdTipoDocumento());
                        if (f.getbIdentificacion().isEmpty() != true) {
                            psSelectConClave.setString(4, f.getbIdentificacion());
                        }
                    } else {
                        if (f.getbIdentificacion().isEmpty() != true) {
                            psSelectConClave.setString(3, f.getbIdentificacion());
                        }
                    }
                } else {
                    if (f.getbIdTipoDocumento().isEmpty() != true) {
                        psSelectConClave.setString(2, f.getbIdTipoDocumento());
                        if (f.getbIdentificacion().isEmpty() != true) {
                            psSelectConClave.setString(3, f.getbIdentificacion());
                        }
                    } else {
                        if (f.getbIdentificacion().isEmpty() != true) {
                            psSelectConClave.setString(2, f.getbIdentificacion());
                        }
                    }
                }
            } else {
                if (f.getbIdRol().isEmpty() != true) {
                    psSelectConClave.setString(1, f.getbIdRol());
                    if (f.getbIdTipoDocumento().isEmpty() != true) {
                        psSelectConClave.setString(2, f.getbIdTipoDocumento());
                        if (f.getbIdentificacion().isEmpty() != true) {
                            psSelectConClave.setString(3, f.getbIdentificacion());
                        }
                    } else {
                        if (f.getbIdentificacion().isEmpty() != true) {
                            psSelectConClave.setString(2, f.getbIdentificacion());
                        }
                    }
                } else {
                    if (f.getbIdTipoDocumento().isEmpty() != true) {
                        psSelectConClave.setString(1, f.getbIdTipoDocumento());
                        if (f.getbIdentificacion().isEmpty() != true) {
                            psSelectConClave.setString(2, f.getbIdentificacion());
                        }
                    } else {
                        if (f.getbIdentificacion().isEmpty() != true) {
                            psSelectConClave.setString(1, f.getbIdentificacion());
                        }
                    }
                }
            }
            ResultSet rs = psSelectConClave.executeQuery();

            BeanUsuarios bu;
            while (rs.next()) {
                bu = new BeanUsuarios();

                bu.setIdUsuario(rs.getObject("p.id"));
                bu.setLogin(rs.getObject("p.login"));
                bu.setIdRol(rs.getObject("p.roles_id"));
                bu.setIdentificacion(rs.getObject("p.identificacion"));
                bu.setNombre(rs.getObject("r.nombre"));

                GR_USUARIO.add(bu);


            }

            if (transac == false) { // si no es una transaccion cierra la conexion

                cn.close();

            }

            resultado.add(false); //si no hubo un error asigna false
            resultado.add(GR_USUARIO); // y registros consultados

        } catch (Exception e) {

            resultado.add(true); //si hubo error asigna true
            resultado.add(e); //y asigna el error para retornar y visualizar

            if (cn != null) {
                cn.rollback();
                cn.close();
            }

        } finally {

            return resultado;

        }

    }

    public ArrayList<Object> ModificaUsuarios(UsuariosForm f, Boolean transac, Connection tCn) {

        int mod = -99;
        ArrayList<Object> resultado = new ArrayList<Object>();
        PreparedStatement psUpdate = null;

        try {

            if (transac == false) { //si no es una transaccion busca una nueva conexion

                ArrayList<Object> resultad = new ArrayList<Object>();
                resultad = (ArrayList) getConection();

                if ((Boolean) resultad.get(0) == false) { // si no hubo error al obtener la conexion

                    cn = (Connection) resultad.get(1);

                } else { //si hubo error al obtener la conexion retorna el error para visualizar

                    resultado.add(true);
                    resultado.add(resultad.get(1));
                    return resultado;

                }

            } else { //si es una transaccion asigna la conexion utilizada

                cn = tCn;

            }

            String query = "UPDATE susuario SET login = ?";
            if (f.getActPassword() != null) {
                if (f.getActPassword().equals("on")) {
                    query += ", password= AES_ENCRYPT(?, 'mundoodnum')";
                }
            }
            query += ", roles_id =?";
            query += ", id_tipo_documento =?";
            query += ", identificacion=?";
            query += ", susuarios_id=?";
            query += ", fecha_modificacion=now()";
            query += " WHERE id=?";
            psUpdate = cn.prepareStatement(query);
            psUpdate.setString(1, f.getLogin());
            boolean oo = false;
            if (f.getActPassword() != null) {
                if (f.getActPassword().equals("on")) {
                    psUpdate.setString(2, f.getPassword());
                    psUpdate.setInt(3, f.getIdRol());
                    psUpdate.setInt(4, f.getIdTipoDocumento());
                    psUpdate.setInt(5, f.getIdentificacion());
                    psUpdate.setInt(6, f.getIdUsu());
                    psUpdate.setInt(7, f.getIdUsuario());
                    oo = true;
                }
            }
            if (oo == false) {
                psUpdate.setInt(2, f.getIdRol());
                psUpdate.setInt(3, f.getIdTipoDocumento());
                psUpdate.setInt(4, f.getIdentificacion());
                psUpdate.setInt(5, f.getIdUsuario());
                psUpdate.setInt(6, f.getIdUsu());
            }
            psUpdate.executeUpdate();

            mod = psUpdate.getUpdateCount();

            if (transac == false) { // si no es una transaccion cierra la conexion

                cn.close();

            }

            resultado.add(false); //si no hubo un error asigna false
            resultado.add(mod); // y el numero de registros consultados

        } catch (Exception e) {

            resultado.add(true); //si hubo error asigna true
            resultado.add(e); //y asigna el error para retornar y visualizar

            if (cn != null) {
                cn.rollback();
                cn.close();
            }

        } finally {

            return resultado;

        }

    }

    public ArrayList<Object> EliminaUsuarios(UsuariosForm f, Boolean transac, Connection tCn) {

        int mod = -99;
        ArrayList<Object> resultado = new ArrayList<Object>();
        PreparedStatement psDelete = null;

        try {

            if (transac == false) { //si no es una transaccion busca una nueva conexion

                ArrayList<Object> resultad = new ArrayList<Object>();
                resultad = (ArrayList) getConection();

                if ((Boolean) resultad.get(0) == false) { // si no hubo error al obtener la conexion

                    cn = (Connection) resultad.get(1);

                } else { //si hubo error al obtener la conexion retorna el error para visualizar

                    resultado.add(true);
                    resultado.add(resultad.get(1));
                    return resultado;

                }

            } else { //si es una transaccion asigna la conexion utilizada

                cn = tCn;

            }

            psDelete = cn.prepareStatement("DELETE FROM susuario WHERE  id = ?");
            psDelete.setInt(1, f.getIdUsuario());
            psDelete.executeUpdate();

            mod = psDelete.getUpdateCount();

            if (transac == false) { // si no es una transaccion cierra la conexion

                cn.close();

            }

            resultado.add(false); //si no hubo un error asigna false
            resultado.add(mod); // y el numero de registros consultados

        } catch (Exception e) {

            resultado.add(true); //si hubo error asigna true
            resultado.add(e); //y asigna el error para retornar y visualizar

            if (cn != null) {
                cn.rollback();
                cn.close();
            }

        } finally {

            return resultado;

        }

    }

    public ArrayList<Object> MostrarUsuarioFormulario(int IdUsuario, Boolean transac, Connection tCn) {

        ArrayList<Object> resultado = new ArrayList<Object>();
        PreparedStatement psSelectConClave = null;

        try {

            if (transac == false) { //si no es una transaccion busca una nueva conexion

                ArrayList<Object> resultad = new ArrayList<Object>();
                resultad = (ArrayList) getConection();

                if ((Boolean) resultad.get(0) == false) { // si no hubo error al obtener la conexion

                    cn = (Connection) resultad.get(1);

                } else { //si hubo error al obtener la conexion retorna el error para visualizar

                    resultado.add(true);
                    resultado.add(resultad.get(1));
                    return resultado;

                }

            } else { //si es una transaccion asigna la conexion utilizada

                cn = tCn;

            }

            psSelectConClave = cn.prepareStatement("SELECT p.id, p.login, AES_DECRYPT(p.password,'mundoodnum') password, p.roles_id, p.id_tipo_documento, p.identificacion, p.susuarios_id, p.fecha_modificacion FROM susuario p WHERE  p.id =?");
            psSelectConClave.setInt(1, IdUsuario);
            ResultSet rs = psSelectConClave.executeQuery();

            BeanUsuarios bu;
            while (rs.next()) {
                bu = new BeanUsuarios();

                setIdUsuario(rs.getObject("p.id"));
                setLogin(rs.getObject("p.login"));
                setPassword(rs.getObject("password"));
                setIdRol(rs.getObject("p.roles_id"));
                setIdTipoDocumento(rs.getObject("p.id_tipo_documento"));
                setIdentificacion(rs.getObject("p.identificacion"));
                setIdUsuM(rs.getObject("p.susuario_id"));
                setFechaModificacion(rs.getObject("p.fecha_modificacion"));

            }

            if (transac == false) { // si no es una transaccion cierra la conexion

                cn.close();

            }

            resultado.add(false); //si no hubo un error asigna false

        } catch (Exception e) {

            resultado.add(true); //si hubo error asigna true
            resultado.add(e); //y asigna el error para retornar y visualizar

            if (cn != null) {
                cn.rollback();
                cn.close();
            }

        } finally {

            return resultado;

        }

    }

    public ArrayList<Object> commint(Connection tCn) {

        ArrayList<Object> resultado = new ArrayList<Object>();

        try {

            tCn.commit();
            resultado.add(false); //si no hubo un error asigna false

        } catch (Exception e) {

            resultado.add(true); //si hubo error asigna true
            resultado.add(e); //y asigna el error para retornar y visualizar

            if (cn != null) {
                cn.rollback();
                cn.close();
            }

        } finally {

            return resultado;

        }

    }

    public ArrayList<Object> autoCommint(boolean valor, Connection tCn) {

        ArrayList<Object> resultado = new ArrayList<Object>();

        try {

            tCn.setAutoCommit(valor);
            resultado.add(false); //si no hubo un error asigna false

        } catch (Exception e) {

            resultado.add(true); //si hubo error asigna true
            resultado.add(e); //y asigna el error para retornar y visualizar

        } finally {

            return resultado;

        }

    }

    public ArrayList<Object> rollback(Connection tCn) {

        ArrayList<Object> resultado = new ArrayList<Object>();

        try {

            tCn.rollback();
            resultado.add(false); //si no hubo un error asigna false

        } catch (Exception e) {

            resultado.add(true); //si hubo error asigna true
            resultado.add(e); //y asigna el error para retornar y visualizar

        } finally {

            return resultado;

        }

    }

    public ArrayList<Object> ObtenerConexion() {

        ArrayList<Object> resultado = new ArrayList<Object>();

        try {

            ArrayList<Object> resultad = new ArrayList<Object>();
            resultad = (ArrayList) getConection();

            if ((Boolean) resultad.get(0) == false) { // si no hubo error al obtener la conexion

                cn = (Connection) resultad.get(1);
                resultado.add(false); //si no hubo un error asigna false
                resultado.add(cn); // y se envia la nueva conexion

            } else { //si hubo error al obtener la conexion retorna el error para visualizar

                resultado.add(true);
                resultado.add(resultad.get(1));
                return resultado;

            }

        } catch (Exception e) {

            resultado.add(true); //si hubo error asigna true
            resultado.add(e); //y asigna el error para retornar y visualizar

        } finally {

            return resultado;

        }

    }
//    private ArrayList<Object> GR_USUARIOS2;
//
//    public ArrayList<Object> MostrarUsuarios2(String aux, String aux2) {
//        try {
//            GR_USUARIOS2 = new ArrayList<Object>();
//            cn = getConection();
//
//            String query = "SELECT p.idUsuarios, p.tipoDocumentacion, p.documento, p.nombre1, p.nombre2, p.apellido1, ";
//            query += "p.apellido2, p.foto ";
//            query += "FROM usuarios p, cargo c ";
//            query += "WHERE p.id_cargo = c.id_cargo ";
//            if (aux.equals("nombre")) {
//                query += " AND p.nombres = '" + aux2 + "'";
//            } else if (aux.equals("cargo")) {
//                query += " AND p.id_cargo='" + aux2 + "'";
//            }
//
//            System.out.println("***********************************************");
//            System.out.println("*****       Cargando grilla  GR_USUARIOS  *****");
//            System.out.println("***********************************************");
//
//            System.out.println(query);
//            st = cn.createStatement();
//            ResultSet rs = st.executeQuery(query);
//
//            BeanUsuarios bu;
//            while (rs.next()) {
//                bu = new BeanUsuarios();
//
//                bu.setIdUsuarios(rs.getObject("p.idUsuarios"));
//                bu.setTipoDocumentacion(rs.getObject("p.tipoDocumentacion"));
//                bu.setDocumento(rs.getObject("p.documento"));
//                bu.setNombre1(rs.getObject("p.nombre1"));
//                bu.setNombre2(rs.getObject("p.nombre2"));
//                bu.setApellido1(rs.getObject("p.apellido1"));
//                bu.setApellido2(rs.getObject("p.apellido2"));
//                bu.setFoto(rs.getObject("p.foto"));
//
//                GR_USUARIOS2.add(bu);
//
//            }
//
//            st.close();
//            cn.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//        }
//        return GR_USUARIOS2;
//    }
    private Object idUsuario;
    private Object login;
    private Object password;
    private Object idTipoDocumento;
    private Object identificacion;
    private Object idRol;
    private Object idUsuM;
    private Object fechaModificacion;

    public Object getIdUsuM() {
        return idUsuM;
    }

    public void setIdUsuM(Object idUsuM) {
        this.idUsuM = idUsuM;
    }

    public Object getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Object fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    /**
     * @return the idUsuario
     */
    public Object getIdUsuario() {
        return idUsuario;
    }

    /**
     * @param idUsuario the idUsuarios to set
     */
    public void setIdUsuario(Object idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Object getLogin() {
        return login;
    }

    public void setLogin(Object login) {
        this.login = login;
    }

    public Object getPassword() {
        return password;
    }

    public void setPassword(Object password) {
        this.password = password;
    }

    public Object getIdRol() {
        return idRol;
    }

    public void setIdRol(Object idRol) {
        this.idRol = idRol;
    }

    public Object getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(Object idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public Object getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(Object identificacion) {
        this.identificacion = identificacion;
    }
}
