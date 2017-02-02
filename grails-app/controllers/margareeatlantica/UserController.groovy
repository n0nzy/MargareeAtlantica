package margareeatlantica

import groovy.sql.GroovyRowResult;

class UserController {
	def groovySql
	AuthService authService = AuthService.getInstance()

	
    def index() {
		Boolean submitted = false
		List<String> errors = []
		
		// make sure we have a logged in user (customer or employee)
		if (!authService.isAuthorizedAs(AuthService.ROLE_ANY_USER)) {
			redirect(controller: "user", action: "login", params: [allowRelogin: true, redirectTo: "user:index"])
		}
		// in case logged in and authorized, get user object
		AuthUser user = authService.getSessionUser()
		
		// extract parameters
		if (params.containsKey("submitted")) submitted = params['submitted']
		
		// if form submitted, validate fields
		// and try to update profile
		if (submitted) {
			// check whether an employee or customer
			if (user.type == AuthService.TYPE_CUSTOMER) {
				// validate name
				if (!params.containsKey("name") || params['name'].length() < 1) {
					errors.add("Name is not given")
				}
				// validate address
				if (!params.containsKey("address") || params['address'].length() < 1) {
					errors.add("Address is not given")
				}
				// validate home phone
				if (!params.containsKey("resident_phone") || params['resident_phone'].length() < 1) {
					errors.add("Home Phone is not given")
				}
				// validate mobile phone
				if (!params.containsKey("mobile_phone") || params['mobile_phone'].length() < 1) {
					errors.add("Mobile Phone is not given")
				}
				// validate email
				if (!params.containsKey("email_id") || params['email_id'].length() < 1) {
					errors.add("Email is not given")
				} else if (!params['email_id'].toString().contains("@") || !params['email_id'].toString().contains(".")) {
					errors.add("Invalid Email given")
				}
				// validate country
				if (!params.containsKey("country_id")) {
					errors.add("Country not selected")
				}
				// validate password
				Boolean updatePassword = false
				if (params.containsKey("password") && params['password'].length() > 0) {
					if (params['password'].length() < 2) {
						errors.add("Password too short")
					} else if (!params.containsKey("password_confirm") || !params['password'].equals(params['password_confirm'])) {
						errors.add("Passwords don't match")
					} else {
						updatePassword = true
					}
				}
				// try to update now
				if (errors.isEmpty()) {
					if (updatePassword) {
						groovySql.execute("UPDATE `customer` SET `password`=? WHERE `customer_id`=?", [params['password'], user.customerID])
					}
					user.name = params['name']
					groovySql.execute(
						"UPDATE `customer` SET `name`=?,`address`=?,`resident_phone`=?,`mobile_phone`=?,`email_id`=?,`country_id`=? WHERE `customer_id`=?",
						[
							params['name'],
							params['address'],
							params['resident_phone'],
							params['mobile_phone'],
							params['email_id'],
							params['country_id'],
							user.customerID
						]
					)
				}
			} else {
				// validate name
				if (!params.containsKey("name") || params['name'].length() < 1) {
					errors.add("Name is not given")
				}
				// validate password
				Boolean updatePassword = false
				if (params.containsKey("password") && params['password'].length() > 0) {
					if (params['password'].length() < 2) {
						errors.add("Password too short")
					} else if (!params.containsKey("password_confirm") || !params['password'].equals(params['password_confirm'])) {
						errors.add("Passwords don't match")
					} else {
						updatePassword = true
					}
				}
				// try to update now
				if (errors.isEmpty()) {
					if (updatePassword) {
						groovySql.execute("UPDATE `employee` SET `password`=? WHERE `employee_id`=?", [params['password'], user.employeeID])
					}
					user.name = params['name']
					groovySql.execute("UPDATE `employee` SET `name`=? WHERE `employee_id`=?", [params['name'], user.employeeID])
				}
			}
		}
		
		Map<Integer, String> countryList = new HashMap<Integer, String>()
		List<GroovyRowResult> rows = groovySql.rows("SELECT * FROM country ORDER BY country_name")
		for (GroovyRowResult row : rows) {
			countryList.put(row.country_id, row.country_name)
		}
		
		def customer = null
		def employee = null
		if (user.type == AuthService.TYPE_CUSTOMER) {
			GroovyRowResult row = groovySql.firstRow("SELECT customer.*,country.country_name FROM customer,country WHERE customer.customer_id=? AND country.country_id=customer.country_id", [user.customerID])
			if (row) {
				customer = [id:user.customerID, username:user.username, password:row.password, name:row.name, address:row.address, residentPhone:row.resident_phone, mobilePhone:row.mobile_phone, email:row.email_id, type:row.type, countryID:row.country_id, countryName:row.country_name]
			}
		} else {
			GroovyRowResult row = groovySql.firstRow("SELECT * FROM employee WHERE employee.employee_id=?", [user.employeeID])
			if (row) {
				employee = [id:user.employeeID, username:user.username, password:row.password, name:row.name, roleID:row.role_id]
			}
		}
		
		return [submitted: submitted, errors: errors, user: user, customer: customer, employee: employee, countries: countryList]
	}
	
	def login() {
		AuthUser user = null
		Boolean loginSuccess = false
		Boolean allowRelogin = false
		String username = null
		String password = null
		String redirectToController = "index"
		String redirectToAction = "index"
		
		// extract parameters
		if (params.containsKey("allowRelogin")) allowRelogin = params['allowRelogin']
		if (params.containsKey("username")) username = params['username']
		if (params.containsKey("password")) password = params['password']
		if (params.containsKey("redirectTo")) {
			String[] rtSplit = params['redirectTo'].split(":")
			if (rtSplit.length == 2) {
				redirectToController = rtSplit[0]
				redirectToAction = rtSplit[1]
			}
		}
		
		// check if re-login is not allowed and user already logged in
		if (!allowRelogin && authService.isLoggedIn()) {
			// user is logged in, redirect to the index page
			redirect(action: "index")
		}
		
		// check if login form submitted
		if (username != null && password != null && username.length() > 0) {
			// try to login
			user = authService.login(username, password)
			loginSuccess = user.loggedIn
			// logged in successfully?
			if (loginSuccess) {
				redirect(controller: redirectToController, action: redirectToAction)
			}
		}
		
		// if not logged in or if re-login is allowed, display login page
		return [allowRelogin: allowRelogin, redirectTo: redirectToController + ":" + redirectToAction]
	}
	
	def logout() {
		// log out
		authService.logout()
		// redirect to login page
		redirect(action: "login")
	}
	
	def register() {
		String redirectToController = "index"
		String redirectToAction = "index"
		Boolean submitted = false
		Boolean registerSuccess = false
		
		// extract parameters
		if (params.containsKey("submitted")) submitted = params['submitted']
		if (params.containsKey("redirectTo")) {
			String[] rtSplit = params['redirectTo'].split(":")
			if (rtSplit.length == 2) {
				redirectToController = rtSplit[0]
				redirectToAction = rtSplit[1]
			}
		}
		
		// check if logged in, in that case, just redirect to
		// wherever the user came from
		if (authService.isLoggedIn()) {
			redirect(controller: redirectToController, action: redirectToAction)
		}
		
		List<String> errors = []
		Map<Integer, String> countryList = new HashMap<Integer, String>()
		List<GroovyRowResult> rows = groovySql.rows("SELECT * FROM country ORDER BY country_name")
		for (GroovyRowResult row : rows) {
			countryList.put(row.country_id, row.country_name)
		}
		
		// if registration form submitted, validate fields
		// and try to enroll customer
		if (submitted) {
			// validate name
			if (!params.containsKey("name") || params['name'].length() < 1) {
				errors.add("Customer Name is not given")
			}
			// validate address
			if (!params.containsKey("address") || params['address'].length() < 1) {
				errors.add("Address is not given")
			}
			// validate home phone
			if (!params.containsKey("resident_phone") || params['resident_phone'].length() < 1) {
				errors.add("Home Phone is not given")
			}
			// validate mobile phone
			if (!params.containsKey("mobile_phone") || params['mobile_phone'].length() < 1) {
				errors.add("Mobile Phone is not given")
			}
			// validate email
			if (!params.containsKey("email_id") || params['email_id'].length() < 1) {
				errors.add("Email is not given")
			} else if (!params['email_id'].toString().contains("@") || !params['email_id'].toString().contains(".")) {
				errors.add("Invalid Email given")
			}
			// validate country
			if (!params.containsKey("country_id")) {
				errors.add("Country not selected")
			}
			// validate username
			if (!params.containsKey("username") || params['username'].length() < 1) {
				errors.add("User Id not given")
			} else if (authService.checkUserExists(params['username'])) {
				errors.add("User Id already taken")
			}
			// validate password
			if (!params.containsKey("password") || params['password'].length() < 1) {
				errors.add("Password is not given")
			}
			
			// if no errors, try to register
			if (errors.isEmpty()) {
				groovySql.execute(
					'INSERT INTO customer (user_id,password,name,address,resident_phone,mobile_phone,email_id,type,country_id) values (?,?,?,?,?,?,?,?,?)',
					[
						params['username'],
						params['password'],
						params['name'],
						params['address'],
						params['resident_phone'],
						params['mobile_phone'],
						params['email_id'],
						'S',
						params['country_id'],
					]
				)
				// registered, now try to login
				authService.login(params['username'], params['password'])
				// redirect
				redirect(controller: redirectToController, action: redirectToAction)
			}
		}
		
		return [redirectTo: redirectToController + ":" + redirectToAction, countries: countryList, errors: errors]
	}
}
