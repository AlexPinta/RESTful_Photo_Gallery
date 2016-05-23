var xhttp = new XMLHttpRequest();
var scanFolder = function() {
    const FILE_EXTENSION = '.png';
    var input = document.getElementById('uploadFolder');
    var onLoad = function(){
        var dataURL = this.result;
        xhttp.open("POST", "/addPicture", true);
        xhttp.setRequestHeader("Content-type", "image/png");
        xhttp.send(dataURL);
    };

    var files = input.files;
    for (var i = 0; i < files.length; i++){
        var reader = new FileReader();
        reader.onload = onLoad;
        var a = URL.get(files[i]);
        if (files[i].webkitRelativePath.slice(-4) === FILE_EXTENSION) {
            reader.readAsArrayBuffer(files[i]);
        }
    }
};