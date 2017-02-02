<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta name="layout" content="main"/>
<title> e-Retail Order Management System| Product Manager Home </title>
</head>
<body>
    <div class="row-fluid"> 
    	${user.name}
    </div>
    
    <!-- Initialize variable to store a cache of Product.category when iterating through loop of Products -->
    <g:set var="category_cache" value="" />
    
    <div class="row-fluid">
        <div class="span10">
            <section class="widget">
                <header>
                    <h4><i class="icon-shopping-cart"></i> All Products [Approved and Retired] </h4>
                </header>
                <div class="body">
                    
                    <div id="bar" class="progress progress-inverse progress-small">
                        <div class="bar" style="width: 100%;"></div>
                    </div>
                    <div class="tab-content">
                        <div class="tab-pane active" id="tabMain">
                            <form id="poForm" name="poForm" class="form-horizontal form-condensed" action="" method="POST">
                                <table class="table table-hover table-condensed">
                                    <thead>
                                        <tr>
                                            <th width="30%">Name</th>
                                            <th width="15%">Category</th>
                                            <th width="15%">Cost</th>
                                            <th width="20%">Warranty (months)</th>
                                            <th width="10%">Stock</th>
                                            <th width="10%" style="text-align: right">Status</th>
                                        </tr>
                                    </thead>
                                    <tbody>  
                                    	<g:if test="${categoriesAndProducts['count'] <= 0}">
                                    		<tr><td colspan="5"><p>&nbsp;</p> <p>Sorry, there are no products to choose from. </p> </td></tr>
										</g:if>
										<g:else>
    										<g:each var="category_id" in="${categoriesAndProducts.keySet()}">
        										<g:if test="${category_id != 'count'}">
    	    										<g:each var="product" in="${categoriesAndProducts[category_id]}">
    	    											
    	    											<!-- If Product.category has changed then add a divider line <hr /> -->
    	    											<g:if test = "${product.category != category_cache}">
    	    												<tr><td colspan="6"> <hr /> </td></tr>
	        											</g:if>
	        											
                										<tr>
                    										<td width="30%">${product.name} </td>
                    										<td width="15%">${product.category} </td>
                    										<td width="15%">$${product.cost} </td>
                    										<td width="20%" style="text-align: right">${product.warrantyPeriod} </td>
                    										<td width="10%" style="text-align: center">${product.stock} </td>
                    										<td width="10%" style="text-align: center">${product.status} </td>                    										
	        											</tr>
	        											
	        											<!-- Keep a cache of the last product category for comparison in the next iteration of product.category -->	        												        											
	        											<g:set var="category_cache" value="${product.category}" />
            										</g:each>
        										</g:if>
    										</g:each>
										</g:else>                                      
                                    </tbody>
                                </table>
                           <input type="hidden" id="Products" name="Products" value=""/>  
                            </form>
                        </div>
                        <p>&nbsp;</p>
                        <div class="description">
                            
                        </div>
                    </div>
                </div>
            </section>
        </div>
       
    </div>
    
    <hr />
    
    <div class="row-fluid box-row">
        
    </div>
</body>
</html>