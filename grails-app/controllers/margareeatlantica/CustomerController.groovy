package margareeatlantica

import groovy.sql.GroovyRowResult;
import groovy.sql.Sql;

import java.sql.Date;


class CustomerController {
	// Reference to groovySql defined in resources.groovy (grails-app/conf/spring/resources.groovy)
	def groovySql
	// Reference to authService defined in AuthService.groovy (grails-app/services/margareeatlantica/AuthService.groovy)
	AuthService authService = AuthService.getInstance()

    def index() {
		// make sure we have a logged in customer
		if (!authService.isAuthorizedAs(AuthService.ROLE_CUSTOMER)) {
			redirect(controller: "user", action: "login", params: [allowRelogin: true, redirectTo: "customer:index"])
		}
		// in case logged in and authorized, get user object
		AuthUser user = authService.getSessionUser()
		
		
		// get latest order
		List<GroovyRowResult> rows = groovySql.rows("SELECT `order`.*,`item`.`product_id`,`item`.`quantity`,`product`.`name`,`category`.`category_name` FROM `order`,`item`,`product`,`category` WHERE `order`.`customer_id`=" + user.customerID.toString() + " AND `item`.`order_id`=`order`.`order_id` AND `product`.`product_id`=`item`.`product_id` AND `category`.`category_id`=`product`.`category_id` ORDER BY `order`.`order_date` DESC,`order`.`order_id` DESC")
		Map<String, Object> latest_order = new HashMap<String, Object>()
		Integer tmpInt = null
		for (GroovyRowResult row : rows) {
			def product = [id:row.product_id, name:row.name, category:row.category_name, quantity:row.quantity]
			if (!tmpInt) {
				tmpInt = row.order_id
				latest_order['id'] = row.order_id
				latest_order['order_date'] = row.order_date
				latest_order['total_amount'] = row.total_amount
				latest_order['amount_paid'] = row.amount_paid
				latest_order['shipping_address'] = row.shipping_address.toString()
				if (row.status == 'P') {
					latest_order['status'] = "Pending"
				} else {
					latest_order['status'] = "Cleared"
				}
				latest_order['products'] = [product]
			} else if (tmpInt != row.order_id) {
				break
			} else {
				latest_order['products'].add(product)
			}
		}
		
		// get order count
		Integer total_orders = 0
		GroovyRowResult row = groovySql.firstRow("SELECT COUNT(`order`.`order_id`) AS total_orders FROM `order` WHERE `order`.`customer_id`=" + user.customerID.toString())
		if (row) {
			total_orders = row.total_orders
		}
		
		// get proposed product count
		Integer total_proposed_products = 0
		row = groovySql.firstRow("SELECT COUNT(propose_product.proposal_id) AS total_pp FROM propose_product WHERE propose_product.customer_id=?", [user.customerID])
		if (row) {
			total_proposed_products = row.total_pp
		}
		
		return [user: user, latestOrder: latest_order, totalOrders: total_orders, totalProposedProducts: total_proposed_products]
	}
	
	def placeOrder() {
		Integer selProductID = null
		Integer remProductID = null
		def cartProducts = [count: 0]
		String cartProductsString = ""
		def categories_products = [count: 0]
		
		// make sure we have a logged in customer
		if (!authService.isAuthorizedAs(AuthService.ROLE_CUSTOMER)) {
			redirect(controller: "user", action: "login", params: [allowRelogin: true, redirectTo: "customer:placeOrder"])
		}
		// in case logged in and authorized, get user object
		AuthUser user = authService.getSessionUser()
		
		// extract parameters
		if (params.containsKey("selProductID") && params['selProductID'].toString().length() > 0) selProductID = Integer.parseInt(params['selProductID'].toString())
		if (params.containsKey("remProductID") && params['remProductID'].toString().length() > 0) remProductID = Integer.parseInt(params['remProductID'].toString())
		if (params.containsKey("cartProducts") && params['cartProducts'].toString().length() > 2) {
			String[] ciSplit = params['cartProducts'].toString().split(",")
			String[] pdSplit
			for (String cp : ciSplit) {
				pdSplit = cp.split(":")
				if (pdSplit[1].toInteger() > 0) {
					cartProducts[pdSplit[0]] = [id:pdSplit[0].toInteger(), name:"", category:"", quantity:pdSplit[1].toInteger(), cost:0]
					cartProducts['count']++
				}
			}
		}
		if (selProductID && !cartProducts[selProductID.toString()]) {
			cartProducts[selProductID.toString()] = [id:selProductID, name:"", category:"", quantity:1, cost:0]
			cartProducts['count']++
		} else if (remProductID && cartProducts[remProductID.toString()]) {
			cartProducts.remove(remProductID.toString())
			cartProducts['count']--
			if (cartProducts['count'] <= 0) cartProducts['count'] = 0
		}
		
		// find products
		List<GroovyRowResult> rows = groovySql.rows("SELECT product.product_id,product.name,product.category_id,category.category_name,cost,warranty_period,stock FROM product,category WHERE category.category_id=product.category_id AND product.status='A' ORDER BY category.category_name ASC,product.name ASC")
		for (GroovyRowResult row : rows) {
			def product = [id:row.product_id, name:row.name, category:row.category_name, cost:row.cost, warrantyPeriod:row.warranty_period, stock:row.stock, selected:false]
			if (cartProducts[product.id.toString()]) {
				cartProducts[product.id.toString()].name = product.name
				cartProducts[product.id.toString()].category = product.category
				cartProducts[product.id.toString()].cost = product.cost
				product.selected = true
				if (cartProductsString.length() == 0) {
					cartProductsString = product.id.toString() + ":" + cartProducts[product.id.toString()].quantity.toString()
				} else {
					cartProductsString += "," + product.id.toString() + ":" + cartProducts[product.id.toString()].quantity.toString()
				}
			}
			if (!categories_products[row.category_id.toString()]) {
				categories_products[row.category_id.toString()] = [product]
				categories_products['count']++
			} else {
				categories_products[row.category_id.toString()].add(product)
			}
		}
		
		return [user: user, cartProducts: cartProducts, cartProductsString: cartProductsString, categoriesAndProducts: categories_products]
	}
	
	def placeOrderCart() {
		def cartProducts = [count: 0]
		String cartProductsString = ""
		
		// make sure we have a logged in customer
		if (!authService.isAuthorizedAs(AuthService.ROLE_CUSTOMER)) {
			redirect(controller: "user", action: "login", params: [allowRelogin: true, redirectTo: "customer:placeOrder"])
		}
		// in case logged in and authorized, get user object
		AuthUser user = authService.getSessionUser()
		
		// get customer object
		def customer = null
		GroovyRowResult customerRow = groovySql.firstRow("SELECT customer.*,country.country_name FROM customer,country WHERE customer_id=? AND country.country_id=customer.customer_id", [user.customerID])
		if (customerRow) {
			customer = [id:customerRow.customer_id, name:customerRow.name, address:customerRow.address, residentPhone:customerRow.resident_phone, mobilePhone:customerRow.mobile_phone, email:customerRow.email_id, type:customerRow.type.toString(), countryID:customerRow.country_id, countryName:customerRow.country_name]
		}
		
		// extract parameters
		if (params.containsKey("cartProducts") && params['cartProducts'].toString().length() > 2) {
			String[] ciSplit = params['cartProducts'].toString().split(",")
			String[] pdSplit
			for (String cp : ciSplit) {
				pdSplit = cp.split(":")
				if (pdSplit[1].toInteger() > 0) {
					cartProducts[pdSplit[0]] = [id:pdSplit[0].toInteger(), name:"", category:"", quantity:pdSplit[1].toInteger(), cost:0, stock:0]
					cartProducts['count']++
				}
			}
		}
		
		// find products and update cart products details
		List<GroovyRowResult> rows = groovySql.rows("SELECT product.product_id,product.name,product.category_id,category.category_name,cost,warranty_period,stock FROM product,category WHERE category.category_id=product.category_id AND product.status='A'")
		String pID = null
		for (GroovyRowResult row : rows) {
			pID = row.product_id.toString()
			if (cartProducts[pID]) {
				// update cart product's details from DB
				cartProducts[pID].name = row.name
				cartProducts[pID].category = row.category_name
				cartProducts[pID].cost = row.cost
				cartProducts[pID].stock = row.stock
				if (cartProducts[pID].quantity > cartProducts[pID].stock) {
					cartProducts[pID].quantity = cartProducts[pID].stock
				}
			}
		}
		
		for (String product_id : cartProducts.keySet()) {
			if (product_id.equals("count")) continue
			if (cartProductsString.length() == 0) {
				cartProductsString = cartProducts[product_id].id.toString() + ":" + cartProducts[product_id].quantity.toString()
			} else {
				cartProductsString += "," + cartProducts[product_id].id.toString() + ":" + cartProducts[product_id].quantity.toString()
			}
		}
		
		// find discount offer
		List<GroovyRowResult> offerRows = groovySql.rows("SELECT * FROM offer WHERE offer.launch_date<=CURDATE() AND CURDATE()<=DATE_ADD(offer.launch_date,INTERVAL offer.validity DAY) ORDER BY offer.discount DESC")
		def offer = null
		for (GroovyRowResult offerRow : offerRows) {
			if (offerRow.type && !offerRow.type.toString().equals(customer.type)) continue
			if (offerRow.country_id && offerRow.country_id != customer.countryID) continue
			offer = [id:offerRow.offer_id, description:offerRow.description, discount:offerRow.discount]
		}
		
		// order summary
		def order_summary = [totalAmount:0, discountAmount:0, totalPaid:0]
		for (String product_id : cartProducts.keySet()) {
			if (product_id.equals("count")) continue
			order_summary.totalAmount += cartProducts[product_id].quantity * cartProducts[product_id].cost
		}
		if (offer) {
			order_summary.discountAmount = Math.round((order_summary.totalAmount * offer.discount) / 100)
		}
		order_summary.totalPaid = order_summary.totalAmount - order_summary.discountAmount
		
		return [user: user, customer: customer, cartProducts: cartProducts, cartProductsString: cartProductsString, offer: offer, orderSummary: order_summary]
	}
	
	def placeOrderComplete() {
		def cartProducts = [count: 0]
		def offer = null
		def order_summary = null
		List<String> errors = []
		
		// make sure we have a logged in customer
		if (!authService.isAuthorizedAs(AuthService.ROLE_CUSTOMER)) {
			redirect(controller: "user", action: "login", params: [allowRelogin: true, redirectTo: "customer:placeOrder"])
		}
		// in case logged in and authorized, get user object
		AuthUser user = authService.getSessionUser()
		
		// get customer object
		def customer = null
		GroovyRowResult customerRow = groovySql.firstRow("SELECT customer.*,country.country_name FROM customer,country WHERE customer_id=? AND country.country_id=customer.customer_id", [user.customerID])
		if (customerRow) {
			customer = [id:customerRow.customer_id, name:customerRow.name, address:customerRow.address, residentPhone:customerRow.resident_phone, mobilePhone:customerRow.mobile_phone, email:customerRow.email_id, type:customerRow.type.toString(), countryID:customerRow.country_id, countryName:customerRow.country_name]
		}
		
		// extract parameters
		if (params.containsKey("cartProducts") && params['cartProducts'].toString().length() > 2) {
			String[] ciSplit = params['cartProducts'].toString().split(",")
			String[] pdSplit
			for (String cp : ciSplit) {
				pdSplit = cp.split(":")
				if (pdSplit[1].toInteger() > 0) {
					cartProducts[pdSplit[0]] = [id:pdSplit[0].toInteger(), quantity:pdSplit[1].toInteger()]
					cartProducts['count']++
				}
			}
		}
		if (params.containsKey("offer") && params['offer'].toString().length() > 2) {
			String[] tmpSplit = params['offer'].toString().split(":")
			if (tmpSplit.length == 2) {
				offer = [id:tmpSplit[0].toInteger(), discount:tmpSplit[1].toInteger()]
			}
		}
		if (params.containsKey("orderSummary") && params['orderSummary'].toString().length() > 2) {
			String[] tmpSplit = params['orderSummary'].toString().split(":")
			if (tmpSplit.length == 3) {
				order_summary = [id:0, totalAmount:tmpSplit[0].toInteger(), discountAmount:tmpSplit[1].toInteger(), totalPaid:tmpSplit[2].toInteger()]
			}
		}
		
		// insert order data into DB if things look ok
		if (order_summary != null && cartProducts.count > 0) {
			// first, fill in order table and get auto-generated order ID
			def retList
			if (offer) {
				retList = groovySql.executeInsert("INSERT INTO `order` (`customer_id`,`order_date`,`offer_id`,`total_amount`,`amount_paid`,`status`,`shipping_address`) VALUES (?,CURDATE(),?,?,?,'P',?)", [customer.id, offer.id, order_summary.totalAmount, order_summary.totalPaid, customer.address])
			} else {
				retList = groovySql.executeInsert("INSERT INTO `order` (`customer_id`,`order_date`,`total_amount`,`amount_paid`,`status`,`shipping_address`) VALUES (?,CURDATE(),?,?,'P',?)", [customer.id, order_summary.totalAmount, order_summary.totalPaid, customer.address + ", " + customer.countryName])
			}
			// get the new order ID
			Integer orderID = retList[0][0]
			order_summary.id = orderID
			// insert details into items table
			// and update product stock count
			for (String product_id : cartProducts.keySet()) {
				if (product_id.equals("count")) continue
				groovySql.executeInsert("INSERT INTO `item` (`order_id`,`product_id`,`quantity`) VALUES (?,?,?)", [orderID, product_id.toInteger(), cartProducts[product_id].quantity])
				//groovySql.execute("UPDATE `product` SET stock=stock-? WHERE product_id=?", [cartProducts[product_id].quantity, product_id.toInteger()])
			}
		} else {
			errors.add("There's no order to complete. Go back and try again...")
		}
		
		return [errors: errors, user: user, customer: customer, cartProducts: cartProducts, offer: offer, orderSummary: order_summary]
	}
	
	def proposeProduct() {
		Integer product_id = null
		def proposedProduct = null
		Integer proposeStatus = 0
		Map<String, Object> categories_products = new HashMap<String, Object>()
		
		// make sure we have a logged in customer
		if (!authService.isAuthorizedAs(AuthService.ROLE_CUSTOMER)) {
			redirect(controller: "user", action: "login", params: [allowRelogin: true, redirectTo: "customer:proposeProduct"])
		}
		// in case logged in and authorized, get user object
		AuthUser user = authService.getSessionUser()
		
		// extract parameters
		if (params.containsKey("productID") && params['productID'].toString().length() > 0) product_id = params['productID'].toString().toInteger()
		
		// find retired products that can be proposed
		List<GroovyRowResult> rows = groovySql.rows("SELECT product.*,category.category_name FROM product,category WHERE product.status='R' AND category.category_id=product.category_id ORDER BY category_name ASC,name ASC")
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
		
		// if a product is proposed, add it to DB
		// or increase vote count if already in DB
		if (proposedProduct) {
			GroovyRowResult row = groovySql.firstRow("SELECT * FROM propose_product WHERE product_name=? AND category_id=? AND (`status`='P' OR `status`='A')", [proposedProduct.name, proposedProduct.categoryID])
			// somebody already proposed, so just increase the votes
			if (row) {
				if (row.customer_id != user.customerID) {
					groovySql.execute("UPDATE propose_product SET votes=votes+1 WHERE product_name=? AND category_id=? AND (`status`='P' OR `status`='A')", [proposedProduct.name, proposedProduct.categoryID])
					proposeStatus = 2
				} else {
					proposeStatus = 3
				}
			} else {
				// otherwise, propose this product on behalf of this customer
				groovySql.execute("INSERT INTO `propose_product` (`product_name`,`customer_id`,`category_id`,`votes`,`status`,`quantity`) VALUES (?,?,?,?,'P',?)", [proposedProduct.name, user.customerID, proposedProduct.categoryID, 1, 0])
				proposeStatus = 1
			}
		}
		
		return [proposeStatus: proposeStatus, user: user, proposedProduct: proposedProduct, categoriesAndProducts: categories_products]
	}
}
