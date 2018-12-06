const ajaxUrl = "ajax/profile/meals/";
let filterEnabled;
let datatableApi;

function getAjaxUrl() {
    return ajaxUrl + (filterEnabled ? "filtered?" + $("#filterForm").serialize() : "");
}


// $(document).ready(function () {
$(function () {
    $("#filterForm").on('submit', function (event) {
        event.preventDefault();
        filterEnabled = true;
        updateTable();
    });

    $("#filterForm").on('reset', function (event) {
        filterEnabled = false;
        setTimeout(function () {
            updateTable();
        });
    });

    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime"
            },
            {
                "data": "description",
                "orderable": false
            },
            {
                "data": "calories",
                "orderable": false
            },
            {
                "defaultContent": "Edit",
                "orderable": false
            },
            {
                "defaultContent": "Delete",
                "orderable": false
            }
        ],
        "order": [
            [
                0,
                "desc"
            ]
        ]
    });
    makeEditable();
});