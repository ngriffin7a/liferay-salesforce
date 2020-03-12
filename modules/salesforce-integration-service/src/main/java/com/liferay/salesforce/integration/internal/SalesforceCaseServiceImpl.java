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

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.salesforce.integration.SalesforceCase;
import com.liferay.salesforce.integration.SalesforceCaseService;
import com.liferay.salesforce.integration.SalesforceConfiguration;
import com.liferay.salesforce.integration.SalesforceToken;

import java.io.IOException;

import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Neil Griffin
 */
@Component(
	configurationPid = "com.liferay.salesforce.integration.SalesforceConfiguration",
	immediate = true, service = SalesforceCaseService.class
)
public class SalesforceCaseServiceImpl implements SalesforceCaseService {

	@Activate
	public void activate(Map<Object, Object> properties) {
		_salesforceConfiguration = ConfigurableUtil.createConfigurable(
			SalesforceConfiguration.class, properties);
	}

	@Override
	public List<SalesforceCase> getCases(
			SalesforceToken salesforceToken, String accountId)
		throws IOException {

		List<SalesforceCase> salesforceCases = new ArrayList<>();

		HttpRequest httpRequest;

		try {
			httpRequest = HttpRequest.newBuilder(
			).uri(
				new URI(
					"https", _salesforceConfiguration.restApiHostname(),
					"/services/data/v46.0/query",
					StringBundler.concat(
						"q=SELECT+Id,AccountId,CaseNumber,CreatedDate,IsClosed,Description,Reason+FROM+Case+WHERE+AccountId='" +
							SOQLUtil.sanitize(accountId) + "'"),
					null)
			).setHeader(
				"Authorization", "Bearer " + salesforceToken.getAccessToken()
			).setHeader(
				"Content-Type", "application/json"
			).GET(
			).build();

			_log.trace("headers={}", httpRequest.headers());
			_log.trace("httpRequest={}", httpRequest);
		}
		catch (URISyntaxException urise) {
			throw new IOException(urise);
		}

		HttpResponse<String> httpResponse;

		try {
			HttpClient httpClient = HttpClient.newBuilder(
			).proxy(
				ProxySelector.getDefault()
			).version(
				HttpClient.Version.HTTP_1_1
			).build();

			httpResponse = httpClient.send(
				httpRequest, HttpResponse.BodyHandlers.ofString());
		}
		catch (InterruptedException ie) {
			throw new IOException(ie);
		}

		String responseBody = SalesforceResponseUtil.trim(httpResponse.body());

		_log.trace("responseBody=" + responseBody);

		try {
			JSONObject jsonObject = _jsonFactory.createJSONObject(responseBody);

			JSONArray records = jsonObject.getJSONArray("records");

			if (records == null) {
				throw new JSONException(
					"Array named 'records' not found in response JSON");
			}

			int length = records.length();

			for (int i = 0; i < length; i++) {
				salesforceCases.add(
					SalesforceCaseFactory.create(records.getJSONObject(i)));
			}
		}
		catch (JSONException jsone) {
			throw new IOException(jsone);
		}

		return salesforceCases;
	}

	private static final Logger _log = LoggerFactory.getLogger(
		SalesforceCaseServiceImpl.class);

	@Reference
	private JSONFactory _jsonFactory;

	private volatile SalesforceConfiguration _salesforceConfiguration;

}