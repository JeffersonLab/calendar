$(function() {
    var dates = $( ".datepicker" ).datepicker({
        dateFormat: 'yy-mm-dd',
        onSelect: function( selectedDate ) {
            var option = this.id === "start" ? "minDate" : "maxDate",
            instance = $( this ).data( "datepicker" ),
            date = $.datepicker.parseDate(
                instance.settings.dateFormat ||
                $.datepicker._defaults.dateFormat,
                selectedDate, instance.settings );
            dates.not( this ).datepicker( "option", option, date );
        }
    });
    $("#end").datepicker("option","minDate", $("#start").val());

    $( ".basicdatepicker" ).datepicker({
        dateFormat: 'yy-mm-dd'
    });
});