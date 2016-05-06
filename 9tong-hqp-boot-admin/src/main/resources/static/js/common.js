// JavaScript Document

$(function(){
	
	$('.wrap').css('height',$(window).height()+'px');						//页面高度
	
	//导航收缩展开
	var w_w = $(window).width();
	if( w_w < 1300 ){
		$('.all_menu').hide();
		$('.mini_menu').addClass('contraction');
	}else{
		$('.all_menu').show();
		$('.mini_menu').removeClass('contraction');
	};
	
	//导航收缩展开-点击
	$('.mini_menu').live('click',function(){
		if($('.all_menu').is(':hidden')){
			$(this).siblings('.all_menu').show();
			$(this).removeClass('contraction');
		}else{
			$(this).siblings('.all_menu').hide();
			$(this).addClass('contraction');
		};
	});
	
	//二级导航显示
	$('.com_menu .a_menu').live('click',function(){
		
		if($(this).siblings('ul').is(':visible')){
			$(this).siblings('ul').hide(200,function(){						//收缩当前导航
				$(this).parent('li').removeClass('on');						//改变箭头方向
			 });						
		}else{
			$(this).siblings('ul').show(200,function(){						//收缩当前导航
				$(this).parent('li').addClass('on');						//改变箭头方向
			 });
			 
			$(this).parent().siblings('li').removeClass('on');				//同节点改变箭头方向
			$(this).parent().siblings('li').find('ul').hide(200);			//同节点二级导航收缩
		};
	
	});
	
	//弹窗显示
	$('.com_box_show_btn').live('click',function(){
		var w_h = $(window).height();										//获取内容高度

		$('.com_box_bg,.com_box').css('height',w_h + 'px').show();			//遮罩高度和显示
		$('.com_box').removeClass('hide').animate({right:'0px'},"fast");	//弹窗推出
		$('body').css('overflow-y','hidden');
	});
	
	//弹窗隐藏
	$('.com_box_bg,.com_box .close').live('click',function(){
		$('.com_box').addClass('hide').animate({right:'-850px'},"fast");	//弹窗隐藏
		$('.com_box_bg').hide();											//遮罩隐藏
		$('body').css('overflow-y','auto');
	});
	
	//表格-鼠标经过
	$('table tr').live('mouseenter',function(){
		$(this).addClass('hover');
	}).live('mouseleave',function(){
		$(this).removeClass('hover');
	});
	
	//表格-全选删除滑出
	$('table input[type="checkbox"]').live('click',function(){
		$('.table_title_txt').addClass('hide');
		$('.table_title_checkbox').show(120);
	});
	
	//tab切换
	$('.tab_btn a').live('click',function(){
		$(this).addClass('on').siblings().removeClass('on');
		$('.tab_a_info').eq($(this).index()).removeClass('hide').siblings().addClass('hide');
	});
	
});


//覆盖alert提示
function alert(text){
	$('body').append('<div class="pointout_bg">'+text+'</div>');
	
	var w_width = $(window).width();
	var txt_width = $('.pointout_bg').width()/2;
	var box_auto = w_width/2 - txt_width - 15;
	$('.pointout_bg').css('left',box_auto+'px');
	$('.pointout_bg').fadeIn().delay(900).fadeOut(1000,function(){				
		$('.pointout_bg').remove();				
	});	
};

//asyncbox提示
function asyncbox_tips(content,ok_content){
	var options = { 
		text:content,														//提示文案
		oktext:ok_content,													//按钮文案
		call:function(action){
																			//回调写这里
		}
	} 		
	asyncbox.confirm(options)	
};