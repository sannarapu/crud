package crud



import grails.converters.JSON
import org.grails.plugins.extui.util.ParamFilter

class StudentController {

static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def readJson = {
        def studentInstance = Student.get(params.id)
        if ( studentInstance ) {

                    if ( studentInstance.hasErrors() ) {
                        def json = [success: false, totalCount: Student.count(), message: message(code:"js.domain.read.errorMsg", args:[ Student,params.id]), data: studentInstance.errors]
                        render(text: json as JSON, contentType: 'application/json', encoding: "UTF-8")
                    } else {
                        def json = [success: true, totalCount: Student.count(), message: message(code:"js.domain.read", args:[ Student]), data: studentInstance]
                        render(text: json as JSON, contentType: 'application/json', encoding: "UTF-8")
                    }

        } else {
            /*params.max = Math.min(params.max ? params.int('max') : 10, 100)
            def studentInstanceList = Student.list(params)
            withFormat {
                json {
                    def json = [success: true, totalCount: Student.count(), message: 'Successfully loaded', data: studentInstanceList]
                    render json as JSON
                }
            } */

            // return the list of users.
            // check if the filter parameters exist
            def filterParams = params.findAll {key, value -> key =~ /filter(.*)/ }
            if (filterParams.size() == 0) {
                def studentInstanceList
                // check for sort parameters
                if (params.sort) {
                    def query = {
                        order(JSON.parse(params.sort).property[0], JSON.parse(params.sort).direction[0]?.toString().toLowerCase())
                    }
                    studentInstanceList = Student.createCriteria().list(offset: params.start, max: params.limit, query)
                } else {
                    studentInstanceList = Student.list([offset: params.start, max: params.limit])
                }


                        def json = [success: true, totalCount: Student.count(), message: message(code:"js.domain.read", args:[ Student]), data: studentInstanceList]
                        render(text: json as JSON, contentType: 'application/json', encoding: "UTF-8")

            } else {
                List<ParamFilter> filters = ParamFilter.decodeFilterParameters(params)
                def countQuery = ParamFilter.getFilterQueryCount(filters)
                def count = Student.createCriteria().count(countQuery)
                def query = ParamFilter.getFilterQuery(filters,params.sort)
                def studentInstanceList = Student.createCriteria().list(max: params.limit, offset: params.start, query)


                        def json = [success: true, totalCount: count, message: message(code:"js.domain.read", args:[ Student]), data: studentInstanceList]
                        render(text: json as JSON, contentType: 'application/json', encoding: "UTF-8")


            }

        }
    }

    def createJson = {
        def studentInstance = new Student()
        if (studentInstance) {
            studentInstance.properties = request.JSON.data
            studentInstance.validate()
            studentInstance.save(flush: true)

                    if (!studentInstance.hasErrors()) {
                        def json = [success: true, totalCount: Student.count(), message: message(code:"js.domain.created", args:[ Student]), data: studentInstance]
                        render(text: json as JSON, contentType: 'application/json', encoding: "UTF-8")
                    } else {
                        def json = [success: false, totalCount: Student.count(), message: message(code:"js.domain.create.errorMsg", args:[ Student ]), data: studentInstance.errors]
                        render(text: json as JSON, contentType: 'application/json', encoding: "UTF-8")
                    }

        }
    }

    def updateJson = {
        def studentInstance = Student.get(params?.id)
        if (studentInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (studentInstance.version > version) {
                    def json = [success: false, totalCount: Student.count(), message: message(code:"js.domain.lock.errorMsg", args:[ Student ]), data: studentInstance]
                    render(text: json as JSON, contentType: 'application/json', encoding: "UTF-8")
                }
            }
            studentInstance.properties = request.JSON.data
            studentInstance.validate()
            if (!studentInstance.hasErrors() && studentInstance.save(flush: true)) {
                def json = [success: true, totalCount: Student.count(), message: message(code:"js.domain.updated", args:[ Student]), data: studentInstance]
                render(text: json as JSON, contentType: 'application/json', encoding: "UTF-8")
            }
            else {
                def json = [success: false, totalCount: Student.count(), message: message(code:"js.domain.update.errorMsg", args:[ Student ]), data: studentInstance]
                render(text: json as JSON, contentType: 'application/json', encoding: "UTF-8")
            }
        }
        else {
            def json = [success: false, totalCount: Student.count(), message: message(code:"js.domain.nofound.errorMsg", args:[ Student, params.id ]), data: studentInstance]
            render(text: json as JSON, contentType: 'application/json', encoding: "UTF-8")
        }
    }

    def deleteJson = {
        def studentInstance = Student.get(params.id)
        def json = [success: true, totalCount: Student.count(), message: message(code:"js.domain.deleted", args:[ Student])]
        if (studentInstance) {
            try {
                studentInstance.delete(flush: true)
                render(text: json as JSON, contentType: 'application/json', encoding: "UTF-8")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                json = [success: false, totalCount: Student.count(), message: message(code:"js.domain.delete.errorMsg", args:[ Student ])]
                render(text: json as JSON, contentType: 'application/json', encoding: "UTF-8")
            }
        } else {
            json = [success: false, totalCount: Student.count(), message: message(code:"js.domain.nofound.errorMsg", args:[ Student, params.id ])]
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
