package com;

import org.junit.Test;
import org.junit.Ignore;

//import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

//import static org.hamcrest.CoreMatchers.is;

//import net.sf.json.JSONArray;
//import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestJson {
    private static final Logger log = LoggerFactory.getLogger(TestJson.class);

    @Test
    public void testJson() throws Exception {
        log.debug("testJson");

        final String jsonStr = "{}";
        JSONObject j = (JSONObject) JSONSerializer.toJSON(jsonStr);
        assertNotNull(j);

        assertFalse(j.containsKey("data"));
        //String data = j.getString("data");
        //assertNotNull(data);
    }
}
