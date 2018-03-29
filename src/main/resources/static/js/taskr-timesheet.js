$(document).ready(function() {
    $("section.day-view h4 a").not(".none").click(function(){
        $("#day-details h1").text("Loading");

        var dayview = $(this).parents("section.day-view");
        var day = dayview.find("h1").text();
        var month = $(".month-view h1:first").text();
        var currentDay = month + " " + day;
        var detail = dayview.attr("date");

        $("#timesheet-loading").show();

        $.getJSON("/timesheet/dayIssues/" + detail, function( data ) {
            $("#timesheet-loading").hide();
            $("#day-details h1").text(currentDay);
        });
    });
});