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

package com.liferay.salesforce.integration;

import java.io.IOException;

import java.util.Date;
import java.util.Optional;

/**
 * @author Neil Griffin
 */
public interface SalesforceContactService {

	/**
	 * Retrieves the {@link SalesforceAuthorization} associated with the
	 * specified Salesforce contact.
	 *
	 * @param salesforceToken The Salesforce OAuth2 token retrieved from the
	 * {@link SalesforceTokenService}.
	 * @param salesforceContact The Salesforce contact retreived from the
	 * {@link #getUserByEmail(SalesforceToken, String)} method.
	 *
	 * @return The non-null {@link SalesforceAuthorization} which will contain
	 * non-null lists of permission names and role names.
	 *
	 * @throws IOException When there is a failure connecting to Salesforce.
	 */
	public SalesforceAuthorization getAuthorization(
			SalesforceToken salesforceToken,
			SalesforceContact salesforceContact)
		throws IOException;

	/**
	 * Retrieves the {@link SalesforceContact} associated with the specified
	 * email address.
	 *
	 * @param salesforceToken The Salesforce OAuth2 token retrieved from the
	 * {@link SalesforceTokenService}.
	 * @param email The email address of the Salesforce contact.
	 *
	 * @return If the {@link SalesforceContact} was found as a valid contact,
	 * then returns an {@link Optional} such that {@link Optional#isPresent()}
	 * returns <code>true</code> and {@link Optional#get()}} returns the contact
	 * info. Otherwise, {@link Optional#isPresent()} returns <code>false</code>.
	 *
	 * @throws IOException When there is a failure connecting to Salesforce.
	 */
	public Optional<SalesforceContact> getUserByEmail(
			SalesforceToken salesforceToken, String email)
		throws IOException;

	/**
	 * Sets the last login date of a contact in Salesforce.
	 *
	 * @param lastLoginDate The last login date.
	 *
	 * @throws IOException When there is a failure connecting to Salesforce.
	 */
	public void setLastLogin(
			SalesforceToken salesforceToken,
			SalesforceContact salesforceContact, Date lastLoginDate)
		throws IOException;

}