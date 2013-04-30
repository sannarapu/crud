

Ext.define('AM.model.student', {
    extend:'Ext.data.Model',
    idProperty:'id',
    //requires: ['Ext.data.SequentialIdGenerator'],
    //idgen: 'sequential',
    fields:[
    
        
        {name: 'id', type: 'int'},
        
        
        
        { name: 'course', type: 'string' }  , 
        
        
        { name: 'dateOfBirth', type: 'date', dateFormat: 'd/m/Y' }  , 
        
        
        { name: 'name', type: 'string' }  , 
        
        
        { name: 'regNumber', type: 'int' }  
        
        
    ]
});
