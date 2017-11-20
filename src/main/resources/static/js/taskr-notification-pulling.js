$(function() {
	//log stuff	- only for debugging
	function log(text) {
		var dt = new Date();
		var time = dt.getHours() + ":" + dt.getMinutes() + ":" + dt.getSeconds();		
		//console.log(text + ' [' + time + ']');
	}		
	//end log stuff	- only for debugging		
	
	
	//-----------
	
	var paused = document.hidden; //current state call
	var timeBetweenPullsInMs = 5000;
	
	//actual happy flow method being called x time
	function activePull() {
		log('This is a pulling event that pulls every second.');
		//load this data into jStorage for other tabs to read
		$.jStorage.set("pulling-demo", $.jStorage.get("pulling-demo", 0) +1, {TTL: 60000})
	}
	
	//
	$.jStorage.listenKeyChange("pulling-demo", function(key, action){
		if(paused && null != $.jStorage.get(key)){
			log('Change in the session storage during pause: ' + $.jStorage.get(key));
		}
	});
	
	//function being called recursively, with pause checks
	function loopedCall() {
		if(!document.hidden){ //request current state, this could also do a lot, but lacks wide support
			activePull(); //do the pull here
		} else if(!paused){
			log('> Paused (via document hidden), tab not active');
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
			log('> Paused (via visibility), tab not active');
			paused = true;
		}
	});
});