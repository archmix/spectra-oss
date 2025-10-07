package spectrumTest;

import spectra.interfaces.Band;

public enum Bands implements Band {
    SIMPLE,
    CUSTOM,
    INACTIVE,
    DERIVED,
    HOOKS,
    HOOKED;
}