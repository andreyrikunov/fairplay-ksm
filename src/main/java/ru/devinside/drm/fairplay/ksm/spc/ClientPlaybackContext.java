package ru.devinside.drm.fairplay.ksm.spc;

import javax.annotation.Nullable;
import java.util.Optional;

public class ClientPlaybackContext {
    private final AssetId assetId;
    private final StreamingIndicator streamingIndicator;
    private final ClientCapabilities clientCapabilities;
    private final String deviceIdHex; // Anonymized unique ID of the playback device

    public ClientPlaybackContext(
            AssetId assetId,
            @Nullable StreamingIndicator streamingIndicator,
            @Nullable ClientCapabilities clientCapabilities,
            String deviceIdHex
    ) {
        this.assetId = assetId;
        this.streamingIndicator = streamingIndicator;
        this.clientCapabilities = clientCapabilities;
        this.deviceIdHex = deviceIdHex;

    }

    public AssetId getAssetId() {
        return assetId;
    }

    public Optional<StreamingIndicator> getStreamingIndicator() {
        return Optional.ofNullable(streamingIndicator);
    }

    public Optional<ClientCapabilities> getClientCapabilities() {
        return Optional.ofNullable(clientCapabilities);
    }

    public String getDeviceIdHex() {
        return deviceIdHex;
    }
}
