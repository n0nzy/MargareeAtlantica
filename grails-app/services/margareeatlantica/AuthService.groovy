package margareeatlantica

import groovy.sql.GroovyRowResult
import groovy.sql.Sql

import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.RequestAttributes


class AuthService {
	def groovySql
	
	static final String SESSION_KEY_AUTH_USER = 'ma.authenticatedUser'
	static final String REQUEST_KEY_AUTH_USER = 'ma.authenticatedUser'
	
	static final int ROLE_ANY_USER = 15
	static final int ROLE_CUSTOMER = 1
	static final int ROLE_EMPLOYEE = 14
	static final int ROLE_CUSTOMER_RELATIONSHIP_MANAGER = 2
	static final int ROLE_ORDER_MANAGER = 4
	static final int ROLE_PRODUCT_MANAGER = 8
	
	static final int TYPE_CUSTOMER = 1
	static final int TYPE_EMPLOYEE = 2
	
	protected static final AuthService _instance = new AuthService()
	
	
	protected AuthService() {
		// private constructor, ensuring a singleton class
	}
	
	static AuthService getInstance() {
		// return the singleton object
		return _instance;
	}
	
	AuthUser getSessionUser() {
		AuthUser user = null
		def attribs = RequestContextHolder.requestAttributes
		if (attribs) {
			user = attribs.request.session.getAttribute(SESSION_KEY_AUTH_USER)
		}
		// no session user object, create a default one (not logged in)
		if (!user) {
			user = new AuthUser()
			setSessionUser(user)
		}
		return user
	}
	
	protected void setSessionUser(AuthUser user) {
		def attribs = RequestContextHolder.requestAttributes
		if (attribs) {
			attribs.request.session.setAttribute(SESSION_KEY_AUTH_USER, user)
		}
	}
	
	boolean isAuthorizedAs(int userRole) {
		AuthUser user = getSessionUser()
		// check if there's a logged in user and its role is
		// compatible with the supplied userRole
		return (user.loggedIn && ((user.role | userRole) == userRole))
	}

    boolean isLoggedIn() {
		AuthUser user = getSessionUser()
		return user.loggedIn
    }
	
	boolean checkUserExists(String username) {
		boolean exists = false
		
		GroovyRowResult sqlResult = groovySql.firstRow("SELECT * from customer WHERE user_id = ?", [username])
		// is it a customer?
		if (sqlResult) {
			exists = true
		} else {
			// if not a customer, is it an employee?
			sqlResult = groovySql.firstRow("SELECT * from employee WHERE user_id = ?", [username])
			if (sqlResult) {
				exists = true
			}
		}
		
		return exists
	}
	
	AuthUser login(String username, String password) {
		AuthUser user = new AuthUser()
		
		GroovyRowResult sqlResult = groovySql.firstRow("SELECT * from customer WHERE user_id = ? AND password = ?", [username, password])
		// is it a customer?
		if (sqlResult) {
			user.loggedIn = true
			user.customerID = sqlResult.customer_id
			user.username = username
			user.type = TYPE_CUSTOMER
			user.role = ROLE_CUSTOMER
			user.name = sqlResult.name
		} else {
			// if not a customer, is it an employee?
			sqlResult = groovySql.firstRow("SELECT employee.*,role.role_description from employee,role WHERE employee.user_id = ? AND employee.password = ? AND role.role_id = employee.role_id", [username, password])
			if (sqlResult) {
				user.loggedIn = true
				user.employeeID = sqlResult.employee_id
				user.username = username
				user.type = TYPE_EMPLOYEE
				if (sqlResult.role_description.equalsIgnoreCase("Customer Relationship Manager")) {
					user.role = ROLE_CUSTOMER_RELATIONSHIP_MANAGER
				} else if (sqlResult.role_description.equalsIgnoreCase("Order Manager")) {
					user.role = ROLE_ORDER_MANAGER
				} else {
					user.role = ROLE_PRODUCT_MANAGER
				}
				user.name = sqlResult.name
			}
		}
		
		// set session user value
		setSessionUser(user)
		
		return user
	}
	
	void logout() {
		AuthUser user = new AuthUser()
		setSessionUser(user)
	}
}
