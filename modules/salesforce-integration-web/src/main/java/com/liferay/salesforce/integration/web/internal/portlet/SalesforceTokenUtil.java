package com.liferay.salesforce.integration.web.internal.portlet;

import com.liferay.salesforce.integration.SalesforceToken;
import com.liferay.salesforce.integration.SalesforceTokenService;

import java.io.IOException;

import javax.portlet.PortletSession;

public class SalesforceTokenUtil {

	public static SalesforceToken getSalesforceTokenFromPortletSession(
			SalesforceTokenService salesforceTokenService,
			PortletSession portletSession)
		throws IOException {

		SalesforceToken salesforceToken =
			(SalesforceToken)portletSession.getAttribute("salesforceToken");

		if (salesforceToken == null) {
			salesforceToken = salesforceTokenService.getToken();
		}

		portletSession.setAttribute("salesforceToken", salesforceToken);

		return salesforceToken;
	}

}