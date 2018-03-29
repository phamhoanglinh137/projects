package com.integration.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.integration.AppGateway;
import com.integration.Application;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes={Application.class})
public class ApplicationTests {
	
	@BeforeClass
	public static void init() {
		
	}
	
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext wac;
	
	@Before
	public void setup() throws Exception {
	    this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}
	
	
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
	
	@Test
	public void testAddBySoap() {
		Assert.assertEquals("Add Opertion is failed", (long)300, (long)gateway.add(100, 200));
	}
	
	@Test
	public void testCitynameInBound() throws Exception {
		this.mockMvc.perform(get("/city/london").contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().is2xxSuccessful());
	       
	}
}
