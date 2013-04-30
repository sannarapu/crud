<% import grails.persistence.Event %>

Ext.define('AM.view.${domainClass.getPropertyName()}.List' ,{
    extend: 'Ext.grid.Panel',
    alias : 'widget.${domainClass.getPropertyName()}list',

    title : messages.js.list.page.title('${domainClass.getNaturalName()}'),
    store: '${domainClass.getPropertyName()}',

    requires:[

        'Ext.ux.grid.FiltersFeature',
        'Ext.toolbar.Paging'

    ],


    initComponent:function () {

       //this.store = Ext.create('AM.store.${domainClass.getPropertyName()}'),

       this.columns = [<%  excludedProps = Event.allEvents.toList() << 'version' << 'id'
allowedNames = domainClass.persistentProperties*.name << 'dateCreated' << 'lastUpdated'
props = domainClass.properties.findAll { allowedNames.contains(it.name) && !excludedProps.contains(it.name) && !Collection.isAssignableFrom(it.type) }
Collections.sort(props, comparator.constructors[0].newInstance([domainClass] as Object[]))
display = true
props.eachWithIndex { p, i ->
    if (!Collection.class.isAssignableFrom(p.type)) {
    cp = domainClass.constrainedProperties[p.name]
    display = (cp ? cp.display : true)

    if (display) {
    if(p.type == int.class || p.type == Integer.class )  {  %>
{header: messages.${domainClass.getPropertyName()}.field.${p.name}(), dataIndex: '${p.name}', flex: 1}<% if(i != props.size-1) {  %>,<% }
    }else if (p.type == Boolean.class || p.type == boolean.class) {   %>
{header: messages.${domainClass.getPropertyName()}.field.${p.name}(), dataIndex: '${p.name}', flex: 1}<% if(i != props.size-1) {  %>,<% }
    }else if(p.type == float.class || p.type == Float.class || p.type == double.class || p.type == Double.class || p.type == BigDecimal.class ) {   %>
{header: messages.${domainClass.getPropertyName()}.field.${p.name}(), dataIndex: '${p.name}', flex: 1}<% if(i != props.size-1) {  %>,<% }
    }else if (p.type == Date.class || p.type == java.sql.Date.class || p.type == java.sql.Time.class || p.type == Calendar.class) {   %>
{header: messages.${domainClass.getPropertyName()}.field.${p.name}(), dataIndex: '${p.name}', flex: 1, xtype: 'datecolumn', format: 'd/m/Y'}<% if(i != props.size-1) {  %>,<% }
    } else if(p.type == String.class ) {  %>
{header: messages.${domainClass.getPropertyName()}.field.${p.name}(), dataIndex: '${p.name}', flex: 1, filter: {type: 'string'}}<% if(i != props.size-1) {  %>,<% }
    }  %>

    <%} } }%>

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
            store: '${domainClass.getPropertyName()}',
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
