var jlab = jlab || {};

jlab.su = function(url) {
    var i = document.createElement('iframe');
    i.style.display = 'none';
    i.onload = function() { i.parentNode.removeChild(i); window.location.href = url; };
    i.src = jlab.logoutUrl;
    document.body.appendChild(i);
};

jlab.login = function(url) {
    var i = document.createElement('iframe');
    i.style.display = 'none';
    i.onload = function() { i.parentNode.removeChild(i); window.location.href = url; };
    i.src = jlab.loginUrl;
    document.body.appendChild(i);
};
$(document).on("click", "#login-link", function () {
    var url = $(this).attr("href");

    jlab.login(url);

    return false;
});
$(document).on("click", "#su-link", function() {
    var url = $(this).attr("href");

    jlab.su(url);

    return false;
});


$(function() {
    $( "#viewDialog" ).dialog({
        autoOpen: false,
        show: "blind",
        hide: "explode",
        modal: true,
        resizable: false
    });

    $( "#viewOpener" ).click(function() {
        $( "#viewDialog" ).dialog( "open" );
        return false;
    });

    $("#viewclose").click(function() {
        $("#viewDialog").dialog("close");
        return false;
    });

    $( "#helpDialog" ).dialog({
        autoOpen: false,
        show: "blind",
        hide: "explode",
        modal: true,
        resizable: false
    });

    $( "#helpOpener" ).click(function() {
        $( "#helpDialog" ).dialog( "open" );

        $('#helpclose').blur();

        return false;
    });

    $("#helpclose").click(function() {
        $("#helpDialog").dialog("close");
        return false;
    });

    $( "#moveDialog" ).dialog({
        autoOpen: false,
        show: "blind",
        hide: "explode",
        modal: true,
        resizable: false
    });

    $( "#move-opener-link" ).click(function() {
        $( "#moveDialog" ).dialog( "open" );
        return false;
    });

    $( "#move-button" ).click(function() {
        var doIt = confirm('Are you sure you want to move?');
        if(doIt) {
            moveDays();
        }
        return false;
    });

    $(".dialog-close-button").click(function() {
        $(this).closest(".dialog").dialog("close");
        return false;
    });

    function moveDays() {
        var fromCalendar = $("#move-from-calendar-select").val(),
            fromStart = $("#startDay").val(),
            days = $("#numberOfDays").val(),
            toCalendar = $("#move-to-calendar-select").val(),
            toStart = $("#moveToDate").val();

        var request = $.ajax({
            url: "move-days",
            type: "POST",
            data: {
                fromCalendar: fromCalendar,
                fromStart: fromStart,
                days: days,
                toCalendar: toCalendar,
                toStart: toStart
            },
            dataType: 'html'
        });

        request.done(function (data) {
            if ($(".status", data).html() !== "Success") {
                alert('Unable to move: ' + $(".reason", data).html());
            } else {
                window.location.reload();
            }
        });
        request.fail(function (xhr, textStatus) {
            alert('Unable to move: Text Status: ' + textStatus + ', Ready State: ' + xhr.readyState + ', HTTP Status Code: ' + xhr.status);
        });
    }
});



$(function() {

    $("#viewselect").change(function() {
        var action = $(this).val();
        $("#viewform").attr("action",  action);

        if(action === 'view-outlook' || action === 'view-week') {
            $(".day-line").addClass("ui-helper-hidden");
            $(".week-line").removeClass("ui-helper-hidden");
            $(".month-line").addClass("ui-helper-hidden");
            $(".year-line").removeClass("ui-helper-hidden");

            $("#yearMonthDay").prop("disabled", true);
            $("#week").prop("disabled", false);
            $("#month").prop("disabled", true);
            $("#year").prop("disabled", false);
        }
        else if(action === 'view-month') {
            $(".day-line").addClass("ui-helper-hidden");
            $(".week-line").addClass("ui-helper-hidden");
            $(".month-line").removeClass("ui-helper-hidden");
            $(".year-line").removeClass("ui-helper-hidden");

            $("#yearMonthDay").prop("disabled", true);
            $("#week").prop("disabled", true);
            $("#month").prop("disabled", false);
            $("#year").prop("disabled", false);
        }
        else if(action === 'view-day') {
            $(".day-line").removeClass("ui-helper-hidden");
            $(".week-line").addClass("ui-helper-hidden");
            $(".month-line").addClass("ui-helper-hidden");
            $(".year-line").addClass("ui-helper-hidden");

            $("#yearMonthDay").prop("disabled", false);
            $("#week").prop("disabled", true);
            $("#month").prop("disabled", true);
            $("#year").prop("disabled", true);
        }
    });

    $("#viewselect").change();
});


$(function() {
    $(".styled-button").button();
    $(".linkify").linkify();
});


    $(function() {
        $("#viewOccurrenceDialog").dialog({
            autoOpen: false,
            show: "blind",
            hide: "explode",
            modal: true,
            resizable: false,
            width: 450
        });

        $(".viewOccurrenceOpener").click(function() {
            $("#viewOccurrenceDialog").dialog("option", "title", "Occurrence " + $(this).data("occurrenceid") + " / Event " + $(this).data("eventid"));

            var oSpan = this;

            $("#oeditlink").click(function() {
                window.location.href="edit-occurrence?occurrenceId=" + $(oSpan).data("occurrenceid");
                return false;
            });

            $("#ocopylink").click(function() {
                window.location.href="copy-occurrence?copyId=" + $(oSpan).data("occurrenceid");
                return false;
            });

            $("#oclose").click(function() {
                $("#viewOccurrenceDialog").dialog("close");
                return false;
            });

            $("#hiddenId").val($(this).data("occurrenceid"));
            $("#oday").text($(this).closest("ul").data("day"));
            $("#oshift").text($(this).closest("ul").data("shift"));
            $("#oeventidviewlink").attr("href", "view-event?eventId=" + $(this).data("eventid"));
            $("#oeventideditlink").attr("href", "edit-event?eventId=" + $(this).data("eventid"));
            $("#otitle").text($(this).data("title"));
            $("#oliaison").text($(this).data("liaison") == null ? ' ' : $(this).data("liaison"));
            $("#odescription").text($(this).attr("title") == null ? ' ' : $(this).attr("title"));
            $("#oremark").text($(this).data("remark") == null ? ' ' : $(this).data("remark"));

            $("#odescription, #oremark").linkify();

            $("#viewOccurrenceDialog").dialog("open");

            $('#oclose').blur();
            $('#oeventideditlink').blur();

            return false;
        });
    });