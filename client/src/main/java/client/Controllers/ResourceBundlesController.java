package client.Controllers;

import java.util.ResourceBundle;

public class ResourceBundlesController {
    private static ResourceBundlesController RESOURCE_BUNDLE_CONTROLLER = null;

    public static ResourceBundlesController getInstance(){
        if(RESOURCE_BUNDLE_CONTROLLER == null){
            RESOURCE_BUNDLE_CONTROLLER = new ResourceBundlesController();
        }
        return RESOURCE_BUNDLE_CONTROLLER;
    }

    private ResourceBundlesController(){}

    public ResourceBundle getExceptionsBundle() {
        return ResourceBundle.getBundle("Exceptions", LocaleController.getInstance().getCurrentLocale());
    }

    public ResourceBundle getMessagesBundle() {
        return ResourceBundle.getBundle("Messages", LocaleController.getInstance().getCurrentLocale());
    }

    public ResourceBundle getFieldsBundle() {
        return ResourceBundle.getBundle("Fields", LocaleController.getInstance().getCurrentLocale());
    }

    public ResourceBundle getLogInBundle() {
        return ResourceBundle.getBundle("LogInGuiLabels", LocaleController.getInstance().getCurrentLocale());
    }

    public ResourceBundle getMainBundle() {
        return ResourceBundle.getBundle("MainGuiLabels", LocaleController.getInstance().getCurrentLocale());
    }

    public ResourceBundle getSignUpBundle() {
        return ResourceBundle.getBundle("SignUpGuiLabels", LocaleController.getInstance().getCurrentLocale());
    }
}
