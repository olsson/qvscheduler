/**
 * 
 */
package se.mrpeachum.scheduler.controllers;

import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.http.client.utils.URIBuilder;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.node.TextNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestOperations;

import se.mrpeachum.scheduler.exception.RedirectException;

/**
 * @author eolsson
 * 
 */
@Controller
public class ScheduleController {

    private static final String AUTH_CODE = "auth_code";

    private RestOperations googleRestTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleController.class);

    @Autowired
    public ScheduleController(RestOperations googleRestTemplate) {
        this.googleRestTemplate = googleRestTemplate;
    }

    @RequestMapping("/oauth2callback")
    public String redirectLocation(@RequestParam(required = false) String error, @RequestParam(required = false) String code,
            HttpSession session) {
        if (error != null) {
            LOGGER.info("Error is: {}", error);
        }
        else if (code != null) {
            LOGGER.info("Got code: {}", code);
            session.setAttribute(AUTH_CODE, code);
        }
        return "redirect:/";
    }

    @RequestMapping("/")
    public String getMain(ModelMap model, HttpSession session, @RequestParam(value="w", required=false) String yearAndWeek) {
        ObjectNode res = null;
        try {
            res = fetchGoogleInfo(session);
        } catch (RedirectException re) {
            return re.getRedirectUrl();
        }
        TextNode name = (TextNode) res.get("name");
        TextNode email = (TextNode) res.get("email");
        model.addAttribute("email", email.asText());
        model.addAttribute("name", name.asText());
        model.addAttribute("firstDayOfWeek", getFirstDayOfWeek(yearAndWeek));
        model.addAttribute("nextWeek", makeWeekLink(yearAndWeek, 1));
        model.addAttribute("previousWeek", makeWeekLink(yearAndWeek, -1));
        return "main";
    }

    protected final String makeWeekLink(String yearAndWeek, int increment) {
        // TODO Auto-generated method stub
        return null;
    }

    protected final Date getFirstDayOfWeek(String yearAndWeek) {
        final DateFormat format = new SimpleDateFormat("YYYYww");
        final Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(format.parse(yearAndWeek));
        } catch (ParseException e) {
            LOGGER.error("Failed to parse the year and week. Using now. {}", e);
            cal.setTime(new Date());
        }
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        return cal.getTime();
    }
    
    protected ObjectNode fetchGoogleInfo(HttpSession session) throws RedirectException {
        ObjectNode res = null;
        try {
            String code = (String) session.getAttribute(AUTH_CODE);
            if (code != null) {
                ((OAuth2RestTemplate) googleRestTemplate).getOAuth2ClientContext().getAccessTokenRequest().setAuthorizationCode(code);
            }
            res = googleRestTemplate.getForObject("https://www.googleapis.com/oauth2/v1/userinfo", ObjectNode.class);
        } catch (UserRedirectRequiredException redirectException) {
            try {
                URIBuilder builder = new URIBuilder(redirectException.getRedirectUri());
                for (Map.Entry<String, String> entry : redirectException.getRequestParams().entrySet()) {
                    builder.addParameter(entry.getKey(), entry.getValue());
                }
                throw new RedirectException("redirect:" + builder.build());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } 
        }
        return res;
    }

}
