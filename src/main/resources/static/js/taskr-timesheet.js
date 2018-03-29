$(document).ready(function() {
    $("section.day-view h4 a").not(".none").click(function(){
        $("#day-details h1").text("Loading");

        var dayview = $(this).parents("section.day-view");
        var day = dayview.find("h1").text();
        var month = $(".month-view h1:first").text();
        var currentDay = month + " " + day;
        var detail = dayview.attr("date");

        $("#timesheet-loading").show();
        $("#day-details table tbody").html("");

        $.getJSON("/timesheet/dayIssues/" + detail, function( data ) {
            $("#timesheet-loading").hide();
            $("#day-details h1").text(currentDay);
            data.forEach(function(d) {
                var $tr = $("<tr></tr>");
                $tr.addClass("day-view");

                var $td = $("<td><a></a></td>");
                var $a = $td.find("a");
                $a.text(d.name);
                $a.attr("href", "/tasks/" + d.issueId);
                $tr.html($td);

                $("#day-details table tbody").append($tr);
            });
        });
    });
});