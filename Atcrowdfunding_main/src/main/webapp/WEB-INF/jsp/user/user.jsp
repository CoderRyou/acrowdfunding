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
    <link rel="stylesheet" href="${APP_PATH}/script/pagination/pagination.css">
    <style>
        .tree li {
            list-style-type: none;
            cursor:pointer;
        }
        table tbody tr:nth-child(odd){background:#F4F4F4;}
        table tbody td:nth-child(even){color:#C00;}
    </style>
</head>

<body>

<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <div><a class="navbar-brand" style="font-size:32px;" href="#">众筹平台 - 用户维护</a></div>
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
                <input type="text" class="form-control" placeholder="查询">
            </form>
        </div>
    </div>
</nav>

<div class="container-fluid">
    <div class="row">
        <jsp:include page="/WEB-INF/jsp/common/menu.jsp"></jsp:include>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input id="search_user_input" class="form-control has-success" type="text" placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button type="button" class="btn btn-warning" id="search_user_btn"><i class="glyphicon glyphicon-search"></i> 查询</button>
                    </form>
                    <button type="button" class="btn btn-danger" style="float:right;margin-left:10px;" id="delete_batch_btn"><i class=" glyphicon glyphicon-remove"></i> 删除</button>
                    <button type="button" class="btn btn-primary" style="float:right;" onclick="window.location.href='${APP_PATH}/user/addUser.htm'"><i class="glyphicon glyphicon-plus"></i> 新增</button>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr >
                                <th width="30">#</th>
                                <th width="30"><input type="checkbox" id="check_all"></th>
                                <th>账号</th>
                                <th>名称</th>
                                <th>邮箱地址</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>1</td>
                                <td><input type="checkbox"></td>
                                <td>Lorem</td>
                                <td>ipsum</td>
                                <td>dolor</td>
                                <td>
                                    <button type="button" class="btn btn-success btn-xs"><i class=" glyphicon glyphicon-check"></i></button>
                                    <button type="button" class="btn btn-primary btn-xs"><i class=" glyphicon glyphicon-pencil"></i></button>
                                    <button type="button" class="btn btn-danger btn-xs"><i class=" glyphicon glyphicon-remove"></i></button>
                                </td>
                            </tr>

                            </tbody>
                            <tfoot>
                            <tr >
                                <td colspan="6" align="center">
                                    <!--<ul class="pagination">
                                        <li class="disabled"><a href="#">上一页</a></li>
                                        <li class="active"><a href="#">1 <span class="sr-only">(current)</span></a></li>
                                        <li><a href="#">2</a></li>
                                        <li><a href="#">3</a></li>
                                        <li><a href="#">4</a></li>
                                        <li><a href="#">5</a></li>
                                        <li><a href="#">下一页</a></li>
                                    </ul>-->
                                    <div id="pagination" class="pagination"></div>
                                </td>
                            </tr>

                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="${APP_PATH}/jquery/jquery-2.1.1.min.js"></script>
<script src="${APP_PATH}/bootstrap/js/bootstrap.min.js"></script>
<script src="${APP_PATH}/script/docs.min.js"></script>
<script src="${APP_PATH}/script/layer/layer.js"></script>
<script src="${APP_PATH}/script/pagination/jquery.pagination.js"></script>
<script type="text/javascript">

    var currentPage;//当前页码

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

        toPage(0);
    });

    $(document).on("click",".assign_btn",function(){
        window.location.href = "${APP_PATH}/user/assignRole.htm?id="+$(this).attr("id");
    });

    //修改用户按钮事件
    $(document).on("click",".edit_btn",function () {
        window.location.href = "${APP_PATH}/user/editUser.htm?id="+$(this).attr("id");
    });

    var selectParam = {};

    //加载用户json数据
    function toPage(page_index) {
        var index = -1;
        $.ajax({
            url:"${APP_PATH}/user/loadUsers.do?pn="+(page_index+1),
            type:"GET",
            data:selectParam,
            beforeSend:function () {
                index = layer.load();
                return true;
            },
            success:function (result) {
                layer.close(index);
                layer.msg("数据加载成功",{time:1000});
                loadTbody(result.datas.pageInfo.list);

                //加载分页
                var num_entries = result.datas.pageInfo.total;
                $("#pagination").pagination(num_entries,{
                    num_edge_entries:2,//边缘页数
                    num_display_entries:4,//主体页s数
                    callback:toPage,//查询当前分页的数据
                    item_per_page:result.datas.pageInfo.pageSize,
                    current_page:(result.datas.pageInfo.pageNum - 1),
                    prev_text:"上一页",
                    next_text:"下一页"
                });
            },
            error:function () {
                layer.close(index);
                layer.msg("数据请求失败",{time:1000});
            }
        });
    }

    //条件查询事件
    $("#search_user_btn").on("click",function () {
        var searchText = $("#search_user_input").val();
        selectParam.text = searchText;
        toPage(0);
    });

    //加载表格
    function loadTbody(date) {
        var content = "";
        $.each(date,function (index,item) {
            content += '<tr>';
            content += '    <td>'+item.id+'</td>';
            content += '    <td><input type="checkbox" id="'+item.id+'" class="check_item"></td>';
            content += '        <td>'+item.loginacct+'</td>';
            content += '        <td>'+item.username+'</td>';
            content += '        <td>'+item.email+'</td>';
            content += '        <td>';
            content += '    <button type="button" class="btn btn-success btn-xs assign_btn" id="'+item.id+'"><i class=" glyphicon glyphicon-check"></i></button>';
            content += '    <button type="button" class="btn btn-primary btn-xs edit_btn" id="'+item.id+'"><i class=" glyphicon glyphicon-pencil"></i></button>';
            content += '    <button type="button" class="btn btn-danger btn-xs delete_btn" id="'+item.id+'"><i class=" glyphicon glyphicon-remove"></i></button>';
            content += '    </td> ';
            content += '/tr>';
        });
        $("table tbody").html(content);
    }

    //加载分页栏
    function loadPageUl(date) {

        currentPage = date.pageNum;

        var content = "";
        content += '<li><a href="javascript:;" onclick="toPage(1)">首页</a></li>';

        if(date.hasPreviousPage){
            content += '<li><a href="javascript:;" onclick="toPage('+(date.pageNum-1)+')">上一页</a></li>';
        }else {
            content += '<li class="disabled"><a href="#">上一页</a></li>';
        }

        $.each(date.navigatepageNums,function (index,item) {
            if(date.pageNum == item){
                content += '<li class="active"><a href="javascript:;" onclick="toPage('+item+')">'+item+'</a></li>';
            }else {
                content += '<li><a href="javascript:;" onclick="toPage('+item+')">'+item+'</a></li>';
            }
        });

        if(date.hasNextPage){
            content += '<li><a href="javascript:;" onclick="toPage('+(date.pageNum+1)+')">下一页</a></li>';
        }else {
            content += '<li class="disabled"><a href="#">下一页</a></li>';
        }

        content += '<li><a href="javascript:;" onclick="toPage('+date.pages+')">末页</a></li>';

        $("tfoot .pagination").html(content);
    }

    //全选按钮事件
    $("#check_all").on("click",function () {
        $(".check_item").prop("checked",$(this).prop("checked"));
    });

    //单选按钮事件
    $(document).on("click",".check_item",function () {
        var flag = $(".check_item:checked").length == $(".check_item").length;
        $("#check_all").prop("checked",flag);
    });

    //单个删除事件
    $(document).on("click",".delete_btn",function () {
        var id = $(this).attr("id");
        layer.confirm('确定删除该用户？', {
            btn: ['确定','取消'] //按钮
        }, function(){
            deleteBatchUserByIds({"ids[0]":id});
        });
    });


    //批量删除
    $("#delete_batch_btn").on("click",function () {
        var jsonObj = {};
        $.each($(".check_item:checked"),function (index,item) {
            jsonObj["ids["+index+"]"] = $(this).attr("id");
        });

        layer.confirm('确定删除这些用户？', {
            btn: ['确定','取消'] //按钮
        }, function(){
            deleteBatchUserByIds(jsonObj);
        });
    });

    function deleteBatchUserByIds(ids) {
//        ids._method = "DELETE";
        console.log(ids);
        var index = -1;
        $.ajax({
            url:"${APP_PATH}/user/doDelete.do",
            type:"POST",
            data:ids,
            beforeSend:function () {
                index = layer.load();
                return true;
            },
            success:function (result) {
                if(result.success){
                    layer.close(index);
                    layer.msg("删除成功",{time:1000});
                    toPage(currentPage);
                }else{
                    layer.close(index);
                    layer.msg("删除出错",{time:1000});
                }
            },
            error:function () {
                layer.close(index);
                layer.msg("删除失败",{time:1000});
            }
        });
    }

</script>
</body>
</html>

