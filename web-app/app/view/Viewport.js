

Ext.define('AM.view.Viewport', {
    extend:'Ext.container.Viewport',
    layout:'border',

    requires:[

     'Ext.state.*',
     'Ext.layout.container.Border' ,
        'AM.view.student.List' 

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
                      
                        {
                           stateful: true,
                           stateId: 'studentlist',
                           xtype:'studentlist',
                           closable: true,
                           listeners: {
                               beforeclose: function(panel,opts){
                                   this.ownerCt.remove(panel);
                               }
                           }
                        } 
                ]
            }];

        this.callParent();
    }
});

