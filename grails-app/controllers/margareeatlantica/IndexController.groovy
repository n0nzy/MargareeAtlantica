package margareeatlantica


import groovy.sql.GroovyRowResult;
import groovy.sql.Sql;

class IndexController {
	// Reference to authService defined in AuthService.groovy (grails-app/services/margareeatlantica/AuthService.groovy)
	AuthService authService = AuthService.getInstance()

    def index() {
		// make sure we have a logged in user (customer or employee)
		if (!authService.isAuthorizedAs(AuthService.ROLE_ANY_USER)) {
			redirect(controller: "user", action: "login", params: [allowRelogin: true, redirectTo: "index:index"])
		}
		// in case logged in and authorized, get user object
		AuthUser user = authService.getSessionUser()
		
		// redirect to user-specific controller depending on the user role
		switch (user.role) {
			case AuthService.ROLE_CUSTOMER:
				redirect(controller: "customer")
				break;
			case AuthService.ROLE_CUSTOMER_RELATIONSHIP_MANAGER:
				redirect(controller: "crm")
				break;
			case AuthService.ROLE_ORDER_MANAGER:
				redirect(controller: "om")
				break;
			case AuthService.ROLE_PRODUCT_MANAGER:
				redirect(controller: "pm")
				break;
		}
	}
}
