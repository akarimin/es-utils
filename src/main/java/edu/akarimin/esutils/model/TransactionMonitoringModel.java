package edu.akarimin.esutils.model;

import java.math.BigDecimal;

/**
 * Created by akarimin on 6/19/16.
 */
public class TransactionMonitoringModel extends AbstractModel {

	private String channelNumber;
	private String sessionId;
	private String transactionType;
	private String transactionState;
	private Short connectionType = 2;
	private String cif;
	private BigDecimal amount;
	private String ip;
	private String errorDescription;
	private String osType;
	private Long transactionDate;
	private String passwordType;
	private String username;
	private String fullName;

	public String getChannelNumber() {
		return channelNumber;
	}

	public void setChannelNumber(String channelNumber) {
		this.channelNumber = channelNumber;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getTransactionState() {
		return transactionState;
	}

	public void setTransactionState(String transactionState) {
		this.transactionState = transactionState;
	}

	public Short getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(Short connectionType) {
		this.connectionType = connectionType;
	}

	public String getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public String getOsType() {
		return osType;
	}

	public void setOsType(String osType) {
		this.osType = osType;
	}

	public Long getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Long transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getPasswordType() {
		return passwordType;
	}

	public void setPasswordType(String passwordType) {
		this.passwordType = passwordType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

}
