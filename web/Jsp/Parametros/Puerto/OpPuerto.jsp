<%-- 
    Document   : OpPuerto
    Created on : 18-septiembre-2012, 09:44:34
    Author     : Gilberth
--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri= "/WEB-INF/c.tld" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html:html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" href="css/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="css/comun.css" rel="stylesheet" />
        <link rel="stylesheet" type="text/css" media="screen" href="css/ui.jqgrid.css" />
        <script src="Js/jquery-1.7.2.min.js" type="text/javascript"></script>
        <script src="Js/i18n/grid.locale-es.js" type="text/javascript"></script>
        <script src="Js/jquery.jqGrid.min.js" type="text/javascript"></script>
        <title>Opciones de Puertos</title>
        <%
            String usuario = "";
            HttpSession sesionOk = request.getSession();
            if (sesionOk.getAttribute("usuario") == null) {
        %>
        <jsp:forward page="/index.jsp">
            <jsp:param name="mensaje" value="Es obligatorio identificarse"/>
        </jsp:forward>
        <%    } else {
                usuario = String.valueOf(sesionOk.getAttribute("usuario"));
            }
        %>
        <%
            if (request.getAttribute("getOp") == "buscar") {
        %>
        <jsp:forward page="/OpPuerto.do">
            <jsp:param name="getOp" value="buscar"/>
        </jsp:forward>
        <%            }
        %>
        <script type="text/javascript">
            $(document).ready(function(){
                anchoPantalla = document.body.offsetWidth - 50;
            })

            $(function(){ 
                jQuery("#list4").jqGrid({
                    url:'Jsp/Parametros/Puerto/getGriddahico.jsp?op=bus',
                    datatype: "json",
                    colNames:['ID', 'Nombre Corto', 'Descripcion', 'Pais', 'Departamento', 'Municipio', 'Sucursal', 'Editar'],
                    colModel:[
                        {name:'idPuerto',index:'idPuerto', width:50, sortable:false},
                        {name:'nombreCorto',index:'nombreCorto', width:160, sortable:false},
                        {name:'descripcion',index:'descripcion', width:160, sortable:false},
                        {name:'pais',index:'pais', width:160, sortable:false},
                        {name:'departamento',index:'departamento', width:160, sortable:false},
                        {name:'municipio',index:'municipio', width:160, sortable:false},
                        {name:'sucursal',index:'sucursal', width:160, sortable:false},
                        {name:'editar',index:'editar', width:110, formatter:'showlink', sortable:false}
                    ],
                    pager: '#prowed1',
                    width: anchoPantalla,
                    height: "100%",
                    rowNum:10,
                    viewrecords: true,
                    caption: "Lista de Puertos"
                }); 
                jQuery("#list4").jqGrid('navGrid',"#prowed1",{edit:false,add:false,del:false,search:false});
                $("#bIdPais").change(function(){
                    $.post("Jsp/Comun/getDepartamentoOp.jsp",{ 
                        id:$(this).val() 
                    },
                    function(data){
                        $("#bIdDepartamento").html(data);
                        $.post("Jsp/Comun/getMunicipioOp.jsp",{
                            id:document.forms[0].bIdDepartamento.value,
                            id2:document.forms[0].bIdPais.value 
                        },
                        function(data){
                            $("#bIdMunicipio").html(data);
                        })
                    })
                });
                $("#bIdDepartamento").change(function(){
                    $.post("Jsp/Comun/getMunicipioOp.jsp",{
                        id:$(this).val(), 
                        id2:$("#bIdPais").val() 
                    },
                    function(data){
                        $("#bIdMunicipio").html(data);
                    })
                });
            }); 

            function buscar(){
                document.forms[0].op.value="buscar";
                document.forms[0].id.value="";
                document.forms[0].submit();
            }
            
            function modifica(id){
                document.forms[0].op.value="modificar";
                document.forms[0].id.value=id;
                document.forms[0].submit();
            }

            function nuevo(){
                document.forms[0].op.value="nuevo";
                document.forms[0].id.value="";
                document.forms[0].submit();
            }
            
            function historico(){
                var emer = window.open('../Estados/Jsp/Log/Auditoria/Auditoria2.jsp?getOp=buscar&accion=eliminadas&formulario=puerto&num=1','Auditoria','width=950,height=500,top=100%,left=100%,scrollbars=yes,resizable=yes');
                emer.focus();
            }
        </script>

    </head>
    <body  bgcolor="#EFFBFB">
        <html:form action="/OpPuerto.do" method="post">
            <input type="hidden" name="op" value=""> 
            <input type="hidden" name="id" value=""> 
            <fieldset>
                <legend>Consulta de Puertos</legend>
                <table>
                    <tr>
                        <td>Nombre Corto<input type="text" name="bNombreCorto" value="<%= session.getAttribute("getbNombreCorto")%>"/> </td>
                        <td>Descripcion<input type="text" name="bDescripcion" value="<%= session.getAttribute("getbDescripcion")%>"/> </td>
                        <td colspan="2"><a class="boton" href="javascript:buscar()">Buscar</a> <a class="boton" href="javascript:nuevo()">Nuevo</a> <a class="boton" href="javascript:historico()">Historico Eliminados</a></td>
                    </tr>
                    <tr>
                        <td>Pais<html:select property="bIdPais" styleId="bIdPais" size="1" style="width:240px;" value='<%= String.valueOf(session.getAttribute("getbIdPais"))%>'>
                                <html:option value=""><c:out value='[Todos]'/></html:option>    
                                <c:forEach items="${CMB_PAIS}" var="cat">
                                    <html:option value="${cat.idPais}"><c:out value='${cat.nombre}'/></html:option>
                                </c:forEach>
                            </html:select>
                        </td>
                        <td>Departamento<html:select property="bIdDepartamento" styleId="bIdDepartamento" size="1" style="width:240px;" value='<%= String.valueOf(session.getAttribute("getbIdDepartamento"))%>'>
                                <html:option value=""><c:out value='[Todos]'/></html:option>    
                                <c:forEach items="${CMB_DEPARTAMENTO}" var="cat">
                                    <html:option value="${cat.idDepartamento}"><c:out value='${cat.nombre}'/></html:option>
                                </c:forEach>
                            </html:select>
                        </td>
                        <td>Municipio<html:select property="bIdMunicipio" styleId="bIdMunicipio" size="1" style="width:240px;" value='<%= String.valueOf(session.getAttribute("getbIdMunicipio"))%>'>
                                <html:option value=""><c:out value='[Todos]'/></html:option>    
                                <c:forEach items="${CMB_MUNICIPIO}" var="cat">
                                    <html:option value="${cat.idMunicipio}"><c:out value='${cat.nombre}'/></html:option>
                                </c:forEach>
                            </html:select>
                        </td>
                        <td>Sucursal<html:select property="bIdSucursal" styleId="bIdSucursal" size="1" style="width:240px;" value='<%= String.valueOf(session.getAttribute("getbIdSucursal"))%>'>
                                <html:option value=""><c:out value='[Todos]'/></html:option>    
                                <c:forEach items="${CMB_SUCURSAL}" var="cat">
                                    <html:option value="${cat.idSucursal}"><c:out value='${cat.descripcion}'/></html:option>
                                </c:forEach>
                            </html:select>
                        </td>
                    </tr>
                </table>
            </fieldset>
            <fieldset>
                <legend>Listado de Puertos</legend>
                <table>
                    <tr>
                        <td><table id="list4"></table></td>
                        <td><div id="prowed1"></div></td>
                    </tr>
                </table>
            </fieldset>
        </html:form>
    </body>
</html:html>
