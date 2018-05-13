//************************  
//******航线自动切换*********  
//************************  
  
$(document).ready(function(){  
    auto_switch_timer();  
});  
  
//用来选中的节点，因为默认是 “北京 Top10” 先选中的，默认为select0  
//'北京 Top10', '上海 Top10', '广州 Top10'  
var select0 = {  
    '北京 Top10' : true,  
    '上海 Top10' : false,  
    '广州 Top10' : false  
};  
var select1 = {  
    '北京 Top10' : false,  
    '上海 Top10' : true,  
    '广州 Top10' : false  
};  
var select2 = {  
    '北京 Top10' : false,  
    '上海 Top10' : false,  
    '广州 Top10' : true  
};  
  
  
var restTime = 2;// 2秒切换一次  
var switch_index = 1;//激活的select变量，因为0是默认的，所以从1开始  
var switch_count = 3;//总共有多少个 select变量  
  
/* 倒计时js */  
function auto_switch_timer() {  
    // debugger;  
    var intDiff = parseInt(restTime);// 倒计时总秒数量  
    window.setInterval(function() {  
        if (intDiff > 0) {  
            intDiff--;  
        } else {  
            intDiff = parseInt(restTime);  
  
            // 获取 option 里的 legend 参数  
            var legend = option.legend;  
              
            // 判断是不是激活最后一个selectX变量，是，下次就从 select0 开始  
            if (switch_index == switch_count) {  
                switch_index = 0;  
            }  
  
            switch (switch_index) {  
            case 0:  
                legend.selected = select0;  
                break;  
            case 1:  
                // selected 是当前选中的  
                legend.selected = select1;  
                break;  
            case 2:  
                legend.selected = select2;  
                break;  
            }  
            // 设置 option  
            option.legend = legend;  
            // 重新加载 option，因为修改了局部，所以不会出现整个页面刷新  
            myChart.setOption(option);  
  
            switch_index++;  
        }  
    }, 20);  
}  