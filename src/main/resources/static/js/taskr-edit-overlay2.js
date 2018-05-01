$(document).ready(function() {
	makeEveryChangeableSpanClickable();
    cancellingOverlay();
});

function makeEveryChangeableSpanClickable(){
     $("span.changeable").click(startOverlay);
}

function cancellingOverlay(){
	$("#overlay").click(function(e){
		var position = $("#overlay-detail").position();
		if(isClickOutsideOfOverlay(e, position)) {
			hideOverlay();
		}
	});

    $("#button-overlay-cancel").click(function(e){
        hideOverlay();
    });
}

function hideOverlay(e){
	$("#overlay, #overlay-detail").hide();
}

function startOverlay(){
    let $span = $(this);
    let $relevantSubElement = getRelevantSubElement($span);

    setCurrentValueAsTitle($span);
    setVisualPartOfOverlay($span, $relevantSubElement);
    setAutocompletes($relevantSubElement);
    setButtonsAvailable($relevantSubElement);
    showPositionalOverlay($span);
}

function getRelevantSubElement($span){
    let value = $span.attr("id").replace("change-", "#overlay-");
    let hasNumberAsLastChar = !isNaN(value.substr(value.length - 1));

    if(hasNumberAsLastChar){
        let lastIndex = value.lastIndexOf("-");
        value = value.substring(0, lastIndex);
    }

    return $(value);
}

function showPositionalOverlay($span){
    let speed = 100;
    let position = $span.offset();
    let left = position.left - (($span.width()/3) + ($span.width()/2));
    let top = position.top - ($span.height()/3);

    $("#overlay").show().css({opacity: "0"}).animate({opacity: "1"}, speed);
    $("#overlay-detail").show().css({ "left": left, "top": top }).css({opacity: "0"}).animate({opacity: "1"}, speed);
}

function setCurrentValueAsTitle($span){
    $("#overlay-descr").text($span.attr("data-status"));
    $("#overlay-current-val").text($span.text());
}

function setVisualPartOfOverlay($span, $relevantSubElement){
    $("#overlay-detail .overlay-subelement").hide();
    $relevantSubElement.show();

    let isOptions = $relevantSubElement.hasClass("overlay-options");
    if(isOptions) {
        setDefaultSelectionForOptionList($span, $relevantSubElement);
    }
}

function setDefaultSelectionForOptionList($span, $relevantSubElement){
     $("#overlay-detail ul li").removeClass("active");
     $relevantSubElement.find("li:contains('" + $span.text() + "')").addClass("active");
}

function setAutocompletes($relevantSubElement){
    if($relevantSubElement.hasClass("hasAutoCs")){
		$relevantSubElement.find(".auto-cs").each(function() {
            let label = "#" + $(this).attr("id");
            setupAutocomplete(label);
		});

		$relevantSubElement.find("a").click(function(){
		    let text = $(this).text();
            let attr = "#" + $(this).attr("class").replace("-val","-text");
            $(attr).val(text);
		});
    }
}

function setupAutocomplete(label){
    let $autocomplete = $(label);

    if(!$autocomplete.hasClass("autoc-setup")){
        $autocomplete.addClass("autoc-setup");
        new autoComplete({
            selector: label,
            minChars: 1,
            source: function(term, suggest){
                term = term.toLowerCase();
                var choices = autoCLoad(label);
                var matches = [];
                for (i=0; i<choices.length; i++)
                    if (~choices[i].toLowerCase().indexOf(term)) matches.push(choices[i]);
                suggest(matches);
            }
        });
    }
}

function autoCLoad(type) {
    if(labelContains(type, "version")){
        return ['4.0.28.3', '1.0.0', 'Asp']; //TODO preloaded
    }
    return ["Blabla", "Bloop bloop"];
}

function labelContains(label, type){
    return label.indexOf(type) != -1;
}

function setButtonsAvailable($relevantSubElement){
    $(".button-group button").hide();

    let type = $relevantSubElement.attr("id").replace("overlay-", "");
    if(type == "version"){
        $("#button-overlay-add, #button-overlay-remove, #button-overlay-cancel").show();
    } else {
        $("#button-overlay-change, #button-overlay-cancel").show();
    }
}

function isClickOutsideOfOverlay(e, position){
	return (e.pageX < position.left || e.pageX > (position.left + $("#overlay-detail").width()))
	|| (e.pageY < position.top || e.pageY > (position.top + $("#overlay-detail").height()));
}


/*
function changeableFunction() {
    $("#remove-changer-submit").hide();
    var relativeTo = $(this).attr("id");

    if(getActiveClass() === "undetermined-issue" && relativeTo !== "change-workload"){ return; }

    $("#overlay-detail").attr("relative-to", relativeTo);

    $("#overlay-detail p:first span:first").text($(this).attr("data-status"));
    $("#overlay-detail p:first span.changing").text($(this).text());

    $("#overlay-detail .overlay-options, #overlay-detail #overlay-version, #overlay-detail #overlay-assignee").hide();

    //TODO specifically these exceptions added over time as quick prototypes are not good and would work better as their own classes
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

function changeSubmitFunctionality(){
	var issueId = $("aside section.active").attr("issue-id");
	var action = $("#overlay-detail ul:visible li.active").attr("data-value");
	
	if(typeof issueId === "undefined"){
		var ulId = $("#overlay-detail ul:visible").attr("id");
		if(action === "-1"){
			$("#content-new-project").show().css({opacity: "0"}).animate({opacity: "1"}, "fast");
			$("#main").hide();
			$("#content-new-project-main form input:first").focus();
		} else if(ulId === "overlay-project"){
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
			var branch = $(".branch-text").val();
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

function changeRelativeToText(){
	var relativeTo = "#"+$("#overlay-detail").attr("relative-to");
	if(relativeTo.indexOf("version") !== -1){
		var branch = $("#overlay-version .changing:first").val();
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
}*/

