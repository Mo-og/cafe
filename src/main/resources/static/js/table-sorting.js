$(document).ready(function () {
    $('#table').DataTable({
        language: {
            // lengthMenu: "Отображать _MENU_ блюд",
            url: "https://cdn.datatables.net/plug-ins/1.10.21/i18n/Russian.json",
        },
        // "bLengthChange": false,
        "pageLength": 25,
    });
});
