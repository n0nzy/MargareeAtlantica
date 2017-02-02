package margareeatlantica

import groovy.sql.GroovyRowResult;
import groovy.sql.Sql;

import java.sql.Date;

class CrmController {
	// Reference to groovySql defined in resources.groovy (grails-app/conf/spring/resources.groovy)
	def groovySql
	
	//define availabe actions
	static upgradeAction = 'upgradeCustomer';
	static releaseAction = 'releaseOffer';
	AuthService authService = AuthService.getInstance()

    def index() {
		// make sure we have a logged in customer relationship manager
		if (!authService.isAuthorizedAs(AuthService.ROLE_CUSTOMER_RELATIONSHIP_MANAGER)) {
			redirect(controller: "user", action: "login", params: [allowRelogin: true, redirectTo: "crm:index"])
		}
		// in case logged in and authorized, get user object
		AuthUser user = authService.getSessionUser()
		
		return [user: user]
	}
	
	def upgradeCustomer() {
		List<String> messages = []
		Integer customerID = null
		
		if (!authService.isAuthorizedAs(AuthService.ROLE_CUSTOMER_RELATIONSHIP_MANAGER)) {
			redirect(controller: "user", action: "login", params: [allowRelogin: true, redirectTo: "crm:upgradeCustomer"])
		}
		// in case logged in and authorized, get user object
		AuthUser user = authService.getSessionUser()
		
		
		if (params.containsKey("customerID") && params['customerID'].toString().length() > 0) customerID = params['customerID'].toString().toInteger()
		if (customerID) {
			groovySql.execute("UPDATE `customer` SET `customer`.`type`='P' WHERE `customer`.`type`='G' AND `customer`.`customer_id`=?", [customerID])
			groovySql.execute("UPDATE `customer` SET `customer`.`type`='G' WHERE `customer`.`type`='S' AND `customer`.`customer_id`=?", [customerID])
			messages.add("Customer with ID " + customerID.toString() + " has been upgraded!")
		}
		
		//Query customer and order table to populate upgrade customer table 
		List<GroovyRowResult> rows = groovySql.rows("SELECT `customer_id`, `name`, `email_id`, `type` from `customer` WHERE `type`<>'P'")
		def customers = []
		for (GroovyRowResult row : rows) {
			def customer = [customer_id:row.customer_id, name:row.name, email_id:row.email_id, type:row.type, total_orders:0, amount_paid:0]
			GroovyRowResult order_row = groovySql.firstRow("SELECT count(`order`.`customer_id`) AS total_order, sum(`order`.`amount_paid`) AS total_spent from `order` where `order`.`customer_id`=?", [customer.customer_id])
			customer.total_orders = order_row.total_order
			customer.total_spent = order_row.total_spent
			customers.add(customer)
		}
		
		
		
		return [messages:messages, user: user, customers: customers]
	}
	
	
	def releaseOffer() {
		Boolean submitted = null
		List<String> errors = []
	
		if (!authService.isAuthorizedAs(AuthService.ROLE_CUSTOMER_RELATIONSHIP_MANAGER)) {
			redirect(controller: "user", action: "login", params: [allowRelogin: true, redirectTo: "crm:releaseOffer"])
		}
		// in case logged in and authorized, get user object
		AuthUser user = authService.getSessionUser()
		
		// extract parameters
		if (params.containsKey("submitted")) submitted = params['submitted']
		
		// if form submitted, validate fields
		// and try to update profile
		if (submitted) {
				// validate type
				String type = null
				if (!params.containsKey("type") || params['type'].length() < 1) {
					errors.add("Customer Status is not given")
				} else {
					type = params['type'].toString()
					if (type.equals("A")) type = null
				}
				// validate country
				Integer countryID = null
				if (!params.containsKey("country_id") || params['country_id'].length() < 1) {
					errors.add("Country is not given")
				} else {
					countryID = params['country_id'].toString().toInteger()
					if (countryID < 0) countryID = null
				}
				// validate validity
				if (!params.containsKey("validity") || params['validity'].length() < 1) {
					errors.add("Validity is not given")
				} else if (params['validity'].toString().toInteger() < 0) {
					errors.add("Validity can't be a negative value")
				}
				// validate discount
				if (!params.containsKey("discount") || params['discount'].length() < 1) {
					errors.add("Discount is not given")
				} else if (params['discount'].toString().toInteger() <= 0) {
					errors.add("Discount can't be a zero or negative value")
				}
				// validate date
				String launch_date = ""
				if (!params.containsKey("launch_date") || params['launch_date'].length() < 1) {
					errors.add("Date is not given")
				} else {
					String ldate = params['launch_date'].toString()
					String[] ld = ldate.split("/")
					launch_date = ld[2] + "-" + ld[0] + "-" + ld[1]
				}
				// validate description
				if (!params.containsKey("description") || params['description'].length() < 1) {
					errors.add("Description is not given")
				}
				// try to update now
				if (errors.isEmpty()) {
					groovySql.execute(
						"INSERT INTO `offer` (`type`, `country_id`, `employee_id`, `launch_date`, `validity`, `discount`, `description`) VALUES (?,?,?,?,?,?,?)",
						[
							type,
							countryID,
							user.employeeID,
							launch_date,
							params['validity'].toString().toInteger(),
							params['discount'].toString().toInteger(),
							params['description']
						]
					)
				}
		}
		
		Map<Integer, String> countryList = new HashMap<Integer, String>()
		List<GroovyRowResult> rows = groovySql.rows("SELECT * FROM country ORDER BY country_name")
		for (GroovyRowResult row : rows) {
			countryList.put(row.country_id, row.country_name)
		}
		
		return [submitted: submitted, errors: errors, user: user, countries: countryList]
	}
}
