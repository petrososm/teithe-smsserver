package com.smsserver.controllers.models.gunetapi;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smsserver.services.GetPropertyValues;

@XmlRootElement
public class SendSmsModel {

    private String serviceId;// id tis ipiresias pou periexei to to
    // prokathorismeno minima pros apostoli
    private String messageId;// to id toy minimatos
    private String[] replacements; // se periptwsi p thelei array
    private String recipient;// username i msisdn toy xristi
    private final String institution = "TEITHE";// ypoxrewtiko gia teithe
    private final String preSharedKey = GetPropertyValues.getProperties().getProperty("preSharedKey");
    private final String dlrUrl = GetPropertyValues.getProperties().getProperty("dlrUrl");

    private String smsForwardId;// efoson ginei se dinexeia enos forward
    // request-proeraitiko

    public SendSmsModel(String serviceId, String messageId, String[] replacements, String recipient,
            String smsForwardId) {
        super();
        this.serviceId = serviceId;
        this.messageId = messageId;
        this.replacements = replacements;
        this.recipient = recipient;
        this.smsForwardId = smsForwardId;
    }

    public SendSmsModel(String serverId, String messageId, String[] replacements, String recipient) {
        super();
        this.serviceId = serverId;
        this.messageId = messageId;
        this.replacements = replacements;
        this.recipient = recipient;

    }

    public SendSmsModel(String serverId, String messageId, String[] replacements) {
        super();
        this.serviceId = serverId;
        this.messageId = messageId;
        this.replacements = replacements;

    }

    public SendSmsModel(SendSmsModel sms) {
        super();
        this.serviceId = sms.serviceId;
        this.messageId = sms.messageId;
        this.replacements = sms.replacements;

    }

    public SendSmsModel() {
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String[] getReplacements() {
        return replacements;
    }

    public void setReplacements(String[] replacements) {
        this.replacements = replacements;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void setSmsForwardId(String smsForwardId) {
        this.smsForwardId = smsForwardId;
    }

    public String getInstitution() {
        return institution;
    }

    @JsonProperty("sms-forward-id")
    public String getSmsForwardId() {
        return smsForwardId;
    }

    @JsonProperty("pre-shared key")
    public String getPreSharedKey() {
        return preSharedKey;
    }

    @JsonProperty("dlr-url")
    public String getDlrUrl() {
        return dlrUrl;
    }

    @Override
    public String toString() {
        return "SendSmsModel [serviceId=" + serviceId + ", messageId=" + messageId + ", replacements="
                + Arrays.toString(replacements) + ", recipient=" + recipient + ", smsForwardId=" + smsForwardId + "]";
    }

}
