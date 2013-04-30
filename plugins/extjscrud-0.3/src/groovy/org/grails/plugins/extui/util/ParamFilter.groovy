package org.grails.plugins.extui.util

import grails.converters.JSON
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap

/**
 * Created by IntelliJ IDEA.
 * User: san
 * Date: 15-03-12
 * Time: 09:29
 * To change this template use File | Settings | File Templates.
 */
class ParamFilter {

    private final String fieldName
    private final String fieldType
    private final String fieldValue
    private final String fieldComparison

    public ParamFilter(String fieldName, String type, String value, String comparison) {
        this.fieldName = fieldName;
        this.fieldValue = value;
        this.fieldType = type;
        this.fieldComparison = comparison;
    }

    public String getFieldName() {
        return fieldName
    }

    public String getFieldType() {
        return fieldType
    }

    public String getFieldValue() {
        return fieldValue
    }

    public String getFieldComparison() {
        return fieldComparison
    }

    /*
      This Method is used to create the Query closure depending on the Filter values and Sort values.
     */
    public static Closure getFilterQuery(List<ParamFilter> filters, String sortParam) {

        def query = {
            filters.each {   filter ->
                and {
                    if (filter.getFieldType().equals("numeric") && filter.getFieldComparison().equals("gt")) {
                        gt(filter.getFieldName(), Integer.parseInt(filter.getFieldValue()))
                    } else if (filter.getFieldType().equals("numeric") && filter.getFieldComparison().equals("lt")) {
                        lt(filter.getFieldName(), Integer.parseInt(it.getFieldValue()))
                    } else if (filter.getFieldType().equals("numeric") && filter.getFieldComparison().equals("eq")) {
                        eq(filter.getFieldName(), Integer.parseInt(filter.getFieldValue()))
                    } else if (filter.getFieldType().equals("string")) {
                        ilike(filter.getFieldName(), '%' + filter.getFieldValue() + '%')
                    }
                }
                if (sortParam) {
                    order(JSON.parse(sortParam).property[0], JSON.parse(sortParam).direction[0]?.toString().toLowerCase())
                }
            }
        }
        return query
    }

    /*
     This Method is used to create the Query count closure depending on the Filter values.
    */
    public static Closure getFilterQueryCount(List<ParamFilter> filters) {

        def query = {
            filters.each {   filter ->
                and {
                    if (filter.getFieldType().equals("numeric") && filter.getFieldComparison().equals("gt")) {
                        gt(filter.getFieldName(), Integer.parseInt(filter.getFieldValue()))
                    } else if (filter.getFieldType().equals("numeric") && filter.getFieldComparison().equals("lt")) {
                        lt(filter.getFieldName(), Integer.parseInt(it.getFieldValue()))
                    } else if (filter.getFieldType().equals("numeric") && filter.getFieldComparison().equals("eq")) {
                        eq(filter.getFieldName(), Integer.parseInt(filter.getFieldValue()))
                    } else if (filter.getFieldType().equals("string")) {
                        ilike(filter.getFieldName(), '%' + filter.getFieldValue() + '%')
                    }
                }

            }
        }
        return query
    }

    /*
      This method is convert Filter URL Parameters to the Param Filter Objects.
     */
    public static List<ParamFilter> decodeFilterParameters(GrailsParameterMap params) {

        List<ParamFilter> paramFilters = new ArrayList<ParamFilter>();
        def filterParams = params.findAll {key, value -> key =~ /filter\[\d\]\[field\]/ }
        //println('filterParams class = ' + filterParams.class)
        filterParams.each { key, value ->
            def index = key.toString().substring(7, 8);
            //ParamFilter pFilter = new ParamFilter();
            //pFilter.setFieldName(value)
            //println('fieldName ' + value);
            def fieldType = params.find { k, v -> k =~ /filter\[/ + index + /\]\[data\]\[type\]/ }
            //println('fieldValue ' + fieldType.getValue());
            //pFilter.setFieldType(fieldType.getValue())
            def fieldValue = params.find { k, v -> k =~ /filter\[/ + index + /\]\[data\]\[value\]/ }
            //println('fieldValue ' + fieldValue.getValue());
            //pFilter.setFieldValue(fieldValue.getValue());
            def fieldCompare = params.find { k, v -> k =~ /filter\[/ + index + /\]\[data\]\[comparison\]/ }
            //println('fieldCompare ' + fieldCompare.getValue());
            //pFilter.setFieldComparison(fieldCompare.getValue());
            ParamFilter pFilter;
            if(fieldCompare){
                pFilter = new ParamFilter(value, fieldType.getValue(), fieldValue.getValue(), fieldCompare.getValue())
            }else{
                pFilter = new ParamFilter(value, fieldType.getValue(), fieldValue.getValue(), null)
            }
            paramFilters.add(pFilter)
        }

        return paramFilters
    }
}
