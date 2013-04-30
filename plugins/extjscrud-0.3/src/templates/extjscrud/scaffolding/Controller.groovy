<%=packageName ? "package ${packageName}\n\n" : ''%>

import grails.converters.JSON
import org.grails.plugins.extui.util.ParamFilter

class ${className}Controller {

static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def readJson = {
        def ${propertyName} = ${className}.get(params.id)
        if ( ${propertyName} ) {

                    if ( ${propertyName}.hasErrors() ) {
                        def json = [success: false, totalCount: ${className}.count(), message: message(code:"js.domain.read.errorMsg", args:[ ${className},params.id]), data: ${propertyName}.errors]
                        render(text: json as JSON, contentType: 'application/json', encoding: "UTF-8")
                    } else {
                        def json = [success: true, totalCount: ${className}.count(), message: message(code:"js.domain.read", args:[ ${className}]), data: ${propertyName}]
                        render(text: json as JSON, contentType: 'application/json', encoding: "UTF-8")
                    }

        } else {
            /*params.max = Math.min(params.max ? params.int('max') : 10, 100)
            def ${propertyName}List = ${className}.list(params)
            withFormat {
                json {
                    def json = [success: true, totalCount: ${className}.count(), message: 'Successfully loaded', data: ${propertyName}List]
                    render json as JSON
                }
            } */

            // return the list of users.
            // check if the filter parameters exist
            def filterParams = params.findAll {key, value -> key =~ /filter(.*)/ }
            if (filterParams.size() == 0) {
                def ${propertyName}List
                // check for sort parameters
                if (params.sort) {
                    def query = {
                        order(JSON.parse(params.sort).property[0], JSON.parse(params.sort).direction[0]?.toString().toLowerCase())
                    }
                    ${propertyName}List = ${className}.createCriteria().list(offset: params.start, max: params.limit, query)
                } else {
                    ${propertyName}List = ${className}.list([offset: params.start, max: params.limit])
                }


                        def json = [success: true, totalCount: ${className}.count(), message: message(code:"js.domain.read", args:[ ${className}]), data: ${propertyName}List]
                        render(text: json as JSON, contentType: 'application/json', encoding: "UTF-8")

            } else {
                List<ParamFilter> filters = ParamFilter.decodeFilterParameters(params)
                def countQuery = ParamFilter.getFilterQueryCount(filters)
                def count = ${className}.createCriteria().count(countQuery)
                def query = ParamFilter.getFilterQuery(filters,params.sort)
                def ${propertyName}List = ${className}.createCriteria().list(max: params.limit, offset: params.start, query)


                        def json = [success: true, totalCount: count, message: message(code:"js.domain.read", args:[ ${className}]), data: ${propertyName}List]
                        render(text: json as JSON, contentType: 'application/json', encoding: "UTF-8")


            }

        }
    }

    def createJson = {
        def ${propertyName} = new ${className}()
        if (${propertyName}) {
            ${propertyName}.properties = request.JSON.data
            ${propertyName}.validate()
            ${propertyName}.save(flush: true)

                    if (!${propertyName}.hasErrors()) {
                        def json = [success: true, totalCount: ${className}.count(), message: message(code:"js.domain.created", args:[ ${className}]), data: ${propertyName}]
                        render(text: json as JSON, contentType: 'application/json', encoding: "UTF-8")
                    } else {
                        def json = [success: false, totalCount: ${className}.count(), message: message(code:"js.domain.create.errorMsg", args:[ ${className} ]), data: ${propertyName}.errors]
                        render(text: json as JSON, contentType: 'application/json', encoding: "UTF-8")
                    }

        }
    }

    def updateJson = {
        def ${propertyName} = ${className}.get(params?.id)
        if (${propertyName}) {
            if (params.version) {
                def version = params.version.toLong()
                if (${propertyName}.version > version) {
                    def json = [success: false, totalCount: ${className}.count(), message: message(code:"js.domain.lock.errorMsg", args:[ ${className} ]), data: ${propertyName}]
                    render(text: json as JSON, contentType: 'application/json', encoding: "UTF-8")
                }
            }
            ${propertyName}.properties = request.JSON.data
            ${propertyName}.validate()
            if (!${propertyName}.hasErrors() && ${propertyName}.save(flush: true)) {
                def json = [success: true, totalCount: ${className}.count(), message: message(code:"js.domain.updated", args:[ ${className}]), data: ${propertyName}]
                render(text: json as JSON, contentType: 'application/json', encoding: "UTF-8")
            }
            else {
                def json = [success: false, totalCount: ${className}.count(), message: message(code:"js.domain.update.errorMsg", args:[ ${className} ]), data: ${propertyName}]
                render(text: json as JSON, contentType: 'application/json', encoding: "UTF-8")
            }
        }
        else {
            def json = [success: false, totalCount: ${className}.count(), message: message(code:"js.domain.nofound.errorMsg", args:[ ${className}, params.id ]), data: ${propertyName}]
            render(text: json as JSON, contentType: 'application/json', encoding: "UTF-8")
        }
    }

    def deleteJson = {
        def ${propertyName} = ${className}.get(params.id)
        def json = [success: true, totalCount: ${className}.count(), message: message(code:"js.domain.deleted", args:[ ${className}])]
        if (${propertyName}) {
            try {
                ${propertyName}.delete(flush: true)
                render(text: json as JSON, contentType: 'application/json', encoding: "UTF-8")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                json = [success: false, totalCount: ${className}.count(), message: message(code:"js.domain.delete.errorMsg", args:[ ${className} ])]
                render(text: json as JSON, contentType: 'application/json', encoding: "UTF-8")
            }
        } else {
            json = [success: false, totalCount: ${className}.count(), message: message(code:"js.domain.nofound.errorMsg", args:[ ${className}, params.id ])]
            render(text: json as JSON, contentType: 'application/json', encoding: "UTF-8")
        }
    }

    /*
        Sample Template Method
        Domains:-   D1 and D2 are the two domains having one-to-many relationship
        URL Parameters:-  parameter id of Domain D1 is required in the url to filter the records in the Domain D2
        Description:- This Template Method is for Filtering records in Domain2 based on the id value of the Domain1


    def filterRecords = {

        def D1Instance = D1.get(params?.id)
        if (D1Instance) {

            def D2List = D2.findAllByD1(D1Instance, [offset: params.start, max: params.limit])

            withFormat {
                json {
                    def json = [success: true, totalCount: D2List.size(), message: 'Successfully loaded', data: D2List]
                    render json as JSON
                }
            }
        } else {
            withFormat {
                json {
                    def json = [success: false, totalCount: 0, message: 'Data not loaded', data: []]
                    render json as JSON
                }
            }
        }
    }  */

}
