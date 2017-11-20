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
		location.reload();
	}, 28800000);
	
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
});

function hideScreen(screenId){
	$(screenId).removeClass("visible").hide();
}

function showScreen(screenId){
	$(screenId).removeClass("invisible").show();
}