package com.sample.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UtilsService {
	
	public Object command(String command) throws IOException, InterruptedException {
		StringBuilder str = new StringBuilder();
		Process process = Runtime.getRuntime().exec(command);
		new BufferedReader(new InputStreamReader(process.getInputStream())).lines()
        .forEach(p -> str.append(p + "</br>"));
		str.append("[exitCode = " + process.waitFor() +"]");
		log.info("returned : {}", str.toString());
		return str.toString();
	}
	
	@Autowired
	EntityManager entityManager;
	
	public Object sql(String sql) {
		StringBuilder str = new StringBuilder("<table style='width:100%'>");
		@SuppressWarnings("unchecked")
		List<Object[]> objs = entityManager.createNativeQuery(sql).getResultList();
		if(!objs.isEmpty()) {
			objs.forEach(p -> {;
				log.info(p.toString());
				str.append("<tr>");
				for (Object col  : p) {
					str.append("<td>" + col + "</td>");
				}
				str.append("</tr>");
			});
		} else {
			str.append("<tr><td>No records found</td></tr>");
		}
		str.append("</table>");
		return str;
	}
}
