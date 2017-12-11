"use strict";
function UserService(httpService) {

    this.createPlayer = function (name) {
        return httpService.post("/user/new", name);
    };
}
