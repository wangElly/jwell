$(function() {
	// 初始化tip标签显示
	$('[data-toggle="tooltip"]').tooltip();

	// 初始化sidebar左侧滚动条
	$("#sidebar .nano").nanoScroller();

	// 显示/隐藏子菜单	
	$("#sidebar .first li a[data-toggle='subclass']").click(function() {
		var currType = $(this).attr("curr-type");
		if (currType === undefined || currType == "false") {
			$(this).parent().addClass("selected");
			$(this).attr("curr-type", true);
			$(this).siblings(".second").slideDown(showScroll);
		} else {
			$(this).parent().removeClass("selected");
			$(this).attr("curr-type", false);
			$(this).siblings(".second").slideUp(showScroll);
		}
	});

	// 显示sidebar左侧滚动条
	// 调用window的resize事件，触发NanoScroll.reset()函数
	function showScroll(){
		$(window).resize();
	}
});