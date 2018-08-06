package com.baidu.disconf.client.support;

import java.util.Properties;

public class PropertyPlaceholderConfigurerResolver implements PlaceholderResolver{

	private Properties props;
	
	public PropertyPlaceholderConfigurerResolver(){
		//
	}

	public PropertyPlaceholderConfigurerResolver(Properties props) {
		this.props = props;
	}
	
	public String resolvePlaceholder(String placeholderName) {
		String propVal = null;
		if(this.props != null){
			propVal = this.props.getProperty(placeholderName);
		}
		if(propVal == null){
			propVal = System.getProperty(placeholderName);
		}
		if (propVal == null) {
			propVal = System.getenv(placeholderName);
		}
		return propVal;
	}

	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

}
