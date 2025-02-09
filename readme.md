# P2P

Who sendSecrets when e.g. #1 is offline

## Protocol

### Join network

Tell a known node that you wish to join the network. They will assign a UUID and return a list of current nodes.

Request

```
BIZ.RTYN.DCP/1 JOIN_NETWORK <hostname> <port> 
```

Response

```
BIZ.RTYN.DCP/1 200 OK
<assigned id>
<node1_id>:<node1_hostname>:<node1_port>
<node2_id>:<node2_hostname>:<node2_port>
...
```

### Notify nodes

Tell other nodes that you've joined

Request

```
BIZ.RTYN.DCP/1 ADD_NODE <id> <hostname> <port>
```

Response

```
BIZ.RTYN.DCP/1 200 OK
```

### Exchange secret

Start exchanging keys for a round. Put nodesList in order by id for round robin propogate. If last node in nodesList, designate random node to calculate message and submit to dead drop / blockchain, or IPFS

Request

```
BIZ.RTYN.DCP/1 EXCHANGE_SECRET <from_id> <value>
```

Response

```
BIZ.RTYN.DCP/1 200 OK
```








TODO

testing async
howto determine who will send message next round?
authentication
how to detect dodgy nodes?
test
dead drop - address is public key, which message is encrypted with
notify protocol to propogate