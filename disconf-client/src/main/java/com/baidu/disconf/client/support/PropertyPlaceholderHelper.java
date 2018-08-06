package com.baidu.disconf.client.support;

public class PropertyPlaceholderHelper {
	
	public String replacePlaceholders(String value, PlaceholderResolver placeholderResolver) {
		return parseStringValue(value, placeholderResolver);
	}
	
	protected String parseStringValue(String strVal, PlaceholderResolver placeholderResolver) {

		StringBuilder result = new StringBuilder(strVal);

		int startIndex = strVal.indexOf(PlaceholderResolver.placeholderPrefix);
		if (startIndex != -1) {
			int endIndex = result.indexOf(PlaceholderResolver.placeholderSuffix);
			if (endIndex != -1) {
				String placeholder = result.substring(startIndex + PlaceholderResolver.placeholderPrefix.length(), endIndex);
				String propVal = placeholderResolver.resolvePlaceholder(placeholder);
				if (propVal != null) {
					result.replace(startIndex, endIndex + PlaceholderResolver.placeholderSuffix.length(), propVal);
				}
			}
		}
		return result.toString();
	}
}
