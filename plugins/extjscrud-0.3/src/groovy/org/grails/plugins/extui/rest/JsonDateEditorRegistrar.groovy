package org.grails.plugins.extui.rest;

// Enables JSON serialized dates to be properly deserialized
// 
// Ref: http://stackoverflow.com/questions/2871977/binding-a-grails-date-from-params-in-a-controller


import java.text.SimpleDateFormat
import org.springframework.beans.PropertyEditorRegistrar
import org.springframework.beans.PropertyEditorRegistry
import org.springframework.beans.propertyeditors.CustomDateEditor

public class JsonDateEditorRegistrar implements PropertyEditorRegistrar {

    public void registerCustomEditors(PropertyEditorRegistry registry) {

        registry.registerCustomEditor(Date, new JsonDateEditor(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"), true))
    }
}
