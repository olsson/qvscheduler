/**
 * 
 */
package se.mrpeachum.scheduler.service;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestOperations;

import se.mrpeachum.scheduler.controllers.oauth.OAuth2Handler;
import se.mrpeachum.scheduler.dao.PositionDao;
import se.mrpeachum.scheduler.dao.UserDao;
import se.mrpeachum.scheduler.entities.Position;
import se.mrpeachum.scheduler.entities.PositionList;
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
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HibernateSchedulerService.class);
	
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
	@Transactional(readOnly = false)
	public void mergePositions(User user, PositionList newPositions) {
		LOGGER.debug("New positions class: {}, toString: {}", newPositions.getClass(), newPositions);
		List<Position> existingPositions = positionDao.getPositionsForUser(user);
		List<Position> positionsToRemove = new ArrayList<>();
		// see if any existing are now missing (aka deleted)
		for (Position pos : existingPositions) {
			if (!newPositions.contains(pos)) {
				positionsToRemove.add(pos);
			}
		}
		// delete the removed positions
		for (Position pos : positionsToRemove) {
			positionDao.delete(pos);
		}
		for (Position newPosition: newPositions) {
			if (existingPositions.contains(newPosition)) {
				for (Position existingPos : existingPositions) {
					if (existingPos.getId() == newPosition.getId()) {
						existingPos.setName(newPosition.getName());
						existingPos.setColor(newPosition.getColor());
						positionDao.save(existingPos);
						break;
					}
				}
			} else {
				newPosition.setUser(user);
				positionDao.save(newPosition);
			}
		}
	}
	

}
