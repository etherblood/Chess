function PollService(httpService, eventService) {

    this.init = function () {
        httpService.post("/api/poll/subscribe").then(function (clientId) {
            let retries = 2;
            let poll = function () {
                httpService.get("/api/poll/" + clientId).then(function (events) {
                    poll();
                    for (let i = 0; i < events.length; i++) {
                        let event = events[i];
                        console.log(event);
                        eventService.broadcast(event.type, event.data);
                    }
                    retries = 2;
                }, function () {
                    if (retries) {
                        setTimeout(poll, 6000);
                        retries--;
                    } else {
                        console.error("too many consecutive polling failures, no further retries");
                    }
                });
            };
            poll();
        });
    };
}
