<html lang="en">
    <head>
        <meta charset="UTF-8">
        <link href="https://cdn.bootcss.com/twitter-bootstrap/3.0.1/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <title>商家页面</title>
<body>
<div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>订单id</th>
                    <th>姓名</th>
                    <th>手机号</th>
                    <th>地址</th>
                    <th>金额</th>
                    <th>订单状态</th>
                    <th>支付状态</th>
                    <th>创建时间</th>
                    <th colspan="2">操作</th>
                </tr>
                </thead>
                <tbody>
                <#list orderDToPage.content as orderDTO>
                <tr>
                    <td>${orderDTO.orderId}</td>
                    <td>${orderDTO.buyerName}</td>
                    <td>${orderDTO.buyerPhone}</td>
                    <td>${orderDTO.buyerAddress}</td>
                    <td>${orderDTO.orderAmount}</td>
                    <td>${orderDTO.getOrderStatusEnum().getMsg()}</td>
                    <td>${orderDTO.getPayStatusEnum().getMsg()}</td>
                    <td>${orderDTO.createTime}</td>
                    <td>详情</td>
                    <td>
                        <#if orderDTO.getOrderStatusEnum().msg != "已取消">
                            <a href="${springMacroRequestContext.contextPath}/seller/order/cancel?orderId=${orderDTO.orderId}">取消</a>
                        </#if>
                    </td>
                </tr>
                </#list>
                </tbody>
            </table>
        </div>
        <#--分页-->
        <div class="col-md-12">
            <ul class="pagination pull-right">
                <li><a href="#">上一页</a></li>
                <#list 1.. orderDToPage.getTotalPages() as index>
                    <li><a href="#">${index}</a></li>
                </#list>
                <li><a href="#">下一页</a></li>
            </ul>
        </div>
    </div>
</div>
</body>
</html>