package com.nineplusten.app.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.nineplusten.app.App;
import com.nineplusten.app.cache.Cache;
import com.nineplusten.app.model.SessionModel;
import com.nineplusten.app.model.UserRole;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class LandingController {
  @FXML
  private TemplateCreationController templateCreationController;
  @FXML
  private UserManagementController UserManagementController;
  @FXML
  private AgencyDataViewController agencyDataViewController;
  @FXML
  private TabPane tabBar;

  private App mainApp;

  @FXML
  private void initialize() {}

  public void setMainApp(App mainApp) {
    this.mainApp = mainApp;
    templateCreationController.setMainApp(mainApp);
    UserManagementController.setMainApp(mainApp);
    agencyDataViewController.initDataService(mainApp.getSession().getUser());
  }

  public void configureUserView() {
    SessionModel session = mainApp.getSession();
    List<Tab> tabs = tabBar.getTabs();
    Map<String, Tab> tabMap = tabs.stream().collect(Collectors.toMap(Tab::getText, t -> t));
    
    List<Tab> userTabs = new ArrayList<>();
    UserRole role = session.getUser().getUserRole();
    Cache.permissions.stream().filter(p -> p.getRole().equals(role)).forEach(p -> {
      p.getRights().stream().forEach(right -> {
        userTabs.add(tabMap.get(p.getPermission().permissions().get(right)));
      });
    });
    tabBar.getTabs().retainAll(userTabs);
  }

}
