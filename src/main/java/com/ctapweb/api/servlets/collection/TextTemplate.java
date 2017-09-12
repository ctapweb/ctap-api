package com.ctapweb.api.servlets.collection;

import java.util.Arrays;
import java.util.Map;

import net.hamnaberg.json.Property;
import net.hamnaberg.json.Template;
import net.hamnaberg.json.ValueFactory;

public class TextTemplate {
	public static final String KEY_TEXT_TITLE = "text-title";
	public static final String KEY_TEXT_CONTENT = "text-content";
	public static final String KEY_TEXT_LANGUAGE = "text-language";
	public static final String KEY_FILE_TYPE = "file-type";
	
	private String textTitle;
	private String textContent;
	private String textLanguage;
	private String fileType;

	/**
	 * Generate a text template in collection+json that looks like:
	 * <pre>
	 * {@code
	 *{"template": {
	 *            "data": [
	 *                {
	 *                    "name": "text-title",
	 *                    "prompt": "Text-title",
	 *                    "value": "title of text"
	 *                },
	 *                {
	 *                    "name": "text-content",
	 *                    "prompt": "Text-content",
	 *                    "value": "text content to be analyzed"
	 *                },
	 *                {
	 *                    "name": "file-type",
	 *                    "prompt": "File-type",
	 *                    "value": "a mime type, e.g. 'application/msword', 'text/plain', 'application/gzip'"
	 *                }
	 *            ]
	 *        }
	 *}
	 * }
	 * </pre>
	 * @return
	 */
	public static Template generateTemplate() {
		Template template = Template.create(Arrays.asList(
				Property.template(KEY_TEXT_TITLE).withValue(ValueFactory.createValue("title of text")),
				Property.template(KEY_TEXT_CONTENT).withValue(ValueFactory.createValue("text content to be analyzed")),
				Property.template(KEY_TEXT_LANGUAGE).withValue(ValueFactory.createValue("language of the text: 'en', 'fr', 'de', or 'es'")),
				Property.template(KEY_FILE_TYPE).withValue(
						ValueFactory.createValue("a mime type, e.g. 'application/msword', 'text/plain', 'application/gzip', etc."))
				));
		return template;
	}

	public TextTemplate() {
		
	}

	public TextTemplate(String textTitle, String textContent, String textLanguage, String fileType) {
		super();
		this.textTitle = textTitle;
		this.textContent = textContent;
		this.textLanguage = textLanguage;
		this.fileType = fileType;
	}

	public TextTemplate(Template template) {
		Map<String, Property> dataMap = template.getDataAsMap();
		this.textTitle = dataMap.get(KEY_TEXT_TITLE).getValue().get().asString();
		this.textContent = dataMap.get(KEY_TEXT_CONTENT).getValue().get().asString();
		this.textLanguage = dataMap.get(KEY_TEXT_LANGUAGE).getValue().get().asString();
		this.fileType = dataMap.get(KEY_FILE_TYPE).getValue().get().asString();
	}

	public String getTextTitle() {
		return textTitle;
	}

	public void setTextTitle(String textTitle) {
		this.textTitle = textTitle;
	}

	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

	public String getTextLanguage() {
		return textLanguage;
	}

	public void setTextLanguage(String textLanguage) {
		this.textLanguage = textLanguage;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
}
