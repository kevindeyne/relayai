$(document).ready(function() { 				
	//create new issue - submit button
	$("#create-project-submit").click(function() {
		var projectForm = new Object();
		projectForm.title = $("#title").val();		
		projectForm.newOrExisting = $("input[name='r-new-or-existing']").val();
		projectForm.releaseSchedule = $("input[name='r-release-schedule']").val();
		projectForm.sprintFrequency = $("input[name='r-sprint-frequency']").val();
		projectForm.existingVersion = $("#existing-version").val();
		
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
	
	$("#existing-version, label[for='existing-version']").hide();
	$("#r-existing, #r-new-project").change(function () {
		if($("#r-existing").is(":checked")){
			$("#existing-version, label[for='existing-version']").show();
		} else {
			$("#existing-version, label[for='existing-version']").hide();
		}
	});
	
	$("#r-rs-weekly, #r-rs-biweekly, #r-rs-monthly").change(function () {
		if($("#r-rs-weekly").is(":checked")){
			$("#r-sf-weekly").prop("checked", true);
			$("#r-sf-biweekly").prop("disabled", true).prop("checked", false);
			$("label[for='r-sf-biweekly']").attr("class", "r-option disabled")
		} else {
			$("#r-sf-biweekly").prop("disabled", false);
			$("label[for='r-sf-biweekly']").attr("class", "r-option")
		}
	});
	
	
	$("input[name='r-sprint-frequency']").change(function (){
		if($("input[name='r-sprint-frequency']").filter(":checked").length == 1) {
			$("#create-project-submit").prop("disabled", false);
		}
	});
		
	$("#existing-version").mask('09.09.09');
});