/**
 * 
 */
package se.mrpeachum.scheduler.controllers;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import se.mrpeachum.scheduler.entities.Position;
import se.mrpeachum.scheduler.entities.User;
import se.mrpeachum.scheduler.exception.RedirectException;
import se.mrpeachum.scheduler.service.SchedulerService;

/**
 * @author eolsson
 *
 */
@Controller
public class StyleController {

	@Autowired
	private SchedulerService schedulerService;
	
	@RequestMapping("/styleGenerator")
	public String getStyle(ModelMap model, HttpServletResponse response, HttpSession session) {
    	User user;
    	try {
    		user = schedulerService.fetchOrSaveUser(session);
    	} catch (RedirectException r) {
    		throw new IllegalStateException("Must be logged in");
    	}
    	
    	List<Position> positions = schedulerService.getPositions(user);
    	model.addAttribute("positions", positions);
    	response.setContentType("text/css");
    	return "css";
	}
	
	
}
