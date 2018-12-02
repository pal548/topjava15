const ajaxUrl = "ajax/profile/meals/";
let datatableApi;

function getAjaxUrl() {
    return ajaxUrl + "?" + $("#filterForm").serialize();
}


// $(document).ready(function () {
$(function () {
    $("#filterFormSubmitBtn").click(function (event) {
        event.preventDefault();
        updateTable();
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