<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta name="layout" content="main"/>
<title>Welcome to e-Retail Order Management System</title>
</head>
<body>
    <div class="row-fluid">
        <ma:greetUser name="${user.name}"/>
    </div>
    <div class="row-fluid box-row">
        <div class="span2">
            <div class="box">
                <div class="icon"><i class="icon-shopping-cart"></i></div>
                <div class="description">
                    <strong>${totalOrders}</strong> orders placed
                </div>
            </div>
            <div class="box">
                <div class="icon"><i class="icon-tags"></i></div>
                <div class="description">
                    <strong>${totalProposedProducts}</strong> products proposed
                </div>
            </div>
        </div>
        <div class="span6">
            <section class="widget">
                <header>
                    <h4><i class="icon-reorder"></i> Most Recent Order <small>details of the last order your placed</small></h4>
                </header>
                <div class="body">
                    <g:if test="${latestOrder.isEmpty()}">
                    <p>&nbsp;</p>
                    <p>You haven't ordered anything recently.</p>
                    <p>&nbsp;</p>
                    </g:if>
                    <g:else>
                    <legend class="section">Products purchased</legend>
                    <g:each var="product" in="${latestOrder['products']}">
                    <p><strong>${product['name']}</strong> (<strong>${product['quantity']}</strong> items)</p>
                    </g:each>
                    <legend class="section">Totals and information</legend>
                    <table width="100%">
                        <tr><td width="50%">Order Status:</td><td width="50%" align="right">${latestOrder['status']}</td></tr>
                        <tr><td width="50%">Order Date:</td><td width="50%" align="right">${latestOrder['order_date']}</td></tr>
                        <tr><td width="50%">Shipping Address:</td><td width="50%" align="right">${latestOrder['shipping_address']}</td></tr>
                        <tr><td width="50%">Product Total:</td><td width="50%" align="right">${latestOrder['total_amount']}</td></tr>
                        <g:if test="${latestOrder['amount_paid'] < latestOrder['total_amount']}">
                        <tr><td width="50%">Discount Amount:</td><td width="50%" align="right">${latestOrder['total_amount'] - latestOrder['amount_paid']}</td></tr>
                        </g:if>
                        <tr><td width="50%">Amount Paid:</td><td width="50%" align="right">${latestOrder['amount_paid']}</td></tr>
                    </table>
                    </g:else>
                </div>
            </section>
        </div>
    </div>
</body>
</html>