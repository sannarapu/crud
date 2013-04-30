

Ext.define('AM.view.student.Edit', {
    extend:'Ext.window.Window',
    alias:'widget.studentedit',

    requires:['Ext.form.Panel'],

    title: messages.js.edit.window.title('Student'),
    layout:'fit',
    autoShow:true,
    height:280,
    width:280,

    initComponent:function () {
        this.items = [
            {
                xtype:'form',
                padding:'5 5 0 5',
                border:false,
                style:'background-color: #fff;',

                items:[
                    {
                        xtype: 'numberfield',
                        name: 'regNumber',
                        fieldLabel: messages.student.field.regNumber(),
                        allowDecimals: false,
                        hideTrigger: true

                    },

                    {
                        xtype: 'textfield',
                        name: 'name',
                        fieldLabel: messages.student.field.name()
                    },



                    {
                        xtype: 'datefield',
                        name: 'dateOfBirth',
                        fieldLabel: messages.student.field.dateOfBirth(),
                        format: 'd/m/Y'

                    },

                    {
                        xtype: 'textfield',
                        name: 'course',
                        fieldLabel: messages.student.field.course()
                    }

                ]
            }];

        this.buttons = [
        {
            text:messages.js.button.saveBtn(),
            action:'save'
        },
        {
            text:messages.js.button.cancelBtn(),
            scope:this,
            handler:this.close
        }];

        this.callParent(arguments);
    }
});


