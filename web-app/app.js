

Ext.Loader.setConfig({
    enabled: true
});

Ext.Loader.setPath('Ext.ux', 'plugins/extjscrud-0.3/ext-4.0/ux');

Ext.application({
    name: 'AM',
    autoCreateViewport: true,

    controllers: [

        'domain' ,
        'student' 



    ],

    launch: function() {

    }
});
