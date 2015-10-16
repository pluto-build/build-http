package build.pluto.buildhttp;

import build.pluto.dependency.RemoteRequirement;
import build.pluto.builder.BuildUnitProvider;

import java.io.File;
import java.io.IOException;

public class HTTPRequirement extends RemoteRequirement {
    public HTTPRequirement(File persistentPath, long consistencyCheckInterval) {
        super(persistentPath, consistencyCheckInterval);
    }

    @Override
    public boolean isConsistentWithRemote() {
        return false;
    }

    @Override
    public boolean tryMakeConsistent(
            BuildUnitProvider manager) throws IOException {
        return this.isConsistent();
    }
}
