$(document).ready(function() { 
	//create new issue button on taskboard
	$("#new-issue").click(function() {
		$("#main, aside").hide();
		$("#content-new-issue").show().css({opacity: '0'}).animate({opacity: '1'}, "fast");
		$("#main, aside, #content-new-issue").removeClass("visible").removeClass("invisible");
		$("form input:first").focus();
		window.history.pushState('taskr-currentpage', null, '/tasks/create');
	});
				
	//create new issue - submit button
	$("#create-issue-submit").click(function() {
		var issueForm = new Object();
		issueForm.title = $("#title").val();
		issueForm.description = $("#description").val().replace(/(?:\r\n|\r|\n)/g, '<br />');
		
		hideScreen("#content-new-issue");
		$("#loader").show().css({opacity: '0'}).animate({opacity: '1'}, "fast");
		
		var progressLine = new ProgressBar.Line("#savebar", {
	        color: '#2070f7',
	        duration: 1500,
	        easing: 'easeInOut'
	    });
		
		var isAnimationDone = false;
		var isPOSTDone = false;
		
		progressLine.animate(1, function() {
			isAnimationDone = true;
			if(isPOSTDone && isAnimationDone){
				$("#loader").hide();
	    		$("#content-new-issue-main button.altpath").click(); //cancel button functionality	
	    	}
			try {
				progressLine.destroy();
			} catch(err) {
			   //
			}
		});
		
		$.post('/issue/new', JSON.stringify(issueForm), function(response) {		
		    if(response.status === "OK"){
		    	isPOSTDone = true;
		    	
		    	if(isPOSTDone && isAnimationDone){
		    		$("#loader").hide();
		    		$("#content-new-issue-main button.altpath").click(); //cancel button functionality	
		    	}				    	
		    } else {
		    	//did not validate - so show the form again
		    	$("#loader").hide();
		    	$("#content-new-issue").show().css({opacity: '0'}).animate({opacity: '1'}, "fast");
		    	isAnimationDone = false;
				isPOSTDone = false;
		    }
		}, 'json');
	});
	
	//create new issue - cancel button
	$("#content-new-issue-main button.altpath").click(function(e) {				
		hideScreen("#content-new-issue");
		$("#main, aside, #content-new-issue").removeClass("visible").removeClass("invisible");
		$("#main, aside").show().css({opacity: '0'}).animate({opacity: '1'}, "fast");
		window.history.pushState('taskr-currentpage', null, '/tasks/' + localStorage.getItem("current-issue"));
	});
});