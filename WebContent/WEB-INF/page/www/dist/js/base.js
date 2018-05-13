var basePath='http://172.21.64.213:8001/'
var token=sessionStorage.getItem("token");
window.post = function(path, param, fnSuccess, async){
    if (async == undefined) {
        async = true;
    }
    $.ajax({
        type:"post",
        url:basePath+path,
        data:JSON.stringify(param),
        async: async,
        beforeSend:function(xhr){
            xhr.setRequestHeader('Authorization', "Bearer "+token);
        },
        contentType:"application/json",
        success: function(res){
            if (!fnSuccess) return false;
          // console.log(res);
            if (res) {
                fnSuccess(res);
            }
        },
        error: function (res) {
            console.log("%c isError : " + path , res.msg);
        }
    });
}


window.formPost = function(path, param, fnSuccess, async){
    if (async == undefined) {
        async = true;
    }
    $.ajax({
        type:"post",
        url:basePath+path,
        data: param,
        async: async,
        beforeSend:function(xhr){
            xhr.setRequestHeader('Authorization', "Bearer "+token);
        },
        contentType:false,
        processData : false, 
        success: function(res){
            if (!fnSuccess) return false;
          // console.log(res);
            if (res) {
                fnSuccess(res);
            }
        },
        error: function (res) {
            console.log("%c isError : " + path , res.msg);
        }
    });
}
