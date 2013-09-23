package com.medisanaspace.jsf;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;

import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.component.submenu.Submenu;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;
import org.springframework.roo.addon.jsf.application.RooJsfApplicationBean;

@RooJsfApplicationBean
public class ApplicationBean {
	
	 private MenuModel  menuModel;
	 
    public String getColumnName(String column) {
        if (column == null || column.length() == 0) {
            return column;
        }
        final Pattern p = Pattern.compile("[A-Z][^A-Z]*");
        final Matcher m = p.matcher(Character.toUpperCase(column.charAt(0)) + column.substring(1));
        final StringBuilder builder = new StringBuilder();
        while (m.find()) {
            builder.append(m.group()).append(" ");
        }
        return builder.toString().trim();
    }

	@PostConstruct
    public void init() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application application = facesContext.getApplication();
        ExpressionFactory expressionFactory = application.getExpressionFactory();
        ELContext elContext = facesContext.getELContext();
        
       
        menuModel = new DefaultMenuModel();
        Submenu submenu;
        MenuItem item;
        
        submenu = new Submenu();
        submenu.setId("logSubmenu");
        submenu.setLabel("Log");
        item = new MenuItem();
        item.setId("createLogMenuItem");
        item.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{messages.label_create}", String.class));
        item.setActionExpression(expressionFactory.createMethodExpression(elContext, "#{logBean.displayCreateDialog}", String.class, new Class[0]));
        item.setIcon("ui-icon ui-icon-document");
        item.setAjax(false);
        item.setAsync(false);
        item.setUpdate(":dataForm:data");
        submenu.getChildren().add(item);
        

        item = new MenuItem();
        item.setId("listLogMenuItem");
        item.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{messages.label_list}", String.class));
        item.setActionExpression(expressionFactory.createMethodExpression(elContext, "#{logBean.displayList}", String.class, new Class[0]));
        item.setIcon("ui-icon ui-icon-folder-open");
        item.setAjax(false);
        item.setAsync(false);
        item.setUpdate(":dataForm:data");
        submenu.getChildren().add(item);
        menuModel.addSubmenu(submenu);
        
        submenu = new Submenu();
        submenu.setId("TestSubmenu");
        submenu.setLabel("API-Test");
        
        item = new MenuItem();
        item.setId("testrunnermenuitem");
        item.setValueExpression("value", expressionFactory.createValueExpression(elContext, "#{messages.label_runntest}", String.class));
        item.setActionExpression(expressionFactory.createMethodExpression(elContext, "#{logBean.displayList}", String.class, new Class[0]));
        item.setIcon("ui-icon ui-icon-folder-open");
        item.setAjax(false);
        item.setAsync(false);
        item.setUpdate(":dataForm:data");
        submenu.getChildren().add(item);
        
        
        
    }
}