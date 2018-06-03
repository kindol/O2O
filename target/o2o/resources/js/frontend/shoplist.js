$(function() {
    var loading = false;
    //分页允许访问的最大条数，超过此条数则进制访问后台
    var maxItems = 999;
    //一页的最大条数
    var pageSize = 3;
    //获取店铺列表的URL
    var listUrl = '/o2o/frontend/listshops';
    //获取店铺类别以及区域列表的URL
    var searchDivUrl = '/o2o/frontend/listshopspageinfo';
    //页码
    var pageNum = 1;
    //从地址栏URL尝试获取parent，shop，categoryId
    var parentId = getQueryString('parentId');
    var areaId = '';
    var shopCategoryId = '';
    var shopName = '';

    getSearchDivData();
    addItems(pageSize, pageNum);

    //渲染店铺列表以及区域列表信息
    function getSearchDivData() {
        var url = searchDivUrl + '?' + 'parentId=' + parentId;
        $
            .getJSON(
                url,
                function(data) {
                    if (data.success) {
                        var shopCategoryList = data.shopCategoryList;
                        var html = '';
                        html += '<a href="#" class="button" data-category-id=""> 全部类别  </a>';
                        shopCategoryList
                            .map(function(item, index) {
                                html += '<a href="#" class="button" data-category-id='
                                    + item.shopCategoryId
                                    + '>'
                                    + item.shopCategoryName
                                    + '</a>';
                            });
                        $('#shoplist-search-div').html(html);
                        var selectOptions = '<option value="">全部街道</option>';
                        var areaList = data.areaList;
                        areaList.map(function(item, index) {
                            selectOptions += '<option value="'
                                + item.areaId + '">'
                                + item.areaName + '</option>';
                        });
                        $('#area-search').html(selectOptions);
                    }
                });
    }

    // 预先加载pageSize条店铺信息
    function addItems(pageSize, pageIndex) {
        // 拼接出查询的URL，赋空值就去掉条件限制
        var url = listUrl + '?' + 'pageIndex=' + pageIndex + '&pageSize='
            + pageSize + '&parentId=' + parentId + '&areaId=' + areaId
            + '&shopCategoryId=' + shopCategoryId + '&shopName=' + shopName;
        //设定加载符，若还在后台取数据则不能再次访问后台，避免多次加载
        loading = true;
        $.getJSON(url, function(data) {
            if (data.success) {
                //获取当前查询条件下店铺的总数
                maxItems = data.count;
                var html = '';
                //遍历店铺列表，拼接出卡片
                data.shopList.map(function(item, index) {
                    html += '' + '<div class="card" data-shop-id="'
                        + item.shopId + '">' + '<div class="card-header">'
                        + item.shopName + '</div>'
                        + '<div class="card-content">'
                        + '<div class="list-block media-list">' + '<ul>'
                        + '<li class="item-content">'
                        + '<div class="item-media">' + '<img src="'
                        + item.shopImg + '" width="44">' + '</div>'
                        + '<div class="item-inner">'
                        + '<div class="item-subtitle">' + item.shopDesc
                        + '</div>' + '</div>' + '</li>' + '</ul>'
                        + '</div>' + '</div>' + '<div class="card-footer">'
                        + '<p class="color-gray">'
                        + new Date(item.lastEditTime).Format("yyyy-MM-dd")
                        + '更新</p>' + '<span>点击查看</span>' + '</div>'
                        + '</div>';
                });
                $('.list-div').append(html);
                //获取目前已显示的卡片总数，包含之前已经加载的
                var total = $('.list-div .card').length;
                //若总数达到按此查询条件获得的总数，则停止后台加载
                if (total >= maxItems) {
                    $('.infinite-scroll-preloader').hide();
                }else
                    $('.infinite-scroll-preloader').show();
                //页码+1，继续load出新的店铺
                pageNum += 1;
                //加载结束，可以再次加载了
                loading = false;
                //刷新店铺，显示新加载的店铺
                $.refreshScroller();
            }
        });
    }

    function getQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) {
            return decodeURIComponent(r[2]);
        }
        return '';
    }

    //下滑屏幕自动进行分页搜索
    $(document).on('infinite', '.infinite-scroll-bottom', function() {
        if (loading)
            return;
        addItems(pageSize, pageNum);
    });

    //点击卡片进入店铺的详情页
    $('.shop-list').on('click', '.card', function(e) {
        var shopId = e.currentTarget.dataset.shopId;
        window.location.href = '/o2o/frontend/shopdetail?shopId=' + shopId;
    });

    //选择新的店铺类别后，重置页码，清空原先的店铺列表，按照新的类别去查询
    $('#shoplist-search-div').on(
        'click',
        '.button',
        function(e) {
            if (parentId) {// 如果传递过来的是一个父类下的子类
                shopCategoryId = e.target.dataset.categoryId;
                //若之前选择了别的category，则移除其选定效果
                if ($(e.target).hasClass('button-fill')) {
                    $(e.target).removeClass('button-fill');
                    shopCategoryId = '';
                } else {
                    $(e.target).addClass('button-fill').siblings()
                        .removeClass('button-fill');
                }
                //由于查询条件改变，清空店铺列表再进行查询
                $('.list-div').empty();
                //重置页码
                pageNum = 1;
                addItems(pageSize, pageNum);
            } else {// 如果传递过来的父类为空，则按照父类查询
                parentId = e.target.dataset.categoryId;
                if ($(e.target).hasClass('button-fill')) {
                    $(e.target).removeClass('button-fill');
                    parentId = '';
                } else {
                    $(e.target).addClass('button-fill').siblings()
                        .removeClass('button-fill');
                }
                $('.list-div').empty();
                pageNum = 1;
                addItems(pageSize, pageNum);
                parentId = '';
            }

        });

    //需要查询的名字发生变化，重置页码，清空原先的店铺列表，按照新的名字加载信息
    $('#search').on('change', function(e) {
        shopName = e.target.value;
        $('.list-div').empty();
        pageNum = 1;
        addItems(pageSize, pageNum);
    });

    $('#area-search').on('change', function() {
        areaId = $('#area-search').val();
        $('.list-div').empty();
        pageNum = 1;
        addItems(pageSize, pageNum);
    });

    $('#me').click(function() {
        $.openPanel('#panel-left-demo');
    });

    $.init();
});
