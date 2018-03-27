@Test
	public void testAddBySoap() {
		Assert.assertEquals("Add Opertion is failed", (long)300, (long)gateway.add(100, 200));
	}

--
@Gateway(requestChannel="addSoapChannel", payloadExpression = "#args[0] + ':' + #args[1]")
	public Integer add(Integer num1, Integer num2);
--

// -- SOAP --
	@Bean
	@ServiceActivator(inputChannel = "addSoapChannel")
	public MessageHandler soapChannelHandler() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("com.integration.soap.obj");
		
		MarshallingWebServiceOutboundGateway outboundGateway = new MarshallingWebServiceOutboundGateway(env.getProperty("soap.add.uri"), marshaller);
		WebServiceMessageCallback messageCallback = new SoapActionCallback(env.getProperty("soap.add.action"));
		
		SaajSoapMessageFactory saajSoapMessageFactory = new SaajSoapMessageFactory();
		saajSoapMessageFactory.setSoapVersion(SoapVersion.SOAP_12);
		saajSoapMessageFactory.afterPropertiesSet();
		
		DefaultSoapHeaderMapper defHeader = new DefaultSoapHeaderMapper();
		defHeader.setRequestHeaderNames("SOAPAction");

		outboundGateway.setMessageFactory(saajSoapMessageFactory);
		outboundGateway.setHeaderMapper(defHeader);
		outboundGateway.setRequestCallback(messageCallback);

		return outboundGateway;
	}
	  
	@Bean
	public IntegrationFlow addNumber() {
		return IntegrationFlows.from("addSoapChannel")
				.<String, Add>transform(payload -> Add.builder().intA(Integer.parseInt(payload.split(":")[0]))
																.intB(Integer.parseInt(payload.split(":")[1]))
																.build())
				.handle(soapChannelHandler())
				.transform(Transformers.toJson())
				.transform("#jsonPath(payload, '$.addResult')")
				.get();
	}
	
--
soap: 
  add: 
    uri: http://www.dneonline.com/calculator.asmx
    action: http://tempuri.org/Add
--
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "Add")
public class Add {
