package com.liferay.salesforce.integration.web.internal.portlet;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.salesforce.integration.SalesforceCase;
import com.liferay.salesforce.integration.SalesforceCaseService;
import com.liferay.salesforce.integration.SalesforceToken;
import com.liferay.salesforce.integration.SalesforceTokenService;
import com.liferay.salesforce.integration.web.internal.constants.PortletKeys;
import com.liferay.salesforce.integration.web.internal.el.CurrentUser;

import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.RenderParameters;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Neil Griffin
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=Cases",
		"javax.portlet.init-param.template-path=/views/",
		"javax.portlet.init-param.view-template=/views/cases.jsp",
		"javax.portlet.name=" + PortletKeys.CASES,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user",
		"javax.portlet.supported-public-render-parameter=salesforceAccountId",
		"javax.portlet.version=3.0"
	},
	service = Portlet.class
)
public class CasesPortlet extends MVCPortlet {

	@Override
	public void doView(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		RenderParameters renderParameters = renderRequest.getRenderParameters();

		String salesforceAccountId = renderParameters.getValue(
			"salesforceAccountId");

		if (Validator.isNotNull(salesforceAccountId)) {
			PortletSession portletSession = renderRequest.getPortletSession();

			Map<String, List<SalesforceCase>> salesforceCaseMap =
				(Map<String, List<SalesforceCase>>)portletSession.getAttribute(
					"salesforceCaseMap");

			if (salesforceCaseMap == null) {
				salesforceCaseMap = new HashMap<>();
			}

			List<SalesforceCase> cases = salesforceCaseMap.get(
				salesforceAccountId);

			if (cases == null) {
				SalesforceToken salesforceToken =
					SalesforceTokenUtil.getSalesforceTokenFromPortletSession(
						_salesforceTokenService, portletSession);

				cases = _salesforceCaseService.getCases(
					salesforceToken, salesforceAccountId);

				salesforceCaseMap.put(salesforceAccountId, cases);
			}

			portletSession.setAttribute("salesforceCaseMap", salesforceCaseMap);

			renderRequest.setAttribute("cases", cases);
			renderRequest.setAttribute("currentUser", new CurrentUser());
		}

		super.doView(renderRequest, renderResponse);
	}

	@Reference
	private SalesforceCaseService _salesforceCaseService;

	@Reference
	private SalesforceTokenService _salesforceTokenService;

}