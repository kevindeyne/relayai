var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");
$(document).ajaxSend(function(e, xhr, options) {
    xhr.setRequestHeader(header, token);
    xhr.setRequestHeader("Content-Type", "application/json");
});

//once the back/forward button is popped, actually load the page
$(window).on("popstate", function () {
	location.reload();
});

$(document).ready(function() {
	setTimeout(function(){
		$("#initial-load").fadeOut(100);
	}, 200);
	
	setTimeout(function(){
		location.reload();
	}, 28800000);
	
	$("form").submit(function(e){
        e.preventDefault();
    });
	
	var article = document.querySelector('article');

	$("article").each(function() {
		SimpleScrollbar.initEl($(this)[0]);
	});

	//generic form limiter
	$(".length-limiter").each(function() {
		var linkedWithId = "#" + $(this).attr("linked-with");
		var max = parseInt($(this).attr("max-length"));
						
		$(linkedWithId).data("linked", $(this)).keyup(function() {
			var charsLeft = max-$(this).val().length;
			if(0 <= charsLeft){
				$($(this).data("linked")).text(charsLeft);
			}else{
				$(this).val($(this).val().substring(0, max));
				return false;
			}		
		});
	});
	
	
	$("#send-comment").click(function(){
		var form = new Object();
		form.text = $("#comment-box").val();
		var url = '/comment/currentIssue/'.replace("currentIssue", $("aside section.active").attr("issue-id"));
		$.post(url, JSON.stringify(form), function(response) {
			$("#comment-box").val("");
			
			var newComment = $("<section class='comment'></section>");
			newComment.append("<p><strong><span></span> at <span></span></strong></p>");
			newComment.find("span:first").text(response.username);
			newComment.find("span:last").text(response.date);
			newComment.append("<p></p>");
			newComment.find("p:last").text(response.text);
			$("#comments").append(newComment);
			
		}, 'json');
		
	});
});

function hideScreen(screenId){
	$(screenId).removeClass("visible").hide();
}

function showScreen(screenId){
	$(screenId).removeClass("invisible").show();
}