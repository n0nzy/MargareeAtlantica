<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta name="layout" content="main"/>
<title>Retire Product</title>
</head>
<body>
    <div class="row-fluid">
        <ma:pageTitle title="Retire Product" subTitle="Suggested Products to be retired"/>
    </div>
    <div class="row-fluid">
        <div class="span8">
            <section class="widget">
                <header>
                	
                    <g:if test="${updateStatus == 0}">
                    	<!-- updateStatus -> 0 means that no product has been selected for update -->
                    	<h4><i class="icon-gift"></i> Choose a product to retire </h4>
                    </g:if>
                    <g:elseif test="${updateStatus == 1}">
                    	<!-- updateStatus -> 1 means that a product has been selected and updated successfully -->
                    	<h4><i class="icon-thumbs-up"></i> Product Retire Action - Successful</h4>
                    </g:elseif>
                    <g:else>
                    	<!-- updateStatus -> 2 means that a product has been selected, but was not updated successfully -->
                    	<h4><i class="icon-thumbs-up"></i> Product Retire Action - Not Successful</h4>
                    </g:else>
                </header>
                <div class="body">
                    <g:if test="${updateStatus == 0}">
                    <g:if test="${categoriesAndProducts.isEmpty()}">
                    <p>&nbsp;</p>
                    <p>There are no products that can be retired at this time.</p>
                    </g:if>
                    <g:else>
                    <p>&nbsp;</p>
                    <form class="form-horizontal label-left" action="${createLink(action:'retireProduct')}" method="post" novalidate="novalidate" data-validate="parsley">
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
                            <button class="btn btn-primary" type="submit">Retire</button>
                        </div>
                    </form>
                    </g:else>
                    </g:if>
                    <g:elseif test="${updateStatus == 1}">
                    <p> Product has been retired! </p>
                    <p>&nbsp;</p>
                    <p><strong>Thank You!</strong></p>
                    <div class="form-actions">
                        <button class="btn btn-primary" onclick="location.href='${createLink(action:'index')}'">Continue</button>
                    </div>
                    </g:elseif> 
                    <g:elseif test="${updateStatus == 2}">
                    <p> Attempt to retire product failed! </p>
                    <p>&nbsp;</p>
                    <p><strong>Thank You!</strong></p>
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
                    <h4> <i class="icon-lightbulb"> </i> Information </h4>
                </header>
                <div class="body">
                    <g:if test="${updateStatus == 0}">
                    <p>Select a product from the drop-down box and click <strong>Retire</strong> button.</p>
                    <p>&nbsp;</p>                    
                    </g:if>
                    <g:elseif test="${updateStatus == 1}">
                    <p> The product was successfully retired. </p>
                    </g:elseif>
                    <g:elseif test="${updateStatus == 2}">
                    <p> The product retire was not successful </p>
                    </g:elseif>
                    
                </div>
            </section>
        </div>
    </div>
</body>
</html>