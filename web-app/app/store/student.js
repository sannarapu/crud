


Ext.define('AM.store.student', {
    extend:'Ext.data.Store',
    model:'AM.model.student',
    autoLoad:{params:{start:0,limit:20}},
    pageSize: 20,
    remoteSort:true,
    remoteFilter:true,

    proxy:{
        type:'rest',
        api:{
            create:'api/student',
            read:'api/student',
            update:'api/student',
            destroy:'api/student'
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

