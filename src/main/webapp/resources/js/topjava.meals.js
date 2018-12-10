const mealsAjaxUrl = "ajax/profile/meals/";
const mealsAjaxUrlFiltered = mealsAjaxUrl + "filter";

moment.locale('ru');

function updateFilteredTable() {
    $.ajax({
        type: "GET",
        url: mealsAjaxUrlFiltered,
        data: $("#filter").serialize()
    }).done(updateTableByData);
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealsAjaxUrl, updateTableByData);
}

$(function () {
    makeEditable({
        ajaxUrl: mealsAjaxUrl,
        datatableApi: $("#datatable").DataTable({
            "ajax": {
                url: mealsAjaxUrl,
                dataSrc: ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (data, type, row) {
                        if (type === "display") {
                            return moment(data).format("DD.MM.YYYY HH:mm");
                        }
                        return data;
                    }
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                $(row).attr("data-mealExcess", data.excess);
            }
        }),
        updateTable: updateFilteredTable
    });
});