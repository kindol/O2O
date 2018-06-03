$(function () {
    var shopId = getQueryString("shopId");
    var isEdit = shopId ? true : false;       //非空表示修改店铺信息、空则表示注册店铺
    //获取初始化信息，服务端内部转化URL
    var initUrl = '/o2o/shopadmin/getshopinitinfo';
    //填写完信息后的注册
    var registerShopUrl = '/o2o/shopadmin/registershop';
    var shopInfoUrl = "/o2o/shopadmin/getshopbyid?shopId=" + shopId;
    var editShopUrl = "/o2o/shopadmin/modifyshop";
    if (!isEdit){
        getShopInitInfo();
    } else {
        getShopInfo(shopId);
    }

    //通过ID获取shop信息
    function getShopInfo(shopId) {
        $.getJSON(shopInfoUrl, function (data) {
            if (data.success){
                var shop = data.shop;
                 $('#shop-name').val(shop.shopName);
                 $('#shop-addr').val(shop.shopAddr);
                 $('#shop-phone').val(shop.phone);
                 $('#shop-desc').val(shop.shopDesc);
                 var shopCategory = '<option data-id="'
                        + shop.shopCategory.shopCategoryId + '"selected>'
                        + shop.shopCategory.shopCategoryName + '</option>';
                 var tempAreaHtml = '';
                 data.areaList.map(function (item, index) {
                     tempAreaHtml += '<option data-id="' + item.areaId + '">'
                         + item.areaName + '</option>';
                 });
                $('#shop-category').html(shopCategory);
                $('#shop-category').attr('disabled', 'disabled');
                $('#area').html(tempAreaHtml);
                $('#area option[data-id="' + shop.area.areaId + '"]').attr('selected', 'selected');
            }
        });
    }

    //从后台获取商铺分类、区域信息
    function getShopInitInfo() {
        $.getJSON(initUrl, function (data){
            if (data.success) {
                var tempHtml = '';
                var tempAreaHtml = '';
                data.shopCategoryList.map(function (item, index) {
                    tempHtml += '<option data-id="' + item.shopCategoryId + '">'
                        + item.shopCategoryName + '</option>';
                });
                data.areaList.map(function (item, index) {
                    tempAreaHtml += '<option data-id="' + item.areaId + '">'
                        + item.areaName + '</option>';
                });
                $('#shop-category').html(tempHtml);
                $('#area').html(tempAreaHtml);
            }
        });
    }

    //点击提交之后进行商铺注册
    $('#submit').click(function () {
        var shop = {};
        if (isEdit) {
            shop.shopId = shopId;
        }
        shop.shopName = $('#shop-name').val();
        shop.shopAddr = $('#shop-addr').val();
        shop.phone = $('#shop-phone').val();
        shop.shopDesc = $('#shop-desc').val();
        //双重否定
        shop.shopCategory = {
            shopCategoryId: $('#shop-category').find('option').not(function () {
                return !this.selected;
            }).data('id')
        };
        shop.area = {
            areaId: $('#area').find('option').not(function () {
                return !this.selected;
            }).data('id')
        }
        var shopImg = $('#shop-img')[0].files[0];
        //使用formData辅助发送图片和类
        var formData = new FormData();
        formData.append('shopImg', shopImg);
        formData.append('shopStr', JSON.stringify(shop));
        //验证码
        var verifyCodeActual = $('#j-captcha').val();
        if (!verifyCodeActual){
            $.toast("请输入验证码！");
            return;
        }
        formData.append("verifyCodeActual", verifyCodeActual);

        $.ajax({
            url: isEdit ? editShopUrl : registerShopUrl,
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
                if (data.success){
                    //弹出信息
                    $.toast('提交成功！')
                } else {
                    $.toast('提交失败！' + data.errMsg);
                }
                $('#captcha-img').click();
            }
        })
    })
})