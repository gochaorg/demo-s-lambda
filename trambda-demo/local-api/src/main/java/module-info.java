open module org.example.localapi {
    requires java.base;

    exports org.example.api;
    exports org.example.api.spi;

    uses org.example.api.spi.OSInfoProvider;
}