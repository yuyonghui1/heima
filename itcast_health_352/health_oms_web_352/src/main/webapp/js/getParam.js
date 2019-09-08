//获取指定的URL参数值 http://localhost/pages/setmeal_detail.html?id=3&name=jack
function getUrlParam(paraName) {
    var url = document.location.toString();
    // alert(url);
    var arrObj = url.split("?");
    //arrObj[0]=http://localhost/pages/setmeal_detail.html
    //arrObj[1]=   id=3&name=jack
    if (arrObj.length > 1) {
        var arrPara = arrObj[1].split("&");
        //arrPara[0] =  id=3    ===== arr[0] = id  arr[1] = 3
         //arrPara[1] = name=jack  ===== arr[0] =name  arr[1] === jack
        var arr;
        for (var i = 0; i < arrPara.length; i++) {
            arr = arrPara[i].split("=");
            if (arr != null && arr[0] == paraName) {
                // alert(arr[1]);
                return arr[1];
            }
        }
        return "";
    }
    else {
        return "";
    }
}

