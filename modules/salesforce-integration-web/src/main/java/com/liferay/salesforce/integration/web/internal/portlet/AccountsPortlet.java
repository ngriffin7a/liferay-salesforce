package com.liferay.salesforce.integration.web.internal.portlet;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.salesforce.integration.SalesforceAccount;
import com.liferay.salesforce.integration.SalesforceAccountService;
import com.liferay.salesforce.integration.SalesforceToken;
import com.liferay.salesforce.integration.SalesforceTokenService;
import com.liferay.salesforce.integration.web.internal.constants.PortletKeys;
import com.liferay.salesforce.integration.web.internal.el.CurrentUser;

import java.io.IOException;

import java.util.List;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
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
		"javax.portlet.display-name=Accounts",
		"javax.portlet.init-param.template-path=/views/",
		"javax.portlet.init-param.view-template=/views/accounts.jsp",
		"javax.portlet.name=" + PortletKeys.ACCOUNTS,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user",
		"javax.portlet.supported-public-render-parameter=salesforceAccountId",
		"javax.portlet.version=3.0"
	},
	service = Portlet.class
)
public class AccountsPortlet extends MVCPortlet {

	@Override
	public void doView(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		PortletSession portletSession = renderRequest.getPortletSession();

		List<SalesforceAccount> accounts =
			(List<SalesforceAccount>)portletSession.getAttribute("accounts");

		if (accounts == null) {
			SalesforceToken salesforceToken =
				SalesforceTokenUtil.getSalesforceTokenFromPortletSession(
					_salesforceTokenService, portletSession);

			accounts = _salesforceAccountService.getAccounts(salesforceToken);

			portletSession.setAttribute("accounts", accounts);
		}

		renderRequest.setAttribute("accounts", accounts);
		renderRequest.setAttribute("currentUser", new CurrentUser());

		super.doView(renderRequest, renderResponse);
	}

	@Reference
	private SalesforceAccountService _salesforceAccountService;

	@Reference
	private SalesforceTokenService _salesforceTokenService;

}