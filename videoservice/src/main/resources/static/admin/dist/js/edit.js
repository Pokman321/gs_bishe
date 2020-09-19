var blogEditor;
// Tags Input
$('#blogTags').tagsInput({
    width: '100%',
    height: '38px',
    defaultText: '文章标签'
});

//Initialize Select2 Elements
$('.select2').select2()

$(function () {

    new AjaxUpload('#uploadCoverImage', {
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
                $("#cameraCoverImage").attr("src", r.data);
                $("#cameraCoverImage").attr("style", "width: 128px;height: 128px;display:block;");
                swal("成功上传",{
                    icon:"success"
                });
                return false;
            } else {
                alert("error");
            }
        }
    });
});

$('#getresultButton').click(function () {
    var cameraUrl = $('#cameraUrl').val();
    $.ajax({
        type:"post",
        url:"/myvideo/upload/ajaxtest",
        data:{},
        success: function (result) {
            if(result!=null && result.resultCode==200){
                swal("可以成功连接",{
                    icon:"success"
                });
            }
            else{
                swal("连接出现错误",{
                    icon:"error"
                })
            }

        }
    })
});

$('#connectionTest').click(function () {
    var cameraUrl = $('#cameraUrl').val();
    if(cameraUrl===""){
        swal("请先输入相机路径",{
            icon:"error"
        });
        return false;
    }
    $.ajax({
        type:"get",
        url:"/admin/cameras/connect/?cameraUrl="+cameraUrl,
        data:{},
        dataType:"json",
        success: function (result) {

            if (result != null && result.resultCode === 200) {
                swal("执行完成",{
                    icon:"success"
                });
                $("#img").attr("src", result.data);
                $("#img").attr("style", "width: 400px;display:block;");
                return false;
            } else {
                swal("连接相机失败",{
                    icon:"error"
                });
            }
        }
    })

});

$('#confirmButton').click(function () {
    var cameraName = $('#cameraName').val();
    var cameraUrl = $('#cameraUrl').val();
    var cameraCategoryId = $('#cameraCategoryId').val();

    if (isNull(cameraName)) {
        swal("请输入相机名称", {
            icon: "error",
        });
        return;
    }
    if (!validLength(cameraName, 150)) {
        swal("相机名称过长", {
            icon: "error",
        });
        return;
    }
    if (isNull(cameraUrl)){
        swal("请输入相机路径", {
            icon: "error",
        });
        return;
    }
    if (!validLength(cameraUrl, 150)) {
        swal("路径过长", {
            icon: "error",
        });
        return;
    }
    if (isNull(cameraCategoryId)) {
        swal("请选择相机地点分类", {
            icon: "error",
        });
        return;
    }
    $('#articleModal').modal('show');
});

$('#saveButton').click(function () {
    var cameraId = $('#cameraId').val();
    var cameraName = $('#cameraName').val();
    var cameraUrl = $('#cameraUrl').val();
    var cameraCategoryId = $('#cameraCategoryId').val();
    var cameraCoverImage = $('#cameraCoverImage')[0].src;
    var cameraEnable = $("input[name='cameraEnable']:checked").val();
    if (isNull(cameraCoverImage) || cameraCoverImage.indexOf('img-upload') != -1) {
        swal("封面图片不能为空", {
            icon: "error",
        });
        return;
    }
    var url = '/admin/cameras/save';
    var swlMessage = '保存成功';
    var data = {
        "cameraName": cameraName, "cameraUrl": cameraUrl, "cameraCategoryId": cameraCategoryId,
        "cameraCoverImage": cameraCoverImage, "cameraEnable": cameraEnable
    };
    if (cameraId > 0) {
        url = '/admin/cameras/update';
        swlMessage = '修改成功';
        data = {
            "cameraId": cameraId,
            "cameraName": cameraName,
            "cameraUrl": cameraUrl,
            "cameraCategoryId": cameraCategoryId,
            "cameraCoverImage": cameraCoverImage,
            "cameraEnable": cameraEnable
        };
    }
    console.log(data);
    $.ajax({
        type: 'POST',//方法类型
        url: url,
        data: data,
        success: function (result) {
            if (result.resultCode == 200) {
                $('#articleModal').modal('hide');
                swal({
                    title: swlMessage,
                    type: 'success',
                    showCancelButton: false,
                    confirmButtonColor: '#3085d6',
                    confirmButtonText: '返回相机列表',
                    confirmButtonClass: 'btn btn-success',
                    buttonsStyling: false
                }).then(function () {
                    window.location.href = "/admin/cameras";
                })
            }
            else {
                $('#articleModal').modal('hide');
                swal(result.message, {
                    icon: "error",
                });
            }
            ;
        },
        error: function () {
            swal("操作失败", {
                icon: "error",
            });
        }
    });
});

$('#cancelButton').click(function () {
    window.location.href = "/admin/cameras";
});

/**
 * 默认封面
 */
$('#testAsCoverImage').click(function () {
    swal("进来了", {
        icon: "success"
    });
    $("#cameraCoverImage").attr("src", $('#img')[0].src);
    $("#cameraCoverImage").attr("style", "width:160px ;height: 120px;display:block;");
});
