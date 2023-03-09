var currentControlId = 0;
var itemContext = null;
var originalDropId = null;

$.fn.saveOrder = function () {
    var order = $(this).sortable("serialize", {
        key: 'order'
    });
    var calendar = '&calendar=' + $("#calendar-name-banner").attr("data-calendar-id"),
            day = '&yearMonthDay=' + $(this).data('day'),
            shift = '&shift=' + $(this).data('shift'),
            data = order + calendar+ day + shift;

    var request = $.ajax({
        url: "change-order-ajax",
        type: "POST",
        data: data,
        timeout: 5000,
        dataType: 'html',
        context: $(this)
    });
    request.done(function (data) {
        if ($(".status", data).html() !== "Success") {
            alert('Unable to reorder');
            $(this).sortable('cancel');
        }
    });
    request.fail(function (xhr, textStatus) {
        alert('Unable to reorder: Text Status: ' + textStatus + ', Ready State: ' + xhr.readyState + ', HTTP Status Code: ' + xhr.status);
        $(this).sortable('cancel');
    });
    return this;
};

$(function () {
    $(".ui-state-orderable .sortable").sortable({
        revert: true,
        disabled: false,
        connectWith: ".sortable",
        placeholder: "ui-state-highlight",
        cancel: ".editable, .viewOccurrenceOpener",
        beforeStop: function (event, ui) {
            itemContext = ui.item.context;
        },
        receive: function (event, ui) {
            if ($(itemContext).hasClass("spacer")) {
                $(itemContext).attr("id", 'order_spacer:global:' + currentControlId++);
            } else {
                originalDropId = $(itemContext).attr("id");
                $(itemContext).attr("id", originalDropId + ':move');
            }
        },
        update: function (event, ui) {
            // If spacer move from one list to other we need two separate saves!
            // Otherwise, only save list receiving
            if (this === ui.item.parent()[0] || $(ui.item.context).hasClass("spacer")) {
                $(this).saveOrder();

                if (originalDropId != null) {
                    $(itemContext).attr("id", originalDropId);
                    originalDropId = null;
                }
            }
            /*console.dir(event);*/
            /*console.dir(ui);*/
        }
    });
    $(".draggable").draggable({
        connectToSortable: ".sortable",
        helper: "clone",
        revert: "invalid"
    });
    $(".sortable, .draggable").disableSelection();

    /*if($('.username-container').length > 0) {
     $('#change-order-box').click();
     }*/
});

$.fn.removeItem = function () {
    this.closest('li').remove();
    $('.sortable').sortable('refresh');
    return this;
};

$(document).on("click", ".remove", function () {
    var sortable = $(this).closest('ul'); /*Obtain ul reference BEFORE remove li!*/
    $(this).removeItem();
    sortable.saveOrder();
});

/*$('#change-order-box').live('change', function() {
 if($('#change-order-box').attr('checked')) {
 $('.shift-events-cell').addClass('ui-state-orderable').removeClass('ui-state-not-orderable');
 $( ".sortable" ).sortable( "option", "disabled", false );
 }
 else {
 $('.shift-events-cell').addClass('ui-state-not-orderable').removeClass('ui-state-orderable');
 $( ".sortable" ).sortable( "option", "disabled", true );
 }
 })*/