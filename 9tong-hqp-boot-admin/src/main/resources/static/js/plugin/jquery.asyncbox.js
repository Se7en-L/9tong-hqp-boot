(function($,w){

	var asyncbox = {};
	
	/**
	 * confirm 
	 */
	asyncbox.confirm = function(options){
		var defaults = { 
			text:'',									//提示内容
			oktext:'确定',								//确认按钮文案
			call:null									//回调函数(ok：代表点击确定按钮，cancel：代表点击取消按钮)
		} 	
		var options = $.extend(defaults,options);		
		
		$("body").append('<div class="asyncbox_cover"></div><div class="asyncbox"><div class="text"><h1>'+options.text+'</h1></div><div class="btn"><a href="javascript:void(0);" class="cancel">取消</a><a href="javascript:void(0);" class="ok">'+options.oktext+'</a></div></div>');
		
		$(".ok",'.asyncbox').click(function(){
			$('.asyncbox,.asyncbox_cover').remove();
			
			if(options.call instanceof Function){
				options.call('ok');
			}
		});

		$('.cancel','.asyncbox').click(function(){
			$('.asyncbox,.asyncbox_cover').remove();
			
			if(options.call instanceof Function){
				options.call('cancel');
			}
		});	
	};
	
	/**
	* alert
	*/
	asyncbox.alert = function(text){	
		$("body").append('<div class="asyncbox_cover"></div><div class="asyncbox"><div class="text"><h1>'+text+'</h1></div><div class="btn"><a href="javascript:void(0);" class="ok single_btn">确定</a></div></div>');
		
		$(".ok",'.asyncbox').click(function(){
			$('.asyncbox,.asyncbox_cover').remove();
		});		
	};

	w.asyncbox = asyncbox;
	
	/**
	* 自定义弹窗
	* 关闭弹窗：$.modal.close();
	*/
	$.fn.modal = function(options){
		var defaults = { 
			overlayClose : true,						//点击遮罩层是否关闭弹出窗体
			onOpen:null,								//弹出窗体打开时候的回调函数
			onShow:null,								//弹出窗体显示时候的回调函数
			onClose:null								//弹出窗体关闭时候的回调函数
		} 	
		var options = $.extend(defaults,options); 

		if(options.onOpen instanceof Function){
			options.onOpen();0
		}

		$.modal = $(this);

		var modal_width = $.modal.width()/2;

		var style = 'margin-left:-'+modal_width+'px';
		
		var content = $(this).prop("outerHTML");
		
		$("body").append('<div class="asyncbox_cover"></div><div class="modal" style="'+style+'">'+content+'</div>');
		
		if(options.onShow instanceof Function){
			options.onShow();
		}
		
		var box_close = function(){
			$('.modal,.asyncbox_cover').remove();
			if(options.onClose instanceof Function){
				options.onClose();
			}
		}
		
		$('.close_btn','.modal').click(function(){
			box_close();
		});	

		$.modal.close = function(){
			box_close();
		};
		
		if(options.overlayClose){
			$('.asyncbox_cover').click(function(){
				box_close();
			});	
		}
		
	};
	
})(jQuery,window);	