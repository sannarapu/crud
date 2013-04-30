import net.jawr.web.servlet.JawrRequestHandler;

/**
 * Jawr controller for javascript requests. 
 * It will delegate in the corresponding requestHandler to attend requests. 
 */
class JawrCSSController {
	def defaultAction = "doGet"
	JawrRequestHandler requestHandler;
		
		 
		def doGet = {			
			
			if(null == requestHandler)
				requestHandler = servletContext.getAttribute("CSSJawrRequestHandler");

			// In grails the request is always internally forwarded. This takes account for that. 
			String path = request['javax.servlet.forward.servlet_path'];			
			if(grailsApplication.config.jawr.css.mapping)
				path = path.replaceFirst(grailsApplication.config.jawr.css.mapping, '');
			
			render "";
			requestHandler.processRequest(path,request, response );
			
			return null;
		}
}