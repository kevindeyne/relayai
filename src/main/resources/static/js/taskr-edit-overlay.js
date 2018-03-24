var autocomplete_version;
var autocomplete_assignee;

$(document).ready(function() {
	
	$("span.changeable").click(changeableFunction);
	
	$("#overlay-detail button.altpath").click(function(){
		if($(this).attr("id") === "remove-changer-submit"){			
			var relativeTo = "#"+$("#overlay-detail").attr("relative-to");
			if($("span.changeable[id*='change-version-']").length > 1){	
				$(relativeTo).remove();
				
				var issueId = $("aside section.active").attr("issue-id");
				var branch = $(".branch-text").text();
				var version = $("#overlay-detail .changing:first").text().replace(" [" + branch + "]", "");
				$.ajax({ url: "/issue/"+issueId+"/version/"+branch+"/"+version, type: "DELETE" });
			}
			
			if ($("span.changeable[id*='change-version-']").length == 1) {
				$("span.changeable[id*='change-version-']").text("No version attributed");
			}
		}
		
		hideOverlay();
	});
	
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

function changeableFunction() {
    $("#remove-changer-submit").hide();
    var relativeTo = $(this).attr("id");

    if(getActiveClass() === "undetermined-issue" && relativeTo !== "change-workload"){ return; }

    $("#overlay-detail").attr("relative-to", relativeTo);

    $("#overlay-detail p:first span:first").text($(this).attr("data-status"));
    $("#overlay-detail p:first span.changing").text($(this).text());

    $("#overlay-detail .overlay-options, #overlay-detail #overlay-version, #overlay-detail #overlay-assignee").hide();

    if(relativeTo.indexOf("version") !== -1){
        setupAutocompleteVersion();
        $("#overlay-version a").off().click(function() { $("#overlay-version input.version-text").val($(this).text()); });
        $("#overlay-version, #remove-changer-submit").show();
        var relativeTo = "#"+$("#overlay-detail").attr("relative-to");
        $(relativeTo).show();
    } else if(relativeTo.indexOf("assignee") !== -1){
        $("#overlay-assignee").show();
    } else {
        if(typeof(relativeTo) !== "undefined") {$(relativeTo.replace("change-", "#overlay-")).show();}

        $("#overlay-detail ul li").removeClass("active");
        $("#overlay-detail ul li:contains('"+$(this).text()+"')").addClass("active");
    }

    var position = $(this).position();
    var left = $("nav").width() + $("aside").width() + position.left;
    var top = position.top;
    if(isNaN(left)) { left = $("nav").width() + position.left }
    if("change-workload" === relativeTo) { top += $(this).parent().parent().position().top; }

    $("#overlay").show().css({opacity: "0"}).animate({opacity: "1"}, "fast");
    $("#overlay-detail").show().css({ "left": left, "top": top }).css({opacity: "0"}).animate({opacity: "1"}, "fast");
}

function setupAutocompleteVersion(){
	if(autocomplete_version === undefined || autocomplete_version === null){
		autocomplete_version = new autoComplete({
		    selector: 'input[class="version-text"]',
		    minChars: 1,
		    source: function(term, suggest){
		        term = term.toLowerCase();
		        var choices = ['4.0.28.3', '1.0.0', 'Asp'];
		        var matches = [];
		        for (i=0; i<choices.length; i++)
		            if (~choices[i].toLowerCase().indexOf(term)) matches.push(choices[i]);
		        suggest(matches);
		    }
		});
	}
}

function changeSubmitFunctionality(){
	var issueId = $("aside section.active").attr("issue-id");
	var action = $("#overlay-detail ul:visible li.active").attr("data-value");
	
	if(typeof issueId === "undefined"){
		if(action === "-1"){
			$("#content-new-project").show().css({opacity: "0"}).animate({opacity: "1"}, "fast");
			$("#main").hide();
			$("#content-new-project-main form input:first").focus();
		} else {
			$.post("/project/changeto/"+action, {}, function(response) {}, "json");
			localStorage.removeItem("current-issue");
			$("#taskurl").attr("href", "/tasks/");
		}		
	} else {	
		var relativeTo = $("#overlay-detail").attr("relative-to").replace("change-", "");

		if(relativeTo.indexOf("-") !== -1) {
		    relativeTo = relativeTo.substring(0, relativeTo.indexOf("-"));
		}
		
		if("version" === relativeTo){
			var version = $(".version-text").val();
			var branch = $(".branch-text").text();	
			action = branch+"/"+version;	
		}
			
		$.post("/issue/"+issueId+"/"+relativeTo+"/"+action, {}, function(response) {}, "json");
	}
		
	afterChange(action);
}

function afterChange(action){
	colorCodeChangeables();
	
	var issueId = $("aside section.active").attr("issue-id");
	
	if(action !== "IN_PROGRESS") {
		$("#tracker span[entity-id='"+issueId+"']").remove();
		if($("#tracker span").length === 0){
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
	if(relativeTo.indexOf("version") !== -1){
		var branch = $("#overlay-version .changing:first").text();
		var version = $("#overlay-detail input.version-text").val();
		$(relativeTo).text(version+" ["+branch+"]");
		
		var noAddsAvailable = $("span.changeable[id*='change-version-']:contains('[+]')").length;
		if(noAddsAvailable > 0){
			$("span.changeable[id*='change-version-']:contains('[+]')").remove();
		}		
		var newOption = $("span.changeable[id*='change-version-']:last").clone(true, true);
		var newId = parseInt($("span.changeable[id*='change-version-']:last").attr('id').replace("change-version-", "")) + 1;
		$(newOption).text("[+]");
		$(newOption).attr("id", "change-version-" + newId);
		$("div.milestone-details-col p:first").append(newOption);
		positionAsideContent();
	} else if(relativeTo.indexOf("assignee") !== -1){
		$(relativeTo).text($("#overlay-detail input.assignee-text").val());
	} else {
		$(relativeTo).text($("#overlay-detail ul:visible li.active").text());	
	}
}

function hideOverlay(e){
	$("#overlay, #overlay-detail").hide();
}