package spectrumTest;

import spectra.interfaces.Band;

public enum Bands implements Band {
    SIMPLE,
    CUSTOM,
    INACTIVE,
    DERIVED,
    HOOKS,
    INVALID_WARMUP,
    INVALID_COOLDOWN,
    SHARED_HOOKS,
    HOOKED;
}