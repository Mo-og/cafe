$(document).ready(function () {
    $.fn.dataTable.moment('D-MM-YYYY H:m:s');

    $('#myTable').DataTable({
        "bLengthChange": false,
        "pageLength": 15,
    });
});
