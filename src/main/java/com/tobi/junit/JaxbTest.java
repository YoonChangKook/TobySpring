package com.tobi.junit;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.springframework.oxm.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.XmlMappingException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tobi.config.OxmTestContext;
import com.tobi.database.sqlservice.OxmSqlService;
import com.tobi.database.sqlservice.jaxb.SqlType;
import com.tobi.database.sqlservice.jaxb.Sqlmap;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = OxmTestContext.class)
@ActiveProfiles("test")
public class JaxbTest {
	@Autowired
	private Unmarshaller unmarshaller;

	@Autowired
	private OxmSqlService oxmSqlService;

	@Test
	public void unmarshalSqlMapTest() throws XmlMappingException, IOException {
		Source xmlSource = new StreamSource(getClass().getClassLoader().getResourceAsStream("sqlmap.xml"));

		Sqlmap sqlmap = (Sqlmap)this.unmarshaller.unmarshal(xmlSource);

		List<SqlType> sqlList = sqlmap.getSql();
		assertEquals(6, sqlList.size());
		assertEquals("userAdd", sqlList.get(0).getKey());
		assertEquals("userGetAll", sqlList.get(2).getKey());
	}

	@Test
	public void sqlServiceTest() {
		assertEquals("select * from users order by id", this.oxmSqlService.getSql("userGetAll"));
		assertEquals("delete from users", this.oxmSqlService.getSql("userDeleteAll"));
	}
}
