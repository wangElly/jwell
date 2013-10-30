package com.fenwell.jwell.mvc.spi.resolve;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;
import com.fenwell.jwell.mvc.Config;
import com.fenwell.jwell.mvc.Mvcs;
import com.fenwell.jwell.mvc.api.Resolve;
import com.fenwell.jwell.utils.Collections;
import com.fenwell.jwell.utils.Jsons;
import com.fenwell.jwell.utils.Strings;

public class JsonResolve implements Resolve {

    public void resolve(String file, Mvcs ctx) {
        if (Strings.isBlank(file)) {
            return;
        }
        String jsonFile = Jsons.removeComment(file);
        List<String> keys = getKey(jsonFile);
        if (Collections.isEmpty(keys)) {
            return;
        }
        for (String key : keys) {
            Config conf = setConfig(key, jsonFile);
            ctx.set(key, conf);
        }
    }

    private List<String> getKey(String jsonFile) {
        List<String> keys = new ArrayList<String>();
        String regex = "var ([\\w\\$]+?)\\s*=";
        Matcher matcher = Pattern.compile(regex).matcher(jsonFile);
        while (matcher.find()) {
            keys.add(matcher.group(1));
        }
        return keys;
    }

    private Config setConfig(String key, String jsonFile) {
        String jsonStr = getVarConfig(key, jsonFile);
        if (Strings.isEmpty(jsonStr)) {
            return null;
        }
        JSONObject json = (JSONObject) JSONObject.parse(jsonStr);
        Config cfg = Config.newInstance();
        for (String k : json.keySet()) {
            cfg.put(k, json.get(k));
        }
        return cfg;
    }

    private String getVarConfig(String var, String jsonFile) {
        String regex = "var\\s+" + var + "\\s*=\\s*(\\{[\\s\\S]*?\\});";
        Matcher matcher = Pattern.compile(regex).matcher(jsonFile);
        String json = null;
        if (matcher.find()) {
            json = matcher.group(1);
        }
        return json;
    }

}
