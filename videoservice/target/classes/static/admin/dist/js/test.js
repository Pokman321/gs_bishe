var isUpdate = 0;
var isMOT = 0;
$(function () {
    // var button = $('#uploadButton');

    new AjaxUpload($('#uploadButton'), {
        action: '/myvideoedit/upload/file',
        name: 'file',
        autoSubmit: true,
        responseType: "json",
        onSubmit: function (file, extension) {
            isUpdate=0;
            if (!(extension && /^(jpg|jpeg|png|gif|mp4)$/.test(extension.toLowerCase()))) {
                alert('只支持jpg、png、gif格式的文件！');
                return false;
            }
            document.getElementById("uploadButton").setAttribute("disabled", true);
            $('#uploadButton').text('上传中');
        },
        onComplete: function (file, r) {
            document.getElementById("uploadButton").removeAttribute("disabled");
            if (r != null && r.resultCode == 200) {
                isUpdate = 1;
                // alert("success");

                // swal(r.getMessage,"请继续操作!","success");
                // swal({title:r.getMessage,type:"success"});
                swal(r.message, {
                    icon: "success",
                });

                    // console.log(r.data);
                $("#img").attr("src", r.data.videoPath);
                $("#img").attr("style", "width: 200px;display:block;");
                var formFieldvideopath = document.getElementById("videoPath");
                formFieldvideopath.value = r.data.videoPath;

                var formFieldvideotime = document.getElementById("videoTime");
                formFieldvideotime.value = r.data.videoTime;

                // document.getElementById(button).removeAttribute("disabled");
                $('#uploadButton').text("上传文件");
                return false;
            } else {
                // alert("error");
                swal(r.getMessage,"请重新操作!","error");
                $('#uploadButton').text("重新上传");
            }
        }
    });

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
                $("#videoCoverImage").attr("src", r.data);
                $("#videoCoverImage").attr("style", "width:160px ;height: 120px;display:block;");
                return false;
            } else {
                alert("error");
            }
        }
    });


    $('#domotButton').click(function () {
        isMOT=0;
        data = {
            "videoId": $('#videoId').val(),
            "videoPath": $('#videoPath').val(),
            "videoTime": $('#videoTime').val()

        };
        // document.getElementById("domotButton").setAttribute("disabled", true);
        $.ajax({
            type:"get",
            // url:"/myvideo/upload/ajaxtest",
            url:"/rabbit/getmessage",
            data:{},
            dataType:"json",
            success: function (result) {
                // document.getElementById("domotButton").removeAttribute("disabled");
                if (result.resultCode == 200) {
                    var rank = result.data;

                    // swal({
                    //     title: 'Are you sure?',
                    //     text: 'You will not be able to recover this imaginary file!',
                    //     type: 'warning',
                    //     showCancelButton: true,
                    //     confirmButtonText: 'Yes, delete it!',
                    //     cancelButtonText: 'No, keep it',
                    // }).then(function(isConfirm) {
                    //     if (isConfirm === true) {
                    //         swal(
                    //             'Deleted!',
                    //             'Your imaginary file has been deleted.',
                    //             'success'
                    //         );
                    //
                    //     } else if (isConfirm === false) {
                    //         swal(
                    //             'Cancelled',
                    //             'Your imaginary file is safe :)',
                    //             'error'
                    //         );
                    //
                    //     } else {
                    //         // Esc, close button or outside click
                    //         // isConfirm is undefined
                    //     }
                    // });

                    // swal(
                    //     {title:"您确定要删除这条信息吗",
                    //         text:"删除后将无法恢复，请谨慎操作！",
                    //         type:"warning",
                    //         showCancelButton:true,
                    //         confirmButtonColor:"#DD6B55",
                    //         confirmButtonText:"是的，我要删除！",
                    //         cancelButtonText:"让我再考虑一下…",
                    //         closeOnConfirm:false,
                    //         closeOnCancel:false
                    //     },
                    //
                    //     function(isConfirm)
                    //     {
                    //         console.log("1111111")
                    //         if(isConfirm)
                    //         {
                    //             swal({title:"删除成功！",
                    //                 text:"您已经永久删除了这条信息。",
                    //                 type:"success"},function(){window.location="www.baidu.com"})
                    //         }
                    //         else{
                    //             swal({title:"已取消",
                    //                 text:"您取消了删除操作！",
                    //                 type:"error"})
                    //         }
                    //     }
                    // )

                    swal({
                        title: "等待人数提示",
                        text: "当前需要等待" + rank,
                        icon : "success",
                        buttons : {
                            button1 : {
                                text : "在线等待",
                                value : true,
                            },
                            button2 : {
                                text : "离线提交",
                                value : false,
                            }
                        },
                    }).then(function(value) {   //这里的value就是按钮的value值，只要对应就可以啦
                        if (value) {
                            $.ajax({
                                type: "post",
                                url: "/rabbit/domot",
                                dataType: "json",
                                data: data,
                                success: function (result2) {
                                    if (result2.resultCode == 200) {
                                        document.getElementById("domotButton").innerText("开始计算")
                                        document.getElementById("domotButton").removeAttribute("disable")
                                        $("#videoresult").attr("src", result2.data.resultPath);
                                        $("#videoresult").attr("style", "width: 200px;display:block;");
                                        var formFieldresultpath = document.getElementById("resultPath");
                                        formFieldresultpath.value = result2.data.resultPath;

                                        var formFieldresulttime = document.getElementById("resultTime");
                                        formFieldresulttime.value = r.data.resultTime;
                                    } else {
                                        swal("计算错误", {icon: "error"});
                                    }
                                }
                            })
                        } else {
                            window.location.href = "http://www.baidu.com"
                        }
                    });

                    // swal({
                    //         title: "等待人数提示",
                    //         text: "当前需要等待" + rank,
                    //         icon: "warning",
                    //         showCancelButton: true,
                    //         closeOnConfirm: false,
                    //         buttons:true,
                    //         dangerMode:true
                    //         // confirmButtonText: "确 认",
                    //         // showCancelButton: true,
                    //         // confirmButtonColor: "#DD6B55",
                    //         // confirmButtonText: "确认",
                    //         // cancelButtonText: "取消",
                    //         // closeOnConfirm: false,
                    //         // closeOnCancel: false
                    //     }).then(function (isConfirm) {
                    //         console.log("1111")
                    //         // document.getElementById("domotButton").innerText("正在处理")
                    //         $.ajax({
                    //             type: "post",
                    //             url: "/rabbit/domot",
                    //             dataType: "json",
                    //             data: data,
                    //             success: function (result2) {
                    //                 if (result2.resultCode == 200) {
                    //                     document.getElementById("domotButton").innerText("开始计算")
                    //                     document.getElementById("domotButton").removeAttribute("disable")
                    //                     $("#videoresult").attr("src", result2.data.resultPath);
                    //                     $("#videoresult").attr("style", "width: 200px;display:block;");
                    //                     var formFieldresultpath = document.getElementById("resultPath");
                    //                     formFieldresultpath.value = result2.data.resultPath;
                    //
                    //                     var formFieldresulttime = document.getElementById("resultTime");
                    //                     formFieldresulttime.value = r.data.resultTime;
                    //                 } else {
                    //                     swal("计算错误", {icon: "error"});
                    //                 }
                    //             }
                    //         })
                    // })


                } else {

                    swal("执行失败", {
                        icon: "error"
                    })

                }
            }
        })

    });

    // $('#domotButton').click(function () {
    //     isMOT=0;
    //     document.getElementById("domotButton").setAttribute("disabled", true);
    //     $.ajax({
    //         type:"get",
    //         // url:"/myvideo/upload/ajaxtest",
    //         url:"/rabbit/getmessage",
    //         data:{cameraId:"0"},
    //         dataType:"json",
    //         success: function (result) {
    //             document.getElementById("domotButton").removeAttribute("disabled");
    //             if (result.resultCode==200){
    //                 isMOT=1;
    //
    //                 var formFieldresultpath = document.getElementById("resultPath");
    //                 formFieldresultpath.value = r.data.resultpath;
    //
    //                 var formFieldresulttime = document.getElementById("resultTime");
    //                 formFieldresulttime.value = r.data.resulttime;
    //
    //                 $("#videoresult").attr("src", result.data);
    //                 $("#videoresult").attr("style", "width: 200px;display:block;");
    //                 swal("执行完成",{
    //                     icon:"success"
    //                 });
    //             }
    //             else{
    //
    //                 swal("执行失败", {
    //                     icon: "error"
    //                 })
    //
    //             }
    //         }
    //     })
    //
    // });

    $('#getresultButton').click(function () {

        $.ajax({
            type:"get",
            url:"/myvideo/upload/ajaxresult",
            data:{cameraId:"0"},
            dataType:"json",
            success: function (result) {
                // if (result.data != null && result.resultCode === 200){
                //     swal(result.data,{
                //         icon:"success"
                //     });
                //     window.location.href = "https://www.baidu.com"
                // }
                if (result.data != null && result.resultCode === 200){
                    swal({
                        title:"结果生成成功",
                        icon:"success",
                        buttons:{
                            button1:{
                                text:"立即查看结果",
                                value:true,
                            },
                            button2:{
                                text:"不了，过会儿查看",
                                // text:"当前页面显示",
                                value:false,
                            }

                        },
                    }).then(function (value) {

                        if(value){
                            window.location.href=result.data
                        }
                        // else{
                        //     $("#img").attr("src", result.data);
                        //     $("#img").attr("style", "width: 200px;display:block;");
                        // }
                    })
                }
                else{
                    swal({
                        title:"结果还未生成",
                        text:"请稍后再来",
                        icon:"error"
                    });
                }

            }
        })

    })

});


$('#originCoverImage').click(function () {

    $("#videoCoverImage").attr("src",$('#videoCoverImage').val());
    $("#videoCoverImage").attr("style", "width:160px ;height: 120px;display:block;");
});

$('#saveButton').click(function () {
    var videoId = $('#videoId').val();
    var videoName = $('#videoName').val();
    var videoCoverImage = $('#videoCoverImage')[0].src;

    var videoPath = $('#videoPath').val();
    var videoTime = $('#videoTime').val();
    var resultPath = $('#resultPath').val();
    var resultTime = $('#resultTime').val();
    var hasResult = $('#hasResult').val();

    var cameraCategoryId = $('#cameraCategoryId').val();
    var isShow = $("input[name='isShow']:checked").val();

    if (isNull(videoName)) {
        swal("请输入视频名称", {
            icon: "error",
        });
        return;
    }

    if (isNull(cameraCategoryId)) {
        swal("请选择文章分类", {
            icon: "error",
        });
        return;
    }

    if (isNull(videoCoverImage) || videoCoverImage.indexOf('img-upload') != -1) {
        swal("封面图片不能为空", {
            icon: "error",
        });
        return;
    }



    if (videoId > 0) {
        var url = '/myvideoedit/save';
        var swlMessage = '保存成功';
        var data = {
            "videoId":videoId,
            "videoName": videoName,
            "videoCoverImage":videoCoverImage,
            "videoPath":videoPath,
            "videoTime":videoTime,
            "hasResult": hasResult,
            "resultPath":resultPath,
            "resultTime":resultTime,

            "cameraCategoryId": cameraCategoryId,
            "isShow": isShow,

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
                    confirmButtonText: '返回博客列表',
                    confirmButtonClass: 'btn btn-success',
                    buttonsStyling: false
                }).then(function () {
                    window.location.href = "/admin/blogs";
                })
            }
            else {
                $('#articleModal').modal('hide');
                swal(result.message, {
                    icon: "error",
                });
            }

        },
        error: function () {
            swal("操作失败", {
                icon: "error",
            });
        }
    });
});

$('#confirmButton').click(function () {
    if (isUpdate===0){
        swal("请先正确上传视频",{
            icon:"error",
        });
        return;
    }

    if (isMOT===0){
        swal("请先生成轨迹结果",{
            icon:"error",
        });
        return;
    }

    $('#articleModal').modal('show');
});