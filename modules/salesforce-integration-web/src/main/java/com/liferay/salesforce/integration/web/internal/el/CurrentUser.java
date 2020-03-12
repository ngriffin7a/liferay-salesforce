package com.liferay.salesforce.integration.web.internal.el;

/**
 * @author Neil Griffin
 */
public class CurrentUser {

	public boolean maySelectAccount(String accountId) {
		return true;
	}

	public boolean mayViewAccount(String accountId) {
		return true;
	}

	public boolean mayViewCase(String caseId) {
		return true;
	}

}