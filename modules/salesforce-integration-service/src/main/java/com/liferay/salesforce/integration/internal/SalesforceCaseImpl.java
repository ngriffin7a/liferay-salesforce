package com.liferay.salesforce.integration.internal;

import com.liferay.salesforce.integration.SalesforceCase;

import java.util.Date;

public class SalesforceCaseImpl implements SalesforceCase {

	public SalesforceCaseImpl(
		String caseId, String accountId, String caseNumber, Date createdDate,
		boolean closed, String description, String reason) {

		_caseId = caseId;
		_accountId = accountId;
		_caseNumber = caseNumber;
		_createdDate = createdDate;
		_closed = closed;
		_description = description;
		_reason = reason;
	}

	@Override
	public String getAccountId() {
		return _accountId;
	}

	@Override
	public String getCaseId() {
		return _caseId;
	}

	@Override
	public String getCaseNumber() {
		return _caseNumber;
	}

	@Override
	public Date getCreatedDate() {
		return _createdDate;
	}

	@Override
	public String getDescription() {
		return _description;
	}

	@Override
	public String getReason() {
		return _reason;
	}

	@Override
	public boolean isClosed() {
		return _closed;
	}

	private String _accountId;
	private String _caseId;
	private String _caseNumber;
	private boolean _closed;
	private Date _createdDate;
	private String _description;
	private String _reason;

}