package build.pluto.buildhttp;

import java.io.File;
import java.io.Serializable;
import java.net.URL;

public class HTTPInput implements Serializable {
    public final String remoteLocation;
    public final File locationOnLocal;
    public final String fileName;
    public final long consistencyCheckInterval;

    public HTTPInput(
            String remoteLocation,
            File locationOnLocal,
            String fileName,
            long consistencyCheckInterval) {
        this.remoteLocation = remoteLocation;
        this.locationOnLocal = locationOnLocal;
        this.fileName = fileName;
        this.consistencyCheckInterval = consistencyCheckInterval;
    }

    // public String getPathToLocalFile() {
    //     return locationOnLocal.getAbsolutePath() + "/" + fileName;
    // }
}
