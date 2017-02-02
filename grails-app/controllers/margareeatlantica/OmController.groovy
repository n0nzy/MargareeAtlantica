package margareeatlantica

import groovy.sql.GroovyRowResult;
import groovy.sql.Sql;

import java.lang.ref.ReferenceQueue.Null;
import java.sql.Date;

class OmController 
{
	def groovySql
	AuthService authService = AuthService.getInstance()

	def index() 
	{
		// make sure we have a logged in order manager
		if (!authService.isAuthorizedAs(AuthService.ROLE_ORDER_MANAGER)) 
		{
			redirect(controller: "user", action: "login", params: [allowRelogin: true, redirectTo: "om:index"])
		}
		// in case logged in and authorized, get user object
		AuthUser user = authService.getSessionUser()
		
		
		
		return [user: user]
	}
	
	def analyzeProposals() 
	{
		Integer proposalID = null
		Integer quantity = null
		String proposalAction = null
		def messages = []
		
		// make sure we have a logged in Order Manager
		if (!authService.isAuthorizedAs(AuthService.ROLE_ORDER_MANAGER)) 
		{
			redirect(controller: "user", action: "login", params: [allowRelogin: true, redirectTo: "om:analyzeProposals"])
		}
		// in case logged in and authorized, get user object
		AuthUser user = authService.getSessionUser()
		
		// extract parameters
		if (params.containsKey("proposalID") && params['proposalID'].toString().length() > 0) proposalID = params['proposalID'].toString().toInteger()
		if (params.containsKey("quantity") && params['quantity'].toString().length() > 0) quantity = params['quantity'].toString().toInteger()
		if (params.containsKey("proposalAction") && params['proposalAction'].toString().length() > 0) proposalAction = params['proposalAction'].toString()
		if (proposalAction != null && proposalAction.equals("A") && quantity != null && quantity <= 0) {
			quantity = null
			messages.add("ERROR: Quantity can't be zero or negative. Please enter a positive quantity.")
		}
		if (proposalID && proposalAction && quantity != null) 
		{
			if (proposalAction.equals("R")) quantity = 0
			groovySql.execute("UPDATE `propose_product` SET quantity=?, `status`=?, employee_id=? WHERE `propose_product`.`proposal_id`=?", [quantity, proposalAction, user.employeeID, proposalID])
			// update DB and approve proposal
			if (proposalAction.equals("A")) {
				messages.add("The proposal with ID " + proposalID.toString() + " has been approved.")
			} else {
				messages.add("The proposal with ID " + proposalID.toString() + " has been rejected.")
			}
		}
				
		// find products
		def proposals = []
		//List<GroovyRowResult> rows = groovySql.rows("SELECT product.product_id,product.name,product.category_id,category.category_name,cost,warranty_period,stock FROM product,category WHERE category.category_id=product.category_id AND product.status='A' ORDER BY category.category_name ASC,product.name ASC")
		List<GroovyRowResult> rows = groovySql.rows("SELECT propose_product.proposal_id,propose_product.product_name,propose_product.category_id,propose_product.quantity,propose_product.votes,category.category_name FROM propose_product,category WHERE category.category_id=propose_product.category_id AND propose_product.status='P' ORDER BY category.category_name ASC,propose_product.product_name ASC")
		for (GroovyRowResult row : rows) 
		{
			def proposal = [id:row.proposal_id, name:row.product_name, category:row.category_name, quantity:row.quantity, votes:row.votes, selected:false]
			proposals.add(proposal)
		}
		
		return [proposals: proposals, messages:messages]
	}
	
	def procureProduct() 
	{	
		//Updates the Procurement Table
		Integer productID = null
		Integer quantity = null
		Boolean procured = false
		Boolean submitted = false
		def messages = []
		
		// make sure we have a logged in Order Manager
		if (!authService.isAuthorizedAs(AuthService.ROLE_ORDER_MANAGER)) 
		{
			redirect(controller: "user", action: "login", params: [allowRelogin: true, redirectTo: "om:procureProduct"])
		}
		// in case logged in and authorized, get user object
		AuthUser user = authService.getSessionUser()
		
		// extract parameters
		if (params.containsKey("submitted") && params['submitted'].toString().length() > 0) submitted = params['submitted']
		if (params.containsKey("productID") && params['productID'].toString().length() > 0) productID = params['productID'].toString().toInteger()
		if (params.containsKey("quantity") && params['quantity'].toString().length() > 0) quantity = params['quantity'].toString().toInteger()
		if (quantity != null && quantity <= 0) {
			quantity = null
			messages.add("ERROR: Quantity can't be zero or negative.")
		}
		if (submitted && productID && quantity) 
		{
			groovySql.execute("INSERT INTO `procurement` (`product_id`,`employee_id`,`quantity`) VALUES (?,?,?)", [productID, user.employeeID, quantity])
			groovySql.execute("UPDATE `product` SET `product`.`stock`=`product`.`stock`+? WHERE `product`.`product_id`=?", [quantity, productID])
			// update DB and approve proposal
			procured = true
			messages.add("The product with Product ID " + productID.toString() + " has been procured.")
		}
		// find products
		Map<String, Object> categories_products = new HashMap<String, Object>()
		List<GroovyRowResult> rows = groovySql.rows("SELECT product.*,category.category_name FROM product,category WHERE product.stock <= product.reorder_level AND product.status='A' AND category.category_id=product.category_id ORDER BY category.category_name ASC, product.name ASC")
		for (GroovyRowResult row : rows) 
		{
			def product = [id:row.product_id, name:row.name, category:row.category_name, stock:row.stock, reorderLevel:row.reorder_level]
			if (categories_products.containsKey(row.category_id)) {
				categories_products[row.category_id].add(product)
			} else {
				categories_products[row.category_id] = [product]
			}
		}
		
		return [categoriesAndProducts: categories_products, messages:messages, procured: procured]
	}
	
	def clearOrders()
	{
		Integer selProductID = null
		Integer remProductID = null
		def cartProducts = [count: 0]
		String cartProductsString = ""
		def messages = []
		def categories_products = [count: 0]
		
		// make sure we have a logged in Order Manager
		if (!authService.isAuthorizedAs(AuthService.ROLE_ORDER_MANAGER)) 
		{
			redirect(controller: "user", action: "login", params: [allowRelogin: true, redirectTo: "om:clearOrders"])
		}
		// in case logged in and authorized, get user object
		AuthUser user = authService.getSessionUser()
		
		// extract parameters
		if (params.containsKey("orderID") && params['orderID'].toString().length() > 0) selProductID = params['orderID'].toString().toInteger()
		if (selProductID) 
		{
			// update DB and clear order
			groovySql.execute("UPDATE `order` SET `status`='C' WHERE `order`.`order_id`=?", [selProductID])
			List<GroovyRowResult> rows = groovySql.rows("SELECT * FROM `item` WHERE `item`.`order_id`=?", [selProductID])
			for (GroovyRowResult row : rows) {
				groovySql.execute("UPDATE `product` SET `product`.`stock`=`product`.`stock`-? WHERE `product`.`product_id`=?", [row.quantity, row.product_id])
			}
			messages.add("The order with ID " + selProductID.toString() + " has been cleared.")
		}
				
		// find orders
		def orders = [:]
		//SELECT order.order_id,item.product_id,item.quantity,product.stock,product.reorder_level FROM order,item,product WHERE item.order_id=order.order_id AND product.product_id=item.product_id AND order.status='P' ORDER BY order.order_id ASC
		//List<GroovyRowResult> rows = groovySql.rows("SELECT product.product_id,product.name,product.category_id,category.category_name,cost,warranty_period,stock FROM product,category WHERE category.category_id=product.category_id AND product.status='A' ORDER BY category.category_name ASC,product.name ASC")
		List<GroovyRowResult> rows = groovySql.rows("SELECT `order`.`order_id`,`order`.`order_date`,`customer`.`name` AS `customer_name`,`item`.`product_id`,`item`.`quantity`,`product`.`name`,`product`.`stock`,`product`.`reorder_level` FROM `order`,`item`,`product`,`customer` WHERE `customer`.`customer_id`=`order`.`customer_id` AND `item`.`order_id`=`order`.`order_id` AND `product`.`product_id`=`item`.`product_id` AND `order`.`status`='P' ORDER BY `order`.`order_id` ASC")
		for (GroovyRowResult row : rows) 
		{
			def order = [oid:row.order_id, customer:row.customer_name, order_date:row.order_date, pid:row.product_id, qty:row.quantity, product_name:row.name, st:row.stock, rl:row.reorder_level, clearable:true]
			if (orders.containsKey(row.order_id.toString())) {
				orders[order.oid.toString()].add(order)
			} else {
				orders[order.oid.toString()] = [order]
			}
		}
		boolean clearable = true
		for (def order_id : orders.keySet()) {
			clearable = true
			// determine whether order can be cleared
			for (def order_product : orders[order_id]) {
				if ((order_product.st - order_product.qty) <= order_product.rl) {
					clearable = false
					break
				}
			}
			// now set clearable
			for (def order_product : orders[order_id]) {
				order_product.clearable = clearable
			}
		}
		
		return [orders: orders, messages:messages]
	}
	
}
