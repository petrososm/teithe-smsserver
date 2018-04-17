package com.smsserver.services.mobileoriginated;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.sql.DataSource;

import com.smsserver.controllers.models.gunetapi.SendSmsModel;
import com.smsserver.controllers.models.gunetapi.SmsForwardModel;
import com.smsserver.dao.Discovery;
import com.smsserver.services.models.mobileoriginated.Message;
import com.smsserver.services.models.mobileoriginated.MobileOriginatedService;

import javassist.NotFoundException;

@Stateless
public class GradeService {

	@EJB
	Discovery discovery;
	@Resource(lookup = "jdbc/pithia")
	DataSource pithia;

	public SendSmsModel prepareReply(SmsForwardModel smsRequest) throws SQLException, NotFoundException {
		String[] replacements = null;
		String[] userParameters = smsRequest.getBody().split("\\s+");
		;
		String authenticator = discovery.getUsername(smsRequest.getMsisdn());
		SendSmsModel sms = new SendSmsModel();
		sms.setServiceId("gradeServiceTEITHE");

		try {
			if (userParameters[0].equalsIgnoreCase("KSEXWR")) {
				replacements = getVathmosKsexwr(authenticator, userParameters);
				sms.setMessageId("gradeServiceTEITHEMsg2");
			} else {
				replacements = getVathmos(authenticator, userParameters);
				sms.setMessageId("gradeServiceTEITHEMsg1");
			}
		} catch (Exception e) {
			sms.setMessageId("gradeServiceTEITHEMsg3");
		}
		sms.setReplacements(replacements);
		sms.setRecipient(smsRequest.getMsisdn());
		sms.setSmsForwardId(sms.getSmsForwardId());
		System.out.println(sms);
		return sms;
	}

	private String[] getVathmosKsexwr(String username, String[] parameters) throws SQLException, NotFoundException {
		try (Connection conn = pithia.getConnection();
				PreparedStatement getLessonId = conn.prepareStatement(
						"SELECT top 1 coursecode,ctitle " + "FROM [eUniCentral].[dbo].[v_SMS_gradeService]"
								+ " where username=? " + "and ctitle like ? order by coursecode asc");
				PreparedStatement getGrade = conn.prepareStatement(
						"SELECT ctitle,CONVERT(DECIMAL(10,1),grade) FROM [eUniCentral].[dbo].[v_SMS_gradeService] "
						+ "where username=? and coursecode=?");){

			String titleLike = "%";
			for (int k=1;k<parameters.length;k++) {
				titleLike += parameters[k] + "%";
			}
			System.out.println(titleLike);
			getLessonId.setString(1, username);
			getLessonId.setString(2, titleLike);

			ResultSet rs = getLessonId.executeQuery();
			String courseCode;
			String[] replacements = new String[3];
			if (rs.next()) {
				replacements[0] = rs.getString(2);
				courseCode=rs.getString(1);
			}
			else{
				throw new SQLException();
			}
			
			getGrade.setString(1, username);
			for(int i=1;i<=2;i++){
				getGrade.setString(2, courseCode+i);
				rs=getGrade.executeQuery();
				if(rs.next())
					replacements[i]=rs.getString(2);
				else
					replacements[i]="Δεν υπάρχει";
			}
			
			return replacements;
			

		}
	}

	protected String[] getVathmos(String username, String[] parameters) throws SQLException, NotFoundException {

		try (Connection conn = pithia.getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						"SELECT ctitle,CONVERT(DECIMAL(10,1),grade) " + "FROM [eUniCentral].[dbo].[v_SMS_gradeService]"
								+ " where username=? " + "and ctitle like ?");) {

			String titleLike = "%";
			for (String s : parameters) {
				titleLike += s + "%";
			}
			System.out.println(titleLike);
			stmt.setString(1, username);
			stmt.setString(2, titleLike);

			ResultSet rs = stmt.executeQuery();
			String[] replacements = new String[2];
			if (rs.next()) {
				replacements[0] = rs.getString(1);
				replacements[1] = rs.getString(2);
			} else {
				throw new SQLException();

			}
			return replacements;

		}
	}

}
