$(function () {

    var InterValObjIf = true;

    var InterValObj;

    //定义令牌
    var token = "";

    var InterValObjlogin;

    getToken();

    //js轮询登陆
    InterValObjlogin = window.setInterval(getToken, 5000);

    if(InterValObjIf){
        //js轮询加载秒杀商品
        InterValObj = window.setInterval(loadUPSecKill, 2000);
    }

    //取登陆模块的令牌
    function getToken() {
        console.log("执行了获取令牌的方法")
        $.ajax({
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
        $.ajax({
            type:'POST',
            async:false,
            data:{"token":token},
            url:'http://localhost:7073/webusers/getLoginUser',
            //设置ajax请求跨域
            xhrFields:{withCredentials:true},
            success:function (data) {
                console.log(data);
                $("#userText").html('你好，'+data.uname+'&nbsp; <img src=" '+data.userheader+'" width="20px" height="20px"/><a href="#" style="color:#ff4e00;" id="exitUser">注销</a>&nbsp;|&nbsp;<a href="#">我的订单</a>&nbsp;|')
            },
            error:function () {
                alert("服务器异常！！");
            }
        });
    }

    if(token==''){
        $("#userText").html('你好，请<a href="localhost:8084/webusers/toLogin" id="titleUser">登录</a>&nbsp; <a href="localhost:8084/webusers/toReg" style="color:#ff4e00;">免费注册</a>&nbsp;|&nbsp;<a href="#">我的订单</a>&nbsp;|')
    }


    //加载可以秒杀的商品
    function loadUPSecKill(){
        console.log("执行了轮询。。")
        $.ajax({
            type:'POST',
            async:false,
            url:'/webseckill/loadUPSecKill',
            success:function (data) {
                if(data.length!=0){
                    var liStr = '';
                    $.each(data,function (i,map) {
                        liStr += '<li>\n' +
                            '                \t<div class="img"><img src="'+map.avthorImg+'" width="160" height="140" /></div>\n' +
                            '                    <div class="name">'+map.title+'</div>\n' +
                            '                    <div class="price">\n' +
                            '                    \t<table border="0" style="width:100%; color:#888888;" cellspacing="0" cellpadding="0">\n' +
                            '                          <tr style="font-family:\'宋体\';">\n' +
                            '                            <td width="33%">市场价 </td>\n' +
                            '                            <td width="33%">优惠</td>\n' +
                            '                            <td width="33%">秒杀价</td>\n' +
                            '                          </tr>\n' +
                            '                          <tr>\n' +
                            '                            <td style="text-decoration:line-through;">￥'+map.price+'</td>                   \n' +
                            '                            <td>'+(map.price-map.secPrice)+'</td>\n' +
                            '                            <td>￥'+map.secPrice+'</td>\n' +
                            '                          </tr>\n' +
                            '                        </table>\n' +
                            '                    </div>\n' +
                            '                    <div class="ch_bg">\n' +
                            '                        <span class="ch_txt">￥<font>'+map.secPrice+'</font></span>\n' +
                            '                        <button class="ch_a" id="seckill" style="background-color: darkorange" secId="'+map.secId+'">秒杀</button>\n' +
                            '                    </div>\n' +
                            '                    <div class="times">结束时间：'+map.endTime+'<p id="show"></p></div>\n' +
                            '                </li>';
                    })
                    $("#secKillUl").html(liStr);
                    TimeDown("show", data[0].endTime);  //开启倒计时
                    InterValObjIf = false;
                    window.clearInterval(InterValObj);
                }else {
                    $("#secKillUl").html('<font color="red" size="40px;">对不起，暂时没有秒杀商品！！</font>');
                }

            },
            error:function () {
                alert("服务器异常！！");
            }
        });

        $("#secKillUl").on("click","#seckill",function () {
            var secId = $(this).attr("secId");
            var abtn = $(this)
            if(token!=''){
                //执行秒杀
                doSecKill(secId,token,abtn);
            }else {
                alert("你还为登陆！！请先登陆再秒杀")
            }
            return false;
        })
    }

    //秒杀商品
    function doSecKill(secId,token,abtn) {
        $.ajax({
            type: 'POST',
            data: {"secId": secId, "token": token},
            async: false,
            url: '/webseckill/doSecKill',
            success: function (data) {
                alert(data.msg);
                if(data.code==200){
                    abtn.css("background-color","gray");
                    abtn.attr("disabled","disabled");
                    return false;
                }
            },
            error: function () {
                alert("服务器异常！！");
            }
        });
    }

    $("#exitUser").click(function () {
        exitUser();
    });

    //注销
    function exitUser(){
        console.log("退出。。")
        if(window.confirm("您确定要注销吗？")){
            $.ajax({
                type:'POST',
                async:false,
                data:{"token":token},
                url:'http://localhost:7073/webusers/exitUser',
                //设置ajax请求跨域
                xhrFields:{withCredentials:true},
                success:function (data) {
                    if(data=='success'){
                        $("#userText").html('你好，请<a href="localhost:8084/webusers/toLogin" id="titleUser">登录</a>&nbsp; <a href="localhost:8084/webusers/toReg" style="color:#ff4e00;">免费注册</a>&nbsp;|&nbsp;<a href="#">我的订单</a>&nbsp;|')
                        InterValObj = window.setInterval(getToken, 5000);
                    }
                },
                error:function () {
                    alert("服务器异常！！");
                }
            });
        }


    }

    //倒计时插件
    function TimeDown(id, endDateStr) {
        //结束时间
        var endDate = new Date(endDateStr);
        //当前时间
        var nowDate = new Date();
        //相差的总秒数
        var totalSeconds = parseInt((endDate - nowDate) / 1000);
        //天数
        var days = Math.floor(totalSeconds / (60 * 60 * 24));
        //取模（余数）
        var modulo = totalSeconds % (60 * 60 * 24);
        //小时数
        var hours = Math.floor(modulo / (60 * 60));
        modulo = modulo % (60 * 60);
        //分钟
        var minutes = Math.floor(modulo / 60);
        //秒
        var seconds = modulo % 60;
        //输出到页面
        document.getElementById(id).innerHTML = "还剩:" + days + "天" + hours + "小时" + minutes + "分钟" + seconds + "秒";
        //延迟一秒执行自己
        var time = setTimeout(function () {
            TimeDown(id, endDateStr);
        }, 1000)
        if(totalSeconds==0){
            window.clearTimeout(time);
            $("#secKillUl button").attr("disabled","disabled");
            $("#secKillUl button").css("background-color","gray");
            $("#show").html("秒杀已结束，欢迎下次再来")
        }

    }
});