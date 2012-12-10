<!DOCTYPE html>
<html>
	<head>
		<title>Quick View Scheduler</title>
		<link rel="stylesheet" href="static/css/bootstrap.css" type="text/css"/>
		<link rel="stylesheet" href="static/css/scheduler.css" type="text/css"/>
		<link rel="stylesheet" href="styleGenerator" type="text/css"/>
	</head>

	<body>
		
		<div class="container">

			<div class="row title-and-user">
				
				<div class="span6 header-row"><a href="?w=${previousWeek}">&laquo;</a> <span>${firstDayOfWeek?string('MMMM, yyyy')}, Week ${firstDayOfWeek?string('w')}</span> <a href="?w=${nextWeek}">&raquo;</a></div>
				<div class="span2 offset4">
					<div class="btn-group pull-right">
					  <a class="btn btn-primary" href="#">${user.name} (${user.email})</a>
					  <a class="btn btn-primary dropdown-toggle" data-toggle="dropdown" href="#"><span class="caret"></span></a>
					  <ul class="dropdown-menu">
						<li><a href="#employeeBox" role="button" data-toggle="modal"><i class="icon-user"></i> Staff</a></li>
						<li><a href="#positionBox" role="button" data-toggle="modal"><i class="icon-briefcase"></i> Positions</a></li>
						<li class="divider"></li>
						<li><a href="#"><i class="i"></i> Logout</a></li>
					  </ul>
					</div>
				</div>		
			</div>
			
			<table class="table table-striped table-hover">
				<thead>
					<tr>
						<th>&nbsp;</th>
						<#list 0..6 as i>
							<#assign day = (firstDayOfWeek?long) + (i * 86400000) />
							<td>
								${day?number_to_date?string('EEEE d')}
							</td>
						</#list>
					</tr>
				</thead>
				<tbody>
					<#list employees as employee>
						<tr>
							<td>${employee.firstName} ${employee.lastName}</td>
							
							<#list 0..6 as i>
								<td>
									<#assign day = (firstDayOfWeek?long) + (i * 86400000) />
									<#list employee.getShiftsForDay(day?number_to_date) as shift>
										<span class="label label-${shift.position.name?lower_case?replace(' ','')}">
											${shift.startHour}<#if shift.startMinute??>:${shift.startMinute}</#if>&mdash;
											${shift.endHour}<#if shift.endMinute??>:${shift.endMinute}</#if>
										</span>
									</#list>
									<i class="icon-plus" data-employee="${employee.id}" data-day="${day?c}"></i>
								</td>
							</#list>  
						</tr>
					</#list>
					
				</tbody>
			</table>
			
			<div class="row">
				<div class="span2 offset10">
					<div class="well">
						<#list positions as pos> 
							<span class="label label-${pos.name?lower_case?replace(' ','')}">${pos.name}</span>
						</#list>
					</div>
				</div>
			</div>
			
		</div>
		
		<div id="positionBox" class="modal hide fade">
		  <div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h3>Manage Positions</h3>
		  </div>
		  <div class="modal-body">
		  	<form id="positions-form">
				<p>Find your desired color's <a href="http://www.colorpicker.com/" target="_new">HEX code</a> and enter it after a #-sign or type its name.</p>
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
				<input class="span2" type="text" name="color" placeholder="Color HEX code..." />
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
		  	<ul>
		  		<#list employees as emp>
		  			<li><a href="#" class="btn btn-large disabled"><i class="icon-remove"></i><span class="first">${emp.firstName}</span><span class="last">${emp.lastName}</span></a></li>
		  		</#list>
		  	</ul>
		  	<div>
				<input type="text" class="span2" style="margin-bottom:0" name="first" placeholder="First" /> 
				<input type="text" class="span2" style="margin-bottom:0" name="last" placeholder="Last" /> 
				<a class="btn btn-small" href="#"><i class="icon-plus"></i></a>
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
				<div class="btn-group startHour">
				  <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
				    <span class="value">1</span>
				    <span class="caret"></span>
				  </a> 
				  <ul class="dropdown-menu hour" role="menu" aria-labelledby="dropdownMenu">
					<#list 1..12 as hour>
						<li><a tabindex="-1" href="#">${hour}</a></li>
					</#list>
				  </ul>
				</div>
				<div class="btn-group startMinute">
				  <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
				    <span class="value">00</span>
				    <span class="caret"></span>
				  </a> 
				  <ul class="dropdown-menu minute" role="menu" aria-labelledby="dropdownMenu">
					<li><a tabindex="-1" href="#">00</a></li>
					<li><a tabindex="-1" href="#">15</a></li>
					<li><a tabindex="-1" href="#">30</a></li>
					<li><a tabindex="-1" href="#">45</a></li>
				  </ul>
				</div>
			</div>
			
			<div>
				End: 
				<div class="btn-group endHour">
				  <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
				    <span class="value">1</span>
				    <span class="caret"></span>
				  </a>
				  <ul class="dropdown-menu hour" role="menu" aria-labelledby="dropdownMenu">
					<#list 1..12 as hour>
						<li><a tabindex="-1" href="#">${hour}</a></li>
					</#list>
				  </ul>
				</div>
				<div class="btn-group endMinute">
				  <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
				    <span class="value">00</span>
				    <span class="caret"></span>
				  </a>
				  <ul class="dropdown-menu minute" role="menu" aria-labelledby="dropdownMenu">
					<li><a tabindex="-1" href="#">00</a></li>
					<li><a tabindex="-1" href="#">15</a></li>
					<li><a tabindex="-1" href="#">30</a></li>
					<li><a tabindex="-1" href="#">45</a></li>
				  </ul>
				</div>
			</div>
			
			<a href="#" class="btn btn-primary">Add</a>
		</div>
		
	</body>

	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
	<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.9.2/jquery-ui.min.js"></script>
	<script src="static/js/bootstrap.js"></script>	
	<script src="static/js/scheduler.js"></script>
</html>


