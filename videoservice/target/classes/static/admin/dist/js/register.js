$(function () {

    new AjaxUpload('#uploadAvatar', {
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
                $("#img").attr("src", r.data);
                $("#img").attr("style", "width: 128px;height: 128px;display:block;");
                // $("#userAvatar").attr
                var formFieldUserAvatar = document.getElementById("userAvatar");
                formFieldUserAvatar.value = r.data;
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

