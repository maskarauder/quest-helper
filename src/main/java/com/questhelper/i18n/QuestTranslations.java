package com.questhelper.i18n;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/** Translates display text only. Game matching and saved identifiers must remain in English. */
@Singleton
@Slf4j
public class QuestTranslations
{
	private final Gson gson;
	private volatile Catalog catalog = new Catalog(QuestHelperLanguage.ENGLISH, Map.of());

	@Inject
	public QuestTranslations(Gson gson)
	{
		this.gson = gson;
	}

	public synchronized void setLanguage(QuestHelperLanguage language)
	{
		if (catalog.language == language)
		{
			return;
		}

		Map<String, Map<String, String>> entries = Map.of();
		if (language != QuestHelperLanguage.ENGLISH)
		{
			String resource = "/i18n/" + language.getLocale().toLanguageTag() + ".json";
			try (InputStream stream = QuestTranslations.class.getResourceAsStream(resource))
			{
				if (stream == null)
				{
					log.warn("Missing translation catalog {}; using English", resource);
				}
				else
				{
					try (Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8))
					{
						entries = readCatalog(reader);
					}
				}
			}
			catch (IOException | JsonParseException exception)
			{
				log.warn("Could not load translation catalog {}; using English", resource, exception);
			}
		}
		catalog = new Catalog(language, entries);
	}

	public String text(String english)
	{
		return text("common", english);
	}

	public String text(String context, String english)
	{
		return catalog.text(context, english);
	}

	/** Only explicit templates use MessageFormat; ordinary text keeps apostrophes unchanged. */
	public String format(String context, String english, Object... arguments)
	{
		Catalog current = catalog;
		String translated = current.text(context, english);
		try
		{
			return new MessageFormat(translated, current.language.getLocale()).format(arguments);
		}
		catch (IllegalArgumentException exception)
		{
			return new MessageFormat(english, QuestHelperLanguage.ENGLISH.getLocale()).format(arguments);
		}
	}

	Map<String, Map<String, String>> readCatalog(Reader reader)
	{
		JsonObject json = gson.fromJson(reader, JsonObject.class);
		if (json == null)
		{
			throw new JsonParseException("Translation catalog must be a JSON object");
		}
		Map<String, Map<String, String>> contexts = new HashMap<>();
		for (Map.Entry<String, JsonElement> context : json.entrySet())
		{
			if (!context.getValue().isJsonObject())
			{
				throw new JsonParseException("Translation context must be an object: " + context.getKey());
			}
			Map<String, String> translations = new HashMap<>();
			for (Map.Entry<String, JsonElement> entry : context.getValue().getAsJsonObject().entrySet())
			{
				JsonElement value = entry.getValue();
				if (!value.isJsonPrimitive() || !value.getAsJsonPrimitive().isString())
				{
					throw new JsonParseException("Translation must be a string: " + entry.getKey());
				}
				String translated = value.getAsString();
				if (!translated.isEmpty())
				{
					translations.put(entry.getKey(), translated);
				}
			}
			contexts.put(context.getKey(), Map.copyOf(translations));
		}
		return Map.copyOf(contexts);
	}

	private static final class Catalog
	{
		private final QuestHelperLanguage language;
		private final Map<String, Map<String, String>> entries;

		private Catalog(QuestHelperLanguage language, Map<String, Map<String, String>> entries)
		{
			this.language = language;
			this.entries = entries;
		}

		private String text(String context, String english)
		{
			if (english == null)
			{
				return null;
			}
			if (context != null)
			{
				String translated = entries.getOrDefault(context, Map.of()).get(english);
				if (translated != null)
				{
					return translated;
				}
			}
			return entries.getOrDefault("common", Map.of()).getOrDefault(english, english);
		}
	}
}
