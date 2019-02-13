package com.tobi.junit;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.springframework.oxm.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.XmlMappingException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tobi.database.sqlservice.jaxb.SqlType;
import com.tobi.database.sqlservice.jaxb.Sqlmap;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/oxmTestContext.xml")
public class JaxbTest {
	@Autowired
	private Unmarshaller unmarshaller;

	@Test
	public void unmarshalSqlMapTest() throws XmlMappingException, IOException {
		Source xmlSource = new StreamSource(getClass().getClassLoader().getResourceAsStream("sqlmap.xml"));

		Sqlmap sqlmap = (Sqlmap)this.unmarshaller.unmarshal(xmlSource);

		List<SqlType> sqlList = sqlmap.getSql();
		assertEquals(6, sqlList.size());
		assertEquals("userAdd", sqlList.get(0).getKey());
		assertEquals("userGetAll", sqlList.get(2).getKey());
	}
}
