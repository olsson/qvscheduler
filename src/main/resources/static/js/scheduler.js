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
		
		$('.labels .label').bind("click", Sched.deleteConfirmShift);
	};
	
	Sched.deleteConfirmShift = function() {
		var id = $(this).data('id');
		if (confirm('Are you sure you want to delete this shift?')) {
			$.ajax('shifts?id=' + id, 
				{ type: 'DELETE',
				  success: function() {
					  location.reload();
				  }
				}
			);
		}
	};
	
	Sched.addNewShift = function() {
		var $popover = $(this).closest('.popover-content'),
			position = $popover.find('.position .value').text(),
			startTime = $popover.find('.startTime input').val(),
			endTime = $popover.find('.endTime input').val(),
			$days = $popover.find('.days input'),
			json = {}, daysSum = "", map = [1, 2, 4, 8, 16, 32, 64];
		
		if (position === 'Position') {
			alert('Select a position');
			return;
		}
		if (startTime === endTime) {
			alert('Start and end times should be different');
			return;
		}
		
		daysSum += "[";
		for (var i=0; i<$days.size(); i++) {
			if ($days.eq(i).attr('checked') == 'checked') {
				daysSum += '"' + (i + 1) + '",'; 
			}
		}
		daysSum = daysSum.substring(0, daysSum.length-1);
		daysSum += "]";
		
		json = '{ "employee" : "' + $(this).data('employee') + '", ' +
				' "day" : "' + $(this).data('day') + '", ' +
				' "position" : "' + position + '", ' +
				' "startTime" : "' + startTime + '", ' +
				' "endTime" : "' + endTime + '", ' +
				' "days" : ' + daysSum + ' }';
		
		$.ajax('shifts', 
				{ data: json, 
				  type: 'POST',
				  dataType: 'json',
				  contentType: 'application/json',
				  success: function() {
					  $('table .icon-plus[data-visible="1"]').click();
					  location.reload();
				  }
				}
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
		
		if ((thisVisible == undefined || thisVisible == 0) && visiblePopovers >= 1) {
			return;
		}
		
		$form.find('.btn-primary').attr('data-employee', $elem.data('employee'));
		$form.find('.btn-primary').attr('data-day', $elem.data('day'));
		$form.find('.days input').eq($elem.data('pos')).attr('checked', 'checked');
		
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
			$('.popover-content .timepicker-default').each(function(idx, item){
				$(item).timepicker();
			});
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