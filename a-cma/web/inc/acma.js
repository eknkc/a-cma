$(document).ready(function() {
	$('h4').addClass("ui-widget-header").addClass("ui-corner-all");
	$('table.datatable th').addClass("ui-state-hover").addClass("ui-corner-all");
	
	$('table.datatable tr').each(function(i) {
		$(this).find('td').addClass((i % 2) == 0 ? 'alt1' : 'alt2');
	});
	
	$('input[type=submit], button').button();
	
	$('button').filter(function(i) { return $(this).text() == "Continue"; }).button('option', 'icons', {primary: 'ui-icon-arrowthick-1-e'});
	
	$('.ui-state-error').prepend(
		$('<span></span>').addClass('ui-icon').addClass('ui-icon-alert').css('float', 'left').css('margin-right', '5px')
	).addClass('ui-corner-all');
	
	$('#algorithms h3').css('margin', 0).css('padding', 0);
	$('#algorithms').accordion({collapsible: true, active: false});
	
	$('input:checkbox[value=MoveDownField]').change(function() { $('input:checkbox[value=MoveUpField]').prop('checked', this.checked); });
	$('input:checkbox[value=MoveUpField]').change(function() { $('input:checkbox[value=MoveDownField]').prop('checked', this.checked); });
	$('input:checkbox[value=MoveDownMethod]').change(function() { $('input:checkbox[value=MoveUpMethod]').prop('checked', this.checked); });
	$('input:checkbox[value=MoveUpMethod]').change(function() { $('input:checkbox[value=MoveDownMethod]').prop('checked', this.checked); });
	$('input:checkbox[value=MakeClassFinal]').change(function() { $('input:checkbox[value=MakeClassNonFinal]').prop('checked', this.checked); });
	$('input:checkbox[value=MakeClassNonFinal]').change(function() { $('input:checkbox[value=MakeClassFinal]').prop('checked', this.checked); });
	$('input:checkbox[value=DecreaseFieldSecurity]').change(function() { $('input:checkbox[value=IncreaseFieldSecurity]').prop('checked', this.checked); });
	$('input:checkbox[value=IncreaseFieldSecurity]').change(function() { $('input:checkbox[value=DecreaseFieldSecurity]').prop('checked', this.checked); });
	$('input:checkbox[value=DecreaseMethodSecurity]').change(function() { $('input:checkbox[value=IncreaseMethodSecurity]').prop('checked', this.checked); });
	$('input:checkbox[value=IncreaseMethodSecurity]').change(function() { $('input:checkbox[value=DecreaseMethodSecurity]').prop('checked', this.checked); });
	
	$('img[title]').click(function() {
		$('<div></div>').attr("title", $(this).attr('dialogtitle')).text($(this).attr('title')).dialog({modal: true, minHeight: 30, resizable: false});
	});
});