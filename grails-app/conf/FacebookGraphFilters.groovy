import grails.converters.JSON

class FacebookGraphFilters {
	
	// Injected by grails
	def facebookGraphService
	def grailsApplication
	
	def filters = {
		
		// Checking the facebook session
		facebook(controller:"*", action:"*") {
			before = {
				def pair, sig, payload = ""
				//def cookieName = "fbsr_" + grailsApplication.config.facebook.applicationId
                def cookieName = "authStore"
				
				log.debug("Executing facebook filter")
				
				def cookie = request.cookies.find {
					it.name == cookieName
				}
				
				session.facebook = [:] // Without cookie we remove the session data
				if(cookie) {
                    def facebook = [:] // Don't write to session directly as that may cause NullPointerExceptions

                    def cookieValue = JSON.parse(cookie.value.decodeURL());
					session.facebook = facebookGraphService.validateOAuth2Data(cookieValue)
				}
			}
		}
	}
} 
