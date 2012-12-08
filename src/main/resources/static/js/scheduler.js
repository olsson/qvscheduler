(function($){
	"use strict";
	
	var Sched = {};
	
	Sched.init = function() {
		$('#positionBox .modal-footer .btn-primary').bind("click", Sched.handleNewPositions);
		$('.position-row input').live("keyup", Sched.updatePositionPreview);
		$('#positionBox .btn-small').bind("click", Sched.addNewPositionRow);
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
					  $(positionBox).find('button.close').click();
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
	
	Sched.init();
	
})(jQuery);