<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <link rel="stylesheet" href="${APP_PATH}/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${APP_PATH}/css/font-awesome.min.css">
    <link rel="stylesheet" href="${APP_PATH}/css/main.css">
    <style>
        .tree li {
            list-style-type: none;
            cursor:pointer;
        }
        table tbody tr:nth-child(odd){background:#F4F4F4;}
        table tbody td:nth-child(even){color:#C00;}
        .row{
            padding: 10px 0px;
            border-bottom: #00AA88 solid 1px;
        }
    </style>
</head>

<body>
<div class="container-fluid">
    <div class="row">
        <div class="col-sm-4">
            <strong>会员真实姓名:</strong>
        </div>
        <div class="col-sm-8">
            <span>${member.realname}</span>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-4">
            <strong>会员身份证号码:</strong>
        </div>
        <div class="col-sm-8">
            <span>${member.cardnum}</span>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-4">
            <strong>会员电话号码:</strong>
        </div>
        <div class="col-sm-8">
            <span>${member.phonenum}</span>
        </div>
    </div>
    <c:forEach items="${certimgs}" var="certimg">
        <div class="row">
            <div class="col-sm-4">
                <strong>${certimg.name}:</strong>
            </div>
            <div class="col-sm-8">
                <img src="${APP_PATH}/pics/cert/${certimg.iconpath}" alt="${certimg.name}"  class="img-thumbnail"/>
            </div>
        </div>
    </c:forEach>
    <p class="text-center" style="margin-top: 10px">
        <button id="passBtn" type="button" class="btn btn-success"><i class="glyphicon glyphicon-ok-sign">通过申请</i></button>
        <button id="refuseBtn" type="button" class="btn btn-danger"><i class="glyphicon glyphicon-remove-sign">拒绝申请</i></button>
    </p>
</div>
<script src="${APP_PATH}/jquery/jquery-2.1.1.min.js"></script>
<script src="${APP_PATH}/bootstrap/js/bootstrap.min.js"></script>
<script src="${APP_PATH}/script/docs.min.js"></script>
<script src="${APP_PATH}/script/layer/layer.js"></script>
<script type="text/javascript">
    //通过申请
    $("#passBtn").click(function () {
        var index = -1;
        $.ajax({
            url:"${APP_PATH}/authCert/pass.do",
            type:"POST",
            data:{
                memberid:${member.id},
                taskid:${param.taskid}
            },
            beforeSend:function () {
                index = layer.load();
                return true;
            },
            success:function (result) {
                layer.close(index);
                if(result.success){
                    layer.msg("通过申请！",{time:1000});

                    //注意：parent 是 JS 自带的全局对象，可用于操作父页面
                    var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
                    parent.toPage(0);
                    parent.layer.close(index);
                }else{
                    layer.msg("通过申请错误！",{time:1000});
                }
            },
            error:function () {
                layer.close(index);
                layer.msg("请求出错！",{time:1000});
            }
        });
    });

    //拒绝申请
    $("#refuseBtn").click(function () {
        var index = -1;
        $.ajax({
            url:"${APP_PATH}/authCert/refuse.do",
            type:"GET",
            data:{
                memberid:${member.id},
                taskid:${param.taskid}
            },
            beforeSend:function () {
                index = layer.load();
                return true;
            },
            success:function (result) {
                layer.close(index);
                if(result.success){
                    layer.msg("拒绝申请！",{time:1000});

                    //注意：parent 是 JS 自带的全局对象，可用于操作父页面
                    var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
                    parent.toPage(0);
                    parent.layer.close(index);
                }else{
                    layer.msg("拒绝申请错误！",{time:1000});
                }
            },
            error:function () {
                layer.close(index);
                layer.msg("请求出错！",{time:1000});
            }
        });
    });
</script>
</body>
</html>
