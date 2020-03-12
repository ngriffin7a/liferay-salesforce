package com.liferay.salesforce.integration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Neil Griffin
 */
@ExtendedObjectClassDefinition(category = "salesforce-integration")
@Meta.OCD(
	id = "com.liferay.salesforce.integration.SalesforceConfiguration",
	localization = "content/Language"
)
public interface SalesforceConfiguration {

	@Meta.AD(deflt = "", name = "auth-client-id", required = false)
	public String authClientId();

	@Meta.AD(deflt = "", name = "auth-client-secret", required = false)
	public String authClientSecret();

	@Meta.AD(deflt = "", name = "auth-password", required = false)
	public String authPassword();

	@Meta.AD(deflt = "auth-username", required = false)
	public String authUsername();

	@Meta.AD(
		deflt = "login.salesforce.com", name = "login-api-hostname",
		required = false
	)
	public String loginApiHostname();

	@Meta.AD(deflt = "", name = "rest-api-hostname", required = false)
	public String restApiHostname();

}