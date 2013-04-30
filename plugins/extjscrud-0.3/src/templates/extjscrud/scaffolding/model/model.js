<% import grails.persistence.Event %>

Ext.define('AM.model.${domainClass.getPropertyName()}', {
    extend:'Ext.data.Model',
    idProperty:'id',
    //requires: ['Ext.data.SequentialIdGenerator'],
    //idgen: 'sequential',
    fields:[
    <%  excludedProps = Event.allEvents.toList() << 'version'
        allowedNames = domainClass.persistentProperties*.name << 'id' << 'dateCreated' << 'lastUpdated'
        props = domainClass.properties.findAll { allowedNames.contains(it.name) && !excludedProps.contains(it.name) && !it.isAssociation() }
        Collections.sort(props, comparator.constructors[0].newInstance([domainClass] as Object[])) %>
        <% if(!props.contains('id')) { %>
        {name: 'id', type: 'int'},
        <% } %>
        <% props.eachWithIndex { p, i ->
        isComma = true
        if(i == props.size-1) {  isComma = false }
        if (p.type == Boolean.class || p.type == boolean.class) { %>
        { name: '${p.name}', type: 'boolean'}   <% if(isComma) { %> , <% } %>
        <%  } else if (p.type == Date.class || p.type == java.sql.Date.class || p.type == java.sql.Time.class || p.type == Calendar.class) { %>
        { name: '${p.name}', type: 'date', dateFormat: 'd/m/Y' } <% if(isComma) { %> , <% } %>
        <%  } else if(p.type == int.class || p.type == Integer.class ) { %>
        { name: '${p.name}', type: 'int' }  <% if(isComma) { %> , <% } %>
        <%  } else if(p.type == float.class || p.type == Float.class || p.type == double.class || p.type == Double.class || p.type == BigDecimal.class ) { %>
        { name: '${p.name}', type: 'float' }  <% if(isComma) { %> , <% } %>
        <%  } else if(p.type == String.class ) { %>
        { name: '${p.name}', type: 'string' } <% if(isComma) { %> , <% } %>
        <%  } %>
        <% } %>
    ]
});
