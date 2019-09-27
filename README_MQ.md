##### IBM MQ Lingo:
- Durable Topic Subscriber: Message consumer that receives all messages sent to a destination, including those sent while the consumer is inactive.
- > The scope of a durable subscription is a queue manager. 
- > The name that is used to identify a durable subscription must be unique only within the client identifier
- Nondurable Topic Subscriber: Receives only those messages that are published while the subscriber is active.
- Client Identifier:  Durable topic subscriber must have an associated client identifier. The client identifier associated with a session is the same as the client identifier for the connection that is used to create the session.
- Message selectors:  An application can specify that only those messages that satisfy certain criteria are returned by successive receive() calls
- Asynchronous delivery of messages: An application can receive messages asynchronously by registering a message listener with a message consumer.

