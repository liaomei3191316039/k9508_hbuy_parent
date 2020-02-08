$(function () {

    //加载所有的导航菜单
    loadAllMeau();

    //加载所有导航菜单
    function loadAllMeau() {
        $.ajax({
            type:'POST',
            url:'/webMenuCon/loadAllMenu',
            dataType:'JSON',
            success:function (data) {
                var menuLis = '';
                $.each(data,function (i,meau) {
                    menuLis += '<li><a href="'+meau.url+'">'+meau.title+'</a></li>';
                });
                $(".menu_r").html(menuLis);
            },
            error:function () {
                alert("服务器异常！！");
            }
        });
    }

});