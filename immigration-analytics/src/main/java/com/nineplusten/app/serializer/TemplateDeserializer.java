package com.nineplusten.app.serializer;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.nineplusten.app.model.Template;

public class TemplateDeserializer implements JsonDeserializer<Template>{

  @Override
  public Template deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    JsonObject o = json.getAsJsonObject();
    String id = o.get("_id").getAsString();
    String templateName = o.get("template_name").getAsString();
    Map<String, String> columns = new LinkedHashMap<>();
    JsonArray columnIds = o.get("column_ids").getAsJsonArray();
    JsonArray columnNames = o.get("column_names").getAsJsonArray();
    for (int i = 0; i < columnIds.size(); i++) {
      columns.put(columnIds.get(i).getAsString(), columnNames.get(i).getAsString());
    }
    return new Template(id, templateName, columns);
  }

}
