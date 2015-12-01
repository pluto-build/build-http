package build.pluto.buildhttp;

import java.io.File;
import java.io.Serializable;
import java.net.URL;

public class HTTPInput implements Serializable {
    private static final long serialVersionUID = 7462906909865275828L;

    public final String remoteLocation;
    public final File locationOnLocal;
    public final long consistencyCheckInterval;

    public HTTPInput(
            String remoteLocation,
            File locationOnLocal,
            long consistencyCheckInterval) {
        this.remoteLocation = remoteLocation;
        this.locationOnLocal = locationOnLocal;
        this.consistencyCheckInterval = consistencyCheckInterval;
    }
}
