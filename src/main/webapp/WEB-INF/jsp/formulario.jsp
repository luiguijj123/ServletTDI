<%-- 
    Document   : formulario
    Created on : 24 set. 2026, 09:55:06
    Author     : luiguicruzsandiga
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Formulario de crédito</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos.css">
    </head>
    <body>
        <main>
            <header>Financiera Huanca <span>Formulario de crédito</span></header>
            <section>
                <p class="paso">PRIMERA VISTA</p>
                <h1>Datos del crédito</h1>
                <p>Completa los campos y pulsa ACEPTAR para enviarlos.</p>
                <c:if test="${not empty error}">
                    <p class="error" role="alert"><c:out value="${error}"/></p>
                </c:if>
                <form action="${pageContext.request.contextPath}/credito" method="post">
                    <div class="campos">
                        <label for="fecha">Fecha
                            <input id="fecha" name="fecha" value="<c:out value='${credito.fecha}'/>" type="date" min="1000-01-01" max="9999-12-31" required>
                        </label>
                        <fieldset>
                            <legend>Moneda</legend>
                            <label class="opcion"><input type="radio" name="moneda" value="PEN" ${credito.moneda eq "PEN" ? "checked" : ""} required> Soles (S/.)</label>
                            <label class="opcion"><input type="radio" name="moneda" value="USD" ${credito.moneda eq "USD" ? "checked" : ""} required> Dólares (US$)</label>
                        </fieldset>
                        <label for="monto">Monto
                            <input id="monto" name="monto" value="<c:out value='${credito.monto}'/>" type="number" min="0" step="0.01" required>
                        </label>
                        <label for="periodo">Periodo (meses)
                            <input id="periodo" name="periodo" value="<c:out value='${credito.periodo}'/>" type="number" min="1" step="1" required>
                        </label>
                        <label for="cuota">Cuota
                            <input id="cuota" name="cuota" value="<c:out value='${credito.cuota}'/>" type="number" min="0" step="0.01" required>
                        </label>
                        <label for="tea">TEA (%)
                            <input id="tea" name="tea" value="<c:out value='${credito.tea}'/>" type="number" min="0" step="0.01" required>
                        </label>
                        <label for="vencimiento">Fecha vencimiento
                            <input id="vencimiento" name="vencimiento" value="<c:out value='${credito.vencimiento}'/>" type="date" min="1000-01-01" max="9999-12-31" required>
                        </label>
                    </div>
                    <h2>Pagos con tarjetas de crédito</h2>
                    <p>Registre abajo los pagos realizados de la tarjeta</p>
                    <div class="tabla-scroll">
                    <table class="pagos">
                        <thead><tr><th scope="col" style="text-align: center;">FECHA</th><th scope="col" style="text-align: center;">MES</th><th scope="col" style="text-align: center;">TARJETA</th><th scope="col" style="text-align: center;">TRANSACCIÓN</th><th scope="col" style="text-align: center;">GASTOS</th><th scope="col" style="text-align: center;">PAGOS</th></tr></thead>
                        <tbody>
<c:forEach items="${pagoForm.pagos}" var="pago" varStatus="fila">
                            <tr><td><c:out value="${pago.fecha}"/></td>
                                <td><c:out value="${pago.mes}"/></td>
                                <td><c:out value="${pago.tarjeta}"/></td>
                                <td><c:out value="${pago.transaccion}"/></td>
                                <td class="importe"><c:out value="${pago.gastos}"/></td>
<td><input type="hidden" name="pagos[${fila.index}].id" value="${pago.id}">
                                <input aria-label="Pago de ${pago.tarjeta}, fila ${fila.count}" name="pagos[${fila.index}].pago" value="<c:out value='${pago.pago}'/>" type="number" min="0" max="9999999999999.99" step="0.01"></td></tr>
                        </c:forEach>
                        </tbody>
                    </table></div>
                    <c:if test="${empty pagoForm.pagos}"><p class="error">No hay filas de pagos disponibles.</p></c:if>
<button type="submit" ${empty pagoForm.pagos ? "disabled" : ""}>ACEPTAR</button>
                </form>
            </section>
            <footer>Tarea 3 · Tecnologías para Desarrollo en Internet</footer>
        </main>
    </body>
</html>
