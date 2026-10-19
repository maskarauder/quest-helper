package com.questhelper.i18n;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.Reader;
import java.io.StringReader;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QuestTranslationsTest
{
	private final QuestTranslations translations = new QuestTranslations(new Gson());

	@Test
	void englishIsTheDefaultAndPreservesOrdinaryText()
	{
		assertEquals("General requirements:", translations.text("General requirements:"));
		assertEquals("Don't change {0}.", translations.text("Don't change {0}."));
		assertNull(translations.text(null));
	}

	@Test
	void loadsBundledSpanishAndFallsBackThroughCommonToEnglish()
	{
		translations.setLanguage(QuestHelperLanguage.SPANISH);
		assertEquals("Cambiar la configuración", translations.text("Change your settings"));
		String common = translations.text("General requirements:");
		assertNotEquals("General requirements:", common);
		assertEquals(common, translations.text("UNKNOWN_HELPER", "General requirements:"));
		assertEquals("An untranslated instruction.", translations.text("UNKNOWN_HELPER", "An untranslated instruction."));
		assertEquals("Don't change {0}.", translations.text("Don't change {0}."));
	}

	@Test
	void helperInstructionsRequireTheirContext()
	{
		translations.setLanguage(QuestHelperLanguage.SPANISH);
		String instruction = "Use a spade to enter the Brine Rat Cavern.";
		assertNotEquals(instruction, translations.text("FREMENNIK_MEDIUM", instruction));
		assertEquals(instruction, translations.text("OTHER_HELPER", instruction));
	}

	@Test
	void switchingBackToEnglishDoesNotRetainTranslations()
	{
		translations.setLanguage(QuestHelperLanguage.SPANISH);
		String spanish = translations.text("General requirements:");
		assertNotEquals("General requirements:", spanish);
		translations.setLanguage(QuestHelperLanguage.ENGLISH);
		assertEquals("General requirements:", translations.text("General requirements:"));
		translations.setLanguage(QuestHelperLanguage.SPANISH);
		assertEquals(spanish, translations.text("General requirements:"));
	}

	@Test
	void explicitTemplatesSupportReorderedArguments()
	{
		assertEquals("Construction: 37", translations.format("common", "{1}: {0}", 37, "Construction"));
	}

	@Test
	void contextOverridesCommonAndTranslatedTemplatesCanReorderArguments()
	{
		QuestTranslations contextual = withCatalog("{\"common\":{\"Open\":\"Abierto\"},"
			+ "\"TEST_HELPER\":{\"Open\":\"Abrir\",\"{0} {1}\":\"{1}: {0}\"}}");
		assertEquals("Abrir", contextual.text("TEST_HELPER", "Open"));
		assertEquals("Abierto", contextual.text("OTHER_HELPER", "Open"));
		assertEquals("Construction: 37", contextual.format("TEST_HELPER", "{0} {1}", 37, "Construction"));
	}

	@Test
	void malformedCatalogFallsBackToEnglish()
	{
		QuestTranslations malformed = withCatalog("{\"common\":null}");
		assertEquals("General requirements:", malformed.text("General requirements:"));
	}

	@ParameterizedTest
	@ValueSource(strings = {"null", "[]", "{", "{\"common\":[]}", "{\"common\":{\"Hello\":null}}", "{\"common\":{\"Hello\":12}}"})
	void rejectsMalformedCatalogs(String json)
	{
		assertThrows(JsonParseException.class, () -> translations.readCatalog(new StringReader(json)));
	}

	@Test
	void catalogIsImmutableAndEmptyTranslationsAreIgnored()
	{
		Map<String, Map<String, String>> catalog = translations.readCatalog(new StringReader(
			"{\"common\":{\"Hello\":\"Hola\",\"Keep English\":\"\"}}"));
		assertEquals(Map.of("Hello", "Hola"), catalog.get("common"));
		assertThrows(UnsupportedOperationException.class, () -> catalog.put("other", Map.of()));
		assertThrows(UnsupportedOperationException.class, () -> catalog.get("common").put("Bye", "Adiós"));
	}

	private QuestTranslations withCatalog(String json)
	{
		QuestTranslations fromFixture = new QuestTranslations(new Gson())
		{
			@Override
			Map<String, Map<String, String>> readCatalog(Reader reader)
			{
				return super.readCatalog(new StringReader(json));
			}
		};
		fromFixture.setLanguage(QuestHelperLanguage.SPANISH);
		return fromFixture;
	}
}
