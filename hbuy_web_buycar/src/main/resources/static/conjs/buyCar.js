jQuery(function () {

    //定义是令牌
    var token = "";

    //加载token
    getToken();

    var InterValObj;
    //选择的商品id
    var proIds = '';
    //选中的购物车价格
    var allPrice = 0;

    //js轮询
    InterValObj = window.setInterval(getToken, 5000);

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
                    window.clearInterval(InterValObj);
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
                if(data!=""){
                    loadBuyCar();  //加载购物车
                }
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

    //初始化购物车
    loadBuyCar();

    //加载购物车
    function loadBuyCar(){
        jQuery.ajax({
            type:'post',
            url:'/buyCar/loadBuyCar/',
            data:{"token":token},
            success:function (data) {
                console.log(data);
                if(data!=""){
                    var trBuyCarStr = '';
                    jQuery.each(data,function (i,buyCar) {
                        trBuyCarStr += '<tr>\n' +
                            '              <td>\n' +
                            '                  <p style="float: left"><input type="checkbox" name="chk" value="'+buyCar.proId+'" price="'+buyCar.zprice+'"/></p><div class="c_s_img" style="margin-left: 120px;"><img src="'+buyCar.avatorimg+'" width="73" height="73" /></div>\n' +
                            '                  '+buyCar.title+'\n' +
                            '              </td>\n' +
                            '              <td align="center">'+buyCar.subtitle+'</td>\n' +
                            '              <td align="center" style="color:#ff4e00;">￥'+buyCar.price+'</td>\n' +
                            '              <td align="center">\n' +
                            '                  <div class="c_num">\n' +
                            '                      <input type="button" value="" onclick="jianUpdate1(jq(this));" class="car_btn_1" />\n' +
                            '                      <input type="text" value="'+buyCar.num+'" name="" class="car_ipt" />\n' +
                            '                      <input type="button" value="" onclick="addUpdate1(jq(this));" class="car_btn_2" />\n' +
                            '                  </div>\n' +
                            '              </td>\n' +
                            '              <td align="center">'+buyCar.zprice+'</td>\n' +
                            '              <td align="center"><a onclick="ShowDiv(\'MyDiv\',\'fade\')">删除</a>&nbsp; &nbsp;<a href="#">加入收藏</a></td>\n' +
                            '          </tr>';
                    });
                    jQuery("#buyCarTbody").html(trBuyCarStr);
                }
            },
            error:function () {
                layer.msg("操作异常！！！",{"icon":3,anim:6,shade:0.6,"time":2000});
            }
        });
    }

    jQuery("#chkAll").click(function () {
        //1.找到所有的复选框
        var arrchecks = document.getElementsByName("chk");
        //2.获取全选/全不选的标签
        var chkAll = document.getElementById("chkAll");
        //3.将所有复选框状态改为全选/全不选的标签的状态一致
        for(var i=0;i<arrchecks.length;i++){
            arrchecks[i].checked = chkAll.checked;
            var price = arrchecks[i].attributes["price"].nodeValue;
            allPrice += parseFloat(price);
            proIds += arrchecks[i].value + ",";
        }
        //当反选时，proIds为""
        if(!jQuery('#chkAll').is(':checked')){//判断是否被选中
            proIds = '';
            allPrice = 0;
        }
        proIds = proIds.substring(0,proIds.length-1);
        jQuery("#allPrice").html(allPrice);
    });

    //单个的选
    jQuery("#buyCarTbody").on("click",'input:checkbox',function () {
        var proId = jQuery(this).val();
        var price = parseFloat(jQuery(this).attr("price"));
        if(jQuery(this).is(':checked')){
            allPrice += price;
            proIds += ","+proId;
        }else {
            allPrice -= price;
        }
        console.log(proIds);
        jQuery("#allPrice").html(allPrice);
    });

    //提交订单
    jQuery("#subOrders").click(function () {
        if(token!=""){
            if(allPrice==0){
               alert("还未选中商品");
            }else {
                //进行添加订单
                addToMQ(token,proIds,allPrice);
            }
        }else {
            alert("你还未登陆。。")
        }
        return false;
    });

    function addToMQ(token,proIds,allPrice) {
        jQuery.ajax({
            type: 'post',
            url: '/buyCar/addToMQ/',
            data: {"token": token,"proIds":proIds,"allPrice":allPrice},
            success: function (data) {
                alert(data.msg);
            },
            error: function () {

            }
        });
    }

});


