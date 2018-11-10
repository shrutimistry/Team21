package com.nineplusten.app.serializer;

import java.lang.reflect.Type;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.nineplusten.app.model.Template;

public class TemplateSerializer implements JsonSerializer<Template>{

  @Override
  public JsonElement serialize(Template src, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject templateJson = new JsonObject();
    templateJson.add("_id", new JsonPrimitive(src.get_id()));
    templateJson.add("template_name", new JsonPrimitive(src.getTemplateName()));
    JsonArray columnIds = new JsonArray();
    JsonArray columnNames = new JsonArray();
    src.getColumns().entrySet().stream().forEach(col -> {
      columnIds.add(col.getKey());
      columnNames.add(col.getValue());
    });
    templateJson.add("column_ids", columnIds);
    templateJson.add("column_names", columnNames);
    return templateJson;
  }

}
