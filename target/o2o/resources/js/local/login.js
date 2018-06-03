$(function () {
    //登录验证的controller url
    var loginUrl = "/o2o/local/logincheck";
    //从地址栏的URL里获取usertype
    //usertype=1则为customer，其余为shopowner
    var usertype = getQueryString("usertype");
    //登录次数，积累登录三次失败之后会自动弹出验证码要求输入
    var loginCount = 0;

    $('#submit').click(function () {
        var username = $('#username').val();
        var password = $('#psw').val();
        var verifyCodeActual = $('#j_captcha').val();
        //是否需要输入验证码
        var needVerify = false;
        //如果登录三次都失败
        if(loginCount >= 3){
            if (!verifyCodeActual){
                $.toast('请输入验证码！');
                return;
            }else {
                needVerify = true;
            }
        }
        $.ajax({
            url: loginUrl,
            async: false,
            cache: false,
            type: 'post',
            dataType: 'json',
            data: {
                username: username,
                password: password,
                verifyCodeActual: verifyCodeActual,
                needVerify: needVerify
            },
            success: function (data) {
                if (data.success){
                    $.toast('登录成功！');
                    if (usertype == 1){
                        //若用户在前端展示系统页面则自动连接到前端展示系统首页
                        window.location.href = '/o2o/frontend/index';
                    }else {
                        window.location.href = '/o2o/shopadmin/shoplist';
                    }
                }else {
                    $.toast('登录失败！');
                    loginCount++;
                    if (loginCount >= 3)
                        $('#verifyPart').show();
                }
            }
        });
    });
});

function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return decodeURIComponent(r[2]);
    }
    return '';
};