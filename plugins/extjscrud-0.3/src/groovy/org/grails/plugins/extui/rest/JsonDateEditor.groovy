package org.grails.plugins.extui.rest


import org.codehaus.groovy.grails.web.json.JSONObject
import org.springframework.beans.propertyeditors.CustomDateEditor

/**
 * Created by IntelliJ IDEA.
 * User: san
 * Date: 25-04-12
 * Time: 15:38
 * To change this template use File | Settings | File Templates.
 */
class JsonDateEditor extends CustomDateEditor {

    public JsonDateEditor(java.text.DateFormat dateFormat, boolean allowEmpty) {
        super(dateFormat,allowEmpty)
    }

    public JsonDateEditor(java.text.DateFormat dateFormat, boolean allowEmpty, int exactDateLength) {
        super(dateFormat,allowEmpty,exactDateLength)
    }

    @Override
    public void setValue(Object value) {
        if (value) {
            if (JSONObject.NULL.getClass().isInstance(value)) {
                super.setValue(null);
            } else {
                super.setValue(value);
            }
        } else {
            super.setValue(null);
        }
    }

}
