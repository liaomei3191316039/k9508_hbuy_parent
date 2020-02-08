<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>test01.ftl</title>
</head>
<body>
   <h1 align="center">test01.ftl测试页面</h1>
   <hr/>
   <h3>1.简单数据的传递</h3>
   <p>用户名：${userName}</p>
   <p>数量：${num}</p>
   <p>价格：${price}</p>
   <p>时间：${nowDate?string('yyyy-MM-dd HH:mm:ss')}</p>
   <hr/>
   <h3>2.对象的传值</h3>
   <p>编号：${productDetail.id}</p>
   <p>名称：${productDetail.subtitle}</p>
   <p>价格：${productDetail.price}</p>
   <p>修改时间：${productDetail.updatetime?string('yyyy-MM-dd HH:mm:ss')}</p>
   <p>颜色：${productDetail.color}</p>
   <hr/>
   <h3>3.list集合的传值</h3>
   <#list strs as str>
       <p>每一个元素：${str}</p>
   </#list>
   <hr/>
   <h3>4.map集合的传值</h3>
   <p>${dataMap['pwd']}</p>
   <p>${dataMap['price']}</p>
   <p>${dataMap['num']}</p>
   <p>${dataMap['createDate']?string('yyyy-MM-dd HH:mm:ss')}</p>
</body>
</html>