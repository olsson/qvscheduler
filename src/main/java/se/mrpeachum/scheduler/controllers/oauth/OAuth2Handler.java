/**
 * 
 */
package se.mrpeachum.scheduler.controllers.oauth;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author eolsson
 *
 */
@Controller
public class OAuth2Handler {

	public static final String AUTH_CODE = "auth_code";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2Handler.class);
	
	@RequestMapping("/oauth2callback")
    public String redirectLocation(@RequestParam(required = false) String error, @RequestParam(required = false) String code, HttpSession session) {
        if (error != null) {
            LOGGER.info("Error is: {}", error);
        } else if (code != null) {
            LOGGER.info("Got code: {}", code);
            session.setAttribute(AUTH_CODE, code);
        }
        return "redirect:/";
    }

	
}
