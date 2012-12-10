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
		
		$('table .icon-plus').bind("click", Sched.togglePopover);
		$('.popover-content .dropdown-menu a').live("click", Sched.updateSelectedValue);
		$('.popover-content .btn-primary').live("click", Sched.addNewShift);
	};
	
	Sched.addNewShift = function() {
		var $popover = $(this).closest('.popover-content'),
			position = $popover.find('.position .value').text(),
			startHour = $popover.find('.startHour .value').text(),
			startMinute = $popover.find('.startMinute .value').text(),
			endHour = $popover.find('.endHour .value').text(),
			endMinute = $popover.find('.endMinute .value').text(),
			json = {};
		
		json = '{ "employee" : "' + $(this).data('employee') + '", ' +
				' "day" : "' + $(this).data('day') + '", ' +
				' "position" : "' + position + '", ' +
				' "startHour" : "' + startHour + '", ' +
				' "startMinute" : "' + startMinute + '", ' +
				' "endHour" : "' + endHour + '", ' +
				' "endMinute" : "' + endMinute + '" }';
		
		$.ajax('shift', 
				{ data: json, 
				  type: 'PUT',
				  dataType: 'json',
				  contentType: 'application/json',
				  success: function() {
					  $('table .icon-plus[data-visible="1"]').click();
					  //location.reload();
				  }}
		);
	};
	
	Sched.updateSelectedValue = function() {
		var newText = $(this).text(),
			$updateNode = $(this).closest('.btn-group').find('.value');
		$updateNode.text(newText);
	};
	
	Sched.togglePopover = function() {
		var $elem = $(this), 
			thisVisible = $elem.attr('data-visible'),
			visiblePopovers = $('table .icon-plus[data-visible="1"]').size(),
			$form = $('.shift-template').clone().removeClass('hide shift-template');
		
		console.log(thisVisible);
		console.log(visiblePopovers);
		
		if ((thisVisible == undefined || thisVisible == 0) && visiblePopovers >= 1) {
			return;
		}
		
		$form.find('.btn-primary').attr('data-employee', $elem.data('employee'));
		$form.find('.btn-primary').attr('data-day', $elem.data('day'));
		$elem.popover( { html: true,
						placement: 'bottom',
						content: $form.html(),
						title: 'Add Shift',
						trigger: 'manual' } );
		if (thisVisible === '1') {
			$elem.popover('destroy');
			$elem.attr('data-visible', '0');
		} else {
			$elem.popover('show');
			$elem.attr('data-visible', '1');
		}
	};
	
	Sched.saveEmployees = function() {
		var staff = '[ ', total = $('#employeeBox .modal-body li').size();
		$('#employeeBox .modal-body li').each(function(idx, item){
			staff += '{ "id" : "' + $(item).find('span.id').text() + '", ' +
						'"firstName" : "' + $(item).find('span.first').text() + '", ' +
						'"lastName" : "' + $(item).find('span.last').text() + '" }';
			if (idx + 1 != total) {
				staff += ',';
			}
		});
		staff += ' ]';
		$.ajax('employees', 
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