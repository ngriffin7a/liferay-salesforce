package com.liferay.salesforce.integration.web.internal.view;

import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenu;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.portlet.view.state.ClayToolbarViewState;
import com.liferay.portlet.view.state.ClayToolbarViewStateWrapper;

import java.util.List;
import java.util.Objects;

import javax.portlet.MutableRenderParameters;
import javax.portlet.RenderURL;

/**
 * @author Neil Griffin
 */
public class AccountToolBarViewState extends ClayToolbarViewStateWrapper {

	public AccountToolBarViewState(
		ClayToolbarViewState clayToolbarViewState, String orderByMessage,
		String sizeMessage, String titleMessage) {

		super(clayToolbarViewState);

		_addEntryURL = clayToolbarViewState.getAddEntryURL();

		MutableRenderParameters renderParameters =
			_addEntryURL.getRenderParameters();

		renderParameters.setValue("javax.portlet.render", "assignment");
		renderParameters.setValue("viewId", "add");

		_orderByMessage = orderByMessage;
		_sizeMessage = sizeMessage;
		_titleMessage = titleMessage;
	}

	@Override
	public CreationMenu getCreationMenu() {
		if (_creationMenu == null) {
			_creationMenu = new CreationMenu();

			_creationMenu.addDropdownItem(
				dropdownItem -> {
					dropdownItem.setHref(_addEntryURL);

					ClayToolbarViewState clayToolbarViewState = getWrapped();

					dropdownItem.setLabel(
						clayToolbarViewState.getAddEntryMessage());
				});
		}

		return _creationMenu;
	}

	@Override
	public List<DropdownItem> getFilterDropdownItems() {
		if (_filterDropdownItems == null) {
			_filterDropdownItems = new DropdownItemList();

			_filterDropdownItems.addGroup(
				dropdownGroupItem -> {
					dropdownGroupItem.setDropdownItems(
						_getOrderByDropdownItems());
					dropdownGroupItem.setLabel(_orderByMessage);
				});
		}

		return _filterDropdownItems;
	}

	private List<DropdownItem> _getOrderByDropdownItems() {
		DropdownItemList orderByDropDownItems = new DropdownItemList();

		orderByDropDownItems.add(
			dropdownItem -> {
				dropdownItem.setActive(
					Objects.equals(getSortingOrder(), "title"));
				dropdownItem.setHref(
					getSortingURLCurrent(), "orderByCol", "title");
				dropdownItem.setLabel(_titleMessage);
			});

		orderByDropDownItems.add(
			dropdownItem -> {
				dropdownItem.setActive(
					Objects.equals(getSortingOrder(), "size"));
				dropdownItem.setHref(
					getSortingURLCurrent(), "orderByCol", "size");
				dropdownItem.setLabel(_sizeMessage);
			});

		return orderByDropDownItems;
	}

	private RenderURL _addEntryURL;
	private CreationMenu _creationMenu;
	private DropdownItemList _filterDropdownItems;
	private String _orderByMessage;
	private String _sizeMessage;
	private String _titleMessage;

}