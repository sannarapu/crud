<% import grails.persistence.Event %>

Ext.define('AM.view.Viewport', {
    extend:'Ext.container.Viewport',
    layout:'border',

    requires:[

     'Ext.state.*',
     'Ext.layout.container.Border' <% domainClassesSize = application.domainClasses.size()
if(domainClassesSize > 0) { %>,<% }
           domainClasses = application.domainClasses
           domainClasses.eachWithIndex  { p , i ->    %>
        'AM.view.${p.getPropertyName()}.List' <% if(i != domainClasses.size()-1) {  %>,<% } } %>

    ],

    initComponent:function () {

        // setup the state provider, all state information will be saved to a cookie
        Ext.state.Manager.setProvider(Ext.create('Ext.state.CookieProvider'));

        this.items = [
            {
                region: 'center',
                xtype:'tabpanel',
                flex:1,
                layout:'fit',
                items:[
                      <% domainClassesSize = application.domainClasses.size()
                        domainClasses = application.domainClasses
                        if(domainClassesSize > 0) {  %>
                        {
                           stateful: true,
                           stateId: '${domainClasses[0].getPropertyName()}list',
                           xtype:'${domainClasses[0].getPropertyName()}list',
                           closable: true,
                           listeners: {
                               beforeclose: function(panel,opts){
                                   this.ownerCt.remove(panel);
                               }
                           }
                        } <% } %>
                ]
            },
            {
                region: 'west',
                width: 225,
                xtype: 'treepanel',
                collapsible: true,
                store: Ext.create('AM.store.domains')
            }];

        this.callParent();
    }
});

