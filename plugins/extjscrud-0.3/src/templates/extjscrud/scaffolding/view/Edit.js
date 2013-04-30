<% import grails.persistence.Event %>

Ext.define('AM.view.${domainClass.getPropertyName()}.Edit', {
    extend:'Ext.window.Window',
    alias:'widget.${domainClass.getPropertyName()}edit',

    requires:['Ext.form.Panel'],

    title: messages.js.edit.window.title('${domainClass.getNaturalName()}'),
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
                    <%  excludedProps = Event.allEvents.toList() << 'version' << 'id' << 'dateCreated' << 'lastUpdated'
                            persistentPropNames = domainClass.persistentProperties*.name
                            props = domainClass.properties.findAll { persistentPropNames.contains(it.name) && !excludedProps.contains(it.name) && !it.isAssociation() }
                            Collections.sort(props, comparator.constructors[0].newInstance([domainClass] as Object[]))
                            display = true
                            //boolean hasHibernate = PluginManagerHolder.pluginManager.hasGrailsPlugin('hibernate')
                            props.eachWithIndex  { p , i ->
                                if (!Collection.class.isAssignableFrom(p.type)) {

                                cp = domainClass.constrainedProperties[p.name]
                                display = (cp ? cp.display : true)

                                if (display) {
                                if(p.type == int.class || p.type == Integer.class )  {  %>
                                {
                                    xtype: 'numberfield',
                                    name: '${p.name}',
                                    fieldLabel: messages.${domainClass.getPropertyName()}.field.${p.name}(),
                                    allowDecimals: false,
                                    hideTrigger: true
                                }<% if(i != props.size-1) {  %>,<% }
                                }else if (p.type == Boolean.class || p.type == boolean.class) {   %>
                                {
                                   xtype: 'checkbox',
                                   name: '${p.name}',
                                   fieldLabel: messages.${domainClass.getPropertyName()}.field.${p.name}() ,
                                   inputValue: 'true',
                                   uncheckedValue: 'false'
                                }<% if(i != props.size-1) {  %>,<% }
                                }else if(p.type == float.class || p.type == Float.class || p.type == double.class || p.type == Double.class || p.type == BigDecimal.class ) {   %>
                                {
                                   xtype: 'numberfield',
                                   name: '${p.name}',
                                   fieldLabel: messages.${domainClass.getPropertyName()}.field.${p.name}(),
                                   allowDecimals: true,
                                   hideTrigger: true
                                }<% if(i != props.size-1) {  %>,<% }
                                }else if (p.type == Date.class || p.type == java.sql.Date.class || p.type == java.sql.Time.class || p.type == Calendar.class) {   %>
                                {
                                   xtype: 'datefield',
                                   name: '${p.name}',
                                   fieldLabel: messages.${domainClass.getPropertyName()}.field.${p.name}(),
                                   format: 'd/m/Y'

                                }<% if(i != props.size-1) {  %>,<% }
                                } else if(p.type == String.class ) {  %>
                                {
                                   xtype: 'textfield',
                                   name: '${p.name}',
                                   fieldLabel: messages.${domainClass.getPropertyName()}.field.${p.name}()
                                }<% if(i != props.size-1) {  %>,<% }
                                }  %>

                                <%} } }%>

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


