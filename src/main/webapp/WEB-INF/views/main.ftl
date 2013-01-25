<!DOCTYPE html>
<html>
	<head>
		<title>Quick View Scheduler</title>
		<link rel="stylesheet" href="static/css/bootstrap.css" type="text/css"/>
		<link rel="stylesheet" href="static/css/scheduler.css" type="text/css"/>
		<link rel="stylesheet" href="styleGenerator" type="text/css"/>
	</head>

	<#assign lastDayOfWeek = (firstDayOfWeek?long + (6 * 86400000))?number_to_date />

	<body data-week="${firstDayOfWeek?string('yyyyww')}">
		
		<div class="container">
			<div class="row title-and-user">
				<div class="span8 header-row">
					<a href="?w=${previousWeek}" class="move-week">&laquo;</a> 
					<span>${lastDayOfWeek?string('MMMM, yyyy')}, Week ${lastDayOfWeek?string('w')}</span> 
					<a href="?w=${nextWeek}" class="move-week">&raquo;</a>
				</div>
				<div class="span2 offset2">
					<div class="btn-group pull-right name-drop">
					  <a class="btn btn-primary" href="#">${user.name}</a>
					  <a class="btn btn-primary dropdown-toggle" data-toggle="dropdown" href="#"><span class="caret"></span></a>
					  <ul class="dropdown-menu">
						<li><a href="#employeeBox" role="button" data-toggle="modal"><i class="icon-user"></i> Staff</a></li>
						<li><a href="#positionBox" role="button" data-toggle="modal"><i class="icon-briefcase"></i> Positions</a></li>
					  </ul>
					</div>
				</div>		
			</div>
			
			<#assign shiftsOnPage = 0 />
			
			<table class="table table-striped table-hover">
				<thead>
					<tr>
						<th class="span2">&nbsp;</th>
						<#list 0..6 as i>
							<#assign day = (firstDayOfWeek?long) + (i * 86400000) />
							<th class="span1">
								${day?number_to_date?string('EEE d')}
							</th>
						</#list>
					</tr>
				</thead>
				<tbody>
					<#list employees as employee>
						<tr>
							<td><#if (positions?size > 0)><i class="icon-repeat dim" data-employee="${employee.id}" data-week="${firstDayOfWeek?string('yyyyw')}"></i></#if> ${employee.firstName} ${employee.lastName}</td>
							
							<#list 0..6 as i>
								<td class="labels">
									<#assign day = (firstDayOfWeek?long) + (i * 86400000) />
									<#list employee.getShiftsForDay(day?c) as shift>
										<#assign shiftsOnPage = shiftsOnPage + 1 /> 
										<span class="label label-${shift.position.name?lower_case?replace(' ','')}" data-id="${shift.id?c}" title="Click to delete shift">
											${shift.startHour}<#if shift.startMinute != 0>:${shift.startMinute}</#if>&mdash;${shift.endHour}<#if shift.endMinute != 0>:${shift.endMinute}</#if>
										</span>
									</#list>
									<#if (positions?size > 0)>
										<i class="icon-plus dim" data-employee="${employee.id}" data-day="${firstDayOfWeek?long?c}" data-pos="${i}"></i>
									</#if>
								</td>
							</#list>  
						</tr>
					</#list>
					
				</tbody>
			</table>
			
			<#if employees?size == 0 && positions?size == 0>
				<div class="row">
					<div class="span8 alert alert-block offset2 rounded">
					  <h3>It looks like you're new to Quick View Scheduler</h3>
					  <p>To get started, click on the arrow by your name and add some <strong>Positions</strong> and <strong>Staff</strong>.</p>
					</div>
				</div>
			<#elseif employees?size == 0>
				<div class="row">
					<div class="span8 alert alert-block offset2 rounded">
						<h3>You're doing great</h3>
						<p>Now add some <strong>Staff</strong>.</p>
					</div>
				</div>	
			<#elseif positions?size == 0>
				<div class="row">
					<div class="span8 alert alert-block offset2 rounded">
						<h3>You're doing great</h3>
						<p>Now add some <strong>Positions</strong>.</p>
					</div>
				</div>				
			<#elseif shiftsOnPage == 0>
				<div class="row">
					<div class="span8 alert alert-block offset2 rounded">
						<h3>Add some shifts</h3>
						<p>Click the <i class="icon-plus"></i> icon to add a shift. You can also click the <i class="icon-repeat"></i> icon to copy shifts from a previous week.</p>
					</div>
				</div>				
			</#if>
			
			<#if (positions?? && positions?size > 0)>
				<div class="row">
					<div class="span12">
						<div class="well centered">
							<#list positions as pos> 
								<span class="label label-${pos.name?lower_case?replace(' ','')}">${pos.name}</span>
							</#list>
						</div>
					</div>
				</div>
			</#if>
			
			<div class="row only-print">
				<div class="span12 centered">
					Printed <span class="date"></span>.<br/> &copy; ${.now?string('yyyy')}, Erik Olsson. github.com/mrpeachum
				</div>			
			</div>
			
			<div class="span4 offset4 donate">
			
				<form action="https://www.paypal.com/cgi-bin/webscr" method="post">
					<input type="hidden" name="cmd" value="_s-xclick">
					<input type="hidden" name="encrypted" value="-----BEGIN PKCS7-----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-----END PKCS7-----
					">
					<input type="image" src="https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif" border="0" name="submit" alt="PayPal - The safer, easier way to pay online!">
					<img alt="" border="0" src="https://www.paypalobjects.com/en_US/i/scr/pixel.gif" width="1" height="1">
				</form>
						
			
			</div>
			
		</div>
		
		<div id="positionBox" class="modal hide fade">
		  <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h3>Manage Positions</h3>
		  </div>
		  <div class="modal-body">
		  	<form id="positions-form">
				<p class="hint">Enter a position and a <a href="http://www.w3schools.com/cssref/css_colornames.asp" target="_new">name of a color</a>.</p>
				<#list positions as pos> 
					<div class="position-row">
						<i class="icon-remove"></i>
						<input type="hidden" name="id" value="${pos.id}"/>
						<input type="text" name="name" value="${pos.name}" />
						<input class="span2" type="text" name="color" value="${pos.color}"/>
						<span class="label" style="background-color:${pos.color}">${pos.name}</span>
					</div>
				</#list>
			</form>
			<div class="position-row hide template">
				<i class="icon-remove"></i>
				<input type="hidden" name="id" value=""/>
				<input type="text" name="name" placeholder="New position..." />
				<input class="span2" type="text" name="color" placeholder="Color name..." />
				<span class="label" style=""></span>
			</div>
			<div>
				<a class="btn btn-small" href="#"><i class="icon-plus"></i></a>
			</div>
		  </div>
		  <div class="modal-footer">
			<a href="#" class="btn" data-dismiss="modal">Close</a>
			<a href="#" class="btn btn-primary">Save changes</a>
		  </div>
		</div>
		
		<div id="employeeBox" class="modal hide fade">
		  <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h3>Manage Staff</h3>
		  </div>
		  <div class="modal-body">
		  	<p class="hint">Drag and drop names to change their order.</p>
		  	<ul>
		  		<#list employees as emp>
		  			<li><a href="#" class="btn btn-large disabled"><i class="icon-remove"></i><span class="first">${emp.firstName}</span><span class="last">${emp.lastName}</span></a></li>
		  		</#list>
		  	</ul>
		  	<div>
				<input type="text" class="span2" style="margin-bottom:0" name="first" placeholder="First" /> 
				<input type="text" class="span2" style="margin-bottom:0" name="last" placeholder="Last" /> 
				<a class="btn btn-small" href="#"><i class="icon-plus"></i></a> 
				<span class="hint">&larr; Click to add</span>
			</div>
			<a href="#" class="template hide btn btn-large disabled"><i class="icon-remove"></i></a> 
		  </div> 
		   <div class="modal-footer">
			<a href="#" class="btn" data-dismiss="modal">Close</a>
			<a href="#" class="btn btn-primary">Save changes</a>
		  </div>
		</div>
		
		<div class="shift-template hide">
			<div class="btn-group position">
			  <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
			    <span class="value">Position</span>
			    <span class="caret"></span>
			  </a>
				<ul class="dropdown-menu position" role="menu" aria-labelledby="dropdownMenu">
					<#list positions as pos> 
						<li><a tabindex="-1" href="#">${pos.name}</a></li>
					</#list>
				</ul>
			</div>
			
			<div>
				Start: 
				<div class="input-append bootstrap-timepicker-component startTime">
				    <input type="text" class="timepicker-default input-small">
				    <span class="add-on">
				        <i class="icon-time"></i>
				    </span>
				</div>
			</div>
			
			<div>
				End: 
				<div class="input-append bootstrap-timepicker-component endTime">
				    <input type="text" class="timepicker-default input-small">
				    <span class="add-on">
				        <i class="icon-time"></i>
				    </span>
				</div>
			</div>
			
			<div class="days">
				Days:
				<ul>
					<li><input type="checkbox"/>S</li>
					<li><input type="checkbox"/>M</li>
					<li><input type="checkbox"/>T</li>
					<li><input type="checkbox"/>W</li>
					<li><input type="checkbox"/>T</li>
					<li><input type="checkbox"/>F</li>
					<li><input type="checkbox"/>S</li>
				</ul>
			</div>
			
			<a href="#" class="btn btn-primary shift">Add</a>
		</div>
		
		<div class="copy-popover hide">
			<div class="btn-group copy">
			  <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
			    <span class="value">Copy From Week</span>
			    <span class="caret"></span>
			  </a>
				<ul class="dropdown-menu copy" role="menu" aria-labelledby="dropdownMenu">
					<#list 1..10 as i>
						<#assign day = (firstDayOfWeek?long) - (i * 604800000) />
						<li><a tabindex="-1" href="#">${day?number_to_date?string('MMM d, yyyy (w)')}</a></li>							
					</#list>
				</ul>
			</div>
			<a href="#" class="btn btn-primary copy">Copy</a>
		</div>
		
	</body>

	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
	<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.9.2/jquery-ui.min.js"></script>
	<script src="static/js/bootstrap.js"></script>	
	<script src="static/js/bootstrap-timepicker.js"></script>	
	<script src="static/js/scheduler.js"></script>
</html>


