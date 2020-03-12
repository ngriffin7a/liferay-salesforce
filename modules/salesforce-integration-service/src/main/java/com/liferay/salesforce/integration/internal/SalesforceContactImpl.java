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

import com.liferay.salesforce.integration.SalesforceContact;

import java.util.Date;

/**
 * @author Neil Griffin
 */
public class SalesforceContactImpl implements SalesforceContact {

	public SalesforceContactImpl(
		String accountId, boolean active, Date birthDate, String email,
		String fax, String firstName, String homePhone, String id,
		String lastName, String middleName, String mobilePhone, String phone,
		String salutation, String title) {

		_accountId = accountId;
		_active = active;
		_birthDate = birthDate;
		_email = email;
		_fax = fax;
		_firstName = firstName;
		_homePhone = homePhone;
		_id = id;
		_lastName = lastName;
		_middleName = middleName;
		_mobilePhone = mobilePhone;
		_phone = phone;
		_salutation = salutation;
		_title = title;
	}

	@Override
	public String getAccountId() {
		return _accountId;
	}

	@Override
	public Date getBirthDate() {
		return _birthDate;
	}

	@Override
	public String getEmail() {
		return _email;
	}

	@Override
	public String getFax() {
		return _fax;
	}

	@Override
	public String getFirstName() {
		return _firstName;
	}

	@Override
	public String getHomePhone() {
		return _homePhone;
	}

	@Override
	public String getId() {
		return _id;
	}

	@Override
	public String getLastName() {
		return _lastName;
	}

	@Override
	public String getMiddleName() {
		return _middleName;
	}

	@Override
	public String getMobilePhone() {
		return _mobilePhone;
	}

	@Override
	public String getPhone() {
		return _phone;
	}

	@Override
	public String getSalutation() {
		return _salutation;
	}

	@Override
	public String getTitle() {
		return _title;
	}

	@Override
	public boolean isActive() {
		return _active;
	}

	private String _accountId;
	private boolean _active;
	private Date _birthDate;
	private String _email;
	private String _fax;
	private String _firstName;
	private String _homePhone;
	private String _id;
	private String _lastName;
	private String _middleName;
	private String _mobilePhone;
	private String _phone;
	private String _salutation;
	private String _title;

}