package com.liferay.salesforce.integration;

import java.util.Date;

/**
 * @author Neil Griffin
 */
public interface SalesforceCase {

	public String getAccountId();

	public String getCaseId();

	public String getCaseNumber();

	public Date getCreatedDate();

	public String getDescription();

	public String getReason();

	public boolean isClosed();

}