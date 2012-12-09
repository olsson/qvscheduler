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
					<tr>
						<td>Judy</td>
						<td>
							<span class="label label-cashier">9&mdash;6</span>
						</td>
						<td>
							<span class="label label-cashier">9&mdash;6</span>
						</td>
						<td>
							<span class="label label-cashier">9&mdash;6</span>
						</td>
						<td>
							<a class="btn btn-small" href="#"><i class="icon-plus"></i></a>
						</td>
						<td>
							<span class="label label-cashier">9&mdash;6</span>
						</td>
						<td>
							<span class="label label-cashier">9&mdash;6</span>
						</td>
						<td>
							<a class="btn btn-small" href="#"><i class="icon-plus"></i></a>
						</td>
					</tr>
					<tr>
						<td>Tim</td>
						<td>
							<span class="label label-pharmacist">7&mdash;5</span>
						</td>
						<td>
							<span class="label label-pharmacist">7&mdash;5</span>
						</td>
						<td>
							<span class="label label-pharmacist">7&mdash;5</span>
						</td>
						<td>
							<span class="label label-pharmacist">7&mdash;5</span>
						</td>
						<td>
							<span class="label label-pharmacist">7&mdash;5</span>
						</td>
						<td>
							<a class="btn btn-small" href="#"><i class="icon-plus"></i></a>
						</td>
						<td>
							<a class="btn btn-small" href="#"><i class="icon-plus"></i></a>
						</td>
					</tr>
				</tbody>
			</table>
			
			<div class="row">
				<div class="span2 offset10">
					<div class="well">
						<#list positions as pos> 
							<span class="label" style="background-color:${pos.color}">${pos.name}</span>
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
		
	</body>

	<script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
	<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.9.2/jquery-ui.min.js"></script>
	<script src="static/js/scheduler.js"></script>
	<script src="static/js/bootstrap.js"></script>	
</html>


