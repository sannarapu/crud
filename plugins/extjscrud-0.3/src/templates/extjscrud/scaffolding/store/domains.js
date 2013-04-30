<% import grails.persistence.Event %>

Ext.define('AM.store.domains', {
    extend:'Ext.data.TreeStore',

    autoLoad:true,

    root: {
        expanded: true,
        text: 'Domains',
        children: [

           <% domainClassesSize = application.domainClasses.size()
           if(domainClassesSize > 0) {
           domainClasses = application.domainClasses
           domainClasses.eachWithIndex  { p , i ->    %>
           { text: '${p.getPropertyName()}list', leaf: true } <% if(i != domainClasses.size()-1) {  %>,<% } } } %>




        ]
    },
    sorters:[
        {
            property:'text',
            direction:'ASC'
        }
    ]

});
