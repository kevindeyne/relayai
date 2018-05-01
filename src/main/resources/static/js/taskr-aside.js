var latestClickedIssue = null;
var asideLoaded = "#aside-issue-list";
var issueLoaded = new Object();
		
$(document).ready(function() {
	initScrollbar("aside");
	
	//task list logic on taskboard
	$("aside section").click(function (){
		var issueId = $(this).attr("issue-id");
		localStorage.setItem("current-issue", issueId);
		var sectionId = '#' + $(this).find(".progress").attr("id");
		if(latestClickedIssue !== sectionId){
			$("section svg").remove();
			
			latestClickedIssue = sectionId;
			
			var progressLine = buildProgressBar(sectionId);
			var issueLoadingAnimationDone = false;
			var issueLoadingDone = false;
			
			$.getJSON("../issue/" + issueId, function(data) {
				issueLoadingDone = true;
				issueLoaded = data;
				handleIssueLoaded(latestClickedIssue, progressLine, issueLoadingAnimationDone, issueLoadingDone, issueId);
			});
			
			progressLine.animate(1, function() {
				issueLoadingAnimationDone = true;				
				handleIssueLoaded(latestClickedIssue, progressLine, issueLoadingAnimationDone, issueLoadingDone, issueId);
			});
		}
	});
	
	//select active issue from localstorage
	if(localStorage.getItem("current-issue") !== undefined && $("aside section.active").length == 0) {
		$("section[issue-id='"+localStorage.getItem("current-issue")+"']").addClass("active");
	}
	
	determineUndecided();
	colorCodeChangeables();
	
	$("#aside-issue, #aside-team, #aside-backlog").click(function(event){
		event.preventDefault();
		$("#aside-issue, #aside-team, #aside-backlog").removeClass("t-active");
		$("#aside-issue-list, #aside-team-list, #aside-backlog-list").hide();
		$("#aside-loader").show();
		$(this).addClass("t-active");
		asideLoaded = "#" + $(this).attr("id") + "-list";
		
		if($(asideLoaded + " section").length === 0){
			var switchUrl = "/issue/list/" + $(this).attr("id").replace("aside-", "");
			$.getJSON(switchUrl, function(data) {
				setTimeout(function(){				
					for (var newIssueIndex in data) {
						var newIssue = data[newIssueIndex];
						if(newIssue.id > maxid){ maxid = newIssue.id; }
						cloneAndPrepend(newIssue);
					 }
					
					$("#aside-loader").hide();
					$(asideLoaded).fadeIn(500);
				}, 500);
			});
		} else {
			$("#aside-loader").hide();
			$(asideLoaded).fadeIn(500);
		}
		
		return false;
	});

	$("#start-tracking-button").click(function(event){
		event.preventDefault();
		
		//set to in progress, assign to you
		var issueId = $("aside section.active").attr("issue-id");
		$.post("/issue/"+issueId+"/progress/IN_PROGRESS", {}, function(response) {colorCodeChangeables();}, "json"); //implicit assign to you
		$("#change-progress").attr("class", "changeable in-progress").text("In progress");
		$("#change-assignee").text("you");
		reorderIssueInAside();
		
		//hide 
		$("#not-in-progress-warning").hide();
		
		return false;
	});
	
	if($("aside section.active").length==0){
		$("aside section:first").click();
	}
	
	positionAsideContent();
});

function cloneAndPrepend(newIssue){
	var newSection = $("aside section:first").clone(true, true);
	newSection.find("i").remove();
	newSection.removeClass("active");
	newSection.attr("issue-id", newIssue.id);
	newSection.attr("importance", newIssue.importance);
	newSection.find("h1").text(newIssue.title);
	newSection.prepend("<i class='fa fa-circle "+ newIssue.clazz +"' aria-hidden='true'></i>");
	newSection.find("p").text(newIssue.descr);
	newSection.find("div").attr("id", "progress-"+newIssue.id+Math.random().toString(36).substring(7));	
	$(asideLoaded).append(newSection);
}

function colorCodeChangeables(){
	if($("#change-progress").text() === "In progress"){
		$("#change-progress").addClass("in-progress");
		$("#not-in-progress-warning").hide();
	} else {
		$("#change-progress").removeClass("in-progress");
		
		if($("#change-assignee").text() === "you" && $("#change-progress").text() !== "New"){
			$("#not-in-progress-warning").show();
		} else {
			$("#not-in-progress-warning").hide();
		}
	}
	
	if($("#change-urgency").text() === "High priority"){
		$("#change-urgency").addClass("high-prio");
	} else {
		$("#change-urgency").removeClass("high-prio");
	}
	
	reorderIssueInAside();
}

function initScrollbar(element){
	var aside = document.querySelector(element);
	SimpleScrollbar.initEl(aside);
}

function buildProgressBar(sectionId){
	return new ProgressBar.Line(sectionId, {
        color: "#2070f7",
        duration: 1500,
        easing: "easeInOut"
    });
}

function handleIssueLoaded(latestClickedIssue, progressLine, issueLoadingAnimationDone, issueLoadingDone, issueId){
	if(issueLoadingAnimationDone && issueLoadingDone){
		var sectionId = "#"+progressLine._container.id;
		if(latestClickedIssue === sectionId){
			$("aside .active").removeClass("active");
			$(sectionId).parent().addClass("active");
			$(sectionId).parent().find("h1 i.new-issue").fadeOut();
			
			loadingContent(issueLoaded);
			
			var taskurl = "/tasks/" + issueId;
			if(issueId != null) {
				window.history.pushState("taskr-currentpage", null, taskurl);
				$("#taskurl").attr("href", taskurl);	
			}			
		}
		
		try {
			progressLine.destroy();
		} catch(err) {
		   //
		}
	}
}

function loadingContent(issue){
	$("#content-userinfo h1").text(issueLoaded.title);
	$("p.content").html(issueLoaded.descr);	
	$("#change-progress").text(issueLoaded.status);
	$("#change-urgency").text(issueLoaded.urgency);
	$("#change-impact").text(issueLoaded.impact);
	$("#issue-creator").text(issueLoaded.creator);
	$("#issue-create-date").text(issueLoaded.createDate);
	$("#change-assignee").text(issueLoaded.assigned);

	$("div.milestone-details-col p:first").html("");
	issueLoaded.versions.forEach(function(item, index) {
	    $("div.milestone-details-col p:first").append("<span data-status=\"Current fix version\" class=\"changeable\" id=\"change-version-"+index+"\">" + item + "</span>");
	    $("div.milestone-details-col p:first span.changeable:last").click(startOverlay);
	});

	determineUndecided();
	colorCodeChangeables();
	
	$("#comment-box").val("");
	$("#comments").html("");
	highestComment = 0;
	
	for (commentIndex in issueLoaded.comments) {
		var comment = issueLoaded.comments[commentIndex];
		
		var newComment = $("<section class='comment'></section>");
		newComment.append("<p><strong><span></span> at <span></span></strong></p>");
		newComment.find("p:first").attr("comment-id", comment.id);
		newComment.find("span:first").text(comment.username);
		newComment.find("span:last").text(comment.date);
		newComment.append("<p></p>");
		newComment.find("p:last").addClass("comment-text").html(comment.text);
		
		if(highestComment <= comment.id){
			highestComment = comment.id;
		}
		
		$("#comments").append(newComment);
	}
	
	positionAsideContent();	
}

function positionAsideContent(){
	$("article#main section#content-main p.content").css("padding-top", Math.ceil($("#content-userinfo").height())+25 + "px");

	if($("#aside-issue-list section.active").attr("issue-id") == -1) {
	    $("#main .ss-content").hide();
	} else {
        $("#main .ss-content").show();
	}
}

function determineUndecided(){
	$("#content-main #comment-box, #content-main .button-group, #comments, #change-urgency, #change-impact, .userinfo-details-col:last, .milestone-details-col").show();
	$("#undecided-info").hide();
	
	if(getActiveClass() === "undetermined-issue"){
		$("#content-main #comment-box, #content-main .button-group, #comments, #change-urgency, #change-impact, .userinfo-details-col:last, .milestone-details-col").hide();
		$("#undecided-info").show();
	}
}

function getActiveClass(){
	if($("aside section.active").length !== 0){
		return $("aside section.active i").attr("class").replace("fa fa-circle ", "");
	}
	return "";
}