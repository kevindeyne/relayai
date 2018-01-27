$(document).ready(function() { 				
	//create new issue - submit button
	$("#create-project-submit").click(function() {
		var projectForm = new Object();
		projectForm.title = $("#title").val();
		projectForm.description = "description";
		
		hideScreen("#content-new-project");
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
				window.location.href = "/project"
	    	}
			try {
				progressLine.destroy();
			} catch(err) {
			   //
			}
		});
		
		$.post('/project/new', JSON.stringify(projectForm), function(response) {			
		    if(response.status === "OK"){
		    	isPOSTDone = true;
		    	
		    	if(isPOSTDone && isAnimationDone){
		    		$("#loader").hide();
		    		window.location.href = "/project"		    		
		    	}				    	
		    } else {
		    	//did not validate - so show the form again
		    	$("#loader").hide();
		    	$("#content-new-project").show().css({opacity: '0'}).animate({opacity: '1'}, "fast");
		    	isAnimationDone = false;
				isPOSTDone = false;
		    }
		}, 'json');
	});
	
	//create new issue - cancel button
	$("#content-new-project-main button.altpath").click(function(e) {				
		hideScreen("#content-new-project");
		$("#main, aside, #content-new-project").removeClass("visible").removeClass("invisible");
		$("#main, aside").show().css({opacity: '0'}).animate({opacity: '1'}, "fast");
	});
});