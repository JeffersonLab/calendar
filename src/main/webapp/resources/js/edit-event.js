var MAX_DAY_IN_RANGE = 60;

/* http://paulschreiber.com/blog/2007/03/02/javascript-date-validation/ */
String.prototype.isValidDate = function() {
  var IsoDateRe = new RegExp("^([0-9]{4})-([0-9]{2})-([0-9]{2})$");

  var matches = IsoDateRe.exec(this);
  if (!matches) return false;
  

  var composedDate = new Date(matches[1], (matches[2] - 1), matches[3]);

  return ((composedDate.getMonth() == (matches[2] - 1)) &&
          (composedDate.getDate() == matches[3]) &&
          (composedDate.getFullYear() == matches[1]));

};

function deleteTR() {
    var row = this.parentNode.parentNode;
    var table = row.parentNode.parentNode;
    table.deleteRow(row.rowIndex);
}                

function appendRowForEach(start, end) {
    if(end == null || end == '') {
        end = new Date(start);
    }
    
    if(start > end) {
        alert('start must come before end');
    }
    else {
        var count = 1;
        while(start <= end) {
            if(count > MAX_DAY_IN_RANGE) {
                alert('Date range limited to ' + MAX_DAY_IN_RANGE + ' days');
                break;
            }
            
            appendRowWithDate('occurrences-table', start);
            
            count++;
            start.setDate(start.getDate() + 1);
        }
    }
}

function appendRowWithDate(tableId, day) {
    var table = document.getElementById(tableId);
    var tbody = table.getElementsByTagName("tbody")[0];
    var row = tbody.insertRow(-1);
    var cell1 = row.insertCell(0);
    var cell2 = row.insertCell(1);
    
    var shift = document.getElementById("shift");
    
    var date = formatDate(day) + ' ' + shift.options[shift.selectedIndex].value;

    var input1 = document.createElement("input");

    input1.setAttribute("type", "hidden");
    input1.setAttribute("name", "date");
    input1.setAttribute("value", date); 
      
    var input2 = document.createElement("input");

    input2.setAttribute("type", "button");
    input2.setAttribute("value", "Remove");
    input2.setAttribute("class", "styled-button");
      
    input2.onclick = deleteTR;
      
    cell1.appendChild(input1);
    cell1.appendChild(document.createTextNode(date));
    cell2.appendChild(input2);

    $(".styled-button").button();
}

function validateDayAndShift() {
    var day = document.getElementById("start");
    var toDay = document.getElementById("end");
    var shift = document.getElementById("shift");

    var dayValue = day.value;
    var toDayValue = toDay.value;
    var shiftValue = shift.options[shift.selectedIndex].value;

    if(!dayValue.isValidDate()) {
        alert('Day must be in the format yyyy-mm-dd');
        return false;
    }

    if(toDayValue != null && toDayValue != '' && !toDayValue.isValidDate()) {
        alert('Day (to) must be in the format yyyy-mm-dd');
        return false;
    }

    if(shiftValue !== 'OWL' && shiftValue !== 'DAY' && shiftValue !== 'SWING') {
        alert('You must select a shift');
        return false;
    } 

    return true;
}

function formatDate(date) {
    return $.datepicker.formatDate('yy-mm-dd', date);
}

function parseDate(date) {
    return $.datepicker.parseDate('yy-mm-dd', date);
}

function handleAddOccurrenceClick() {
    if(validateDayAndShift()) {
        var start = document.getElementById("start");
        var end = document.getElementById("end");
        var shift = document.getElementById("shift");
        
        appendRowForEach(parseDate(start.value), parseDate(end.value));
        
        $('.datepicker').datepicker('setDate', null);
        $('.datepicker').datepicker('option', {
            maxDate: null,
            minDate: null
        });
        shift.selectedIndex = 1; // Reset to default of 'DAY'        
    }    
}

function setupDeleteEvents() {
    var table = document.getElementById('occurrences-table');
   
    var rows = table.getElementsByTagName("tbody")[0].getElementsByTagName("tr");
    
    for(var i = 0; i < rows.length; i++) {
        var cells = rows[i].getElementsByTagName("td");
        /*cells[1].childNodes[0].onclick = deleteTR;*/
        
        var inputs = cells[1].getElementsByTagName("input");
        
        if(inputs.length > 0) {
            inputs[0].onclick = deleteTR;
        }        
    }  
}

function setupEvents() {
    setupDeleteEvents();
    
    var button = document.getElementById('add-occurrence-button');
    
    if(button != null) {
        button.onclick = handleAddOccurrenceClick;    
    }
}

/* This is very dangerous because there can only be one! */
window.onload = setupEvents;

$('#field-all-link').click(function() {
    $('#occurrence-fields .select-for-update').prop('checked', true);
});

$('#field-none-link').click(function() {
    $('#occurrence-fields .select-for-update').prop('checked', false);
});

$('#occurrence-all-link').click(function() {
    $('#occurrences-table .select-for-update').prop('checked', true);
});

$('#occurrence-none-link').click(function() {
    $('#occurrences-table .select-for-update').prop('checked', false);
});
 
