# Overview

This is a simple poc using [vert.x] to create reactive java applications.


## Test

You can see in the log one message created and 3 consumers received this message.

### Producers

`PublisherVerticle - Publishing Message(id=66abe68d-5ac2-4f1d-8daf-639e8c0e3b18) to message-channel`


### Consumers

`SubscriberVerticle[2] - Consuming Message(id=66abe68d-5ac2-4f1d-8daf-639e8c0e3b18) from message-channel`

`SubscriberVerticle[1] - Consuming Message(id=66abe68d-5ac2-4f1d-8daf-639e8c0e3b18) from message-channel`

`SubscriberVerticle[3] - Consuming Message(id=66abe68d-5ac2-4f1d-8daf-639e8c0e3b18) from message-channel`

[vert.x]: https://vertx.io/
