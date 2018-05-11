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
import com.smsserver.services.models.mobileoriginated.Keywords;
import com.smsserver.services.models.mobileoriginated.MobileOriginatedService;
import com.smsserver.services.models.mobileterminated.Message;
import com.smsserver.services.models.mobileterminated.MobileTerminatedService;

@Startup
@Singleton
public class Services {

	private HashMap<String, MobileTerminatedService> mobileTerminatedServices = new HashMap<String, MobileTerminatedService>();
	private HashMap<String, MobileOriginatedService> mobileOriginatedServices = new HashMap<String, MobileOriginatedService>();
	private ArrayList<ServiceDescription> descriptions = new ArrayList<ServiceDescription>();
	private ArrayList<SmsTemplate> smsTemplatesMoodle = new ArrayList<SmsTemplate>();
	private ArrayList<SmsTemplate> smsTemplatesDirect = new ArrayList<SmsTemplate>();

	@PostConstruct
	private void init() {
		this.loadMobileOriginated();
		this.loadMobileTerminated();
	}

	public HashMap<String, MobileOriginatedService> getMobileOriginatedServices() {
		return mobileOriginatedServices;
	}

	public ArrayList<ServiceDescription> getDescriptions() {
		return descriptions;
	}

	public ArrayList<SmsTemplate> getSmsTemplatesMoodle() {
		return smsTemplatesMoodle;
	}

	public ArrayList<SmsTemplate> getSmsTemplatesDirect() {
		return smsTemplatesDirect;
	}

	public void loadMobileOriginated() {
		try {
			JAXBContext jc = JAXBContext.newInstance(com.smsserver.services.models.mobileoriginated.Root.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			ClassLoader classLoader = Services.class.getClassLoader();
			File questionXML = new File(classLoader.getResource("mobileoriginated.xml").getFile());

			com.smsserver.services.models.mobileoriginated.Root moRoot = (com.smsserver.services.models.mobileoriginated.Root) unmarshaller
					.unmarshal(questionXML);

			for (MobileOriginatedService s : moRoot.getMobileOriginatedService()) {
				for (String keyword : s.getKeywords().keywordList){
					mobileOriginatedServices.put(StringConverter.EnglishToGSM7(keyword), s);
                                }
                                for(com.smsserver.services.models.mobileoriginated.Message m:s.getMessages()){
                                        m.setKeyword(StringConverter.EnglishToGSM7(m.getKeyword()));
                                }
				for (com.smsserver.services.models.mobileoriginated.Message m : s.getMessages()) {
					descriptions.add(new ServiceDescription(
							"TEITHE "+s.getKeywords().keywordList.get(0) + " " + m.getFullKeyword(), m.getDescription()));

					m.computeQueryParams();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(mobileOriginatedServices);

	}

	public void loadMobileTerminated() {
		try {
			JAXBContext jc = JAXBContext.newInstance(com.smsserver.services.models.mobileterminated.Root.class);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			ClassLoader classLoader = Services.class.getClassLoader();
			File mtxml = new File(classLoader.getResource("mobileterminated.xml").getFile());
			com.smsserver.services.models.mobileterminated.Root mtRoot = (com.smsserver.services.models.mobileterminated.Root) unmarshaller
					.unmarshal(mtxml);

			for (MobileTerminatedService s : mtRoot.getMobileTerminatedService()) {
				for (Message m : s.getMessages()) {
					m.setReplacements(m.getMessageId().length() - m.getMessageId().replace("?", "").length());
					mobileTerminatedServices.put(m.getMessageId(), s);
					if (s.getType().equalsIgnoreCase("moodle"))
						smsTemplatesMoodle.add(new SmsTemplate(m.getMessageId(), m.getMessage()));
					else if (s.getType().equalsIgnoreCase("direct")) {
						smsTemplatesDirect.add(new SmsTemplate(m.getMessageId(), m.getMessage()));
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(mobileTerminatedServices);
	}



	public HashMap<String, MobileTerminatedService> getMobileTerminatedServices() {
		return mobileTerminatedServices;
	}

}
class StringConverter {

    static final HashMap<Character, Character> characters;

    static {
        characters = new HashMap<>();
        characters.put('A', 'Á');
        characters.put('B', 'Â');
        characters.put('E', 'Å');
        characters.put('Z', 'Æ');
        characters.put('I', 'É');
        characters.put('H', 'Ç');
        characters.put('K', 'Ê');
        characters.put('M', 'Ì');
        characters.put('N', 'Í');
        characters.put('O', 'Ï');
        characters.put('P', 'Ñ');
        characters.put('T', 'Ô');
        characters.put('X', '×');
        characters.put('Y', 'Õ');

    }

    public static String EnglishToGSM7(String s) {
        String newString = "";
        for (char c : s.toCharArray()) {
            if (characters.containsKey(c)) {
                newString += characters.get(c);
            } else {
                newString += c;
            }
        }
        System.out.println(newString);
        return newString;

    }

}

