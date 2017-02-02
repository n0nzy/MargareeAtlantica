// Place your Spring DSL code here
beans = {
	// Create Spring bean for Groovy SQL.
	// groovySql is the name of the bean and can be used
	// for injection.
	groovySql(groovy.sql.Sql, ref('dataSource'))
}
