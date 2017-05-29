package com.smsserver.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.smsserver.models.services.mobileoriginated.ExtraKeyword;
import com.smsserver.models.services.mobileoriginated.MobileOriginatedService;
import com.smsserver.models.services.mobileterminated.Message;
import com.smsserver.models.services.mobileterminated.MobileTerminatedService;
import com.smsserver.models.site.ServiceDescription;
import com.smsserver.models.site.SmsTemplate;
public class ServicesOnLoad {

	static HashMap<String, MobileTerminatedService> mobileTerminatedServices = new HashMap<String, MobileTerminatedService>();
	static HashMap<String, MobileOriginatedService> mobileOriginatedServices = new HashMap<String, MobileOriginatedService>();
	static ArrayList<ServiceDescription> descriptions=new ArrayList<ServiceDescription>();
	static ArrayList<SmsTemplate> smsTemplates=new ArrayList<SmsTemplate>();


	public static HashMap<String, MobileOriginatedService> getMobileOriginatedServices() {
		return mobileOriginatedServices;
	}

	public static ArrayList<ServiceDescription> getDescriptions() {
		return descriptions;
	}

	public static ArrayList<SmsTemplate> getSmsTemplates() {
		return smsTemplates;
	}

	static void loadMobileOriginated() {
		try {
			JAXBContext jc = JAXBContext.newInstance(com.smsserver.models.services.mobileoriginated.Root.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			File questionXML = new File("/usr/share/tomcat/webapps/sms/WEB-INF/mobileoriginated.xml");
			if (!questionXML.exists())
				questionXML = new File("D:/Documents/GitHub/teithe-smsserver/target/sms-0.0.1-SNAPSHOT/WEB-INF/mobileoriginated.xml");
			com.smsserver.models.services.mobileoriginated.Root moRoot = (com.smsserver.models.services.mobileoriginated.Root) unmarshaller.unmarshal(questionXML);

			for (MobileOriginatedService s : moRoot.mobileOriginatedService) {
				if(s!=null){//se periptwsi poy ginei lathos parsing min paralisei to simpan
					s.queryParams = s.query.length() - s.query.replace("?", "").length();//metraei ta ? sto query gia na vgalei arithmo parametrwn
					if(s.extraKeyword != null)
						for(ExtraKeyword ek:s.extraKeyword)
							ek.queryParams=ek.query.length() - ek.query.replace("?", "").length();

						if(s.userInput==null)
							descriptions.add(new ServiceDescription("TEITHE "+ s.keywords.keywordList.get(0),s.description));
						else
							descriptions.add(new ServiceDescription("TEITHE "+ s.keywords.keywordList.get(0)+" <"+s.userInput+">",s.description));
						
						for(String k:s.keywords.keywordList)
							mobileOriginatedServices.put(k, s); // fortwnei sto hastmap ola ta	
						if(s.extraKeyword != null)
							for(ExtraKeyword ek:s.extraKeyword)
								descriptions.add(new ServiceDescription("TEITHE "+s.keywords.keywordList.get(0)+" "+ ek.keyword ,ek.description));

													// questions me index ta keyword
					mobileOriginatedServices.put(s.serviceId, s);//vazei sto hashmap kai to serviceId gia na xrisimopoieitai kai se MO k MT
					System.out.println(s);
					
				}
			}
			System.out.println(descriptions);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static void loadMobileTerminated(){
		try {
			JAXBContext jc = JAXBContext.newInstance(com.smsserver.models.services.mobileterminated.Root.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			File mtxml = new File("/usr/share/tomcat/webapps/sms/WEB-INF/mobileterminated.xml");
			if (!mtxml.exists())
				mtxml = new File("D:/Documents/GitHub/teithe-smsserver/target/sms-0.0.1-SNAPSHOT/WEB-INF/mobileterminated.xml");
			com.smsserver.models.services.mobileterminated.Root mtRoot = (com.smsserver.models.services.mobileterminated.Root) unmarshaller.unmarshal(mtxml);

			for (MobileTerminatedService s : mtRoot.mobileTerminatedService) {
					for(Message m : s.messages){
						m.replacements=m.messageId.length() - m.messageId.replace("?", "").length();
						mobileTerminatedServices.put(m.messageId, s);
						smsTemplates.add(new SmsTemplate(m.messageId,m.message));
					}		
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(mobileTerminatedServices);
	}

	public static HashMap<String, MobileOriginatedService> getServices() {
		return mobileOriginatedServices;
	}

	public static HashMap<String, MobileTerminatedService> getMobileTerminatedServices() {
		return mobileTerminatedServices;
	}


	


}
