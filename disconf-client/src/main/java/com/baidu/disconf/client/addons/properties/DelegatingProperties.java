package com.baidu.disconf.client.addons.properties;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Overrides all methods of java.util.Properties using delegation.
 * Would implement instead of extend java.util.Properties if it was an interface.
 */
public abstract class DelegatingProperties extends Properties {
    protected abstract Properties getDelegate();

    public void load(InputStream inStream) throws IOException {
        getDelegate().load(inStream);
    }

    public void list(PrintStream out) {
        getDelegate().list(out);
    }

    public void list(PrintWriter out) {
        getDelegate().list(out);
    }

    public Enumeration propertyNames() {
        return getDelegate().propertyNames();
    }

    public void save(OutputStream out, String header) {
        getDelegate().save(out, header);
    }

    public void store(OutputStream out, String header) throws IOException {
        getDelegate().store(out, header);
    }

    public String getProperty(String key) {
        return getDelegate().getProperty(key);
    }

    public Object setProperty(String key, String value) {
        return getDelegate().setProperty(key, value);
    }

    public String getProperty(String key, String defaultValue) {
        return getDelegate().getProperty(key, defaultValue);
    }

    public int hashCode() {
        return getDelegate().hashCode();
    }

    public int size() {
        return getDelegate().size();
    }

    public void clear() {
        getDelegate().clear();
    }

    public boolean isEmpty() {
        return getDelegate().isEmpty();
    }

    public Object clone() {
        return getDelegate().clone();
    }

    public boolean contains(Object value) {
        return getDelegate().contains(value);
    }

    public boolean containsKey(Object key) {
        return getDelegate().containsKey(key);
    }

    public boolean containsValue(Object value) {
        return getDelegate().containsValue(value);
    }

    public boolean equals(Object o) {
        return getDelegate().equals(o);
    }

    public String toString() {
        return getDelegate().toString();
    }

    public Collection values() {
        return getDelegate().values();
    }

    public Enumeration elements() {
        return getDelegate().elements();
    }

    public Enumeration keys() {
        return getDelegate().keys();
    }

    public void putAll(Map t) {
        getDelegate().putAll(t);
    }

    public Set entrySet() {
        return getDelegate().entrySet();
    }

    public Set keySet() {
        return getDelegate().keySet();
    }

    public Object get(Object key) {
        return getDelegate().get(key);
    }

    public Object remove(Object key) {
        return getDelegate().remove(key);
    }

    public Object put(Object key, Object value) {
        return getDelegate().put(key, value);
    }
}
