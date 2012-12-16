/**
 * 
 */
package se.mrpeachum.scheduler.service;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
import se.mrpeachum.scheduler.dao.EmployeeDao;
import se.mrpeachum.scheduler.dao.PositionDao;
import se.mrpeachum.scheduler.dao.ShiftDao;
import se.mrpeachum.scheduler.dao.UserDao;
import se.mrpeachum.scheduler.entities.Employee;
import se.mrpeachum.scheduler.entities.Position;
import se.mrpeachum.scheduler.entities.Shift;
import se.mrpeachum.scheduler.entities.ShiftDto;
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
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private ShiftDao shiftDao;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(HibernateSchedulerService.class);
	
	private static final int ONE_DAY_MS = 86_400_000;
	
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
	public void mergePositions(User user, List<Position> newPositions) {
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

	@Override
	@Transactional(readOnly = false)
	public void mergeEmployees(User user, List<Employee> newEmployees) {
		List<Employee> existingEmployees = employeeDao.getEmployeesForUser(user);
		List<Employee> remove = new ArrayList<>();
		
		for (Employee emp: existingEmployees) {
			if (!newEmployees.contains(emp)) {
				remove.add(emp);
			}
		}
		
		for (Employee emp: remove) {
			employeeDao.delete(emp);
		}
		
		int i = 0;
		for (Employee emp: newEmployees) {
			if (existingEmployees.contains(emp)) {
				for (Employee existing: existingEmployees) {
					if (existing.equals(emp)) {
						existing.setOrder(i++);
						employeeDao.save(existing);
					}
				}
			} else {
				emp.setUser(user);
				emp.setOrder(i++);
				employeeDao.save(emp);
			}
		}
	}

	@Override
	@Transactional
	public List<Employee> getEmployees(User user) {
		List<Employee> employees = employeeDao.getEmployeesForUser(user);
		Collections.sort(employees);
		return employees;
	}

	@Override
	@Transactional(readOnly = false)
	public void saveShift(ShiftDto dto, User user) {
		Employee emp = employeeDao.findById(dto.getEmployee());
		Position pos = positionDao.findByNameAndUser(dto.getPosition(), user);
		
		for (int i=1; i<=6; i++) {
			if (dto.shouldCopyToDayOfWeek(i)) {
				LOGGER.debug("Adding shift to day #{}", i);
				addShiftForDay(dto, user, i, emp, pos);
			}
		}
		employeeDao.save(emp);
	}

	private void addShiftForDay(final ShiftDto dto, final User user, final int dayOfWeek, final Employee emp, final Position pos) {
		Shift shift = new Shift();
		Calendar cal = Calendar.getInstance(Locale.US);
		
		cal.setTime(dto.getStartDate());
		shift.setStartHour(cal.get(Calendar.HOUR));
		shift.setStartMinute(cal.get(Calendar.MINUTE));
		
		cal.setTime(dto.getEndDate());
		shift.setEndHour(cal.get(Calendar.HOUR));
		shift.setEndMinute(cal.get(Calendar.MINUTE));

		shift.setPosition(pos);

		Date day = new Date(dto.getDay() + ((dayOfWeek - 1) * ONE_DAY_MS));
		shift.setDay(day);

		shiftDao.save(shift);
		LOGGER.debug("Added to: {}", day);
		
		emp.getShifts().add(shift);
	}
	

}
