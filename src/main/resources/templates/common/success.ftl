<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>成功页面</title>
</head>
<body>
<div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <div class="alert alert-dismissable alert-success">
              ${msg}<a href="${url}" class="alert-link">3秒后自动跳转</a>
            </div>
        </div>
    </div>
</div>
</body>
<script>
    setTimeout("location.href=${url}",3000);
</script>
</html>