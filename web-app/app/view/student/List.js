

Ext.define('AM.view.student.List' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.studentlist',

    title : messages.js.list.page.title('Student'),
    store: 'student',

    requires:[

        'Ext.ux.grid.FiltersFeature',
        'Ext.toolbar.Paging'

    ],


    initComponent:function () {

       //this.store = Ext.create('AM.store.student'),

       this.columns = [

           {header: messages.student.field.regNumber(), dataIndex: 'regNumber', flex: 1},

           {header: messages.student.field.name(), dataIndex: 'name', flex: 1, filter: {type: 'string'}},

           {header: messages.student.field.dateOfBirth(), dataIndex: 'dateOfBirth', flex: 1, xtype: 'datecolumn', format: 'd/m/Y'},

           {header: messages.student.field.course(), dataIndex: 'course', flex: 1, filter: {type: 'string'}}
    

],
    this.dockedItems = [
        {
            xtype: 'toolbar',
            dock: 'top',
            items: [
                {
                    text: messages.js.toolbar.addTbr(),
                    action: 'add'
                },
                {
                    text: messages.js.toolbar.editTbr(),
                    action: 'edit'
                },
                {
                    text: messages.js.toolbar.deleteTbr(),
                    action: 'delete'
                }
            ]
        },
        {
            xtype: 'pagingtoolbar',
            store: 'student',
            dock: 'bottom',
            displayInfo: true,
            beforePageText: messages.js.pagination.beforepageMsg(),
            afterPageText: messages.js.pagination.afterpageMsg(),
            displayMsg: messages.js.pagination.displayMsg(),
            emptyMsg: messages.js.pagination.emptyMsg()

        }
    ],
           this.features = [ { ftype : 'filters',
                               local: false,
                               showMenu: true} ]


        this.callParent(arguments);
    }
});
