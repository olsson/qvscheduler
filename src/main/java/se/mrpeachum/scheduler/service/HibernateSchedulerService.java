/**
 * 
 */
package se.mrpeachum.scheduler.service;

import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.http.client.utils.URIBuilder;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.node.TextNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.UserRedirectRequiredException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestOperations;

import se.mrpeachum.scheduler.controllers.oauth.OAuth2Handler;
import se.mrpeachum.scheduler.dao.PositionDao;
import se.mrpeachum.scheduler.dao.UserDao;
import se.mrpeachum.scheduler.entities.Position;
import se.mrpeachum.scheduler.entities.User;
import se.mrpeachum.scheduler.exception.RedirectException;

/**
 * @author eolsson
 *
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class HibernateSchedulerService implements SchedulerService {

	@Autowired
	private RestOperations googleRestTemplate;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PositionDao positionDao;
	
	/* (non-Javadoc)
	 * @see se.mrpeachum.scheduler.service.SchedulerService#fetchOrSaveUser(javax.servlet.http.HttpSession)
	 */
	@Override
	@Transactional(readOnly = false)
	public User fetchOrSaveUser(HttpSession session) {
		ObjectNode res = null;
        try {
            String code = (String) session.getAttribute(OAuth2Handler.AUTH_CODE);
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
        } catch (Exception e) {
        	session.invalidate();
        	throw new RedirectException("redirect:");
        }
        TextNode name = (TextNode) res.get("name");
        TextNode email = (TextNode) res.get("email");
        TextNode id = (TextNode) res.get("id");
        
		User user = userDao.findByAccountId(id.asText());
        
        if (user == null) {
        	user = new User();
        	user.setAccountId(id.asText());
        	user.setName(name.asText());
        	user.setEmail(email.asText());
        	userDao.save(user);
        	userDao.flush();
        }
        
        return user;
	}

	@Override
	@Transactional
	public List<Position> getPositions(User user) {
		return positionDao.getPositionsForUser(user);
	}

	@Override
	public void mergePositions(User user, List<Position> newPositions) {
		List<Position> positions = positionDao.getPositionsForUser(user);
		for (Iterator<Position> iter = positions.iterator(); iter.hasNext();) {
			Position pos = iter.next();
			
		}
	}

}
