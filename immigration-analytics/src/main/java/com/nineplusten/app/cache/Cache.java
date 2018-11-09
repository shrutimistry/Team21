package com.nineplusten.app.cache;

import java.util.List;
import com.nineplusten.app.model.Agency;
import com.nineplusten.app.model.Permissions;
import com.nineplusten.app.model.Template;
import com.nineplusten.app.model.UserRole;

public class Cache {
  public static List<UserRole> userRoles;
  public static List<Template> templates;
  public static List<Agency> agencies;
  public static List<Permissions> permissions;
}
