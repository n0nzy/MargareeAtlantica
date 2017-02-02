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
                    <h4>Procure Product <small> Procure products to maintain the reorder level!</small></h4>
                </header>
                <div class="body">
                    <p>&nbsp;</p>
                    <g:each var="message" in="${messages}"><p>${message}</p></g:each>
                    <g:if test="${!procured}">
                    <form class="form-horizontal label-left" action="" method="post" novalidate="novalidate" data-validate="parsley">
                        <fieldset>
                            <div class="control-group">
                                <label class="control-label" for="name">Product</label>
                                <div class="controls controls-row">
                                    <select id="productID" data-placeholder="Select Product to procure" required="required" class="span7 chzn-select" name="productID">
                                        <option value="" />
                                        <g:each var="category_id" in="${categoriesAndProducts.keySet()}">
                                        <option value="">----- ${categoriesAndProducts[category_id][0].category} -----</strong></option>
                                        <g:each var="product" in="${categoriesAndProducts[category_id]}">
                                        <option value="${product.id}" <g:if test="${product.id.toString().equals(params['productID'])}">selected</g:if> >${product.name}</option>
                                        </g:each>
                                        </g:each>
                                    </select>
                                </div>
                            </div>
                            <div class="control-group">
                                <label class="control-label" for="name">Quantity</label>
                                <div class="controls controls-row">
                                    <input class="span7" type="number" id="quantity" name="quantity" data-trigger="change" required="required" value="${params['quantity']}" />
                                </div>
                            </div>
                            <div class="form-actions">
                                <button type="submit" class="btn btn-success">Procure Product</button>
                                <button type="button" class="btn">Cancel</button>
                            </div>
                        </fieldset>
                        <input type="hidden" name="submitted" id="submitted" value="true"/>
                    </form>
                    </g:if>
                </div>
            </section>
        </div>
    </div>
</body>
</html>