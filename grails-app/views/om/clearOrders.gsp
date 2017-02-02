<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta name="layout" content="main"/>
<title>Clear Orders</title>
</head>
<body>
    <div class="row-fluid">
        <div class="span7 offset1">
            <section class="widget">
                <header>
                    <h4>Clear Orders <small> Clear pending orders if the stock available after service is above the Reorder Level!</small></h4>
                </header>
                <div class="body">
                    <g:if test="${messages.size() > 0}"><p>&nbsp;</p></g:if>
                    <g:each var="message" in="${messages}"><p>${message}</p></g:each>
                    <g:if test="${orders.keySet().size() == 0}">
                    <p>&nbsp;</p>
                    <p>There are no pending orders that can be cleared.</p>
                    </g:if>
                    <g:else>
                    <g:each var="order_id" in="${orders.keySet()}">
                    <form action="" method="POST">
                    <legend class="section">Order ${order_id}, placed by ${orders[order_id][0].customer} on ${orders[order_id][0].order_date}</legend>
                    <div style="float: right;">
                        <g:if test="${orders[order_id][0].clearable}">
                        <button type="submit" class="btn btn-small btn-success">Clear Order</button>
                        </g:if>
                        <g:else>
                        <button type="reset" class="btn btn-small btn-warning disabled">Can't be cleared</button>
                        </g:else>
                    </div>
                    <table>
                        <g:each var="order" in="${orders[order_id]}">
                        <tr><td><strong>${order.product_name}</strong></td><td>&nbsp&nbsp; ${order.qty} unit<g:if test="${order.qty>1}">s</g:if></td></tr>
                        </g:each>
                    </table>
                    <input type="hidden" name="orderID" value="${order_id}"/>
                    </form>
                    </g:each>
                    </g:else>
                </div>
            </section>
        </div>
    </div>
</body>
</html>