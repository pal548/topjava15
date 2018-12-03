const ajaxUrl = "ajax/admin/users/";
let datatableApi;

function getAjaxUrl() {
    return ajaxUrl;
}

/*function userEnabledOnChange(id) {
    console.log($(this).attr("id"));
    console.log(id);
}*/

// $(document).ready(function () {
$(function () {

    // console.log($(".enabled-chb"));


    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "name"
            },
            {
                "data": "email"
            },
            {
                "data": "roles"
            },
            {
                "data": "enabled"
            },
            {
                "data": "registered"
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
                "asc"
            ]
        ]
    });
    makeEditable();

    $(".enabled-chb").change(function () {
        let id = $(this).attr("data-id");
        let checked = this.checked;
        $.ajax({
            type: "POST",
            url: ajaxUrl + id + "/set-enabled",
            data: "checked=" + checked
        }).done(function () {
            updateTable();
            successNoty("Updated");
        })

    });
});