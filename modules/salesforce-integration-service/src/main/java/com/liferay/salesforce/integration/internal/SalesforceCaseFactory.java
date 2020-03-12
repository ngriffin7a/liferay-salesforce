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
import com.liferay.salesforce.integration.SalesforceCase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author Neil Griffin
 */
public class SalesforceCaseFactory {

	public static SalesforceCase create(JSONObject jsonObject)
		throws JSONException {

		DateFormat dateFormat = new SimpleDateFormat(
			SalesforceDateUtil.DATE_PATTERN);

		try {
			return new SalesforceCaseImpl(
				jsonObject.getString("Id"), jsonObject.getString("AccountId"),
				jsonObject.getString("CaseNumber"),
				SalesforceDateUtil.getDate(
					dateFormat, jsonObject.getString("CreatedDate")),
				jsonObject.getBoolean("IsClosed"),
				jsonObject.getString("Description"),
				jsonObject.getString("Reason"));
		}
		catch (ParseException pe) {
			throw new JSONException(pe);
		}
	}

}