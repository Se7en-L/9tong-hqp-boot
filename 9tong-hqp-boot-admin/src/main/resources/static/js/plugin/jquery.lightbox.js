$(document).ready(function() {
    // 图片上下滚动
    var count = $(".image_menu li").length - 5; // 显示 6 个 li标签内容
    var interval = $(".image_menu li:first").width();
    var curIndex = 0;

    $('.scroll_button').click(function() {
        if ($(this).hasClass('disabled')) return false;

        if ($(this).hasClass('small_img_up'))--curIndex;
        else++curIndex;

        $('.scroll_button').removeClass('disabled');
        if (curIndex == 0) $('.small_img_up').addClass('disabled');
        if (curIndex == count - 1) $('.small_img_down').addClass('disabled');

        $(".image_menu ul").stop(false, true).animate({
            "marginLeft": -curIndex * interval + "px"
        },
        600);
    });
    // 解决 ie6 select框 问题
    $.fn.decorateIframe = function(options) {
        if ($.browser.msie && $.browser.version < 7) {
            var opts = $.extend({},
            $.fn.decorateIframe.defaults, options);
            $(this).each(function() {
                var $myThis = $(this);
                //创建一个IFRAME
                var divIframe = $("<iframe />");
                divIframe.attr("id", opts.iframeId);
                divIframe.css("position", "absolute");
                divIframe.css("display", "none");
                divIframe.css("display", "block");
                divIframe.css("z-index", opts.iframeZIndex);
                divIframe.css("border");
                divIframe.css("top", "0");
                divIframe.css("left", "0");
                if (opts.width == 0) {
                    divIframe.css("width", $myThis.width() + parseInt($myThis.css("padding")) * 2 + "px");
                }
                if (opts.height == 0) {
                    divIframe.css("height", $myThis.height() + parseInt($myThis.css("padding")) * 2 + "px");
                }
                divIframe.css("filter", "mask(color=#fff)");
                $myThis.append(divIframe);
            });
        }
    }
    $.fn.decorateIframe.defaults = {
        iframeId: "decorateIframe1",
        iframeZIndex: -1,
        width: 0,
        height: 0
    }
    //放大镜视窗
    $(".big_view").decorateIframe();
    //点击到中图
    var midChangeHandler = null;

    $(".image_menu li img").bind("click",
    function() {
        if ($(this).attr("class") != "onlick_img") {
            midChange($(this).attr("src").replace("small", "mid"));
            $(".image_menu li").removeAttr("class");
            $(this).parent().attr("class", "onlick_img");
        }
    }).bind("click",
    function() {
        if ($(this).attr("class") != "onlick_img") {
            window.clearTimeout(midChangeHandler);
            midChange($(this).attr("src").replace("small", "mid"));
            $(this).css({
                "border": "3px solid #959595"
            });
        }
    }).bind("mouseout",
    function() {
        if ($(this).attr("class") != "onlick_img") {
            $(this).removeAttr("style");
            midChangeHandler = window.setTimeout(function() {
                midChange($(".onlick_img img").attr("src").replace("small", "mid"));
            },
            1000);
        }
    });
    function midChange(src) {
        $(".mid_img").attr("src", src).load(function() {
            changeViewImg();
        });
    }
    //大视窗看图
    function mouseover(e) {
        if ($(".win_selector").css("display") == "none") {
            $(".win_selector,.big_view").show();
        }
        $(".win_selector").css(fixedPosition(e));
        e.stopPropagation();
    }
    function mouseOut(e) {
        if ($(".win_selector").css("display") != "none") {
            $(".win_selector,.big_view").hide();
        }
        e.stopPropagation();
    }
    $(".mid_img").mouseover(mouseover); //中图事件
    $(".mid_img,.win_selector").mousemove(mouseover).mouseout(mouseOut); //选择器事件
    var $divWidth = $(".win_selector").width(); //选择器宽度
    var $divHeight = $(".win_selector").height(); //选择器高度
    var $imgWidth = $(".mid_img").width(); //中图宽度
    var $imgHeight = $(".mid_img").height(); //中图高度
    var $viewImgWidth = $viewImgHeight = $height = null; //IE加载后才能得到 大图宽度 大图高度 大图视窗高度
    function changeViewImg() {
        $(".big_view img").attr("src", $(".mid_img").attr("src").replace("mid", "big"));
    }
    changeViewImg();
    $(".big_view").scrollLeft(0).scrollTop(0);
    function fixedPosition(e) {
        if (e == null) {
            return;
        }
        var $imgLeft = $(".mid_img").offset().left; //中图左边距
        var $imgTop = $(".mid_img").offset().top; //中图上边距
        //selector顶点坐标 X
        X = e.pageX - $imgLeft - $divWidth / 2;
        //selector顶点坐标 Y
        Y = e.pageY - $imgTop - $divHeight / 2;
        X = X < 0 ? 0 : X;
        Y = Y < 0 ? 0 : Y;
        X = X + $divWidth > $imgWidth ? $imgWidth - $divWidth: X;
        Y = Y + $divHeight > $imgHeight ? $imgHeight - $divHeight: Y;

        if ($viewImgWidth == null) {
            $viewImgWidth = $(".big_view img").outerWidth();
            $viewImgHeight = $(".big_view img").height();
            if ($viewImgWidth < 200 || $viewImgHeight < 200) {
                $viewImgWidth = $viewImgHeight = 800;
            }
            $height = $divHeight * $viewImgHeight / $imgHeight;
            $(".big_view").width($divWidth * $viewImgWidth / $imgWidth);
            $(".big_view").height($height);
        }
        var scrollX = X * $viewImgWidth / $imgWidth;
        var scrollY = Y * $viewImgHeight / $imgHeight;
        $(".big_view img").css({
            "left": scrollX * -1,
            "top": scrollY * -1
        });
        $(".big_view").css({
            "top": 75,
            "left": $(".preview").offset().left + $(".preview").width() + 15
        });

        return {
            left: X,
            top: Y
        };
    }
});