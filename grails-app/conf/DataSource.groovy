dataSource {
    pooled = true
    driverClassName = "com.mysql.jdbc.Driver"
    dialect = "org.hibernate.dialect.MySQL5InnoDBDialect"
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
}
// environment specific settings
environments {
	development {
		dataSource {
			dbCreate = "update" // one of 'create', 'create-drop','update'
			url = "jdbc:mysql://108.62.143.68/margareeatlantica?useUnicode=yes&characterEncoding=UTF-8"
			username = "ma"
			password = "ma"
		}
		hibernate {
			show_sql = true
		}
	}
	test {
		dataSource {
			dbCreate = "update" // one of 'create', 'create-drop','update'
			url = "jdbc:mysql://108.62.143.68/margareeatlantica?useUnicode=yes&characterEncoding=UTF-8"
			username = "ma"
			password = "ma"
		}
	}
	production {
		dataSource {
			dbCreate = "update"
			url = "jdbc:mysql://108.62.143.68/margareeatlantica?useUnicode=yes&characterEncoding=UTF-8"
			username = "ma"
			password = "ma"
		}
	}
}
