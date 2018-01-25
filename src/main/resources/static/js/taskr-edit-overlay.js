$(document).ready(function() {
	
	$("span.changeable").click(function(){
		var relativeTo = $(this).attr("id");
				
		if(getActiveClass() === "undetermined-issue" && relativeTo !== "change-workload"){ return; }
		
		$("#overlay-detail").attr("relative-to", relativeTo);
		
		$("#overlay-detail p:first span:first").text($(this).attr("data-status"));
		$("#overlay-detail p:first span.changing").text($(this).text());
		
		$("#overlay-detail .overlay-options").hide();
		$(relativeTo.replace("change-", "#overlay-")).show();
		
		$("#overlay-detail ul li").removeClass("active");
		$("#overlay-detail ul li:contains('"+$(this).text()+"')").addClass("active");
		
		var position = $(this).position();
		var left = $("nav").width() + $("aside").width() + position.left;
		var top = position.top;
		if(isNaN(left)) { left = $("nav").width() + position.left }
		if("change-workload" == relativeTo) { top += $(this).parent().parent().position().top; }
		
		$("#overlay").show().css({opacity: '0'}).animate({opacity: '1'}, "fast");
		$("#overlay-detail").show().css({ "left": left, "top": top }).css({opacity: '0'}).animate({opacity: '1'}, "fast");				
	});
	
	$("#overlay-detail button.altpath").click(function(){hideOverlay();});
	
	$("#overlay-detail ul li").click(function(e){
		$("#overlay-detail ul li").removeClass("hover").removeClass("active");
		$(this).addClass("active");
	});
	
	$("#edit-changer-submit").click(function () {
		changeRelativeToText();
		if(getActiveClass() !== "undetermined-issue"){ changeSubmitFunctionality();	}
		hideOverlay();
	});
	
	$("#overlay").click(function(e){
		var position = $("#overlay-detail").position();
		if(isClickOutsideOfOverlay(e, position)) {
			hideOverlay();	
		}
	});
});

function changeSubmitFunctionality(){
	var issueId = $("aside section.active").attr("issue-id");
	var action = $("#overlay-detail ul:visible li.active").attr("data-value");
	
	if(typeof issueId === "undefined"){
		if(action === "-1"){
			$("#content-new-project").show().css({opacity: '0'}).animate({opacity: '1'}, "fast");
			$("#main").hide();
			$("#content-new-project-main form input:first").focus();
		} else {
			$.post("/project/changeto/"+action, {}, function(response) {}, 'json');
			localStorage.removeItem("current-issue");
			$("#taskurl").attr("href", "/tasks/");
		}		
	} else {	
		var relativeTo = $("#overlay-detail").attr("relative-to").replace("change-", "");
		$.post("/issue/"+issueId+"/"+relativeTo+"/"+action, {}, function(response) {}, 'json');	
	}
		
	afterChange(action);
};

function afterChange(action){
	colorCodeChangeables();
	
	var issueId = $("aside section.active").attr("issue-id");
	
	if(action !== "IN_PROGRESS") {
		$("#tracker span[entity-id='"+issueId+"']").remove();
		if($("#tracker span").length == 0){
			$("#tracker").addClass("invisible");
		}
	}
	
	if(action === "DONE") {
		$("aside section.active").hide();
	} else if(action === "IN_PROGRESS") {
		var inProgressTask = $("<p class='tracking-data'><a><span></span></a></p>");
		$(inProgressTask).find("span").attr("entity-id", issueId).text($("#content-userinfo h1").text());		
		$(inProgressTask).find("a").attr("href", "/tasks/" + issueId);
		$("#tracker").append(inProgressTask);
		$("#tracker").removeClass("invisible").fadeIn(200);
	}
}


function isClickOutsideOfOverlay(e, position){
	return (e.pageX < position.left || e.pageX > (position.left + $("#overlay-detail").width())) 
	|| (e.pageY < position.top || e.pageY > (position.top + $("#overlay-detail").height()));
}

function changeRelativeToText(){
	var relativeTo = "#"+$("#overlay-detail").attr("relative-to");
	$(relativeTo).text($("#overlay-detail ul:visible li.active").text());	
}

function hideOverlay(e){
	$("#overlay, #overlay-detail").hide();
}