<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta name="layout" content="main"/>
<title>Propose a Product</title>
</head>
<body>
    <div class="row-fluid">
        <ma:pageTitle title="Propose Product" subTitle="Tell us which product should we add to our store"/>
    </div>
    <div class="row-fluid">
        <div class="span8">
            <section class="widget">
                <header>
                    <g:if test="${proposeStatus == 0}">
                    <h4><i class="icon-gift"></i> Choose a product</h4>
                    </g:if>
                    <g:else>
                    <h4><i class="icon-thumbs-up"></i> Proposal Successful</h4>
                    </g:else>
                </header>
                <div class="body">
                    <g:if test="${proposeStatus == 0}">
                    <g:if test="${categoriesAndProducts.isEmpty()}">
                    <p>&nbsp;</p>
                    <p>There are no products that can be proposed at this time.</p>
                    </g:if>
                    <g:else>
                    <p>&nbsp;</p>
                    <form class="form-horizontal label-left" action="${createLink(action:'proposeProduct')}" method="post" novalidate="novalidate" data-validate="parsley">
                        <fieldset>
                            <div class="control-group">
                                <label for="productID" class="control-label">Select Product</label>
                                <div class="controls">
                                    <select id="productID" required="required" data-placeholder="Select Product" class="span7 chzn-select" name="productID">
                                        <option value="" />
                                        <g:each var="category_id" in="${categoriesAndProducts.keySet()}">
                                        <option value="">----- ${categoriesAndProducts[category_id][0].categoryName} -----</strong></option>
                                        <g:each var="product" in="${categoriesAndProducts[category_id]}">
                                        <option value="${product.id}">${product.name}</option>
                                        </g:each>
                                        </g:each>
                                    </select>
                                </div>
                            </div>
                        </fieldset>
                        <div class="form-actions">
                            <button class="btn btn-primary" type="submit">Propose</button>
                        </div>
                    </form>
                    </g:else>
                    </g:if>
                    <g:elseif test="${proposeStatus == 1}">
                    <p>We have received your product proposal and will be reviewing it soon.</p>
                    <p>&nbsp;</p>
                    <p><strong>Thank You!</strong></p>
                    <div class="form-actions">
                        <button class="btn btn-primary" onclick="location.href='${createLink(action:'index')}'">Continue</button>
                    </div>
                    </g:elseif>
                    <g:elseif test="${proposeStatus == 2}">
                    <p>You're not the first one proposing this product, which means we'll try harder to get it in our inventory soon. Your vote has been counted.</p>
                    <p>&nbsp;</p>
                    <p><strong>Thank You!</strong></p>
                    <div class="form-actions">
                        <button class="btn btn-primary" onclick="location.href='${createLink(action:'index')}'">Continue</button>
                    </div>
                    </g:elseif>
                    <g:elseif test="${proposeStatus == 3}">
                    <p>You have already proposed this product.</p>
                    <div class="form-actions">
                        <button class="btn btn-primary" onclick="location.href='${createLink(action:'index')}'">Continue</button>
                    </div>
                    </g:elseif>
                </div>
            </section>
        </div>
        <div class="span4">
            <section class="widget">
                <header>
                    <h4><i class="icon-lightbulb"></i> Information</h4>
                </header>
                <div class="body">
                    <g:if test="${proposeStatus == 0}">
                    <p>Select a product from the drop-down box and click <strong>Propose</strong> button.</p>
                    <p>&nbsp;</p>
                    <p>Having several customers propose the same product increases it's chances of approval and ending up in our inventory sooner.</p>
                    </g:if>
                    <g:elseif test="${proposeStatus == 1}">
                    <p>Your proposal has been submitted for review.</p>
                    </g:elseif>
                    <g:elseif test="${proposeStatus == 2}">
                    <p>Somebody already proposed this product and it's under review.</p>
                    </g:elseif>
                    <g:elseif test="${proposeStatus == 3}">
                    <p>You have already proposed this product and your proposal is under review.</p>
                    </g:elseif>
                </div>
            </section>
        </div>
    </div>
</body>
</html>