package com.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weather.WeatherConfig.MsgGateway;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


@SpringBootApplication
@Configuration
@EnableIntegration
@Slf4j
public class WeatherApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(WeatherApplication.class, args);
	}
	
	@RestController
	public static class Controller {

		@Autowired
		private MsgGateway msgGateway;

		@GetMapping(path = "/weather")
		public String send(@RequestParam("country") String country) {
			log.info("send...{}", country);
//			return msgGateway.send(Request.builder().countryName(country).build());
			return msgGateway.send(country);
		}
	}

	@Data
	@Builder
	public static class Request {
		private String countryName;
	}
}


----------------

package com.weather;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.interceptor.WireTap;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.handler.LoggingHandler.Level;
import org.springframework.integration.http.outbound.HttpRequestExecutingMessageHandler;
import org.springframework.integration.http.support.DefaultHttpHeaderMapper;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.client.ResponseErrorHandler;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

@Slf4j
@Profile("default")
@Configuration
public class WeatherConfig {


	@MessagingGateway(defaultRequestChannel = "channel")
	public static interface MsgGateway {
		//String send(@Payload Request payload);
		String send(@Payload String payload);
	}
	
	@Bean
	public MessageChannel channel(@Autowired WireTap wireTap) {
		return MessageChannels.direct().interceptor(wireTap).get();
	}

	@Bean
	@ServiceActivator(inputChannel = "channel")
	public MessageHandler handleMsg() {

		HttpRequestExecutingMessageHandler messageHandler = new HttpRequestExecutingMessageHandler(
				"http://www.webservicex.net/globalweather.asmx/GetCitiesByCountry?CountryName={CountryName}");
		SpelExpressionParser expressionParser = new SpelExpressionParser();
		Map<String, Expression> uriVariableExpressions = new HashMap<String, Expression>();
		uriVariableExpressions.put("CountryName", expressionParser.parseExpression("payload"));

		DefaultHttpHeaderMapper defaultHttpHeaderMapper = new DefaultHttpHeaderMapper();
		defaultHttpHeaderMapper.setOutboundHeaderNames(new String[] { "" });
		defaultHttpHeaderMapper.setUserDefinedHeaderPrefix("");

		messageHandler.setRequestFactory(ntbBufferingClientHttpRequestFactory());
		messageHandler.setHeaderMapper(defaultHttpHeaderMapper);
		messageHandler.setExpectedResponseType(String.class);
		messageHandler.setErrorHandler(new ResponseErrorHandler() {

			@Override
			public boolean hasError(ClientHttpResponse response) throws IOException {
				log.error("hasError ::: {}", response.toString());
				return false;
			}

			@Override
			public void handleError(ClientHttpResponse response) throws IOException {
				log.error("handleError ::: {}", response.toString());
			}
		});

		// messageHandler.setOutputChannel(fxRateReplyChannel());
		messageHandler.setUriVariableExpressions(uriVariableExpressions);
		messageHandler.setHttpMethod(HttpMethod.GET);
		messageHandler.setEncodeUri(false);

		return messageHandler;
	}

	@Bean
	public BufferingClientHttpRequestFactory ntbBufferingClientHttpRequestFactory() {
		return new BufferingClientHttpRequestFactory(ntbOkHttpClientHttpRequestFactory());
	}

	@Bean
	public OkHttp3ClientHttpRequestFactory ntbOkHttpClientHttpRequestFactory() {
		OkHttp3ClientHttpRequestFactory okClientRequestFactory = new OkHttp3ClientHttpRequestFactory(
				new OkHttpClient());
		okClientRequestFactory.setReadTimeout(60 * 100);
		okClientRequestFactory.setConnectTimeout(60 * 100);
		okClientRequestFactory.setWriteTimeout(60 * 100);
		return okClientRequestFactory;
	}
	
	
	// logging ------
	
	  @Bean
	  public WireTap wireTap() {
	    return new WireTap(gatewayLogging());
	  }

	  @ServiceActivator(inputChannel = "gatewayLogging")
	  @Bean
	  public LoggingHandler loggingHandler() {
	    LoggingHandler loggingHandler = new LoggingHandler(Level.DEBUG);
	    loggingHandler.setShouldLogFullMessage(true);
	    return loggingHandler;
	  }

	  @Bean
	  public MessageChannel gatewayLogging() {
	    return new DirectChannel();
	  }
}

-------------

package com.weather;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.dsl.IntegrationFlow;

@Profile("dsl")
@Configuration
public class WeatherDslConfig {
	
	public IntegrationFlow flow () {
		return f -> f.transform(payload ->
        "<FahrenheitToCelsius xmlns=\"https://www.w3schools.com/xml/\">"
      +     "<Fahrenheit>" + payload + "</Fahrenheit>"
      + "</FahrenheitToCelsius>");
	}
}

----------------
buildscript {
	ext {
		springBootVersion = '2.0.0.RELEASE'
	}
	repositories {
		mavenCentral()
	}
	dependencies {
		classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
	}
}

apply plugin: 'java'
apply plugin: 'eclipse'
apply plugin: 'org.springframework.boot'
apply plugin: 'io.spring.dependency-management'

group = 'com.weather'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = 1.8

repositories {
	mavenCentral()
	maven { url "https://repo.spring.io/milestone" }
}


ext {
	springCloudVersion = 'Finchley.M8'
}

dependencies {
	compile('org.springframework.boot:spring-boot-starter-integration')
	compile("org.springframework.integration:spring-integration-java-dsl:1.2.3.RELEASE")
	compile('org.springframework.integration:spring-integration-http')
	compile('org.springframework.integration:spring-integration-ws')
	
	compile('org.springframework.boot:spring-boot-starter-web')
	compile('org.springframework.cloud:spring-cloud-starter-netflix-zuul')
	
	compile("com.squareup.okhttp3:okhttp:3.10.0")
	compile "org.projectlombok:lombok:1.16.20"
	
	testCompile('org.springframework.boot:spring-boot-starter-test')
}

dependencyManagement {
	imports {
		mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
	}
}


-------------------

  @Bean
  @ServiceActivator(inputChannel = "abc")
  public MessageHandler acctHolderInqOutbound() throws Exception {

    AcctHolderInqMarshaller marshaller = new AcctHolderInqMarshaller(statusCodes);
    marshaller.setContextPaths("com.schema",
        "com.schema");

    MarshallingWebServiceOutboundGateway outboundGateway =
        new MarshallingWebServiceOutboundGateway(acctHolderInqUrl, marshaller, marshaller);
    WebServiceMessageCallback messageCallback = new SoapActionCallback(soapAction);

    outboundGateway.setSendTimeout(defaultTimeOut);
    outboundGateway.setMessageSender(fwkNtbSoiRequestSender);
    logger.debug("uri::{} \n\r soapAction:: {}", acctHolderInqUrl, soapAction);

    DefaultSoapHeaderMapper defHeader = new DefaultSoapHeaderMapper();
    defHeader.setRequestHeaderNames("SOAPAction");

    SaajSoapMessageFactory saajSoapMessageFactory = new SaajSoapMessageFactory();
    saajSoapMessageFactory.setMessageFactory(MessageFactory.newInstance());
    saajSoapMessageFactory.setSoapVersion(SoapVersion.SOAP_12);
    saajSoapMessageFactory.afterPropertiesSet();

    outboundGateway.setMessageFactory(saajSoapMessageFactory);
    outboundGateway.setHeaderMapper(defHeader);
    outboundGateway.setInterceptors(new AcctHolderInqInterceptor(msgVersion, srvVersion, clientId, clientOrg,
      clientCtry, operatorRole, operatorLoginId, ServerIpAddress.get()));
    outboundGateway.setRequestCallback(messageCallback);

    logger.debug("created messagehandler for::{}", acctHolderInqUrl);

    return outboundGateway;
  }

  @Bean
  public MessageChannel abc() {
    return new DirectChannel();
  }
  
  ------------------
  
  
  
  public class AcctHolderInqMarshaller extends Jaxb2Marshaller {
  
  private List<String> statusCodeLs;
  
  public AcctHolderInqMarshaller(String statusCodes) {
    this.statusCodeLs = Arrays.asList(statusCodes.split("\\|"));
  }

  @Override
  public Object unmarshal(Source source) throws XmlMappingException {
    return super.unmarshal(source, null);
  }

  @Override
  public Object unmarshal(Source source, MimeContainer mimeContainer) throws XmlMappingException {
    logger.debug("Inside Custom JaxbWrapper unmarshal");
    Object mimeMessage = new DirectFieldAccessor(mimeContainer).getPropertyValue("mimeMessage");
    Object unmarshalObject = null;
    if (mimeMessage instanceof SaajSoapMessage) {
      SaajSoapMessage soapMessage = (SaajSoapMessage) mimeMessage;
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      try {
        soapMessage.writeTo(out);
      } catch (IOException e) {
        logger.error("Error Unmarshalling:" + e.getMessage());
        throw convertJaxbException(new JAXBException(e.getMessage()));
      }
      String responseMsg = new String(out.toByteArray());
      logger.info("response envelope: \n\r" + responseMsg);

      String faultReason = soapMessage.getFaultReason();
      if (StringUtils.isNotBlank(faultReason)) {
        Source detailSource = (Source) soapMessage
            .getEnvelope().getBody().getFault().getFaultDetail().getDetailEntries().next().getSource();
        DetailInfo detailInfo = (DetailInfo) this.unmarshal(detailSource);
        
        logger.info("Status Code: " + detailInfo.getStatusCode());
        if(statusCodeLs.contains(detailInfo.getStatusCode())) {
          logger.info("Account Holder profile not found");
          return new AcctHolderDetlInqResponse(); // Spring integration not support NULL return
        } else {
          logger.error("Response got FAULT, faultReason::" + faultReason);
          throw convertJaxbException(new JAXBException(faultReason));
        }
      } else {
        logger.info("Response Success");
        unmarshalObject = super.unmarshal(source, mimeContainer);
      }
    }
    return unmarshalObject;
  }
  
}
-------------------
public class ABC implements ClientInterceptor {

  
  
  private String msgVersion;

  private String srvVersion;

  private String clientId;

  private String clientOrg;

  private String clientCtry;

  private String operatorRole;
  
  private String operatorLoginId;
  
  private String operatorInternalId;
  
  public AcctHolderInqInterceptor(String msgVersion, String srvVersion, String clientId,
      String clientOrg, String clientCtry, String operatorRole, String operatorLoginId, String operatorInternalId) {
    this.msgVersion = msgVersion;
    this.srvVersion = srvVersion;
    this.clientId = clientId;
    this.clientOrg = clientOrg;
    this.clientCtry = clientCtry;
    this.operatorRole = operatorRole;
    this.operatorLoginId = operatorLoginId;
    this.operatorInternalId = operatorInternalId;
  }
  
  @Override
  public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
    SoapMessage soapMessage = (SoapMessage) messageContext.getRequest();
    logger.info("SOAPAction::{}", soapMessage.getSoapAction());
    try {
      
      SoapHeader sh = soapMessage.getSoapHeader();
      JAXBContext context = JAXBContext.newInstance(MsgDetl.class, Trace.class);
      Marshaller marshaller = context.createMarshaller();
      
      MsgDetl msgDetail = new MsgDetl();
      msgDetail.setMsgVersion(msgVersion);
      
      msgDetail.setMsgUID(UUID.randomUUID().toString().replace("-", StringUtils.EMPTY));
      msgDetail.setSvcVersion(srvVersion);
      marshaller.marshal(msgDetail, sh.getResult());
      
      Trace trace = new Trace();
      GregorianCalendar currentDate = new GregorianCalendar();
      currentDate.setTime(LocalDateTime.now().toDate());
      
      trace.setRqDateTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(currentDate));
      
      RqClient rqClient = new RqClient();
      rqClient.setRqClientId(clientId);
      rqClient.setRqClientOrg(clientOrg);
      rqClient.setRqClientCtry(clientCtry);
      trace.setRqClient(rqClient);
      
      Operator operator = new Operator();
      operator.setOpInternalId(operatorInternalId);
      operator.setOpLoginId(operatorLoginId);
      operator.setOpRole(operatorRole);
      trace.setOperator(operator);
      
      marshaller.marshal(trace, sh.getResult());

      ByteArrayOutputStream out = new ByteArrayOutputStream();
      soapMessage.writeTo(out);
      String strMsg = new String(out.toByteArray());
      logger.info("request envelope: \n\r {}", strMsg);
      
    } catch (IOException| JAXBException | DatatypeConfigurationException ex) {
      
      throw new WebServiceFaultException(ex.getMessage());
    } 
    return true;
  }

  @Override
  public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
    return false;
  }

  @Override
  public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
    return false;
  }

  @Override
  public void afterCompletion(MessageContext messageContext, Exception ex)
      throws WebServiceClientException {
  }
}
