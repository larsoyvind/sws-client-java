SendRegning Web Services
========================

* [Implementation guide](docs/guide.md)
* [Examples](docs/integration.md)

Common repository hosting different clients for SendRegning.no Web Services.

SendRegning.no is a Norwegian SaaS for issuing invoices and keep track of payments etc.

### Available clients

* [Java 1.5+](docs/java.md)
* [PHP 5+](docs/php.md)
* [Perl](docs/perl.md)
* [Python](docs/python.md)
* [Ruby](https://github.com/elektronaut/sendregning) - Thanks to [Inge Jørgensen](https://github.com/elektronaut)
* [C#](docs/csharp.md)

### Java

You can have a look at our Java client demo to get an idea of what [this API](src/main/java/no/sws/client/SwsClient.java) is capable to do.

```
mvn clean install
```

### PHP

```
cd /src/main/php
php SendRegningClient.php email password
```

### Perl

This client is implemented against our old form based authentication SWS. Form based authentication will be ended in late 2011

```
cd /src/main/perl
```
