<% import grails.persistence.Event %>

Ext.define('AM.controller.${domainClass.getPropertyName()}', {
    extend:'Ext.app.Controller',

    stores:['${domainClass.getPropertyName()}'],

    models:['${domainClass.getPropertyName()}'],

    views:['${domainClass.getPropertyName()}.Add', '${domainClass.getPropertyName()}.Edit', '${domainClass.getPropertyName()}.List'],

    init:function () {
        this.control({
            'viewport > ${domainClass.getPropertyName()}list dataview':{
                itemdblclick:this.edit${domainClass.getNaturalName().replaceAll(' ','')}
            },
            '${domainClass.getPropertyName()}edit button[action=save]':{
                click:this.update${domainClass.getNaturalName().replaceAll(' ','')}
            },
            '${domainClass.getPropertyName()}add button[action=save]':{
                click:this.updateAdd${domainClass.getNaturalName().replaceAll(' ','')}
            },
            '${domainClass.getPropertyName()}list button[action=add]':{
                click:this.add${domainClass.getNaturalName().replaceAll(' ','')}Toolbar
            },
            '${domainClass.getPropertyName()}list button[action=delete]':{
                click:this.delete${domainClass.getNaturalName().replaceAll(' ','')}Toolbar
            },
            '${domainClass.getPropertyName()}list button[action=edit]':{
                click:this.edit${domainClass.getNaturalName().replaceAll(' ','')}Toolbar
            }


        });
    },

    edit${domainClass.getNaturalName().replaceAll(' ','')}:function (grid, record) {
        var edit = Ext.create('AM.view.${domainClass.getPropertyName()}.Edit').show();
        edit.down('form').loadRecord(record);
    },

    update${domainClass.getNaturalName().replaceAll(' ','')}:function (button) {
        var win = button.up('window'),
            form = win.down('form'),
            record = form.getRecord(),
            values = form.getValues();
            if (form.getForm().isValid()) {
                for(var key in values) {
                    record.set(key,values[key]) ;
                }
                record.set(values);
                win.close();
                record.store.sync();
            }
    },

    updateAdd${domainClass.getNaturalName().replaceAll(' ','')}:function (button) {
        var win = button.up('window'),
            form = win.down('form'),
            record = form.getRecord(),
            values = form.getValues();

            //var fields = record.fields.items
            //for(var field in fields) {
            //if(field.type == 'date'){
            //var fieldValue = Ext.Date.format(values[field.name],'d/m/Y');
            //alert('field name is '+ field.name+' field value is '+fieldValue);
            //values[field.name] = fieldValue;
            // }

            if (form.getForm().isValid()) {
                for(var key in values) {
                    record.set(key,values[key]) ;
                }
                var store = this.get${domainClass.getNaturalName().replaceAll(' ','')}Store();
                win.close();
                store.add(record);
                store.sync();
            }
    },

    // Edit the record by using the Edit button in the toolbar
    edit${domainClass.getNaturalName().replaceAll(' ','')}Toolbar:function (button) {
        var grid = button.up('gridpanel');
        var arraySelected = grid.getSelectionModel().getSelection();
        var record = arraySelected[0];
        if(!record){
            Ext.MessageBox.alert(messages.js.select.error(),messages.js.select.errorDesc());
        }else{
            var edit = Ext.create('AM.view.${domainClass.getPropertyName()}.Edit').show();
            edit.down('form').loadRecord(record);
            var firstCmp = edit.down('form').items;
            //alert('firstCmp ='+firstCmp);
            Ext.defer(function(){
                firstCmp.getAt(0).focus();
            }, 20);
        }
    },

    // Add a record by using the Add button in the toolbar
    add${domainClass.getNaturalName().replaceAll(' ','')}Toolbar:function (button) {
        var addform = Ext.create('AM.view.${domainClass.getPropertyName()}.Add').show();
        record = Ext.create('AM.model.${domainClass.getPropertyName()}');
        addform.down('form').loadRecord(record);
        var firstCmp = addform.down('form').items
        //alert('firstCmp ='+firstCmp);
        Ext.defer(function(){
            firstCmp.getAt(0).focus();
        }, 20);
    },

    // Delete the record by using the Delete button in the toolbar
    delete${domainClass.getNaturalName().replaceAll(' ','')}Toolbar:function (button) {
        var grid = button.up('gridpanel');
        var arraySelected = grid.getSelectionModel().getSelection();
        var record = arraySelected[0];
        if(!record){
            Ext.MessageBox.alert(messages.js.select.error(),messages.js.select.errorDesc());
        }else{
            Ext.MessageBox.msgButtons['yes'].text = messages.js.deleteFn.yesBtn();
            Ext.MessageBox.msgButtons['no'].text = messages.js.deleteFn.noBtn();
            Ext.MessageBox.confirm(messages.js.deleteFn.confirmText(),
            messages.js.deleteFn.confirmDesc(),
            function (btn, text) {
                if (btn == 'yes') {
                    var store = record.store;
                    store.remove(record);
                    store.sync();
                }
            });
        }

    }


});


