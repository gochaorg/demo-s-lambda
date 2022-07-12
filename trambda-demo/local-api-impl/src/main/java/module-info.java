open module org.example.api.impl {
    requires org.example.localapi;
    requires com.github.oshi;

    exports org.example.api.oshi;

    provides org.example.api.spi.OSInfoProvider with org.example.api.oshi.OSHIProvider;
}