var $ = {};
$.ajax = function (options) {

    var url = options.url;
    var type = options.type || 'GET';
    var headers = options.headers || {};
    var body = options.data || null;
    var timeout = options.timeout || null;
    var success = options.success || function (err, data) {
            console.log("options.success was missing for this request");
        };
    var contentType = options.contentType || 'application/json';
    var error = options.error || function (err, data) {
            console.log("options.error was missing for this request");
        };

    if (!url) {
        throw 'loadURL requires a url argument';
    }

    var xhr = new XMLHttpRequest();
    xhr.responseType = 'json';
    xhr.timeout = timeout;
    xhr.onreadystatechange = function () {
        try {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    if (xhr.responseType === 'json') {
                        success(null, xhr.response);
                    } else {
                        success(null, JSON.parse(xhr.responseText));
                    }
                } else {
                    success(new Error("Error [" + xhr.status + "] making http request: " + url));
                }
            }
        } catch (err) {
            console.error('Aborting request ' + url + '. Error: ' + err);
            xhr.abort();
            error(new Error("Error making request to: " + url + " error: " + err));
        }
    };

    xhr.open(type, url, true);

    xhr.setRequestHeader("Content-Type", contentType);
    xhr.setRequestHeader("Accept", 'application/json, text/javascript, */*');

    Object.keys(headers).forEach(function (key) {
        xhr.setRequestHeader(key, headers[key]);
    });

    if (!body) {
        xhr.send();
    } else {
        xhr.send(body);
    }

    return xhr;
}


App.onLaunch = function (options) {
    showAlbumOverview();
}

var showAlbumOverview = function () {
    navigationDocument.presentModal(createAlbumOverview());
}

var showAlbum = function (id) {
    console.log("SHOW ALBUM: " + id);
    navigationDocument.presentModal(createAlbumView(id));
}

var createAlbumView = function (id) {
    //TODO
    return createAlbumOverview();
}


var createAlbumLockup = function (data) {
    var oneItem = '<title>' + data.name + '</title>' +
        '<relatedContent>' +
        '<lockup>' +
        '<img src="' + data.coverUrl + '" />' +
        '<title>' + data.name + ' (' + data.imageCount + ')</title>' +
        '</lockup>' +
        '</relatedContent>';
    return oneItem;
}

var createAlbumOverview = function () {
    var view = '<?xml version="1.0" encoding="UTF-8" ?>' +
        '<document>' +
        '<listTemplate>' +
        '<banner>' +
        '<background>' +
        '<img src="http://localhost:8080//thumbs/get/241edbb3-e503-414b-bb10-cade1d1721c7" width="1920" height="360" />' +
        '</background>' +
        '</banner>' +
        '<list>' +
        '<header>' +
        '<title>Photonia</title>' +
        '</header>' +
        '<relatedContent>' +
        '<lockup>' +
        '<img src="http://localhost:8080//thumbs/get/cf40f62b-7432-403a-b28b-0308e37aa446" width="857" height="482" />' +
        '<title>Common Related Title</title>' +
        '<subtitle>Common Related Subtitle</subtitle>' +
        '<description>.....</description>' +
        '</lockup>' +
        '</relatedContent>' +
        '<section id="mainSection">' +
        '<header>' +
        '<title>Album overview</title>' +
        '</header>' +
        '</section>' +
        '</list>' +
        '</listTemplate>' +
        '</document>';

    var parser = new DOMParser();
    var doc = parser.parseFromString(view, "application/xml");
    var mainSection = doc.getElementById("mainSection");
    var onSuccess = function (err, dataList) {
        for (i = 0; i < dataList.length; i++) {
            dataList[i].coverUrl = dataList[i].coverUrl.replace("192.168.2.102", "localhost");
            listItemLockup = doc.createElement("listItemLockup");
            listItemLockup.innerHTML = createAlbumLockup(dataList[i]);
            mainSection.appendChild(listItemLockup);
            var uuid = dataList[i].uuid;
            listItemLockup.addEventListener("select", function () {
                showAlbum(uuid);
            }, false);
        }
    };

    var dataList = $.ajax({
        type: 'GET',
        url: "http://localhost:8080/library/all",
        timeout: 2000,
        success: onSuccess
    }).response;

    return doc;
}



