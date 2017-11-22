$(document).ready(function() {
	//tasklist, nav hover on taskboard
	$("aside section, nav a, ul.overlay-options li").mouseenter(function(){
		if(!$(this).hasClass("active")){
			$(this).addClass("hover");
		}				
	}).mouseleave(function(){
		$(this).removeClass("hover");
	});
});