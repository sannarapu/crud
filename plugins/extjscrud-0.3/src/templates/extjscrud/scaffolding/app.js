<% import grails.persistence.Event %>

Ext.Loader.setConfig({
    enabled: true
});

Ext.Loader.setPath('Ext.ux', 'plugins/extjscrud-0.3/ext-4.0/ux');

Ext.application({
    name: 'AM',
    autoCreateViewport: true,

    controllers: [

        'domain' <% domainClassesSize = application.domainClasses.size()
if(domainClassesSize > 0) { %>,<% }
           domainClasses = application.domainClasses
           domainClasses.eachWithIndex  { p , i ->    %>
        '${p.getPropertyName()}' <% if(i != domainClasses.size()-1) {  %>,<% } } %>



    ],

    launch: function() {

    }
});
