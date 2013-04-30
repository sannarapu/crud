<% import grails.persistence.Event %>


Ext.define('AM.store.${domainClass.getPropertyName()}', {
    extend:'Ext.data.Store',
    model:'AM.model.${domainClass.getPropertyName()}',
    autoLoad:{params:{start:0,limit:20}},
    pageSize: 20,
    remoteSort:true,
    remoteFilter:true,

    proxy:{
        type:'rest',
        api:{
            create:'api/${domainClass.getPropertyName()}',
            read:'api/${domainClass.getPropertyName()}',
            update:'api/${domainClass.getPropertyName()}',
            destroy:'api/${domainClass.getPropertyName()}'
        },
        reader:{
            root:'data',
            successProperty:'success',
            totalProperty: 'totalCount'
        },
        writer:{
            root:'data',
            successProperty:'success'
        }
    }
});

