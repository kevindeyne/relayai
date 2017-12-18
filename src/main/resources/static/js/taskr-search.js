$(document).ready(function() {
	$("#search-textbar").focus();
	
	$("#search-textbar").keypress(function(e) {
	    if(e.which == 13) {
	    	$("#search-submit").click();
	    }
	});
	
	$("#search-submit").click(function(){
		$("#search-issues").attr("class", "invisible");
		$("#search-actions").attr("class", "invisible");					
		$("#search-users").attr("class", "invisible");				
		$("#search-results, #search-in-progress").removeClass("invisible");
		
		var searchForm = new Object();
		
		searchForm.search = $("#search-textbar").val();
		var $usersDiv = $("#search-users").next();
		
		$.post('/search', JSON.stringify(searchForm), function(response) {
			$("#search-in-progress").addClass("invisible");
			$usersDiv.html("");

			var hasIssues = false;
			var hasActions = false;
			var hasUsers = false;
			var hasProjects = false;
			
			
			for (i = 0; i < response.length; i++) {
				var reponseElement = response[i];
				
				var $section = $("<p><a></a></p>");
				var $a = $section.find("a");
				
				$a.text(reponseElement.name);
				var prefix = "/";
				
				var type = reponseElement.type;				
				if(type === "ISSUE"){
					hasIssues = true;
					prefix = prefix + "tasks/";
				} else if(type === "PROJECT"){
					hasProjects = true;
				} else if(type === "ACTION"){
					hasActions = true;
				} else if(type === "USER"){
					hasUsers = true;
				}
				
				$a.attr("href", prefix + reponseElement.linkedId);
				$usersDiv.append($section);
			}
								
			if(hasIssues) {
				$("#search-issues").removeClass("invisible");
			} else if(hasActions) {						
				$("#search-actions").removeClass("invisible");
			} else if(hasUsers) {						
				$("#search-users").removeClass("invisible");
			} else if(hasProjects) {						
				$("#search-projects").removeClass("invisible");
			}
		}, 'json');
	});
});