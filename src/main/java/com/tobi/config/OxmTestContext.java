package com.tobi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.tobi.database.sqlservice.OxmSqlService;
import com.tobi.database.sqlservice.SqlService;

@Configuration
@Profile("test")
public class OxmTestContext {
	@Bean
	public SqlService oxmSqlService() {
		OxmSqlService sqlService = new OxmSqlService();
		sqlService.setUnmarshaller(unmarshaller());
		return sqlService;
	}

	@Bean
	public Unmarshaller unmarshaller() {
		Jaxb2Marshaller unmarshaller = new Jaxb2Marshaller();
		unmarshaller.setContextPath("com.tobi.database.sqlservice.jaxb");
		return unmarshaller;
	}
}
