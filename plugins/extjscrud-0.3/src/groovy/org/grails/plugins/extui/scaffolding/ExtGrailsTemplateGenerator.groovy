package org.grails.plugins.extui.scaffolding

import org.codehaus.groovy.grails.scaffolding.GrailsTemplateGenerator
import org.springframework.context.ResourceLoaderAware
import org.codehaus.groovy.grails.plugins.PluginManagerAware
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import groovy.text.SimpleTemplateEngine
import org.springframework.core.io.ResourceLoader
import groovy.text.Template
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.plugins.GrailsPluginManager
import org.codehaus.groovy.grails.commons.GrailsDomainClass
import org.springframework.util.Assert
import org.codehaus.groovy.grails.scaffolding.DomainClassPropertyComparator
import org.codehaus.groovy.grails.scaffolding.SimpleDomainClassPropertyComparator
import org.codehaus.groovy.grails.cli.CommandLineHelper
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import grails.persistence.Event

/**
 * Created by IntelliJ IDEA.
 * User: san
 * Date: 29-02-12
 * Time: 17:26
 * To change this template use File | Settings | File Templates.
 */
class ExtGrailsTemplateGenerator implements GrailsTemplateGenerator, ResourceLoaderAware, PluginManagerAware {
    static final Log LOG = LogFactory.getLog(ExtGrailsTemplateGenerator)

    String basedir = "."
    File extjscrudPluginDir
    boolean overwrite = false
    def engine = new SimpleTemplateEngine()
    ResourceLoader resourceLoader
    Template renderEditorTemplate
    String domainSuffix = 'Instance'
    GrailsApplication grailsApplication
    GrailsPluginManager pluginManager

    /**
     * Used by the scripts so that they can pass in their AntBuilder instance.
     */
    ExtGrailsTemplateGenerator(ClassLoader classLoader) {
        engine = new SimpleTemplateEngine(classLoader)
    }

    /**
     * Default constructor.
     */
    ExtGrailsTemplateGenerator() {}

    void setGrailsApplication(GrailsApplication ga) {
        this.grailsApplication = ga
        if (ga != null) {
            def suffix = ga.config?.grails?.scaffolding?.templates?.domainSuffix
            if (suffix != [:]) {
                domainSuffix = suffix
            }
        }
    }

    void setExtuiPluginDir(File extjscrudPluginDir) {
        this.extjscrudPluginDir = extjscrudPluginDir
    }

    void setResourceLoader(ResourceLoader rl) {
        LOG.info "Scaffolding template generator set to use resource loader ${rl}"
        this.resourceLoader = rl
    }

    // uses the type to render the appropriate editor
    def renderEditor = { property ->
        def domainClass = property.domainClass
        def cp
        if (pluginManager.hasGrailsPlugin('hibernate')) {
            cp = domainClass.constrainedProperties[property.name]
        }

        if (!renderEditorTemplate) {
            // create template once for performance
            def templateText = getTemplateText("renderEditor.template")
            renderEditorTemplate = engine.createTemplate(templateText)
        }

        def binding = [application: grailsApplication,
                property: property,
                domainClass: domainClass,
                cp: cp,
                domainInstance: getPropertyName(domainClass)]
        return renderEditorTemplate.make(binding).toString()
    }

    void generateViews(GrailsDomainClass domainClass, String destdir) {
        Assert.hasText destdir, "Argument [destdir] not specified"

        def appDir = new File("${destdir}/web-app/app")
        if (!appDir.exists()) {
            appDir.mkdir()
        }

        def viewHomeDir = new File("${destdir}/web-app/app/view")
        if (!viewHomeDir.exists()) {
            viewHomeDir.mkdir()
        }

        def viewsDir = new File("${destdir}/web-app/app/view/${domainClass.propertyName}")
        if (!viewsDir.exists()) {
            viewsDir.mkdirs()
        }

        for (t in getExtViewTemplateNames()) {
            println('generateViews t= ' + t)
            LOG.info "Generating $t view for domain class [${domainClass.fullName}]"
            generateView domainClass, t, viewsDir.absolutePath
        }
    }

    void generateExtController(GrailsDomainClass domainClass, String destdir) {
        Assert.hasText destdir, "Argument [destdir] not specified"

        def appDir = new File("${destdir}/web-app/app")
        if (!appDir.exists()) {
            appDir.mkdir()
        }

        def controllerHomeDir = new File("${destdir}/web-app/app/controller")
        if (!controllerHomeDir.exists()) {
            controllerHomeDir.mkdir()
        }

        for (t in getExtControllerTemplateNames()) {
            println('generateViews t= ' + t)
            LOG.info "Generating $t view for domain class [${domainClass.fullName}]"
            generateView domainClass, t, controllerHomeDir.absolutePath
        }


    }

    void generateExtModel(GrailsDomainClass domainClass, String destdir) {
        Assert.hasText destdir, "Argument [destdir] not specified"

        def appDir = new File("${destdir}/web-app/app")
        if (!appDir.exists()) {
            appDir.mkdir()
        }

        def modelHomeDir = new File("${destdir}/web-app/app/model")
        if (!modelHomeDir.exists()) {
            modelHomeDir.mkdir()
        }

        for (t in getExtModelTemplateNames()) {
            println('generateViews t= ' + t)
            LOG.info "Generating $t view for domain class [${domainClass.fullName}]"
            generateView domainClass, t, modelHomeDir.absolutePath
        }


    }

    void generateExtStore(GrailsDomainClass domainClass, String destdir) {
        Assert.hasText destdir, "Argument [destdir] not specified"

        def appDir = new File("${destdir}/web-app/app")
        if (!appDir.exists()) {
            appDir.mkdir()
        }

        def storeHomeDir = new File("${destdir}/web-app/app/store")
        if (!storeHomeDir.exists()) {
            storeHomeDir.mkdir()
        }

        for (t in getExtStoreTemplateNames()) {
            println('generateViews t= ' + t)
            LOG.info "Generating $t view for domain class [${domainClass.fullName}]"
            generateView domainClass, t, storeHomeDir.absolutePath
        }


    }

    void generateExtAppView(GrailsDomainClass domainClass, String destdir) {
        Assert.hasText destdir, "Argument [destdir] not specified"

        def appDir = new File("${destdir}/web-app")
        if (!appDir.exists()) {
            appDir.mkdir()
        }

        for (t in getExtAppTemplateNames()) {
            println('generateViews t= ' + t)
            LOG.info "Generating $t view for domain class [${domainClass.fullName}]"
            generateView domainClass, t, appDir.absolutePath
        }

    }

    void generateExtViewport(GrailsDomainClass domainClass, String destdir) {
        Assert.hasText destdir, "Argument [destdir] not specified"

        def appDir = new File("${destdir}/web-app/app")
        if (!appDir.exists()) {
            appDir.mkdir()
        }

        def viewHomeDir = new File("${destdir}/web-app/app/view")
        if (!viewHomeDir.exists()) {
            viewHomeDir.mkdir()
        }

        for (t in getExtViewportTemplateNames()) {
            println('generateViews t= ' + t)
            LOG.info "Generating $t view for domain class [${domainClass.fullName}]"
            generateView domainClass, t, viewHomeDir.absolutePath
        }

    }


    void generateController(GrailsDomainClass domainClass, String destdir) {
        Assert.hasText destdir, "Argument [destdir] not specified"

        if (domainClass) {
            def fullName = domainClass.fullName
            def pkg = ""
            def pos = fullName.lastIndexOf('.')
            if (pos != -1) {
                // Package name with trailing '.'
                pkg = fullName[0..pos]
            }

            def destFile = new File("${destdir}/grails-app/controllers/${pkg.replace('.' as char, '/' as char)}${domainClass.shortName}Controller.groovy")
            if (canWrite(destFile)) {
                destFile.parentFile.mkdirs()

                destFile.withWriter { w ->
                    generateController(domainClass, w)
                }

                LOG.info("Controller generated at ${destFile}")
            }
        }
    }

    void generateView(GrailsDomainClass domainClass, String viewName, String destDir) {
        println('generateView destDir= ' + destDir)
        String fileName = viewName
        if (viewName.equals("controller") || viewName.equals("model") || viewName.equals("store")) {
            fileName = "${domainClass.propertyName}"
        }
        File destFile = new File("$destDir/${fileName}.js")
        if (canWrite(destFile)) {
            destFile.withWriter { Writer writer ->
                generateView domainClass, viewName, writer
            }
        }
    }

    void generateView(GrailsDomainClass domainClass, String viewName, Writer out) {
        println('generateView viewName= ' + viewName)
        def templateText = getExtTemplateText("${viewName}.js")

        def t = engine.createTemplate(templateText)
        def multiPart = domainClass.properties.find {it.type == ([] as Byte[]).class || it.type == ([] as byte[]).class}

        boolean hasHibernate = pluginManager.hasGrailsPlugin('hibernate')
        def packageName = domainClass.packageName ? "<%@ page import=\"${domainClass.fullName}\" %>" : ""
        def binding = [application: grailsApplication,
                packageName: packageName,
                domainClass: domainClass,
                multiPart: multiPart,
                className: domainClass.shortName,
                propertyName: getPropertyName(domainClass),
                renderEditor: renderEditor,
                comparator: hasHibernate ? DomainClassPropertyComparator : SimpleDomainClassPropertyComparator]

        t.make(binding).writeTo(out)
    }

    void generateController(GrailsDomainClass domainClass, Writer out) {
        def templateText = getTemplateText("Controller.groovy")

        boolean hasHibernate = pluginManager.hasGrailsPlugin('hibernate')
        def binding = [application: grailsApplication,
                packageName: domainClass.packageName,
                domainClass: domainClass,
                className: domainClass.shortName,
                propertyName: getPropertyName(domainClass),
                comparator: hasHibernate ? DomainClassPropertyComparator : SimpleDomainClassPropertyComparator]

        def t = engine.createTemplate(templateText)
        t.make(binding).writeTo(out)
    }

    private String getPropertyName(GrailsDomainClass domainClass) { "${domainClass.propertyName}${domainSuffix}" }

    private helper = new CommandLineHelper()

    private canWrite(testFile) {
        if (!overwrite && testFile.exists()) {
            try {
                def response = helper.userInput("File ${testFile} already exists. Overwrite?", ['y', 'n', 'a'] as String[])
                overwrite = overwrite || response == "a"
                return overwrite || response == "y"
            }
            catch (Exception e) {
                // failure to read from standard in means we're probably running from an automation tool like a build server
                return true
            }
        }
        return true
    }

    private getTemplateText(String template) {
        def application = grailsApplication
        // first check for presence of template in application
        if (resourceLoader && application?.warDeployed) {
            return resourceLoader.getResource("/WEB-INF/templates/extjscrud/scaffolding/${template}").inputStream.text
        }

        println('getTemplateText template= ' + template)
        def templateFile = new FileSystemResource("${basedir}/src/templates/extjscrud/scaffolding/${template}")
        if (!templateFile.exists()) {
            templateFile = new FileSystemResource("${extjscrudPluginDir}/src/templates/extjscrud/scaffolding/${template}")
        }
        return templateFile.inputStream.getText()
    }

    private getExtTemplateText(String template) {
        def application = grailsApplication

        // first check for presence of template in application
        if (resourceLoader && application?.warDeployed) {
            def resource = resourceLoader.getResource("/WEB-INF/templates/extjscrud/scaffolding/view/${template}")
            if (!resource.exists()) {
                resource = resourceLoader.getResource("/WEB-INF/templates/extjscrud/scaffolding/controller/${template}")
            }
            if (!resource.exists()) {
                resource = resourceLoader.getResource("/WEB-INF/templates/extjscrud/scaffolding/model/${template}")
            }
            if (!resource.exists()) {
                resource = resourceLoader.getResource("/WEB-INF/templates/extjscrud/scaffolding/store/${template}")
            }
            if (!resource.exists()) {
                resource = resourceLoader.getResource("/WEB-INF/templates/extjscrud/scaffolding/store/${template}")
            }
            return resource.inputStream.text
        }

        println('getExtTemplateText template= ' + template)
        def templateFile = new FileSystemResource("${extjscrudPluginDir}/src/templates/extjscrud/scaffolding/view/${template}")
        if (!templateFile.exists()) {
            templateFile = new FileSystemResource("${extjscrudPluginDir}/src/templates/extjscrud/scaffolding/controller/${template}")
        }
        if (!templateFile.exists()) {
            templateFile = new FileSystemResource("${extjscrudPluginDir}/src/templates/extjscrud/scaffolding/model/${template}")
        }
        if (!templateFile.exists()) {
            templateFile = new FileSystemResource("${extjscrudPluginDir}/src/templates/extjscrud/scaffolding/store/${template}")
        }
        if (!templateFile.exists()) {
            templateFile = new FileSystemResource("${extjscrudPluginDir}/src/templates/extjscrud/scaffolding/${template}")
        }
        return templateFile.inputStream.getText()
    }




    def getExtViewTemplateNames() {
        Closure filter = { it[0..-4] }
        def resources = []
        def resolver = new PathMatchingResourcePatternResolver()
        String templatesDirPath = "${basedir}/src/templates/extjscrud/scaffolding/view"
        def templatesDir = new FileSystemResource(templatesDirPath)
        if (templatesDir.exists()) {
            try {
                println("picking ${basedir}/src/templates/extjscrud/scaffolding/view/*.js")
                resources = resolver.getResources("file:$templatesDirPath/*.js").filename.collect(filter)
            }
            catch (e) {
                LOG.info("Error while loading views from grails-app scaffolding folder", e)
            }
        } else {
            try {
                def pluginsHomeTemplates = resolver.getResources("file:${extjscrudPluginDir}/src/templates/extjscrud/scaffolding/view/*.js").filename.collect(filter)
                resources.addAll(pluginsHomeTemplates)
            }
            catch (e) {
                // ignore
                LOG.debug("Error locating templates from pluginsHome: ${e.message}", e)
            }
        }
        return resources

    }

    def getExtControllerTemplateNames() {
        Closure filter = { it[0..-4] }
        def resources = []
        def resolver = new PathMatchingResourcePatternResolver()
        String templatesDirPath = "${basedir}/src/templates/extjscrud/scaffolding/controller"
        def templatesDir = new FileSystemResource(templatesDirPath)
        if (templatesDir.exists()) {
            try {
                println("picking ${basedir}/src/templates/extjscrud/scaffolding/controller/*.js")
                resources = resolver.getResources("file:$templatesDirPath/*.js").filename.collect(filter)
            }
            catch (e) {
                LOG.info("Error while loading views from grails-app scaffolding folder", e)
            }
        } else {
            try {
                def pluginsHomeTemplates = resolver.getResources("file:${extjscrudPluginDir}/src/templates/extjscrud/scaffolding/controller/*.js").filename.collect(filter)
                resources.addAll(pluginsHomeTemplates)
            }
            catch (e) {
                // ignore
                LOG.debug("Error locating templates from pluginsHome: ${e.message}", e)
            }
        }
        return resources
    }

    def getExtModelTemplateNames() {
        Closure filter = { it[0..-4] }
        def resources = []
        def resolver = new PathMatchingResourcePatternResolver()
        String templatesDirPath = "${basedir}/src/templates/extjscrud/scaffolding/model"
        def templatesDir = new FileSystemResource(templatesDirPath)
        if (templatesDir.exists()) {
            try {
                println("picking ${basedir}/src/templates/extjscrud/scaffolding/model/*.js")
                resources = resolver.getResources("file:$templatesDirPath/*.js").filename.collect(filter)
            }
            catch (e) {
                LOG.info("Error while loading views from grails-app scaffolding folder", e)
            }
        } else {
            try {
                def pluginsHomeTemplates = resolver.getResources("file:${extjscrudPluginDir}/src/templates/extjscrud/scaffolding/model/*.js").filename.collect(filter)
                resources.addAll(pluginsHomeTemplates)
            }
            catch (e) {
                // ignore
                LOG.debug("Error locating templates from pluginsHome: ${e.message}", e)
            }
        }
        return resources
    }

    def getExtStoreTemplateNames() {
        Closure filter = { it[0..-4] }
        def resources = []
        def resolver = new PathMatchingResourcePatternResolver()
        String templatesDirPath = "${basedir}/src/templates/extjscrud/scaffolding/store"
        def templatesDir = new FileSystemResource(templatesDirPath)
        if (templatesDir.exists()) {
            try {
                println("picking ${basedir}/src/templates/extjscrud/scaffolding/store/*.js")
                resources = resolver.getResources("file:$templatesDirPath/*.js").filename.collect(filter)
            }
            catch (e) {
                LOG.info("Error while loading views from grails-app scaffolding folder", e)
            }
        } else {
            try {
                def pluginsHomeTemplates = resolver.getResources("file:${extjscrudPluginDir}/src/templates/extjscrud/scaffolding/store/*.js").filename.collect(filter)
                resources.addAll(pluginsHomeTemplates)
            }
            catch (e) {
                // ignore
                LOG.debug("Error locating templates from pluginsHome: ${e.message}", e)
            }
        }
        return resources
    }

    def getExtAppTemplateNames() {
        Closure filter = { it[0..-4] }
        def resources = []
        def resolver = new PathMatchingResourcePatternResolver()
        String templatesDirPath = "${basedir}/src/templates/extjscrud/scaffolding"
        def templatesDir = new FileSystemResource(templatesDirPath)
        if (templatesDir.exists()) {
            try {
                println("picking ${basedir}/src/templates/extjscrud/scaffolding/app.js")
                resources = resolver.getResources("file:$templatesDirPath/app.js").filename.collect(filter)
            }
            catch (e) {
                LOG.info("Error while loading views from grails-app scaffolding folder", e)
            }
        } else {
            try {
                def pluginsHomeTemplates = resolver.getResources("file:${extjscrudPluginDir}/src/templates/extjscrud/scaffolding/app.js").filename.collect(filter)
                resources.addAll(pluginsHomeTemplates)
            }
            catch (e) {
                // ignore
                LOG.debug("Error locating templates from pluginsHome: ${e.message}", e)
            }
        }
        return resources

    }

    def getExtViewportTemplateNames() {
        Closure filter = { it[0..-4] }
        def resources = []
        def resolver = new PathMatchingResourcePatternResolver()
        String templatesDirPath = "${basedir}/src/templates/extjscrud/scaffolding"
        def templatesDir = new FileSystemResource(templatesDirPath)
        if (templatesDir.exists()) {
            try {
                println("picking ${basedir}/src/templates/extjscrud/scaffolding/Viewport.js")
                resources = resolver.getResources("file:$templatesDirPath/Viewport.js").filename.collect(filter)
            }
            catch (e) {
                LOG.info("Error while loading views from grails-app scaffolding folder", e)
            }
        } else {
            try {
                def pluginsHomeTemplates = resolver.getResources("file:${extjscrudPluginDir}/src/templates/extjscrud/scaffolding/Viewport.js").filename.collect(filter)
                resources.addAll(pluginsHomeTemplates)
            }
            catch (e) {
                // ignore
                LOG.debug("Error locating templates from pluginsHome: ${e.message}", e)
            }
        }
        return resources

    }


    def generateMessageProperties(GrailsDomainClass domainClass, String destdir) {

        println('in generateMessageProperties');
        println('destdir = '+destdir)
        Assert.hasText destdir, "Argument [destdir] not specified"

        if (domainClass) {
            def fullName = domainClass.fullName
            def pkg = ""
            def pos = fullName.lastIndexOf('.')
            if (pos != -1) {
                // Package name with trailing '.'
                pkg = fullName[0..pos]
            }

            def msgFile = new File("${destdir}/grails-app/i18n/messages.properties")
            println('picking msgFile '+msgFile);

            //domainClass.

            def excludedProps = Event.allEvents.toList() << 'version' << 'id' << 'dateCreated' << 'lastUpdated'
            def persistentPropNames = domainClass.persistentProperties*.name
            def props = domainClass.properties.findAll { persistentPropNames.contains(it.name) && !excludedProps.contains(it.name) && !it.isAssociation() }

            def property
            props.each { p ->
                property = "\n"
                property += domainClass.getPropertyName() + ".field." + p.name + "=" + p.naturalName
                msgFile.append(property)
            }

            //msgFile.close()
            LOG.info("Message properties appended at ${msgFile}")

        }

    }

    /*
    def getTemplateNames() {
        Closure filter = { it[0..-4] }
        if (resourceLoader && application?.isWarDeployed()) {
            def resolver = new PathMatchingResourcePatternResolver(resourceLoader)
            try {
                println("picking /WEB-INF/templates/scaffolding/extjscrud/*.js")
                return resolver.getResources("/WEB-INF/templates/scaffolding/extjscrud/*.js").filename.collect(filter)
            }
            catch (e) {
                return []
            }
        }

        def resources = []
        def resolver = new PathMatchingResourcePatternResolver()
        String templatesDirPath = "${basedir}/src/templates/extjscrud/scaffolding"
        def templatesDir = new FileSystemResource(templatesDirPath)
        if (templatesDir.exists()) {
            try {
                println("picking ${basedir}/src/templates/extjscrud/scaffolding/*.js")
                resources = resolver.getResources("file:$templatesDirPath/*.js").filename.collect(filter)
            }
            catch (e) {
                LOG.info("Error while loading views from grails-app scaffolding folder", e)
            }
        }

        try {
            println("picking ${extuiPluginDir}/src/templates/extjscrud/scaffolding/*.js");
            def pluginsHomeTemplates = resolver.getResources("file:${extjscrudPluginDir}/src/templates/extjscrud/scaffolding/*.js").filename.collect(filter)
            resources.addAll(pluginsHomeTemplates)
        }
        catch (e) {
            // ignore
            LOG.debug("Error locating templates from pluginsHome: ${e.message}", e)
        }
        return resources
    }     */
}
