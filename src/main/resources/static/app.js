var xhttp = new XMLHttpRequest();
var scanFolder = function() {
    const FILE_EXTENSION = '.png';
    var input = document.getElementById('uploadFolder');
    var onLoad = function(file){
        var dataURL = this.result;
        xhttp.open("POST", "/addPicture", true);
        xhttp.setRequestHeader("Content-type", "image/png");
        xhttp.setAttribute('path', file.webkitRelativePath);
        xhttp.send(dataURL);
    };

    var files = input.files;
    for (var i = 0; i < files.length; i++){
        var reader = new FileReader();
        var file = files[i];
        reader.onload = onLoad(file);
        var a = URL.get(file);
        if (file.webkitRelativePath.slice(-4) === FILE_EXTENSION) {
            reader.readAsArrayBuffer(file);
        }
    }
};