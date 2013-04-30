package org.grails.plugins.extui.rest

import grails.converters.JSON
import org.codehaus.groovy.grails.web.converters.marshaller.ObjectMarshaller
import java.text.Format
import org.apache.commons.lang.time.FastDateFormat
import org.codehaus.groovy.grails.web.converters.exceptions.ConverterException
import org.codehaus.groovy.grails.web.json.JSONException

/**
 * Created by IntelliJ IDEA.
 * User: san
 * Date: 08-03-12
 * Time: 15:51
 * To change this template use File | Settings | File Templates.
 */
public class JsonDateMarshaller implements ObjectMarshaller<JSON> {

// TODO Tests resulted in java.text.SimpleDateFormat beeing a bit faster - but it's not thread-safe - need to discuss
    //private final Format JSON_DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss'Z'", TimeZone.getTimeZone("GMT"), Locale.US);
    private final Format formatter;

    public JsonDateMarshaller(Format formatter) {
        this.formatter = formatter;
    }

    public JsonDateMarshaller() {
        this(FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss'Z'", TimeZone.getTimeZone("GMT"), Locale.US));
    }

    public boolean supports(Object object) {
        return object instanceof Date;
    }

    public void marshalObject(Object object, JSON converter) throws ConverterException {
        try {
            //converter.getWriter().value(JSON_DATE_FORMAT.format(object));
            converter.getWriter().value(formatter.format(object));
        }
        catch (JSONException e) {
            throw new ConverterException(e);
        }
    }
}