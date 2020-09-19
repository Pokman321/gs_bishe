$(function () {
    // var form = this;
    $('#domotButton').click(function () {
        // swal("fddfsd",{icon:"success"});
        document.getElementById("domotButton").setAttribute("disabled", true);
        $.ajax({
            type:"get",
            url:"/myvideoedit/upload/ajaxresult",
            // data:{cameraId:"0"},
            data:{},
            dataType:"json",
            success: function (result) {
                if (result.resultCode===200){
                    // var formField = this.find("videoForm");
                    var formField = document.getElementById("videoPath");
                    formField.value = result.data.videopath;
                    console.log(formField);
                    // var fieldTagName = formField[0].tagName;
                    // console.log(fieldTagName);
                    // if (fieldTagName==="INPUT"){
                    //     console.log("11111111");
                    //     // if (formField.attr("type")==="text"){
                    //     $("input:text[videoPath]").attr(result.data);
                    //     // }
                    // }
                    // document.getElementById("domotButton").removeAttribute("disabled");

                    // $("#videoPath").attr(result.data);
                    // $("#videoTime").attr(result.data["videoTime"]);
                    swal("执行完成",{
                        icon:"success"
                    });
                }
                else{
                    document.getElementById("domotButton").removeAttribute("disabled");
                    swal("执行失败", {
                        icon: "error"
                    })

                }
            }
        })

    });
});

function search() {
    //标题关键字
    var keyword = $('#keyword').val();
    if (!validLength(keyword, 20)) {
        swal("搜索字段长度过大!", {
            icon: "error",
        });
        return false;
    }
    //数据封装
    var searchData = {keyword: keyword};
    //传入查询条件参数
    $("#jqGrid").jqGrid("setGridParam", {postData: searchData});
    //点击搜索按钮默认都从第一页开始
    $("#jqGrid").jqGrid("setGridParam", {page: 1});
    //提交post并刷新表格
    $("#jqGrid").jqGrid("setGridParam", {url: '/admin/cameras/list'}).trigger("reloadGrid");
}


function deleteBlog(id) {
    // var ids = getSelectedRows();
    if (ids == null) {
        return;
    }
    swal({
        title: "确认弹框",
        text: "确认要删除数据吗?",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    }).then((flag) => {
            if (flag) {
                $.ajax({
                    type: "POST",
                    url: "/myvideo/videos/delete",
                    contentType: "application/json",
                    data: JSON.stringify(id),
                    success: function (r) {
                        if (r.resultCode == 200) {
                            swal("删除成功", {
                                icon: "success",
                            });
                            window.location.reload();
                        } else {
                            swal(r.message, {
                                icon: "error",
                            });
                        }
                    }
                });
            }
        }
    );
}



