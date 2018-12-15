const mealsAjaxUrl = "ajax/profile/meals/";
const mealsAjaxUrlFiltered = mealsAjaxUrl + "filter";

const dateFormat = "DD.MM.YYYY";
const timeFormat = "HH:mm";
const dateTimeFormat = dateFormat + " " + timeFormat;

function updateFilteredTable() {
    $.ajax({
        type: "GET",
        url: mealsAjaxUrlFiltered,
        data: getFilterFormParamString(),
    }).done(updateTableByData);
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealsAjaxUrl, updateTableByData);
}

function transformDateTimeStrIfValid(inputStr, inputFormat, outputFormat) {
    // the parser is very forgiving, does not fail on non-format symbols
    if (moment(inputStr, inputFormat).isValid()) {
        return moment(inputStr, inputFormat).format(outputFormat);
    } else {
        return "";
    }
}
function getISODateStr(inputStr) {
    return transformDateTimeStrIfValid(inputStr, dateFormat, "YYYY-MM-DD");
}

function getISOTimeStr(inputStr) {
    return transformDateTimeStrIfValid(inputStr, timeFormat, "HH:mm:ss");
}

function getISODateTimeStrFromDisplayStr(inputStr) {
    return transformDateTimeStrIfValid(inputStr, dateTimeFormat, "YYYY-MM-DD[T]HH:mm:ss");
}

function getFilterFormParamString() {
    return "startDate=" + getISODateStr($("#startDate").val()) +
        "&endDate=" + getISODateStr($("#endDate").val()) +
        "&startTime=" + getISOTimeStr($("#startTime").val()) +
        "&endTime="  + getISOTimeStr($("#endTime").val());
}

function getSerializedFormData() {
    return "id=" + $("#id").val() +
        "&dateTime=" + getISODateTimeStrFromDisplayStr($("#dateTime").val()) +
        "&description=" + $("#description").val() +
        "&calories=" + $("#calories").val();
}

$(function () {
    const lang = "ru";

    $.datetimepicker.setLocale(lang);
    $.datetimepicker.setDateFormatter('moment');

    $("#startDate").datetimepicker({
        dayOfWeekStart: 1,
        timepicker: false,
        format: dateFormat,
        lang: lang
    });

    $("#endDate").datetimepicker({
        dayOfWeekStart: 1,
        timepicker: false,
        format: dateFormat,
        lang: lang
    });

    $("#startTime").datetimepicker({
        datepicker: false,
        format: timeFormat,
    });

    $("#endTime").datetimepicker({
        datepicker: false,
        format: timeFormat,
    });

    $("#dateTime").datetimepicker({
        dayOfWeekStart: 1,
        format: dateTimeFormat,
        lang: lang,
    });

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
                            return moment(data).format(dateTimeFormat);
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
        updateTable: updateFilteredTable,
        getSerializedFormData: getSerializedFormData,
        fillEditFormBeforeUpdate : function (data) {
            $.each(data, function (key, value) {
                const val = key == "dateTime" ? moment(value).format(dateTimeFormat) : value;
                form.find("input[name='" + key + "']").val(val);
            });
        }
    });
});