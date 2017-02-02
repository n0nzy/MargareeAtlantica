<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta name="layout" content="main"/>
<title>Place a new Order</title>
<script>
<g:if test="${cartProducts['count'] > 0}">
var cartProductsStock = {
	<g:each var="product_id" in="${cartProducts.keySet()}">
	<g:if test="${product_id != 'count'}">
	'${product_id}':${cartProducts[product_id].stock},
	</g:if>
	</g:each>
};
</g:if>
<g:else>
var cartProductsStock = null;
</g:else>

function isNumeric(num) {
	return !isNaN(num)
}

function doPrevious(docForm) {
	docForm.action = "${createLink(action: 'placeOrder')}";
	docForm.submit();
}
function doFinish(docForm) {
	docForm.action = "${createLink(action: 'placeOrderComplete')}";
	docForm.submit();
}
function doUpdate(docForm) {
	var cpstr = docForm.elements["cartProducts"].value;
	var cps = cpstr.split(",");
	var ncpstr = "";
	var pID = 0, pQty = 0;
	var pfQty, pfMaxQty;
	for (var i = 0; i < cps.length; i++) {
		if (cps[i].length > 2) {
			if (cps[i].indexOf(":") > -1) {
				pID = cps[i].substring(0, cps[i].indexOf(":"));
				pQty = parseInt(cps[i].substring(cps[i].indexOf(":") + 1, cps[i].length));
				pfQty = docForm.elements["p" + pID + "quantity"].value;
				pfMaxQty = cartProductsStock[pID];
				if (isNumeric(pfQty) && parseInt(pfQty) >= 0) {
					if (parseInt(pfQty) > pfMaxQty) {
						window.alert("Quantity is greater than availability of this product!");
						return false;
					} else {
						pQty = parseInt(pfQty);
					}
				} else {
					window.alert("Invalid Quantity given!");
					return false;
				}
				if (pQty > 0) {
					if (ncpstr == "") {
						ncpstr = pID + ":" + pQty;
					} else {
						ncpstr += "," + pID + ":" + pQty;
					}
				}
			}
		}
	}
	
	docForm.action = "${createLink(action: 'placeOrderCart')}";
	docForm.elements["cartProducts"].value = ncpstr;
	docForm.submit();
}
function doRemoveProduct(docForm, productID) {
	var cpstr = docForm.elements["cartProducts"].value;
	var cps = cpstr.split(",");
	var ncpstr = "";
	for (var i = 0; i < cps.length; i++) {
		if (cps[i].length > 2) {
			if (cps[i].indexOf(":") > -1 && cps[i].substring(0, cps[i].indexOf(":")) != productID) {
				if (ncpstr == "") {
					ncpstr = cps[i];
				} else {
					ncpstr += "," + cps[i];
				}
			}
		}
	}
	docForm.action = "${createLink(action: 'placeOrderCart')}";
	docForm.elements["cartProducts"].value = ncpstr;
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
                        <li class="span4"><a class="" href="#"><small>1.</small><strong> Choose Products</strong></a></li>
                        <li class="span4 active"><a class="" href="#"><small>2.</small> <strong>View Shopping Cart</strong></a></li>
                        <li class="span4"><a class="" href="#"><small>3.</small> <strong>Complete Order</strong></a></li>
                    </ul>
                    <div id="bar" class="progress progress-inverse progress-small">
                        <div class="bar" style="width: 60%;"></div>
                    </div>
                    <div class="tab-content">
                        <form id="cart" name="cart" class="form-horizontal form-condensed" action="" method="POST">
                            <fieldset>
                            <div class="tab-pane active" id="tabMain">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th width="30%">Name</th>
                                            <th width="25%">Category</th>
                                            <th width="15%">Quantity</th>
                                            <th width="15%">Unit Cost</th>
                                            <th width="15%" style="text-align: right"></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <g:if test="${cartProducts['count'] <= 0}">
                                        <tr><td colspan="5"><p>&nbsp;</p><p>You haven't selected any product, go back and select a product first.</p></td></tr>
                                        </g:if>
                                        <g:else>
                                        <g:each var="product_id" in="${cartProducts.keySet()}">
                                        <g:if test="${product_id != 'count'}">
                                        <tr>
                                            <td width="30%">${cartProducts[product_id].name}</td>
                                            <td width="25%">${cartProducts[product_id].category}</td>
                                            <td width="15%">
                                                <input type="number" style="width:50px" data-range="[1, ${cartProducts[product_id].stock}]" data-trigger="change" data-validation-minlength="1" id="p${product_id}quantity" name="p${product_id}quantity" value="${cartProducts[product_id].quantity}" required="required" />
                                            </td>
                                            <td width="15%">$${cartProducts[product_id].cost}</td>
                                            <td width="15%" style="text-align: right"><button class="btn btn-small btn-warning" onclick="doRemoveProduct(document.cart,'${product_id}')">Remove</button></td>
                                        </tr>
                                        </g:if>
                                        </g:each>
                                        </g:else>
                                    </tbody>
                                </table>
                            </div>
                            <div class="description">
                                <ul class="pager wizard">
                                    <li class="previous">
                                        <button class="btn btn-primary pull-left" onclick="doPrevious(document.cart)"><i class="icon-caret-left"></i> Previous</button>
                                    </li>
                                    <li class="next <g:if test="${cartProducts['count'] <= 0}">disabled</g:if>">
                                        <button class="btn btn-primary" <g:if test="${cartProducts['count'] > 0}">onclick="doUpdate(document.cart)"</g:if> >Update Cart</button>
                                    </li>
                                    <li class="finish <g:if test="${cartProducts['count'] <= 0}">disabled</g:if>">
                                        <button class="btn btn-success pull-right" <g:if test="${cartProducts['count'] > 0}">onclick="doFinish(document.cart)"</g:if> >Complete Order <i class="icon-ok"></i></button>
                                    </li>
                                </ul>
                            </div>
                        </fieldset>
                        <input type="hidden" id="cartProducts" name="cartProducts" value="${cartProductsString}"/>
                        <g:if test="${offer}"><input type="hidden" id="offer" name="offer" value="${offer.id}:${offer.discount}"/></g:if>
                        <g:else><input type="hidden" id="offer" name="offer" value=""/></g:else>
                        <input type="hidden" id="orderSummary" name="orderSummary" value="${orderSummary.totalAmount}:${orderSummary.discountAmount}:${orderSummary.totalPaid}"/>
                        </form>
                    </div>
                </div>
            </section>
        </div>
        <div class="span3 offset1">
            <section class="widget">
                <header>
                    <h4><i class="icon-tags"></i> Order Review</h4>
                </header>
                <div class="body">
                    <g:if test="${cartProducts['count'] <= 0}">
                    <p>&nbsp;</p><p>No products in cart...</p><p>&nbsp;</p>
                    </g:if>
                    <g:else>
                    <p>Review order totals (please Update Cart first if you have made any changes):</p>
                    <p>&nbsp;</p>
                    <g:each var="product_id" in="${cartProducts.keySet()}">
                    <g:if test="${product_id != 'count'}">
                    <p><span>${cartProducts[product_id].name}</span><span style="float:right">$${cartProducts[product_id].quantity * cartProducts[product_id].cost}</span></p>
                    </g:if>
                    </g:each>
                    <p><span>Product Total:</span><span style="float:right">$${orderSummary.totalAmount}</span></p>
                    <g:if test="${offer}">
                    <p><span><strong>Discount Offer:</strong></span><span style="float:right">${offer.description}</span></p>
                    <p><span><strong>Discount Amount:</strong></span><span style="float:right"><strong>$${orderSummary.discountAmount} (${offer.discount}%)</strong></span></p>
                    </g:if>
                    <p><span><strong>Order Total:</strong></span><span style="float:right"><strong>$${orderSummary.totalPaid}</strong></span></p>
                    </g:else>
                </div>
            </section>
        </div>
    </div>
</body>
</html>