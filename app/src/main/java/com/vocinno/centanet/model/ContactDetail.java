package com.vocinno.centanet.model;

import java.util.ArrayList;
import java.util.List;

public class ContactDetail {
	private String delCode = "";
	private List<ContactItem> contactList = new ArrayList<ContactItem>();

	public String getDelCode() {
		return delCode;
	}

	public void setDelCode(String delCode) {
		this.delCode = delCode;
	}

	public List<ContactItem> getContactList() {
		return contactList;
	}

	public void setContactList(List<ContactItem> contactList) {
		this.contactList = contactList;
	}

}
