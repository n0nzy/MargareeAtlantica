<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta name="layout" content="main"/>
<title>Place a new Order</title>
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
                        <li class="span4"><a class="" href="#"><small>2.</small> <strong>View Shopping Cart</strong></a></li>
                        <li class="span4 active"><a class="" href="#"><small>3.</small> <strong>Complete Order</strong></a></li>
                    </ul>
                    <div id="bar" class="progress progress-inverse progress-small">
                        <div class="bar" style="width: 100%;"></div>
                    </div>
                    <div class="tab-content">
                        <div class="tab-pane active" id="tabMain">
                            <p>&nbsp;</p>
                            <g:if test="${errors.size > 0}">
                            <g:each var="error" in="${errors}">
                            <p><strong>${error}</strong></p>
                            </g:each>
                            </g:if>
                            <g:else>
                            <p><strong>Order completed successfully!</strong></p>
                            <p>Your Order Number is <strong>${orderSummary.id}</strong>, please save it for further communication with us.</p>
                            <p>&nbsp;</p>
                            <p>Thank you for your order.</p>
                            <ul class="pager wizard">
                                <li class="finish">
                                    <button class="btn btn-success pull-right" onclick="location.href='${createLink(action:'index')}'">Continue <i class="icon-caret-right"></i></button>
                                </li>
                            </ul>
                            </g:else>
                        </div>
                    </div>
                </div>
            </section>
        </div>
        <div class="span3 offset1">
            <section class="widget">
                <header>
                    <h4><i class="icon-tags"></i> Order Summary</h4>
                </header>
                <div class="body">
                    <p>&nbsp;</p>
                    <g:if test="${errors.size > 0}">
                    <p>No summary available due to errors in the order.</p>
                    </g:if>
                    <g:else>
                    <p><span>Product Total:</span><span style="float:right">$${orderSummary.totalAmount}</span></p>
                    <g:if test="${offer}">
                    <p><span><strong>Discount Amount:</strong></span><span style="float:right"><strong>$${orderSummary.discountAmount} (${offer.discount}%)</strong></span></p>
                    </g:if>
                    <p><span><strong>Total Paid:</strong></span><span style="float:right"><strong>$${orderSummary.totalPaid}</strong></span></p>
                    </g:else>
                </div>
            </section>
        </div>
    </div>
</body>
</html>