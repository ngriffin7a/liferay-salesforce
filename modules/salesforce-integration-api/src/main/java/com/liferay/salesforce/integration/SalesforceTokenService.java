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

/**
 * @author Neil Griffin
 */
public interface SalesforceTokenService {

	/**
	 * Retrieves an OAuth2 {@link SalesforceToken} by invoking the Salesforce
	 * RESTful authentication API with the specified credentials.
	 *
	 * @return The {@link SalesforceToken} associated with a successful authentication.
	 * @throws IOException When there is a failure connecting to Salesforce or
	 * when there is an authenticate failure with the specified credentials.
	 */
	public SalesforceToken getToken() throws IOException;

}