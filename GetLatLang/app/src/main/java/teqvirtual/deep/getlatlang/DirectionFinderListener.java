package teqvirtual.deep.getlatlang;

import java.util.List;

interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
