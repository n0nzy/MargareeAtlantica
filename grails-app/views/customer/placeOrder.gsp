<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta name="layout" content="main"/>
<title>Place a new Order</title>
<script>
function doNext(docForm) {
	docForm.action = "${createLink(action: 'placeOrderCart')}";
	docForm.submit();
}
function doSelectProduct(docForm, selProductID) {
	docForm.action = "${createLink(action: 'placeOrder')}";
	docForm.elements["selProductID"].value = selProductID;
	docForm.submit();
}
function doRemoveProduct(docForm, remProductID) {
	docForm.action = "${createLink(action: 'placeOrder')}";
	docForm.elements["remProductID"].value = remProductID;
	docForm.submit();
}
</script>
</head>
<body>
    <div class="row-fluid">
        <div class="span7 offset1">
            <section class="widget">
                <header>
                    <h4><i class="icon-shopping-cart"></i> Place an Order <small>Choose from our wide variety of products and order right away!</small></h4>
                </header>
                <div class="body">
                    <ul class="row-fluid wizard-navigation nav nav-pills">
                        <li class="span4 active"><a class="" href="#"><small>1.</small><strong> Choose Products</strong></a></li>
                        <li class="span4"><a class="" href="#"><small>2.</small> <strong>View Shopping Cart</strong></a></li>
                        <li class="span4"><a class="" href="#"><small>3.</small> <strong>Complete Order</strong></a></li>
                    </ul>
                    <div id="bar" class="progress progress-inverse progress-small">
                        <div class="bar" style="width: 30%;"></div>
                    </div>
                    <div class="tab-content">
                        <div class="tab-pane active" id="tabMain">
                            <form id="poForm" name="poForm" class="form-horizontal form-condensed" action="" method="POST">
                                <table class="table table-hover table-condensed">
                                    <thead>
                                        <tr>
                                            <th width="40%">Name</th>
                                            <th width="15%">Cost</th>
                                            <th width="20%">Warranty (months)</th>
                                            <th width="10%">In Stock?</th>
                                            <th width="15%" style="text-align: right"></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <g:if test="${categoriesAndProducts['count'] <= 0}">
                                        <tr><td colspan="5"><p>&nbsp;</p><p>Sorry, there are no products to choose from. Do you want to <a href="${createLink(action: 'proposeProduct')}">Propose a Product</a>?</p></td></tr>
                                        </g:if>
                                        <g:else>
                                        <g:each var="category_id" in="${categoriesAndProducts.keySet()}">
                                        <g:if test="${category_id != 'count'}">
                                        <tr><td colspan="5"><legend class="section">${categoriesAndProducts[category_id][0].category}</legend></td></tr>
                                        <g:each var="product" in="${categoriesAndProducts[category_id]}">
                                        <tr>
                                            <td width="40%">${product.name}</td>
                                            <td width="15%">$${product.cost}</td>
                                            <td width="20%">
                                                <g:if test="${product.warrantyPeriod <= 0}">
                                                None
                                                </g:if>
                                                <g:else>
                                                ${product.warrantyPeriod}
                                                </g:else>
                                            </td>
                                            <td width="10%">
                                                <g:if test="${product.stock > 0}">
                                                <span class="badge badge-success">Yes</span>
                                                </g:if>
                                                <g:else>
                                                <span class="badge badge-important">No</span>
                                                </g:else>
                                            </td>
                                            <td width="15%" style="text-align: right">
                                                <g:if test="${product.stock <= 0}">
                                                <button class="disabled btn btn-small">Select</button>
                                                </g:if>
                                                <g:elseif test="${product.selected == true}">
                                                <button class="btn btn-small btn-warning" onclick="doRemoveProduct(document.poForm,${product.id})">Remove</button>
                                                </g:elseif>
                                                <g:elseif test="${product.selected == false}">
                                                <button class="btn btn-small btn-success" onclick="doSelectProduct(document.poForm,${product.id})">Select</button>
                                                </g:elseif>
                                            </td>
                                        </tr>
                                        </g:each>
                                        </g:if>
                                        </g:each>
                                        </g:else>
                                    </tbody>
                                </table>
                                <input type="hidden" id="cartProducts" name="cartProducts" value="${cartProductsString}"/>
                                <input type="hidden" id="selProductID" name="selProductID" value=""/>
                                <input type="hidden" id="remProductID" name="remProductID" value=""/>
                            </form>
                        </div>
                        <p>&nbsp;</p>
                        <div class="description">
                            <ul class="pager wizard">
                                <li class="previous disabled">
                                    <button class="btn btn-primary pull-left"><i class="icon-caret-left"></i> Previous</button>
                                </li>
                                <li class="next <g:if test="${cartProducts['count'] <= 0}">disabled</g:if>">
                                    <button class="btn btn-primary pull-right" <g:if test="${cartProducts['count'] > 0}">onclick="doNext(document.poForm)"</g:if> >Next <i class="icon-caret-right"></i></button>
                                </li>
                                <!-- li class="finish" style="display: none;">
                                    <button class="btn btn-success pull-right">Submit Order <i class="icon-ok"></i></button>
                                </li -->
                            </ul>
                        </div>
                    </div>
                </div>
            </section>
        </div>
        <div class="span3 offset1">
            <section class="widget">
                <header>
                    <h4><i class="icon-tags"></i> Selected Products</h4>
                </header>
                <div class="body">
                    <p>You can adjust the quantity of the selected products in the Shopping Cart later.</p>
                    <p>&nbsp;</p>
                    <g:if test="${cartProducts['count'] <= 0}">
                    <p>You haven't selected any products...</p>
                    </g:if>
                    <g:else>
                    <g:each var="product_id" in="${cartProducts.keySet()}">
                    <g:if test="${product_id.toString().equals('count') == false}">
                    <p><strong>${cartProducts[product_id].name}</strong> (${(cartProducts[product_id].quantity)})</p>
                    </g:if>
                    </g:each>
                    </g:else>
                </div>
            </section>
        </div>
    </div>
</body>
</html>