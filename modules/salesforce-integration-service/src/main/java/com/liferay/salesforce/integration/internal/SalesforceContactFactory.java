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

import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.salesforce.integration.SalesforceContact;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author Neil Griffin
 */
public class SalesforceContactFactory {

	public static SalesforceContact create(JSONObject jsonObject)
		throws JSONException {

		DateFormat dateFormat = new SimpleDateFormat(
			SalesforceDateUtil.DATE_PATTERN);

		try {
			return new SalesforceContactImpl(
				jsonObject.getString("AccountId"),
				"active".equalsIgnoreCase(
					jsonObject.getString("Contact_Status__c")),
				SalesforceDateUtil.getDate(
					dateFormat, jsonObject.getString("Birthdate")),
				jsonObject.getString("Email"), jsonObject.getString("Fax"),
				jsonObject.getString("FirstName"),
				jsonObject.getString("HomePhone"), jsonObject.getString("Id"),
				jsonObject.getString("LastName"),
				jsonObject.getString("MiddleName"),
				jsonObject.getString("MobilePhone"),
				jsonObject.getString("Phone"),
				jsonObject.getString("Salutation"),
				jsonObject.getString("Title"));
		}
		catch (ParseException pe) {
			throw new JSONException(pe);
		}
	}

}