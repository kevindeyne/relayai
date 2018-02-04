var latestupdate = new Date().getTime();

$(function() {	
	var paused = document.hidden; //current state call
	var timeBetweenPullsInMs = 5*1000;
	
	//actual happy flow method being called x time
	function activePull() {
		//This is a pulling event that pulls every second.
		var issueid = $("aside section.active").attr("issue-id");
				
		if(typeof(maxid) === "undefined" || typeof(issueid) === "undefined"){
			$.getJSON("/pull", function(data) {
			  latestupdate = new Date().getTime();
			  //notifications only
			});
		} else {
			var listtype = $("a.t-active").attr("id").replace("aside-", "");
			$.getJSON("/pull/"+issueid+"/"+latestupdate+"/"+highestComment+"/"+listtype, function(data) {
			  latestupdate = new Date().getTime();
			  if (Number.isInteger(data.myIssueCounter)) {
				  $("#my-issues-counter").text(data.myIssueCounter);
				  $("#sprint-counter").text(data.sprintCounter);
				  $("#backlog-counter").text(data.backlogCounter);
			  }	

			  for (var updateIssueIndex in data.updateIssues) {
				var newIssue = data.updateIssues[updateIssueIndex];
				if(newIssue.id > maxid){ maxid = newIssue.id; }
				
				if($("#aside-issue-list section[issue-id='"+newIssue.id+"']").length == 0){
					cloneAndPrepend(newIssue, data.listId);
				} else {
					var issueToEdit = $("#aside-issue-list section[issue-id='"+newIssue.id+"']");
					$(issueToEdit).find("h1 span").text(newIssue.title);
					$(issueToEdit).find("p").text(newIssue.descr);
				}
			  }
			  
			  for (var removeIssueIndex in data.removeIssues) {
				  var removeThisIssue = data.removeIssues[removeIssueIndex];
				  var issueToRemove = $("aside section[issue-id='"+removeThisIssue+"']");
				  var isActive = issueToRemove.hasClass("active");
				  if(isActive) { issueToRemove.next().click(); }
				  issueToRemove.remove();
			  }
			});
		}
		
		//load this data into jStorage for other tabs to read
		$.jStorage.set("pulling-demo", $.jStorage.get("pulling-demo", 0) +1, {TTL: 60000})
	}
		
	function cloneAndPrepend(newIssue, listId){
		var newSection = $("aside section:first").clone(true, true);
		newSection.removeClass("active");
		newSection.attr("issue-id", newIssue.id);
		newSection.attr("importance", newIssue.importance);
		newSection.find("h1").text(newIssue.title);
		newSection.find("h1").append("<i class='fa fa-circle "+ newIssue.clazz +"' aria-hidden='true'></i>");
		newSection.find("p").text(newIssue.descr);
		newSection.find("div").attr("id", "progress-"+newIssue.id);
		
		var lastImportance = $(listId + " section").filter(function() {
		    return $(this).attr("importance") > newIssue.importance;
		}).filter(":last");
		
		if(lastImportance.length == 0){
			$(newSection).insertAfter($(listId + " section:first"));
		} else {
			$(newSection).insertAfter(lastImportance);
		}
	}
	
	//TODO should probably change the name from my demo to an actual key/value
	$.jStorage.listenKeyChange("pulling-demo", function(key, action){
		if(paused && null != $.jStorage.get(key)){
			//Change in the session storage during pause: ' + $.jStorage.get(key));
		}
	});
	
	//function being called recursively, with pause checks
	function loopedCall() {
		if(!document.hidden){ //request current state, this could also do a lot, but lacks wide support
			activePull(); //do the pull here
		} else if(!paused){
			//> Paused (via document hidden), tab not active
			paused = true;
		}
		setTimeout(function () { loopedCall(); }, timeBetweenPullsInMs); //loop back
	}
	
	retrievedValue = $.jStorage.get("pulling-demo", 0) //initial load
	loopedCall(); //initial call

	//jquery-visibility fills in the lacking support with events
	$(document).on({
		'show.visibility': function() {
			paused = false;
		},
		'hide.visibility': function() {
			//> Paused (via visibility), tab not active'
			paused = true;
		}
	});
});