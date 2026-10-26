<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Datos del crédito</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/estilos.css">
    </head>
    <body>
        <main>
            <header>Financiera Huanca <span>Formulario de crédito</span></header>
            <section>
                <h1>Datos del crédito</h1>
                <p>Datos del crédito guardado.</p>
                <table>
                    <tbody>
                        <tr><th scope="row">Fecha</th><td><c:out value="${credito.fecha}"/></td></tr>
                        <tr><th scope="row">Moneda</th><td>${credito.moneda eq 'PEN' ? 'Soles (S/.)' : 'Dólares (US$)'}</td></tr>
                        <tr><th scope="row">Monto</th><td><c:out value="${credito.monto}"/></td></tr>
                        <tr><th scope="row">Periodo (meses)</th><td><c:out value="${credito.periodo}"/></td></tr>
                        <tr><th scope="row">Cuota</th><td><c:out value="${credito.cuota}"/></td></tr>
                        <tr><th scope="row">TEA (%)</th><td><c:out value="${credito.tea}"/></td></tr>
                        <tr><th scope="row">Fecha vencimiento</th><td><c:out value="${credito.vencimiento}"/></td></tr>
                    </tbody>
                </table>
<h2>Pagos de tarjetas de crédito</h2>
                    <p>Pagos actuales guardados en MySQL. Esta tabla es independiente de las solicitudes de crédito.</p>
                    <div class="tabla-scroll">
                    <table class="pagos">
                        <thead><tr><th scope="col">Fecha</th><th scope="col">Mes</th><th scope="col">Tarjeta</th><th scope="col">Transacción</th><th scope="col">Gastos</th><th scope="col">Pagos</th></tr></thead>
                        <tbody>
<c:forEach items="${pagos}" var="pago"><tr><td><c:out value="${pago.fecha}"/></td>
                                <td><c:out value="${pago.mes}"/></td>
                                <td><c:out value="${pago.tarjeta}"/></td>
                                <td><c:out value="${pago.transaccion}"/></td>
                                <td class="importe"><c:out value="${pago.gastos}"/></td>
<td class="importe"><c:out value="${pago.pago}"/></td></tr></c:forEach>
                        </tbody></table></div>
                <a class="boton" href="${pageContext.request.contextPath}/credito">Capturar otro crédito</a>
            </section>
            <footer>Tarea 3 · Tecnologías para Desarrollo en Internet</footer>
        </main>
    </body>
</html>
