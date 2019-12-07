<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
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
    <link rel="stylesheet" href="${APP_PATH}/css/doc.min.css">
    <style>
        .tree li {
            list-style-type: none;
            cursor:pointer;
        }
    </style>
</head>

<body>

<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <div><a class="navbar-brand" style="font-size:32px;" href="user.html">众筹平台 - 用户维护</a></div>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav navbar-right">
                <li style="padding-top:8px;">
                    <jsp:include page="/WEB-INF/jsp/common/userInfo.jsp"></jsp:include>
                </li>
                <li style="margin-left:10px;padding-top:8px;">
                    <button type="button" class="btn btn-default btn-danger">
                        <span class="glyphicon glyphicon-question-sign"></span> 帮助
                    </button>
                </li>
            </ul>
            <form class="navbar-form navbar-right">
                <input type="text" class="form-control" placeholder="Search...">
            </form>
        </div>
    </div>
</nav>

<div class="container-fluid">
    <div class="row">
        <jsp:include page="/WEB-INF/jsp/common/menu.jsp"></jsp:include>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <ol class="breadcrumb">
                <li><a href="#">首页</a></li>
                <li><a href="#">数据列表</a></li>
                <li class="active">分配角色</li>
            </ol>
            <div class="panel panel-default">
                <div class="panel-body">
                    <form role="form" class="form-inline">
                        <div class="form-group">
                            <label>未分配角色列表</label><br>
                            <select id="unassignRole" class="form-control" multiple size="10" style="width:100px;overflow-y:auto;">
                                <option value="pm">PM</option>
                                <option value="sa">SA</option>
                                <option value="se">SE</option>
                                <option value="tl">TL</option>
                                <option value="gl">GL</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <ul>
                                <li class="btn btn-default glyphicon glyphicon-chevron-right" id="assign_btn"></li>
                                <br>
                                <li class="btn btn-default glyphicon glyphicon-chevron-left" id="unassign_btn" style="margin-top:20px;"></li>
                            </ul>
                        </div>
                        <div class="form-group" style="margin-left:40px;">
                            <label>已分配角色列表</label><br>
                            <select id="assignedRole" class="form-control" multiple size="10" style="width:100px;overflow-y:auto;">
                                <option value="qa">QA</option>
                                <option value="qc">QC</option>
                                <option value="pg">PG</option>
                            </select>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" id="myModalLabel">帮助</h4>
            </div>
            <div class="modal-body">
                <div class="bs-callout bs-callout-info">
                    <h4>测试标题1</h4>
                    <p>测试内容1，测试内容1，测试内容1，测试内容1，测试内容1，测试内容1</p>
                </div>
                <div class="bs-callout bs-callout-info">
                    <h4>测试标题2</h4>
                    <p>测试内容2，测试内容2，测试内容2，测试内容2，测试内容2，测试内容2</p>
                </div>
            </div>
            <!--
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
              <button type="button" class="btn btn-primary">Save changes</button>
            </div>
            -->
        </div>
    </div>
</div>
<script src="${APP_PATH}/jquery/jquery-2.1.1.min.js"></script>
<script src="${APP_PATH}/bootstrap/js/bootstrap.min.js"></script>
<script src="${APP_PATH}/script/docs.min.js"></script>
<script src="${APP_PATH}/script/layer/layer.js"></script>
<script type="text/javascript">
    $(function () {
        $(".list-group-item").click(function(){
            if ( $(this).find("ul") ) {
                $(this).toggleClass("tree-closed");
                if ( $(this).hasClass("tree-closed") ) {
                    $("ul", this).hide("fast");
                } else {
                    $("ul", this).show("fast");
                }
            }
        });

        loadRoles();
    });

    //分配角色
    $("#assign_btn").on("click",function () {
        var selected = $("#unassignRole option:selected");
        var jsonObj = {
            userId:${param.id}
        };

        $(selected).each(function (index,item) {
            jsonObj["ids["+index+"]"] = item.value;
        });

        $.ajax({
            url:"${APP_PATH}/user/doAssignRole.do",
            type:"POST",
            data:jsonObj,
            beforeSend:function () {
                index = layer.load();
                return true;
            },
            success:function (result) {
                if(result.success){
                    layer.close(index);
                    $("#assignedRole").append(selected);
                    layer.msg("角色分配成功",{time:1000});
                }else {
                    layer.close(index);
                    layer.msg("角色分配失败",{time:1000});
                }
            },
            error:function () {
                layer.close(index);
                layer.msg("请求失败",{time:1000});
            }
        });
    });

    //取消分配的角色
    $("#unassign_btn").on("click",function () {
        var selected = $("#assignedRole option:selected");

        var jsonObj = {
            userId:${param.id}
        };

        $(selected).each(function (index,item) {
            jsonObj["ids["+index+"]"] = item.value;
        });

        $.ajax({
            url:"${APP_PATH}/user/doUnAssignRole.do",
            type:"POST",
            data:jsonObj,
            beforeSend:function () {
                index = layer.load();
                return true;
            },
            success:function (result) {
                if(result.success){
                    layer.close(index);
                    $("#unassignRole").append(selected);
                    layer.msg("角色分配成功",{time:1000});
                }else {
                    layer.close(index);
                    layer.msg("角色分配失败",{time:1000});
                }
            },
            error:function () {
                layer.close(index);
                layer.msg("请求失败",{time:1000});
            }
        });
    });

    function loadRoles(){
        var index = -1;
        $.ajax({
            url:"${APP_PATH}/user/loadRoles.do?id=${param.id}",
            type:"GET",
            beforeSend:function () {
                index = layer.load();
                return true;
            },
            success:function (result) {
                if(result.success){
                    layer.close(index);
                    layer.msg("数据加载成功",{time:1000});
                    loadLeftList(result.datas.leftRoleList);
                    loadRightList(result.datas.rightRoleList);
                }else {
                    layer.close(index);
                    layer.msg("数据加载失败",{time:1000});
                }
            },
            error:function () {
                layer.close(index);
                layer.msg("数据请求失败",{time:1000});
            }
        });
    }

    function loadLeftList(data) {
        var content = "";
        $.each(data,function (index,item) {
            content += '<option value="'+item.id+'">'+item.name+'</option>';
        });
        $("#unassignRole").html(content);
    }

    function loadRightList(data) {
        var content = "";
        $.each(data,function (index,item) {
            content += '<option value="'+item.id+'">'+item.name+'</option>';
        });
        $("#assignedRole").html(content);
    }
</script>
</body>
</html>
