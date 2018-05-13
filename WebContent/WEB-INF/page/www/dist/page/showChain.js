$(function () {
	imgpanel = $('#imgpanel');
	//dialog = $('#blockDialog');
	curHeight = 0;
	preHeight = 0;
	totalPanelWeight = 750;
	imageWeight = 50;
	totalCountBlock = 15;
	pageMoveBlock = [];
	bufferBlock = [];
	newBufferBlock=new Map();
	firstQuery = 'true';
	numflag = 0;
	flag = true;
	//maxShowBlock=10;
	index = 0;
	first = 0;

	//
	imageId = new Map();

	// 	leftpx = (document.body.clientWidth - totalCountBlock * 50) / 2
	//	+ (totalCountBlock - 1) * 50;
	//leftpx = (document.body.clientWidth - totalCountBlock * 50) / 2;
	leftpx = (document.body.clientWidth - totalCountBlock * 50) / 2 + totalCountBlock * 50;

	autoQueryChainBlock();

	drawChart();
});

function autoQueryChainBlock() {
	queryChainHeight();

	if (curHeight <= 0) {
		return;
	}

	first = (curHeight - totalCountBlock);
	
	//alert(preHeight+" "+curHeight);
	for (var i = (curHeight - totalCountBlock); i < curHeight; i++) {
		console.log(i);
		if (i < 0) {
			i = 0;
			first = 0;
		}
//		queryBlockInfo(i);
	}

	leftpx = (document.body.clientWidth -(curHeight-first) * 50) / 2 + (curHeight-first) * 50

	preHeight = curHeight;
	index=first;

	queryBlock(curHeight - 1);


	//showAllBlock();
	//index = curHeight - first;
	setInterval(function(){
		queryChainHeight();
		
		preHeight=curHeight;
	}, 15000);
}

function getBlockByHash(targetHash) {
	var param = { hash: targetHash }
	window.post('getBlockByHash',param,function(data){
		alert(JSON.stringify(data));
	},false);
}

function refreshChain() {

	location.reload();
	/*
	queryChainHeight();
	for (var i = preHeight; i < curHeight; i++) {
		queryBlockInfo(i);
	}
	preHeight = curHeight;

	queryBlock(curHeight - 1);
	*/
}

function search() {
	var text = $("input[name='searchContent']").val();
	if (text == undefined || text == "") {
		alert("Wrong Block number.");
		return;
	}
	var n = parseInt(text);
	if (isNaN(n) || n < 0 || n >= curHeight || n == undefined) {
		alert("Wrong Block number.");
		return;
	}
	queryBlock(n);
}

function searchByHash() {

	var hash = $("input[name='searchContent2']").val();

	if (hash == undefined || hash == "") {
		alert("Wrong Hash.");
		return;
	}
	getBlockByHash(hash);
}

function queryBlock(height) {

	var param =  { curHeight: height };
	window.post('queryBlockInfoHeight',param,function(rs){
		
		if (rs == "failed") {
			alert("get block info failed");
			return;
		}

		newBufferBlock.set(height,rs);

		$("#blcHeight").text(rs.height);
		$("#create_time").text(rs.create_ts);
		$("#curHash").text(rs.cur_hash);
		$("#preHash").text(rs.pre_hash);

		var len = rs.count;
		var table = $("#tab1");
		table.empty();

		for (var i = 0; i < len; i++) {
			//console.log(JSON.stringify(rs.txdata[i]));
			if (rs.txdata[i].docType == "nostro") {

				table.append("<tr><td><strong>银&nbsp;&nbsp;行&nbsp;&nbsp;号: </strong><p id=\"instrId_" + i + "\">" + rs.txdata[i].actid + "</p></td><td></td>"
					+ "</tr><tr><td><strong>转&nbsp;&nbsp;出&nbsp;&nbsp;行: </strong><p id=\"bkcode\">" + rs.txdata[i].bkcode + "</p></td><td><strong>转&nbsp;&nbsp;入&nbsp;&nbsp;行: </strong>"
					+ "<p id=\"clrbkcde_" + i + "\">" + rs.txdata[i].clrbkcde + "</p></td></tr><tr><td><strong>余&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;额: </strong>"
					+ "<p id=\"txamt_" + i + "\">" + rs.txdata[i].nostrobal
					+ "<td><strong>货&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;币: </strong><p id=\"curcde_" + i + "\">" + rs.txdata[i].curcde + "</p></td></tr>"
					+ "<tr><td><br></td><td></td></tr>");

			} else {
				table.append("<tr><td><strong>交&nbsp;&nbsp;易&nbsp;&nbsp;号: </strong><p id=\"instrId_" + i + "\">" + rs.txdata[i].instrId + "</p></td>"
					+ "<td><strong>日&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;期: </strong><p id=\"txndat\">" + rs.txdata[i].txndat + "</p></td></tr>"
					+ "<tr><td><strong>转&nbsp;&nbsp;出&nbsp;&nbsp;行: </strong><p id=\"bkcode\">" + rs.txdata[i].bkcode + "</p></td><td><strong>转&nbsp;&nbsp;入&nbsp;&nbsp;行: </strong>"
					+ "<p id=\"clrbkcde_" + i + "\">" + rs.txdata[i].clrbkcde + "</p></td></tr><tr><td><strong>转出账户: </strong>"
					+ "<p id=\"actnofrom_" + i + "\">" + rs.txdata[i].actnofrom + "</p></td><td><strong>转入账户: </strong>"
					+ "<p id=\"actnoto_" + i + "\">" + rs.txdata[i].actnoto + "</p></td></tr><tr><td><strong>金&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;额: </strong>"
					+ "<p id=\"txamt_" + i + "\">" + rs.txdata[i].txamt + "<td><strong>状&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;态: </strong>"
					+ "<p id=\"compst_" + i + "\">" + rs.txdata[i].compst + "</p></td>"
					+ "<tr><td><strong>货&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;币: </strong><p id=\"curcde_" + i + "\">" + rs.txdata[i].curcde + "</p></td><td></td></tr>"
					+ "<tr><td><br></td><td></td></tr>");

				if (rs.txdata[i].compst == "S") {
					$("#compst_" + i).text("成功");
				} else if (rs.txdata[i].compst == "P") {
					$("#compst_" + i).text("等待");
				} else if (rs.txdata[i].compst == "R") {
					$("#compst_" + i).text("拒纳");
				} else if (rs.txdata[i].compst == "N") {
					$("#compst_" + i).text("新建");
				}
			}
		}
	},false);
	
}

function queryChainHeight() {
	var param= {};
	window.post('queryLastBlock',param,function(rs){
		curHeight = rs.height;
	},false);
}

function queryBlockInfo(height) {
	var param ={ curHeight: height };
	window.post('queryBlockInfoHeight',param,function (rs) {
		if (rs == "failed") {
			alert("get block info failed");
			return;
		}
		//bufferBlock.push(rs);
		//if(!newBufferBlock.has(height))
		newBufferBlock.set(height,rs);
	},false);
	
}

function drawChart() {

	var drawInterval = setInterval(function () {
		moveChart();
		//console.log(curHeight);
	}, 1000);
	//	imgpanel.hover(function() {
	//		clearInterval(drawInterval);
	//	}, function() {
	//		drawInterval = setInterval(function() {
	//			moveChart();
	//		}, 1000);
	//	});
}

function showAllBlock() {
	for (var i = 0; i < (preHeight - first); i++) {
		//var firstBlock = bufferBlock[i];
		
		var height =first+i; //firstBlock.height;

		//console.log(height);
		//console.log(""+i+"  "+ firstBlock.create_ts);
		//var blockCreateTime = firstBlock.create_ts;
		
		//var cTime = blockCreateTime.split(' ',4);
		//var time = cTime[0] + " " + cTime[1] + " " + cTime[2] + " " + cTime[3];

		if (height == 0) {
			imgpanel.append("<div id='img"
				+ 0
				+ "_"
				+ 1
				+ "' style=\"width:50px;height:78px;position:absolute;left:"
				+ leftpx
				+ "px;display:none;background:url(../images/firstBlock_1.png)\">&nbsp;创世区块</div>");
			imgpanel.append("<div id='img"
				+ 0
				+ "_"
				+ 2
				+ "' style=\"width:50px;height:78px;position:absolute;left:"
				+ leftpx
				+ "px;display:none;background:url(../images/firstBlock_2.png)\">&nbsp;创世区块</div>");
		} else {
			imgpanel.append("<div id='img"
				+ height
				+ "_" + 1
				+ "'  style=\"width:50px;height:78px;position:absolute;left:"
				+ leftpx
				+ "px;display:none;background:url(../images/lastBlock.png)\"><a style=\"color:#fff;text-decoration:none;margin-left:10px;font-size:10px\">"
				+ height + "<br/><br/><br/>" //+ "&nbsp;" + time
				+ "</a></div>");
			imgpanel.append("<div id='img"
				+ height
				+ "_"
				+ 2
				+ "'  style=\"width:50px;height:78px;position:absolute;left:"
				+ leftpx
				+ "px;display:none;background:url(../images/middleBlock.png)\"><a style=\"color:#fff;text-decoration:none;margin-left:10px;font-size:10px\">"
				+ height + "<br/><br/><br/>" // + "&nbsp;" + time
				+ "</a></div>");
			$("#img" + height + "_1").on("click", displayBlockInfo);
			$("#img" + height + "_2").on("click", displayBlockInfo);
		}

		leftpx += 50;
		pageMoveBlock.push(height);

		imageId.set(height,"#img" + height + "_2"); //imageId.push("#img" + height + "_2");
		//mouseOver("#img" + height + "_1", '../images/lastBlock.png');
		//mouseOut("#img" + height + "_1", '../images/lastBlock.png');
		mouseOver("#img" + height + "_2", '../images/middleBlock_new.png');
		mouseOut("#img" + height + "_2", '../images/middleBlock.png');

	}
	leftpx -= 50;
	$('#imgpanel div').show();
}

function mouseOver(element, imageUrl) {
	$(element).mouseover(function (e) {

		$(this).css('background', 'url(' + imageUrl + ')');
		$(this).fadeTo(200, 1);
		$(this).css('width', '58px');
		$(this).css('border-radius', '10px');
		$(this).css('cursor', 'pointer');
		$(this).css('height', '60px');
		$(this).show();
	});
}

function mouseOut(element, imageUrl) {
	$(element).mouseout(function () {

		$(this).css('background', 'url(' + imageUrl + ')');
		$(this).fadeTo(200, 1);
		$(this).css('width', '50px');
		$(this).css('height', '78px');
	});
}

function moveChart() {
	if (index < preHeight) { //(preHeight - first)
		// var firstBlock = bufferBlock.shift();
		//if(!newBufferBlock.has(index))
		//	queryBlockInfo(index);
		//var firstBlock = newBufferBlock.get(index++);//bufferBlock[index++];
		
		var height = index++;//firstBlock.height;

		console.log(height);

		//var blockCreateTime = firstBlock.create_ts;
		//var cTime = blockCreateTime.split(' ', 4);
		//var time = cTime[0] + " " + cTime[1] + " " + cTime[2] + " " + cTime[3];
		// 添加新的区块
		// var leftpx = 1000;


		// 判断标志位，清除定时器

		if (height == 0) {//position:absolute;
			imgpanel.append("<div id='img"
				+ 0
				+ "_"
				+ 1
				+ "' style=\"width:50px;height:78px;position:absolute;left:"
				+ leftpx
				+ "px;display:none;background:url(../images/firstBlock_1.png)\">&nbsp;创世区块</div>");
			imgpanel.append("<div id='img"
				+ 0
				+ "_"
				+ 2
				+ "' style=\"width:50px;height:78px;position:absolute;left:"
				+ leftpx
				+ "px;display:none;background:url(../images/firstBlock_2.png)\">&nbsp;创世区块</div>");
		} else {
			imgpanel.append("<div id='img"
				+ height
				+ "_"
				+ 1
				+ "'  style=\"width:50px;height:78px;position:absolute;left:"
				+ leftpx
				+ "px;display:none;background:url(../images/lastBlock.png)\"><a style=\"color:#fff;text-decoration:none;margin-left:10px;font-size:10px\">"
				+ height + "<br/><br/><br/>" //+ "&nbsp;" + time
				+ "</a></div>");
			imgpanel.append("<div id='img"
				+ height
				+ "_"
				+ 2
				+ "'  style=\"width:50px;height:78px;position:absolute;left:"
				+ leftpx
				+ "px;display:none;background:url(../images/middleBlock.png)\"><a style=\"color:#fff;text-decoration:none;margin-left:10px;font-size:10px\">"
				+ height + "<br/><br/><br/>" //+ "&nbsp;" + time
				+ "</a></div>");
			$("#img" + height + "_1").on("click", displayBlockInfo);
			$("#img" + height + "_2").on("click", displayBlockInfo);

		}

		//leftpx-=50;

		// 区块动画效果
		var sliptTime = 1000;
		if (pageMoveBlock.length > 0) {
			// 第一块图片切换
			if (pageMoveBlock.length >= totalCountBlock) {
				$("#img" + pageMoveBlock[0] + "_1").fadeOut();
				$("#img" + pageMoveBlock[0] + "_2").fadeOut();
			}

			// 获取页面上区块元素ID, 整体移动 imageWeight
			pageMoveBlock.forEach(function (item, i) {
				$("#img" + item + "_1").animate({
					left: '-=' + imageWeight + 'px'
				});
				$("#img" + item + "_2").animate({
					left: '-=' + imageWeight + 'px'
				});
			});

			// 旧有区块最后一块图片切换
			$("#img" + pageMoveBlock[pageMoveBlock.length - 1] + "_1").fadeOut();
			$("#img" + pageMoveBlock[pageMoveBlock.length - 1] + "_2").fadeIn();
		}

		// 新区块淡入效果
		$("#img" + height + "_1").delay(sliptTime * i).fadeIn(sliptTime / 2);
		$("#img" + height + "_2").delay(sliptTime * i).fadeOut(sliptTime / 2);
		pageMoveBlock.push(height);

		if (pageMoveBlock.length > totalCountBlock) {
			var pageMoveBlock_remove = pageMoveBlock.length - totalCountBlock;
			for (var i = 0; i < pageMoveBlock_remove; i++) {
				pageMoveBlock.shift();
			}
		}
		
		$("#img" + height + "_2").fadeTo(10,1);
		
		imageId.set(height,"#img" + height + "_2");//imageId.push("#img" + height + "_2");
		mouseOver("#img" + height + "_2", '../images/middleBlock_new.png');
		mouseOut("#img" + height + "_2", '../images/middleBlock.png');
	}
}

function displayBlockInfo() {
	var imgid = $(this).attr("id");
	var height = imgid.substr(3, imgid.length - 5);

	$("#blcHeight").text(height);

	if(!newBufferBlock.has(height)){
		queryBlock(height);
	}
	else{
	//$("#create_time").text(bufferBlock[height - first].create_ts);
	//$("#curHash").text(bufferBlock[height - first].cur_hash);
	//$("#preHash").text(bufferBlock[height - first].pre_hash);
	}
	
	//$("#create_time").text(bufferBlock[height - first].create_ts);
	//$("#curHash").text(bufferBlock[height - first].cur_hash);
	//$("#preHash").text(bufferBlock[height - first].pre_hash);

	$(imgid).unbind("mouseover");
	$(imgid).unbind("mouseout");

	if (imageId.length > 0) {
		for (var i = first; i != height, i < imageId.length; i++) {

			$(imageId.get(i)).fadeTo(200, 1)
			$(imageId.get(i)).css('background', 'url(../images/middleBlock.png)');
			mouseOut(imageId.get(i), '../images/middleBlock.png')
		}
		$(imageId.get(height)).unbind("mouseout");
		$(imageId.get(height)).css('background', 'url(../images/middleBlock_new.png)');

		$(imageId.get(height)).fadeTo(200, 1);
	}



	var len = newBufferBlock.get(height).count;//bufferBlock[height - first].count;
	var table = $("#tab1");
	table.empty();
	for (var i = 0; i < len; i++) {
		//alert(bufferBlock[height-first].txdata[i].docType);
		if (newBufferBlock.get(height).txdata[i].docType == "nostro") {//bufferBlock[height-first].txdata[i].docType=="nostro"
			table.append("<tr><td><strong>银&nbsp;&nbsp;行&nbsp;&nbsp;号: </strong><p id=\"instrId_" + i + "\">" + newBufferBlock.get(height).txdata[i].actid + "</p></td><td></td>"
				+ "</tr><tr><td><strong>转&nbsp;&nbsp;出&nbsp;&nbsp;行: </strong><p id=\"bkcode\">" + newBufferBlock.get(height).txdata[i].bkcode + "</p></td><td><strong>转&nbsp;&nbsp;入&nbsp;&nbsp;行: </strong>"
				+ "<p id=\"clrbkcde_" + i + "\">" + newBufferBlock.get(height).txdata[i].clrbkcde + "</p></td></tr><tr><td><strong>余&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;额: </strong>"
				+ "<p id=\"txamt_" + i + "\">" + newBufferBlock.get(height).txdata[i].nostrobal
				+ "<td><strong>货&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;币: </strong><p id=\"curcde_" + i + "\">" + newBufferBlock.get(height).txdata[i].curcde + "</p></td></tr>"
				+ "<tr><td><br></td><td></td></tr>");

		} else {

			table.append("<tr><td><strong>交&nbsp;&nbsp;易&nbsp;&nbsp;号: </strong><p id=\"instrId_" + i + "\">" + newBufferBlock.get(height).txdata[i].instrId + "</p></td>"
				+ "<td><strong>日&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;期: </strong><p id=\"txndat_" + i + "\">" + newBufferBlock.get(height).txdata[i].txndat + "</p></td></tr>"
				+ "<tr><td><strong>转&nbsp;&nbsp;出&nbsp;&nbsp;行: </strong><p id=\"bkcode\">" + newBufferBlock.get(height).txdata[i].bkcode + "</p></td><td><strong>转&nbsp;&nbsp;入&nbsp;&nbsp;行: </strong>"
				+ "<p id=\"clrbkcde_" + i + "\">" + newBufferBlock.get(height).txdata[i].clrbkcde + "</p></td></tr><tr><td><strong>转出账户: </strong>"
				+ "<p id=\"actnofrom_" + i + "\">" + newBufferBlock.get(height).txdata[i].actnofrom + "</p></td><td><strong>转入账户: </strong>"
				+ "<p id=\"actnoto_" + i + "\">" + newBufferBlock.get(height).txdata[i].actnoto + "</p></td></tr><tr><td><strong>金&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;额: </strong>"
				+ "<p id=\"txamt_" + i + "\">" + newBufferBlock.get(height).txdata[i].txamt + "</p></td><td><strong>状&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;态: </strong>"
				+ "<p id=\"compst_" + i + "\">" + "</p></td></tr>" + "<tr><td><strong>货&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;币: </strong>" +
				"<p id=\"curcde_" + i + "\">" + newBufferBlock.get(height).txdata[i].curcde + "</p></td><td></td></tr>"
				+ "<tr><td><br></td><td></td></tr>");


			if (newBufferBlock.get(height).txdata[i].compst == "S") {
				$("#compst_" + i).text("成功");
			} else if (newBufferBlock.get(height).txdata[i].compst == "P") {
				$("#compst_" + i).text("等待");
			} else if (newBufferBlock.get(height).txdata[i].compst == "R") {
				$("#compst_" + i).text("拒纳");
			} else if (newBufferBlock.get(height).txdata[i].compst == "N") {
				$("#compst_" + i).text("新建");
			}

		}
	}

}



