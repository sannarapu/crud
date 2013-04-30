

Ext.define('AM.controller.student', {
    extend:'Ext.app.Controller',

    stores:['student'],

    models:['student'],

    views:['student.Add', 'student.Edit', 'student.List'],

    init:function () {
        this.control({
            'viewport > studentlist dataview':{
                itemdblclick:this.editStudent
            },
            'studentedit button[action=save]':{
                click:this.updateStudent
            },
            'studentadd button[action=save]':{
                click:this.updateAddStudent
            },
            'studentlist button[action=add]':{
                click:this.addStudentToolbar
            },
            'studentlist button[action=delete]':{
                click:this.deleteStudentToolbar
            },
            'studentlist button[action=edit]':{
                click:this.editStudentToolbar
            }


        });
    },

    editStudent:function (grid, record) {
        var edit = Ext.create('AM.view.student.Edit').show();
        edit.down('form').loadRecord(record);
    },

    updateStudent:function (button) {
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

    updateAddStudent:function (button) {
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
                var store = this.getStudentStore();
                win.close();
                store.add(record);
                store.sync();
            }
    },

    // Edit the record by using the Edit button in the toolbar
    editStudentToolbar:function (button) {
        var grid = button.up('gridpanel');
        var arraySelected = grid.getSelectionModel().getSelection();
        var record = arraySelected[0];
        if(!record){
            Ext.MessageBox.alert(messages.js.select.error(),messages.js.select.errorDesc());
        }else{
            var edit = Ext.create('AM.view.student.Edit').show();
            edit.down('form').loadRecord(record);
            var firstCmp = edit.down('form').items;
            //alert('firstCmp ='+firstCmp);
            Ext.defer(function(){
                firstCmp.getAt(0).focus();
            }, 20);
        }
    },

    // Add a record by using the Add button in the toolbar
    addStudentToolbar:function (button) {
        var addform = Ext.create('AM.view.student.Add').show();
        record = Ext.create('AM.model.student');
        addform.down('form').loadRecord(record);
        var firstCmp = addform.down('form').items
        //alert('firstCmp ='+firstCmp);
        Ext.defer(function(){
            firstCmp.getAt(0).focus();
        }, 20);
    },

    // Delete the record by using the Delete button in the toolbar
    deleteStudentToolbar:function (button) {
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


