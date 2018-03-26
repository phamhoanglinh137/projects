package com.integration.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.integration.AppGateway;
import com.integration.Application;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes={Application.class})
public class ApplicationTests {
	
	@Autowired
	private AppGateway gateway;
	
	@Test
	public void testGetByCityName() {
		Map<String, String> variables = new HashMap<String, String>();
		variables.put("city","london");
		Assert.assertNotNull("Get Weather by city name is failed", gateway.getByCityName(variables));
	}
	
	@Test
	public void testGetPersonInfo() {
		String nric="G4444444N";
		Assert.assertNotNull("send JMS outbound gateway failed", gateway.getPersonInfo(nric));
		Assert.assertEquals("NRIC retured is not expected",nric, gateway.getPersonInfo(nric).getNric());
	}
}
