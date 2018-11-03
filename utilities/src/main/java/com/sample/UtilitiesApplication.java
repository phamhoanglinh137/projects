package com.sample;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sample.util.UtilsService;

@RestController
@SpringBootApplication
public class UtilitiesApplication {

	public static void main(String[] args) {
		SpringApplication.run(UtilitiesApplication.class, args);
	}
	
	@Autowired
	public UtilsService utilService;
	
	@GetMapping(value = "/exec", produces = "text/html")
	public Object exec(@RequestParam("command") String sql) {
		try {
			return utilService.command(sql);
		} catch(Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			return sw.toString();
		}
	}
	
	@GetMapping(value = "/sql", produces = "text/html")
	public Object getSql(@RequestParam("sql") String sql) {
		try {
			return utilService.sql(sql);
		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			return sw.toString();
		}
	}
	
}
