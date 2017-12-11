"use strict";
function EventService() {
    let listeners = {};
    let nextListener = 0;

    let getListeners = function (type) {
        let map = listeners[type];
        if (!map) {
            map = {};
            listeners[type] = map;
        }
        return map;
    };

    this.subscribe = function (type, callback) {
        let id = nextListener;
        nextListener++;
        getListeners(type)[id] = callback;
        return id;
    };

    this.unsubscribe = function (type, id) {
        delete getListeners(type)[id];
    };

    this.broadcast = function (type, event) {
        let map = getListeners(type);
        for (let key in map) {
            map[key](event);
        }
    };

}
