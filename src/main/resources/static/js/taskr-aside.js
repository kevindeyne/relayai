var latestClickedIssue = null;

var issueLoaded = new Object();
		
$(document).ready(function() {
	initScrollbar('aside');
	
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
	
	$('aside .ss-content').animate({ scrollTop: $("section.active").offset().top-10 }, 1); //scroll to active - convenience; for clarity we probably want to keep the active one fixed and inbox moving TODO
});


function initScrollbar(element){
	var aside = document.querySelector(element);
	SimpleScrollbar.initEl(aside);
}

function buildProgressBar(sectionId){
	return new ProgressBar.Line(sectionId, {
        color: '#2070f7',
        duration: 1500,
        easing: 'easeInOut'
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
			
			window.history.pushState('taskr-currentpage', null, '/tasks/' + issueId);
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
	$("p.content").text(issueLoaded.descr);	
	$("#change-progress").text(issueLoaded.status);
	$("#change-urgency").text(issueLoaded.urgency);
	$("#change-impact").text(issueLoaded.impact);
	
	$("#comment-box").val("");
	$("#comments").html("");
	for (commentIndex in issueLoaded.comments) {
		var comment = issueLoaded.comments[commentIndex];
		
		var newComment = $("<section class='comment'></section>");
		newComment.append("<p><strong><span></span> at <span></span></strong></p>");
		newComment.find("span:first").text(comment.username);
		newComment.find("span:last").text(comment.date);
		newComment.append("<p></p>");
		newComment.find("p:last").text(comment.text);
		$("#comments").append(newComment);
	}
	
}