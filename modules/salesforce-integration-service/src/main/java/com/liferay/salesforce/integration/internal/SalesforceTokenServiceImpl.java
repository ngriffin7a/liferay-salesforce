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
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.salesforce.integration.SalesforceConfiguration;
import com.liferay.salesforce.integration.SalesforceToken;
import com.liferay.salesforce.integration.SalesforceTokenService;

import java.io.IOException;

import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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
	immediate = true, service = SalesforceTokenService.class
)
public class SalesforceTokenServiceImpl implements SalesforceTokenService {

	@Activate
	public void activate(Map<Object, Object> properties) {
		_salesforceConfiguration = ConfigurableUtil.createConfigurable(
			SalesforceConfiguration.class, properties);
	}

	@Override
	public SalesforceToken getToken() throws IOException {
		String requestBody = ParamBuilder.newBuilder(
		).add(
			"grant_type", "password"
		).add(
			"client_id",
			URLEncoder.encode(_salesforceConfiguration.authClientId(), "UTF-8")
		).add(
			"client_secret",
			URLEncoder.encode(
				_salesforceConfiguration.authClientSecret(), "UTF-8")
		).add(
			"username",
			URLEncoder.encode(_salesforceConfiguration.authUsername(), "UTF-8")
		).add(
			"password",
			URLEncoder.encode(_salesforceConfiguration.authPassword(), "UTF-8")
		).build(
			ParamBuilder.Type.FORM_URLENCODED
		);

		_log.trace("requestBody={}", requestBody);

		HttpRequest.BodyPublisher bodyPublisher =
			HttpRequest.BodyPublishers.ofString(requestBody);

		HttpRequest httpRequest;

		try {
			httpRequest = HttpRequest.newBuilder(
			).uri(
				new URI(
					"https", _salesforceConfiguration.restApiHostname(),
					"/services/oauth2/token", null)
			).setHeader(
				"Content-Type", "application/x-www-form-urlencoded"
			).POST(
				bodyPublisher
			).build();

			_log.trace("httpRequest headers={}", httpRequest.headers());

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

		String responseBody = httpResponse.body();

		_log.trace("responseBody={}", responseBody);

		String accessToken = null;

		JSONObject jsonObject = null;

		try {
			jsonObject = _jsonFactory.createJSONObject(responseBody);

			if (jsonObject.has("error")) {
				throw new IOException(jsonObject.toJSONString());
			}

			if (jsonObject.has("access_token")) {
				accessToken = jsonObject.getString("access_token");

				if (Validator.isNull(accessToken)) {
					throw new IOException("Empty access_token");
				}
			}
		}
		catch (JSONException jsone) {
			jsone.printStackTrace();
		}

		SalesforceToken salesforceToken = new SalesforceTokenImpl(
			accessToken, jsonObject.getString("id"),
			jsonObject.getString("instance_url"),
			jsonObject.getLong("issued_at"), jsonObject.getString("signature"));

		_log.debug("salesforceToken={}", salesforceToken);

		return salesforceToken;
	}

	private static final Logger _log = LoggerFactory.getLogger(
		SalesforceTokenServiceImpl.class);

	@Reference
	private JSONFactory _jsonFactory;

	private volatile SalesforceConfiguration _salesforceConfiguration;

}