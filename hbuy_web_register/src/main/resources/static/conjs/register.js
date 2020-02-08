layui.use(['layer','form'], function() {
    var layer = layui.layer,   //实例化弹出层对象
        form = layui.form;  //实例化表单对象

    var code;  //随机产生的验证码

    var curCount = 60;  //倒计时的时间设置

    var usernameIf = false;  //判断用户是否可用

    var phoneIf = false;  //判断手机号是否可用

    var smsIf = false;  //判断验证码是否可用

    //验证用户名唯一性
    $("#username").blur(function () {
        var username = $(this).val();
        if(username.length>=4&&username.length<=12){
            checkUsername(username);  //验证唯一性
        }else {
            layer.tips('用户名长度在4-12之内', '#username', {tips: [2,'#c62e3d'], time:3000});
            usernameIf = false;
        }
    });

    //验证手机号唯一性
    $("#phone").blur(function () {
        var phone = $(this).val();
        if(!(/^1[3456789]\d{9}$/.test(phone))){
            layer.tips('手机号格式不正确', '#phone', {tips: [2,'#c62e3d'], time:3000});
            phoneIf = false;
        }else {
            checkPhone(phone);
        }
    });

    //自定义验证
    form.verify({
        pwd: [/^[\S]{6,18}$/,'登录密码必须6到18位，且不能出现空格']
        ,pwd2: function(value, item){ //value：表单的值、item：表单的DOM对象
            var pwdVal = $("#pwd").val();
            if(value!=pwdVal){
                return '两次密码不一致';
            }
        }
    });

    //点击发送短信
    $("#send_btn").click(function () {
       if(phoneIf){  //手机号可用发送短信
           //产生6位数的随机验证码（阿里云的短信验证码首位不能为0）
           code = getCode(6);
           //将用户输入的手机号和产生的验证码发送到服务器完成短信发送
           sendSms($("#phone").val(),code);
       } else {
           layer.msg("手机号填入有误！！！",{icon:3,time: 2000,shade: 0.5,anim: 6})
       }
    });

    //验证手机验证码（短信发送服务器，验证在前端的js中）
    $("#userSms").blur(function () {
        if($(this).val()!=''){
            if($(this).val()==code){
                smsIf = true;
            } else {
                layer.tips('验证码错误！！', '#userSms', {tips: [2,'#c62e3d'], time:3000});
                smsIf = false;
            }
        }else {
            smsIf = false;
        }
    });

    //提交注册
    form.on('submit(demo2)', function(res){
        if(usernameIf&&smsIf){
            var saveJsonUser = res.field; //当前容器的全部表单字段，名值对形式：{name: value}
            saveJsonUser['uname'] = '无名式';
            saveJsonUser['updatetime'] = new Date();
            saveJsonUser['userheader'] = 'http://q1cydzrcd.bkt.clouddn.com/b62a7832f1714b59b602392f1a43c54c';
            saveUser(saveJsonUser);
        }else if(!usernameIf&&smsIf){
            layer.msg("该用户账号有误！！",{icon:3,time: 2000,shade: 0.5,anim: 6})
        }else if(usernameIf&&!smsIf){
            layer.msg("验证码有误！！",{icon:3,time: 2000,shade: 0.5,anim: 6})
        }else {
            layer.msg("用户名和验证码均有误！！",{icon:3,time: 2000,shade: 0.5,anim: 6})
        }
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });

    //验证用户名唯一性
    function checkUsername(username)  {
        $.ajax({
            type: 'POST',
            url: 'webusers/getTotalByPramas',
            async:false,  //允许外部变量取到ajax异步加载的数据
            data: {"username":username},
            success: function (res) {
                if(res>0){
                    usernameIf = false;
                    layer.tips('该用户名已存在', '#username', {tips: [2,'#c62e3d'], time:3000});
                }else {
                    usernameIf = true;
                    layer.tips('该用户名可用。。', '#username', {tips: [2,'#28cf70'], time:3000});
                }
            },
            error: function () {
                layer.msg("服务器异常！！！",{icon:3,time: 2000,shade: 0.5,anim: 4})
            }
        });
    }

    //验证手机号唯一性
    function checkPhone(phone) {
        $.ajax({
            type: 'POST',
            url: 'webusers/getTotalByPramas',
            async:false,  //允许外部变量取到ajax异步加载的数据
            data: {"phone":phone},
            success: function (res) {
                if(res>0){
                    phoneIf = false;
                    layer.tips('该手机号已存在', '#phone', {tips: [2,'#c62e3d'], time:3000});
                }else {
                    phoneIf = true;
                    layer.tips('该手机号可用。。', '#phone', {tips: [2,'#28cf70'], time:3000});
                }
            },
            error: function () {
                layer.msg("服务器异常！！！",{icon:3,time: 2000,shade: 0.5,anim: 4})
            }
        });
    }

    //产生随机验证码
    function getCode(codeLength) {
        //产生验证码
        var icon = "";
        var sCode = "0,1,2,3,4,5,6,7,8,9";
        var aCode = sCode.split(",");
        var aLength = aCode.length;
        for (var i = 1; i <= codeLength; i++) {
            var j = Math.floor(Math.random() * aLength);
            //不想在验证码第1位产生0
            if (i == 1 && aCode[j] == '0') {
                icon += 1;
            } else {
                icon += aCode[j];
            }
        }
        console.log("code=" + icon);//打印发送的验证码
        return icon;
    }

    //发送短信验证码
    function sendSms(phone,code){
      /*  layer.msg("短信验证码已发送。。。",{"icon":1,anim:4,shade:0.6,"time":2000});
        //设置button效果，开始计时
        $("#send_btn").attr("disabled", "true");  //将发送短信的按钮设置为不可用
        $("#phone").attr("disabled", "true");  //手机号的输入框设置为不可用
        $("#send_btn").css('background-color', "grey");  //将发送短信的按钮背景色设置为灰色
        $("#send_btn").val("剩余" + curCount + "秒");  //将发送短信的按钮设计倒计时（从60秒开始）
        InterValObj = window.setInterval(SetRemainTime, 1000); //启动计时器，1秒执行一次*/
        $.ajax({
             type:'post',
             url:'webusers/sendSms',
             data:{"phone":phone,"code":code},
             success:function (data) {
                 console.log(data);
                 if(data=='OK'){
                     layer.msg("短信验证码已发送。。。",{"icon":1,anim:4,shade:0.6,"time":2000});
                     //设置button效果，开始计时
                     $("#send_btn").attr("disabled", "true");  //将发送短信的按钮设置为不可用
                     $("#phone").attr("disabled", "true");  //手机号的输入框设置为不可用
                     $("#send_btn").css('background-color', "grey");  //将发送短信的按钮背景色设置为灰色
                     $("#send_btn").val("剩余" + curCount + "秒");  //将发送短信的按钮设计倒计时（从60秒开始）
                     InterValObj = window.setInterval(SetRemainTime, 1000); //启动计时器，1秒执行一次
                 }else {
                     layer.msg("短信验证码发送失败！！！",{"icon":2,anim:3,shade:0.6,"time":2000});
                     //重新发送
                     $("#send_btn").val("重新发送");
                 }
             },
             error:function () {
                 layer.msg("操作异常！！！",{"icon":3,anim:6,shade:0.6,"time":2000});
             }
         });
    }

    //timer处理函数(定时器执行的方法，进行倒计时)
    function SetRemainTime() {
        if (curCount == 0) {  //剩余的时间为0，表示倒计时已结束
            window.clearInterval(InterValObj);//停止计时器，清除定义的定时器
            $("#send_btn").removeAttr("disabled");//启用按钮
            $("#phone").removeAttr("disabled");//启用输入手机号
            $("#send_btn").css('background-color', "blue");
            $("#send_btn").val("重新发送");
            smsIf = false;   //将判断验证码是否可用改为false
            curCount = 60;  //将倒计时的时间改为60秒，方便用户从新发送短信
            code = ""; //清除验证码。如果不清除，过时间后，输入收到的验证码依然有效
        }
        else {
            curCount--;
            $("#send_btn").val("剩余" + curCount + "秒");
        }
    }

    //用户注册
    function saveUser(saveJsonUser){
        $.ajax({
            type:'post',
            url:'webusers/saveT',
            data:saveJsonUser,
            success:function (data) {
                if(data=='saveSuccess'){
                    layer.msg("恭喜你，注册成功。。。",{"icon":1,anim:4,shade:0.6,"time":2000});
                    //注册成功后的页面跳转
                    setTimeout('window.location = "model/toLogin"',2000);
                }else {
                    layer.msg("很遗憾，注册失败！！！",{"icon":2,anim:3,shade:0.6,"time":2000});
                }
            },
            error:function () {
                layer.msg("操作异常！！！",{"icon":3,anim:6,shade:0.6,"time":2000});
            }
        });
    }



});