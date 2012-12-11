/**
 * 
 */
package se.mrpeachum.scheduler.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import se.mrpeachum.scheduler.service.SchedulerService;

/**
 * @author eolsson
 *
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class ScheduleControllerTest {

       private ScheduleController target;
       
       @Mock
       private SchedulerService schedulerService;
       
       
       @Before
       public void setup() {
           MockitoAnnotations.initMocks(this);
           target = new ScheduleController(schedulerService);
       }
       
       @Test
       public void shouldCalculateFirstDayOfWeekCurrent() {
           Date test = target.getFirstDayOfWeek("201245");
           Calendar cal = Calendar.getInstance();
           cal.setTime(test);
           assertThat(cal.get(Calendar.DAY_OF_WEEK), is(Calendar.MONDAY));
           assertThat(cal.get(Calendar.DAY_OF_MONTH), is(5));
           assertThat(cal.get(Calendar.MONTH), is(Calendar.NOVEMBER));
       }
    
       @Test
       public void shouldCalculateFirstDayOfWeekPast() {
           Date test = target.getFirstDayOfWeek("201223");
           Calendar cal = Calendar.getInstance();
           cal.setTime(test);
           assertThat(cal.get(Calendar.DAY_OF_WEEK), is(Calendar.MONDAY));
           assertThat(cal.get(Calendar.DAY_OF_MONTH), is(4));
           assertThat(cal.get(Calendar.MONTH), is(Calendar.JUNE));
       }
       
       @Test
       public void shouldCalculateFirstDayOfWeekFuture() {
           Date test = target.getFirstDayOfWeek("201302");
           Calendar cal = Calendar.getInstance();
           cal.setTime(test);
           assertThat(cal.get(Calendar.DAY_OF_WEEK), is(Calendar.MONDAY));
           assertThat(cal.get(Calendar.DAY_OF_MONTH), is(7));
           assertThat(cal.get(Calendar.MONTH), is(Calendar.JANUARY));
           assertThat(cal.get(Calendar.YEAR), is(2013));
       }
       
    
}
