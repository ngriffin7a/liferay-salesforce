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

import java.util.Date;

/**
 * @author Neil Griffin
 */
public interface SalesforceContact {

	public String getAccountId();

	public Date getBirthDate();

	public String getEmail();

	public String getFax();

	public String getFirstName();

	public String getHomePhone();

	public String getId();

	public String getLastName();

	public String getMiddleName();

	public String getMobilePhone();

	public String getPhone();

	public String getSalutation();

	public String getTitle();

	public boolean isActive();

}