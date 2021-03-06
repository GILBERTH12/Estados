<%-- 
    Document   : getGriddahico
    Created on : 4/02/2010, 11:00:58 PM
    Author     : @dahico.
    Blog:[URL="http://dahicotux.wordpress.com"]http://dahicotux.wordpress.com[/URL]
--%>

<%@page import="forms.bean.BeanPlantillaDispositivo"%>
<%@page import="java.lang.Object"%>
<%@page import="java.util.ArrayList"%>
<%@page import="forms.bean.BeanPlantillaDispositivoHija"%>
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
    int start = 0;
    int total_pages = 0;
    String op = request.getParameter("op");
    if (op != null) {
        if (op.equals("bus")) {
            ArrayList<Object> GR_AUT = (ArrayList) session.getAttribute("GR_PlantillaHija");
            int intpage = new Integer(request.getParameter("page"));
            int limit = new Integer(request.getParameter("rows"));

            String sidx = request.getParameter("sidx");
            String sord = request.getParameter("sord");

            /*
             * -----------------------------------
             */

            BeanPlantillaDispositivoHija buPlantillaDispositivoHija2;

            /*
             * -----------------------------------
             */
            String json = "";

            boolean rc;

            if (sidx == "") {
                sidx = "1";
            }

            if (GR_AUT.size() > 0) {
                double d = Math.ceil((double) (GR_AUT.size()) / (double) (limit));
                total_pages = (int) (d);
            } else {
                total_pages = 0;
            }

            if (intpage > total_pages) {
                intpage = total_pages;
            }

            start = limit * intpage - limit;

            if (start < 0) {
                start = 0;
            }

            //strQuery = "SELECT * FROM tbl_Usuario ORDER BY " + sidx + " " + sord + " LIMIT " + start + " , " + limit;

            /*
             * Ac� viene la parte donde creamos la cadena para el JSON
             * manualmente
             */

            response.setContentType("text/x-json");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache, must-revalidate");
            response.setHeader("Pragma", "no-cache");

            json = "";
            json = json + "{\n";
            json = json + " \"page\":\"" + intpage + "\",\n";
            json = json + "\"total\":" + total_pages + ",\n";
            json = json + "\"records\":" + GR_AUT.size() + ",\n";
            json = json + "\"rows\": [";
            rc = false;

            int maximo = start + limit;
            if (maximo > GR_AUT.size()) {
                maximo = GR_AUT.size();
            }

            for (int i = start; i < maximo; i++) {

                if (rc) {
                    json = json + ",";
                }

                buPlantillaDispositivoHija2 = new BeanPlantillaDispositivoHija();
                buPlantillaDispositivoHija2 = (BeanPlantillaDispositivoHija) GR_AUT.get(i);

                json = json + "\n{";
                json = json + "\"id\":\"" + i + "\",";
                json = json + "\"cell\":[" + buPlantillaDispositivoHija2.getIdPlantillaDispositivo() + "";
                json = json + "," + buPlantillaDispositivoHija2.getIdPlantillaDispositivoHija() + "";
                json = json + ",\"" + buPlantillaDispositivoHija2.getNombre() + "\"]";
                json = json + "}";

                rc = true;
            }

            json = json + "]\n";

            json = json + "}";

            out.print(json);
        } else {
            if (op.equals("del")) {
                int id = Integer.parseInt(String.valueOf(request.getParameter("id")));
                ArrayList<Object> GR_PlantillaDisponible = (ArrayList) session.getAttribute("GR_PlantillaDisponible");
                ArrayList<Object> GR_PlantillaHija = (ArrayList) session.getAttribute("GR_PlantillaHija");
                ArrayList<Object> GR_EliminadosHija = (ArrayList) session.getAttribute("GR_EliminadosHija");
                BeanPlantillaDispositivo buPlantillaDispositivo = new BeanPlantillaDispositivo();
                BeanPlantillaDispositivoHija buPlantillaDispositivoHija = new BeanPlantillaDispositivoHija();
                BeanPlantillaDispositivoHija buPlantillaDispositivoHija2 = new BeanPlantillaDispositivoHija();
                buPlantillaDispositivoHija = (BeanPlantillaDispositivoHija) GR_PlantillaHija.get(id);
                GR_PlantillaHija.remove(id);
                buPlantillaDispositivo.setIdPlantillaDispositivo(buPlantillaDispositivoHija.getIdPlantillaDispositivoHija());
                buPlantillaDispositivo.setNombre(buPlantillaDispositivoHija.getNombre());
                GR_PlantillaDisponible.add(buPlantillaDispositivo);
                if (GR_EliminadosHija == null) {
                    GR_EliminadosHija = new ArrayList<Object>();
                    buPlantillaDispositivoHija2.setIdPlantillaDispositivoHija(buPlantillaDispositivoHija.getIdPlantillaDispositivoHija());
                    buPlantillaDispositivoHija2.setNombre(buPlantillaDispositivoHija.getNombre());
                    GR_EliminadosHija.add(buPlantillaDispositivoHija2);
                }
                session.setAttribute("GR_PlantillaDisponible", GR_PlantillaDisponible);
                session.setAttribute("GR_PlantillaHija", GR_PlantillaHija);
                session.setAttribute("GR_EliminadosHija", GR_EliminadosHija);
            }
        }
    } else {
        /*
         * String oper = request.getParameter("oper"); if (oper != null) { if
         * (oper.equals("add")) {
         *
         * ArrayList<Object> GR_AUT = (ArrayList)
         * session.getAttribute("GR_CARACTERISTICAPLANTILLA");
         * BeanCaracteristicaPlantilla buCaracteristicaPlantilla2;
         * buCaracteristicaPlantilla2 = new BeanCaracteristicaPlantilla();
         * buCaracteristicaPlantilla2.setNombre(request.getParameter("nombre"));
         * buCaracteristicaPlantilla2.setHabilitar(request.getParameter("habilitar"));
         * buCaracteristicaPlantilla2.setObligatorio(request.getParameter("obligatorio"));
         * GR_AUT.add(buCaracteristicaPlantilla2);
         * session.setAttribute("GR_CARACTERISTICAPLANTILLA", GR_AUT);
         *
         * } else if (oper.equals("edit")) {
         *
         * int id =
         * Integer.parseInt(String.valueOf(request.getParameter("id")));
         * ArrayList<Object> GR_AUT = (ArrayList)
         * session.getAttribute("GR_CARACTERISTICAPLANTILLA");
         * BeanCaracteristicaPlantilla buCaracteristicaPlantilla2;
         * buCaracteristicaPlantilla2 = (BeanCaracteristicaPlantilla)
         * GR_AUT.get(id);
         * buCaracteristicaPlantilla2.setNombre(request.getParameter("nombre"));
         * buCaracteristicaPlantilla2.setHabilitar(request.getParameter("habilitar"));
         * buCaracteristicaPlantilla2.setObligatorio(request.getParameter("obligatorio"));
         * GR_AUT.set(id, buCaracteristicaPlantilla2);
         * session.setAttribute("GR_CARACTERISTICAPLANTILLA", GR_AUT);
         *
         * } else if (oper.equals("del")) {
         *
         * int id =
         * Integer.parseInt(String.valueOf(request.getParameter("id")));
         * ArrayList<Object> GR_AUT = (ArrayList)
         * session.getAttribute("GR_CARACTERISTICAPLANTILLA"); ArrayList<Object>
         * GR_Eliminados = (ArrayList) session.getAttribute("GR_Eliminados"); if
         * (GR_Eliminados == null) { GR_Eliminados = new ArrayList<Object>(); }
         * GR_Eliminados.add((BeanCaracteristicaPlantilla) GR_AUT.get(id));
         * GR_AUT.remove(id); session.setAttribute("GR_CARACTERISTICAPLANTILLA",
         * GR_AUT); session.setAttribute("GR_Eliminados", GR_Eliminados);
         *
         * }
         * }
         */
    }

%>