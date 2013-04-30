//
// This script is executed by Grails after plugin was installed to project.
// This script is a Gant script so you can use all special variables provided
// by Gant (such as 'baseDir' which points on project base dir). You can
// use 'ant' to access a global instance of AntBuilder
//
// For example you can create directory under project tree:
//
//    ant.mkdir(dir:"${basedir}/grails-app/jobs")
//

//ant.copy(file:"${extjscrudPluginDir}/grails-app/views/index.gsp", tofile:"${basedir}/grails-app/views/index.gsp")

//ant.copy(file:"${extjscrudPluginDir}/grails-app/controllers/extjscrud/MessageController.groovy", tofile:"${basedir}/grails-app/controllers/extjscrud/MessageController.groovy")

//ant.copy(file:"${extjscrudPluginDir}/grails-app/views/message/index.gsp", tofile:"${basedir}/grails-app/views/message/index.gsp")

//ant.move(file:"${extjscrudPluginDir}/grails-app/conf/ExtjsCrudConfig.groovy", tofile:"${basedir}/grails-app/conf/Config.groovy")

//ant.move(file:"${extjscrudPluginDir}/grails-app/conf/ExtjsCrudUrlMappings.groovy", tofile:"${basedir}/grails-app/conf/UrlMappings.groovy")

//ant.copy(file:"${extjscrudPluginDir}/grails-app/i18n/messages.properties", tofile:"${basedir}/grails-app/i18n/messages.properties")



//includeTargets << grailsScript("_GrailsCreateArtifacts")

target('installscript': "The description of the script goes here!") {
    // TODO: Implement script here
  //  jawrConfig()
    editConfig()
    rewriteUrlMappings()
    createMsgProps()
    createDanishMsgProps()
}

/*

target('jawrConfig': "Creating Message Controller and index view") {

    depends(checkVersion, parseArguments, packageApp)

    try {
        def type = "Controller"
        def name = "Message"

        println('Before calling createArtifact');
        createArtifact(name: name, suffix: type, type: type, path: "grails-app/controllers")
        println('After calling createArtifact');
        def viewsDir = "${basedir}/grails-app/views/${propertyName}"
        println('Before making dir ' + viewsDir);
        ant.mkdir(dir: viewsDir)
        println('After creating viewsdor');
        println("extjscrud directory is " + "${extjscrudPluginDir}");
        ant.copy(file: "${extjscrudPluginDir}/grails-app/views/index.gsp", tofile: "${viewsDir}/index.gsp")
        event("CreatedFile", [viewsDir])

        //createUnitTest(name: name, suffix: type, superClass: "ControllerUnitTestCase")


    }
    catch (Exception e) {
        logError("Error running jawrConfig", e)
        exit(1)
    }

}       */

def editConfig() {

    def configObj = new ConfigSlurper().parse(new File("${basedir}/grails-app/conf/Config.groovy").toURL())

    Properties props = configObj.toProperties()

    if (props.get("jawr.js.mapping") == null) {


        def jawrConfig = '''jawr {
            js {
                // Specific mapping to disable resource handling by plugin.
                mapping = '/jawr/'
                bundle {
                    lib {
                        // Bundle id is used in views.
                        id = '/i18n/messages.js'
                        // Tell which messages need to localized in Javascript.
                        mappings = 'messages:grails-app.i18n.messages'
                    }
                }
            }
            locale {
                // Define resolver so ?lang= Grails functionality works with controllers.
                resolver = 'net.jawr.web.resource.bundle.locale.SpringLocaleResolver'
            }
        }
    '''

        def jawrConfigObj = new ConfigSlurper().parse(jawrConfig)

        new File("${basedir}/grails-app/conf/Config.groovy").withWriterAppend { writer ->
            jawrConfigObj.writeTo(writer)

        }
    }


}

def rewriteUrlMappings() {

    def urlMappings = '''class UrlMappings {
        static mappings = {

            "/$controller/$action?/$id?"{
                constraints {
                    // apply constraints here
                }
            }

            "/api/$controller/$id?"(parseRequest: true)  {
                action = [ GET: 'readJson', PUT: 'updateJson', DELETE: 'deleteJson', POST: 'createJson']
            }

            "/" {
                controller = 'message'
                action = 'index'
            }
            "500"(view:'/error')
        }
    }
   '''

    new File("${basedir}/grails-app/conf/UrlMappings.groovy").withWriter { writer ->
        writer.write(urlMappings)
    }


}

def createMsgProps() {

    Properties msgProps = new java.util.Properties()

    def msgFile = new File("${basedir}/grails-app/i18n/messages.properties")

    msgFile.withInputStream { stream ->
        msgProps.load(stream)
    }

    def defaultProperties = new java.util.Properties()

    defaultProperties.setProperty("js.button.saveBtn","Save")
    defaultProperties.setProperty("js.button.cancelBtn","Cancel")
    defaultProperties.setProperty("js.toolbar.addTbr","Add")
    defaultProperties.setProperty("js.toolbar.editTbr","Edit")
    defaultProperties.setProperty("js.toolbar.deleteTbr","Delete")
    defaultProperties.setProperty("js.select.error","Error")
    defaultProperties.setProperty("js.select.errorDesc","Select the record before clicking the button")
    defaultProperties.setProperty("js.deleteFn.confirmText","Sure??")
    defaultProperties.setProperty("js.deleteFn.confirmDesc", "Are you sure to delete? This operation cannot be undone.")
    defaultProperties.setProperty("js.deleteFn.yesBtn","Yes")
    defaultProperties.setProperty("js.deleteFn.noBtn","No")
    defaultProperties.setProperty("js.domain.read.errorMsg","{0} not found with id {1}")
    defaultProperties.setProperty("js.domain.create.errorMsg","Error occured while creating the {0}")
    defaultProperties.setProperty("js.domain.update.errorMsg","Error occured while updating the {0}")
    defaultProperties.setProperty("js.domain.delete.errorMsg","{0} {1} could not be deleted")
    defaultProperties.setProperty("js.domain.lock.errorMsg","Another user has updated this {0} while you were editing")
    defaultProperties.setProperty("js.domain.nofound.errorMsg","{0} not found with id {1}")
    defaultProperties.setProperty("js.domain.created","{0} Successfully created")
    defaultProperties.setProperty("js.domain.updated","{0} Successfully updated")
    defaultProperties.setProperty("js.domain.deleted","{0} Successfully deleted")
    defaultProperties.setProperty("js.domain.read","{0} Successfully loaded")
    defaultProperties.setProperty("js.pagination.beforepageMsg","Page")
    defaultProperties.setProperty("js.pagination.afterpageMsg","of {0}")
    defaultProperties.setProperty("js.pagination.displayMsg","Displaying {0} - {1} of {2}")
    defaultProperties.setProperty("js.pagination.emptyMsg","No data to display")
    defaultProperties.setProperty("js.list.page.title","All {0}")
    defaultProperties.setProperty("js.add.window.title","Add {0}")
    defaultProperties.setProperty("js.edit.window.title","Edit {0}")


    // check for default properties in the messages.properties file.
    if(msgProps.get("js.button.saveBtn") == null){

        defaultProperties.each {  prop ->
            msgFile.append("\n"+prop.key+"="+prop.value)
        }

    }
    
    //ant.copy(file: "${extjscrudPluginDir}/grails-app/i18n/messages.properties", tofile: "${basedir}/grails-app/i18n/messages.properties")
}

def createDanishMsgProps() {

    Properties msgProps = new java.util.Properties()

    def msgFile = new File("${basedir}/grails-app/i18n/messages_da.properties")

    msgFile.withInputStream { stream ->
        msgProps.load(stream)
    }

    def defaultProperties = new java.util.Properties()

    defaultProperties.setProperty("js.button.saveBtn","Gem")
    defaultProperties.setProperty("js.button.cancelBtn","Fortryd")
    defaultProperties.setProperty("js.toolbar.addTbr","Opret")
    defaultProperties.setProperty("js.toolbar.editTbr","Ret")
    defaultProperties.setProperty("js.toolbar.deleteTbr","Slet")
    defaultProperties.setProperty("js.select.error","Fejl")
    defaultProperties.setProperty("js.select.errorDesc","Vælg posten før du trykker på knappen")
    defaultProperties.setProperty("js.deleteFn.confirmText","Er du sikker??")
    defaultProperties.setProperty("js.deleteFn.confirmDesc", "Er du sikker på at du ønsker at slette? Denne handling kan ikke fortrydes.")
    defaultProperties.setProperty("js.deleteFn.yesBtn","Ja")
    defaultProperties.setProperty("js.deleteFn.noBtn","Nej")
    defaultProperties.setProperty("js.domain.read.errorMsg","{0} ikke fundet med id {1}")
    defaultProperties.setProperty("js.domain.create.errorMsg","Der opstod en fejl under oprettelse af {0}")
    defaultProperties.setProperty("js.domain.update.errorMsg","Der opstod en fejl under opdatering af {0}")
    defaultProperties.setProperty("js.domain.delete.errorMsg","{0} {1} kunne ikke slettes")
    defaultProperties.setProperty("js.domain.lock.errorMsg","En anden bruger har opdateret posten {0} mens du rettede denne")
    defaultProperties.setProperty("js.domain.nofound.errorMsg","{0} ikke fundet med id {1}")
    defaultProperties.setProperty("js.domain.created","{0} Blev oprettet uden fejl")
    defaultProperties.setProperty("js.domain.updated","{0} Blev opdateret")
    defaultProperties.setProperty("js.domain.deleted","{0} Blev slettet")
    defaultProperties.setProperty("js.domain.read","{0} Blev hentet uden fejl")
    defaultProperties.setProperty("js.pagination.beforepageMsg","Side")
    defaultProperties.setProperty("js.pagination.afterpageMsg","af {0}")
    defaultProperties.setProperty("js.pagination.displayMsg","Viser {0} - {1} af {2}")
    defaultProperties.setProperty("js.pagination.emptyMsg","Ingen data")
    defaultProperties.setProperty("js.list.page.title","Alle {0}")
    defaultProperties.setProperty("js.add.window.title","Opret {0}")
    defaultProperties.setProperty("js.edit.window.title","Ret {0}")


    // check for default properties in the messages.properties file.
    if(msgProps.get("js.button.saveBtn") == null){

        defaultProperties.each {  prop ->
            msgFile.append("\n"+prop.key+"="+prop.value)
        }

    }

    //ant.copy(file: "${extjscrudPluginDir}/grails-app/i18n/messages.properties", tofile: "${basedir}/grails-app/i18n/messages.properties")
}

installscript()







