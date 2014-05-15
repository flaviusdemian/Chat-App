package com.example.demoflavius.util;

import java.util.ArrayList;

import com.example.demoflavius.data.Section;

public class MenuHelper 
{
	public static ArrayList<Section> createMenu() {
		ArrayList<Section> sectionList = new ArrayList<Section>();

		Section oGeneralSection = new Section("Settings");
		oGeneralSection.addSectionItem(301, "Account settings", "linkedtranslators_settingsicon");
		oGeneralSection.addSectionItem(302, "Terms and conditions", "linkedtranslators_termsandconditionsicon");
		oGeneralSection.addSectionItem(303, "Report a problem", "linkedtranslators_reportaproblemicon");
		oGeneralSection.addSectionItem(304, "Logout", "linkedtranslators_logouticon");

		sectionList.add(oGeneralSection);
		return sectionList;
	}
}
