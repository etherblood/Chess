function PollService(httpService, eventService) {

    this.run = function () {
//        httpService.post("/poll/subscribe").then(function (clientId) {
            let retries = 10;
            let poll = function () {
                httpService.get("/poll/").then(function (events) {
                    poll();
                    for (let i = 0; i < events.length; i++) {
                        let event = events[i];
                        eventService.broadcast(event.type, event.data);
                    }
                    retries = 10;
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
//        });
    };
}
