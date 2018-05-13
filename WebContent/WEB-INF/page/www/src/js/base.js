

var basePath='http://127.0.0.1:8080/FileSharePoc/'
//basePath填写后台服务器地址
var token=sessionStorage.getItem("token");

window.post = function(path, param, fnSuccess, async){
    console.log("============",token);
    if (async == undefined) {
        async = true;
    }
    console.log(param);
    $.ajax({
        type:"post",
        url:basePath+path,
        data:"data="+JSON.stringify(param),
        // beforeSend:function(xhr){
        //     xhr.setRequestHeader('token', token);
        // },
         
        dataType:"json",
        success: function(res){
            if (!fnSuccess) return false;
            if (res) {
                fnSuccess(res);
            }
        },
        error: function (res) {
            console.log("%c isError : " + path , res);
        }
    });
}
//普通的ajax post 加上了token的验证，指定传输文件方式为json


// window.formPost = function(path, param, fnSuccess, async){
//     if (async == undefined) {
//         async = true;
//     }
   
//     $.ajax({
//         type:"post",
//         url:basePath+path,
//         data: param,
//         async: false,
//         contentType:false,
//         processData : false, 
//         success: function(res){
//             console.log(fnSuccess);
//             alert("fileup");
//             if (!fnSuccess) return false;
//           // console.log(res);
//             if (res) {
//                 fnSuccess(res);
//             }
//         },
//         error: function (res) {
//             console.log("%c isError : " + path , res.msg);
//         }
//     });
// }

window.formPost = function(path, param, fnSuccess, async){
    if (async == undefined) {
        async = true;
    }

    $.ajax({
        type:"post",
        url:basePath+path,
        data: param,
        async: async,
        // beforeSend:function(xhr){
        //     xhr.setRequestHeader('Authorization', "Bearer "+token);
        // }, 
        contentType:false,
        processData : false, 
        success: function(res){
            alert(0);
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
//普通的ajax formpost 加上了token的验证，取消指定传输文件方式和格式，实现断点文件传输

window.postlogin=function(path,param,fnSuccess,async)
{
    $.ajax({
        type:"post",
        url:basePath+path,
        async:false,
        dataType:'json',
        data:"data="+JSON.stringify(param),
        success: function(res){
            console.log(res);
            if (!fnSuccess) return false;
            
            if (res) {
                fnSuccess(res);
            }
        },
        error: function (res) {
            console.log("%c isError : " + path , res.msg);
        }
    });
}
//普通的ajax post  没有token验证，用于Login验证

window.get = function(path, param, fnSuccess, async){
    console.log("============",token);
    if (async == undefined) {
        async = true;
    }
    console.log(param);
    $.ajax({
        type:"get",
        url:basePath+path,
        data:"data="+JSON.stringify(param),
        beforeSend:function(xhr){
            xhr.setRequestHeader(token,'token');
        },
        dataType:"json",
        success: function(res){
            if (!fnSuccess) return false;
            if (res) {
                fnSuccess(res);
            }
        },
        error: function (res) {
            console.log("%c isError : " + path , res);
        }
    });
}