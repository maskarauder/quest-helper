package com.questhelper.i18n;

import java.util.Locale;

public enum QuestHelperLanguage
{
	ENGLISH("English", Locale.ENGLISH),
	SPANISH("Español", Locale.forLanguageTag("es"));

	private final String displayName;
	private final Locale locale;

	QuestHelperLanguage(String displayName, Locale locale)
	{
		this.displayName = displayName;
		this.locale = locale;
	}

	public Locale getLocale()
	{
		return locale;
	}

	@Override
	public String toString()
	{
		return displayName;
	}
}
