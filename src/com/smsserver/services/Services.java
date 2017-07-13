package com.smsserver.services;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.smsserver.controllers.models.site.ServiceDescription;
import com.smsserver.controllers.models.site.SmsTemplate;
import com.smsserver.services.models.mobileoriginated.ExtraKeyword;
import com.smsserver.services.models.mobileoriginated.MobileOriginatedService;
import com.smsserver.services.models.mobileterminated.Message;
import com.smsserver.services.models.mobileterminated.MobileTerminatedService;


@Singleton
public class Services {

	 private HashMap<String, MobileTerminatedService> mobileTerminatedServices = new HashMap<String, MobileTerminatedService>();
	 private HashMap<String, MobileOriginatedService> mobileOriginatedServices = new HashMap<String, MobileOriginatedService>();
	 private ArrayList<ServiceDescription> descriptions=new ArrayList<ServiceDescription>();
	 private ArrayList<SmsTemplate> smsTemplatesMoodle=new ArrayList<SmsTemplate>();
	 private ArrayList<SmsTemplate> smsTemplatesDirect=new ArrayList<SmsTemplate>();

	@PostConstruct
	private void init(){
		this.loadMobileOriginated();
		this.loadMobileTerminated();
	}

	public  HashMap<String, MobileOriginatedService> getMobileOriginatedServices() {
		return mobileOriginatedServices;
	}

	public  ArrayList<ServiceDescription> getDescriptions() {
		return descriptions;
	}

	public  ArrayList<SmsTemplate> getSmsTemplatesMoodle() {
		return smsTemplatesMoodle;
	}

	public  ArrayList<SmsTemplate> getSmsTemplatesDirect() {
		return smsTemplatesDirect;
	}
	

	

	public  void loadMobileOriginated() {
		try {
			JAXBContext jc = JAXBContext.newInstance(com.smsserver.services.models.mobileoriginated.Root.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			ClassLoader classLoader = Services.class.getClassLoader();
			File questionXML = new File(classLoader.getResource("mobileoriginated.xml").getFile());

			com.smsserver.services.models.mobileoriginated.Root moRoot = (com.smsserver.services.models.mobileoriginated.Root) unmarshaller.unmarshal(questionXML);

			for (MobileOriginatedService s : moRoot.mobileOriginatedService) {
				if(s!=null){//se periptwsi poy ginei lathos parsing min paralisei to simpan
					if(s.query!=null)
						s.queryParams = s.query.length() - s.query.replace("?", "").length();//metraei ta ? sto query gia na vgalei arithmo parametrwn
					if(s.extraKeyword != null){
						for(ExtraKeyword ek:s.extraKeyword)
							if(ek.query!=null)
								ek.queryParams=ek.query.length() - ek.query.replace("?", "").length();
					}
					
					if(s.userInput!=null)
							descriptions.add(new ServiceDescription("TEITHE "+ s.keywords.keywordList.get(0)+" <"+s.userInput+">",s.description));
					else
						descriptions.add(new ServiceDescription("TEITHE "+ s.keywords.keywordList.get(0),s.description));
						
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadMobileTerminated(){
		try {
			JAXBContext jc = JAXBContext.newInstance(com.smsserver.services.models.mobileterminated.Root.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			ClassLoader classLoader = Services.class.getClassLoader();
			File mtxml = new File(classLoader.getResource("mobileterminated.xml").getFile());
			com.smsserver.services.models.mobileterminated.Root mtRoot = (com.smsserver.services.models.mobileterminated.Root) unmarshaller.unmarshal(mtxml);

			for (MobileTerminatedService s : mtRoot.mobileTerminatedService) {
					for(Message m : s.messages){
						m.replacements=m.messageId.length() - m.messageId.replace("?", "").length();
						mobileTerminatedServices.put(m.messageId, s);
						if(s.type.equalsIgnoreCase("moodle"))
							smsTemplatesMoodle.add(new SmsTemplate(m.messageId,m.message));
						else if (s.type.equalsIgnoreCase("direct")){
							smsTemplatesDirect.add(new SmsTemplate(m.messageId,m.message));
						}
					}		
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(mobileTerminatedServices);
	}

	public HashMap<String, MobileOriginatedService> getServices() {
		return mobileOriginatedServices;
	}

	public HashMap<String, MobileTerminatedService> getMobileTerminatedServices() {
		return mobileTerminatedServices;
	}


	


}
