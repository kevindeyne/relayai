$(document).ready(function() {
	
	$("span.changeable").click(function(){
		var position = $(this).position();
		var relativeTo = $(this).attr("id");
		
		$("#overlay-detail").attr("relative-to", relativeTo);
		
		$("#overlay-detail p:first span:first").text($(this).attr("data-status"));
		$("#overlay-detail p:first span.changing").text($(this).text());
		
		$("#overlay-detail .overlay-options").hide();
		$(relativeTo.replace("change-", "#overlay-")).show();
		
		$("#overlay-detail ul li").removeClass("active");
		$("#overlay-detail ul li:contains('"+$(this).text()+"')").addClass("active");	
		
		var left = $("nav").width() + $("aside").width() + position.left;
		$("#overlay").show().css({opacity: '0'}).animate({opacity: '1'}, "fast");				
		$("#overlay-detail").show().css({ "left": left, "top": position.top }).css({opacity: '0'}).animate({opacity: '1'}, "fast");				
	});
	
	$("#overlay-detail button.altpath").click(function(e){
		$("#overlay, #overlay-detail").hide();	
	});
	
	$("#overlay-detail ul li").click(function(e){
		$("#overlay-detail ul li").removeClass("hover").removeClass("active");
		$(this).addClass("active");
	});
	
	$("#edit-changer-submit").click(function () {
		var relativeTo = "#"+$("#overlay-detail").attr("relative-to");
		$(relativeTo).text($("#overlay-detail ul:visible li.active").text());
		$("#overlay, #overlay-detail").hide();
		//send postJSON
	});
	
	$("#overlay").click(function(e){
		var position = $("#overlay-detail").position();
		if((e.pageX < position.left || e.pageX > (position.left + $("#overlay-detail").width())) 
			|| (e.pageY < position.top || e.pageY > (position.top + $("#overlay-detail").height()))) {
			$("#overlay, #overlay-detail").hide();		
		}
	});
});