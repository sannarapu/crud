import org.grails.plugins.extui.rest.JsonDateEditorRegistrar
import org.apache.commons.lang.time.FastDateFormat

class ExtjscrudGrailsPlugin {
    // the plugin version
    def version = "0.3"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3.7 > *"
    // the other plugins this plugin depends on
    def dependsOn = [jawr: "3.3.3 > *" ]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def author = "Sudha Naidu Annarapu"
    def authorEmail = "sudha@multi-support.dk"
    def title = "Grails Extjs4 CRUD Plugin"
    def description = '''\\
This Application creates CRUD functionality using Grails 1.3.7 and Extjs 4.0. The communication between the Extjs 4.0 and Grails is JSON communication.
The plugin supports all primitive data types, like int, float, double, date, boolean and String.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/extjscrud"

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before 
    }

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
        customPropertyEditorRegistrar(JsonDateEditorRegistrar)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
        grails.converters.JSON.registerObjectMarshaller(new org.grails.plugins.extui.rest.JsonDateMarshaller(FastDateFormat.getInstance("dd/MM/yyyy", TimeZone.getTimeZone("CET"), Locale.GERMANY)))
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}
