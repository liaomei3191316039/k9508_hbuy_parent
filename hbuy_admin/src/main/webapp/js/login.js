layui.use(['jquery','layer','form',],function () {
    //创建出内置模块的对象
    var $ = layui.jquery,
        layer = layui.layer,
        form = layui.form;

    //定义验证码的变量
    var userLoginVerifyIf = false;

    var loginUIMsg = $("#loginUIMsg").val();

    if(loginUIMsg!=""){
        layer.msg("你还未登陆！！",{icon:2,time:2000,anim:6});
    }

    //表单验证
    form.verify({
        userName: function (value, item) { //value：表单的值、item：表单的DOM对象
            if (value.length>9||value.length<3) {
                return '用户名长度在3-9之间';
            }
        },
        pwd: function (value, item) { //value：表单的值、item：表单的DOM对象
            if (value.length>12||value.length<6) {
                return '密码长度在6-12之间';
            }
        }
    });

    //验证码的验证
    $("#yzm").blur(function () {
        //进行服务器端的验证
        userLoginVerify($(this).val());
    });

    //监听登陆表单提交
    form.on('submit(login)', function(data){
        if(userLoginVerifyIf){
            var loginJsonUser = data.field;
            login(loginJsonUser);
        }else {
            layer.msg("验证码输入错误！！",{icon:2,time:2000,anim:6});
        }
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });


    //进行验证码的验证
    function userLoginVerify(verify) {
        $.ajax({
            type:'POST',
            url:'adminusers/userLoginVerify',
            async: false, //允许外部的变量取到ajax里面的数据
            data:{"verify":verify},
            success:function (data) {
                if(data=='success'){
                    layer.tips('验证码输入正确。。', '#yzm', {tips: [2,'green'], time:3000});  //吸附框
                    userLoginVerifyIf = true;
                }else {
                    layer.tips('验证码输入错误！！', '#yzm', {tips: [2,'#fc1505'], time:3000});  //吸附框
                    userLoginVerifyIf = false;
                }
            },
            error:function () {
                layer.msg("服务器异常！！",{icon:3,time:2000,anim:3});
            }
        });
    }

    function login(loginJsonUser) {
        $.ajax({
            type:'POST',
            url:'adminusers/login',
            data:loginJsonUser,
            success:function (data) {
                if(data=='success'){
                    layer.msg("恭喜你，登陆成功。。",{icon:1,time:2000,anim:4});
                    setTimeout('window.location="model/toLayout"',2000);
                }else {
                    layer.msg("很遗憾，登陆失败！！",{icon:2,time:2000,anim:4});
                }
            },
            error:function () {
                layer.msg("服务器异常！！",{icon:3,time:2000,anim:3});
            }
        });
    }

});