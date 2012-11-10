(function($){
	"use strict";
	
	var Sched = {};
	
	Sched.init = function() {
		$('#positionBox .modal-footer .btn-primary').bind("click", Sched.handleNewPositions);
		$('.position-row input').live("keyup", Sched.updatePositionPreview);
		$('#positionBox .btn-small').bind("click", Sched.addNewPositionRow);
		$('#positionBox .template input[name="colors"]').colorpicker();
	};
	
	Sched.handleNewPositions = function() {
		var token = $('body').data('token');
		$('#positions-form').append('<input type="hidden" name="token" value="'+token+'"/>');
		$.post('ajax-positions.php', $('#positions-form').serialize());
	};
	
	Sched.updatePositionPreview = function() {
		var positionText, positionColor,
			$positionRow = $(this).parent();
		positionText = $positionRow.find('input[name^="names"]').val();
		positionColor = $positionRow.find('input[name^="colors"]').val();
		$positionRow.find('.label').text(positionText);
		$positionRow.find('.label').css('background-color', '#'+positionColor);
	};
	
	Sched.addNewPositionRow = function() {
		var template = $('#positions-form .template').clone();
		template.removeClass('hide template');
		$(this).parents().eq(1).find('form').append(template);
		$(this).find('input[name="colors"]').colorpicker();
	};
	
	Sched.init();
	
})(jQuery);