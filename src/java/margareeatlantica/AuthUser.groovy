package margareeatlantica

class AuthUser {
	Boolean loggedIn
	Integer type
	String username
	String name
	Integer role
	Integer customerID
	Integer employeeID
	
	AuthUser() {
		loggedIn = false
		type = 0
		username = ""
		name = ""
		role = 0
		customerID = 0
		employeeID = 0
	}
}
