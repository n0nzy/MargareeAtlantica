package margareeatlantica

import groovy.sql.GroovyRowResult;

class PmController {
	
	def groovySql
	
	AuthService authService = AuthService.getInstance()

	def index() {
		// Initializing some variables
		def Products = [count: 0]
		String ProductsString = ""
		def categories_products = [count: 0]
		
		// make sure we have a logged in product manager
		if (!authService.isAuthorizedAs(AuthService.ROLE_PRODUCT_MANAGER)) {
			redirect(controller: "user", action: "login", params: [allowRelogin: true, redirectTo: "pm:index"])
		}
		// in case logged in and authorized, get user object
		AuthUser user = authService.getSessionUser()
		
		// get all products
		List<GroovyRowResult> rows = groovySql.rows("SELECT product.product_id,product.name,product.category_id,category.category_name,cost,warranty_period,stock,product.status FROM product,category WHERE category.category_id=product.category_id ORDER BY product.status DESC")
		
		for (GroovyRowResult row : rows) {
			def product = [id:row.product_id, name:row.name, category:row.category_name, cost:row.cost, warrantyPeriod:row.warranty_period, stock:row.stock, status:row.status, selected:false]
			
			if (Products[product.id.toString()]) {
				Products[product.id.toString()].name = product.name
				Products[product.id.toString()].category = product.category
				Products[product.id.toString()].cost = product.cost
				Products[product.id.toString()].status = product.status
				product.selected = true
				if (ProductsString.length() == 0) {
					ProductsString = product.id.toString() + ":" + Products[product.id.toString()].quantity.toString()
				} else {
					ProductsString += "," + product.id.toString() + ":" + Products[product.id.toString()].quantity.toString()
				}
			}
			
			if (!categories_products[row.category_id.toString()]) {
				categories_products[row.category_id.toString()] = [product]
				categories_products['count']++
			} else {
				categories_products[row.category_id.toString()].add(product)
			}
		}
		
		return [user: user, ProductsString: ProductsString, categoriesAndProducts: categories_products]
		
	}//End index()
	
	def launchProduct() {
		
		def Products = [count: 0]
		String ProductsString = ""
		def categories_products = [count: 0]
		String product_status = ""
		
		//Initializing some individual fields that will update the product table. 
		Integer proposal_id = 0
		String name = ""
		Integer cost = 0
		Integer warranty = 0
		Integer stock = 0
		Integer reorder = 0
		
		// make sure we have a logged in employee
		if (!authService.isAuthorizedAs(AuthService.ROLE_EMPLOYEE)) {
			redirect(controller: "user", action: "login", params: [allowRelogin: true, redirectTo: "employee:launchProduct"])
		}
		// in case logged in and authorized, get user object
		AuthUser user = authService.getSessionUser()
		
		// extract parameters from Form
		if (params.containsKey("productID") && params['productID'].toString().length() > 0) product_id = params['productID'].toString().toInteger()
		
		if (params.containsKey("proposalID") && params['proposalID'].toString().length() > 0) {
			proposal_id = params['proposalID'].toString().toInteger()
			//println 'proposal_id  = ' + proposal_id.toString()						
		}
		
		if (params.containsKey("name") && params['name'].toString().length() > 0) {
			name = params['name'].toString()
			/* println 'name  = ' + name.toString()	*/
		}
		
		if (params.containsKey("cost") && params['cost'].toString().length() > 0) {
			cost = params['cost'].toString().toInteger()
			// println 'cost  = ' + cost.toString()
		}
		
		if (params.containsKey("warranty") && params['warranty'].toString().length() > 0) {
			warranty = params['warranty'].toString().toInteger()
			// println 'warranty  = ' + warranty.toString()
		}
		
		if (params.containsKey("stock") && params['stock'].toString().length() > 0) {
			stock = params['stock'].toString().toInteger()
			// println 'stock  = ' + stock.toString()
		}
		
		if (params.containsKey("reorder") && params['reorder'].toString().length() > 0) {
			reorder = params['reorder'].toString().toInteger()
			// println 'reorder  = ' + reorder.toString()
		}
		
		if (params.containsKey("proposalID") && params.containsKey("name") ) {
						
			//groovySql.execute("UPDATE product SET cost=? WHERE product.product_id=?", [proposedProduct.id])
			groovySql.execute("UPDATE product SET cost = ?, warranty_period = ?, stock = ?, reorder_level = ?, status = 'A' WHERE product.name = ?", [cost, warranty, stock, reorder, name] )
			
			product_status = "Product: " + name + " has been launched! "
			
			groovySql.execute("UPDATE propose_product SET status = 'L' WHERE product_name = ?", [name] )

		}
		
		// find products proposed with status 'accepted' that can be launched
		List<GroovyRowResult> rows = groovySql.rows("SELECT proposal_id, product_name, customer_id, votes, status, employee_id, quantity, category.category_name, category.category_id FROM propose_product, category WHERE category.category_id = propose_product.category_id AND propose_product.status = 'A' ORDER BY product_name ASC")
		
		for (GroovyRowResult row : rows) {
			def product = [id:row.proposal_id, name:row.product_name, category:row.category_name, votes:row.votes, quantity:row.quantity, category_id:row.category_id, proposal_id:row.proposal_id, selected:false]
			
			if (Products[product.id.toString()]) {
				Products[product.id.toString()].proposal_id = product.proposal_id
				Products[product.id.toString()].name = product.name
				Products[product.id.toString()].category = product.category
				Products[product.id.toString()].votes = product.votes
				Products[product.id.toString()].quantity = product.quantity				
				product.selected = true
				if (ProductsString.length() == 0) {
					ProductsString = product.id.toString() + ":" + Products[product.id.toString()].quantity.toString()
				} 
				else {
					ProductsString += "," + product.id.toString() + ":" + Products[product.id.toString()].quantity.toString()
				}
			}
			
			if (!categories_products[row.category_id.toString()]) {
				categories_products[row.category_id.toString()] = [product]
				categories_products['count']++
			} 
			else {
				categories_products[row.category_id.toString()].add(product)
			}
		}//end for
		
		return [user: user, ProductsString: ProductsString, categoriesAndProducts: categories_products, ProductStatus:product_status]
		
	}//End launchProduct()
	
	//Here the product manager chooses a drop-down list of products to be retired.
	def retireProduct() {
		//Initializing some variables that will be used.
		Integer product_id = null
		def proposedProduct = null
		Integer updateStatus = 0
		Map<String, Object> categories_products = new HashMap<String, Object>()
		
		// make sure we have a logged in employee
		if (!authService.isAuthorizedAs(AuthService.ROLE_EMPLOYEE)) {
			redirect(controller: "user", action: "login", params: [allowRelogin: true, redirectTo: "employee:retireProduct"])
		}
		// in case logged in and authorized, get user object
		AuthUser user = authService.getSessionUser()
		
		// extract parameters from Form
		if (params.containsKey("productID") && params['productID'].toString().length() > 0) product_id = params['productID'].toString().toInteger()
		
		// find products with status 'approved' that can be retired
		List<GroovyRowResult> rows = groovySql.rows("SELECT product.*,category.category_name FROM product,category WHERE product.status='A' AND category.category_id=product.category_id ORDER BY category_name ASC,name ASC")
		for (GroovyRowResult row : rows) {
			def product = [id:row.product_id, name:row.name, categoryID:row.category_id, categoryName:row.category_name]
			if (product_id && product_id == product.id) {
				proposedProduct = product
			}
			if (!categories_products[product.id.toString()]) {
				categories_products[product.id.toString()] = [product]
			} else {
				categories_products[product.id.toString()].add(product)
			}
		}
		
		// if a product is proposed, update it in DB
		if (proposedProduct) {			
			//Update database where product is the proposed...
			groovySql.execute("UPDATE product SET status='R' WHERE product.product_id=?", [proposedProduct.id])
			updateStatus = 1  //This means product was updated successfully
		}
		else {
			// otherwise, propose this product on behalf of this customer
			//updateStatus = 2 //This means product failed to be update successfully.
		}
		
		return [updateStatus: updateStatus, user: user, proposedProduct: proposedProduct, categoriesAndProducts: categories_products]
		
	}//End retireProduct()
	
}//End Class PmController