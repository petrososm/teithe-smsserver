package com.smsserver.configuration;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.smsserver.sql.LocalDb;
import com.smsserver.models.gunetapi.*;
import com.smsserver.models.services.*;
public class ServicesOnLoad {

	static HashMap<String, Service> services = new HashMap<String, Service>();


	public static void loadQuestions() {
		try {
			JAXBContext jc = JAXBContext.newInstance(ServicesRoot.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			File questionXML = new File("/usr/share/tomcat/webapps/sms/WEB-INF/services.xml");
			if (!questionXML.exists())
				questionXML = new File("D:/Documents/GitHub/sms-server/target/SMS_Server-0.1/WEB-INF/services.xml");
			ServicesRoot servicesRoot = (ServicesRoot) unmarshaller.unmarshal(questionXML);

			for (Service s : servicesRoot.service) {
				if(s!=null){//se periptwsi poy ginei lathos parsing min paralisei to simpan
					s.queryParams = s.query.length() - s.query.replace("?", "").length();//metraei ta ? sto query gia na vgalei arithmo parametrwn
					if(s.extraKeyword != null)
						for(ExtraKeyword ek:s.extraKeyword)
							ek.queryParams=ek.query.length() - ek.query.replace("?", "").length();
					
					if(s.type.equalsIgnoreCase("MobileOriginated")){
						for(String k:s.keywords.keywordList)
							services.put(k, s); // fortwnei sto hastmap ola ta
					}								// questions me index ta keyword
					services.put(s.serviceId, s);//vazei sto hashmap kai to serviceId gia na xrisimopoieitai kai se MO k MT
					System.out.println(s);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static HashMap<String, Service> getServices() {
		return services;
	}
	


}
