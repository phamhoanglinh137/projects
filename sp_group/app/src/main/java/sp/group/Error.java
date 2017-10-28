package sp.group;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author linhpham
 * 
 * contains all <error_code, message> defined in errors_msg.properties
 */
public enum Error {
	INTERNAL_SYSTEM_ERR, 
	
	VALIDATION_ERR,
	
	EMAIL_NOT_EXISTED;
	
	private final Logger logger = LoggerFactory.getLogger(Error.class);
	
	private static final String FILE = "errors_msg.properties";
	
	private static Properties properties;
	
	private String message;
	
	public String message() {
		if(message == null) {
			init();
		}
		return message;
	}
	
	private void init() {
		if(properties == null) {
			properties = new Properties();
			try {
				Reader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream(FILE),"UTF-8");
				properties.load(reader);
			} catch (IOException ex) {
				logger.error("Unable to load {} from classpath", FILE);
			}
		}
		message = (String) properties.get(this.toString());
	}
}
