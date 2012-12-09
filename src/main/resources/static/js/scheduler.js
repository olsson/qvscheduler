(function($){
	"use strict";
	
	var Sched = {};
	
	Sched.init = function() {
		$('#positionBox .modal-footer .btn-primary').bind("click", Sched.handleNewPositions);
		$('.position-row input').live("keyup", Sched.updatePositionPreview);
		$('.position-row .icon-remove').live("click", Sched.removePositionRow);
		$('#positionBox .btn-small').bind("click", Sched.addNewPositionRow);
		
		$('#employeeBox ul').sortable();
		$('#employeeBox .modal-body li .icon-remove').live("click", Sched.removeEmployee);
		$('#employeeBox .btn-small').bind("click", Sched.addNewEmployee);
		$('#employeeBox .modal-footer .btn-primary').bind("click", Sched.saveEmployees);
	};
	
	Sched.saveEmployees = function() {
		var staff = '[ ', total = $('#employeeBox .modal-body li').size();
		$('#employeeBox .modal-body li').each(function(idx, item){
			staff += '{ "id" : "' + $(item).find('span.id').text() + '", ' +
						'"first" : "' + $(item).find('span.first').text() + '", ' +
						'"last" : "' + $(item).find('span.last').text() + '" }';
			if (idx + 1 != total) {
				staff += ',';
			}
		});
		staff += ' ]';
		$.ajax('staff', 
				{ data: staff, 
				  type: 'PUT',
				  dataType: 'json',
				  contentType: 'application/json',
				  success: function() {
					  $('#employeeBox').find('button.close').click();
					  location.reload();
				  }}
		);
	};
	
	Sched.handleNewPositions = function() {
		var positions = '[ ', total = $('#positions-form .position-row').size();
		$('#positions-form .position-row').each(function(idx, row){
			positions += '{ "id" : "' + $(row).find('input[name="id"]').val() + '", ' +
						   '"name" : "' + $(row).find('input[name="name"]').val() + '", ' +
						   '"color" : "' + $(row).find('input[name="color"]').val() + '" }';
			if (idx + 1 != total) {
				positions += ',';
			}
		});
		positions += ' ]';
		$.ajax('positions', 
				{ data: positions, 
				  type: 'PUT',
				  dataType: 'json',
				  contentType: 'application/json',
				  success: function() {
					  $('#positionBox').find('button.close').click();
					  location.reload();
				  }}
		);
	};
	
	Sched.updatePositionPreview = function() {
		var positionText, positionColor,
			$positionRow = $(this).parent();
		positionText = $positionRow.find('input[name^="name"]').val();
		positionColor = $positionRow.find('input[name^="color"]').val();
		$positionRow.find('.label').text(positionText);
		$positionRow.find('.label').css('background-color', positionColor);
	};
	
	Sched.addNewPositionRow = function() {
		var template = $('#positions-form').parent().find('.template').clone();
		template.removeClass('hide template');
		$(this).parents().eq(1).find('form').append(template);
	};
	
	Sched.removePositionRow = function() {
		var response = window.confirm("All shifts for this position will be permanently deleted. Are you sure?");
		if (response){
			$(this).parent().remove();
		}
	};
	
	Sched.removeEmployee = function() {
		var response = window.confirm("All of this employee's shifts will be permanently deleted. Are you sure?");
		if (response) {
			$(this).closest('li').remove();
		}
	};
	
	Sched.addNewEmployee = function() {
		var template = $('#employeeBox .template').clone(),
			liNode = $('<li></li>'),
			firstNode = $('<span class="first"></span>'),
			lastNode = $('<span class="last"></span>');
		template.removeClass('hide template');
		firstNode.append($(this).parent().find('input[name="first"]').val());
		lastNode.append($(this).parent().find('input[name="last"]').val());
		template.append(firstNode);
		template.append(lastNode);
		liNode.append(template);
		$('#employeeBox ul').append(liNode);
		$(this).parent().find('input').val('');
	};
	
	Sched.init();
	
})(jQuery);