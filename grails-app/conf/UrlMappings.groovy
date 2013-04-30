class UrlMappings {
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
   