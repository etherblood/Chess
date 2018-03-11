"use strict";
function HttpService(csrfToken) {
	
    this.makeRequest = function (method, url, data) {
        return new Promise(function (resolve, reject) {
            let req = new XMLHttpRequest();
            req.open(method, url);
            if (csrfToken && method !== "GET") {
                req.setRequestHeader('X-XSRF-TOKEN', csrfToken);
            }

            req.onload = function () {
                if (req.status === 200) {
                    if (req.responseText) {
                        resolve(JSON.parse(req.responseText));
                    } else {
                        resolve(req.responseText);
                    }
                } else {
                    reject(Error(req.statusText));
                }
            };
            req.onerror = function () {
                reject(Error("Something went wrong during " + method + " to " + url));
            };
            if (data) {
                req.setRequestHeader('Content-type', 'application/json');
                req.send(JSON.stringify(data));
            } else {
                req.send();
            }
        });
    };
    this.get = function (url) {
        return this.makeRequest("GET", url);
    };
    this.post = function (url, data) {
        return this.makeRequest("POST", url, data);
    };
}
