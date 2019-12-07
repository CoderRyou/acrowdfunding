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
            <div><a class="navbar-brand" style="font-size:32px;" href="#">众筹平台 - 流程管理</a></div>
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
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input class="form-control has-success" type="text" placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button type="button" class="btn btn-warning"><i class="glyphicon glyphicon-search"></i> 查询</button>
                    </form>

                    <button id="uploadPrcDefBtn" type="button" class="btn btn-primary" style="float:right;"><i class="glyphicon glyphicon-upload"></i> 上传流程定义文件</button>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">

                        <!--流程定义文件上传表单-->
                        <form id="processDefFileForm" style="display: none" action="" method="post" enctype="multipart/form-data">
                            <input type="file" id="processDefFile" name="processDefFile">
                        </form>

                        <table class="table  table-bordered">
                            <thead>
                            <tr >
                                <th width="30">#</th>
                                <th>流程定义名称</th>
                                <th>流程定义版本</th>
                                <th>流程定义key</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody>

                            </tbody>
                            <tfoot>
                            <tr >
                                <td colspan="6" align="center">
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
<script src="${APP_PATH}/script/jquery-form/jquery-form.min.js"></script>
<script type="text/javascript">

    var currentPage;//当前页码
    var totalPage;//总页数

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
    });

    var selectParam = {};

    toPage(0);

    //加载用户json数据
    function toPage(page_index) {
        var index = -1;
        $.ajax({
            url:"${APP_PATH}/process/loadProcess.do?pn="+(page_index+1),
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

                //获取总页数和当前页数
                totalPage = result.datas.pageInfo.pages;
                currentPage = result.datas.pageInfo.pageNum;

                //加载分页
                var num_entries = result.datas.pageInfo.total;
                $("#pagination").pagination(num_entries,{
                    num_edge_entries:2,//边缘页数
                    num_display_entries:4,//主体页数
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
        toPage(1);
    });

    //加载表格
    function loadTbody(date) {
        var content = "";
        $.each(date,function (index,item) {
            content += '<tr>';
            content += '    <td>'+(index+1)+'</td>';
            content += '    <td>'+item.name+'</td>';
            content += '    <td>'+item.version+'</td>';
            content += '    <td>'+item.key+'</td>';
            content += '    <td>';
            content += '    <button type="button" onclick="showImgFrame(\''+item.id+'\',\''+item.name+'\')" class="btn btn-success btn-xs"><i class=" glyphicon glyphicon-eye-open"></i></button>';
            content += '    <button type="button" onclick="deleteProDef(\''+item.id+'\',\''+item.name+'\')" class="btn btn-danger btn-xs"><i class=" glyphicon glyphicon-remove"></i></button>';
            content += '    </td>';
            content += '</tr>';
        });
        $("table tbody").html(content);
    }

    //删除方法
    function deleteProDef(id,name) {
        var index = -1;
        layer.confirm('确定删除['+name+']流程定义吗？', {
            btn: ['确定','取消'] //按钮
        }, function(){
            $.ajax({
                url:"${APP_PATH}/process/doDelete.do",
                type:"POST",
                data:{
                    "id":id
                },
                beforeSend:function () {
                    index = layer.load();
                    return true;
                },
                success:function (result) {
                    if(result.success){
                        layer.close(index);
                        layer.msg("删除流程定义成功",{time:1000});
                        toPage(currentPage);
                    }else{
                        layer.close(index);
                        layer.msg("删除流程定义失败",{time:1000});
                    }
                },
                error:function () {
                    layer.close(index);
                    layer.msg("删除出错",{time:1000});
                }
            });
        });
    }

    //上传按钮绑定事件
    $("#uploadPrcDefBtn").click(function () {
        $("#processDefFile").click();
    });
    
    //表单内容发生变化事件
    $("input").change(function () {
        var options = {
            url:"${APP_PATH}/process/deploy.do",
            beforeSubmit:function () {
                loadingIndex = layer.msg("流程正在部署中",{icon:6});
                return true;//必须返回true，否则，请求终止
            },
            success:function (result) {
                layer.close(loadingIndex);
                if(result.success){
                    layer.msg("流程部署成功",{time:1000,icon:6});
                    toPage(totalPage);
                }else{
                    layer.msg("流程部署失败",{time:1000,icon:5,shift:6});
                }
            }
        };

        $("#processDefFileForm").ajaxSubmit(options);//异步提交
        return;
    });

    //显示图片iframe层的方法
    function showImgFrame(id,name){
        layer.open({
            type: 2
            ,title: '流程定义图片['+name+']'
            ,area: ['800px', '600px']
            ,shade: 0
            ,maxmin: true
            ,content: 'showImg.do?id='+id
            ,btn: ['关闭']
            ,btn2: function(){
                layer.closeAll();
            }

            ,zIndex: layer.zIndex //重点1
            ,success: function(layero){
                layer.setTop(layero); //重点2
            }
        });
    }
</script>
</body>
</html>

