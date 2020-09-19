$(function () {
    new AjaxUpload('#uploadButton', {
        action: '/admin/upload/file',
        name: 'file',
        autoSubmit: true,
        responseType: "json",
        onSubmit: function (file, extension) {
            if (!(extension && /^(jpg|jpeg|png|gif)$/.test(extension.toLowerCase()))) {
                alert('只支持jpg、png、gif格式的文件！');
                return false;
            }
        },
        onComplete: function (file, r) {
            if (r != null && r.resultCode == 200) {
                console.log(r.data);
                $("#img").attr("src", r.data);
                $("#img").attr("style", "width: 100px;display:block;");
                return false;
            } else {
                alert("error");
            }
        }
    });

    $('#domotButton').click(function () {

        document.getElementById("domotButton").setAttribute("disabled", true);
        $.ajax({
            type:"get",
            url:"/myvideo/upload/ajaxtest",
            data:{cameraId:"0"},
            dataType:"json",
            success: function (result) {
                if (result.resultCode==200){
                    isMOT=1;
                    document.getElementById("domotButton").removeAttribute("disabled");
                    $("#videoresult").attr("src", result.data);
                    $("#videoresult").attr("style", "width: 200px;display:block;");
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