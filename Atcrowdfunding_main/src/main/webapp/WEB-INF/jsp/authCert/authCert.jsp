<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<!DOCTYPE html>
<html lang="GB18030">
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
            <div><a class="navbar-brand" style="font-size:32px;" href="#">众筹平台 - 实名认证审核</a></div>
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
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr >
                                <th width="30">#</th>
                                <th>流程名称</th>
                                <th>流程版本</th>
                                <th>任务名称</th>
                                <th>申请会员</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>1</td>
                                    <td>实名认证审批流程</td>
                                    <td>2</td>
                                    <td>人工审核</td>
                                    <td>张三</td>
                                    <td>
                                        <button type="button" class="btn btn-success btn-xs"><i class=" glyphicon glyphicon-check"></i></button>
                                        <button type="button" class="btn btn-danger btn-xs"><i class=" glyphicon glyphicon-remove"></i></button>
                                    </td>
                                </tr>
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

        toPage(0);
    });

    //加载用户json数据
    function toPage(page_index) {
        var index = -1;
        $.ajax({
            url:"${APP_PATH}/authCert/loadAuthCerts.do?pn="+page_index+1,
            type:"GET",
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

    //加载表格
    function loadTbody(date) {
        var content = "";
        $.each(date,function (index,item) {
            content += '<tr>';
            content += '    <td>'+(index+1)+'</td>';
            content += '    <td>'+item.procDefName+'</td>';
            content += '    <td>'+item.version+'</td>';
            content += '    <td>'+item.taskName+'</td>';
            content += '    <td>'+item.member.username+'</td>';
            content += '    <td>';
            content += '    <button type="button" onclick="showCertDetail('+item.member.id+','+item.taskId+',\''+item.member.username+'\')"  class="btn btn-success btn-xs"><i class="glyphicon glyphicon-eye-open"></i></button>';
            content += '    </td>';
            content += '</tr>';
        });
        $("table tbody").html(content);
    }

    //显示会员上传的资质详情
    function showCertDetail(id,taskid,name) {
        layer.open({
            type: 2
            ,title: '会员资质详情['+name+']'
            ,area: ['800px', '600px']
            ,shade: 0
            ,maxmin: true
            ,content: 'showCertDetail.htm?id='+id+"&taskid="+taskid
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

