var ajaxUrl = "ajax/profile/meals/";
var datatableApi;

function updateTable() {
    $.ajax({
        type: "POST",
        url: ajaxUrl + "filter",
        data: $("#filter").serialize()
    }).done(updateTableByData);
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(ajaxUrl, updateTableByData);
}

$(function () {
    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "createdRow": function (row, data, dataIndex) {
            console.log(data.exceed);
            $(row).addClass(data.exceed ? 'exceeded' : 'normal');
        },
        "columns": [
            {
                "data": "dateTime",
                "render": function (dateTime, type, row) {
                    if (type === "display") {
                        return dateTime.replace('T', ' ').substring(0, 16);
                    }
                    return dateTime;
                }
            },
            {
                "data": "description"
            },
            {
                "data": "calories"
            },
            {
                "defaultContent": "Edit",
                "orderable": false,
                "render": renderEditBtn
            },
            {
                "defaultContent": "Delete",
                "orderable": false,
                "render": renderDeleteBtn
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

$(function () {
    $("#dateTime").datetimepicker({
        format: 'Y-m-d\\TH:i'
    });
    $("#startDate").datetimepicker({
        timepicker: false,
        format: 'Y-m-d'
    });
    $("#endDate").datetimepicker({
        timepicker: false,
        format: 'Y-m-d'
    });
    $("#startTime").datetimepicker({
        datepicker: false,
        format: 'H:i'
    });
    $("#endTime").datetimepicker({
        datepicker: false,
        format: 'H:i'
    });
    updateTable();
});