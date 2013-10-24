package com.fenwell.jwell.mvc.spi.resolve;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;
import com.fenwell.jwell.mvc.Config;
import com.fenwell.jwell.mvc.Mvcs;
import com.fenwell.jwell.mvc.api.Resolve;
import com.fenwell.jwell.utils.Jsons;
import com.fenwell.jwell.utils.Strings;

public class JsonResolve implements Resolve {

    public void resolve(String file, Mvcs ctx) {
        if (Strings.isBlank(file)) {
            return;
        }
        String jsonFile = Jsons.removeComment(file);
        Config loader = setConfig(Mvcs.CONFIG_LOADER, jsonFile);
        Config mvc = setConfig(Mvcs.CONFIG_MVC, jsonFile);
        ctx.set(Mvcs.CONFIG_LOADER, loader);
        ctx.set(Mvcs.CONFIG_MVC, mvc);
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
