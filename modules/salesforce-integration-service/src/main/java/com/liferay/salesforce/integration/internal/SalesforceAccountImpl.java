/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

package com.liferay.salesforce.integration.internal;

import com.liferay.salesforce.integration.SalesforceAccount;

import java.util.Date;

/**
 * @author Neil Griffin
 */
public class SalesforceAccountImpl implements SalesforceAccount {

	public SalesforceAccountImpl(
		String accountId, String accountName, String billingCity,
		String billingState, Date createdDate, String industry) {

		_accountId = accountId;
		_accountName = accountName;
		_billingCity = billingCity;
		_billingState = billingState;
		_createdDate = createdDate;
		_industry = industry;
	}

	@Override
	public String getAccountId() {
		return _accountId;
	}

	@Override
	public String getBillingCity() {
		return _billingCity;
	}

	@Override
	public String getBillingState() {
		return _billingState;
	}

	@Override
	public Date getCreatedDate() {
		return _createdDate;
	}

	@Override
	public String getIndustry() {
		return _industry;
	}

	@Override
	public String getName() {
		return _accountName;
	}

	private String _accountId;
	private String _accountName;
	private String _billingCity;
	private String _billingState;
	private Date _createdDate;
	private String _industry;

}