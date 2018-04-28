$(document).ready(function() {
	$("#send-invite").click(function(){
        var inviteForm = new Object();
		inviteForm.email = $("#invite-email").val();
        inviteForm.userType = $("#overlay-usertype li.active").attr("data-value");
        if(inviteForm.userType === undefined){ inviteForm.userType = "DEVELOPER"; }
        var visualUserType = $("#change-usertype").text();
        $("#invite-email").val("");

        $("#team-members table").append("<tr><td><strong>"+inviteForm.email+" (pending)</strong></td><td><span class=\"changeable\">"+visualUserType+"</span></td><td><a href=\"#11629\">Remove from project</a></td></tr>");

		$.post('/invite', JSON.stringify(inviteForm), function(response) {

		}, 'json');
	});
});