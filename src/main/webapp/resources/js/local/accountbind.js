$(function() {
    var bindUrl = '/o2o/local/bindlocalauth';
    //userType为1则为前端展示系统，2为店家管理系统
    var userType = getQueryString("usertype");

    $('#submit').click(function() {
        var userName = $('#username').val();
        var password = $('#psw').val();
        var verifyCodeActual = $('#j_captcha').val();
        var needVerify = false;
        if (!verifyCodeActual) {
            $.toast('请输入验证码！');
            return;
        }
        $.ajax({
            url : bindUrl,
            async : false,
            cache : false,
            type : "post",
            dataType : 'json',
            data : {
                userName : userName,
                password : password,
                verifyCodeActual : verifyCodeActual
            },
            success : function(data) {
                if (data.success){
                    $.toast("绑定成功");
                    if (userType == 1) {
                        //若用户在前端展示系统页面则自动退回到前端展示系统首页
                        window.location.href = '/o2o/frontend/index';
                    } else {
                        //若用户是在家家管理系统页面则自动回退到店铺列表中
                        window.location.href = '/o2o/shopadmin/shoplist';
                    }
                } else {
                    $.toast("绑定失败");
                    $('#captcha_img').click();
                }
            }
        });
    });

    function getQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) {
            return decodeURIComponent(r[2]);
        }
        return '';
    }
});