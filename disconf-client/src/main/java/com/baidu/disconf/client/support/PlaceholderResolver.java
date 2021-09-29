package com.baidu.disconf.client.support;

public interface PlaceholderResolver {
	
	String placeholderPrefix = "${";
	
	String placeholderSuffix = "}";
	
	String resolvePlaceholder(String placeholderName);
}
