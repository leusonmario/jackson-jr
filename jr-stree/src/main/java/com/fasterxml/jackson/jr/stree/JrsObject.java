package com.fasterxml.jackson.jr.stree;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.TreeNode;

public class JrsObject extends JrsValue
{
    private final Map<String, JrsValue> _values;

    public JrsObject() {
        this(Collections.<String, JrsValue>emptyMap());
    }

    public JrsObject(Map<String, JrsValue> values) {
        _values = values;
    }

    @Override
    public JsonToken asToken() {
        return JsonToken.START_OBJECT;
    }

    @Override
    public int size() {
        return _values.size();
    }

    @Override
    public boolean isValueNode() {
        return false;
    }

    @Override
    public boolean isContainerNode() {
        return true;
    }

    @Override
    public boolean isObject() {
        return true;
    }

    @Override
    public Iterator<String> fieldNames()
    {
        return _values.keySet().iterator();
    }

    @Override
    public TreeNode get(int i) {
        return null;
    }
    
    @Override
    public JrsValue get(String name) {
        return _values.get(name);
    }

    @Override
    public TreeNode path(int i) {
        return JrsMissing.instance();
    }

    @Override
    public JrsValue path(String name) {
        JrsValue v = _values.get(name);
        return (v == null) ? JrsMissing.instance() : v;
    }

    @Override
    protected JrsValue _at(JsonPointer ptr) {
        String prop = ptr.getMatchingProperty();
        // fine to return `null`; caller converts to "missing":
        return get(prop);
    }

    /*
    /**********************************************************************
    /* Extended API
    /**********************************************************************
     */

    public Iterator<Map.Entry<String, JrsValue>> fields() {
        if (_values.isEmpty()) {
            return _values.entrySet().iterator();
        }
        return _values.entrySet().iterator();
    }

    /*
    /**********************************************************************
    /* Abstract methods
    /**********************************************************************
     */

    @Override
    protected void write(JsonGenerator g, JacksonJrsTreeCodec codec) throws IOException
    {
        g.writeStartObject();
        if (!_values.isEmpty()) {
            for (Map.Entry<String,JrsValue> entry : _values.entrySet()) {
                g.writeFieldName(entry.getKey());
                codec.writeTree(g, entry.getValue());
            }
        }
        g.writeEndObject();
    }
}
