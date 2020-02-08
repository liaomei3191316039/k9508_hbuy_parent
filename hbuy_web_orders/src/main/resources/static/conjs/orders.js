jQuery(function () {

    var InterValObj;

    //定义令牌
    var token = "";

    var InterValObjlogin;

    getToken();

    //js轮询登陆
    InterValObjlogin = window.setInterval(getToken, 5000);

    //取登陆模块的令牌
    function getToken() {
        console.log("执行了获取令牌的方法")
        jQuery.ajax({
            type:'POST',
            async:false,
            url:'http://localhost:7073/webusers/getToken',
            //设置ajax请求跨域
            xhrFields:{withCredentials:true},
            success:function (data) {
                if(data!=""){
                    console.log(data);
                    token = data;
                    //关掉js轮询
                    window.clearInterval(InterValObjlogin);
                    //已经获取令牌，获取redis中的数据
                    getLoginUser();
                }
            },
            error:function () {
                alert("服务器异常！！");
            }
        });
    }

    //根据令牌取到redis中的用户数据
    function getLoginUser() {
        jQuery.ajax({
            type:'POST',
            async:false,
            data:{"token":token},
            url:'http://localhost:7073/webusers/getLoginUser',
            //设置ajax请求跨域
            xhrFields:{withCredentials:true},
            success:function (data) {
                console.log(data);
                jQuery("#userText").html('你好，'+data.uname+'&nbsp; <img src=" '+data.userheader+'" width="20px" height="20px"/><a href="#" style="color:#ff4e00;" id="exitUser">注销</a>&nbsp;|&nbsp;<a href="#">我的订单</a>&nbsp;|')
            },
            error:function () {
                alert("服务器异常！！");
            }
        });
    }

    if(token==''){
        jQuery("#userText").html('你好，请<a href="localhost:8084/webusers/toLogin" id="titleUser">登录</a>&nbsp; <a href="localhost:8084/webusers/toReg" style="color:#ff4e00;">免费注册</a>&nbsp;|&nbsp;<a href="#">我的订单</a>&nbsp;|')
    }

    jQuery("#exitUser").click(function () {
        exitUser();
    });

    //注销
    function exitUser(){
        console.log("退出。。")
        if(window.confirm("您确定要注销吗？")){
            jQuery.ajax({
                type:'POST',
                async:false,
                data:{"token":token},
                url:'http://localhost:7073/webusers/exitUser',
                //设置ajax请求跨域
                xhrFields:{withCredentials:true},
                success:function (data) {
                    if(data=='success'){
                        jQuery("#userText").html('你好，请<a href="localhost:8084/webusers/toLogin" id="titleUser">登录</a>&nbsp; <a href="localhost:8084/webusers/toReg" style="color:#ff4e00;">免费注册</a>&nbsp;|&nbsp;<a href="#">我的订单</a>&nbsp;|')
                        InterValObj = window.setInterval(getToken, 5000);
                    }
                },
                error:function () {
                    alert("服务器异常！！");
                }
            });
        }
    }

    if(token!=""){
        loadOrdersByUid(token);
    }

    //加载订单
    function loadOrdersByUid(token) {
        jQuery.ajax({
            type:'POST',
            async:false,
            data:{"token":token},
            url:'/weborder/loadManyOrders',
            success:function (data) {
                var orderStr = '';
                if(data!=""){
                    jQuery.each(data,function (i,orders) {
                        var flagStr = "";
                        if(orders.flag=="1"){
                            flagStr = "购物车";
                        }else {
                            flagStr = "秒杀";
                        }
                        var ordersStatustr = '';
                        var play = '';
                        if(orders.orderstatus=="0"){
                            ordersStatustr = "已创建";
                            play = "去支付"
                        }
                        if(orders.orderstatus=="1"){
                            ordersStatustr = "未支付";
                            play = "去支付"
                        }
                        if(orders.orderstatus=="2"){
                            ordersStatustr = "已支付";
                            play = "待收货"
                        }
                        if(orders.orderstatus=="3"){
                            ordersStatustr = "已收货";
                            play = "待评价"
                        }
                        if(orders.orderstatus=="4"){
                            ordersStatustr = "已评价";
                            play = "查看评价"
                        }
                        if(orders.orderstatus=="5"){
                            ordersStatustr = "已失效";
                            play = "删除"
                        }
                        orderStr += ' <tr>\n' +
                        '                <td><font color="#ff4e00">'+orders.orderno+'</font></td>\n' +
                        '                <td>'+orders.createDate+'</td>\n' +
                        '                <td>'+orders.endDate+'</td>\n' +
                        '                <td>￥'+orders.cost+'</td>\n' +
                        '                <td>'+ordersStatustr+'</td>\n' +
                        '                <td>'+flagStr+'</td>\n' +
                        '                <td>'+play+'</td>\n' +
                        '              </tr>';
                    })
                    jQuery("#ordersTbody").html(orderStr);
                }
            },
            error:function () {
                alert("服务器异常！！");
            }
        });
    }


});