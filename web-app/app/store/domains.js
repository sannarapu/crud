

Ext.define('AM.store.domains', {
    extend:'Ext.data.TreeStore',

    autoLoad:true,

    root: {
        expanded: true,
        text: 'Domains',
        children: [

           
           { text: 'studentlist', leaf: true } 




        ]
    },
    sorters:[
        {
            property:'text',
            direction:'ASC'
        }
    ]

});
