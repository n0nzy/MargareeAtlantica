<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
<meta name="layout" content="main"/>
<title>Launch Product</title>
<style media="screen" type="text/css">
	input.number { width: 30px; height: 20px; padding-top: 0px; padding-bottom: 2px; }
</style>
</head>
<body>

    <div class="row-fluid">
        <ma:pageTitle title="Launch Product" subTitle="List of Approved Products for launch"/>
    </div>
    <div class="row-fluid">
        <div class="span10">
            <section class="widget">
                <header>
                    
                </header>
                <div class="body">
                	<div class="tab-content">
                        <div class="tab-pane active" id="tabMain">
                                <table class="table table-hover table-condensed">
                                    <thead>
                                        <tr>
                                            <th width="15%">Product Name</th>
                                            <th width="10%">Category</th>
                                            <th width="10%">Votes</th>
                                            <th width="10%">Quantity</th>
                                            <th width="10%">Cost</th>
                                            <th width="10%">Warranty</th>
                                            <th width="10%">Stock</th>
                                            <th width="10%">Re-order Level</th>                                            
                                            <th width="15%" style="text-align: center">Action</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    	<g:if test="${categoriesAndProducts['count'] <= 0}">
                                    		<tr><td colspan="5"><p>&nbsp;</p> <p>Sorry, there are no approved products to choose from for launching. </p> </td></tr>
										</g:if>
										<g:else>
    										<g:each var="category_id" in="${categoriesAndProducts.keySet()}">
        										<g:if test="${category_id != 'count'}">
    	    										<g:each var="product" in="${categoriesAndProducts[category_id]}">
    	    										                										
                										<form class="form-horizontal" action="" method="post" novalidate="novalidate" data-validate="parsley">					
                										<fieldset>
                										<tr>
                    										<td width="15%">${product.name} <input type="hidden" name="proposalID" value="${product.proposal_id}"/> <input type="hidden" name="name" value="${product.name}"/> </td>
                    										<td width="10%">${product.category} </td>
                    										<td width="10%">${product.votes} </td>
                    										<td width="10%">${product.quantity} </td>                    										
                    										<td width="10%"><input type="number" class="number" required="required" data-trigger="change" id="cost" name="cost" size="3" min="1" max="9000" value="1"/></td>
                    										<td width="10%"> <input type="number" class="number" required="required" data-trigger="change" id="warranty" name="warranty" size="3" min="0" max="900" value="0"/> </td>
                    										<td width="10%"> <input type="number" class="number" required="required" data-trigger="change" id="stock" name="stock" size="3" min="1" max="9000" value="1"/> </td>
                    										<td width="10%"> <input type="number" class="number" required="required" data-trigger="change" id="reorder" name="reorder" size="3" min="1" max="2000" value="1"/> </td>
                    										<td width="15%" style="text-align: center"> <button class="btn btn-primary" type="submit"> submit </button> </td>                    										
	        											</tr>
	        											</fieldset>
	        											</form>
	        											
            										</g:each>
        										</g:if>
    										</g:each>
										</g:else>                                      
                                    </tbody>
                                </table>
                        </div> <!-- class=tab-pane active -->
                        
                        <p>&nbsp;</p>
                        
                    </div> <!-- class=tab-content -->
                
                </div> <!-- class=body -->
				                                                                            
            </section> <!-- class=widget -->
            
        </div> <!-- class=span8 -->
        
        <div class="span2">
            <section class="widget">
                <header>
                    <h4><i class="icon-lightbulb"></i> Information </h4>
                </header>
                <div class="body">
                    <g:if test="${ProductStatus}">
                    	${ProductStatus}
                    </g:if>
                </div>
            </section>
        </div>
    </div>
</body>
</html>