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
import com.liferay.salesforce.integration.SalesforceAuthorization;
import com.liferay.salesforce.integration.SalesforceConfiguration;
import com.liferay.salesforce.integration.SalesforceContact;
import com.liferay.salesforce.integration.SalesforceContactService;
import com.liferay.salesforce.integration.SalesforceToken;

import java.io.IOException;

import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

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
	immediate = true, service = SalesforceContactService.class
)
public class SalesforceContactServiceImpl implements SalesforceContactService {

	@Activate
	public void activate(Map<Object, Object> properties) {
		_salesforceConfiguration = ConfigurableUtil.createConfigurable(
			SalesforceConfiguration.class, properties);
	}

	@Override
	public SalesforceAuthorization getAuthorization(
			SalesforceToken salesforceToken,
			SalesforceContact salesforceContact)
		throws IOException {

		HttpRequest httpRequest;

		try {
			httpRequest = HttpRequest.newBuilder(
			).uri(
				new URI(
					"https", _salesforceConfiguration.restApiHostname(),
					"/services/data/v46.0/query",
					StringBundler.concat(
						"q=SELECT+",
						"Account_Role__c,Account__c,Proprietary_Permission__c+",
						"FROM+Account_Role__c+WHERE+Account__c='",
						SOQLUtil.sanitize(salesforceContact.getAccountId()),
						"'+AND+Email__c='",
						SOQLUtil.sanitize(salesforceContact.getEmail()), "'"),
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

			if (records.length() > 0) {
				JSONObject firstRecord = records.getJSONObject(0);

				List<String> permissionNames;

				String proprietaryPermission = firstRecord.getString(
					"Proprietary_Permission__c");

				if (proprietaryPermission == null) {
					permissionNames = Collections.emptyList();
				}
				else {
					permissionNames = Arrays.asList(
						proprietaryPermission.split(";"));
				}

				List<String> roleNames;

				String accountRoleC = firstRecord.getString("Account_Role__c");

				if (accountRoleC == null) {
					roleNames = Collections.emptyList();
				}
				else {
					roleNames = Arrays.asList(accountRoleC.split(";"));
				}

				return new SalesforceAuthorizationImpl(
					permissionNames, roleNames);
			}

			return new SalesforceAuthorizationImpl(
				Collections.emptyList(), Collections.emptyList());
		}
		catch (JSONException jsone) {
			throw new IOException(jsone);
		}
	}

	@Override
	public Optional<SalesforceContact> getUserByEmail(
			SalesforceToken salesforceToken, String email)
		throws IOException {

		HttpRequest httpRequest;

		try {
			httpRequest = HttpRequest.newBuilder(
			).uri(
				new URI(
					"https", _salesforceConfiguration.restApiHostname(),
					"/services/data/v46.0/query",
					StringBundler.concat(
						"q=SELECT+AccountId,Birthdate,Contact_Status__c,Email,",
						"Fax,FirstName,HomePhone,Id,LastName,Middle_Name__c,",
						"MobilePhone,Phone,Salutation,Title+FROM+Contact+",
						"WHERE+Email='", SOQLUtil.sanitize(email), "'"),
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

			if (records.length() > 0) {
				return Optional.of(
					SalesforceContactFactory.create(records.getJSONObject(0)));
			}

			return Optional.empty();
		}
		catch (JSONException jsone) {
			throw new IOException(jsone);
		}
	}

	@Override
	public void setLastLogin(
			SalesforceToken salesforceToken,
			SalesforceContact salesforceContact, Date lastLoginDate)
		throws IOException {

		HttpRequest httpRequest;

		JSONObject jsonObject = _jsonFactory.createJSONObject();

		DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

		jsonObject.put("Last_Login_Date__c", dateFormat.format(lastLoginDate));

		HttpRequest.BodyPublisher bodyPublisher =
			HttpRequest.BodyPublishers.ofString(jsonObject.toJSONString());

		try {
			httpRequest = HttpRequest.newBuilder(
			).uri(
				new URI(
					"https", _salesforceConfiguration.restApiHostname(),
					"/services/data/v46.0/sobjects/Contact/" +
						SOQLUtil.sanitize(salesforceContact.getId()),
					null)
			).setHeader(
				"Authorization", "Bearer " + salesforceToken.getAccessToken()
			).setHeader(
				"Content-Type", "application/json"
			).method(
				"PATCH", bodyPublisher
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
	}

	private static final Logger _log = LoggerFactory.getLogger(
		SalesforceContactServiceImpl.class);

	@Reference
	private JSONFactory _jsonFactory;

	private volatile SalesforceConfiguration _salesforceConfiguration;

}