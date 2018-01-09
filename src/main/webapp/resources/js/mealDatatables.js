var ajaxUrl = "ajax/profile/meals/";
var datatableApi;

$(function () {
    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "info": true,
        "rowId": "id",
        "createdRow": function(row, data, dataIndex) {
            $(row).css("color", data.exceed ? 'red' : 'green');
        },
        "columns": [
            {
                "data": function (data) {
                    return data.dateTime.replace('T', ' ').substring(0, 16);
                }
            },
            {
                "data": "description"
            },
            {
                "data": "calories"
            },
            {
                "mData": function () {
                    return "<a class='update'><span class='glyphicon glyphicon-pencil' aria-hidden='true'></span></a>";
                },
                "orderable": false
            },
            {
                "mData": function (data) {
                    return "<a class='delete' onclick='deleteRow("+data.id+")'><span class='glyphicon glyphicon-remove' aria-hidden='true'></span></a>";
                },
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
    $("#datetime").datetimepicker({
        format:'Y-m-d H:i'
    });
    $("#startDate").datetimepicker({
        timepicker:false,
        format:'Y-m-d'
    });
    $("#endDate").datetimepicker({
        timepicker:false,
        format:'Y-m-d'
    });
    $("#startTime").datetimepicker({
        datepicker:false,
        format:'H:i'
    });
    $("#endTime").datetimepicker({
        datepicker:false,
        format:'H:i'
    });
    updateTable();
});
function filter() {
    var form = $("#filterForm");
    $.get(ajaxUrl + "filter", form.serialize(), function (data) {
        datatableApi.clear().rows.add(data).draw();
    });
    return false;
}

function resetFilter() {
    $("#filterForm").find(":input").val("");
    updateTable();
}